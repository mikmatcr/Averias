package com.proyecto.michaelmatamoros.averias.Servicios;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


    //Esta clase de ayuda mantiene una única instancia del servicio
//para toda la aplicación por medio del patrón Singleton
    public class GestorServicio {

        //Variable singleton privada, solo accesible por medio del
        //método obtenerServicio()
        private static ServicioPosts singleton;

        //Metodo por medio se accesa la instancia singleton
        public static ServicioPosts obtenerServicio(){

            //Si la instancia nunca ha sido inicializada, se crea:
            //Notese que durante el ciclo de vida de la aplicacion, este
            //bloque dentro del if solo va a ser llamado una vez
            if(singleton == null) {
                //Construcción del servicio usando patron Builder
                Retrofit retrofit = new Retrofit.Builder()
                        //Se especifica el URL base
                        //.baseUrl("https://jsonplaceholder.typicode.com/")
                        .baseUrl("https://fn3arhnwsg.execute-api.us-west-2.amazonaws.com/produccion/")
                        //Se especifica el serializados a usarse
                        //(en este caso gson para json)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                //Se crea la instancia de la interfaz ServicioPosts
                singleton = retrofit.create(ServicioPosts.class);
            }

            //Se retorna la instancia
            return singleton;
        }
}
