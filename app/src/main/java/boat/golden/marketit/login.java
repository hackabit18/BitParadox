package boat.golden.marketit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    SignInButton glogin;
    GoogleSignInOptions options;
    TextView textView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mUsersDatabaseReference;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging In");
        progressDialog.setCancelable(false);
        configureSignIn();


        textView = findViewById(R.id.login_skip);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("islogin", "false");
                editor.apply();
                finish();
            }
        });
        glogin = (SignInButton) findViewById(R.id.glogin);
        glogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "click");
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, 102);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 102)
            Log.e("result", "102");
        Log.e("result", String.valueOf(resultCode));

        //doubt---------/


        if (resultCode == RESULT_OK) {
            if (requestCode == 102) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    GoogleSignInAccount account = result.getSignInAccount();
                    FirebaseAuthWithGoogle(account);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void FirebaseAuthWithGoogle(final GoogleSignInAccount account) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging In");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("ID", account.getEmail());
                            editor.putString("UID", user.getUid());
                            editor.putString("name", account.getDisplayName());
                            editor.putInt("PUN", 0);

                            editor.putString("islogin", "true");
                            editor.apply();
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            mUsersDatabaseReference = firebaseDatabase.getReference("Users");
                            mUsersDatabaseReference.child(user.getUid()).child("Orders").setValue(0);
                            mUsersDatabaseReference.child(user.getUid()).child("Fav").setValue(0);
                            finish();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), account.getEmail(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }


    public void configureSignIn() {

// Configure sign-in to request the user’s basic profile like name and email
        options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                .build();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}