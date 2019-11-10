package com.transports.account_management;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.transports.MainActivity;
import com.transports.R;
import com.transports.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import static com.transports.data.URLs.PAYMENTS_CREATE_ACCOUNT;
import static com.transports.utils.Constants.PAYMENT_USER_ID;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FirebaseApp.initializeApp(this);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(Constants.MENU_INTENT, R.id.bottom_menu_tickets);//start in the "my tickets menu"
            startActivity(intent);
            finish();
        }

        // set the view now
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    //login success
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra(Constants.MENU_INTENT, R.id.bottom_menu_tickets);//start in the "my tickets menu"
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    private void loginInUserPayments(String firebaseID){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //create list of request ticket creation json objects
        JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put(PAYMENT_USER_ID, firebaseID);
        } catch (JSONException e){ }


        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonArrayRequest  = new JsonObjectRequest(
                Request.Method.POST,
                PAYMENTS_CREATE_ACCOUNT,
                jsonBody,
                new Response.Listener<JSONObject >() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //TODO: store in shared preferences the token of the user of payments
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("errorPayment", error+"");
                        Toast.makeText(getApplication(), "Could not register user in payment service", Toast.LENGTH_SHORT);
                        /*new AlertDialog.Builder(getApplication())
                                .setTitle(getString(R.string.ticket_purchase_error_title))
                                .setMessage(getString(R.string.ticket_purchase_error_message))
                                .setIcon(android.R.drawable.ic_dialog_alert).setNeutralButton("OK", null).show();*/
                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }
}