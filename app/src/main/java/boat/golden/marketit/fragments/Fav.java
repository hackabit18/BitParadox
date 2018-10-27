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
import boat.golden.marketit.adapters.ProductAdapter;
import boat.golden.marketit.datatypes.ProductData;


public class Fav extends Fragment {

    RecyclerView recyclerView;
    ArrayList<ProductData> favdata;
    FirebaseDatabase database;
    FirebaseUser user;
    ArrayList<String> fav_array=new ArrayList<String>();
    String item_name;
    SharedPreferences sharedPreferences;
    DatabaseReference fav_Ref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_fav,container,false);
        recyclerView = v.findViewById(R.id.recycler_fav);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        favdata = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("SHOPS");
        user = FirebaseAuth.getInstance().getCurrentUser();
        fav_Ref = database.getReference("Users/" + user.getUid() + "/Fav/");

        fav_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*try*/ {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String name = snapshot.getKey();
                        if (name != null) {
                            fav_array.add(name);
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

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        DataSnapshot snapshotPRODUCTS = snapshot.child("PRODUCTS");
                        for (DataSnapshot snapshotpro : snapshotPRODUCTS.getChildren()) {
                            ProductData p = snapshotpro.getValue(ProductData.class);
                            if(p != null && fav_array.contains(p.product_name)) {
                                favdata.add(p);
                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }

//                mAdapter = new ProductAdapter(homedata,getContext());
//                recyclerView.setAdapter(mAdapter);
                ProductAdapter adapter = new ProductAdapter(favdata, getContext(), new ProductAdapter.OnItemClickListener() {
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
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return  v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if(sharedPreferences.getBoolean("cart", false)){
                fav_Ref.child(user.getUid()).child("Orders").child(item_name).setValue("Ordered");
            }
            else{
                fav_Ref.child(user.getUid()).child("Orders").child(item_name).removeValue();
            }

            if(sharedPreferences.getBoolean("fav", false)){
                fav_Ref.child(user.getUid()).child("Fav").child(item_name).setValue("Fav");
            }
            else{
                fav_Ref.child(user.getUid()).child("Fav").child(item_name).removeValue();
            }
        }
    }
}