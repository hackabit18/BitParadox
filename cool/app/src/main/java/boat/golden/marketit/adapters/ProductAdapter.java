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
import boat.golden.marketit.datatypes.ProductData;
import boat.golden.marketit.datatypes.ShopData;
import boat.golden.marketit.fragments.Home;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Vipin soni on 01-10-2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.viewholder> {

    ArrayList<ProductData> data;
    Context context;


    public ProductAdapter(ArrayList<ProductData> data,Context context) {
        this.data = data;
        this.context=context;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        if (holder.getItemViewType()!=1) {
            holder.product_name.setText(data.get(position).getProduct_name());
            holder.product_desc.setText(data.get(position).getProduct_desc());
            holder.amount.setText(data.get(position).getPrice());
        }
        else {
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    String[] items = new String[]{
                            "Shirts",
                            "jeans",
                            "Watches",
                            "Mobiles",
                            "Sports",
                            "Biscuits",
                            "Chips",
                            "Chargers",
                            "Toys",
                            "All Other"
                    };

                    final boolean[] checkedItems = new boolean[]{
                            false,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false

                    };
                    final List<String> itemList = Arrays.asList(items);
                    builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            checkedItems[which] = isChecked;
                            String currentItem = itemList.get(which);
                        }
                    });
                    builder.setCancelable(false);
                    builder.setTitle("Select Items").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //blablablablablablablablabla
                        }
                    }).setNegativeButton("Cancel",null);
                    AlertDialog dialog=builder.create();
                    dialog.show();


                }
            });
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


    }
}
