package com.example.causefairy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText etEmail, etPass;
    Button btnLogin, btnResendCode;
    TextView tvReg, tvfrgtPass, tvVerifyMsg;

    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        tvReg = findViewById(R.id.tvReg);
        btnLogin = findViewById(R.id.btnLogin);
        tvfrgtPass = findViewById(R.id.tvfrgtPass);
        progressDialog = new ProgressDialog(this);
        btnResendCode = findViewById(R.id.btnResendCode);
        tvVerifyMsg = findViewById(R.id.tvVerifyMsg);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent d = new Intent(MainActivity.this, SplashScreen.class);
                    startActivity(d);
                }
            }
        };
       /* tvfrgtPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent f = new Intent(MainActivity.this, Reset_Password.class);
                startActivity(f);
            }
        });
*/
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

      /*  btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass;

                email = etEmail.getText().toString();
                pass = etPass.getText().toString();

                if (email.equals("")) {
                    etEmail.setError("Please Enter Email");
                } else if (!email.contains("@")) {
                    etEmail.setError("Please Enter a valid Email Address");
                } else if (pass.equals("")) {
                    etPass.setError("Please Enter Password");
                }
                //mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                //   @Override
                //   public void onComplete(@NonNull Task<AuthResult> task) {
                //      if(!task.isSuccessful())
                //      {
                //          Toast.makeText(MainActivity.this,"Sign In Problem", Toast.LENGTH_LONG).show();
                //       }
                //      else
                //      {
                //           FirebaseUser user = mAuth.getCurrentUser();

                //        }
                //  }
                //});
                else if (email.equals("noumanejaz828@gmail.com") && pass.equals("1234")) {
                    Intent d = new Intent(MainActivity.this, HomePage.class);
                    startActivity(d);
                } else {
                    Toast.makeText(MainActivity.this, "Incorrect Username or Password!", Toast.LENGTH_SHORT).show();
                }
            }
        }); */

        tvReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignUp.class);
                startActivity(i);
                finish();

            }
        });
        /*
        tvfrgtPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your email to receive the Reset Link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Rest Link has been sent to your email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error! Link not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
               /* passwordResetDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        }); */
    }
    private void Login(){
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();
        if(TextUtils.isEmpty(email)){
            etEmail.setError("Enter your Email");
            return;
        }
        else if(TextUtils.isEmpty(password)) {
            etPass.setError("Enter your Password");
            return;
        }
        progressDialog.setMessage("PLease Wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Sign In Successfull!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, HomePage.class );
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this, "Sign In Failed!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }
}