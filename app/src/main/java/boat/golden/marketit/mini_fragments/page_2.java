package boat.golden.marketit.mini_fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.victor.loading.newton.NewtonCradleLoading;

import boat.golden.marketit.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class page_2 extends Fragment {

    NewtonCradleLoading newtonCradleLoading;
    EditText a,b;
    DatabaseReference reference;
    SharedPreferences sharedPreferences;

    public page_2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome_slide2, container, false);
        Button button=v.findViewById(R.id.update);
        a=v.findViewById(R.id.shopname);
        b=v.findViewById(R.id.address);
        sharedPreferences=getContext().getSharedPreferences("login",MODE_PRIVATE);
        String uid=sharedPreferences.getString("UID","NULL");
        reference=FirebaseDatabase.getInstance().getReference("SHOPS/"+uid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String aa=dataSnapshot.child("shop_name").getValue(String.class);
                String bb=dataSnapshot.child("shop_address").getValue(String.class);
                if (aa!=null)
                a.setText(aa);b.setText(bb);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (a.getText().toString().equals("")||b.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Fill Both of them",Toast.LENGTH_SHORT).show();
                }else {
                    reference.child("shop_name").setValue(a.getText().toString());
                    reference.child("shop_address").setValue(b.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(),"Info Uploaded",Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });

        return v;
    }

}
