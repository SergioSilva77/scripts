using Irony.Parsing;
using Newtonsoft.Json;
using RoboDistribuidor.Src;
using System.Text;
using System.Threading;
using System.Xml.Linq;
using System.Xml;
using System.Collections.Concurrent;

namespace ConverterParaPDF
{
    internal class Program
    {
        private static readonly HttpClient client = new HttpClient();
        private static readonly SemaphoreSlim semaphore = new SemaphoreSlim(3);
        private static ConcurrentBag<string> statusInfo = new ConcurrentBag<string>();
        private static ConcurrentBag<string> caminhos = new ConcurrentBag<string>();

#pragma warning disable
        static async Task Main(string[] args)
        {
            var tasks = new List<Task>();
            string arquivoInfo = "info.txt";
            string diretorioXmls = @"C:\Users\sserg\OneDrive\Documentos\Dev\E2K\RPA XMLs Processados\XMLs";
            string caminhoPlanilha = @"C:\Users\sserg\OneDrive\Documentos\Dev\E2K\CHAVES PARA GERAÇÃO DE PDF INPASA - 3°LOTE.xlsx";
            string caminhoPDF = @"C:\Users\sserg\OneDrive\Documentos\Dev\E2K\PDFs_3";

            Dictionary<string, string> dicDirXml = new Dictionary<string, string>();
            DirectoryInfo di = new DirectoryInfo(diretorioXmls);
            foreach (var file in di.GetFiles("*.xml", SearchOption.AllDirectories))
            {
                string nfile = Path.GetFileNameWithoutExtension(file.Name);
                if (!dicDirXml.ContainsKey(nfile))
                {
                    dicDirXml.Add(nfile, file.FullName);
                }
            }
            List<string> naoEncontrados = new List<string>();
            List<ArgsDados> chaves = UtilPlanilha.ObterChaves(caminhoPlanilha, diretorioXmls, caminhoPDF);
            List<string> convertidos = new List<string>();
            int duplicados = 0;
            foreach (var item in chaves)
            {
                string caminhoXml = null;
                if (!dicDirXml.ContainsKey(item.Chave))
                {
                    Console.WriteLine($"O XML: {item.Chave} não existe :(", ConsoleColor.Yellow);
                    naoEncontrados.Add(item.Chave);
                    statusInfo.Add($"A chave: {item.Chave} não existe");
                    continue;
                }

                caminhoXml = dicDirXml[item.Chave];

                if (!Directory.Exists(item.DiretorioPDF))
                {
                    Directory.CreateDirectory(item.DiretorioPDF);
                }

                // Ler xml
                string xmlString = File.ReadAllText(caminhoXml, encoding: Encoding.UTF8);
                XDocument xmlDoc = XDocument.Parse(xmlString);
                var namespaceManager = new XmlNamespaceManager(new NameTable());
                namespaceManager.AddNamespace("nfe", "http://www.portalfiscal.inf.br/nfe");
                var dicIde = UtilPlanilha.GetMultipleXmlElementValues(namespaceManager, xmlDoc, "NFe", "ide", new[] { "nNF", "serie" });
                if (dicIde.Count <= 0)
                {
                    continue;
                }

                string xmlKey = item.Chave;
                string caminhoPdf = Path.Combine(item.DiretorioPDF, $"{dicIde[0]["nNF"].PadLeft(9, '0')}-{dicIde[0]["serie"].PadLeft(3, '0')}.pdf");
                caminhos.Add($"{caminhoPdf}${{split}}{item.Chave}");
                
                if (File.Exists(caminhoPdf))
                {
                    duplicados++;
                    convertidos.Add(caminhoPdf);
                    statusInfo.Add($"{caminhoPdf} já existe");
                    continue;
                }
                xmlString = xmlString.Replace("\n", "");
                xmlString = xmlString.Replace("\r", "");
                xmlString = xmlString.Replace("\t", "");

                tasks.Add(ProcessRequest(xmlKey, caminhoPdf, xmlString));
            }
            await Task.WhenAll(tasks);

            var duplicidades = caminhos.GroupBy(x => x)
                .Select(x => new { Caminho = x.Key.Split("${split}")[0], Chave = x.Key.Split("${split}")[1], Contagem = x.Count() })
                .Where(x => x.Contagem > 1)
                .OrderByDescending(x => x.Contagem)
                .ToList();

            var resultado = string.Join(Environment.NewLine, duplicidades.Select(x =>
                            $"{x.Caminho}\t{Path.GetFileNameWithoutExtension(x.Caminho)}\t{x.Chave}\t{x.Contagem}"));
            File.WriteAllText("resumo.txt", resultado, encoding: Encoding.UTF8);
            File.WriteAllLines("baixados.txt", dicDirXml.Values.Select(x=>Path.GetFileNameWithoutExtension(x)), encoding: Encoding.UTF8);
            File.WriteAllLines("convertidos.txt", convertidos, encoding: Encoding.UTF8);
            File.WriteAllLines("nao_convertidos.txt", naoEncontrados, encoding: Encoding.UTF8);

            File.WriteAllLines(arquivoInfo, statusInfo, encoding: Encoding.UTF8);
        }



        static async Task ProcessRequest(string xmlKey, string caminhoPdf, string contentXML)
        {
            await semaphore.WaitAsync();
            try
            {
                await SendRequest(xmlKey, caminhoPdf, contentXML);
            }
            catch (Exception ex)
            {
                statusInfo.Add($"Aconteceu um erro ao converter a chave: {xmlKey} ao chamar o PHP: {ex.Message}");
            }
            finally
            {
                semaphore.Release();
            }
        }

        static async Task SendRequest(string xmlKey, string caminhoPdf, string contentXML)
        {
            var requestData = new
            {
                xmlKey,
                caminhoPdf,
                xmlContent = contentXML
            };

            var requestContent = new StringContent(
                JsonConvert.SerializeObject(requestData),
                Encoding.UTF8,
                "application/json"
            );

            var response = await client.PostAsync("http://localhost/index.php", requestContent);
            response.EnsureSuccessStatusCode();

            var pdfContent = await response.Content.ReadAsByteArrayAsync();

            File.WriteAllBytes(caminhoPdf, pdfContent);
            Console.WriteLine($"PDF saved to {caminhoPdf}");
        }
    }
}
