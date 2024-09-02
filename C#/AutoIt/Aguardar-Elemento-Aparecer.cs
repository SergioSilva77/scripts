public bool AguardarElementoAutoIt(string windowTitle, string control, int attempts, int delay)
{
    while (attempts-- > 0)
    {
        if (autoIt.ControlCommand(windowTitle, "", control, "IsEnabled", "") == "1")
        {
            return true;
        }
        Thread.Sleep(delay); 
    }
    return false; 