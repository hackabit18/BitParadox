package boat.golden.marketit;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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


public class MainActivity extends AppCompatActivity {
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
        navview = findViewById(R.id.bottom_navigation);
        navview.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navview);
        sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        currentstatus = sharedPreferences.getString("islogin", "false");
        Toast.makeText(this, currentstatus, Toast.LENGTH_SHORT).show();
        if (currentstatus.equals("false")) {
            Intent i = new Intent(this, login.class);
            startActivity(i);
        }
        currentstatus = sharedPreferences.getString("islogin", "false");
        Toast.makeText(this, currentstatus, Toast.LENGTH_SHORT).show();
        fragment = new Home();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

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
                Toast.makeText(MainActivity.this, "Log Out" + " " + currentstatus, Toast.LENGTH_SHORT).show();
                if (currentstatus.equals("false")) {
                    Intent i = new Intent(MainActivity.this, login.class);
                    startActivity(i);
                    currentstatus = sharedPreferences.getString("islogin", "false");
                    Toast.makeText(this, currentstatus, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You r currently logged in", Toast.LENGTH_SHORT).show();
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
}