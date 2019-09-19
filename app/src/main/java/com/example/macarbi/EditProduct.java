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
import android.widget.Button;
import android.widget.EditText;
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

public class EditProduct extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private DatabaseReference fdb;
    private FirebaseAuth fbAuth;
    public static int id;
    public static String Cat;
    private AUD_Prod update;
    private Spinner bsp;
    String pName;
    String pPrice;
    String pQTY;
    String pExchange;
    String pShipping;

    TextView tvProduct;
    EditText ETprice;
    EditText ETqty;
    EditText ETExchange;
    EditText ETShipping;
    Button   btnSaveEdit, btnDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        fdb = FirebaseDatabase.getInstance().getReference();
        tvProduct = findViewById(R.id.textView_Name);
        ETprice =  findViewById(R.id.edit_text_price);
        ETqty =  findViewById(R.id.edit_text_quantity);
        ETExchange = findViewById(R.id.edit_text_exchange);
        ETShipping = findViewById(R.id.edit_text_shipping);
        fbAuth = FirebaseAuth.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fbAuth.getCurrentUser();
        if (user != null) {

        } else {
            NavigationView navigationView= findViewById(R.id.nav_view);
            Menu menuNav = navigationView.getMenu();
            MenuItem inv = menuNav.findItem(R.id.nav_invoices);
            MenuItem todo = menuNav.findItem(R.id.nav_todo);
            MenuItem Add = menuNav.findItem(R.id.nav_add_prod);
            MenuItem edit = menuNav.findItem(R.id.nav_currency);
            // MenuItem inv = menuNav.findItem(R.id.nav_invoices);
            inv.setEnabled(false);
            //todo.setEnabled(false);
            Add.setEnabled(false);
            edit.setEnabled(false);
        }
        read();
        bsp = findViewById(R.id.brand_spinner);
        btnSaveEdit = (Button) findViewById(R.id.button_save_edit);
        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(Cat.equals("ActiveTools") || Cat.equals("Coxmate") || Cat.equals("NK")) {
                update = new AUD_Prod(Cat, tvProduct.getText().toString(), ETprice.getText().toString(), Integer.parseInt(ETqty.getText().toString()),
                        id, Double.parseDouble(ETExchange.getText().toString()), Double.parseDouble(ETShipping.getText().toString()));
                update.updateProd();
                } else {
                    update = new AUD_Prod(Cat, tvProduct.getText().toString(), ETprice.getText().toString(), Integer.parseInt(ETqty.getText().toString()),
                            id);
                    update.updateProdLocal();
                }
                //fdb.child("Products").child(Cat).child(Integer.toString(id)).child("Name").setValue(tvProduct.getText().toString());
                //fdb.child("Products").child(Cat).child(Integer.toString(id)).child("Regular price").setValue(ETprice.getText().toString());
                //fdb.child("Products").child(Cat).child(Integer.toString(id)).child("Stock").setValue(ETqty.getText().toString());
                // Start NewActivity.class

            }
        });
        btnDelete = (Button) findViewById(R.id.button_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
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


        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        //------------------------------------------------------------------

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
            startActivity(new Intent(EditProduct.this, MainActivity.class));
            finish();
        }
        if(id==R.id.nav_currency)
        {
            fbAuth.getInstance().signOut();
            startActivity(new Intent(EditProduct.this, activity_currency.class));
            finish();
        }
        if(id==R.id.nav_invoices)
        {
            startActivity(new Intent(EditProduct.this, Invoices.class));
            finish();
        }
        if(id==R.id.nav_lr)
        {
            startActivity(new Intent(EditProduct.this, Login.class));
            finish();
        }
        if(id==R.id.nav_todo)
        {
            startActivity(new Intent(EditProduct.this, ToDo.class));
            finish();
        }
        if(id==R.id.nav_website)
        {
            startActivity(new Intent(EditProduct.this, Website.class));
            finish();
        }
        if(id==R.id.nav_LO)
        {
            fbAuth.getInstance().signOut();
            startActivity(new Intent(EditProduct.this, MainActivity.class));
            finish();
        }
        return false;
    }


    private void read() {
        fdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
               pName = dataSnapshot.child("Products").child(Cat).child(Integer.toString(id)).child("Name").getValue(String.class);
               pPrice = dataSnapshot.child("Products").child(Cat).child(Integer.toString(id)).child("Price").getValue(String.class);
               Toast.makeText(EditProduct.this, pName +"\t\t" + pPrice, Toast.LENGTH_LONG).show();
               pQTY = dataSnapshot.child("Products").child(Cat).child(Integer.toString(id)).child("Stock").getValue(String.class);
               if(Cat.equals("ActiveTools") || Cat.equals("Coxmate") || Cat.equals("NK")) {
                   pExchange = dataSnapshot.child("Products").child(Cat).child("Exchange Rate").getValue(double.class).toString();
                   pShipping = dataSnapshot.child("Products").child(Cat).child("Shipping").getValue(double.class).toString();
                   ETExchange.setText(pExchange);
                   ETShipping.setText(pShipping);
               } else {
                   ETExchange.setText("N/A");
                   ETShipping.setText("N/A");
               }
                tvProduct.setText(pName);
                ETprice.setText(pPrice);
                ETqty.setText(pQTY);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

    private void delete(){
        update = new AUD_Prod(tvProduct.getText().toString(), Cat, id);
        update.deleteProd();
    }

}
