package com.example.causefairy;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.causefairy.models.User;
import com.example.causefairy.models.UserC;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class Causes extends AppCompatActivity {

    ImageView insta, fb, twit, ivFilter, ivCart;
    TextView about,us, terms, use, privacy, policy, tvCart, tvFilteredCauses;
    private EditText etSearchCauses;
    private Button btn_search, btn_filter;
    private RecyclerView rvCauses;

    private ArrayList<UserC> causeList;
    private AdapterCauses adapterCauses;

    private FirebaseAuth firebaseAuth;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference ListedCauseRef = db.collection("UserC");

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
        etSearchCauses= findViewById(R.id.etSearchCauses);
        ivFilter = findViewById(R.id.ivFilter);
        btn_filter = findViewById(R.id.btn_filter);
        RelativeLayout causesRL1 = findViewById(R.id.causesRL1);
        tvFilteredCauses = findViewById(R.id.tvFilteredCauses);
        rvCauses= findViewById(R.id.rvCauses);

        firebaseAuth = FirebaseAuth.getInstance();

        loadAllCauses();

    }
    private void loadAllCauses() {
        causeList = new ArrayList<>();
        ListedCauseRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot ds : queryDocumentSnapshots) {
                            UserC userc = ds.toObject(UserC.class);
                            causeList.add(userc);
                        }
                        adapterCauses = new AdapterCauses(Causes.this, causeList);
                        rvCauses.setAdapter(adapterCauses);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Causes.this, "Please Sign In", Toast.LENGTH_SHORT).show();

            }
        });


    }
    public void loadNearbyCauses(final String myPostcode){

        causeList = new ArrayList<>();
        Query ref = FirebaseFirestore.getInstance().collection("Users");
        ref.orderBy(String.valueOf(User.getUid().equals(UserC.getUid())))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        causeList.clear();
                        for (QueryDocumentSnapshot ds : queryDocumentSnapshots){
                            UserC userc = ds.toObject(UserC.class);

                            String state = ""+ ds.get("postcode");

                            if(state.equals("3000")){
                                causeList.add(userc);
                                Toast.makeText(Causes.this, "1st Case", Toast.LENGTH_SHORT).show();
                            }
                            if(state.equals(myPostcode)){
                                causeList.add(userc);
                                Toast.makeText(Causes.this, "2nd Case", Toast.LENGTH_SHORT).show();
                            }
                            causeList.add(userc);
                            Toast.makeText(Causes.this, "3rd Case", Toast.LENGTH_SHORT).show();

                        }
                        adapterCauses = new AdapterCauses(Causes.this, causeList);
                        rvCauses.setAdapter(adapterCauses);
                    }

                });



    }
    private void show(){
     //   ivCart.setVisibility(View.VISIBLE);
        ivCart.setVisibility(View.GONE);
    }

}