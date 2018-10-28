package boat.golden.marketit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.zip.Inflater;

import android.net.Uri;

import boat.golden.marketit.datatypes.ProductData;
import is.arontibo.library.ElasticDownloadView;


public class SellerMainActivity extends AppCompatActivity {
RecyclerView recyclerView;
EditText a,b,price;
Button category;
RadioGroup radioGroup;
Button c;
FirebaseDatabase database;
ImageView imageView;
Boolean isimage;

String UID;
DatabaseReference reference;
    AlertDialog.Builder builder2;
    FloatingActionButton fab;
    AlertDialog dialogg;
Uri uri;
Bitmap bitmap;
String uid;
ElasticDownloadView mElasticDownloadView;
FirebaseRecyclerAdapter<ProductData, viewHolder> firebaseRecyclerAdapter;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK&&requestCode==101){
            try {
                uri = data.getData();
                ContentResolver resolver = SellerMainActivity.this.getContentResolver();
                bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);

                imageView.setImageBitmap(bitmap);
                isimage=true;
                //upload();
            } catch (IOException e) {

                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);
        recyclerView=findViewById(R.id.seller_recycler);
        isimage=false;
        fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }
        });
        SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
        if (!prefs.getBoolean("is_seller_first",false)){
        startActivity(new Intent(SellerMainActivity.this,SellerFirst.class));
        }


        uid=prefs.getString("UID","NULL");
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("SHOPS/"+uid+"/PRODUCTS");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        database = FirebaseDatabase.getInstance();


        builder2=new AlertDialog.Builder(this);

        View vv=getLayoutInflater().inflate(R.layout.dialog_elastic_upload,null);
        builder2.setView(vv);

        mElasticDownloadView=vv.findViewById(R.id.elastic_download_view);
        dialogg=builder2.create();
        dialogg.setCancelable(false);
        dialogg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    @Override
    protected void onStart() {
        super.onStart();

        database = FirebaseDatabase.getInstance();
        reference=database.getReference("SHOPS/"+uid+"/PRODUCTS");
        Query query = reference.orderByChild("product_name");//.startAt(loadPreferences()).endAt(loadPreferences());

        firebaseRecyclerAdapter= new
                FirebaseRecyclerAdapter<ProductData, viewHolder>(ProductData.class, R.layout.seller_products_card, viewHolder.class, query) {



                    @Override
                    protected void populateViewHolder(final viewHolder viewholder, final ProductData model, int position) {
                   viewholder.productname.setText(model.getProduct_name());
                   viewholder.productdesc.setText(model.getProduct_desc());
                   viewholder.give_id(firebaseRecyclerAdapter.getRef(position).getKey());


                        if (model.getPic_add().equals("NULL")){
                       //Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.ic_center_focus_weak_black_24dp);
                       viewholder.productimage.setImageResource(R.drawable.ic_center_focus_weak_black_24dp);
                       //Glide.with(getBaseContext()).load(bitmap).into(viewholder.productimage);
                   }else {
                       Uri uri=Uri.parse(model.getPic_add());
                       Glide.with(getBaseContext()).load(uri).into(viewholder.productimage);
                   }


                   viewholder.delete.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {

                           if (model.getPic_add().equals("NULL")){
                               database = FirebaseDatabase.getInstance();
                               DatabaseReference livemart = database.getReference("SHOPS/" + uid + "/PRODUCTS/" + viewholder.takeid());
                               livemart.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if (task.isSuccessful()) {
                                           Toast.makeText(getBaseContext(), "Product deleted", Toast.LENGTH_SHORT).show();
                                       } else
                                           Toast.makeText(getBaseContext(), "Product delete Failed", Toast.LENGTH_SHORT).show();
                                   }
                               });

                           }
                           else {
                               StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                               SharedPreferences prefs = SellerMainActivity.this.getSharedPreferences("login", Context.MODE_PRIVATE);
                               String UID = prefs.getString("UID", "Random");
                               StorageReference ref = storageReference.child("SHOPS/" + UID + "/" + viewholder.takeid() + ".jpg");
                               ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       database = FirebaseDatabase.getInstance();
                                       DatabaseReference livemart = database.getReference("SHOPS/" + uid + "/PRODUCTS/" + viewholder.takeid());
                                       livemart.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if (task.isSuccessful()) {
                                                   Toast.makeText(getBaseContext(), "Product deleted", Toast.LENGTH_SHORT).show();
                                               } else
                                                   Toast.makeText(getBaseContext(), "Product delete Failed", Toast.LENGTH_SHORT).show();
                                           }
                                       });
                                   }
                               });

                           }


                       }
                   });

                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);



    }

    public void showdialog() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SellerMainActivity.this);
        final View dialogView = View.inflate(SellerMainActivity.this,R.layout.dialog_seller_upload,null);
        dialogBuilder.setView(dialogView);

        a = (EditText) dialogView.findViewById(R.id.projectname);
        b = (EditText) dialogView.findViewById(R.id.projectdesc);
        price = (EditText) dialogView.findViewById(R.id.productprice );
        category=dialogView.findViewById(R.id.typespinner);
        radioGroup=dialogView.findViewById(R.id.rGrup);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category.setVisibility(View.GONE);
                radioGroup.setVisibility(View.VISIBLE);
            }
        });

        //d = dialogView.findViewById(R.id.productprice);
