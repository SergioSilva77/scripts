public void obterInstanciaDoBrowser(String seletorXpath) throws InterruptedException, Exception{
 	String instance=getVariable(EnvironmentVariable.WF_INSTANCE_ID.name());
    GlobalStorage gs = GlobalStorage.getInstance();
	ChromeDriver driver = null;
    if (instance != null){
        driver = (ChromeDriver)gs.get("BROWSER1_"+instance+"_driver");
    } else {
        driver = (ChromeDriver)gs.get("BROWSER1_driver");
	}

	if (driver == null){
		return;
	}

	WebElement element = driver.findElement(By.xpath(seletorXpath));

	Actions action = new Actions(driver);

        Point elementPosition = element.getLocation();

        Point windowPosition = driver.manage().window().getPosition();

        int x = windowPosition.getX() + elementPosition.getX();
        int y = windowPosition.getY() + elementPosition.getY();

        Robot robot = new Robot();

        robot.mouseMove(x + 50, y + 100);
}