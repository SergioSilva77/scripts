Date d = new Date("Tue, 11 Oct 2022 15:59:46 GMT");
SimpleDateFormat sdf1= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));
String f = sdf1.format(d);
System.out.println(f);