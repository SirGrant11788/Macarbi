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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private EditText etEmail;
    private EditText etPassword;

    private ProgressDialog progressDialog;
    private FirebaseAuth fbAuth;
    private DatabaseReference fdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        Button button = (Button) findViewById(R.id.btnReg);
        etEmail = (EditText)findViewById(R.id.etEmailReg);
        etPassword = (EditText)findViewById(R.id.etPasswordReg);

        progressDialog = new ProgressDialog(this);
        fbAuth = FirebaseAuth.getInstance();
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
            //todo.setEnabled(false);
        }

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {



                registerUser();
                // Start NewActivity.class

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




            startActivity(new Intent(Register.this, MainActivity.class));
            finish();

        }


        if(id==R.id.nav_invoices)
        {


            startActivity(new Intent(Register.this, Invoices.class));
            finish();
        }

        if(id==R.id.nav_lr)
        {




            startActivity(new Intent(Register.this, Login.class));
            finish();

        }

        if(id==R.id.nav_todo)
        {




            startActivity(new Intent(Register.this, ToDo.class));
            finish();

        }

        if(id==R.id.nav_website)
        {




            startActivity(new Intent(Register.this, Website.class));
            finish();

        }
        if(id==R.id.nav_LO)
        {
            fbAuth.getInstance().signOut();
            startActivity(new Intent(Register.this, MainActivity.class));
            finish();
        }



        return false;
    }

    private void registerUser()
    {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();




        if (TextUtils.isEmpty(email)||TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "all fields are required", Toast.LENGTH_SHORT).show();
            return;
        }





        progressDialog.setMessage("Registering new user...");
        progressDialog.show();
        fbAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){







                    Toast.makeText(Register.this,"User Registered Successfully", Toast.LENGTH_SHORT).show();


                    startActivity(new Intent(Register.this, MainActivity.class));
                    finish();
                }
                else {
                    progressDialog.cancel();
                    Toast.makeText(Register.this,"An error has occurred or the email address is already in use or is not a valid email address", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
