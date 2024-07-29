using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using WindowsInput;
using WindowsInput.Native;

namespace LibClicarImagem.Src.ClicarPorImagem
{
    public class DigitarTexto
    {
        private static Dictionary<string, VirtualKeyCode> keyMap = new Dictionary<string, VirtualKeyCode>
            {
                { "TAB", VirtualKeyCode.TAB },
                { "ENTER", VirtualKeyCode.RETURN },
                { "SPACE", VirtualKeyCode.SPACE },
                { "CTRL", VirtualKeyCode.CONTROL },
                { "SHIFT", VirtualKeyCode.SHIFT },
                { "ALT", VirtualKeyCode.MENU },
                // Adicione mais comandos conforme necessário
            };
        public static void Escrever(string text, bool processAtalho = false)
        {
            var sim = new InputSimulator();
            if (processAtalho)
            {
                ProcessAtalho(sim, text);
            }
            else
            {
                sim.Keyboard.TextEntry(text);
            }
        }

        private static void ProcessAtalho(InputSimulator simulator, string input)
        {
            // Expressão regular para encontrar comandos entre chaves
            var regex = new Regex(@"\{(.*?)\}");

            // Posiciona o índice inicial
            int currentIndex = 0;

            // Percorre todos os comandos encontrados
            foreach (Match match in regex.Matches(input))
            {
                // Digita o texto antes do comando
                if (match.Index > currentIndex)
                {
                    simulator.Keyboard.TextEntry(input.Substring(currentIndex, match.Index - currentIndex));
                }

                // Executa o comando
                ExecuteCommand(simulator, match.Groups[1].Value);

                // Atualiza o índice atual
                currentIndex = match.Index + match.Length;
            }

            // Digita o texto restante após o último comando
            if (currentIndex < input.Length)
            {
                simulator.Keyboard.TextEntry(input.Substring(currentIndex));
            }
        }

        static void ExecuteCommand(InputSimulator simulator, string command)
        {
            var keys = command.Split('+');
            foreach (var key in keys)
            {
                if (keyMap.TryGetValue(key.Trim().ToUpper(), out VirtualKeyCode keyCode))
                {
                    simulator.Keyboard.KeyDown(keyCode);
                }
                else
                {
                    
                    Console.WriteLine($"Comando não reconhecido: {key}");
                }
            }

            foreach (var key in keys)
            {
                if (keyMap.TryGetValue(key.Trim().ToUpper(), out VirtualKeyCode keyCode) || Enum.TryParse(key.Trim(), true, out keyCode))
                {
                    simulator.Keyboard.KeyUp(keyCode);
                }
            }
        }
    }
}
