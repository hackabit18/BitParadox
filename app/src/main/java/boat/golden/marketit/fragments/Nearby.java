package boat.golden.marketit.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import boat.golden.marketit.R;
import boat.golden.marketit.adapters.ShopAdapter;
import boat.golden.marketit.datatypes.ProductData;
import boat.golden.marketit.datatypes.ShopData;
import boat.golden.marketit.helpers.CoordinatesDistance;

public class Nearby extends Fragment {

    RecyclerView recyclerView;
    ArrayList<ShopData> data;
    Button button;
    DataSnapshot localSnapshot;
    TextView seektext,fragmetText;
    SeekBar seekBar;
    CoordinatesDistance distanceHelper;
    ShopAdapter adapter;
    DatabaseReference data_Ref;
    String lato,lono;
    ProgressBar progressBar;
    float a;
    int seek_progress;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_near_by,container,false);
        seek_progress=0;
        a=0.5f;
        distanceHelper=new CoordinatesDistance();
        progressBar=v.findViewById(R.id.prooo);
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        lato=sharedPreferences.getString("lat","0");
        lono=sharedPreferences.getString("lon","0");


        recyclerView=v.findViewById(R.id.recycler_nearby);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        data =new ArrayList<>();
/*        data.add(new ShopData("A to Z Store",null,null,null,null,"A Test Shop",0,0,"SHOP"));
        data.add(new ShopData("A to Z Store",null,null,null,null,"A Test Shop",0,0,"SHOP"));
        data.add(new ShopData("A to Z Store",null,null,null,null,"A Test Shop",0,0,"SHOP"));
        data.add(new ShopData("A to Z Store",null,null,null,null,"A Test Shop",0,0,"SHOP"));
        data.add(new ShopData("A to Z Store",null,null,null,null,"A Test Shop",0,0,"SHOP"));
        data.add(new ShopData("A to Z Store",null,null,null,null,"A Test Shop",0,0,"SHOP"));
        data.add(new ShopData("A to Z Store",null,null,null,null,"A Test Shop",0,0,"SHOP"));
        data.add(new ShopData("A to Z Store",null,null,null,null,"A Test Shop",0,0,"SHOP"));
*/



        if((ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_DENIED)
                && ( ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_DENIED))
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},12345);
        }

        data_Ref = FirebaseDatabase.getInstance().getReference("SHOPS");
        data_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){localSnapshot=dataSnapshot;}
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {

                    String name=snapshot.child("shop_name").getValue(String.class);
                    String details=snapshot.child("shop_address").getValue(String.class);
                    float lat=snapshot.child("lat").getValue(float.class);
                    float lon=snapshot.child("lon").getValue(float.class);
                    Log.e("TAG",(lat+lon+""));
                    if (distanceHelper.verify(lat,lon,Float.parseFloat(lato),Float.parseFloat(lono),a))
                        data.add(new ShopData(name,"NULL","NULL","NULL","NULL",details,lat,lon,"SHOP"));

                }
                progressBar.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
                if (data.isEmpty()){
                    Toast.makeText(getContext(),"No Shops Found Within Range",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        fragmetText=v.findViewById(R.id.fragment_text);
        AlertDialog.Builder builderseekbar2=new AlertDialog.Builder(getContext());
        View vv=View.inflate(getContext(),R.layout.seek_bar,null);
        builderseekbar2.setView(vv);
        seektext=vv.findViewById(R.id.seek_text);
        seekBar=vv.findViewById(R.id.seekit);
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seek_progress=i;

                if (i==100)seektext.setText("10Km");
                if (i<80)seektext.setText("7Km");
                if (i<60)seektext.setText("5Km");
                if (i<40)seektext.setText("2Km");
                if (i<20)seektext.setText("1Km");
                if (i==0)seektext.setText("500m");
                if (i==100)fragmetText.setText("10Km");
                if (i<80)fragmetText.setText("7Km");
                if (i<60)fragmetText.setText("5Km");
                if (i<40)fragmetText.setText("2Km");
                if (i<20)fragmetText.setText("1Km");
                if (i==0)fragmetText.setText("500m");
                if (i==100)a=10;
                if (i<80)a=8;
                if (i<60)a=5;
                if (i<40)a=2;
                if (i<20)a=1;
                if (i==0)a=0.5f;


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        builderseekbar2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FilterData();
            }
        });
        final AlertDialog alertDialog=builderseekbar2.create();
        button=v.findViewById(R.id.map_boundry);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        adapter=new ShopAdapter(data);
        recyclerView.setAdapter(adapter);


        return  v;

    }

    private void FilterData() {

        data.clear();
        if (localSnapshot.exists()) {
            for (DataSnapshot snapshot : localSnapshot.getChildren()) {

                String name = snapshot.child("shop_name").getValue(String.class);
                String details = snapshot.child("shop_address").getValue(String.class);
                float lat = snapshot.child("lat").getValue(float.class);
                float lon = snapshot.child("lon").getValue(float.class);
                if (distanceHelper.verify(lat, lon, Float.parseFloat(lato), Float.parseFloat(lono), a))
                    data.add(new ShopData(name, "NULL", "NULL", "NULL", "NULL", details, lat, lon, "SHOP"));

            }
            adapter.notifyDataSetChanged();
            if (data.isEmpty()) {
                Toast.makeText(getContext(), "No Shops Found Within Range", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
