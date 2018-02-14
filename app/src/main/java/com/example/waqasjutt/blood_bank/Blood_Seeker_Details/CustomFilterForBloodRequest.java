package com.example.waqasjutt.blood_bank.Blood_Seeker_Details;

import android.widget.Filter;

import java.util.ArrayList;

public class CustomFilterForBloodRequest extends Filter {

    Blood_Request_Adapter adapter;
    ArrayList<Blood_Request_Items> filterList;

    public CustomFilterForBloodRequest(ArrayList<Blood_Request_Items> filterList, Blood_Request_Adapter adapter) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if (constraint != null && constraint.length() > 0) {
            //CHANGE TO UPPER
            constraint = constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<Blood_Request_Items> filteredBlood = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {
                //CHECK
                if (filterList.get(i).getCity().toUpperCase().contains(constraint)) {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredBlood.add(filterList.get(i));
                }
            }

            results.count = filteredBlood.size();
            results.values = filteredBlood;
        } else {
            results.count = filterList.size();
            results.values = filterList;

        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.blood_itemses = (ArrayList<Blood_Request_Items>) results.values;
        //REFRESH
        adapter.notifyDataSetChanged();
    }
}