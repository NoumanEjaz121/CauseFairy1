package com.example.causefairy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterCauses extends RecyclerView.Adapter<AdapterCauses.HolderCauses> implements Filterable {

    private  Context context;
    public ArrayList<Product> productList, filterList;
    private FilterProduct filter;

    public AdapterCauses(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.filterList = productList;
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new FilterProduct(this, filterList );
        }
        return filter;
    }

    @NonNull
    @Override
    public HolderCauses onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.row_cause_card, parent, false);
        return new HolderCauses(view);
    }

     public void onBindViewHolder(@NonNull HolderCauses holder, int position) {
        Product product = productList.get(position);

        String id = product.getDocumentId();
        String title = product.getProductName();
        String category = product.getCategory();
        String desc = product.getDescription();
        String qty = String.valueOf(product.getQty()); //maybe?
        String price = String.valueOf(product.getUnitPrice()); //?
        String icon = product.getProductIcon();
        String timestamp = product.getTimestamp();
        String uid = product.getUid();

        holder.tvProductName.setText(title);
        holder.tvUnitPrice.setText("$" +price);
        holder.tvQty.setText(qty);
        holder.tv1.setText(desc+"$$");
        holder.tv2.setText(" +++++ ");


        try{
            Picasso.get().load(icon).placeholder(R.drawable.add_image).into(holder.ivProductIcon);
        }
        catch (Exception e){
            holder.ivProductIcon.setImageResource(R.drawable.add_image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
      //  if(productList != null) {
            return productList.size();
    //    }
    //    return 0;
    }

    class HolderCauses extends RecyclerView.ViewHolder {
        private ImageView ivProductIcon, ivNext;
        private TextView tvProductName, tvQty, tv1, tv2, tvUnitPrice;

        public HolderCauses(@NonNull View itemView){
            super(itemView);

            ivProductIcon = itemView.findViewById(R.id.ivProductIcon);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQty = itemView.findViewById(R.id.tvQty);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tvUnitPrice = itemView.findViewById(R.id.tvUnitPrice);
        }

    }
}
