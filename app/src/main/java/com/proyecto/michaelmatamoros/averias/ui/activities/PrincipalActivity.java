package com.proyecto.michaelmatamoros.averias.ui.activities;


import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.proyecto.michaelmatamoros.averias.Adaptador.AdaptadorFragments;
import com.proyecto.michaelmatamoros.averias.EditarUsuarioActivity;
import com.proyecto.michaelmatamoros.averias.R;
import com.proyecto.michaelmatamoros.averias.helpers.PreferencesManager;

public class PrincipalActivity extends AppCompatActivity {

    ViewPager vp;
    PagerAdapter pa;
    PagerTabStrip pts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


        vp = findViewById(R.id.viewpager);
        pts = findViewById(R.id.strip);

        pa = new AdaptadorFragments(getSupportFragmentManager());

        vp.setAdapter(pa);

        vp.setOffscreenPageLimit(2);

        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
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
                Intent editarusuario = new Intent(PrincipalActivity.this, EditarUsuarioActivity.class);
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
