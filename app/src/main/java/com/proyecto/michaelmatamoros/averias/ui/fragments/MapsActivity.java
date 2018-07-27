package com.proyecto.michaelmatamoros.averias.ui.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.proyecto.michaelmatamoros.averias.CrearAveriaActivity;
import com.proyecto.michaelmatamoros.averias.EditarAveriaActivity;
import com.proyecto.michaelmatamoros.averias.R;
import com.proyecto.michaelmatamoros.averias.Servicios.GestorServicio;
import com.proyecto.michaelmatamoros.averias.Servicios.ServicioPosts;
import com.proyecto.michaelmatamoros.averias.modelo.Averia;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapsActivity extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    public MapsActivity() {
        // Required empty public constructor
    }

    //Codigo del intent para abrir el formulario
    private final static int PERM_CODE = 1;

    //Tenemos que guardar la ubicacion en donde el usuario pidio
    //crear un nuevo marker
    private LatLng mTempPosicion;
    private GoogleMap mMap;
    ArrayList<Averia> posts;
    AlertDialog alert = null;

    LocationManager manager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        //Comprobamos si el GPS esta activado
        manager =  (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Si no esta activado el GPS mostramos un aviso para activarlo
            AlertNoGps();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    //Aviso para activar el GPS
    private void AlertNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("El sistema GPS esta desactivado, ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        //Abrimos el GPS
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alert != null) {
            alert.dismiss();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        //Obtenemos una referencia al mapa inicializado para siempre
        //poder manipularlo
        mMap = googleMap;

        //Indicamos el tipo de mapa a crear
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        //Se obtiene la referencia singleton desde el gestor.
        ServicioPosts servicio = GestorServicio.obtenerServicio();

        //Se llama al metodo definido en el servicio para obtener los posts.
        servicio.obtenerAveria().enqueue(new Callback<ArrayList<Averia>>() {
            @Override
            public void onResponse(Call<ArrayList<Averia>> call, Response<ArrayList<Averia>> response) {
                //Si es exitosa, recuperamos la lista recibida de response.body()
                posts = response.body();
                //Recorre el Arreglo y genera el marcador
                for (Averia ubicacion : posts) {
                    //Llenamos el mapa con las marcas de las averías creadas
                    float lat = ubicacion.getUbicacion().getLat();
                    float lon = ubicacion.getUbicacion().getLon();
                    LatLng latLng = new LatLng(lat, lon);
                    String titulo = ubicacion.getNombre();
                    String descripcion = ubicacion.getDescripcion();
                    String Id = ubicacion.getId();
                    //Se añade un nuevo marcador al mapa con el titulo indicado.
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            //El título y descripción de la avería la colocamos en un snippet
                            .snippet("Nombre de Avería: " + titulo + Html.fromHtml("<br />") + "Descripción: " + descripcion)
                            //Obtenemos el titulo que el usuario
                            //ingreso, por medio del objeto data en este metodo.
                            .title(Id)); //Como titulo colocamos el ID de la avería para poder editarla

                }
                //Mostramos la marca con Material Dialog para poder dar opciones al usuario
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    public boolean onMarkerClick(final Marker marker) {
                        new MaterialDialog.Builder(getActivity())
                                //Titulo del dialogo
                                .title("ID " + marker.getTitle())
                                //Contenido del dialogo
                                .content(marker.getSnippet())
                                .positiveText("Editar")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        //Si da clic en editar lanzamos el al Activiti de Editar Avería
                                        Intent intent = new Intent(getActivity(), EditarAveriaActivity.class);
                                        //En este caso optenemos el titulo del marcador que es el que contiene el ID de la avería
                                        intent.putExtra("ID", marker.getTitle());
                                        getActivity().startActivity(intent);
                                    }
                                })
                                .show();

                        return true;
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
        //


        //Seteamos a este activity como el LongClickListener para escuchar
        //eventos de presion prolongada. En cada evento, se va a llamar a
        //onMapLongClick()
        mMap.setOnMapLongClickListener(this);

        //Revisamos si tenemos permiso para mostrar el boton de ubicacion
        //del usuario
        chequearPermiso();
    }

    private void chequearPermiso() {
        //Obtenemos el estado del permiso de ubicacion
        int state = ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

        //Si lo tenemos, habilitamos el boton de ubicacion del usuario
        if (state == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            LocationManager locationManager = (LocationManager)
                    getActivity().getSystemService(Context.LOCATION_SERVICE);
            String locationProvider = LocationManager.NETWORK_PROVIDER;

            Location location1 = locationManager
                    .getLastKnownLocation(locationProvider);

            if(location1 != null){
                float latitude = (float) location1.getLatitude();
                float longitud = (float) location1.getLongitude();

                LatLng latLng = new LatLng(latitude, longitud);
                //Hacemos acercamiento a nuestra ubicación

                float zoomLevel = 16.0f; //This goes up to 21
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
            }

        } else {
            //Si no, pedimos permiso
            askForPermission();
        }
    }

    public void askForPermission(){
        //Pedimos permiso para el de tipo ACCESS_FINE_LOCATION
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERM_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        //Si el usuario nos dio permiso, entonces podemos llamar a
        //chequearPermiso de nuevo. Si no, no hacemos nada (y el boton de
        //ubicacion no se va a mostrar)
        if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED){
            chequearPermiso();
        }
    }

    //Esta funcion dicta que ocurre al recibir un evento de presion
    //prolongada sobre una posicion no ocupada del mapa
    @Override
    public void onMapLongClick(LatLng latLng) {
        //Guardamos la ubicacion presionada para recordarla
        mTempPosicion = latLng;
        float lat = (float) mTempPosicion.latitude;
        float lon = (float) mTempPosicion.longitude;

        Intent formIntent = new Intent(getActivity(), CrearAveriaActivity.class);
        formIntent.putExtra("Latitude",lat);
        formIntent.putExtra("Longitude", lon);

        getActivity().startActivity(formIntent);
    }



    @Override
    public void onResume() {
        super.onResume();
        if(mMap != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            mMap.clear();

            // add markers from database to the map

            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }
    }
}
