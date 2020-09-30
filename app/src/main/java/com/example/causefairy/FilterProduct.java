package com.example.causefairy;

import android.widget.Filter;

import com.example.causefairy.models.Product;

import java.util.ArrayList;

public class FilterProduct extends Filter {

    private AdapterCauses adapter;
    private ArrayList<Product> filterList;

    public FilterProduct(AdapterCauses adapter, ArrayList<Product> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0) {

            constraint = constraint.toString().toUpperCase();

            ArrayList<Product> filteredModels = new ArrayList<>();
            for (int i = 0; i < filterList.size(); i++) {
                if (filterList.get(i).getProductName().toUpperCase().contains(constraint) ||
                        filterList.get(i).getCategory().toUpperCase().contains(constraint)) {
                    filteredModels.add(filterList.get(i));
                }
            }
            results.count = filteredModels.size();
            results.values = filteredModels;
    }
    else
    {
        results.count = filterList.size();
        results.values = filterList;
    }
    return results;

}

    @Override
    protected void publishResults(CharSequence contraint, FilterResults results){
    //    adapter.productList = (ArrayList<Product>) results.values;
        adapter.notifyDataSetChanged();
    }
}
