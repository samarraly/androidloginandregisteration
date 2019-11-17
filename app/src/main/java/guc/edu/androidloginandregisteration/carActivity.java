package guc.edu.androidloginandregisteration;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.HashMap;

import app.AppConfig;
import guc.edu.loginandregistration.R;
import helper.SQLiteHandler;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class carActivity extends AppCompatActivity {
    public String carname;


    public TextView Carname;

    public double longitude;
    public double latitude;

    public double car_longitude;
    public double car_latitude;

    public String car_id;
 //   public String user_id;

    public Button mapp;
    public Button reviews;

    private FusedLocationProviderClient client;
    public Location locationA;
    private SQLiteHandler db1;

    ArrayList<String> dataModelArrayList=new ArrayList<>();

    CustomAdapter2 listadapter;
    private ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        Intent incoming_intent = getIntent();
        carname = incoming_intent.getStringExtra("Model_Name");
        Log.d("Model_Name", ">>" + carname);

        car_id = incoming_intent.getStringExtra("car_id");
        Carname = (TextView) findViewById(R.id.Model_name);
        mapp = (Button) findViewById(R.id.navigate);
        reviews = (Button) findViewById(R.id.submit_review);
        listview = findViewById(R.id.reviews_list);
        client= LocationServices.getFusedLocationProviderClient(this);
//       longitude = Double.parseDouble(incoming_intent.getStringExtra("longitude"));
  //     latitude = Double.parseDouble(incoming_intent.getStringExtra("latitude"));
//        Log.d ("current_longitude",">>"+longitude);
//        Log.d ("current_latitude",">>"+latitude);
    //    longitude = incoming_intent.getStringExtra("longitude");
        db1 = new SQLiteHandler(getApplicationContext());
        car_latitude = Double.parseDouble(incoming_intent.getStringExtra("car_latitude"));
        car_longitude = Double.parseDouble(incoming_intent.getStringExtra("car_longitude"));

        locationA = new Location("point A");

        HashMap<String, String> user = db1.getUserDetails();

         final String user_id = user.get("id");
        // String email = user.get("email");
        Log.d("user_id",">>"+user_id);


        if (ActivityCompat.checkSelfPermission(carActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //  Log.d("errorrrrrrrrr",">>" + "hnaa");

            return;

        }
        client.getLastLocation().addOnSuccessListener(carActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location!=null) {
//my location
                    latitude=location.getLatitude();
                    Log.d("lat",">>" + latitude);
                    longitude=location.getLongitude();
                    Log.d("logt",">>" + longitude);



                }
                else{
                    Log.d("error",">>" +"nulllllll");
                }
            }
        });



        mapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Uri gmmIntentUri = Uri.parse("google.navigation:q=latitude,longitude");
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                startActivity(mapIntent);

                Intent intent = new Intent(Intent.ACTION_VIEW,

                        Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d" +
                                "&saddr=" + latitude +
                                "," + longitude + "&daddr=" +
                                car_latitude +
                                "," + car_longitude +
                                "&hl=zh&t=m&dirflg=d"));

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivityForResult(intent, 1);
//

            }
        });

        Carname.setText("Model Name :" + carname);


        reviews = (Button) findViewById(R.id.submit_review);
        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(carActivity.this, review.class);
                intent1.putExtra("carname", carname);
                intent1.putExtra("car_id", car_id);
               intent1.putExtra("user_id",user_id);

                startActivity(intent1);

            }
        });


        retrieveJSON();
    }

        private void retrieveJSON() {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_retrieve_data,
                    new Response.Listener<String>() {

                        public void onResponse(String response) {

                            Log.d("strrrrr", ">>" + response);

                            try {
                                JSONArray obj = new JSONArray(response);

                              //  dataModelArrayList = new ArrayList<>();
                                Log.d("length",">>" +obj.length());

                                for (int i = 0; i < obj.length(); i++) {

                                    //  DataModel2 playerModel = new DataModel2();
                                      JSONObject dataobj = obj.getJSONObject(i);
                                      Log.d("object",">>" +dataobj);
                                      String x =dataobj.getString("car_id");
                                    if (x.equals(car_id)){
                                        dataModelArrayList.add(dataobj.getString("user_review"));
                                        Log.d("review",">>" +dataobj.getString("user_review"));
                                    }
                                    setupListview();
                                    //  Reviews_List.add(playerModel);

                                  // dataModelArrayList.add(playerModel);
                                }


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
       // removeSimpleProgressDialog();  //will remove progress dialog
        listadapter = new CustomAdapter2(this, dataModelArrayList);
        listview.setAdapter(listadapter);
    }




}

