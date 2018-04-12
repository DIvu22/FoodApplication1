package favouritetoys.example.com.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import favouritetoys.example.com.myapplication.Common.Common;
import favouritetoys.example.com.myapplication.Model.User;

public class SignIn extends AppCompatActivity {
    MaterialEditText  editphone,password;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        editphone=(MaterialEditText)findViewById(R.id.editPhone);
        password=(MaterialEditText)findViewById(R.id.password);
        btn=(Button)findViewById(R.id.btn_SignIn);

        FirebaseDatabase fd=FirebaseDatabase.getInstance();
        final DatabaseReference dr= fd.getReference("User");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mpd=new ProgressDialog(SignIn.this);
                mpd.setMessage("loading...");
                mpd.show();

                dr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(editphone.getText().toString()).exists()) {


                            mpd.dismiss();
                            User user = dataSnapshot.child(editphone.getText().toString()).getValue(User.class);

                            if (user.getPassword().equals(password.getText().toString())) {

                                Intent homeIntent=new Intent(SignIn.this,Home.class);
                                Common.currentUser=user;
                                startActivity(homeIntent);
                                finish();

                            } else {
                                Toast.makeText(SignIn.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        else
                        {
                            mpd.dismiss();
                            Toast.makeText(SignIn.this,"User doesn't exists",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
