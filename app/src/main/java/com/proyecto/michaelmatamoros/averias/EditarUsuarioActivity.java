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
import com.j256.ormlite.stmt.Where;
import com.proyecto.michaelmatamoros.averias.bd.DatabaseHelper;
import com.proyecto.michaelmatamoros.averias.bd.Usuario;
import com.proyecto.michaelmatamoros.averias.helpers.PreferencesManager;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditarUsuarioActivity extends AppCompatActivity {

    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.til_nombre)
    TextInputLayout til_nombre;


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

    @BindView(R.id.boton_aceptar)
    Button boton_aceptar;



    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        ButterKnife.bind(this);
        //Cargamos los datos de la BD
        try {
            mostrardatos();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Cargamos el toolbar
        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

    }

    @OnClick(R.id.boton_cancelar)
    public void boton_cancelar() {

        finish();
    }

    //Boton para guardar los cambios que hizo el usuario
    @OnClick(R.id.boton_aceptar)
    public void boton_aceptar() {

        //Capturamos los datos digitados en variables
        try {
            String strName = name.getText().toString().trim();
            String spassword = password.getText().toString();
            String spassword2 = password_2.getText().toString();
            String scorreo = correo.getText().toString();
            String scedula = cedula.getText().toString();
            String stelefono = telefono.getText().toString();

            //Obtenemos el nombre de usuario ingresado en el campo de texto
            String susername = getIntent().getStringExtra("Usuario");

            //Recuperamos el dao
            Dao<Usuario, Integer> userDao = dbHelper.getUserDao();

            //Generamos un filtro y obtenemos la lista resultado
            Where filtro = userDao.queryBuilder()
                    .where()
                    .eq("username", susername);

            List<Usuario> usuarios = filtro.query();

            //Obtenemos la referencia al usuario
            final Usuario user = usuarios.get(0);

            if(!spassword.equals(spassword2)){
                //MOSTRAR ERROR
                Toast.makeText(this, "Passwords no concuerdan.", Toast.LENGTH_SHORT).show();
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
            }else {
                // OK, se pasa a la siguiente acción
                Toast.makeText(this, "Se ha guardado el resgistro", Toast.LENGTH_LONG).show();
            }

            //Si todas las validaciones fueron correctas, actualizamos el usuario en la BD
            user.nombre = strName;
            user.password = spassword;
            user.correo = scorreo;
            user.telefono = stelefono;
            user.cedula = scedula;
            userDao.update(user);

            finish();
        } catch (Exception e){
            Toast.makeText(this, "Error creando cuenta.", Toast.LENGTH_SHORT).show();
        }
    }



    public void mostrardatos() throws SQLException {

        //Inicializamos el BD Helper solo si hace falta
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(this);
        }

        //Obtenemos el nombre de usuario ingresado en el campo de texto
        String usuarioIngresado = getIntent().getStringExtra("Usuario");

        //Obtenemos el dao de la tabla de usuarios
        Dao<Usuario, Integer> userDao = dbHelper.getUserDao();

        //Generamos un filtro y obtenemos la lista resultado
        Where filtro = userDao.queryBuilder()
                .where()
                .eq("username", usuarioIngresado);

        List<Usuario> usuarios = filtro.query();

        //Obtenemos la referencia al usuario
        final Usuario user = usuarios.get(0);

        name.setText(user.nombre);
        cedula.setText(user.cedula);
        password.setText("");
        password_2.setText("");
        correo.setText(user.correo);
        telefono.setText(user.telefono);

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
                Intent editarusuario = new Intent(EditarUsuarioActivity.this, EditarUsuarioActivity.class);
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
