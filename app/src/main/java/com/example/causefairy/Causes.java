package com.example.causefairy;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Causes extends AppCompatActivity {

    ImageView insta, fb, twit, ivFilter, ivCart;
    TextView about,us, terms, use, privacy, policy, tvCart, tvFilteredProducts, tvUnitPrice;
    private EditText etSearchProducts;
    private Button btn_search, btn_filter;
    private RecyclerView rvProducts;

    private ArrayList<Product> productList;
    private AdapterCauses adapterCauses;

    private FirebaseAuth firebaseAuth;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference fsListedProductRef = db.collection("fsListedProducts");
    //private DocumentReference fsProductRef = db.document("fsListedProducts/Product fs single");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_causes);

        about = findViewById(R.id.about);
        us = findViewById(R.id.us);
        terms = findViewById(R.id.terms);
        use = findViewById(R.id.use);
        btn_search = findViewById(R.id.btn_search);
        etSearchProducts= findViewById(R.id.etSearchProducts);
        ivFilter = findViewById(R.id.ivFilter);
        btn_filter = findViewById(R.id.btn_filter);
        RelativeLayout prodctsRL1 = findViewById(R.id.prodctsRL1);
        tvFilteredProducts = findViewById(R.id.tvFilteredProducts);
        rvProducts= findViewById(R.id.rvProducts);
        tvUnitPrice = findViewById(R.id.tvUnitPrice);

        firebaseAuth = FirebaseAuth.getInstance();

        loadAllProducts();

    }
    private void loadAllProducts(){
        productList = new ArrayList<>();
        fsListedProductRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot ds : queryDocumentSnapshots){
                            Product product = ds.toObject(Product.class);
                            productList.add(product);
                        }
                        adapterCauses = new AdapterCauses(Causes.this, productList);
                        rvProducts.setAdapter(adapterCauses);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Causes.this, "ERR", Toast.LENGTH_SHORT).show();
            }
        });

    }

}