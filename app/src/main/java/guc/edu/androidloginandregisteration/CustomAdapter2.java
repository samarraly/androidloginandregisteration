package guc.edu.androidloginandregisteration;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import guc.edu.loginandregistration.R;

public class CustomAdapter2 extends BaseAdapter {


    private Context context;
    private ArrayList<String> Reviews_List;


    public CustomAdapter2(Context context, ArrayList<String> Reviews_List) {

        this.context = context;
        this.Reviews_List = Reviews_List;
    }

    @Override
    public int getCount() {
        return Reviews_List.size();
    }

    @Override
    public Object getItem(int position) {
        return Reviews_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.customlistview2, null, true);
            holder.carreview = (TextView) convertView.findViewById(R.id.car_review);
            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        holder.carreview.setText(Reviews_List.get(position));
        Log.d("review_list",">>" +Reviews_List.get(position));

        return convertView;
    }

    private class ViewHolder {

        public TextView carreview;


    }







}
