package boat.golden.marketit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class Prod_Desc extends AppCompatActivity {
    boolean cart, fav;
    Button order, favourite;
    Intent intent1 = new Intent();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod__desc);
        Toast.makeText(this, "launched", Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String name = bundle.getString("item_name");
        String desc = bundle.getString("item_desc");
        String type = bundle.getString("item_type");
        String img = bundle.getString("item_img");

        ImageView imageView = (ImageView) findViewById(R.id.image);
        Picasso.with(this).load(img).into(imageView);

        TextView nameView= findViewById(R.id.name);
        nameView.setText(name);

        TextView descView = findViewById(R.id.desc);
        descView.setText(desc);

        TextView typeView = findViewById(R.id.type);
        typeView.setText(type);

        order = findViewById(R.id.orders);
        favourite = findViewById(R.id.favourites);

        sharedPreferences = this.getSharedPreferences(name, MODE_PRIVATE);

        cart = sharedPreferences.getBoolean("cart", false);
        fav = sharedPreferences.getBoolean("fav", false);

        changeBoolean();
    }

    public void orders(View view) {
        cart = !cart;

        editor = sharedPreferences.edit();
        //intent1.putExtra("cart", cart);
        changeBoolean();
        editor.putBoolean("cart", cart);
        editor.apply();
        //setResult(100, intent1);
        //Toast.makeText(this, "" + sharedPreferences.getBoolean("cart", false), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        //setResult(100, intent1);
        finish();
    }

    public void fav(View view) {
        fav = !fav;
        changeBoolean();
        editor = sharedPreferences.edit();
        //intent1.putExtra("fav", fav);
        editor.putBoolean("fav", fav);
        editor.apply();
        //setResult(100, intent1);
        //Toast.makeText(this, "" + sharedPreferences.getBoolean("fav", false), Toast.LENGTH_SHORT).show();
    }

    public void changeBoolean(){
        if(cart)
        {
            order.setText("remove from orders ");
        }
        else
        {
            order.setText("add to order");
        }

        if(fav)
        {
            favourite.setText("remove from favourites ");
        }
        else
        {
            favourite.setText("add to favourites");
        }
    }
}
