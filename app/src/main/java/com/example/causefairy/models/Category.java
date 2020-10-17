package com.example.causefairy.models;



import java.util.ArrayList;
import java.util.List;

public class Category {

        String categoryTitle;
        List<ItemTrending> categoryItemList;

        public Category(String categoryTitle, List<ItemTrending> categoryItemList) {
            this.categoryTitle = categoryTitle;
            this.categoryItemList = categoryItemList;
        }

        public List<ItemTrending> getCategoryItemList() {
            return categoryItemList;
        }

        public void setCategoryItemList(List<ItemTrending> categoryItemList) {
            this.categoryItemList = categoryItemList;
        }

        public String getCategoryTitle() {
            return categoryTitle;
        }

        public void setCategoryTitle(String categoryTitle) {
            this.categoryTitle = categoryTitle;
        }

}