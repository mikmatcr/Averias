package com.proyecto.michaelmatamoros.averias;


import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.proyecto.michaelmatamoros.averias.bd.DatabaseHelper;
import com.proyecto.michaelmatamoros.averias.bd.Usuario;
import com.proyecto.michaelmatamoros.averias.helpers.PreferencesManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NuevoUsuarioActivity extends AppCompatActivity {


    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.til_nombre)
    TextInputLayout til_nombre;

    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.til_username)
    TextInputLayout til_username;

    @BindView(R.id.cedula)
    EditText cedula;
    @BindView(R.id.til_cedula)
    TextInputLayout til_cedula;


    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.til_password)
    TextInputLayout til_password;


    @BindView(R.id.password_2)
    EditText password_2;
    @BindView(R.id.til_password2)
    TextInputLayout til_password2;



    @BindView(R.id.correo)
    EditText correo;
    @BindView(R.id.til_correo)
    TextInputLayout til_correo;

    @BindView(R.id.telefono)
    EditText telefono;
    @BindView(R.id.til_telefono)
    TextInputLayout til_telefono;

    @BindView(R.id.boton_cancelar)
    Button boton_cancelar;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario);

        ButterKnife.bind(this);

        //Cargamos el toolbar
        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

    }

    //Boton Cancelar
    @OnClick(R.id.boton_cancelar)
    public void boton_cancelar() {

        finish();
    }

    @OnClick(R.id.boton_aceptar)
    public void boton_aceptar() {

        try {
            //Capturamos los datos digitados en variables
            String strName = name.getText().toString().trim();
            String susername = username.getText().toString().trim();
            String spassword = password.getText().toString().trim();
            String spassword2 = password_2.getText().toString().trim();
            String scorreo = correo.getText().toString().trim();
            String scedula = cedula.getText().toString().trim();
            String stelefono = telefono.getText().toString().trim();



            //Inicializamos el DBHelper
            if(dbHelper == null) {
                dbHelper = new DatabaseHelper(this);
            }

            //Recuperamos el dao
            Dao<Usuario, Integer> userDao = dbHelper.getUserDao();

            //Recuperamos todos los usuarios que tengan ese mismo username
            List<Usuario> usuarios = dbHelper.getUserDao().queryBuilder().
                    where().eq("username", susername.trim()).query();

            //Validamos que las contraseñas concuerden
            if(!spassword.equals(spassword2)){
                //MOSTRAR ERROR
                Toast.makeText(NuevoUsuarioActivity.this, "Passwords no concuerdan.", Toast.LENGTH_SHORT).show();
                return;
            }

            //Validamos si existe el usuario
            if(usuarios.size() > 0){
                Toast.makeText(NuevoUsuarioActivity.this, "Ese usuario ya existe.", Toast.LENGTH_SHORT).show();
                return;
            }

            //Validamos que todos los campos esten llenos
            String validarcorreo = til_correo.getEditText().getText().toString();
            String nombre = til_nombre.getEditText().getText().toString();
            String cedula = til_cedula.getEditText().getText().toString();
            String password = til_password.getEditText().getText().toString();
            String password2 = til_password2.getEditText().getText().toString();
            String telefono = til_telefono.getEditText().getText().toString();

            //Validamos que el correo tenga formato correcto
            boolean c = esCorreoValido(validarcorreo);

            if (nombre.isEmpty() | cedula.isEmpty()| password.isEmpty()|
                    password2.isEmpty() | telefono.isEmpty() | c==false) {
                Toast.makeText(this,"Debe llenar todos los campos",Toast.LENGTH_LONG).show();
                return;
            }else {
                // OK, se pasa a la siguiente acción
                Toast.makeText(this, "Se ha guardado el resgistro", Toast.LENGTH_LONG).show();
            }

            //Si todas las validaciones fueron correctas, creamos el usuario en la BD
            Usuario nuevo = new Usuario();

            nuevo.username = susername;
            nuevo.password = spassword;
            nuevo.nombre = strName;
            nuevo.cedula = scedula;
            nuevo.correo = scorreo;
            nuevo.telefono = stelefono;

            userDao.createOrUpdate(nuevo);
            finish();
        } catch (Exception e){
            Toast.makeText(NuevoUsuarioActivity.this, "Error creando cuenta.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean esCorreoValido(String correo) {
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            til_correo.setError("Correo electrónico inválido");
            return false;
        } else {
            til_correo.setError(null);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.Editar_usuario);
        item.setVisible(false);
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
                Intent editarusuario = new Intent(NuevoUsuarioActivity.this, EditarUsuarioActivity.class);
                String mensaje = nickStr;
                editarusuario.putExtra("Usuario", mensaje);
                startActivity(editarusuario);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
