package boat.golden.marketit.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import boat.golden.marketit.MainActivity;
import boat.golden.marketit.Prod_Desc;
import boat.golden.marketit.R;
import boat.golden.marketit.datatypes.Fav_OrdData;
import boat.golden.marketit.datatypes.ProductData;
import boat.golden.marketit.fragments.Orders;


public class FavAdapter extends RecyclerView.Adapter<FavAdapter.viewholder> {

    ArrayList<Fav_OrdData> data;
    int i;
    Context context;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    View vw;
    TextView tw;
    String key;
    SharedPreferences.Editor editor;

    public FavAdapter(ArrayList<Fav_OrdData> data, Context context, int i) {
        this.data = data;
        this.context = context;
        this.i = i;

        if (i == 1)
            databaseReference = firebaseDatabase.getReference("Users/" + user.getUid() + "/Fav/");
        else {
            databaseReference = firebaseDatabase.getReference("Users/" + user.getUid() + "/Orders/");
            vw = View.inflate(context, R.layout.fragment_orders, null);
            tw = vw.findViewById(R.id.tot_amt);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        {
            String sname = data.get(position).getProductData().getProduct_name();
            holder.product_name.setText(sname.substring(0, Math.min(20, sname.length())));
            String sdes = data.get(position).getProductData().getProduct_desc();
            holder.product_desc.setText(sdes.substring(0, Math.min(20, sdes.length())));
            //Log.e("TAG", "onBindViewHolder: "+ data.get(position).getProductData().getPrice());
            holder.amount.setText(data.get(position).getProductData().getPrice());
            key = data.get(position).getFavUid();
            if (!data.get(position).getProductData().getPic_add().equals("NULL")) {
                //ImageView imageView=(ImageView)(holder.image);
                Picasso.with(context).load(data.get(position).getProductData().pic_add).resize(200, 200).into(holder.image);
            }
            //else {holder.progressBar.setVisibility(View.INVISIBLE);}
        }
    }


    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;


        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_orders, parent, false);


        viewholder vv = new viewholder(v);
        return vv;

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView product_name, product_desc, amount;
        ImageView image;
        Button button;
        // ProgressBar progressBar;
        Button cancel;


        public viewholder(final View itemView) {

            super(itemView);
            //progressBar=itemView.findViewById(R.id.progress);
            product_name = itemView.findViewById(R.id.product_name);
            product_desc = itemView.findViewById(R.id.product_desc);
            amount = itemView.findViewById(R.id.product_price);
            image = itemView.findViewById(R.id.orders_image);
            button = itemView.findViewById(R.id.searchit);
            cancel = itemView.findViewById(R.id.cross_button);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (i != 1) {
                        //Orders.s=Orders.s-Float.parseFloat(data.get(getAdapterPosition()).getProductData().getPrice());
                        Log.e("TAG", "onClick: " + Orders.s);
                        // tw.setText(Orders.s+"");
                    }


                    databaseReference.child(data.get(getAdapterPosition()).getFavUid()).removeValue();
                    // Log.e("TAG", "onClick: "+data.get(getAdapterPosition()).getFavUid() );

                    data.remove(getAdapterPosition());


                    notifyItemRemoved(getAdapterPosition());
                    notifyItemChanged(getAdapterPosition());

                    sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);

                    editor = sharedPreferences.edit();
                    //intent1.putExtra("cart", cart);
                    if(i == 1) {
                        editor.putBoolean("fav", false);
                    }else {
                        editor.putBoolean("cart", false);
                    }
                    editor.apply();
                }
            });
        }
    }
}