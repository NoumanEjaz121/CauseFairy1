package com.example.causefairy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.causefairy.R.id.spinner;
import static com.example.causefairy.R.id.spinner1;

public class list_item extends AppCompatActivity {
    private static final String TAG = "list_item";

    Spinner spin, spin1;
    EditText et_title, et_description, et_price, et_qty;
    Button btnPost, auc_btn;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);
        firebaseAuth = FirebaseAuth.getInstance();

        auc_btn = findViewById(R.id.auc_btn);
        btnPost = findViewById(R.id.btnPost);
        spin = findViewById(spinner);
        spin1 = findViewById(spinner1);

        et_title = findViewById(R.id.et_title);
        et_description = findViewById(R.id.item_description);
        et_qty = findViewById(R.id.et_qty);
        et_price = findViewById(R.id.et_price);
        progressDialog = new ProgressDialog(this);

        Spinner mySpinner = findViewById(spinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(list_item.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.droplist));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);



        auc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bus = new Intent(list_item.this, Register_Business.class);
                startActivity(bus);
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductListing();
            }
        });
    }

    public void addProductListing() {
        final String productName = et_title.getText().toString();
        final String description = et_description.getText().toString();
        String qty= et_qty.getText().toString().trim();
        String price = et_price.getText().toString().trim();
        //char shippingFee =
        //String sellerId = "";
        if (TextUtils.isEmpty(productName)) {
            et_title.setError("Title required");
            et_title.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(description)) {
            et_description.setError("Description required");
            et_description.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(qty)) {
            et_qty.setError("Please enter quantity");
            et_qty.requestFocus();
            return;
        }


        else if(Double.parseDouble(price) < 6) {
            et_price.setError("Price must be over $5");
            et_price.requestFocus();
            return;
        }
        progressDialog.setMessage("PLease Wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

                    Toast.makeText(list_item.this, "Successfully Listed Product!", Toast.LENGTH_SHORT).show();
                    userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                    DocumentReference documentReference = db.collection("Products").document("userId");
                    Map<String, Object> product = new HashMap<>();
                    product.put("Title ", productName);
                    product.put("Description", description);
                    product.put("Qty", qty);
                    product.put("Price", price);
                    documentReference.set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Product has been listed for " + userId);
                        }
                    });
                    Intent i = new Intent(list_item.this, HomePage.class );
                    startActivity(i);
                    finish();
                }

}


        /*
        Product product = new Product(productName, description, unitPrice);
        productListRef.add(product);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String dropdown, dropdown1;

                String productName = et_title.getText().toString().trim();
                String description = et_description.getText().toString().trim();
                String qty = et_qty.getText().toString().trim();
                String price = et_price.getText().toString().trim();

                if (!validateInputs(productName, description, qty, price)) {

                    CollectionReference productListRef = db.collection("ProductList");
                    Product product = new Product(productName, description, Integer.parseInt(qty), Double.parseDouble(price));

                    productListRef.add(product)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(list_item.this, "Product Added", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(list_item.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            private boolean validateInputs(String productName, String description, String qty, String price) {
                if (productName.isEmpty()) {
                    et_title.setError("Title required");
                    et_title.requestFocus();
                    return true;
                }
                if (description.isEmpty()) {
                    et_description.setError("Description required");
                    et_description.requestFocus();
                    return true;
                }
                if (qty.isEmpty()) {
                    et_qty.setError("Please enter quantity");
                    et_qty.requestFocus();
                    return true;
                }
                if (price.isEmpty()) {
                    et_price.setError("Price must be more than $5");
                    et_price.requestFocus();
                    return true;
                }
                return false;

            }
        });
    }

  /*  @Override
    protected void onStart() {
        super.onStart();
        productListRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                  //  Toast.makeText(list_item.this, "Error while loading from app locally!", Toast.LENGTH_SHORT).show();
                  //  Log.d(TAG, e.toString());
                    return;
                }
                String data = "";
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshot){
                    Product product = documentSnapshot.toObject(Product.class);
                    product.setDocumentId(documentSnapshot.getId());
                    String documentId = product.getDocumentId();
                    String productName= product.getProductName();
                    String description = product.getDescription();
                  //  int qtyInStock = product.getQtyInStock();
                    double unitPrice = product.getUnitPrice();
                   // char shippingFee = product.getShippingFee();
                   // String sellerId = product.getSellerId();

                    Toast.makeText(list_item.this, "Nowhere to display as yet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    */