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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Invoices extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private FirebaseAuth fbAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);
        fbAuth = FirebaseAuth.getInstance();

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
            todo.setEnabled(false);
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
            startActivity(new Intent(Invoices.this, MainActivity.class));
            finish();
        }



        if(id==R.id.nav_lr)
        {
            startActivity(new Intent(Invoices.this, Login.class));
            finish();
        }

        if(id==R.id.nav_todo)
        {
            startActivity(new Intent(Invoices.this, ToDo.class));
            finish();
        }

        if(id==R.id.nav_website)
        {
            startActivity(new Intent(Invoices.this, Website.class));
            finish();
        }
        if(id==R.id.nav_LO)
        {
            fbAuth.getInstance().signOut();
            startActivity(new Intent(Invoices.this, MainActivity.class));
            finish();
        }
        return false;
    }
}
