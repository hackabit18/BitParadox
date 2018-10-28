package boat.golden.marketit.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import boat.golden.marketit.Prod_Desc;
import boat.golden.marketit.R;
import boat.golden.marketit.adapters.ProductAdapter;
import boat.golden.marketit.datatypes.Fav_OrdData;
import boat.golden.marketit.datatypes.ProductData;
import boat.golden.marketit.helpers.CoordinatesDistance;


public class Home extends Fragment {

    RecyclerView recyclerView;

    Button button,mapButton;
    ProductAdapter adapter;
    List<String> search=new ArrayList<>();
    ArrayList<Fav_OrdData> fav_ordData=new ArrayList<>();
    ArrayList<ProductData> homedata;
    SharedPreferences sharedPreferences,sharedPreferences2;
    ArrayList<ProductData> totalDAta;
    String item_name;
    SeekBar seekBar;
    TextView seektext;
    int seek_progress;
    CoordinatesDistance distanceHelper;
    String[] items;
    FirebaseDatabase database;
    FirebaseUser firebaseUser;
    DatabaseReference data_Ref;
    String key,lato,lono;
    DataSnapshot localDataSnapshot;
    float a;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_home,container,false);
        seek_progress=0;
        a=0.5f;
        distanceHelper=new CoordinatesDistance();
        recyclerView=v.findViewById(R.id.recycler_home);
        recyclerView.setHasFixedSize(true);
        totalDAta=new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        homedata =new ArrayList<>();
        button=v.findViewById(R.id.searchit);
        sharedPreferences2=getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        lato=sharedPreferences2.getString("lat","0");
        lono=sharedPreferences2.getString("lon","0");

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("SHOPS");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        data_Ref = database.getReference("Users");

        adapter = new ProductAdapter(fav_ordData, getContext(), new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Fav_OrdData item) {
                Toast.makeText(getContext(), item.getProductData().product_name, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), Prod_Desc.class);
                i.putExtra("item_name", item.getProductData().product_name);
                i.putExtra("item_desc", item.getProductData().product_desc);
                i.putExtra("item_type", item.getProductData().product_type);
                i.putExtra("item_img", item.getProductData().pic_add);
                i.putExtra("item_price", item.getProductData().price);
                i.putExtra("key", item.getFavUid());
                key=item.getFavUid();

                item_name = item.getProductData().product_name;
                sharedPreferences = getContext().getSharedPreferences(item.getFavUid(), Context.MODE_PRIVATE);
                startActivityForResult(i, 100);
            }
        });
        recyclerView.setAdapter(adapter);

        AlertDialog.Builder builderseekbar=new AlertDialog.Builder(getContext());
        View vvv=View.inflate(getContext(),R.layout.seek_bar,null);
        builderseekbar.setView(vvv);
        seektext=vvv.findViewById(R.id.seek_text);
        seekBar=vvv.findViewById(R.id.seekit);
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
                if (i==100)a=10;
                if (i<80)a=7;
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
        builderseekbar.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Filterit();
            }
        });
        final AlertDialog seekdialog=builderseekbar.create();
        mapButton=v.findViewById(R.id.map_boundry);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar.setProgress(seek_progress);
                seekdialog.show();
            }
        });
        items = new String[]{
                "Shirts",
                "Jeans",
                "Watches",
                "Mobiles",
                "Sports",
                "Snacks",
                "Chargers",
                "Toys",
                "Others"
        };


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

                final boolean[] checkedItems = new boolean[]{
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false

                };
                final List<String> itemList = Arrays.asList(items);
                builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItems[which] = isChecked;
                        // String currentItem = itemList.get(which);
                        //   Log.e("TAG", ""+isChecked);
                    }
                });
                builder.setCancelable(false);
                builder.setTitle("Select Items").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        search.clear();

                        for (int ii=0;ii<items.length;ii++){
                            if (checkedItems[ii]){search.add(items[ii]);
                                Log.e("TAG", items[ii] );}

                        }
                        Filterit();
                        for (int ii=0;ii<items.length;ii++){
                            checkedItems[ii]=false;
                        }

                    }
                }).setNegativeButton("Cancel",null);
                AlertDialog dialog=builder.create();
                dialog.show();


            }
        });

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
               localDataSnapshot=dataSnapshot;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String sid = snapshot.getKey();
                        DataSnapshot snapshotPRODUCTS = snapshot.child("PRODUCTS");
                            for (DataSnapshot snapshotpro : snapshotPRODUCTS.getChildren()) {
                                String tstamp = snapshotpro.getKey();
                                ProductData p = snapshotpro.getValue(ProductData.class);
                           /* p.price=(String)(snapshotpro.child("price")).getValue();
                            p.pic_add=(String)(snapshotpro.child("pic_add")).getValue();*/
                                totalDAta.add(p);
                                homedata.add(p);
                                fav_ordData.add(new Fav_OrdData(p, sid + tstamp));
                            }

                    }
                    adapter.notifyDataSetChanged();



            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });









        // ProductAdapter adapter=new ProductAdapter(homedata,getContext());
        // recyclerView.setAdapter(adapter);



        return  v;

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Toast.makeText(getContext(), sharedPreferences.getBoolean("cart", false) + " " + sharedPreferences.getBoolean("fav", false), Toast.LENGTH_SHORT).show();
            if(firebaseUser != null) {
                if (sharedPreferences.getBoolean("cart", false)) {
                    data_Ref.child(firebaseUser.getUid()).child("Orders").child(key).setValue("Ordered");
                } else {
                    data_Ref.child(firebaseUser.getUid()).child("Orders").child(key).removeValue();
                }

                if (sharedPreferences.getBoolean("fav", false)) {
                    data_Ref.child(firebaseUser.getUid()).child("Fav").child(key).setValue("Fav");
                } else {
                    data_Ref.child(firebaseUser.getUid()).child("Fav").child(key).removeValue();
                }
            }
        }
    }

    private void Filterit() {

        fav_ordData.clear();
        if (localDataSnapshot.exists()) {
            for (DataSnapshot snapshot : localDataSnapshot.getChildren()) {
                String sid = snapshot.getKey();
                DataSnapshot snapshotPRODUCTS = snapshot.child("PRODUCTS");
                float lat = snapshot.child("lat").getValue(float.class);
                float lon = snapshot.child("lon").getValue(float.class);
                Toast.makeText(getContext(), String.valueOf(a), Toast.LENGTH_SHORT).show();
                if (distanceHelper.verify(lat, lon, Float.parseFloat(lato), Float.parseFloat(lono), a)) {
                    for (DataSnapshot snapshotpro : snapshotPRODUCTS.getChildren()) {
                        String tstamp = snapshotpro.getKey();
                        ProductData p = snapshotpro.getValue(ProductData.class);
                           /* p.price=(String)(snapshotpro.child("price")).getValue();
                            p.pic_add=(String)(snapshotpro.child("pic_add")).getValue();*/
                        totalDAta.add(p);
                        homedata.add(p);
                        if (search.contains(p.getProduct_type()))
                            fav_ordData.add(new Fav_OrdData(p, sid + tstamp));
                    }
                }
            }
            adapter.notifyDataSetChanged();
            if (fav_ordData.isEmpty()) {
                Toast.makeText(getContext(), "No Product in this Range", Toast.LENGTH_SHORT).show();
            }


        }
    }

}