package com.example.macarbi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private DatabaseReference fdb;
    private FirebaseAuth fbAuth;
    private ProgressDialog pdLogin;
    private EditText etUserEmail;
    private EditText etUserPassword;
    private TextView tvPassowrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fbAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fbAuth.getCurrentUser();
        //if (user != null) {

        //} else {
        //    NavigationView navigationView= findViewById(R.id.nav_view);
        //    Menu menuNav=navigationView.getMenu();
        //    MenuItem inv = menuNav.findItem(R.id.nav_invoices);
        //    MenuItem todo = menuNav.findItem(R.id.nav_todo);
            // MenuItem inv = menuNav.findItem(R.id.nav_invoices);
        //    inv.setEnabled(false);
        //    todo.setEnabled(false);
        //}

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fdb = FirebaseDatabase.getInstance().getReference();
        fbAuth = FirebaseAuth.getInstance();
        etUserEmail = (EditText) findViewById(R.id.etEmailLogin);
        etUserPassword = (EditText) findViewById(R.id.etPasswordLogin);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        pdLogin = new ProgressDialog(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                UserLogin();
                // Start NewActivity.class
            }
        });

        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Start NewActivity.class
                Intent myIntent = new Intent(Login.this,
                        Register.class);
                startActivity(myIntent);
            }
        });


        FirebaseUser usercheck = fbAuth.getCurrentUser();
        if (usercheck != null) {

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
            btnRegister.setEnabled(false);
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
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
        if(id==R.id.nav_invoices)
        {
            startActivity(new Intent(Login.this, Invoices.class));
            finish();
        }
        if(id==R.id.nav_lr)
        {
            startActivity(new Intent(Login.this, Login.class));
            finish();
        }
        if(id==R.id.nav_todo)
        {
            startActivity(new Intent(Login.this, ToDo.class));
            finish();
        }
        if(id==R.id.nav_website)
        {
            startActivity(new Intent(Login.this, Website.class));
            finish();
        }
        if(id==R.id.nav_LO)
        {
            fbAuth.getInstance().signOut();
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
//        if(id==R.id.nav_currency)
//        {
//           // startActivity(new Intent(Login.this, activity_currency.class));
//            finish();
//        }
        if(id==R.id.nav_add_prod)
        {
            startActivity(new Intent(Login.this, activity_add_product.class));
            finish();
        }
        return false;
    }



    private void UserLogin()
    {
        String email = etUserEmail.getText().toString();
        String Password = etUserPassword.getText().toString();
        if (TextUtils.isEmpty(email)||TextUtils.isEmpty(Password))
        {
            Toast.makeText(this, "all fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
        pdLogin.setMessage("Logging User in");
        pdLogin.show();
        fbAuth.signInWithEmailAndPassword(email, Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                }
                else {
                    Toast toast= Toast.makeText(getApplicationContext(),
                            "Invalid Username Or Password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    pdLogin.cancel();
                }
            }
        });
    }
}
