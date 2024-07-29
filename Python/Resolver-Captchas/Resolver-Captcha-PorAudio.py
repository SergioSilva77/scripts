# recaptcha libraries
import speech_recognition as sr
# import pydub
import pydub
import urllib, urllib.request, random ,os, sys, time
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.chrome.service import Service

#---------------------< CONFIGURACOES ERBDRIVER >------------------------#
def setup_browser(browser_background = False):
    options = webdriver.ChromeOptions()
    servico = Service(ChromeDriverManager().install())
    browser = webdriver.Chrome(service=servico,options=options)
    #options.add_argument("start-minimized")
    #options.add_experimental_option("excludeSwitches", ["enable-automation"])
    #options.add_experimental_option('useAutomationExtension', False)
    if(browser_background==True):
        options.add_argument('--headless')
        options.add_argument('--disable-gpu')
    return browser

def delay():
    time.sleep(random.randint(2,3))
def clear_audio():
    try:
        os.remove('sample.mp3')
        os.remove('sample.wav')
    except: print('sem Arquivos')

clear_audio()
site = ["https://www.google.com/recaptcha/api2/demo"]
driver = setup_browser()
# get link
driver.get(site[0])



############## PARTE PARA DESBLOQUEIO DO RECAPTCHA ###########################
# Switch  to recaptcha frame
frames=driver.find_elements(By.TAG_NAME,"iframe")
driver.switch_to.frame(frames[0]);
delay()

# Switch on checkbox to activate recaptcha
driver.find_element(By.CLASS_NAME, "recaptcha-checkbox-border").click()

#Swuitch to recaptcha audio control frame
driver.switch_to.default_content()
frames=driver.find_element(By.XPATH,'/html/body/div[2]/div[4]').find_elements(By.TAG_NAME,"iframe")
driver.switch_to.frame(frames[0])
delay()

#click on audio challenge
driver.find_element(By.ID, "recaptcha-audio-button").click()

#Switch to recaptcha aidop challenge frame
driver.switch_to.default_content()
frames=driver.find_elements(By.TAG_NAME,"iframe")
driver.switch_to.frame(frames[-1])
delay()


#Information error
if not driver.find_element(By.XPATH, "/html/body/div/div/div[3]/div/button"):
    erro = driver.find_element(By.CLASS_NAME, "rc-doscaptcha-header-text").text
    print('[INFO] Error: ',erro)
    print('Seu computador ou sua rede podem estar enviando consultas automáticas. ')
    driver.close()
    sys.exit('saindo...')
else:

    #Click on the play buttom
    driver.find_element(By.XPATH, "/html/body/div/div/div[3]/div/button").click()

    #get the mp3 audio file
    src = driver.find_element(By.ID,"audio-source").get_attribute("src")
    print("[INFO] Audio src: %s"%src)

    path_to_mp3 = os.path.normpath(os.path.join(os.getcwd(), "sample.mp3"))
    path_to_wav = os.path.normpath(os.path.join(os.getcwd(), "sample.wav"))

    #download the mp3 audio file from the souce
    urllib.request.urlretrieve(src, path_to_mp3)

    sound = pydub.AudioSegment.from_mp3(path_to_mp3)
    sound.export(path_to_wav, format="wav")
    sample_audio = sr.WavFile(path_to_wav)
    print('Passou 2')

    delay()
    r = sr.Recognizer()
    with sample_audio as source:
        audio = r.record(source)

    #translat audio to text with google voice recognition
    key = r.recognize_google(audio)
    print("[INFO] Recaptcha passcode: %s" %key)
    print('Passou 3')

    # key in results and submit
    delay()
    driver.find_element(By.ID,"audio-response").send_keys(key.lower())
    driver.find_element(By.ID,"audio-response").send_keys(Keys.ENTER)
    time.sleep(5)
    driver.switch_to.default_content()
    time.sleep(5)
    driver.find_element(By.ID,"recaptcha-demo-submit").click()    