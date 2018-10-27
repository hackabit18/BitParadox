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
import boat.golden.marketit.datatypes.ProductData;


public class Home extends Fragment {

    RecyclerView recyclerView;
    Button button,mapButton;
    ProductAdapter mAdapter;
    List<String> search=new ArrayList<>();
    DataSnapshot localdataSnapshot;
    ArrayList<ProductData> homedata;
    SharedPreferences sharedPreferences;
    ArrayList<ProductData> totalDAta=new ArrayList<>();
    String item_name;
    SeekBar seekBar;
    TextView seektext;
    int seek_progress;
    String[] items;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_home,container,false);
        seek_progress=0;
        recyclerView=v.findViewById(R.id.recycler_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        homedata =new ArrayList<>();
        button=v.findViewById(R.id.searchit);

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
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        builderseekbar.setPositiveButton("OK", null).setNegativeButton("Cancel",null);
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
//                final String[] items = new String[]{
//                        "Shirts",
//                        "Jeans",
//                        "Watches",
//                        "Mobiles",
//                        "Sports",
//                        "Snacks",
//                        "Chargers",
//                        "Toys",
//                        "Others"
//                };

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

        //homedata.add(new ProductData("a","b","TOP_SEARCH","TOP_SEARCH",null));
        /*homedata.add(new ProductData("Awesome","A cksan sdnkj","PRODUCT","TOP_SEARCH",null));
        homedata.add(new ProductData("Awesome","A cksan sdnkj","PRODUCT","TOP_SEARCH",null));
        homedata.add(new ProductData("Awesome","A cksan sdnkj","PRODUCT","TOP_SEARCH",null));
        homedata.add(new ProductData("Awesome","A cksan sdnkj","PRODUCT","TOP_SEARCH",null));
        homedata.add(new ProductData("Awesome","A cksan sdnkj","PRODUCT","TOP_SEARCH",null));
        homedata.add(new ProductData("Awesome","A cksan sdnkj","PRODUCT","TOP_SEARCH",null));
        homedata.add(new ProductData("Awesome","A cksan sdnkj","PRODUCT","TOP_SEARCH",null));
        homedata.add(new ProductData("Awesome","A cksan sdnkj","PRODUCT","TOP_SEARCH",null));
        homedata.add(new ProductData("Awesome","A cksan sdnkj","PRODUCT","TOP_SEARCH",null));
        homedata.add(new ProductData("Awesome","A cksan sdnkj","PRODUCT","TOP_SEARCH",null));
        homedata.add(new ProductData("Awesome","A cksan sdnkj","PRODUCT","TOP_SEARCH",null));
        homedata.add(new ProductData("Awesome","A cksan sdnkj","PRODUCT","TOP_SEARCH",null));
        homedata.add(new ProductData("Awesome","A cksan sdnkj","PRODUCT","TOP_SEARCH",null));
        homedata.add(new ProductData("Awesome","A cksan sdnkj","PRODUCT","TOP_SEARCH",null));
        */

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("SHOPS");


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {if (dataSnapshot!=null){localdataSnapshot=dataSnapshot;}
                try
                {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {

                        DataSnapshot snapshotPRODUCTS=snapshot.child("PRODUCTS");
                        for(DataSnapshot snapshotpro:snapshotPRODUCTS.getChildren())
                        {
                            ProductData p=snapshotpro.getValue(ProductData.class);
                            totalDAta.add(p);
                            Log.e("TAG",p.getProduct_type() );
                           /* p.price=(String)(snapshotpro.child("price")).getValue();
                            p.pic_add=(String)(snapshotpro.child("pic_add")).getValue();*/
                            homedata.add(p);


                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
                catch(Exception e)
                {
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                }





            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });





        mAdapter = new ProductAdapter(homedata, getContext(), new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ProductData item) {
                Toast.makeText(getContext(), item.product_name, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), Prod_Desc.class);
                i.putExtra("item_name", item.product_name);
                i.putExtra("item_desc", item.product_desc);
                i.putExtra("item_type", item.product_type);
                i.putExtra("item_img", item.pic_add);
                i.putExtra("item_price", item.price);
                item_name = item.product_name;
                sharedPreferences = getContext().getSharedPreferences(item.product_name, Context.MODE_PRIVATE);
                startActivityForResult(i, 100);
            }
        });
        recyclerView.setAdapter(mAdapter);



        // ProductAdapter adapter=new ProductAdapter(homedata,getContext());
        // recyclerView.setAdapter(adapter);



        return  v;

    }

    private void Filterit() {

//        for (DataSnapshot snapshot : localdataSnapshot.getChildren())
//        {
//            Log.e("ok","Not Ok");
//            DataSnapshot snapshotPRODUCTS=snapshot.child("PRODUCTS");
//            homedata.clear();
//            for(DataSnapshot snapshotpro:snapshotPRODUCTS.getChildren()) {
//                ProductData p = snapshotpro.getValue(ProductData.class);
//                if (search.contains(p.getProduct_type())) {
//                    homedata.add(p);
//                }
//
//            }    }
        homedata.clear();
        for(ProductData productData:totalDAta)
        {
            if(search.contains(productData.getProduct_type()))
                homedata.add(productData);
        }


        mAdapter.notifyDataSetChanged();


    }
}