package com.example.macarbi;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = null;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private CheckBox inactive;
    private DatabaseReference fdb;
    private FirebaseAuth fbAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    long childCount, childCountNK, childCountActiveTools, childCountConcept2, childCountCoxmate, childCountCroker, childCountHudson, childCountRowshop, childCountSwift;
    String pName;
    String PR;
    String pPrice;
    String search, searchResult="";//search
    List<String> searchResultList=new ArrayList<String>();//search
    private double Exchange, rands, weight, shipping;
    String pQTY;
    ListView lv;
    Spinner sp;
    String spValue;
    List<String> searchName = new ArrayList<>();
    List<String> searchPrice = new ArrayList<>();
    List<String> searchQty = new ArrayList<>();
    List<String> searchWeight = new ArrayList<>();
    private String[] name;
    private Context context;

    Button btnAdd, btnSub;

    List<String> list1 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        inactive = findViewById(R.id.checkbox_show_inactive);
         //tracking the sign in and singn out operations

        //------ THIS CODE MAY NEED TO BE PLACED ON ALL ACTIVITIES, FOR NOW ONLY WORKS FROM MAINACTIVITY--------------
        fbAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fbAuth.getCurrentUser();
        if (user != null) {

        } else {
            NavigationView navigationView = findViewById(R.id.nav_view);
            Menu menuNav = navigationView.getMenu();
            MenuItem inv = menuNav.findItem(R.id.nav_invoices);
            MenuItem todo = menuNav.findItem(R.id.nav_todo);
            MenuItem Add = menuNav.findItem(R.id.nav_add_prod);
            inv.setEnabled(false);

            Add.setEnabled(false);

        }
        lv = (ListView) findViewById(R.id.lvHome);

        fdb = FirebaseDatabase.getInstance().getReference();

        sp = (Spinner) findViewById(R.id.spinner);
        View v = findViewById(R.id.lvContent);
//
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> list, View v, int pos, long id) {
                int p = pos;
                //share to start
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, lv.getItemAtPosition(p).toString().replaceAll("First Line="," ").replaceAll("Second Line="," ").replace('{',' ').replace('}',' '));
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                //share to end
                return true;
            }
        });
        if (user!=null) {
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                    // Your code for item clicks
                    int p = pos;
                    EditProduct.Cat = spValue;
                    EditProduct.id = Integer.parseInt(name[p]);

                    startActivity(new Intent(MainActivity.this, EditProduct.class));
                }
            });
        }

        FirebaseMessaging.getInstance().subscribeToTopic("Notes")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
//                            msg = getString(R.string.msg_subscribe_failed);
                        }
//                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        //Gets selected category from spinner
        fdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot catSnapshot = dataSnapshot.child("Products");
                Iterable<DataSnapshot> catChildren = catSnapshot.getChildren();

                for (DataSnapshot contact : catChildren) {
                    list1.add(contact.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sp = (Spinner) findViewById(R.id.spinner);
        list1.add("Please select a category");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp.setAdapter(dataAdapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                spValue = String.valueOf(sp.getSelectedItem());

                read();//calls the read method to fill Listview
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        //--------------- Navigation Drawer -----------------------------

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        drawer = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        //------------------------------------------------------------------
        //Start Search
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //run popup method
                searchResult="";
                showAddItemDialog(MainActivity.this);
            }
        });
        //End Search
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Code for Drawer buttons
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_Home) {//   startActivity(new Intent(MainActivity.this, MainActivity.class));
//            finish();
        }
        if (id == R.id.nav_invoices) {
            startActivity(new Intent(MainActivity.this, Invoices.class));
            finish();
        }

        if (id == R.id.nav_lr) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }

        if (id == R.id.nav_todo) {
            startActivity(new Intent(MainActivity.this, ToDo.class));
            finish();
        }

        if (id == R.id.nav_website) {
            startActivity(new Intent(MainActivity.this, Website.class));
            finish();
        }
        if (id == R.id.nav_LO) {
            fbAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
        }
        if (id == R.id.nav_add_prod) {
            startActivity(new Intent(MainActivity.this, activity_add_product.class));
            finish();
        }


        return false;
    }

