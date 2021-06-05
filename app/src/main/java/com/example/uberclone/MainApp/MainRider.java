package com.example.uberclone.MainApp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.uberclone.MainApp.CallBacks.Rider.EndLocationCallBack;
import com.example.uberclone.MainApp.CallBacks.Rider.OnRequestExistsCallBack;
import com.example.uberclone.MainApp.CallBacks.Rider.OnRequestLatLang;
import com.example.uberclone.Models.Requests.DriverLocation;
import com.example.uberclone.Models.Requests.RiderLocation;
import com.example.uberclone.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainRider extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, RoutingListener {

    private GoogleMap mMap;

    private String nameOfRider;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private RiderLocation currentRiderLocation;
    private RiderLocation endRiderLocation;

    private boolean isRequestAccepted;

    private Marker currentRiderMarker;
    private Marker driverMarker;

    private DriverLocation driverLocation;
    private LatLng driverLatLang;

    private List<Polyline> polylines = null;

    private LatLng currentRiderLatLang;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Location lastKnownLocation = getLastKnownLocation();
                    updateLocation(lastKnownLocation, nameOfRider, "START");
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rider);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        nameOfRider = getNameOfRider();

        isRequestAccepted = false;
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocation(location, nameOfRider, "START");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };

        handlePermission();

        setMarkerOnRiderEndLocation();

        checkIfRequestAccepted(new OnRequestExistsCallBack() {
            @Override
            public void onRequestExistsCallback(boolean exists) {

            }
        });


    }

    public void handlePermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Location lastKnownLocation = getLastKnownLocation();
            updateLocation(lastKnownLocation, nameOfRider, "START");
        }
    }

    public Location getLastKnownLocation() {

        Location lastKnwnLoc = null;

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            lastKnwnLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (lastKnwnLoc == null) {
            Log.e("Last known loc", "PROBLEM TO GET LAST KNOWN LOCATION VIA GPS");
            return null;
        }

        return lastKnwnLoc;
    }

    public void checkIfRequestAccepted(OnRequestExistsCallBack onRequestExistsCallBack){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Accepted requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Log.i("Accpted requests",String.valueOf(snapshot.getChildrenCount()));

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if (dataSnapshot.child(nameOfRider) != null){
                            isRequestAccepted = true;

                            onRequestExistsCallBack.onRequestExistsCallback(isRequestAccepted);
                            setMarkerOfDriversLocation(String.valueOf(dataSnapshot.getKey()));

                        }
                    }
                }
                else{
                    Log.e("Path failed","Can not find path to Accpted requesters");
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Log.e("Database problem",error.getMessage());
            }
        });
    }

    public void setMarkerOfDriversLocation(String driversName){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Accepted requests").child(driversName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){
                    double latitude = (double) snapshot.child("Driver's location").child("driver_latitude").getValue();
                    double longitude = (double) snapshot.child("Driver's location").child("driver_longitude").getValue();

                    putCordinatesInMap(driversName,latitude, longitude);
                }
                else{
                    Log.e("Path failed","Failed to get driver from database in accepted requests");
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Log.e("Database error",error.getMessage());
            }
        });
    }

    public void putCordinatesInMap(String driversName, double latitude, double longitude){
        if ((Double)latitude == null || (Double)longitude == null){
            Log.e("Cordinates failed","Failed to ger driver's location from database");
            return;
        }

        LatLng driversposition = new LatLng(latitude,longitude);

        driverMarker= mMap.addMarker(new MarkerOptions().position(driversposition).title("Driver").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));


        Log.i("Marker added","Accepted call from "+driversName+" and marker added on map");
    }

    public void getDriverLatLang(OnRequestLatLang onRequestLatLang){
        checkIfRequestAccepted(new OnRequestExistsCallBack() {
            @Override
            public void onRequestExistsCallback(boolean exists) {
                if (exists){
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference root = firebaseDatabase.getReference();

                    root.child("Requests").child("Accepted requests").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Log.e("Driver's number","Number of drivers who accepted call-> "+String.valueOf(snapshot.getChildrenCount()));

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    if (dataSnapshot.child(nameOfRider) != null){
                                        Log.i("Founded", dataSnapshot.getKey()+" accepted "+nameOfRider);

                                        double latitude = (double) dataSnapshot.child("Driver's location").child("driver_latitude").getValue();
                                        double longitude = (double) dataSnapshot.child("Driver's location").child("driver_longitude").getValue();

                                        onRequestLatLang.onRequestsLatLang(new LatLng(latitude,longitude));


                                    }
                                }
                            }
                            else{
                                Log.e("FAILED","Can not find path to accepted requests");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull  DatabaseError error) {
                            Log.e("Database error",error.getMessage());
                        }
                    });
                }
            }
        });
    }

    public void updateLocation(Location location, String username, String message) {
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        currentRiderMarker =  mMap.addMarker(new MarkerOptions().position(position).title(username + "\n" + message).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));

        currentRiderLatLang = position;
    }

    public void setMarkerOnRiderEndLocation() {
        getEndRiderLocation(new EndLocationCallBack() {
            @Override
            public void onEndLocationCallBack(RiderLocation endRiderLocation) {
                LatLng endPosition = new LatLng(endRiderLocation.getRider_latitude(), endRiderLocation.getRider_longitude());
                driverMarker = mMap.addMarker(new MarkerOptions().position(endPosition).title(nameOfRider + "\n" + "END").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                Log.i("Marker added","Marked added on locations "+endPosition.toString());

            }
        });
    }

    public void getEndRiderLocation(EndLocationCallBack endLocationCallBack) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").child(nameOfRider).child("End location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    double latitude = (double) snapshot.child("rider_latitude").getValue();
                    double longitude = (double) snapshot.child("rider_longitude").getValue();

                    endRiderLocation = new RiderLocation(latitude, longitude);

                    endLocationCallBack.onEndLocationCallBack(endRiderLocation);
                } else {
                    Log.e("Path problem", "Can not find path to user end location");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database error", error.getMessage());
            }
        });

    }

    public String getNameOfRider() {
        if (this.getIntent().getStringExtra("existed requester") != null && !this.getIntent().getStringExtra("existed requester").equalsIgnoreCase("")) {
            return this.getIntent().getStringExtra("existed requester");
        } else {
            if (this.getIntent().getStringExtra("name of rider from payment") != null && !this.getIntent().getStringExtra("name of rider from payment").equalsIgnoreCase("")) {
                return this.getIntent().getStringExtra("name of rider from payment");
            }

            Log.e("Intent problem", "Problem to get name of rider from intent");
            return null;
        }
    }

    // function to find Routes.
    public void Findroutes(LatLng Start, LatLng End)
    {
        if(Start==null || End==null) {
            Toast.makeText(MainRider.this,"Unable to get location", Toast.LENGTH_LONG).show();
        }
        else
        {
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key("AIzaSyAHGvJ92ezvleOW9CGlzKA_thlAcjSRnF4")  //also define your api key here.
                    .build();
            routing.execute();
        }
    }

    //Routing call back functions.
    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar= Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
//        Findroutes(start,end);
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(MainRider.this,"Finding Route...",Toast.LENGTH_LONG).show();
    }

    //If Route finding success..
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(currentRiderLatLang);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if(polylines!=null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i <route.size(); i++) {

            if(i==shortestRouteIndex)
            {
                polyOptions.color(getResources().getColor(R.color.colorPrimary));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng=polyline.getPoints().get(0);
                int k=polyline.getPoints().size();
                polylineEndLatLng=polyline.getPoints().get(k-1);
                polylines.add(polyline);

            }
            else {

            }

        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        mMap.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        mMap.addMarker(endMarker);
    }

    @Override
    public void onRoutingCancelled() {
        Findroutes(currentRiderLatLang,end);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Findroutes(currentRiderLatLang,end);

    }

}