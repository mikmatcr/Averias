package com.proyecto.michaelmatamoros.averias.Servicios;

import com.proyecto.michaelmatamoros.averias.modelo.Averia;
import com.proyecto.michaelmatamoros.averias.modelo.CrearAveria;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

    /*
   Declaracion de la interfaz del servicio. Esta va a ser una representacion
   1 a 1 de los metodos y recursos que expone el servicio y que vamos a utilizar
   en la aplicación.
 */
    public interface ServicioPosts {
        //Se declara para cada llamada, el metodo (GET/POST/etc...) y el endpoint
        //Cada resultado de llamada debe contenerse dentro de un Call<>
        //En este caso el método devuelve una lista JSON de Objetos representados
        //por el objeto entidad Post, por lo que se define como List<Post>
        @GET("posts")
        Call<ArrayList<Averia>> obtenerTodosLosPosts();

        @Headers("x-api-key: rabArf10E86thWRQ5u4MH3pFXVpiQiXv8jg1c4hO")
        @GET("averias")
        Call<ArrayList<Averia>> obtenerAveria();

        @GET("photos")
        Call<ArrayList<Averia>> obtenerTodasLasFotos();



        //Para parametros en el path, se indican entre llaves con un nombre identificador
        //y posteriormente se indica la variable de reemplazo usando la anotacion de @Path
        @GET("posts/{id}")
        Call<Averia> obtenerDetallesDePost(@Path("id") String id);

    @Headers("x-api-key: rabArf10E86thWRQ5u4MH3pFXVpiQiXv8jg1c4hO")
    @GET("averias/{id}")
    Call<Averia> obtenerDetallesDeAverias(@Path("id") String id);

        //Para llamadas que requieran del envio de un body, este se especifica
        //usando la anotación de @Body. Si es un objeto, va a ser serializado
        //automaticamente usando el ConverterFactory con el que se inicialize el
        //objeto de RetroFit
        @Headers("x-api-key: rabArf10E86thWRQ5u4MH3pFXVpiQiXv8jg1c4hO")
        @POST("averias/")
        Call<Averia> crearNuevaAveria(@Body CrearAveria nuevo);

        //EditarAverias
        @Headers("x-api-key: rabArf10E86thWRQ5u4MH3pFXVpiQiXv8jg1c4hO")
        @POST("averias/{id}")
        Call<Averia> actualizarAveria(@Path("id") String id, @Body Averia editar);

        //Eliminar Avería
        @Headers("x-api-key: rabArf10E86thWRQ5u4MH3pFXVpiQiXv8jg1c4hO")
        @DELETE("averias/{id}")
        Call<Averia> eliminarAverias(@Path("id") String id);
}
