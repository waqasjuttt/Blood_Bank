package com.example.waqasjutt.blood_bank.Add_Donors_Details;

import android.widget.Filter;

import java.util.ArrayList;

public class CustomFilterForDonors extends Filter {

    Blood_Donors_Adapter adapter;
    ArrayList<Blood_Donors_Items> filterList;

    public CustomFilterForDonors(ArrayList<Blood_Donors_Items> filterList, Blood_Donors_Adapter adapter) {
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
            ArrayList<Blood_Donors_Items> filteredBlood = new ArrayList<>();

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
        adapter.blood_itemses = (ArrayList<Blood_Donors_Items>) results.values;
        //REFRESH
        adapter.notifyDataSetChanged();
    }
}