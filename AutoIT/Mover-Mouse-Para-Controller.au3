#Region ;**** Directives created by AutoIt3Wrapper_GUI ****
#AutoIt3Wrapper_Compression=4
#AutoIt3Wrapper_UseUpx=y
#EndRegion ;**** Directives created by AutoIt3Wrapper_GUI ****
#include<Timers.au3>

$Width = @DesktopWidth
$Height = @DesktopHeight

while True

   if _Timer_GetIdleTime()>= 30*1000 then
	  MouseMove(($Width/2)+5, ($Height/2)+5,10)
	  MouseMove($Width/2, $Height/2,10)
   endif

   Sleep (5000)
WEnd