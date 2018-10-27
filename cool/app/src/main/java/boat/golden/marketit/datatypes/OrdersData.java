package boat.golden.marketit.datatypes;


import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class OrdersData
{
    int id;
    public String name;
    public String desc;
    public String image;
    public float price;


    public OrdersData()
    {

    }
    /* public OrdersData(OrdersData od)
     {
         this.id = od.id;
         this.name = od.name;
         this.desc=od.desc;
         this.image = od.image;
         this.price=od.price;


     }*/
    public OrdersData(int id, String name,String desc, String image,float price)
    {
        this.id = id;
        this.name = name;
        this.desc=desc;
        this.image = image;
        this.price=price;

    }
    public void upload(DatabaseReference dr)
    {
        dr.child("id").setValue(""+id);
        dr.child("name").setValue(""+name);
        // dr.child("image").setValue(""+image);
        //dr.child("price").setValue(""+price);
        //dr.child("rating").setValue(""+rating);
    }

    public int getId()
    {
        return this.id;
    }

    public String getProductName()
    {
        return name;
    }
    public String getProductDesc()
    {
        return desc;
    }
    public float getProductPrice()
    {
        return price;
    }
    public String getProductImage()
    {
        return image;
    }

    public static OrdersData readObject(DataSnapshot ss)
    {

        OrdersData p = new OrdersData();
        p.id = Integer.parseInt((String)ss.child("itemno").getValue());
        p.desc = (String)ss.child("itemdescription").getValue();
        p.price = Float.parseFloat((String)ss.child("itemcost").getValue());
        p.image = ((String)ss.child("itemimage").getValue());
        p.name = (String)ss.child("itemname").getValue();


        // p.rating = Float.parseFloat((String)ss.child("rating").getValue());
        //p.image = Integer.parseInt((String)ss.child("image").getValue());
        return p;
    }
}
