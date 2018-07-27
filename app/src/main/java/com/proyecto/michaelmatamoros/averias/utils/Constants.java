package com.proyecto.michaelmatamoros.averias.utils;

public class Constants {
    /*
    Logging flag
     */
    public static final boolean LOGGING = false;

    /*
      Your imgur client id. You need this to upload to imgur.

      More here: https://api.imgur.com/
     */
    public static final String MY_IMGUR_CLIENT_ID = "db31aa16994944c";
    public static final String MY_IMGUR_CLIENT_SECRET = "9933161a9ae468173a0f6fd8b0fe546eac8692e5";

    /*
      Redirect URL for android.
     */
    public static final String MY_IMGUR_REDIRECT_URL = "Averias//callback";

    /*
      Client Auth
     */
    public static String getClientAuth() {
        return "Client-ID " + MY_IMGUR_CLIENT_ID;
    }
}
