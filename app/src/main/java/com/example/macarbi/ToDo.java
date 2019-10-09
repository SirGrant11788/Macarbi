package com.example.macarbi;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ToDo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private DatabaseReference fdb;
    private FirebaseAuth fbAuth;
    Button btn;
    EditText et1;
    EditText et2;
    ListView lv;
    String Titlestr;
    String Contentstr;
    String text;
    String[] note;
    List<String> ListTitle = new ArrayList<String>();
    List<String> ListContent = new ArrayList<String>();
    String child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        btn = (Button) findViewById(R.id.btnSave);
        lv = (ListView)findViewById(R.id.lvNote);
        et1 = (EditText) findViewById(R.id.etNoteName);
        et2 = (EditText) findViewById(R.id.etNoteContent);
        fdb = FirebaseDatabase.getInstance().getReference();
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
            todo.setEnabled(false);
            Add.setEnabled(false);
            edit.setEnabled(false);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                // Your code for item clicks
                int p = pos;
                text = lv.getItemAtPosition(p).toString();


                AlertDialog dialog = new AlertDialog.Builder(ToDo.this)
                        .setTitle("DELETE NOTE")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                note=text.split(",");
                                note=note[1].split("=");
                                final StringBuilder sb = new StringBuilder();
                                sb.append(note[1]);
                                sb.deleteCharAt(note[1].length() -1);
                                child = sb.toString();
                                deleteNote();
                                //fdb.child("Notes").child(child).child("Contents").setValue("Test");
                                Toast.makeText(ToDo.this, "|" +child + "|", Toast.LENGTH_LONG).show();

                            }})
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });



        readNotes();


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
            startActivity(new Intent(ToDo.this, MainActivity.class));
            finish();
        }
        if(id==R.id.nav_invoices)
        {
            startActivity(new Intent(ToDo.this, Invoices.class));
            finish();
        }
        if(id==R.id.nav_lr)
        {
            startActivity(new Intent(ToDo.this, Login.class));
            finish();
        }
        if(id==R.id.nav_todo)
        {
            startActivity(new Intent(ToDo.this, ToDo.class));
            finish();
        }
        if(id==R.id.nav_website)
        {
            startActivity(new Intent(ToDo.this, Website.class));
            finish();
        }
        if(id==R.id.nav_LO)
        {
            startActivity(new Intent(ToDo.this, MainActivity.class));
            finish();
        }
//        if(id==R.id.nav_currency)
//        {
//            startActivity(new Intent(ToDo.this, activity_currency.class));
//            finish();
//        }
        if(id==R.id.nav_add_prod)
        {
            startActivity(new Intent(ToDo.this, activity_add_product.class));
            finish();
        }
        return false;
    }
    private void saveNote()
    {
        String Title = et1.getText().toString();
        String Note = et2.getText().toString();

        if (TextUtils.isEmpty(Note)||TextUtils.isEmpty(Title))
        {
            Toast.makeText(this, "all fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
        else
            {
                fdb.child("Notes").child(Title).child("Contents").setValue(Note);

            }





    }

    private void deleteNote()
    {
        fdb.child("Notes").child(child).child("Contents").setValue("Test");
        fdb.child("Notes").child(child).child("Contents").removeValue();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("Notes").orderByChild(child).equalTo(child);
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void readNotes()

    {


        fdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot catSnapshot = dataSnapshot.child("Notes");
                Iterable<DataSnapshot> catChildren = catSnapshot.getChildren();
                HashMap<String, String> pn= new HashMap<>();

                for (DataSnapshot contact : catChildren) {
                    Titlestr = contact.getKey();
                    ListTitle.add(Titlestr);
                    Contentstr = (dataSnapshot.child("Notes").child(contact.getKey()).child("Contents").getValue(String.class));
                    ListContent.add(Contentstr);
                    pn.put(Titlestr,Contentstr);
                }


                List<HashMap<String, String>> listItems = new ArrayList<>();
                SimpleAdapter adapter = new SimpleAdapter(ToDo.this, listItems, R.layout.list_item,
                        new String [] {"First Line","Second Line"},
                        new int[]{R.id.text1, R.id.text2});

                Iterator it = pn.entrySet().iterator();

                while (it.hasNext())
                {
                    HashMap <String, String> resultsMap = new HashMap<>();
                    Map.Entry pair = (Map.Entry) it.next();
                    resultsMap.put("First Line", pair.getKey().toString());
                    resultsMap.put("Second Line", pair.getValue().toString());
                    listItems.add(resultsMap);
                }


//

                lv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







        }
}
