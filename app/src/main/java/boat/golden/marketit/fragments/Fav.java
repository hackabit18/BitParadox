package boat.golden.marketit.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import boat.golden.marketit.R;

/**
 * Created by Vipin soni on 01-10-2018.
 */

public class Fav extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_fav,container,false);



        return  v;

    }
}
