package favouritetoys.example.com.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import favouritetoys.example.com.myapplication.Common.Common;
import favouritetoys.example.com.myapplication.Common.Config;
import favouritetoys.example.com.myapplication.Database.Database;
import favouritetoys.example.com.myapplication.Model.Order;
import favouritetoys.example.com.myapplication.Model.Request;
import favouritetoys.example.com.myapplication.ViewHolder.CartAdapter;

public class Cart extends AppCompatActivity {

    private static final int PAYPAL_REQUEST_CODE = 9999;
    static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference requests;
    TextView txtTotalPrice;
    Button btnPlace;
    Request request;
    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;
    String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        //Init Paypal
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);


        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //Init
        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView) findViewById(R.id.total);
        btnPlace = (Button) findViewById(R.id.btn_PlaceOrder);
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.size() > 0)
                    showAlertDialog();
                else
                    Toast.makeText(Cart.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            }
        });

        loadListFood();

    }

    private void showAlertDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("ONE MORE STEP");
        alertDialog.setMessage("Add your address");
        final EditText edAddress = new EditText(Cart.this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edAddress.setLayoutParams(lp);
        alertDialog.setView(edAddress);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                address = edAddress.getText().toString();

                String formatAmount=txtTotalPrice.getText().toString()
                        .replace(getString(R.string.Rs), "")
                        .replaceAll("\\s+", "")


                        .replace(",","");

                float amount=Float.parseFloat(formatAmount);

                PayPalPayment payPalPayment=new PayPalPayment(new BigDecimal(formatAmount),
                        "INR",
                        "EAT IT Order",
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent=new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
                startActivityForResult(intent,PAYPAL_REQUEST_CODE);

            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if(requestCode==PAYPAL_REQUEST_CODE)
            {
                if (resultCode == RESULT_OK)
                {
                    PaymentConfirmation confirmation =data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                    if(confirmation!=null)
                    {
                        try
                        {
                            String paymentDetail=confirmation.toJSONObject().toString(4);
                            JSONObject jsonObject=new JSONObject(paymentDetail);

                            request = new Request(
                                    jsonObject.getJSONObject("response").getString("state"),
                                    Common.currentUser.getPhone(),
                                    Common.currentUser.getName(),
                                    address,
                                    txtTotalPrice.getText().toString(),
                                    cart
                            );

                            //submit to firebase
                            requests.child(String.valueOf(System.currentTimeMillis()))
                                    .setValue(request);
                            //delete cart
                            new Database(getBaseContext()).cleanCart();
                            Toast.makeText(Cart.this, "Thankyou ! order placed", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                else if(resultCode== Activity.RESULT_CANCELED)
                    Toast.makeText(this,"Payment cancelled",Toast.LENGTH_SHORT).show();
                else if(resultCode==PaymentActivity.RESULT_EXTRAS_INVALID)
                    Toast.makeText(this,"Invalid Payment",Toast.LENGTH_SHORT).show();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void loadListFood() {

        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


        //calculate total price
        int total = 0;
        for (Order order : cart)

            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));

        Locale locale = new Locale("en", "IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;
    }

    private void deleteCart(int position) {
        //wE REMOVE ITEM AT List<Order>by position
        cart.remove(position);
        //now we delete all old data from SQLite
        new Database(this).cleanCart();

        //now we will upload new data from List<Order>to SQLite
        for (Order item : cart)

            new Database(this).addToCart(item);
        loadListFood();
    }
}
