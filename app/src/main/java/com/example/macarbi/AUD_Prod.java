package com.example.macarbi;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.jar.Attributes;

public class AUD_Prod {

    private DatabaseReference fdb;
    private String name, price, cat;
    private int quantity, id;
    private long lID;
    private double exchange, shipping;

    public AUD_Prod(String Cat, String Name, String Price, int Quantity, long ID, double exchange, double shipping){
        name = Name;
        price = Price;
        quantity = Quantity;
        cat = Cat;
        lID = ID;
        this.exchange = exchange;
        this.shipping = shipping;
    }
    public AUD_Prod(String Cat, String Name, String Price, int Quantity, long ID){
        name = Name;
        price = Price;
        quantity = Quantity;
        cat = Cat;
        lID = ID;
    }

    public AUD_Prod(String Name, String Cat,int ID){
        cat = Cat;
        id = ID;
        name = Name;
    }


    public void writeProd (){
        fdb = FirebaseDatabase.getInstance().getReference();
        fdb.child("Products").child(cat).child(Long.toString(lID)).child("Name").setValue(name);
        fdb.child("Products").child(cat).child(Long.toString(lID)).child("Price").setValue(price);
        fdb.child("Products").child(cat).child(Long.toString(lID)).child("Stock").setValue(Integer.toString(quantity));
        Log.d("AUD","\nLine 33:\nPName: "+ name +"\nPPrice: "+ price+"\nPQTY: "+ quantity +"\nID: " + Long.toString(lID) +"\nCat: " +cat);

    }

    public void updateProd(){
        fdb = FirebaseDatabase.getInstance().getReference();
        fdb.child("Products").child(cat).child(Integer.toString(id)).child("Name").setValue(name);
        fdb.child("Products").child(cat).child(Integer.toString(id)).child("Price").setValue(price);
        fdb.child("Products").child(cat).child(Integer.toString(id)).child("Stock").setValue(Integer.toString(quantity));
        fdb.child("Products").child(cat).child("Exchange Rate").setValue(exchange);
        fdb.child("Products").child(cat).child("Shipping").setValue(shipping);
    }
    public void updateProdLocal(){
        fdb = FirebaseDatabase.getInstance().getReference();
        fdb.child("Products").child(cat).child(Integer.toString(id)).child("Name").setValue(name);
        fdb.child("Products").child(cat).child(Integer.toString(id)).child("Price").setValue(price);
        fdb.child("Products").child(cat).child(Integer.toString(id)).child("Stock").setValue(Integer.toString(quantity));
    }

    public void deleteProd(){
        fdb = FirebaseDatabase.getInstance().getReference();
        if(!name.contains(",")){
            fdb.child("Products").child(cat).child(Integer.toString(id)).child("Name").setValue(name + ", (Inactive)");
        }
    }

}
