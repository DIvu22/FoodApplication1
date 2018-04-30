package favouritetoys.example.com.myapplication;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import favouritetoys.example.com.myapplication.Common.Common;
import favouritetoys.example.com.myapplication.Database.Database;
import favouritetoys.example.com.myapplication.Model.Food;
import favouritetoys.example.com.myapplication.Model.Order;

public class FoodDetail extends AppCompatActivity {
    TextView food_name, food_price, food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String foodId = "";
    FirebaseDatabase database;
    DatabaseReference food;
    Food currentFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //Firebase

        database = FirebaseDatabase.getInstance();
        food = database.getReference("Food");

        food_name = (TextView) findViewById(R.id.food_name);
        food_price = (TextView) findViewById(R.id.food_price);
        food_description = (TextView) findViewById(R.id.food_description);

        food_image = (ImageView) findViewById(R.id.img_food);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        btnCart = (FloatingActionButton) findViewById(R.id.btn_cart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(
                        new Order(
                                foodId,
                                currentFood.getName(),
                                numberButton.getNumber(),
                                currentFood.getPrice(),
                                currentFood.getDiscount()
                        )
                );
                Toast.makeText(FoodDetail.this, "Added to Cart successfully", Toast.LENGTH_SHORT).show();
            }
        });
        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);

        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExtendedAppbar);

        //Get FoodId

        if (getIntent() != null) {
            foodId = getIntent().getStringExtra("FoodId");
            if (!foodId.isEmpty()) {
                if (Common.isConnectedToInternet(getBaseContext()))
                    getDetailFood(foodId);

                else {
                    Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

    }

    private void getDetailFood(String foodId) {

        food.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

                Picasso.with(getBaseContext()).load(currentFood.getImage())
                        .into(food_image);
                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_price.setText(currentFood.getPrice());

                food_name.setText(currentFood.getName());
                food_description.setText(currentFood.getDescription());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
