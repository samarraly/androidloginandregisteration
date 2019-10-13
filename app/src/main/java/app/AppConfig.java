package app;
//In this class we declare the login and registration urls. While testing you need to replace the ip address with your localhost pc ip.
public class AppConfig {
    // Server user login url
    public static String URL_LOGIN = "http://192.168.1.5/android_login_api/login.php";

    // Server user register url
    public static String URL_REGISTER = "http://192.168.1.5/android_login_api/register.php";

    public static String URL_getdata= "http://192.168.1.5/android_login_api/include/getdata.php";

}