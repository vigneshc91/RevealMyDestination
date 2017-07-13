package com.vignesh.revealmydestination;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateTripFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateTripFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateTripFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Calendar tripDate = Calendar.getInstance();

    private GoogleMap googleMap;
    private Map<String, LatLng> latLngMap = new HashMap<String, LatLng>();
    private OnFragmentInteractionListener mListener;

    public CreateTripFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateTripFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateTripFragment newInstance(String param1, String param2) {
        CreateTripFragment fragment = new CreateTripFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).setActionBarTitle("Create Trip");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.create_trip_menu, menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.createTripBtn:
                return true;
            case R.id.tripDate:
                this.showDatePicker(item);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDatePicker(final MenuItem item){

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                item.setTitle(dayOfMonth + " " + new DateFormatSymbols().getShortMonths()[month-1]);
            }
        }, tripDate.get(Calendar.YEAR), tripDate.get(Calendar.MONTH), tripDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.setTitle("Select Trip Date");
        datePickerDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_trip, container, false);




        PlaceAutocompleteFragment placeAutoSourceCompleteFragement = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_source_fragment);
        PlaceAutocompleteFragment placeAutoDestinationCompleteFragement = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_destination_fragment);

        MapFragment googleMapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.gMap);
        googleMapFragment.getMapAsync(this);


        // source autocomplete listener
        placeAutoSourceCompleteFragement.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d("place", place.getName().toString());
                Log.d("location", place.getLatLng().toString());

                latLngMap.put("source", place.getLatLng());
                if(latLngMap.get("source") != null && latLngMap.get("destination") != null){

                    setMapWithDirection();
                }
            }

            @Override
            public void onError(Status status) {

            }
        });

        // destination autocomplete listener
        placeAutoDestinationCompleteFragement.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d("place", place.getName().toString());
                Log.d("location", place.getLatLng().toString());

                latLngMap.put("destination", place.getLatLng());

                if(latLngMap.get("source") != null && latLngMap.get("destination") != null){
                    setMapWithDirection();
                }
            }

            @Override
            public void onError(Status status) {

            }
        });

        return view;
    }

    public void setMapWithDirection(){
        GoogleDirection.withServerKey("AIzaSyAUHQMF36kAXv_3bMZ0yVILAiNOm4IU6V0")
                .from(new LatLng(latLngMap.get("source").latitude, latLngMap.get("source").longitude))
                .to(new LatLng(latLngMap.get("destination").latitude,latLngMap.get("destination").longitude))
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if(direction.isOK()){
                            googleMap.addMarker(new MarkerOptions().position(latLngMap.get("source")));
                            googleMap.addMarker(new MarkerOptions().position(latLngMap.get("destination")));

                            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                            googleMap.addPolyline(DirectionConverter.createPolyline(getContext(), directionPositionList, 5, Color.RED));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngMap.get("destination"), 13));

                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Log.d("error", t.getMessage());
                    }
                });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