/*        objtype = dialogView.findViewById(R.id.typespinner);
        objtype.setOnItemSelectedListener(this);

//Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, objects);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        objtype.setAdapter(aa);*/

        imageView = (ImageView) dialogView.findViewById(R.id.imageview);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, 101);
            }
        });

        dialogBuilder.setTitle("Upload your Product");
        dialogBuilder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


                final DatabaseReference livemart;
                SharedPreferences prefs = SellerMainActivity.this.getSharedPreferences("login", Context.MODE_PRIVATE);
                UID = prefs.getString("UID", "Random");
                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                database=FirebaseDatabase.getInstance();
                livemart = database.getReference("SHOPS/" + UID +"/PRODUCTS/"+ts);
                final RadioButton radioButton=dialogView.findViewById(radioGroup.getCheckedRadioButtonId());


                if (a.getText().toString().length() > 0 && b.getText().toString().length() > 0  ) {



                    if (isimage){

                        dialogg.show();
                        mElasticDownloadView.startIntro();



                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        final StorageReference  ref = storageReference.child("SHOPS/" + UID +"/"+ ts + ".jpg");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 15, baos);
                        byte[] data = baos.toByteArray();

                       UploadTask uploadTask = ref.putBytes(data);
                       Task<Uri> task=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                           @Override
                           public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                               if (!task.isSuccessful()) {
                                   throw task.getException();
                               }

                               // Continue with the task to get the download URL
                               return ref.getDownloadUrl();
                           }

                       }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                           @Override
                           public void onComplete(@NonNull Task<Uri> task) {
                               if (task.isSuccessful()) {
                                   Uri downloadUri = task.getResult();
                                   ProductData localdata=new ProductData(a.getText().toString(),b.getText().toString(),radioButton.getText().toString(),downloadUri.toString(),price.getText().toString());

                                livemart.setValue(localdata, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                        SharedPreferences prefs = SellerMainActivity.this.getSharedPreferences("login", Context.MODE_PRIVATE);
                                        Toast.makeText(SellerMainActivity.this, "Product Uploaded", Toast.LENGTH_SHORT).show();
                               }});} else {
                                  Toast.makeText(getApplicationContext(),"Product Upload Failed",Toast.LENGTH_LONG).show();
                                   dialogg.setCancelable(true);
                                   mElasticDownloadView.fail();


                               }
                           }
                       });
                       uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                mElasticDownloadView.setProgress((float)progress);
                                if (progress==100){
                                    mElasticDownloadView.success();
                                    dialogg.setCancelable(true);

                                }

                            }
                        });
//                        uploadTask.addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(SellerMainActivity.this,"Upload Failed",Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                Toast.makeText(SellerMainActivity.this,"Image Uploaded",Toast.LENGTH_SHORT).show();
//                                String downloadUrl = ref.getDownloadUrl().toString();
//                                Log.e("tagg",downloadUrl);
//                                ProductData localdata=new ProductData(a.getText().toString(),b.getText().toString(),"NULL",downloadUrl,price.getText().toString());
//
//                                livemart.setValue(localdata, new DatabaseReference.CompletionListener() {
//                                    @Override
//                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//
//                                        SharedPreferences prefs = SellerMainActivity.this.getSharedPreferences("login", Context.MODE_PRIVATE);
//                                        Toast.makeText(SellerMainActivity.this, "Product Uploaded", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                });
//                            }
//                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
//                                        .getTotalByteCount());
//                            }
//                        });


                    }else {




                        ProductData localdata=new ProductData(a.getText().toString(),b.getText().toString(),radioButton.getText().toString(),"NULL",price.getText().toString());

                        livemart.setValue(localdata, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                SharedPreferences prefs = SellerMainActivity.this.getSharedPreferences("login", Context.MODE_PRIVATE);
                                Toast.makeText(SellerMainActivity.this, "Product Uploaded", Toast.LENGTH_SHORT).show();
                            }});



                    }















                } else {
                    Toast.makeText(SellerMainActivity.this, "Upload Failed : \nEnter All Fields correctly", Toast.LENGTH_SHORT).show();
                }


            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_shop: startActivity(new Intent(SellerMainActivity.this,SellerFirst.class));       break;
            case R.id.sold_items:    startActivity(new Intent(SellerMainActivity.this,SoldItems.class));        break;
            case R.id.current_orders:       startActivity(new Intent(SellerMainActivity.this,CurrentOrders.class));     break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.seller_main_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }



    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView productname,productdesc;
        String id;
        ImageView productimage,delete;
        public void give_id(String iid){id=iid;}
        public String takeid(){return id;}
        public viewHolder(final View itemView) {

            super(itemView);
            productname=itemView.findViewById(R.id.producttittle);
            productdesc=itemView.findViewById(R.id.productdesc);
            productimage=itemView.findViewById(R.id.productimage);
            delete=itemView.findViewById(R.id.verticon);
        }

    }
}
