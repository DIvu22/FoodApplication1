package favouritetoys.example.com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button signUp,signIn;
    TextView slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUp=(Button)findViewById(R.id.btn_signup);
        signIn=(Button)findViewById(R.id.btn_signin);
        slogan=(TextView)findViewById(R.id.slogan);



        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signIn= new Intent(MainActivity.this,SignIn.class);
                startActivity(signIn);

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp=new Intent(MainActivity.this,SignUp.class);
                startActivity(signUp);
            }
        });


    }
}
