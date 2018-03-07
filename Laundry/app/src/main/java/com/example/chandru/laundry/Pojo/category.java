package com.example.chandru.laundry.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chandru on 2/13/2018.
 */

public class category {



        @SerializedName("item")
        private List<cat> results;
        public category(List<cat> results) {
            this.results = results;

        }

        public List<cat> getResults() {
            return results;
        }

        public void setResults(List<cat> results) {
            this.results = results;
        }

}
