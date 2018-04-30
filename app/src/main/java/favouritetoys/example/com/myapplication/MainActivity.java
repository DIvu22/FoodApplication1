package favouritetoys.example.com.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import favouritetoys.example.com.myapplication.Common.Common;
import favouritetoys.example.com.myapplication.Model.User;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    Button signUp, signIn;
    TextView slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUp = (Button) findViewById(R.id.btn_signup);
        signIn = (Button) findViewById(R.id.btn_signin);
        slogan = (TextView) findViewById(R.id.slogan);

        Paper.init(this);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signIn = new Intent(MainActivity.this, SignIn.class);
                startActivity(signIn);

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(MainActivity.this, SignUp.class);
                startActivity(signUp);
            }
        });

        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);

        if (user != null && pwd != null)

        {
            if (!user.isEmpty() && !pwd.isEmpty()) {
                login(user, pwd);
            }
        }

    }

    private void login(final String phone, final String pwd) {

        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        final DatabaseReference dr = fd.getReference("User");

        if (Common.isConnectedToInternet(getBaseContext())) {

            final ProgressDialog mpd = new ProgressDialog(MainActivity.this);
            mpd.setMessage("loading...");
            mpd.show();

            dr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(phone).exists()) {


                        mpd.dismiss();
                        User user = dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone);

                        if (user.getPassword().equals(pwd)) {

                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
                            finish();

                        } else {
                            Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mpd.dismiss();
                        Toast.makeText(MainActivity.this, "User doesn't exists", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            Toast.makeText(MainActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

    }
}
