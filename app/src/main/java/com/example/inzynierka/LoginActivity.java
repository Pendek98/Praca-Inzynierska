package com.example.inzynierka;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    SignInButton button;
    FirebaseAuth mAuth;
    GoogleSignInClient client;


    private static final int REQ_ONE_TAP = 42069;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this,googleSignInOptions);

        mAuth = FirebaseAuth.getInstance();
        Log.v("Auth",mAuth.toString());
        button = findViewById(R.id.bt_sign_in);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


    }

    public void GoogleLogOut(){

    }

    @Override
    protected void onStart() {
        super.onStart();
        client.signOut();
        try {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null)startActivity(new Intent(LoginActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            Log.v("Current User",currentUser.toString());
        } catch (Exception e) {
            Log.v("Error",e.toString());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ_ONE_TAP){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult();
                authWithGoogle(account.getIdToken());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void authWithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    Log.v("Current User",currentUser.toString());
                    startActivity(new Intent(LoginActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                else{
                    Log.v("Error","Task Failed");
                }
            }
        });
    }

    private void signIn(){
        Intent intent = client.getSignInIntent();
        startActivityForResult(intent,REQ_ONE_TAP);
    }

}