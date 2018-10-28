package boat.golden.marketit.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import boat.golden.marketit.Prod_Desc;
import boat.golden.marketit.R;
import boat.golden.marketit.adapters.FavAdapter;
import boat.golden.marketit.adapters.ProductAdapter;
import boat.golden.marketit.datatypes.Fav_OrdData;
import boat.golden.marketit.datatypes.ProductData;


public class Orders extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Fav_OrdData> orddata;
    FirebaseDatabase database;
    FirebaseUser user;
    ArrayList<String> ord_array=new ArrayList<String>();
    String item_name;
    SharedPreferences sharedPreferences;
    DatabaseReference ord_Ref;
    String key;
    TextView net_price;
    public static float s=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_orders,container,false);
        recyclerView = v.findViewById(R.id.cart_orders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        orddata = new ArrayList<>();
        net_price = v.findViewById(R.id.tot_amt);

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("SHOPS");
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            ord_Ref = database.getReference("Users/" + user.getUid() + "/Orders/");

            ord_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    /*try*/
                    {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String name = snapshot.getKey();
                            if (name != null) {
                                ord_array.add(name);
                            }
                        }
                    } /*catch (Exception e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }*/
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            Toast.makeText(getContext(), "Login First", Toast.LENGTH_SHORT).show();
        }

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String sid = snapshot.getKey();
                        DataSnapshot snapshotPRODUCTS = snapshot.child("PRODUCTS");
                        for (DataSnapshot snapshotpro : snapshotPRODUCTS.getChildren()) {
                            String tStamp = snapshotpro.getKey();
                            ProductData p = snapshotpro.getValue(ProductData.class);
                            key = sid + tStamp;
                            if(p != null && ord_array.contains(sid+tStamp)) {
                                orddata.add(new Fav_OrdData(p, sid + tStamp));
                                s += Float.parseFloat(p.price);
                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }

//                mAdapter = new ProductAdapter(homedata,getContext());
//                recyclerView.setAdapter(mAdapter);
                FavAdapter adapter = new FavAdapter(orddata, getContext(),2);
                        //new ProductAdapter.OnItemClickListener() {
               //     @Override
               //     public void onItemClick(Fav_OrdData item) {
//                        Toast.makeText(getContext(), item.getProductData().product_name, Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(getActivity(), Prod_Desc.class);
//                        i.putExtra("item_name", item.getProductData().product_name);
//                        i.putExtra("item_desc", item.getProductData().product_desc);
//                        i.putExtra("item_type", item.getProductData().product_type);
//                        i.putExtra("item_img", item.getProductData().pic_add);
//                        i.putExtra("item_price", item.getProductData().price);
//                        item_name = item.getProductData().product_name;
//                        sharedPreferences = getContext().getSharedPreferences(key, Context.MODE_PRIVATE);
//                        startActivityForResult(i, 100);
                    //}
               // });
                recyclerView.setAdapter(adapter);
                net_price.setText(String.valueOf(s));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return  v;

    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if(sharedPreferences.getBoolean("cart", false)){
                ord_Ref.child(user.getUid()).child("Orders").child(key).setValue("Ordered");
            }
            else{
                ord_Ref.child(user.getUid()).child("Orders").child(key).removeValue();
            }

            if(sharedPreferences.getBoolean("fav", false)){
                ord_Ref.child(user.getUid()).child("Fav").child(key).setValue("Fav");
            }
            else{
                ord_Ref.child(user.getUid()).child("Fav").child(key).removeValue();
            }
        }
    }*/
}
