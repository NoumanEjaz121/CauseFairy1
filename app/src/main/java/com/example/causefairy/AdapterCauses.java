package com.example.causefairy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.causefairy.models.UserB;
import com.example.causefairy.models.UserC;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class AdapterCauses extends RecyclerView.Adapter<AdapterCauses.HolderCauses> implements Filterable {

    private  Context context;
    public ArrayList<UserC> causeList, filterCauseList;
    private FilterProduct filter;

    public AdapterCauses(Context context, ArrayList<UserC> causeList) {
        this.context = context;
        this.causeList = causeList;
        this.filterCauseList = causeList;
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
        //    filter = new FilterCause(this, filterCauseList);
        }
        return filter;
    }

    @NonNull
    @Override
    public HolderCauses onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.row_cause_card, parent, false);
        return new HolderCauses(view);
    }
    String uidB, businessId, businessName, email1, password, confirm, busLogo;
    int abn;
     public void onBindViewHolder(@NonNull HolderCauses holder, int position) {
         UserC userc = causeList.get(position);
         UserB userb= causeList.get(position);
         String uid = userc.getUid();
         if(uid == uidB) {
             String title = userb.getBusinessName(); //??
         }

        //get data
        String id = userc.getCauseId();


        String title = userb.getBusinessName(); //??
        String category = userc.getCategory();
        String desc = userc.getDescription();
        String postcode = String.valueOf(userc.getPostcode());
        String phone = userc.getPhone();
        String acnc = String.valueOf(userc.getAcnc());
        String icon = userc.getCauseLogo();
       // String timestamp = userc.getTimestamp();


        //set card data
        holder.tvCategory.setText(category);
        holder.tvCauseName.setText(title);
        holder.tvDescription.setText(desc);
        holder.tv1.setText(phone);
        if(postcode.substring(1).charAt(0)==3){
            holder.tv2.setText(" VIC ");
         }else if(postcode.charAt(0)==3){
             holder.tv2.setText("NSW");
         }else {
          //  holder.tv2.setVisibility(View.VISIBLE);
            holder.tv2.setText("    ");
        }
        holder.tvPostcode.setText(postcode);

        try{
            Picasso.get().load(icon).placeholder(R.drawable.my_logo2).into(holder.ivLogo);
        }
        catch (Exception e){
            holder.ivLogo.setImageResource(R.drawable.my_logo2);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return causeList.size(); //no of records
    }

    class HolderCauses extends RecyclerView.ViewHolder {
        private ImageView ivLogo, ivNext;
        private TextView tvCategory, tvCauseName, tv1, tv2, tvDescription, tvPostcode, tvBusName;
        private RatingBar rbStars;

        public HolderCauses(@NonNull View itemView){
            super(itemView);

            ivLogo = itemView.findViewById(R.id.ivLogo);
            tvCauseName = itemView.findViewById(R.id.tvCauseName);
            tvCategory= itemView.findViewById(R.id.tvCategory);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPostcode = itemView.findViewById(R.id.tvPostcode);
            rbStars = itemView.findViewById(R.id.rbStars);

        }

    }
}
