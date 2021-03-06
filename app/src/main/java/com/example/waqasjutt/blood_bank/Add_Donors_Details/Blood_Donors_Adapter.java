package com.example.waqasjutt.blood_bank.Add_Donors_Details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.waqasjutt.blood_bank.R;

import java.util.ArrayList;
import java.util.List;

public class Blood_Donors_Adapter extends ArrayAdapter implements Filterable {

    Context mCtx;
    List<Blood_Donors_Items> blood_itemses, filterList;
    CustomFilterForDonors filter;
    int resource;

    public Blood_Donors_Adapter(Context mCtx, int resource, List<Blood_Donors_Items> blood_itemses) {
        super(mCtx, resource, blood_itemses);
        this.mCtx = mCtx;
        this.resource = resource;
        this.blood_itemses = blood_itemses;
        this.filterList = blood_itemses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Blood_Donors_Items blood_items = blood_itemses.get(position);
        View row;
        ViewHolder viewHolder;
        row = convertView;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.blood_item_details, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.tv_Name = (TextView) row.findViewById(R.id.tv_Name);
            viewHolder.tv_Mobile = (TextView) row.findViewById(R.id.imageView_Mobile);
            viewHolder.tv_Blood_Group = (TextView) row.findViewById(R.id.tv_blood_group);
            viewHolder.tv_City = (TextView) row.findViewById(R.id.tv_city);
            viewHolder.tv_Address = (TextView) row.findViewById(R.id.tv_address);

            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        viewHolder.tv_Name.setText(blood_items.getName());
        viewHolder.tv_Mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "Donor Number: " + blood_items.getMobile(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + blood_items.getMobile()));
                mCtx.startActivity(intent);
            }
        });
        viewHolder.tv_Blood_Group.setText(blood_items.getBlood_group());
        viewHolder.tv_City.setText(blood_items.getCity());
        viewHolder.tv_Address.setText(blood_items.getAddress());
        return row;
    }

    static class ViewHolder {
        TextView tv_Name, tv_Mobile, tv_Blood_Group, tv_City, tv_Address;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return blood_itemses.get(position);
    }

    @Override
    public int getCount() {
        return blood_itemses.size();
    }

    //RETURN FILTER OBJ
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilterForDonors((ArrayList<Blood_Donors_Items>) filterList, this);
        }

        return filter;
    }
}