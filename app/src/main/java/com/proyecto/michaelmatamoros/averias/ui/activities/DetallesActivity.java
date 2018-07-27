package com.proyecto.michaelmatamoros.averias.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.proyecto.michaelmatamoros.averias.EditarUsuarioActivity;
import com.proyecto.michaelmatamoros.averias.R;
import com.proyecto.michaelmatamoros.averias.helpers.PreferencesManager;
import com.proyecto.michaelmatamoros.averias.ui.fragments.DetallesAveriaFragment;

public class DetallesActivity extends AppCompatActivity {

    //Objeto con el que se van a realizar las transacciones
    //de fragments
    android.support.v4.app.FragmentManager fm;

    //Instancias de los fragmentos que pueden existir dentro del
    //contenedor (R.id.contenedor en activity_main.xml)
    DetallesAveriaFragment fragmentUno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        //Título del activity
        setTitle("Detalle de avería");

        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        //La instancia de fragmentUno se inicializa acá a modo de
        //ejemplo. Notar que fragmentDos y fragmentTres se inicializan
        //arriba.
        fragmentUno = new DetallesAveriaFragment();


        //Queremos que al iniciarse el activity, se muestre el tab 1 por
        //defecto, por lo que se genera una transaccion inicial para agregarlo.
        fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.contenedor, fragmentUno);
        ft.commit();

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
                Intent editarusuario = new Intent(DetallesActivity.this, EditarUsuarioActivity.class);
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
