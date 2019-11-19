package com.example.macarbi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_add_product extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Button add;
    private TextView pName, pPrice, pQuantity, pWeight;
    private Spinner prod;
    private AUD_Prod save;
    private String Cat;
    private DatabaseReference fdb;
    private FirebaseAuth fbAuth;

    private String name, price, qty, weight = "";

    private long childCount;
    private List<String> list1 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        add = findViewById(R.id.button_add);
        pName = (TextView) findViewById(R.id.product_name);
        pPrice = (TextView) findViewById(R.id.product_price);
        pQuantity = (TextView) findViewById(R.id.product_quantity);
        pWeight = (TextView) findViewById(R.id.product_weight);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                       addProduct();



            }
        });


        pName.setHint("Name");
        pPrice.setHint("Price");
        pQuantity.setHint("Quantity");
        pWeight.setHint("Weight");
        fdb = FirebaseDatabase.getInstance().getReference();
        fbAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fbAuth.getCurrentUser();
        if (user != null) {

        } else {
            NavigationView navigationView= findViewById(R.id.nav_view);
            Menu menuNav=navigationView.getMenu();
            MenuItem inv = menuNav.findItem(R.id.nav_invoices);
            MenuItem todo = menuNav.findItem(R.id.nav_todo);
            // MenuItem inv = menuNav.findItem(R.id.nav_invoices);
            inv.setEnabled(false);
           // todo.setEnabled(false);
        }

        //--------------- Navigation Drawer -----------------------------

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        prod = findViewById(R.id.spinner_add_product);
        prod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fdb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        DataSnapshot catSnapshot = dataSnapshot.child("Products");
                        Iterable<DataSnapshot> catChildren = catSnapshot.getChildren();
                        childCount = dataSnapshot.child("Products").child(String.valueOf(prod.getSelectedItem())).getChildrenCount();//gets the number of items in child for the for loop
                      //  Toast.makeText(activity_add_product.this,Long.toString(childCount),Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        fdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot catSnapshot = dataSnapshot.child("Products");
                Iterable<DataSnapshot> catChildren = catSnapshot.getChildren();
                childCount = dataSnapshot.child("Products").child(String.valueOf(prod.getSelectedItem())).getChildrenCount();//gets the number of items in child for the for loop
                for (DataSnapshot contact : catChildren) {
                    list1.add(contact.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        list1.add("Please select a category");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        prod.setAdapter(dataAdapter);
        //------------------------------------------------------------------
    }

    private void addProduct(){


        name = pName.getText().toString();
        price = pPrice.getText().toString();
        qty = pQuantity.getText().toString();
        weight = pWeight.getText().toString();


//        if(!prod.getSelectedItem().toString().equals("Please select a category")) {
//            save = new AUD_Prod(prod.getSelectedItem().toString(), pName.getText().toString(), pPrice.getText().toString(), Integer.parseInt(pQuantity.getText().toString()), childCount);
//            save.writeProd();
//        } else {
//            for (int i = 0; i<1; i++)
//            Toast.makeText(activity_add_product.this,"Please select a category",Toast.LENGTH_SHORT).show();
//        }

       if (prod.getSelectedItem().toString()!="Please select a category") {
          // Toast.makeText(activity_add_product.this,pName.getText().toString(),Toast.LENGTH_SHORT).show();


               fdb.child("Products").child(prod.getSelectedItem().toString()).child(Long.toString(childCount - 2)).child("Name").setValue(name);
               fdb.child("Products").child(prod.getSelectedItem().toString()).child(Long.toString(childCount - 2)).child("Price").setValue(price);
               fdb.child("Products").child(prod.getSelectedItem().toString()).child(Long.toString(childCount - 2)).child("Stock").setValue(qty);
               fdb.child("Products").child(prod.getSelectedItem().toString()).child(Long.toString(childCount - 2)).child("Weight").setValue(weight);
           Toast.makeText(activity_add_product.this,name+" added",Toast.LENGTH_SHORT).show();
           pName.setText("");
           pPrice.setText("");
           pQuantity.setText("");
           pWeight.setText("");
//           pName.setHint("Name");
//           pPrice.setHint("Price");
//           pQuantity.setHint("Quantity");
//           pWeight.setHint("Weight");
           }

       }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id==R.id.nav_Home)
        {
            startActivity(new Intent(activity_add_product.this, MainActivity.class));
            finish();
        }
        if(id==R.id.nav_invoices)
        {
            startActivity(new Intent(activity_add_product.this, Invoices.class));
            finish();
        }
        if(id==R.id.nav_lr)
        {
            startActivity(new Intent(activity_add_product.this, Login.class));
            finish();
        }
        if(id==R.id.nav_todo)
        {
            startActivity(new Intent(activity_add_product.this, ToDo.class));
            finish();
        }
        if(id==R.id.nav_website)
        {
            startActivity(new Intent(activity_add_product.this, Website.class));
            finish();
        }
        if(id==R.id.nav_LO)
        {
            startActivity(new Intent(activity_add_product.this, MainActivity.class));
            finish();
        }
//        if(id==R.id.nav_currency)
//        {
//            startActivity(new Intent(activity_add_product.this, activity_currency.class));
//            finish();
//        }
//        if(id==R.id.nav_add_prod)
//        {
//            startActivity(new Intent(activity_add_product.this, activity_add_product.class));
//            finish();
//        }
        return false;
    }


}
