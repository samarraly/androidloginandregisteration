package guc.edu.androidloginandregisteration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import guc.edu.loginandregistration.R;
import helper.SQLiteHandler2;

public class review extends AppCompatActivity {
    private static final String TAG = review.class.getSimpleName();
    EditText review_text;
    Button submit_button;
    ProgressDialog pDialog;
    private SQLiteHandler2 db;
   String car_id;

   // private SQLiteHandler db1;




    //public String car_id="2";
   public String  user_id ;
    String car_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Intent incoming_intent = getIntent();
      //  longitude =Double.parseDouble(incoming_intent.getStringExtra("car_longitude"));
       // latitude = Double.parseDouble(incoming_intent.getStringExtra("car_latitude"));
        final String user_email = incoming_intent.getStringExtra("user_email");
        Log.d("user_email(review)",">>"+user_email);
        car_name=incoming_intent.getStringExtra("carname");
        car_id=incoming_intent.getStringExtra("car_id");
        user_id=incoming_intent.getStringExtra("user_id");

     //   db1 = new SQLiteHandler(getApplicationContext());

  //     user_id=2+"";
        review_text = (EditText) findViewById(R.id.review_t);
        submit_button = (Button) findViewById(R.id.submit_b);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        db = new SQLiteHandler2(getApplicationContext());



        submit_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                final String user_review = review_text.getText().toString().trim();
                Log.d("user_review", ">>" + user_review);
                Log.d("model_name", ">>" + car_name);

                Log.d("car_id", ">>" + car_id);


               store_data(car_name,user_review,user_id,car_id);

            }
        });





    }






             //   String tag_string_req = "req_store";
      private void store_data(final String car_name,final String user_review ,final String user_id,final String car_id){
              StringRequest stringRequest =new StringRequest(Request.Method.POST, AppConfig.URL_store_data, new Response.Listener<String>() {

                 // String email = user.get("email");

                  public void onResponse(String response) {
                    //review_text.setText("");
                    Log.d(TAG, "data Response: " + response);
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        if (!error) {
                            // review successfully stored in MySQL
                            // Now store the data in sqlite
                            Log.i("hi","e4t8ltt");
                            JSONObject review = jObj.getJSONObject("review");
                            String car_name = review.getString("car_name");
                            String user_review = review.getString("user_review");
//                            String car_id = review.getString("car_id");
//                            String user_id = review.getString("user_id");
//                            // Inserting row in users table
                            db.addUser(car_name,user_review);

                            Toast.makeText(getApplicationContext(), "data successfully added.", Toast.LENGTH_LONG).show();

//                            // Launch login activity
//                            Intent intent = new Intent(
//                                    RegisterActivity.this,
//
//      LoginActivity.class);
//                            startActivity(intent);
//                            finish();
                        } else {
                            Log.i("hi","oufa");
                            // Error occurred in storing. Get the error
                            // message
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(review.this, "Something went wrong", Toast.LENGTH_LONG).show();
//                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Storing Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String, String> getParams()  {
                    Map<String,String>params=new HashMap<String, String>();
                   params.put("car_id",car_id);
                    params.put("car_name",car_name);
                    params.put("user_review",user_review);
                   params.put("user_id", user_id);

                    return params;
                }
            };
            RequestQueue requestQueue = (RequestQueue)Volley.newRequestQueue(this);

            requestQueue.add(stringRequest);
        }


    }