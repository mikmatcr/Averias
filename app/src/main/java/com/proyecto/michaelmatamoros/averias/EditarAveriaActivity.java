package com.proyecto.michaelmatamoros.averias;


import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.proyecto.michaelmatamoros.averias.Servicios.GestorServicio;
import com.proyecto.michaelmatamoros.averias.Servicios.ServicioPosts;
import com.proyecto.michaelmatamoros.averias.helpers.PreferencesManager;
import com.proyecto.michaelmatamoros.averias.modelo.Averia;
import com.proyecto.michaelmatamoros.averias.modelo.Ubicacion;
import com.proyecto.michaelmatamoros.averias.modelo.Usuario;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarAveriaActivity extends AppCompatActivity {

    @BindView(R.id.nombre)
    EditText nombre;

    @BindView(R.id.tipo)
    EditText tipo;

    @BindView(R.id.usuario)
    TextView usuario;

    @BindView(R.id.correo)
    TextView correo;

    @BindView(R.id.cedula)
    TextView cedula;

    @BindView(R.id.telefono)
    TextView telefono;

    @BindView(R.id.titulofecha)
    TextView fecha;

    @BindView(R.id.descripcion)
    EditText descripcion;

    @BindView(R.id.imagen)
    ImageView imagen;

    @BindView(R.id.boton_cancelar)
    Button boton_cancelar;

    @BindView(R.id.boton_aceptar)
    Button boton_aceptar;

    @BindView(R.id.boton_eliminar)
    Button boton_eliminar;

    float lat;
    float lon;
    String linkimagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_averia);

        ButterKnife.bind(this);
        //Título del activity
        setTitle("Editar avería");

        //Obtenemos los detalles de la avería
        obtenerDetalles();

        //Cargamos el toolbar
        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
    }

    private void obtenerDetalles() {

        String idaveria = getIntent().getStringExtra("ID");
        //Se obtiene la referencia singleton desde el gestor.
        ServicioPosts servicio = GestorServicio.obtenerServicio();

        //Se llama al metodo definido en el servicio para obtener los detalles de un post en particular
        servicio.obtenerDetallesDeAverias(idaveria).enqueue(new Callback<Averia>() {
            @Override
            public void onResponse(Call<Averia> call, Response<Averia> response) {
                //Si es exitosa, recuperamos la lista recibida de response.body()
               Averia resultado = response.body();
                //Mostramos un mensaje para probar que fue exitosa la recuperacion de informacion
                /*Toast.makeText(EditarAveriaActivity.this,
                        "Conexión Exitosa",
                        Toast.LENGTH_SHORT).show();*/

                //Mostramos la imagen asociada a la avería
                if(resultado.getImagen()!=null && !resultado.getImagen().isEmpty()) {
                    Picasso.get()
                            .load(resultado.getImagen())
                            .placeholder(R.drawable.noimagen)
                            .error(R.drawable.noimagen)
                            .resize(150, 150)
                            .centerCrop()
                            .into(imagen);
                }else{ //Si no tiene imagen asociada mostramos una imagen genérica
                    Picasso.get()
                            .load(R.drawable.noimagen)
                            .placeholder(R.drawable.noimagen)
                            .error(R.drawable.noimagen)
                            .resize(150, 150)
                            .centerCrop()
                            .into(imagen);
                }

                //Capturamos la hora del dispositivo para usarlo en el formulario
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                Date date = new Date();
                final String fecha1 = dateFormat.format(date);

                //Rellenamos los campos
                nombre.setText(resultado.getNombre());
                tipo.setText(resultado.getTipo());
                usuario.setText(resultado.getUsuario().getNombre());
                cedula.setText(resultado.getUsuario().getCedula());
                correo.setText(resultado.getUsuario().getCorreo());
                telefono.setText(resultado.getUsuario().getTel());
                descripcion.setText(resultado.getDescripcion());
                fecha.setText(fecha1.toString());
                lat = resultado.getUbicacion().getLat();
                lon = resultado.getUbicacion().getLon();
                linkimagen = resultado.getImagen();
            }

            @Override
            public void onFailure(Call<Averia> call, Throwable t) {
                //Si no, se muestra un error
                Toast.makeText(EditarAveriaActivity.this,
                        "Error al interactuar con el servicio",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @OnClick(R.id.boton_aceptar)
    public void boton_aceptar(){
        editar();
    }


    private void editar( ) {
        String idaveria = getIntent().getStringExtra("ID");

        //Se obtiene la referencia singleton desde el gestor.
        ServicioPosts servicio = GestorServicio.obtenerServicio();

        //Llenamos los datos del usuario
        Usuario usuariomodelo = new Usuario(correo.getText().toString(),usuario.getText().toString(),telefono.getText().toString(),cedula.getText().toString());

        //Llenamos datos de ubicación
        Ubicacion ubicacion = new Ubicacion(lat,lon);

        //Llenamos todos los datos para el post
        Averia post = new Averia(nombre.getText().toString(),tipo.getText().toString(),usuariomodelo,fecha.getText().toString(),descripcion.getText().toString(),linkimagen,ubicacion);

        //Se llama al metodo definido en el servicio para crear un nuevo objeto Post
        servicio.actualizarAveria(idaveria, post).enqueue(new Callback<Averia>() {
            @Override
            public void onResponse(Call<Averia> call, Response<Averia> response) {
                //Si es exitoso, recuperamos la copia del post creado
                Averia resultado = response.body();
                //Mostramos un mensaje para probar que fue exitosa la creacion del elemento
                Toast.makeText(EditarAveriaActivity.this,
                        "Se ha editado la avería",
                        Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Averia> call, Throwable t) {
                //Si no, se muestra un error
                Toast.makeText(EditarAveriaActivity.this,
                        "Error al interactuar con el servicio",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.boton_eliminar)
    public void boton_eliminar() {

        //Desplegamos mensaje para borrar avería
        new MaterialDialog.Builder(this)
                .title("Está seguro de borrar la avería")
                .positiveText("SI, borrar avería")
                .negativeText("No")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String idaveria = getIntent().getStringExtra("ID");

                        //Se obtiene la referencia singleton desde el gestor.
                        ServicioPosts servicio = GestorServicio.obtenerServicio();



                        //Se llama al metodo definido en el servicio para crear un nuevo objeto Post
                        servicio.eliminarAverias(idaveria).enqueue(new Callback<Averia>() {
                            @Override
                            public void onResponse(Call<Averia> call, Response<Averia> response) {
                                //Si es exitoso, recuperamos la copia del post creado
                                Averia resultado = response.body();
                                //Mostramos un mensaje para probar que fue exitosa la creacion del elemento
                                Toast.makeText(EditarAveriaActivity.this, "Se eliminó la avería",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Averia> call, Throwable t) {
                                //Si no, se muestra un error
                                Toast.makeText(EditarAveriaActivity.this,
                                        "Error al interactuar con el servicio",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String nickStr = PreferencesManager.getUsernameFromPreferences(this);

        switch (item.getItemId()) {
            case R.id.Salir:
                finishAffinity();
                return true;
            case R.id.Editar_usuario:
                Intent editarusuario = new Intent(EditarAveriaActivity.this, EditarUsuarioActivity.class);
                String mensaje = nickStr;
                editarusuario.putExtra("Usuario", mensaje);
                startActivity(editarusuario);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.boton_cancelar)
    public void boton_cancelar() {
        finish();
    }
}
