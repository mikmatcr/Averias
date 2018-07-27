package com.proyecto.michaelmatamoros.averias.ui.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.proyecto.michaelmatamoros.averias.EditarAveriaActivity;
import com.proyecto.michaelmatamoros.averias.R;
import com.proyecto.michaelmatamoros.averias.Servicios.GestorServicio;
import com.proyecto.michaelmatamoros.averias.Servicios.ServicioPosts;
import com.proyecto.michaelmatamoros.averias.modelo.Averia;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetallesAveriaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DetallesAveriaFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public DetallesAveriaFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.nombre)
    TextView nombre;

    @BindView(R.id.tipo)
    TextView tipo;

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
    TextView descripcion;

    @BindView(R.id.imagen)
    ImageView imagen;

    @BindView(R.id.boton_cancelar)
    Button boton_cancelar;

    @BindView(R.id.boton_aceptar)
    Button boton_aceptar;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalles_averia, container, false);

        ButterKnife.bind(this, view);
        //Obtenemos los detalles de la avería
        obtenerDetalles();

        return view;
    }

    private void obtenerDetalles() {

        String idaveria = getActivity().getIntent().getStringExtra("ID");
        //Se obtiene la referencia singleton desde el gestor.
        ServicioPosts servicio = GestorServicio.obtenerServicio();

        //Se llama al metodo definido en el servicio para obtener los detalles de un post en particular
        servicio.obtenerDetallesDeAverias(idaveria).enqueue(new Callback<Averia>() {
            @Override
            public void onResponse(Call<Averia> call, Response<Averia> response) {
                //Si es exitosa, recuperamos la lista recibida de response.body()
                Averia resultado = response.body();
                //Mostramos un mensaje para probar que fue exitosa la recuperacion de informacion
                Toast.makeText(getActivity(),
                        "Conexión Exitosa",
                        Toast.LENGTH_SHORT).show();

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

                //Rellenamos los campos con los datos de la avería
                nombre.setText(resultado.getNombre());
                tipo.setText(resultado.getTipo());
                usuario.setText(resultado.getUsuario().getNombre());
                cedula.setText(resultado.getUsuario().getCedula());
                correo.setText(resultado.getUsuario().getCorreo());
                telefono.setText(resultado.getUsuario().getTel());
                descripcion.setText(resultado.getDescripcion());
                fecha.setText(resultado.getFecha());


            }

            @Override
            public void onFailure(Call<Averia> call, Throwable t) {
                //Si no, se muestra un error
                Toast.makeText(getActivity(),
                        "Error al interactuar con el servicio",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }


    @OnClick(R.id.boton_cancelar)
    public void boton_cancelar() {
        getActivity().finish();
    }

    //Boton para ir a editar la avería
    @OnClick(R.id.boton_aceptar)
    public void boton_aceptar() {
        //Creamos intent con el id de la avería a modificar
        String idaveria = getActivity().getIntent().getStringExtra("ID");
        Intent iraedicion = new Intent(getActivity(),EditarAveriaActivity.class);
        iraedicion.putExtra("ID",idaveria);

        startActivity(iraedicion);
        getActivity().finish();

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
