package boat.golden.marketit.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import boat.golden.marketit.R;
import boat.golden.marketit.datatypes.ShopData;


public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.viewholder> {

    ArrayList<ShopData> data;

    public ShopAdapter(ArrayList<ShopData> data) {
        this.data = data;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.shop_name.setText(data.get(position).getShop_name());
        holder.shop_desc.setText(data.get(position).getShort_desc());
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_shops,parent,false);
        viewholder vv=new viewholder(v);
        return vv;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class viewholder extends RecyclerView.ViewHolder{
        TextView shop_name,shop_desc;
        ImageView image;

        public viewholder(View itemView) {

            super(itemView);
            shop_name=itemView.findViewById(R.id.shop_name);
            shop_desc=itemView.findViewById(R.id.shop_desc);
            image=itemView.findViewById(R.id.shop_pic);

        }


    }
}
