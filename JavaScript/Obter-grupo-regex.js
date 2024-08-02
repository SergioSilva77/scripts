var str = "captcha=1629487964&text=291489&is_correct=1&status=0";
var re = /text=(.*?)&/g
match = re.exec(str);
var grupo = match[1]
grupo