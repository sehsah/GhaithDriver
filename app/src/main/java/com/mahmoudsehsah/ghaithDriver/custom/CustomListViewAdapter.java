package com.mahmoudsehsah.ghaithDriver.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.acitivities.OrderShowDirectionActivity;
import com.mahmoudsehsah.ghaithDriver.models.Places;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;

/**
 * Created by mahmoud on 3/19/2018.
 */

public class CustomListViewAdapter extends ArrayAdapter<Places> {

    private Activity activity;
    private Context context;

    public CustomListViewAdapter(Context context,Activity activity, int resource, List<Places> books) {
        super(activity, resource, books);
        this.activity = activity;
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Calligrapher calligrapher = new Calligrapher(context);
        calligrapher.setFont((Activity) context, "font/jf.ttf", true);


        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_places, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }


        final Places book = getItem(position);
        holder.name.setText(book.getName());
        int length = (int) (Math.log10(Double.parseDouble(book.getDistance()))+1);
        String mm = "km";
        if (length < 1 ){
            mm = "m";
        }
        holder.distance.setText(book.getDistance() + mm);
        holder.id_market.setText(book.getId());
        Log.d("id market",book.getId());
        final  String dis = book.getDistance() + mm;
        Picasso.with(activity).load(book.getIcon()).into(holder.image);

        // Places dataa = getItem(position);
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // context.startActivity((new Intent(context, OrderShowDirectionActivity.class)));
                Intent intent = new Intent(context, OrderShowDirectionActivity.class);
                Bundle bundle = new Bundle();
                String lat = book.getlat();
                String name = book.getName();
                String lng = book.getlng();
                String idMarket = book.getId();
                bundle.putSerializable("lat",lat);
                bundle.putSerializable("lng",lng);
                bundle.putSerializable("name",name);
                bundle.putSerializable("km",dis);
                bundle.putSerializable("idMarket",idMarket);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }

        });
        return convertView;
    }

    private static class ViewHolder {
        private TextView name,id_market,distance;
        private ImageView image;
        ImageView icon_list_location;

        public ViewHolder(View v) {
            name = (TextView) v.findViewById(R.id.title);
            image = (ImageView) v.findViewById(R.id.icon);
            distance = (TextView) v.findViewById(R.id.distance);
            icon_list_location =  v.findViewById(R.id.icon_list_location);
            id_market =  v.findViewById(R.id.id_market);


        }
    }



}
