package guc.edu.androidloginandregisteration;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import guc.edu.loginandregistration.R;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DataModel> dataModelArrayList;


    public CustomAdapter(Context context, ArrayList<DataModel> dataModelArrayList) {

        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return dataModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataModelArrayList.get(position);
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
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.customlistview, null, true);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.vechilename = (TextView) convertView.findViewById(R.id.car_name);
            holder.Vechilefuellevel = (TextView) convertView.findViewById(R.id.car_fuel);
            holder.vechile_distance = (TextView) convertView.findViewById(R.id.car_distance);
            holder.vechile_production_year = (TextView) convertView.findViewById(R.id.car_year);


            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        Picasso.get().load(dataModelArrayList.get(position).getImgURL()).into(holder.iv);

        holder.vechilename.setText("Name: "+dataModelArrayList.get(position).getName());
        holder.Vechilefuellevel.setText("Fuel Level: "+dataModelArrayList.get(position).getFuellevel());
        Log.d("distancecustom",">>" +dataModelArrayList.get(position).getdistance());

        holder.vechile_distance.setText("Distance:"+dataModelArrayList.get(position).getdistance()+" Km");

        holder.vechile_production_year.setText("Production Year: "+dataModelArrayList.get(position).getProduction_year());

        return convertView;
    }

    private class ViewHolder {

        protected TextView vechilename,Vechilefuellevel, vechile_production_year,vechile_distance;
        protected ImageView iv;
    }

}