//----------END OF DRAWER CODE--------

    //read data from Firebase and insert into listview
    public void read() {
        fdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                StringBuilder sb = new StringBuilder();
                HashMap<String, String> pn = new LinkedHashMap<>();


                childCount = dataSnapshot.child("Products").child(spValue).getChildrenCount();//gets the number of items in child for the for loop
                //Toast.makeText(MainActivity.this, Long.toString(childCount), Toast.LENGTH_SHORT).show();//todo delete Toast count
//              loops through all products
                for (int i = 0; i <= childCount -3; i++) {
                    pName = dataSnapshot.child("Products").child(spValue).child(Integer.toString(i)).child("Name").getValue(String.class);
                    pPrice = dataSnapshot.child("Products").child(spValue).child(Integer.toString(i)).child("Price").getValue(String.class);
                    pQTY = dataSnapshot.child("Products").child(spValue).child(Integer.toString(i)).child("Stock").getValue(String.class);

                    if (spValue.equals("ActiveTools") || spValue.equals("Coxmate") || spValue.equals("NK")) {
                        weight = Double.parseDouble(dataSnapshot.child("Products").child(spValue).child(Integer.toString(i)).child("Weight").getValue(String.class));
                        Log.d("THIS IS VAL", weight + "\t" + i + "");
                        shipping = Double.parseDouble(dataSnapshot.child("Products").child(spValue).child("Shipping").getValue(String.class));
                        Exchange = Double.parseDouble(dataSnapshot.child("Products").child(spValue).child("Exchange Rate").getValue(String.class));
                        pQTY = dataSnapshot.child("Products").child(spValue).child(Integer.toString(i)).child("Stock").getValue(String.class);

                        double cpr = Double.parseDouble(pPrice) * Exchange;
                        double scd = weight * shipping;
                        double scr = scd * Exchange;
                        double iv = (cpr * 1.1)*0.15;
                        double ctu = cpr + scr + iv;
                        double ctc = Math.round(ctu + (ctu*0.3));
                        String rand = String.format("%.2f", ctc);
                        PR = "R" + rand + "\n" + "QTY: " + pQTY;
                        if (inactive.isChecked()) {
                            pn.put(pName, PR);
                            sb.append(i + ",");
                        } else if (!pName.contains(",")) {
                            pn.put(pName, PR);
                            sb.append(i + ",");
                        }
                    } else {

                        PR = "R" + pPrice + "\n" + "QTY: " + pQTY;
                        if (inactive.isChecked()) {
                            pn.put(pName, PR);
                            sb.append(i + ",");
                        } else if (!pName.contains(",")) {
                            pn.put(pName, PR);
                            sb.append(i + ",");
                        }
                    }
                }
                sb.append("0");
                name = sb.toString().split(",");

                List<HashMap<String, String>> listItems = new ArrayList<>();
                SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, listItems, R.layout.list_item,
                        new String[]{"First Line", "Second Line"},
                        new int[]{R.id.text1, R.id.text2});

                Iterator it = pn.entrySet().iterator();

                while (it.hasNext()) {
                    HashMap<String, String> resultsMap = new LinkedHashMap<>();
                    Map.Entry pair = (Map.Entry) it.next();
                    resultsMap.put("First Line", pair.getKey().toString());
                    resultsMap.put("Second Line", pair.getValue().toString());
                    listItems.add(resultsMap);
                }

                lv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //search popup box and result popup box
    private void showAddItemDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("SEARCH")
                .setMessage("ENTER PRODUCT NAME")
                .setView(taskEditText)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        search = (taskEditText.getText()).toString();
