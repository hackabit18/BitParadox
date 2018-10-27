package boat.golden.marketit.mini_fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.zip.Inflater;

import boat.golden.marketit.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class page_1 extends Fragment {

    NewtonCradleLoading newtonCradleLoading;
    SharedPreferences sharedPreferences;
    FirebaseDatabase database;
    DatabaseReference reference;
    public page_1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome_slide1, container, false);
        newtonCradleLoading = v.findViewById(R.id.newton_cradle_loading);
        newtonCradleLoading.setLoadingColor(R.color.colorPrimary);
        newtonCradleLoading.start();
        return v;
    }



    public int IsSeller(){
        final int[] a = new int[1];
        a[0] =0;
        sharedPreferences=getContext().getSharedPreferences("login",MODE_PRIVATE);
        String uid=sharedPreferences.getString("UID","NULL");
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("SHOPS/"+uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("shop_name")) a[0] =1;
                else a[0]=0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return a[0];
    }

}
