package com.vignesh.revealmydestination;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.Toast;

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
import com.vignesh.revealmydestination.Helper.AppConstants;
import com.vignesh.revealmydestination.Helper.Common;
import com.vignesh.revealmydestination.Helper.SuccessConstants;
import com.vignesh.revealmydestination.Model.Trip;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateTripFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateTripFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateTripFragment extends Fragment implements OnMapReadyCallback, ListTripFragment.OnListFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "tripId";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private Realm realm;
    private Menu menu;
    private PlaceAutocompleteFragment placeAutoSourceCompleteFragement, placeAutoDestinationCompleteFragement;
    private String sourceLocation, destinationLocation;

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
     * @param tripId Parameter 1.
     * @return A new instance of fragment CreateTripFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateTripFragment newInstance(String tripId) {
        CreateTripFragment fragment = new CreateTripFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, tripId);
        Log.d("trip id", tripId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        realm = Realm.getDefaultInstance();
        ((MainActivity) getActivity()).setActionBarTitle("Create Trip");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            Log.d("trip value", mParam1);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        inflater.inflate(R.menu.create_trip_menu, menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.createTripBtn:
                createTrip();
                return true;
            case R.id.tripDate:
                this.showDatePicker(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createTrip(){
        if(sourceLocation.length() == 0 || destinationLocation.length() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Alert").setMessage("Choose Source and Destination to proceed");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        }
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                Trip trip = realm.createObject(Trip.class, UUID.randomUUID().toString());
                trip.setSrc_location(sourceLocation);
                trip.setSrc_latitude(latLngMap.get("source").latitude);
                trip.setSrc_longitude(latLngMap.get("source").longitude);
                trip.setDst_location(destinationLocation);
                trip.setDst_latitude(latLngMap.get("destination").latitude);
                trip.setDst_longitude(latLngMap.get("destination").longitude);
                trip.setDate(tripDate.getTime());
                trip.setCreated_at(new Date());
                trip.setUpdated_at(new Date());
                resetForm();
                Toast.makeText(getActivity(), SuccessConstants.TRIP_CREATED, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void resetForm(){
        googleMap.clear();
        placeAutoSourceCompleteFragement.setText("");
        placeAutoDestinationCompleteFragement.setText("");
        tripDate = Calendar.getInstance();
        menu.findItem(R.id.tripDate).setTitle("Today");
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

        placeAutoSourceCompleteFragement = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_source_fragment);
        placeAutoDestinationCompleteFragement = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_destination_fragment);

        MapFragment googleMapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.gMap);
        googleMapFragment.getMapAsync(this);


        // source autocomplete listener
        placeAutoSourceCompleteFragement.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d("place", place.getName().toString());
                Log.d("location", place.getLatLng().toString());

                sourceLocation = place.getName().toString();

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

                destinationLocation = place.getName().toString();

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
        GoogleDirection.withServerKey(AppConstants.GOOGLE_DIRECTIONS_API_KEY)
                .from(new LatLng(latLngMap.get("source").latitude, latLngMap.get("source").longitude))
                .to(new LatLng(latLngMap.get("destination").latitude,latLngMap.get("destination").longitude))
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if(direction.isOK()){
                            googleMap.clear();
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

    @Override
    public void onListFragmentInteraction(Trip trip) {
        Log.d("trip list", trip.getId());
        sourceLocation = trip.getSrc_location();
        destinationLocation = trip.getDst_location();
        latLngMap.put("source", new LatLng(trip.getSrc_latitude(), trip.getSrc_longitude()));
        latLngMap.put("destination", new LatLng(trip.getDst_latitude(), trip.getDst_longitude()));
        tripDate.setTime(trip.getDate());
        placeAutoSourceCompleteFragement.setText(sourceLocation);
        placeAutoDestinationCompleteFragement.setText(destinationLocation);
        if(this.googleMap != null){
            setMapWithDirection();
        }

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
