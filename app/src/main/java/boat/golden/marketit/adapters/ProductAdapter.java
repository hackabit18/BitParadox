package boat.golden.marketit.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import boat.golden.marketit.MainActivity;
import boat.golden.marketit.Prod_Desc;
import boat.golden.marketit.R;
import boat.golden.marketit.datatypes.ProductData;



public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.viewholder> {

    ArrayList<ProductData> data;
    Context context;
    private final OnItemClickListener listener;

    public ProductAdapter(ArrayList<ProductData> data,Context context, OnItemClickListener listener) {
        this.data = data;
        this.context=context;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        if (holder.getItemViewType()!=1) {
            holder.product_name.setText(data.get(position).getProduct_name());
            holder.product_desc.setText(data.get(position).getProduct_desc());
            holder.amount.setText(data.get(position).getPrice());
            holder.bind(data.get(position), listener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(data.get(position).getProduct_type().equals("TOP_SEARCH")){return 1;}
        return 2;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (viewType){
            case 1:
                v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_search_it, parent, false);
                break;
            case 2:
                v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_products, parent, false);
                break;
            default:
                v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_products, parent, false);

        }
        viewholder vv = new viewholder(v);
        return vv;

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class viewholder extends RecyclerView.ViewHolder
    {
        TextView product_name,product_desc,amount;
        ImageView image;
        Button button;

        public viewholder(View itemView) {

            super(itemView);
            product_name=itemView.findViewById(R.id.product_name);
            product_desc=itemView.findViewById(R.id.product_desc);
            amount=itemView.findViewById(R.id.amount);
            image=itemView.findViewById(R.id.product_pic);
            button=itemView.findViewById(R.id.searchit);

        }

        public void bind(final ProductData item, final OnItemClickListener listener) {
            //name.setText(item.name);
            //Picasso.with(itemView.getContext()).load(item.imageUrl).into(image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }


    }

    public interface OnItemClickListener {
        void onItemClick(ProductData item);
    }
}