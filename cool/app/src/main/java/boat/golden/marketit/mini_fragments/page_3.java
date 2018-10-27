package boat.golden.marketit.mini_fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.victor.loading.newton.NewtonCradleLoading;

import java.security.Permission;

import boat.golden.marketit.MapsActivity;
import boat.golden.marketit.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class page_3 extends Fragment {

//    SharedPreferences sharedPreferences;
//    DatabaseReference reference;
    Button button;
    NewtonCradleLoading newtonCradleLoading;
    float lat=0,lon=0;
    LocationManager locationManager;

    public page_3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome_slide3, container, false);
        locationManager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        lat=0;
        lon=0;
        button=v.findViewById(R.id.location);
//        sharedPreferences=getContext().getSharedPreferences("login",MODE_PRIVATE);
//        String uid=sharedPreferences.getString("UID","NULL");
//        reference=FirebaseDatabase.getInstance().getReference("SHOPS/"+uid);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                if((ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_DENIED)
                    && ( ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_DENIED))
                {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},12345);
                }
                else
                {
                    Intent i=new Intent(getContext(),MapsActivity.class);
                startActivity(i);
                }
                    //code to change the value of lat long for aman


//                reference.child("lat").setValue(lat);
//                reference.child("lon").setValue(lon).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(getContext(),"Location Uploaded",Toast.LENGTH_SHORT).show();
//                    }
//                });



            }
        });





        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==12345)
        {
            Intent i=new Intent(getContext(),MapsActivity.class);
            startActivity(i);
        }
        else
            Toast.makeText(getContext(), "Give Location Permission", Toast.LENGTH_SHORT).show();
    }
}
