var browserFetcher = new BrowserFetcher();
await browserFetcher.DownloadAsync();

// Inicia o navegador
var browser = await Puppeteer.LaunchAsync(new LaunchOptions
{
    DefaultViewport = null,
    Headless = false, // Defina como true se quiser rodar em modo headless (sem interface gráfica)
});

// Abre uma nova página
var page = await browser.NewPageAsync();

           

// Navega para uma URL específica
await page.GoToAsync(url);

Console.WriteLine("Navegador aberto e página carregada.");

// Mantenha o navegador aberto para visualização
Console.WriteLine("Pressione qualquer tecla para fechar o navegador...");

// Fecha o navegador
await browser.CloseAsync();