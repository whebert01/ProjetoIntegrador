package com.dataflair.fooddeliveryapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dataflair.fooddeliveryapp.MainActivity;
import com.dataflair.fooddeliveryapp.R;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    GoogleSignInClient mSignInClient;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressBar;
    Button signInButton;
    EditText phoneNumberEditTxt, addressEditTxt, cityNameEditTxt, pinCodeEdittxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        //Progress bar
        progressBar = new ProgressDialog(this);
        progressBar.setTitle("Please Wait...");
        progressBar.setMessage("We are setting Everything for you...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        //Assigning the address of the android materials
        cityNameEditTxt = (EditText) findViewById(R.id.CityEditText);
        phoneNumberEditTxt = (EditText) findViewById(R.id.PhoneNumberEditText);
        pinCodeEdittxt = (EditText) findViewById(R.id.PinCodeExitText);
        addressEditTxt = (EditText) findViewById(R.id.AddressEditText);
        signInButton = (Button) findViewById(R.id.GoogleSignInBtn);


        //Google Signin Options to get gmail and performa gmail login
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("319466083877-4cdnl01r8q8p84nqgtcp42tv8cn0dq12.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mSignInClient = GoogleSignIn.getClient(getApplicationContext(), googleSignInOptions);


        //Implementing OnClickListener to perform Login action
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Getting user details from the edit text
                String phoneNumber = phoneNumberEditTxt.getText().toString();
                String cityName = cityNameEditTxt.getText().toString();
                String pinCode = pinCodeEdittxt.getText().toString();
                String address = addressEditTxt.getText().toString();

                //Checking all the fields are filled or not
                if (phoneNumber.isEmpty() || cityName.isEmpty() || pinCode.isEmpty() || address.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please,Fill Details", Toast.LENGTH_SHORT).show();
                } else {
                    //Showing all Gmails
                    Intent intent = mSignInClient.getSignInIntent();
                    startActivityForResult(intent, 100);
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            if (googleSignInAccountTask.isSuccessful()) {
                progressBar.show();
                try {
                    GoogleSignInAccount googleSignInAccount = googleSignInAccountTask.getResult(ApiException.class);

                    if (googleSignInAccount != null) {
                        AuthCredential authCredential = GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken(), null);

                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference().child("users");

                                    //Hashmap to store the userdetails and setting it to fireabse
                                    HashMap<String, Object> user_details = new HashMap<>();

                                    //Accessing the user details from gmail
                                    String id = googleSignInAccount.getId().toString();
                                    String name = googleSignInAccount.getDisplayName().toString();
                                    String mail = googleSignInAccount.getEmail().toString();
                                    String pic = googleSignInAccount.getPhotoUrl().toString();


                                    String phoneNumber = phoneNumberEditTxt.getText().toString();
                                    String cityName = cityNameEditTxt.getText().toString();
                                    String pinCode = pinCodeEdittxt.getText().toString();
                                    String address = addressEditTxt.getText().toString();


                                    user_details.put("id", id);
                                    user_details.put("name", name);
                                    user_details.put("mail", mail);
                                    user_details.put("profilepic", pic);
                                    user_details.put("role", "empty");
                                    user_details.put("phoneNumber", phoneNumber);
                                    user_details.put("cityName", cityName);
                                    user_details.put("pinCode", pinCode);
                                    user_details.put("address", address);

                                    //updating the user details in firebase
                                    myRef.child(id).updateChildren(user_details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressBar.cancel();

                                                //navigating to the main activity after user successfully registers
                                                Intent intent = new Intent(getApplicationContext(), UserRoleActivity.class);
                                                //Clears older activities and tasks
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        }
                                    });

                                }
                            }
                        });
                    }

                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
