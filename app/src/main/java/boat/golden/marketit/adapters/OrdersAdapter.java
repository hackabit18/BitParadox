package boat.golden.marketit.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import boat.golden.marketit.R;
import boat.golden.marketit.datatypes.OrdersData;
import boat.golden.marketit.datatypes.ProductData;
import boat.golden.marketit.datatypes.ShopData;
import boat.golden.marketit.fragments.Home;

import static android.os.Build.VERSION_CODES.O;
import static com.facebook.FacebookSdk.getApplicationContext;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.viewholder> {

    ArrayList<OrdersData> data;
    Context context;


    public OrdersAdapter(ArrayList<OrdersData> data,Context context) {
        this.data = data;
        this.context=context;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        holder.product_name.setText(data.get(position).getProductName());

        //use linearlayout to avoid overlapping

        holder.product_price.setText(String.valueOf(data.get(position).getProductPrice()));
        holder.product_desc.setText(data.get(position).getProductDesc ());
    }


    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_orders, parent, false);
        viewholder vv = new viewholder(v);
        return vv;

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class viewholder extends RecyclerView.ViewHolder
    {
        TextView product_name,product_desc,product_price;
        ImageView image;
        Button button;

        public viewholder(View itemView)
        {

            super(itemView);
            product_name=itemView.findViewById(R.id.product_name);
            product_desc=itemView.findViewById(R.id.product_desc);
            product_price=itemView.findViewById(R.id.product_price);
            image=itemView.findViewById(R.id.product_pic);

            Button b=itemView.findViewById(R.id.cross_button);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAt(getPosition());
                }
            });

        }
        public void removeAt(int position) {
            data.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, data.size());
        }


    }
}