package boat.golden.marketit;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener ,GoogleMap.OnMarkerDragListener
        , View.OnClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    Marker cMarker;
    SharedPreferences sharedPreferences;
    DatabaseReference reference;
    FloatingActionButton fab;
    LatLng myLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fab=findViewById(R.id.fabM);
        locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
        sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        String uid=sharedPreferences.getString("UID","NULL");
        reference=FirebaseDatabase.getInstance().getReference("SHOPS/"+uid);
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        String lat=sharedPreferences.getString("lat","0");
        String lon=sharedPreferences.getString("lon","0");
        myLoc=new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerDragListener(this);

        cMarker=mMap.addMarker(new MarkerOptions().position(myLoc).draggable(true));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc,15.0f));
        fab.setOnClickListener(this);
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.getUiSettings().setCompassEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        mMap.setPadding(0,140,0,0);
//        mMap.setOnMarkerClickListener(this);
//        mMap.setOnMyLocationClickListener(this);
   //     locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,this,null);

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {


    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        cMarker.remove();
      cMarker=  mMap.addMarker(new MarkerOptions().position(cMarker.getPosition()).draggable(true));
//        Log.e("TAG"
//                , "onMarkerDragEnd: "+marker.getPosition().longitude );
    }





    @Override
    public void onClick(View v) {

        Log.e("TAG", "onMarkerClick: "+cMarker.getPosition().latitude+ cMarker.getPosition().longitude );
        reference.child("lat").setValue(cMarker.getPosition().latitude);
        reference.child("lon").setValue(cMarker.getPosition().longitude).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(),"Location Uploaded",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

}
