package com.proyecto.michaelmatamoros.averias.Adaptador;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.proyecto.michaelmatamoros.averias.ListaAveriasFragment;
import com.proyecto.michaelmatamoros.averias.ui.fragments.MapsActivity;

public class AdaptadorFragments extends FragmentPagerAdapter {
    ListaAveriasFragment f1;
    MapsActivity f2;

    public AdaptadorFragments(FragmentManager fm){
        super(fm);


        f1 = new ListaAveriasFragment();
        f2 = new MapsActivity();
    }


    @Override
    public Fragment getItem(int position) {
        if(position == 0)
            return f1;
        else
            return f2;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0)
            return "Lista";
        else
            return "Mapa";
    }
}

