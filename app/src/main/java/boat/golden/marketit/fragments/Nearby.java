package boat.golden.marketit.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import boat.golden.marketit.R;
import boat.golden.marketit.adapters.ShopAdapter;
import boat.golden.marketit.datatypes.ShopData;

public class Nearby extends Fragment {

    RecyclerView recyclerView;
    ArrayList<ShopData> data;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_near_by,container,false);
        recyclerView=v.findViewById(R.id.recycler_nearby);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        data =new ArrayList<>();
        data.add(new ShopData("A to Z Store",null,null,null,null,"A Test Shop",0,0,"SHOP"));
        data.add(new ShopData("A to Z Store",null,null,null,null,"A Test Shop",0,0,"SHOP"));
        data.add(new ShopData("A to Z Store",null,null,null,null,"A Test Shop",0,0,"SHOP"));
        data.add(new ShopData("A to Z Store",null,null,null,null,"A Test Shop",0,0,"SHOP"));
        data.add(new ShopData("A to Z Store",null,null,null,null,"A Test Shop",0,0,"SHOP"));
        data.add(new ShopData("A to Z Store",null,null,null,null,"A Test Shop",0,0,"SHOP"));
        data.add(new ShopData("A to Z Store",null,null,null,null,"A Test Shop",0,0,"SHOP"));
        data.add(new ShopData("A to Z Store",null,null,null,null,"A Test Shop",0,0,"SHOP"));
        data.add(new ShopData("A to Z Store",null,null,null,null,"A Test Shop",0,0,"SHOP"));





        ShopAdapter adapter=new ShopAdapter(data);
        recyclerView.setAdapter(adapter);


        return  v;

    }
}