//todo searchResult here
                        //Start load all products


                        try {
                            fdb.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    childCountNK = dataSnapshot.child("Products").child("NK").getChildrenCount();//gets the number of items in child for the for loop
                                    childCountActiveTools = dataSnapshot.child("Products").child("ActiveTools").getChildrenCount();
                                    childCountConcept2 = dataSnapshot.child("Products").child("Concept 2").getChildrenCount();
                                    childCountCoxmate = dataSnapshot.child("Products").child("Coxmate").getChildrenCount();
                                    childCountCroker = dataSnapshot.child("Products").child("Croker").getChildrenCount();
                                    childCountHudson = dataSnapshot.child("Products").child("Hudson").getChildrenCount();
                                    childCountRowshop = dataSnapshot.child("Products").child("Rowshop").getChildrenCount();
                                    childCountSwift = dataSnapshot.child("Products").child("Swift").getChildrenCount();
                                    //              loops through all products
                                    for (int i = 0; i <= childCountNK - 3; i++) {
                                        searchName.add(i, dataSnapshot.child("Products").child("NK").child(Integer.toString(i)).child("Name").getValue(String.class));
                                        searchPrice.add(i, dataSnapshot.child("Products").child("NK").child(Integer.toString(i)).child("Price").getValue(String.class));
                                        searchQty.add(i, dataSnapshot.child("Products").child("NK").child(Integer.toString(i)).child("Stock").getValue(String.class));
                                        searchWeight.add(i, dataSnapshot.child("Products").child("NK").child(Integer.toString(i)).child("Weight").getValue(String.class));
                                        double shippingSearch = Double.parseDouble(dataSnapshot.child("Products").child("NK").child("Shipping").getValue(String.class));
                                        double ExchangeSearch = Double.parseDouble(dataSnapshot.child("Products").child("NK").child("Exchange Rate").getValue(String.class));

                                        double cpr = Double.parseDouble(searchPrice.get(i)) * ExchangeSearch;
                                        double scd = Double.parseDouble(searchWeight.get(i)) * shippingSearch;
                                        double scr = scd * ExchangeSearch;
                                        double iv = (cpr * 1.1)*0.15;
                                        double ctu = cpr + scr + iv;
                                        double ctc = Math.round(ctu + (ctu*0.3));
                                        String rand = String.format("%.2f", ctc);

                                        if (searchName.get(i).toLowerCase().replace(" ","").contains(search.toLowerCase().replace(" ",""))) {
                                            searchResult += "NK\n" + searchName.get(i) + "\nR" + rand + "\nQTY:" + searchQty.get(i) + "\n-------------------\n";
                                            searchResultList.add("NK\n" + searchName.get(i) + "\nR" + rand + "\nQTY:" + searchQty.get(i)+"\n");
                                        }
                                    }
                                    for (int i = 0; i <= (int) childCountActiveTools - 3; i++) {
                                        searchName.add(i, dataSnapshot.child("Products").child("ActiveTools").child(Integer.toString(i)).child("Name").getValue(String.class));
                                        searchPrice.add(i, dataSnapshot.child("Products").child("ActiveTools").child(Integer.toString(i)).child("Price").getValue(String.class));
                                        searchQty.add(i, dataSnapshot.child("Products").child("ActiveTools").child(Integer.toString(i)).child("Stock").getValue(String.class));
                                        searchWeight.add(i, dataSnapshot.child("Products").child("ActiveTools").child(Integer.toString(i)).child("Weight").getValue(String.class));
                                        double shippingSearch = Double.parseDouble(dataSnapshot.child("Products").child("ActiveTools").child("Shipping").getValue(String.class));
                                        double ExchangeSearch = Double.parseDouble(dataSnapshot.child("Products").child("ActiveTools").child("Exchange Rate").getValue(String.class));

                                        double cpr = Double.parseDouble(searchPrice.get(i)) * ExchangeSearch;
                                        double scd = Double.parseDouble(searchWeight.get(i)) * shippingSearch;
                                        double scr = scd * ExchangeSearch;
                                        double iv = (cpr * 1.1)*0.15;
                                        double ctu = cpr + scr + iv;
                                        double ctc = Math.round(ctu + (ctu*0.3));
                                        String rand = String.format("%.2f", ctc);
                                        if (searchName.get(i).toLowerCase().replace(" ","").contains(search.toLowerCase().replace(" ",""))) {
                                            searchResult += "ActiveTools\n" + searchName.get(i) + "\nR" + rand + "\nQTY:" + searchQty.get(i) + "\n-------------------\n";
                                            searchResultList.add("ActiveTools\n" + searchName.get(i) + "\nR" + rand + "\nQTY:" + searchQty.get(i)+"\n");
                                        }
                                    }
                                    for (int i = 0; i <= childCountConcept2 - 3; i++) {
                                        searchName.add(i, dataSnapshot.child("Products").child("Concept 2").child(Integer.toString(i)).child("Name").getValue(String.class));
                                        searchPrice.add(i, dataSnapshot.child("Products").child("Concept 2").child(Integer.toString(i)).child("Price").getValue(String.class));
                                        searchQty.add(i, dataSnapshot.child("Products").child("Concept 2").child(Integer.toString(i)).child("Stock").getValue(String.class));
                                        if (searchName.get(i).toLowerCase().replace(" ","").contains(search.toLowerCase().replace(" ",""))) {
                                            searchResult += "Concept 2\n" + searchName.get(i) + "\nR" + searchPrice.get(i) + "\nQTY:" + searchQty.get(i) + "\n-------------------\n";
                                            searchResultList.add("Concept 2\n" + searchName.get(i) + "\nR" + searchPrice.get(i) + "\nQTY:" + searchQty.get(i)+"\n");
                                        }
                                    }
                                    for (int i = 0; i <= childCountCoxmate - 3; i++) {
                                        searchName.add(i, dataSnapshot.child("Products").child("Coxmate").child(Integer.toString(i)).child("Name").getValue(String.class));
                                        searchPrice.add(i, dataSnapshot.child("Products").child("Coxmate").child(Integer.toString(i)).child("Price").getValue(String.class));
                                        searchQty.add(i, dataSnapshot.child("Products").child("Coxmate").child(Integer.toString(i)).child("Stock").getValue(String.class));
                                        searchWeight.add(i, dataSnapshot.child("Products").child("Coxmate").child(Integer.toString(i)).child("Weight").getValue(String.class));
                                        double shippingSearch = Double.parseDouble(dataSnapshot.child("Products").child("Coxmate").child("Shipping").getValue(String.class));
                                        double ExchangeSearch = Double.parseDouble(dataSnapshot.child("Products").child("Coxmate").child("Exchange Rate").getValue(String.class));

                                        double cpr = Double.parseDouble(searchPrice.get(i)) * ExchangeSearch;
                                        double scd = Double.parseDouble(searchWeight.get(i)) * shippingSearch;
                                        double scr = scd * ExchangeSearch;
                                        double iv = (cpr * 1.1)*0.15;
                                        double ctu = cpr + scr + iv;
                                        double ctc = Math.round(ctu + (ctu*0.3));
                                        String rand = String.format("%.2f", ctc);
                                        if (searchName.get(i).toLowerCase().replace(" ","").contains(search.toLowerCase().replace(" ",""))) {
                                            searchResult += "Coxmate\n" + searchName.get(i) + "\nR" + rand + "\nQTY:" + searchQty.get(i) + "\n-------------------\n";
                                            searchResultList.add("Coxmate\n" + searchName.get(i) + "\nR" + rand + "\nQTY:" + searchQty.get(i)+"\n");
                                        }
                                    }
                                    for (int i = 0; i <= childCountCroker - 3; i++) {
                                        searchName.add(i, dataSnapshot.child("Products").child("Croker").child(Integer.toString(i)).child("Name").getValue(String.class));
                                        searchPrice.add(i, dataSnapshot.child("Products").child("Croker").child(Integer.toString(i)).child("Price").getValue(String.class));
                                        searchQty.add(i, dataSnapshot.child("Products").child("Croker").child(Integer.toString(i)).child("Stock").getValue(String.class));
                                        if (searchName.get(i).toLowerCase().replace(" ","").contains(search.toLowerCase().replace(" ",""))) {
                                            searchResult += "Croker\n" + searchName.get(i) + "\nR" + searchPrice.get(i) + "\nQTY:" + searchQty.get(i) + "\n-------------------\n";
                                            searchResultList.add("Croker\n" + searchName.get(i) + "\nR" + searchPrice.get(i) + "\nQTY:" + searchQty.get(i)+"\n");
                                        }
                                    }
                                    for (int i = 0; i <= childCountHudson - 3; i++) {
                                        searchName.add(i, dataSnapshot.child("Products").child("Hudson").child(Integer.toString(i)).child("Name").getValue(String.class));
                                        searchPrice.add(i, dataSnapshot.child("Products").child("Hudson").child(Integer.toString(i)).child("Price").getValue(String.class));
                                        searchQty.add(i, dataSnapshot.child("Products").child("Hudson").child(Integer.toString(i)).child("Stock").getValue(String.class));
                                        if (searchName.get(i).toLowerCase().replace(" ","").contains(search.toLowerCase().replace(" ",""))) {
                                            searchResult += "Hudson\n" + searchName.get(i) + "\nR" + searchPrice.get(i) + "\nQTY:" + searchQty.get(i) + "\n-------------------\n";
                                            searchResultList.add("Hudson\n" + searchName.get(i) + "\nR" + searchPrice.get(i) + "\nQTY:" + searchQty.get(i)+"\n");
                                        }
                                    }
                                    for (int i = 0; i <= childCountRowshop - 3; i++) {
                                        searchName.add(i, dataSnapshot.child("Products").child("Rowshop").child(Integer.toString(i)).child("Name").getValue(String.class));
                                        searchPrice.add(i, dataSnapshot.child("Products").child("Rowshop").child(Integer.toString(i)).child("Price").getValue(String.class));
                                        searchQty.add(i, dataSnapshot.child("Products").child("Rowshop").child(Integer.toString(i)).child("Stock").getValue(String.class));
                                        if (searchName.get(i).toLowerCase().replace(" ","").contains(search.toLowerCase().replace(" ",""))) {
                                            searchResult += "RowShop\n" + searchName.get(i) + "\nR" + searchPrice.get(i) + "\nQTY:" + searchQty.get(i) + "\n-------------------\n";
                                            searchResultList.add("RowShop\n" + searchName.get(i) + "\nR" + searchPrice.get(i) + "\nQTY:" + searchQty.get(i)+"\n");
                                        }
                                    }
                                    for (int i = 0; i <= childCountSwift - 3; i++) {
                                        searchName.add(i, dataSnapshot.child("Products").child("Swift").child(Integer.toString(i)).child("Name").getValue(String.class));
                                        searchPrice.add(i, dataSnapshot.child("Products").child("Swift").child(Integer.toString(i)).child("Price").getValue(String.class));
                                        searchQty.add(i, dataSnapshot.child("Products").child("Swift").child(Integer.toString(i)).child("Stock").getValue(String.class));
                                        if (searchName.get(i).toLowerCase().replace(" ","").contains(search.toLowerCase().replace(" ",""))) {
                                            searchResult += "Swift\n" + searchName.get(i) + "\nR" + searchPrice.get(i) + "\nQTY:" + searchQty.get(i) + "\n-------------------\n";
                                            searchResultList.add("Swift\n" + searchName.get(i) + "\nR" + searchPrice.get(i) + "\nQTY:" + searchQty.get(i)+"\n");
                                        }
                                    }
                                    showAddItemDialogResult(MainActivity.this);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.d("SEARCH", "ERROR: DB ERROR");
                                }
                            });
                        } catch (Exception ex) {
                            Log.d("SEARCH", " search EXCEPTION: " + ex);
                        }
//
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }

    private void showAddItemDialogResult(Context c) {
        try {
            final CharSequence[] cs = searchResultList.toArray(new CharSequence[searchResultList.size()]);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(search);
            builder.setItems(cs, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, cs[item]);
                    sendIntent.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception ex) {
            Log.d("PopUp", "Result EXCEPTION: " + ex);
        }
    }



}
/*cost price rands = dollar * exchange
shipping cost dollar = weight * shipping (dollars)
shipping cost rands = shipping * exchange
import vat = (cost price rands * 1.1) *0,15
cost to us = cost price rands + shipping cost rands + import vat*/