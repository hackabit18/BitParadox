package boat.golden.marketit.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import boat.golden.marketit.R;
import boat.golden.marketit.adapters.OrdersAdapter;
import boat.golden.marketit.adapters.ProductAdapter;
import boat.golden.marketit.adapters.ShopAdapter;
import boat.golden.marketit.datatypes.ProductData;
import boat.golden.marketit.datatypes.ShopData;

/**
 * Created by Vipin soni on 01-10-2018.
 */

public class Home extends Fragment {

    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    ArrayList<ProductData> homedata;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_home,container,false);
        recyclerView=v.findViewById(R.id.recycler_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        homedata =new ArrayList<>();
       // homedata.add(new ProductData("Awesome","A cksan sdnkj","TOP_SEARCH","TOP_SEARCH",null));
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
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                try
                {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {

                        DataSnapshot snapshotPRODUCTS=snapshot.child("PRODUCTS");
                        for(DataSnapshot snapshotpro:snapshotPRODUCTS.getChildren())
                        {
                            ProductData p=snapshotpro.getValue(ProductData.class);
                            p.price=(String)(snapshotpro.child("price")).getValue();
                            p.pic_add=(String)(snapshotpro.child("pic_add")).getValue();
                            homedata.add(p);

                        }








                    }
                }
                catch(Exception e)
                {
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                }

                mAdapter = new ProductAdapter(homedata,getContext());
                recyclerView.setAdapter(mAdapter);



            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });








        ProductAdapter adapter=new ProductAdapter(homedata,getContext());
        recyclerView.setAdapter(adapter);


        return  v;

    }
}
