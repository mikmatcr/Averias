package com.proyecto.michaelmatamoros.averias;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.proyecto.michaelmatamoros.averias.Servicios.GestorServicio;
import com.proyecto.michaelmatamoros.averias.Servicios.ServicioPosts;
import com.proyecto.michaelmatamoros.averias.Servicios.UploadService;
import com.proyecto.michaelmatamoros.averias.helpers.DocumentHelper;
import com.proyecto.michaelmatamoros.averias.helpers.IntentHelper;
import com.proyecto.michaelmatamoros.averias.helpers.PreferencesManager;
import com.proyecto.michaelmatamoros.averias.modelo.Averia;
import com.proyecto.michaelmatamoros.averias.modelo.CrearAveria;
import com.proyecto.michaelmatamoros.averias.modelo.ImageResponse;
import com.proyecto.michaelmatamoros.averias.modelo.Ubicacion;
import com.proyecto.michaelmatamoros.averias.modelo.Upload;
import com.proyecto.michaelmatamoros.averias.modelo.Usuario;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearAveriaActivity extends AppCompatActivity {

    @BindView(R.id.usuario)
    EditText usuario;
    @BindView(R.id.til_usuario)
    TextInputLayout til_usuario;

    @BindView(R.id.cedula)
    EditText cedula;
    @BindView(R.id.til_cedula)
    TextInputLayout til_cedula;

    @BindView(R.id.correo)
    EditText correo;
    @BindView(R.id.til_correo)
    TextInputLayout til_correo;

    @BindView(R.id.telefono)
    EditText telefono;
    @BindView(R.id.til_telefono)
    TextInputLayout til_telefono;

    @BindView(R.id.imagen)
    ImageView imagen;

    @BindView(R.id.boton_foto)
    Button tomar_foto;

    @BindView(R.id.boton_buscarfoto)
    Button boton_buscarfoto;

    @BindView(R.id.boton_subirfoto)
    Button boton_subirfoto;

    @BindView(R.id.titulo)
    EditText nombre;
    @BindView(R.id.til_titulo)
    TextInputLayout til_titulo;


    @BindView(R.id.descripcion)
    EditText descripcion;
    @BindView(R.id.til_descripcion)
    TextInputLayout til_descipcion;

    @BindView(R.id.titulofecha)
    TextView fecha;

    @BindView(R.id.boton_agregar)
    Button boton_agregar;

    @BindView(R.id.boton_cancelar)
    Button boton_cancelar;

    @BindView((R.id.tipo))
    Spinner tipo;

    String tipos = "";


    private static final int PERM_CODE = 1000;
    private static final int REQUEST_TAKE_PHOTO = 101;

    private Uri mUri;

    private Uri uriSavedImage;

    private String filePath;

    private String urlimagen = null;

    Bitmap imageBitmap;

    private Upload upload; // Upload object containging image and meta data
    private File chosenFile; //chosen file from intent


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_averia);
        ButterKnife.bind(this);

        //Título del activity
        setTitle("Crear avería");

        //Cargamos datos de usuario
        cargarusuario();

        //Cargar tipos de averías
        cargartipo();

        //Cargamos el toolbar
        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

    }

    //Cargamos los datos de usuario desde las preferencias
    private void cargarusuario(){
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        String usuarioStr = PreferencesManager.getNombreFromPreferences(this);
        usuario.setText(usuarioStr);

        String cedulaStr = PreferencesManager.getCedulaFromPreferences(this);
        cedula.setText(cedulaStr);

        String correoStr = PreferencesManager.getCorreoFromPreferences(this);
        correo.setText(correoStr);

        String telefonoStr = PreferencesManager.getTelefonoFromPreferences(this);
        telefono.setText(telefonoStr);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();

        final String fecha1 = dateFormat.format(date);
        fecha.setText(fecha1.toString());

    }

    public void cargartipo(){

        //Cargamos los datos del arreglos de tipos de averías
        ArrayAdapter<CharSequence> adaptador =
                ArrayAdapter.createFromResource(this,
                        R.array.valores_array, android.R.layout.simple_spinner_item);

        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//Saldrán las opciones hacía abajo
        tipo.setAdapter(adaptador);

        tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                tipos = (adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick(R.id.boton_foto)
    public void boton_foto(){

        verificarPermisos();

    }

    @OnClick(R.id.boton_buscarfoto)
    public void boton_buscarfoto(){
        IntentHelper.chooseFileIntent(this);
    }


    @OnClick(R.id.boton_subirfoto)
    public void boton_subirfoto(){
         /*
      Create the @Upload object
     */
        if (chosenFile == null) {
            Toast.makeText(this,"Debe Tomar o Buscar una foto",Toast.LENGTH_SHORT).show();
            return;
        }
        createUpload(chosenFile);

    /*
      Iniciar upload
     */
        new UploadService(this).Execute(upload, new UiCallback());
    }

    //Subimos la imagen a internet
    private void createUpload(File image) {
        upload = new Upload();
        upload.image = image;
        upload.title = nombre.getText().toString();
        upload.description = descripcion.getText().toString();
    }

    private class UiCallback implements retrofit.Callback<ImageResponse> {

        @Override //Capturamos link de la imagen y lo mostramos al usuario
        public void success(ImageResponse imageResponse, retrofit.client.Response response) {
            urlimagen = imageResponse.data.link;
            String titulo = nombre.toString();
            if(urlimagen != null | titulo != null) {
                Toast.makeText(CrearAveriaActivity.this, "Se ha subido con éxito la imagen: " + urlimagen, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            //Assume we have no connection, since error is null
            if (error == null) {
                Snackbar.make(findViewById(R.id.rootView), "No hay conexión a internet", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void verificarPermisos() {
        //Obtenemos el estado actual de los permisos
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //Si ya tenemos permisos, continuamos tomando la foto
        //Si no, pedimos permiso
        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            continuarTomarFoto();
        } else {
            askForPermission();
        }
    }

    public void askForPermission(){
        //Hacemos la solicitud de permiso
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERM_CODE);
    }

    //Este callback es llamado cuando un usuario contesta
    //a la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        //Si obtuvimos valores en grantResults y el primer elemento
        //es de PERMISSION_GRANTED (permiso concedido), volvemos a llamar
        //a verificarPermisos.

        //Si el usuario no dio permiso, llamamos finish() para cerrar la
        //aplicacion
        if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED){
            verificarPermisos();
        } else {
            finish();
        }
    }

    private void continuarTomarFoto() {
        //Construimos un intent con una peticion de captura
        //de imagenes
        Intent takePictureIntent =
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Creamos una carpeta en la memoria del celular
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Averias");
        imagesFolder.mkdirs();

        //añadimos el nombre de la imagen
        File image = new File(imagesFolder, "fotoaverias.jpg");
        uriSavedImage = FileProvider.getUriForFile(this,
                "com.proyecto.michaelmatamoros.averias",image);

        //Le decimos al Intent que queremos grabar la imagen
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        //Especificamos el URI en el que queremos que se guarde
        //la imagen
        //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        //Ejecutamos el intent, cediendo control a la aplicacion
        //de toma de fotos que el usuario seleccione
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

    }

    //Al haber llamado a onStartActivityForResult, indicamos al
    //sistema que llame a este callback una vez la foto haya sido
    //capturada y almacenada
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Verificamos que el codigo de respuesta sea igual al codigo
        //de peticion que especificamos al ejecutar el intent

        //Tambien verificamos que el codigo resultado sea RESULT_OK,
        //lo cual indica que la foto fue capturada exitosamente.
        if (requestCode == REQUEST_TAKE_PHOTO &&
                resultCode == RESULT_OK) {

            try {
                //Obtenemos el BitMap a partir del URI que habiamos
                //obtenido anteriormente
                imageBitmap =
                        MediaStore.Images.Media.
                                getBitmap(getContentResolver(), uriSavedImage);

                //Mostramos el bitmap en el ImageView declarado
                //en nuestro layout file
                imagen.setImageBitmap(imageBitmap);
                filePath = Environment.getExternalStorageDirectory()+
                        "/Averias/"+"fotoaverias.jpg";
                chosenFile = new File(filePath);

            }catch(Exception e){
                Log.d("Prueba", e.getMessage());
            }
        }

        Uri returnUri;

        if (requestCode != IntentHelper.FILE_PICK) {
            return;
        }

        if (resultCode != RESULT_OK) {
            return;
        }

        returnUri = data.getData();
        filePath = DocumentHelper.getPath(this, returnUri);
        //Safety check to prevent null pointer exception
        if (filePath == null || filePath.isEmpty()) return;
        chosenFile = new File(filePath);

        //Mostramos la imagen seleccionada en el ImageView
        Picasso.get()
                .load(chosenFile)
                .placeholder(R.drawable.noimagen)
                .fit()
                .into(imagen);

    }



    private void crearaveria(){
        //Llamamos los datos que nos pasaron los activities anteriores
        float lati = getIntent().getFloatExtra("Latitude",0);
        float longi = getIntent().getFloatExtra("Longitude",0);

        //Creamos un nombre unico para la avería, basado
        //en la fecha y hora actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-mm-ss", Locale.getDefault());
        Date date = new Date();

        String idavería = "Averia_" + dateFormat.format(date);

        //Se obtiene la referencia singleton desde el gestor.
        ServicioPosts servicio = GestorServicio.obtenerServicio();

        Usuario usuariomodelo = new Usuario(correo.getText().toString(),usuario.getText().toString(),telefono.getText().toString(),cedula.getText().toString());
        Ubicacion ubicacion = new Ubicacion(lati,longi);

        CrearAveria post = new CrearAveria(idavería,nombre.getText().toString(),tipos,usuariomodelo,fecha.getText().toString(),descripcion.getText().toString(),urlimagen,ubicacion);

        //Se llama al metodo definido en el servicio para crear un nuevo objeto Post
        servicio.crearNuevaAveria(post).enqueue(new Callback<Averia>() {
            @Override
            public void onResponse(Call<Averia> call, Response<Averia> response) {
                //Si es exitoso, recuperamos la copia del post creado
                Averia resultado = response.body();
                //Mostramos un mensaje para probar que fue exitosa la creacion del elemento
                Toast.makeText(CrearAveriaActivity.this,
                        "Avería Creada",
                        Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Averia> call, Throwable t) {
                //Si no, se muestra un error
                Toast.makeText(CrearAveriaActivity.this,
                        "Error al interactuar con el servicio",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Botón que valida campos y crea la avería
    @OnClick(R.id.boton_agregar)
    public void boton_agregar(){

        //Validamos campos llenos
        String titulo = til_titulo.getEditText().getText().toString();
        String nombre = til_usuario.getEditText().getText().toString();
        String descripcion = til_descipcion.getEditText().getText().toString();
        String cedula = til_cedula.getEditText().getText().toString();
        String correo = til_correo.getEditText().getText().toString();
        String telefono = til_telefono.getEditText().getText().toString();

        if(urlimagen == null | titulo.isEmpty() | nombre.isEmpty() | descripcion.isEmpty() | cedula.isEmpty()
                | correo.isEmpty() | telefono.isEmpty()) {
            Toast.makeText(this,"Debe llenar todos los campos y subir una foto",Toast.LENGTH_LONG).show();//Mostrar mensaje si un campo está vacío
        }else{
            //Si todos los campos están llenos se crea la avería
            crearaveria();
        }

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
                Intent editarusuario = new Intent(CrearAveriaActivity.this, EditarUsuarioActivity.class);
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
    public void boton_cancelar(){
        //Marcamos el resultado como CANCELADO y terminamos este activity.
        this.setResult(Activity.RESULT_CANCELED);
        finish();
    }


}
