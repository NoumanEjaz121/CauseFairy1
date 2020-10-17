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

import com.example.causefairy.models.Category;
import com.example.causefairy.models.ItemTrending;
import com.example.causefairy.models.UserB;
import com.example.causefairy.models.UserC;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterCategoryTrending extends RecyclerView.Adapter<AdapterCategoryTrending.CategoryItemViewHolder> {

    private Context context;
    private List<ItemTrending> categoryItemList;

    public AdapterCategoryTrending(Context context, List<ItemTrending> categoryItemList) {
            this.context = context;
            this.categoryItemList = categoryItemList;
            }

    @NonNull
    @Override
    public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CategoryItemViewHolder(LayoutInflater.from(context).inflate(R.layout.category_trending_row, parent, false));
            }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemViewHolder holder, int position) {

            holder.itemImage.setImageResource(categoryItemList.get(position).getImageUrl());
            }

    @Override
    public int getItemCount() {
            return categoryItemList.size();
            }

    public static final class CategoryItemViewHolder extends RecyclerView.ViewHolder{

        ImageView itemImage;

        public CategoryItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.item_image);

        }
    }

}