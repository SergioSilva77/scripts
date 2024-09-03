Start-Process -FilePath "C:\Program Files\Google\Chrome\Application\chrome.exe" `
              -ArgumentList "--headless",
                            "--print-to-pdf=""C:\Users\Quality\Desktop\test.pdf""",
                            "--no-margins","--enable-logging","https://google.com"