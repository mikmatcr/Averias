package com.proyecto.michaelmatamoros.averias.utils;

import android.util.Log;


    // Basic logger bound to a flag in Constants.java

    public class aLog {
        public static void w (String TAG, String msg){
            if(Constants.LOGGING) {
                if (TAG != null && msg != null)
                    Log.w(TAG, msg);
            }
        }

    }
