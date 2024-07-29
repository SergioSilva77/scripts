public static string ObterTokenRecaptchaV21()
{
    TwoCaptcha.TwoCaptcha solver = new TwoCaptcha.TwoCaptcha("numero_token"); 
    ReCaptcha captcha = new ReCaptcha();
    captcha.SetSiteKey("6LdCwT8iAAAAAHyQr-PCciOhzh7GKlyXvxzDOwi0");
    captcha.SetUrl("https://consultadanfe.com/CDanfe/xml/");
    try
    {
        solver.Solve(captcha).Wait();
        File.WriteAllText("token.txt", captcha.Code, encoding: Encoding.UTF8);
        return captcha.Code;
    }
    catch (AggregateException e)
    {
        Msg.WriteLine("Sem saldo API recaptcha");
    }
    return null;
}