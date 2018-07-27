package com.proyecto.michaelmatamoros.averias;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.proyecto.michaelmatamoros.averias.Adaptador.AdaptadorAverias;
import com.proyecto.michaelmatamoros.averias.Servicios.GestorServicio;
import com.proyecto.michaelmatamoros.averias.Servicios.ServicioPosts;
import com.proyecto.michaelmatamoros.averias.helpers.DividerItemDecoration;
import com.proyecto.michaelmatamoros.averias.modelo.Averia;
import com.proyecto.michaelmatamoros.averias.ui.activities.DetallesActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaAveriasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ListaAveriasFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public ListaAveriasFragment() {
        // Required empty public constructor
    }

    RecyclerView list;
    ArrayList<Averia> posts;
    AdaptadorAverias adaptador;
    final FragmentActivity c = getActivity();


    @BindView(R.id.boton_nuevo)
    Button boton_nuevo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_averias, container, false);

        //manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        ButterKnife.bind(this, view);

        //Damos forma al RecyclerView
        list = (RecyclerView) view.findViewById(R.id.lista_averias);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager((c), LinearLayoutManager.VERTICAL, false));
        list.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        list.setItemAnimator(new DefaultItemAnimator());

        //Se llama al metodo de obtenerPosts para llamar al servicio.
        obtenerPosts();

        return view;
    }

    private void obtenerPosts() {
        //Se obtiene la referencia singleton desde el gestor.
        ServicioPosts servicio = GestorServicio.obtenerServicio();

        //Se llama al metodo definido en el servicio para obtener los posts.
        servicio.obtenerAveria().enqueue(new Callback<ArrayList<Averia>>() {
            @Override
            public void onResponse(Call<ArrayList<Averia>> call, Response<ArrayList<Averia>> response) {
                //Si es exitosa, recuperamos la lista recibida de response.body()
                posts = response.body();
                //y llamamos al metodo para mostrar la lista
                //setupList();
                adaptador = new AdaptadorAverias(posts);
                list.setAdapter(adaptador);


                //Mostrar mensaje al tocar un dato
                adaptador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = list.getChildAdapterPosition(view);
                        if (pos != RecyclerView.NO_POSITION) {
                            Averia clickedDataItem = posts.get(pos);
                            Toast.makeText(view.getContext(), "Datos de " + clickedDataItem.getNombre(), Toast.LENGTH_SHORT).show();
                            //Se crea el Intent para enviar datos de avería y mostrarlos al usuario por su ID
                            Intent iradetalles = new Intent(getActivity(), DetallesActivity.class);
                            String ID = clickedDataItem.getId();
                            iradetalles.putExtra("ID", ID);
                            getActivity().startActivity(iradetalles);

                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<ArrayList<Averia>> call, Throwable t) {
                //Si no, se muestra un error
                Toast.makeText(getActivity(),
                        "Error al interactuar con el servicio",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @OnClick(R.id.boton_nuevo)
    public void boton_nuevo() {

        //Sacamos la ubicación del dispositivo

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;

        Location location = locationManager
                .getLastKnownLocation(locationProvider);

        //Vericamos que la variable location tenga datos
            if (location != null) {
                float lat = (float) location.getLatitude();
                float lon = (float) location.getLongitude();

                //Creamos Intent para pasar los datos de ubicación del usuario al crear una avería nueva
                Intent irNuevaAveria = new Intent(getActivity(), CrearAveriaActivity.class);
                irNuevaAveria.putExtra("Latitude", lat);
                irNuevaAveria.putExtra("Longitude", lon);
                getActivity().startActivity(irNuevaAveria);

            }


    }

    @Override
    public void onResume() {
        super.onResume();
        obtenerPosts();
    }

    @Override
    public void onStart() {
        super.onStart();
        obtenerPosts();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
