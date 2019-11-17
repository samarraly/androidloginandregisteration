package guc.edu.androidloginandregisteration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import activity.LoginActivity;
import app.AppConfig;
import guc.edu.loginandregistration.R;
import helper.SQLiteHandler;
import helper.SessionManager;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity  {

    //  private String URLstring = "http://localhost/android_login_api/include/getdata.php";
    private static ProgressDialog mProgressDialog;
    private ListView listView;
    ArrayList<DataModel> dataModelArrayList;
    public static  ArrayList<DataModel> dataModelArrayList_sorted;
    private CustomAdapter listAdapter;
 //   private Button btnLogout;
    private SQLiteHandler db;
    private SessionManager session;
    public static double lat;
    public static double longt;
    private FusedLocationProviderClient client;
    public Location locationA;
    public Location locationB;
   // public String user_id;

    public ArrayList<DataModel> getDataModelArrayList_sorted() {
        return dataModelArrayList_sorted;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  btnLogout = (Button) findViewById(R.id.btnLogout);
        listView = findViewById(R.id.lv);
        client= LocationServices.getFusedLocationProviderClient(this);

       // Log.d("user_id", ">>" + user_id);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        locationA = new Location("point A");
        locationB = new Location("point B");
        requestPermissions();
        // session manager
        session = new SessionManager(getApplicationContext());


///////////////////////////////////////////////////////////////////////////////////////////////////////from here
       // HashMap<String, String> user = new HashMap<String, String>();
        //user=db.getUserDetails();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO Auto-generated method stub
               Intent intent = new Intent(MainActivity.this, carActivity.class);
               intent.putExtra("Model_Name",dataModelArrayList.get(position).getName());
        //  intent.putExtra("car_id",dataModelArrayList.get(position).getId());
             // intent.putExtra("user_id",getIntent().getStringExtra("user_id"));
               intent.putExtra("car_id",dataModelArrayList.get(position).getId());


           //    intent.putExtra("longitude",locationA.getLongitude());
             //  intent.putExtra("latitude",locationA.getLatitude());
                intent.putExtra("car_longitude",dataModelArrayList.get(position).getLongitude());
                intent.putExtra("car_latitude",dataModelArrayList.get(position).getLatitude());
                //intent.putExtra("car_latitude",lat);

                startActivity(intent);


               // Toast.makeText(MainActivity.this, ListViewClickItemArray.get(position).toString(), Toast.LENGTH_LONG).show();

            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (!session.isLoggedIn()) {
            logoutUser();
        }


        if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //  Log.d("errorrrrrrrrr",">>" + "hnaa");

            return;

        }
        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location!=null) {
//my location
                    lat=location.getLatitude();
                    Log.d("lat",">>" + lat);
                    longt=location.getLongitude();
                    Log.d("logt",">>" + longt);
                    locationA.setLatitude(lat);
                    locationA.setLongitude(longt);


                }                else{
                    Log.d("error",">>" +"nulllllll");
                }
            }
        });

        retrieveJSON();


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);


        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:{
                logoutUser();
            }
        }


        return super.onOptionsItemSelected(item);
    }




    private void requestPermissions(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);

    }




    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void retrieveJSON() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_getdata,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {

                        //Log.d("strrrrr", ">>" + response);

                        try {
                            JSONArray obj = new JSONArray(response);

                            // if(obj.optString("status").equals("true")){

                            dataModelArrayList = new ArrayList<>();
                            //  JSONArray dataArray  = obj.getJSONArray("data");
                            Log.d("length",">>" +obj.length());

                            for (int i = 0; i < obj.length(); i++) {

                                DataModel playerModel = new DataModel();

                                JSONObject dataobj = obj.getJSONObject(i);
                                Log.d("object",">>" +dataobj);
                                // System.out.print(i);
                                // Log.d("i",i);
                                playerModel.setId(dataobj.getString("id"));
                                Log.d("id ",">>" +dataobj.getString("id"));
                               // car_id=dataobj.getString("id");




                                playerModel.setName(dataobj.getString("Model_Name"));
                                Log.d("Name ",">>" +dataobj.getString("Model_Name"));

                                playerModel.setFuellevel(dataobj.getString("Fuel_Level"));
                                Log.d("Fuel",">>" +dataobj.getString("Fuel_Level"));

                                //Location car_location=new Location("");
                                playerModel.setLatitude(dataobj.getString("Latitude"));
                                playerModel.setLongitude(dataobj.getString("Longitude"));


//                                locationA.setLatitude(lat);
//                                locationA.setLongitude(longt);
//


                                locationB.setLatitude(Double.parseDouble(dataobj.getString("Latitude")));
                                locationB.setLatitude(Double.parseDouble(dataobj.getString("Longitude")));

                                float distance = locationA.distanceTo(locationB)/1000;//To convert Meter in Kilometer
                                playerModel.setdistance(distance);




                             playerModel.setProduction_year(dataobj.getString("Production_Year"));
                                Log.d("year",">>" +dataobj.getString("Production_Year"));

                                String URL = "http://192.168.1.18/android_login_api/images/"+dataobj.getString("image_path");
                                playerModel.setImgURL(URL);

                                dataModelArrayList.add(playerModel);
                            }


                            setupListview();



                            //}

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);


    }

    private void setupListview(){

        Collections.sort(dataModelArrayList, new Comparator<DataModel>() {
            @Override
            public int compare(DataModel o1, DataModel o2) {
                String a1=o1.getdistance()+"";
                String a2=o2.getdistance()+"";

                return a1.compareTo(a2);
            }
        });

        removeSimpleProgressDialog();  //will remove progress dialog
        listAdapter = new CustomAdapter(this, dataModelArrayList);
        listView.setAdapter(listAdapter);
    }

    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}