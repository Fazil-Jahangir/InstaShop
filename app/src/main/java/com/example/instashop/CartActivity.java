package com.example.instashop;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instashop.Database.Database;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity  extends AppCompatActivity
{
    RecyclerView recyclerView;
    List<Product> cart = new ArrayList<>();
    List<String> storeScannedItems = new ArrayList<>();
    CartListAdapter adapter;
    DatabaseReference reference;

    private Button button;

    public static int total=0;

    public static TextView tv_total;
    private static String TAG = null;
    public String barcodeValue = "null";
    public String temp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Top bar:
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("Cart");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //checkout_button
        button = (Button) findViewById(R.id.btn_placeorder);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onPause();
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                startActivity(intent);
            }
        });


        tv_total = (TextView)findViewById(R.id.tv_total);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_cart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);


        barcodeValue = getIntent().getStringExtra("BARCODE_VALUE");
        reference = FirebaseDatabase.getInstance().getReference().child("Products").child(barcodeValue);
        reference.keepSynced(true);
        addDataOnMatch();
        if(cart.size()>0)
        {
            Toast.makeText(getApplicationContext(), "Cart Empty!", Toast.LENGTH_SHORT).show();
        }
        loadList();
    }

    private void getIntentData()
    {
        if(getIntent()!=null && getIntent().getExtras()!=null)
        {
            // Get the Required Parameters for sending Order to server.
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addDataOnMatch()
    {
        if(barcodeValue.equals("nomatch"))
        {
            Toast.makeText(getApplicationContext(), "Cart!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            reference.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    Product p = dataSnapshot.getValue(Product.class);
                    assert p != null;
                    p.setQuantity("1");
                    setDBase(p);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void setDBase(Product p)
    {
        //Toast.makeText(getApplicationContext(), "Found! Item Added!", Toast.LENGTH_SHORT).show();
        //pClass.setQuantity("1");
        TAG = p.getItem_id();
        System.out.println("ITEM ID: " + TAG);
        System.out.println("Name: " + p.getName());
        System.out.println("Tax: " + p.getTax());
        System.out.println("Cost: " + p.getCost());
        System.out.println("Image: " + p.getImage());
        System.out.println("Discount: " + p.getDiscount());
        System.out.println("Quantity: " + p.getQuantity());
        //new Database(getBaseContext()).addToCart(p);

        if(storeScannedItems.contains(barcodeValue))
        {
            Toast.makeText(getApplicationContext(), "Item already exists!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            new Database(getBaseContext()).addToCart(new Product(
                    p.getID(),
                    p.getItem_id(),
                    p.getName(),
                    p.getTax(),
                    p.getCost(),
                    p.getImage(),
                    p.getDiscount(),
                    p.getQuantity()
            ));
            storeScannedItems.add(barcodeValue);
        }
    }

    private void loadList()
    {
        cart = new Database(this ).getCarts();
        adapter = new CartListAdapter(cart, this);
        recyclerView.setAdapter(adapter);

        int sum_total = 0;
        for(Product product:cart)
            sum_total+=(Integer.parseInt(product.getCost()))*(Integer.parseInt(product.getQuantity()));
        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        tv_total.setText(fmt.format(sum_total));
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if(item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;
    }

    private void deleteCart(int pos)
    {
        cart.remove(pos);
        new Database(this).cleanCart();
        for(Product item:cart)
            new Database(this).addToCart(item);
        loadList();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}