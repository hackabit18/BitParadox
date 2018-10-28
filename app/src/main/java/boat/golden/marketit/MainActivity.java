package boat.golden.marketit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import boat.golden.marketit.fragments.Fav;
import boat.golden.marketit.fragments.Home;
import boat.golden.marketit.fragments.Nearby;
import boat.golden.marketit.fragments.Orders;
import boat.golden.marketit.helpers.BottomNavigationViewHelper;

import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity implements LocationListener {
    Fragment fragment;
    Menu localmenu;
    BottomNavigationView navview;
    SharedPreferences sharedPreferences;
    String currentstatus = "false";
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;
    FirebaseAuth auth;
    FirebaseUser currentfirebaseuser;
    String s;
    SharedPreferences.Editor editor;
    AlertDialog.Builder dialog_takeloc;
    LocationManager locationManager;
    AlertDialog ddd;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.orderss:
                    fragment = new Orders();
                    break;

                case R.id.homee:

                    fragment = new Home();
                    break;
                case R.id.favv:

                    fragment = new Fav();
                    break;

                case R.id.nearbyy:
                    fragment = new Nearby();
                    break;
            }
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        dialog_takeloc = new AlertDialog.Builder(this);
        locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
        dialog_takeloc.setView(View.inflate(this,R.layout.dialog_loading,null)).setCancelable(false);
         ddd=dialog_takeloc.create();ddd.show();
        getLocation();
        navview = findViewById(R.id.bottom_navigation);
        navview.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navview);
        sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        currentstatus = sharedPreferences.getString("islogin", "false");

        if (currentstatus.equals("false")) {
            Intent i = new Intent(this, login.class);
            startActivity(i);
        }
        currentstatus = sharedPreferences.getString("islogin", "false");
        fragment = new Home();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void getLocation() {
        if((ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_DENIED)
                && ( ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_DENIED))
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},12345);
        }
        else
        {

            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,this,null);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==12345)
        {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,this,null);
        }
        else
        {Toast.makeText(this, "Give Location Permission", Toast.LENGTH_SHORT).show();
         //   finish();
    }}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (currentstatus.equals("false")) {
            menu.findItem(R.id.logout).setTitle("Login");
        }
        super.onCreateOptionsMenu(menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.about:
                Toast.makeText(MainActivity.this, "About", Toast.LENGTH_LONG).show();


                Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);


                break;
            case R.id.help:
                Toast.makeText(MainActivity.this, "Ohhh You need Help", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                currentstatus = sharedPreferences.getString("islogin", "false");
                Toast.makeText(MainActivity.this, "Logged Out" , Toast.LENGTH_SHORT).show();
                if (!currentstatus.equals("true")) {
                    item.setTitle("Login");
                    Intent i = new Intent(MainActivity.this, login.class);
                    startActivity(i);
                } else {
                    item.setTitle("Log Out");

                    FirebaseAuth.getInstance().signOut();
                    editor.putString("islogin","false");
                    editor.apply();
                    Intent i = new Intent(MainActivity.this, login.class);
                    startActivity(i);
                }

                break;
            case R.id.seller:
                if (sharedPreferences.getString("islogin", "false").equals("true")) {
                    startActivity(new Intent(MainActivity.this, SellerMainActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Must Login First", Toast.LENGTH_SHORT).show();
                }
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder buildd = new AlertDialog.Builder(MainActivity.this);

        buildd.setMessage("Do You Want To Exit");
        buildd.setTitle("Market It");
        buildd.setCancelable(true);
        buildd.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog fgi = buildd.create();
        fgi.show();
    }

    @Override
    public void onLocationChanged(Location location) {
     // ddd.cancel();
        ddd.cancel();
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor ed=sharedPreferences.edit();
        ed.putString("lat",location.getLatitude()+"");
        ed.putString("lon",location.getLongitude()+"");
        //Toast.makeText(this,String.valueOf(location.getLatitude())+"  "+String.valueOf(location.getLongitude()),Toast.LENGTH_LONG).show();
        ed.apply();

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}