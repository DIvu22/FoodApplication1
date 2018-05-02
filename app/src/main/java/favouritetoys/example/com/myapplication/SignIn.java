package favouritetoys.example.com.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import favouritetoys.example.com.myapplication.Common.Common;
import favouritetoys.example.com.myapplication.Model.User;
import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {
    MaterialEditText editphone, password;
    Button btn;
    com.rey.material.widget.CheckBox ckbRemember;

    TextView txtForgotPassword;
    FirebaseDatabase fd;
    DatabaseReference dr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        editphone = (MaterialEditText) findViewById(R.id.editPhone);
        password = (MaterialEditText) findViewById(R.id.password);
        btn = (Button) findViewById(R.id.btn_SignIn);
        ckbRemember = (com.rey.material.widget.CheckBox) findViewById(R.id.ckbRemember);
        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPwd);

        Paper.init(this);

        fd = FirebaseDatabase.getInstance();
        dr = fd.getReference("User");
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPasswordDisplay();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    if (ckbRemember.isChecked()) {
                        Paper.book().write(Common.USER_KEY, editphone.getText().toString());
                        Paper.book().write(Common.PWD_KEY, password.getText().toString());
                    }


                    final ProgressDialog mpd = new ProgressDialog(SignIn.this);
                    mpd.setMessage("loading...");
                    mpd.show();

                    dr.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(editphone.getText().toString()).exists()) {


                                mpd.dismiss();
                                User user = dataSnapshot.child(editphone.getText().toString()).getValue(User.class);
                                user.setPhone(editphone.getText().toString());

                                if (user.getPassword().equals(password.getText().toString())) {

                                    Intent homeIntent = new Intent(SignIn.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();

                                } else {
                                    Toast.makeText(SignIn.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                mpd.dismiss();
                                Toast.makeText(SignIn.this, "User doesn't exists", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(SignIn.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void showForgotPasswordDisplay() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password");
        builder.setMessage("Enter your code");

        LayoutInflater inflater = this.getLayoutInflater();
        View forgotpwd = inflater.inflate(R.layout.forgot_password_layout, null);

        builder.setView(forgotpwd);
        builder.setIcon(R.drawable.ic_security_black_24dp);

        final MaterialEditText edtPhone = (MaterialEditText) forgotpwd.findViewById(R.id.editPhone);
        final MaterialEditText edtSecureCode = (MaterialEditText) forgotpwd.findViewById(R.id.editSecureCode);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.child(edtPhone.getText().toString())
                                .getValue(User.class);
                        if (user.getSecureCode().equals(edtSecureCode.getText().toString()))
                            Toast.makeText(SignIn.this, "Your password :" + user.getPassword(), Toast.LENGTH_LONG).show();

                        else
                            Toast.makeText(SignIn.this, "Wrong secure code", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        builder.show();
    }
}
