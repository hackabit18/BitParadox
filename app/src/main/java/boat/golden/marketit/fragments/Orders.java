package boat.golden.marketit.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


import boat.golden.marketit.R;
import boat.golden.marketit.adapters.OrdersAdapter;

import boat.golden.marketit.datatypes.OrdersData;



public class Orders extends Fragment {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private StorageReference mStorageRef;
    private Handler handler;


    ArrayList<OrdersData> ordersdata;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.fragment_orders,container,false);
        mRecyclerView=v.findViewById(R.id.recycler_orders);
        // mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        // mRecyclerView.setDrawingCacheEnabled(true);
        // mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        ordersdata =new ArrayList<>();
        ordersdata.add(new OrdersData(0," item_0","This is a very necessary daily use Product .It is a branded product from one of the worlds most trusted brand Modicare.",null,0.0f));
        ordersdata.add(new OrdersData(1," item_1","This is a very necessary daily use Product",null,10.0f));
        ordersdata.add(new OrdersData(2," item_2","This is a very necessary daily use Product",null,20f));
        ordersdata.add(new OrdersData(3," item_3","This is a very necessary daily use Product",null,30f));
        ordersdata.add(new OrdersData(4," item_4","This is a very necessary daily use Product",null,40f));
        ordersdata.add(new OrdersData(5," item_5","This is a very necessary daily use Product",null,50f));
        ordersdata.add(new OrdersData(6," item_6","This is a very necessary daily use Product",null,60f));
        ordersdata.add(new OrdersData(7," item_7","This is a very necessary daily use Product",null,70f));
        ordersdata.add(new OrdersData(8," item_8","This is a very necessary daily use Product",null,80f));
        ordersdata.add(new OrdersData(9," item_9","This is a very necessary daily use Product",null,90f));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Orders");


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                try
                {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {

                        OrdersData p=snapshot.getValue(OrdersData.class);


                        p.name=(String)(snapshot.child("itemname")).getValue();
                        p.desc=(String)(snapshot.child("itemdescription")).getValue();
                        p.price=Float.parseFloat((String.valueOf(snapshot.child("itemcost").getValue())));
                        ordersdata.add(p);

                    }
                }
                catch(Exception e)
                {
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                }

                mAdapter = new OrdersAdapter(ordersdata,getContext());
                mRecyclerView.setAdapter(mAdapter);



            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });





        OrdersAdapter adapter=new OrdersAdapter(ordersdata,getContext());
        mRecyclerView.setAdapter(adapter);
        return  v;

    }




}


