package com.example.topapps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FeedAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<FeedEntry> applications;

    public FeedAdapter(Context context, int resource, List<FeedEntry> applications) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.applications =  applications;
    }

    @Override
    public int getCount() {
        return applications.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        //View view = layoutInflater.inflate(layoutResource,parent,false);
        //convertView is used when we want to reuse a view. Therefore create new when only when convert view is null
        //view holder pattern is used to hold the views we stored the last time, tag field is used for that.
        if (convertView == null){//create view
            convertView = layoutInflater.inflate(layoutResource,parent,false);
             viewHolder = new ViewHolder(convertView);
             convertView.setTag(viewHolder);
        }else{//reuse view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //since using viewholder to make the views reusable, whereby reducing memory consumption
//        TextView movieName = convertView.findViewById(R.id.movieName);
//        TextView movieArtist = convertView.findViewById(R.id.movieArtist);
//        TextView movieSummary = convertView.findViewById(R.id.movieSummary);

        FeedEntry currentApp = applications.get(position);

        viewHolder.movieName.setText(currentApp.getName());
        viewHolder.movieArtist.setText(currentApp.getArtist());
        viewHolder.movieSummary.setText(currentApp.getSummary());
        return convertView;
    }

    //values of widgets are strored in view holder
    private class ViewHolder{
        final TextView movieName;
        final TextView movieArtist;
        final TextView movieSummary;

        ViewHolder(View view){
            this.movieName = view.findViewById(R.id.movieName);
            this.movieArtist = view.findViewById(R.id.movieArtist);
            this.movieSummary = view.findViewById(R.id.movieSummary);
        }

    }
}
