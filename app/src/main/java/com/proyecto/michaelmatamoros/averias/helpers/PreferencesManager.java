package com.proyecto.michaelmatamoros.averias.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
    private static SharedPreferences mPreferences;
    private static final String PREF_FILE = "app.preferences";

    private static final String ARG_USERNAME = "arg.username";
    private static final String ARG_PASSWORD = "arg.password";
    private static final String ARG_REMEMBER = "arg.remember";
    private static final String ARG_CORREO = "arg.correo";
    private static final String ARG_TELEFONO = "arg.telefono";
    private static final String ARG_NOMBRE = "arg.nombre";
    private static final String ARG_CEDULA = "arg.cedula";

    public static void savePreferencesLogin(Context ctx, String username,
                                       String password, boolean remember){
        if(mPreferences == null){
            mPreferences = ctx.getSharedPreferences(PREF_FILE, ctx.MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = mPreferences.edit();

        editor.putString(ARG_USERNAME, username);
        editor.putString(ARG_PASSWORD, password);
        editor.putBoolean(ARG_REMEMBER, remember);


        editor.commit();
    }


    public static void savePreferences(Context ctx,String username,
                                       String correo, String telefono,
                                       String nombre, String cedula){
        if(mPreferences == null){
            mPreferences = ctx.getSharedPreferences(PREF_FILE, ctx.MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(ARG_USERNAME, username);
        editor.putString(ARG_CORREO, correo);
        editor.putString(ARG_TELEFONO, telefono);
        editor.putString(ARG_NOMBRE, nombre);
        editor.putString(ARG_CEDULA, cedula);

        editor.commit();
    }

    public static String getUsernameFromPreferences(Context ctx){
        if(mPreferences == null){
            mPreferences = ctx.getSharedPreferences(PREF_FILE, ctx.MODE_PRIVATE);
        }

        return mPreferences.getString(ARG_USERNAME, "");
    }

    public static String getPasswordFromPreferences(Context ctx){
        if(mPreferences == null){
            mPreferences = ctx.getSharedPreferences(PREF_FILE, ctx.MODE_PRIVATE);
        }

        return mPreferences.getString(ARG_PASSWORD, "");
    }

    public static boolean getRememberFromPreferences(Context ctx){
        if(mPreferences == null){
            mPreferences = ctx.getSharedPreferences(PREF_FILE, ctx.MODE_PRIVATE);
        }

        return mPreferences.getBoolean(ARG_REMEMBER, false);
    }

    public static String getCorreoFromPreferences(Context ctx){
        if(mPreferences == null){
            mPreferences = ctx.getSharedPreferences(PREF_FILE, ctx.MODE_PRIVATE);
        }

        return mPreferences.getString(ARG_CORREO, "");
    }

    public static String getTelefonoFromPreferences(Context ctx){
        if(mPreferences == null){
            mPreferences = ctx.getSharedPreferences(PREF_FILE, ctx.MODE_PRIVATE);
        }

        return mPreferences.getString(ARG_TELEFONO, "");
    }

    public static String getNombreFromPreferences(Context ctx){
        if(mPreferences == null){
            mPreferences = ctx.getSharedPreferences(PREF_FILE, ctx.MODE_PRIVATE);
        }

        return mPreferences.getString(ARG_NOMBRE, "");
    }

    public static String getCedulaFromPreferences(Context ctx){
        if(mPreferences == null){
            mPreferences = ctx.getSharedPreferences(PREF_FILE, ctx.MODE_PRIVATE);
        }

        return mPreferences.getString(ARG_CEDULA, "");
    }

}
