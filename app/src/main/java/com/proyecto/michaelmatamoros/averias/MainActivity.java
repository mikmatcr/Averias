package com.proyecto.michaelmatamoros.averias;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.proyecto.michaelmatamoros.averias.bd.DatabaseHelper;
import com.proyecto.michaelmatamoros.averias.bd.Usuario;
import com.proyecto.michaelmatamoros.averias.helpers.PreferencesManager;
import com.proyecto.michaelmatamoros.averias.ui.activities.PrincipalActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.user_input)
    EditText usuario;

    @BindView(R.id.password_input)
    EditText password;

    @BindView(R.id.remember)
    CheckBox remember;

    @BindView(R.id.loginButton)
    Button loginButton;

    @BindView(R.id.registerButton)
    Button registerButton;


    DatabaseHelper bdHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //Colocamos el usuario guardado en preferencias si es que existe
        String usuarioStr = PreferencesManager.getUsernameFromPreferences(this);
        usuario.setText(usuarioStr);

        //Verificamos si marcó recordar contraseña y lo mostramos si es asi
        if(PreferencesManager.getRememberFromPreferences(this)) {
            remember.setChecked(true);
            String passwordStr = PreferencesManager.getPasswordFromPreferences(this);
            password.setText(passwordStr);
        }

    }


    @OnClick(R.id.loginButton)
    public void loginButton(){
        try {

            //Guardamos los datos en las preferencias
            PreferencesManager.savePreferencesLogin(MainActivity.this,
                    usuario.getText().toString(),
                    password.getText().toString(),
                    remember.isChecked());

            //Inicializamos el BD Helper solo si hace falta
            if (bdHelper == null) {
                bdHelper = new DatabaseHelper(MainActivity.this);
            }

            //Obtenemos el nombre de usuario ingresado en el campo de texto
            String usuarioIngresado = usuario.getText().toString().trim();

            //Obtenemos el dao de la tabla de usuarios
            Dao<Usuario, Integer> userDao = bdHelper.getUserDao();

            //Generamos un filtro y obtenemos la lista resultado
            Where filtro = userDao.queryBuilder()
                    .where()
                    .eq("username", usuarioIngresado);

            List<Usuario> usuarios = filtro.query();

            //Si no se encontro ningun usuario, es porque no existe
            if(usuarios.size() == 0){
                Toast.makeText(MainActivity.this, "Ese usuario no existe!", Toast.LENGTH_SHORT).show();
                return;
            }

            //Obtenemos la referencia al usuario
            final Usuario user = usuarios.get(0);

            String passwordIngresado = password.getText().toString().trim();

            //Si los passwords son diferentes, mostramos un error
            if(!user.password.equals(passwordIngresado)){
                Toast.makeText(MainActivity.this, "Password incorrecto!", Toast.LENGTH_SHORT).show();
                return;
            }

            //ENTRAR A LA CUENTA
            PreferencesManager.savePreferences(MainActivity.this,
                    user.username,
                    user.correo,
                    user.telefono,
                    user.nombre,
                    user.cedula);
            Toast.makeText(MainActivity.this, "Bienvenido " + user.nombre, Toast.LENGTH_LONG).show();

            //Arreglo para mostrar los datos en un material dialog
            String [] dialogo;
            dialogo = new String [4];
            dialogo[0] = "Nombre: " + user.nombre.toString();
            dialogo[1] = "Cédula: " + user.cedula.toString();
            dialogo[2] = "Correo: " + user.correo.toString();
            dialogo[3] = "Telefono: " + user.telefono.toString();

            new MaterialDialog.Builder(this)
                    .title("Datos de usuario")
                    .items(dialogo)
                    .positiveText("Editar Usuario")
                    .negativeText("Ver averías")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent editarusuario = new Intent(MainActivity.this, EditarUsuarioActivity.class);
                            String mensaje = user.username;

                            editarusuario.putExtra("Usuario", mensaje);
                            startActivity(editarusuario);
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent veraverias = new Intent(MainActivity.this, PrincipalActivity.class);

                            startActivity(veraverias);
                        }
                    })
                    .show();
        }
        catch(Exception e){
            Log.d("Error", "Error");
        }
    }

    //Activity para registrar usuario
    @OnClick(R.id.registerButton)
    public void registerButton(){
        Intent intent = new Intent(MainActivity.this,NuevoUsuarioActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
