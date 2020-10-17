package com.example.causefairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;


import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.causefairy.models.Category;
import com.example.causefairy.models.ItemTrending;
import com.example.causefairy.models.UserC;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Trending2 extends AppCompatActivity {

    RecyclerView mainCategoryRecycler;
    AdapterTrending mainRecyclerAdapter;
  //  private ArrayList<UserC> causeList;

    private FirebaseAuth firebaseAuth;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference ListedCauseRef = db.collection("UserC");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending2);

        // here we will add some dummy data to our model class

        // here we will add data to category item model class

        List<ItemTrending> categoryItemList = new ArrayList<>();
        categoryItemList.add(new ItemTrending(1, R.drawable.image3));
        categoryItemList.add(new ItemTrending(1, R.drawable.image3));
        categoryItemList.add(new ItemTrending(1, R.drawable.image3));

        // added in second category
        List<ItemTrending> categoryItemList2 = new ArrayList<>();
        categoryItemList2.add(new ItemTrending(1, R.drawable.image3));
        categoryItemList2.add(new ItemTrending(1, R.drawable.image3));
        categoryItemList2.add(new ItemTrending(1, R.drawable.image3));

        // added in 3rd category
        List<ItemTrending> categoryItemList3 = new ArrayList<>();
        categoryItemList3.add(new ItemTrending(1, R.drawable.image3));
        categoryItemList3.add(new ItemTrending(1, R.drawable.image3));
        categoryItemList3.add(new ItemTrending(1, R.drawable.image3));

        // added in 4th category
        List<ItemTrending> categoryItemList4 = new ArrayList<>();
        categoryItemList4.add(new ItemTrending(1, R.drawable.image3));
        categoryItemList4.add(new ItemTrending(1, R.drawable.image3));
        categoryItemList4.add(new ItemTrending(1, R.drawable.image3));


        // added in 5th category
        List<ItemTrending> categoryItemList5 = new ArrayList<>();
        categoryItemList5.add(new ItemTrending(1, R.drawable.image3));
        categoryItemList5.add(new ItemTrending(1, R.drawable.image3));
        categoryItemList5.add(new ItemTrending(1, R.drawable.image3));

        List<Category> allCategoryList = new ArrayList<>();
        allCategoryList.add(new Category("Hollywood", categoryItemList));
        allCategoryList.add(new Category("Best of Oscars", categoryItemList2));
        allCategoryList.add(new Category("Movies Dubbed in Hindi", categoryItemList3));
        allCategoryList.add(new Category("Category 4th", categoryItemList4));
        allCategoryList.add(new Category("Category 5th", categoryItemList5));

        setMainCategoryRecycler(allCategoryList);

        firebaseAuth = FirebaseAuth.getInstance();

        loadAllCauses();

    }

    private void setMainCategoryRecycler(List<Category> allCategoryList){

        mainCategoryRecycler = findViewById(R.id.main_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mainCategoryRecycler.setLayoutManager(layoutManager);
        mainRecyclerAdapter = new AdapterTrending(this, allCategoryList);
        mainCategoryRecycler.setAdapter(mainRecyclerAdapter);

    }

    private void loadAllCauses() {
       // causeList = new ArrayList<>();
        ListedCauseRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot ds : queryDocumentSnapshots) {
                            UserC userc = ds.toObject(UserC.class);
                          //  causeList.add(userc);
                        }
                       // adapterTrending = new AdapterTrending(Trending2.this, null, causeList);
                     //   mainCategoryRecycler.setAdapter(adapterTrending);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Trending2.this, "Please Sign In", Toast.LENGTH_SHORT).show();

            }
        });


    }

}