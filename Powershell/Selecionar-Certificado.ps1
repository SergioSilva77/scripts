chcp 1252

Add-Type -Path "C:\flaUi\Libs\FlaUI.UIA3.dll"
Add-Type -Path "C:\flaUi\Libs\FlaUI.Core.dll"
Add-Type -Path "C:\flaUi\Libs\BibliotecaPowershellAutomation.dll"
$instance = New-Object BibliotecaPowershellAutomation.AutomationAcoes

#---------------------------------------<Filtrar nome da janela>
$instance.SetProcessByWindowTitle("Google - Google Chrome", $true, $true, 5000, 3)

#----------------------------------------<Código C#>

$cSource = @'
using System;
using System.Drawing;
using System.Runtime.InteropServices;
using System.Windows.Forms;

public class Clicker
{
    
    [StructLayout(LayoutKind.Sequential)]
    struct INPUT
    { 
        public int        type; // 0 = INPUT_MOUSE
                                // 1 = INPUT_KEYBOARD
                                // 2 = INPUT_HARDWARE
        public MOUSEINPUT mi;
    }

    
    [StructLayout(LayoutKind.Sequential)]
    struct MOUSEINPUT
    {
        public int    dx;
        public int    dy;
        public int    mouseData;
        public int    dwFlags;
        public int    time;
        public IntPtr dwExtraInfo;
    }

    // This covers most use cases although complex mice may have additional buttons.
    // There are additional constants you can use for those cases, see the MSDN page.
    const int MOUSEEVENTF_MOVE       = 0x0001;
    const int MOUSEEVENTF_LEFTDOWN   = 0x0002;
    const int MOUSEEVENTF_LEFTUP     = 0x0004;
    const int MOUSEEVENTF_RIGHTDOWN  = 0x0008;
    const int MOUSEEVENTF_RIGHTUP    = 0x0010;
    const int MOUSEEVENTF_MIDDLEDOWN = 0x0020;
    const int MOUSEEVENTF_MIDDLEUP   = 0x0040;
    const int MOUSEEVENTF_WHEEL      = 0x0080;
    const int MOUSEEVENTF_XDOWN      = 0x0100;
    const int MOUSEEVENTF_XUP        = 0x0200;
    const int MOUSEEVENTF_ABSOLUTE   = 0x8000;

    const int screen_length = 0x10000;

 
    [System.Runtime.InteropServices.DllImport("user32.dll")]
    extern static uint SendInput(uint nInputs, INPUT[] pInputs, int cbSize);

    public static void LeftClickAtPoint(int x, int y)
    {
        // Move the mouse
        INPUT[] input = new INPUT[3];

        input[0].mi.dx = x * (65535 / System.Windows.Forms.Screen.PrimaryScreen.Bounds.Width);
        input[0].mi.dy = y * (65535 / System.Windows.Forms.Screen.PrimaryScreen.Bounds.Height);
        input[0].mi.dwFlags = MOUSEEVENTF_MOVE | MOUSEEVENTF_ABSOLUTE;

        // Left mouse button down
        input[1].mi.dwFlags = MOUSEEVENTF_LEFTDOWN;

        // Left mouse button up
        input[2].mi.dwFlags = MOUSEEVENTF_LEFTUP;

        SendInput(3, input, Marshal.SizeOf(input[0]));
    }
}
'@

write-host "$elements" -ForegroundColor Red

write-host "$x - $y $($elements.Count)" -ForegroundColor Green



$counter = 0
while ($counter -lt 8) {
    # Filtrar elementos
    $instance.FiltrarDescendentesJanela(@('ControlType', 'TitleBar'), 1000, 2)

    $elements = $instance.FiltrarDescendentesJanela(@('ControlType', 'DataItem'), 1000, 2)
    $elements = $elements.Where({$_.Name -ilike '*?{name}*'})

    write-host "$elements" -ForegroundColor Green

    # Encontrar a posição do elemento na tela
    $x = $elements[0].BoundingRectangle.X + $elements[0].BoundingRectangle.Width
    $y = $elements[0].BoundingRectangle.Y + $elements[0].BoundingRectangle.Height
    Add-Type -TypeDefinition $cSource -ReferencedAssemblies System.Windows.Forms,System.Drawing

    write-host "$x - $y $($elements.Count)" -ForegroundColor Red

    Write-Host "Iniciando..."
    Start-Sleep -Seconds 2  # Atraso de 5 segundos
    Write-Host "Continuando após o atraso."
        

    if ($elements.Count -gt 0) {
        $elements[0].ItemStatus
        Write-Host 'CLIQUE EFETUADO'
        [Clicker]::LeftClickAtPoint($x,$y);[Clicker]::LeftClickAtPoint($x,$y)
        $elements = $instance.FiltrarDescendentesJanela(@('ControlType', 'DataItem'), 1000, 1)
        $elements = $elements.Where({$_.Name -ilike '*?{name}*'})
        if ($elements -le 0){
            break
        }
    
    }

    [System.Windows.Forms.SendKeys]::SendWait("{DOWN}")
    $counter++
}