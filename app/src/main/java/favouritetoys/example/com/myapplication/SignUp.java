package favouritetoys.example.com.myapplication;

import android.app.ProgressDialog;
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

public class SignUp extends AppCompatActivity {
    MaterialEditText editName, editPhone, editPassword, edtSecureCode;
    Button btn_Signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editName = (MaterialEditText) findViewById(R.id.editName);
        editPhone = (MaterialEditText) findViewById(R.id.editPhone);
        editPassword = (MaterialEditText) findViewById(R.id.password);

        edtSecureCode = (MaterialEditText) findViewById(R.id.secureCode);

        btn_Signup = (Button) findViewById(R.id.btn_SignUp);

        final FirebaseDatabase fd = FirebaseDatabase.getInstance();
        final DatabaseReference dr = fd.getReference("User");

        btn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isConnectedToInternet(getBaseContext())) {

                    final ProgressDialog pd = new ProgressDialog(SignUp.this);
                    pd.setMessage("Loading...");
                    pd.show();


                    dr.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child(editPhone.getText().toString()).exists()) {

                                pd.dismiss();
                                Toast.makeText(SignUp.this, "Phone Number already exists", Toast.LENGTH_SHORT).show();
                            } else {
                                pd.dismiss();
                                User user = new User(editName.getText().toString(), editPassword.getText().toString(),
                                        edtSecureCode.getText().toString());

                                dr.child(editPhone.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "Sign Up successful", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(SignUp.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
