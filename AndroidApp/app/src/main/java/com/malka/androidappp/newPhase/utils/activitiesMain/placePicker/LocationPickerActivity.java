package com.malka.androidappp.newPhase.utils.activitiesMain.placePicker;

import static com.malka.androidappp.newPhase.data.helper.HelpFunctions.getLocationInfoFromLatLng;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.malka.androidappp.R;
import com.malka.androidappp.newPhase.core.BaseActivity;
import com.malka.androidappp.newPhase.data.helper.ConstantObjects;
import com.malka.androidappp.newPhase.domain.models.servicemodels.LocationPickerModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LocationPickerActivity extends BaseActivity implements View.OnClickListener {


    private final int ZOOM_VALUE = 18;
    //Current Location
    public FusedLocationProviderClient mFusedLocationClient;
    /**
     * GeoDataClient wraps our service connection to Google Play services and provides access
     * to the Google Places API for Android.
     */
    protected PlacesClient mGeoDataClient;
    private MapView mMapView;
    private GoogleMap googleMap;
    private HashMap<String, String> hashMapLocationInfo = new HashMap<>();
    private LatLng currentCenter;
    AppCompatButton Confirmlocation;
    public LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();


            if (locationList.size() > 0) {
                //The last location in the list is the newest
                final Location location = locationList.get(locationList.size() - 1);

                setLatLngAndAnimateCameraPosition(new LatLng(location.getLatitude(), location.getLongitude()));

                if (mFusedLocationClient != null && mLocationCallback != null) {
                    mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                }
            }


        }
    };
    //Search
    private AutoCompleteTextView actSearch;
    private ImageView ivClearSearch;
    private TextView tvSearch;
    private LocationRequest mLocationRequest;
    //Places Search
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private String locationPlacesSearch = "";
    /**
     * Callback for results from a Places Geo Data Client query that shows the first place result in
     * the details view on screen.
     */
    private OnCompleteListener<FetchPlaceResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<FetchPlaceResponse>() {
        @Override
        public void onComplete(@NonNull Task<FetchPlaceResponse> task) {
            try {
                hideSoftKeyboard(actSearch);
                FetchPlaceResponse places = task.getResult();
                // Get the Place object from the buffer.
                final Place place = places.getPlace();

                setLatLngAndAnimateCameraPosition(place.getLatLng());

                locationPlacesSearch = actSearch.getText().toString().trim();

                tvSearch.setText(locationPlacesSearch);
                tvSearch.setVisibility(View.VISIBLE);


            } catch (Throwable e) {
                // Request did not complete successfully

            }
        }
    };
    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data Client
     * to retrieve more details about the place.
     */
    private AdapterView.OnItemClickListener actSearchOnItemClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);
            Log.i("TAG", "Autocomplete item selected: " + primaryText);
            List<Place.Field> fields =
                    Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

            /*
             Issue a request to the Places Geo Data Client to retrieve a Place object with
             additional details about the place.
              */
            Task<FetchPlaceResponse> placeResult = mGeoDataClient.fetchPlace(FetchPlaceRequest.builder(placeId, fields).build());
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        LinearLayout toolbar_main = findViewById(R.id.toolbar_main);

        TextView toolbar_title =toolbar_main.findViewById(R.id.toolbar_title);
        ImageView back_btn =toolbar_main.findViewById(R.id.back_btn);
        toolbar_title.setText( getString(R.string.Choose_location));
        back_btn.setOnClickListener(v -> finish());
        init();
        headerSettings();
        mapViewWidget(savedInstanceState);
        initPlaces();

        setListeners();
    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Confirmlocation = findViewById(R.id.Confirmlocation);
        mMapView = findViewById(R.id.mapView);
        actSearch = findViewById(R.id.actSearch);
        ivClearSearch = findViewById(R.id.ivClearSearch);
        tvSearch = findViewById(R.id.tvSearch);

        actSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (actSearch.getText().toString().trim().equals("")) {
                    ivClearSearch.setVisibility(View.GONE);
                } else {
                    ivClearSearch.setVisibility(View.VISIBLE);
                }
            }
        });

        actSearch.setOnEditorActionListener((v, actionId, event) -> {

            switch (v.getId()) {

                case R.id.actSearch:
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        // Your piece of code on keyboard search click
                        hideSoftKeyboard(actSearch);
                        return true;
                    }
                    break;
            }

            return false;
        });

        actSearch.setOnItemClickListener(actSearchOnItemClickListener);
    }

    private void headerSettings() {

    }



    private void mapViewWidget(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;


                currentLocation();

                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        // Get the center coordinate of the map, if the overlay view is center too
                        CameraPosition cameraPosition = googleMap.getCameraPosition();
                        currentCenter = cameraPosition.target;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                locationPlacesSearch = "";
                                hashMapLocationInfo = getLocationInfoFromLatLng(currentCenter.latitude, currentCenter.longitude, LocationPickerActivity.this);
                                if (hashMapLocationInfo.containsKey("address")
                                        && hashMapLocationInfo.get("address") != null) {
                                    if (actSearch.getText().toString().length() == 0)
                                        tvSearch.setText(hashMapLocationInfo.get("address"));
                                    tvSearch.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                });

            }
        });
    }

    private void initPlaces() {
        Places.initialize(this, getString(R.string.google_maps_key));
        // Construct a GeoDataClient for the Google Places API for Android.
        mGeoDataClient = Places.createClient(this);
        // Set up the adapter that will retrieve suggestions from the Places Geo Data Client.
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient);
        actSearch.setAdapter(mPlaceAutocompleteAdapter);
    }

    private void setLatLngAndAnimateCameraPosition(LatLng currentCenter) {
        this.currentCenter = currentCenter;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(this.currentCenter)             // Sets the center of the map to current location
                .zoom(ZOOM_VALUE)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    private void currentLocation() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                if (googleMap != null) {
                    googleMap.setMyLocationEnabled(true);
                    bottomRightPositioningOfLocationButton();
                }
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    private void bottomRightPositioningOfLocationButton() {
        View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0,
                (int) getResources().getDimension(R.dimen._10sdp), (int) getResources().getDimension(R.dimen._10sdp));
    }

    private void setListeners() {
        Confirmlocation.setOnClickListener(this);
        ivClearSearch.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        hideSoftKeyboard( actSearch);

        switch (v.getId()) {



            case R.id.Confirmlocation: {
                if (hashMapLocationInfo.containsKey("address") && hashMapLocationInfo.get("address") != null) {
                    String address=tvSearch.getText().toString();
                    LocationPickerModel locationPickerModel=new LocationPickerModel(address,currentCenter.latitude,currentCenter.longitude);
                    Intent intent = getIntent();
                    intent.putExtra(ConstantObjects.getData(),new  Gson().toJson(locationPickerModel));
                    setResult(LocationPickerActivity.RESULT_OK, intent);
                    onBackPressed();
                } else {
                    showError(getString(R.string.location_is_not_found));
                }
                break;
            }

            case R.id.ivClearSearch: {
                actSearch.setText("");
                break;
            }

        }

    }



    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume(); // needed to get the map to display immediately
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMapView!=null){
            mMapView.onDestroy();
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }



}