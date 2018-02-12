package com.example.waqasjutt.blood_bank;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Blood_Banks extends Fragment {

    private View view;
    private ListView listView;
    private ArrayList<Blood_Banks_Item> blood_banks_items;
    CustomAdapter customAdapter;

    public Blood_Banks() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.blood_banks_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.listView_BloodBanks);
        blood_banks_items = new ArrayList<>();

        blood_banks_items.add(new Blood_Banks_Item("Hamza Foundation", "Hamza Foundation, 33-F, Khushal khan khattak Road, Old Bara Road, Peshawar, Pakistan", "03219006003", "03219006003", "Hamza Foundation, 33-F, Khushal khan khattak Road, Old Bara Road, Peshawar, Pakistan"));
        blood_banks_items.add(new Blood_Banks_Item("Pakistan Blood Bank", "Pakistan Blood Bank, G-1/15 Karachi 74600, Pakistan", "02136609023", "02136609023", "Pakistan Blood Bank, G-1/15 Karachi 74600, Pakistan"));
        blood_banks_items.add(new Blood_Banks_Item("Fatima Foundation", "Fatima Foundation, Sequire St, Karachi", "02132257356", "02132257356", "Fatima Foundation, Sequire St, Karachi"));

        customAdapter = new CustomAdapter(getActivity(), blood_banks_items);
        listView.setAdapter(customAdapter);

        return view;
    }
}

class CustomAdapter extends ArrayAdapter<Blood_Banks_Item> {

    private ArrayList<Blood_Banks_Item> blood_banks_items;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView name, address, contact, contactImage, location;
    }

    public CustomAdapter(Context context, ArrayList<Blood_Banks_Item> blood_banks_items) {
        super(context, R.layout.blood_banks_item, blood_banks_items);
        this.blood_banks_items = blood_banks_items;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Blood_Banks_Item blood_banks_item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.blood_banks_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.address = (TextView) convertView.findViewById(R.id.tv_address);
            viewHolder.contact = (TextView) convertView.findViewById(R.id.tv_contact);
            viewHolder.contactImage = (TextView) convertView.findViewById(R.id.tv_Mobile);
            viewHolder.location = (TextView) convertView.findViewById(R.id.tv_location);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.name.setText("Name: " + blood_banks_item.getName());
        viewHolder.address.setText("Address: " + blood_banks_item.getAddress());
        viewHolder.contact.setText("Contact: " + blood_banks_item.getContact());
        viewHolder.contactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + blood_banks_item.getContactImage()));
                mContext.startActivity(intent);
            }
        });
        viewHolder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(blood_banks_item.getLocation()));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                mContext.startActivity(mapIntent);
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}