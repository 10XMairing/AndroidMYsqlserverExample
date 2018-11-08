package com.hashtech.tenx.androidmysqlserverexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnPost, btnFetch;
    TextView tvResult;
    EditText etFirst, etlast, etAge;
    RequestQueue requestQueue;
    //TODO enter local ip here
    String requestUrl = "http://localhost/test/showStudent.php";
    String insertUrl = "http://localhost/test/insertStudent.php";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tv_result);
        btnFetch = findViewById(R.id.btn_fetch);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        etFirst = findViewById(R.id.et_first);
        etlast = findViewById(R.id.et_last);
        etAge = findViewById(R.id.et_age);

        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,requestUrl,null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       try{
                           JSONArray students = response.getJSONArray("students");
                           Log.d("test", "student list size : "+students.length());
                           for(int i=0; i<students.length(); i++){
                               JSONObject student = students.getJSONObject(i);
                               String firstname = student.getString("firstname");
                               String lastname = student.getString("lastname");
                               String age = student.getString("age");

                               tvResult.append("fnname : "+firstname+" lname : "+lastname+ " age : "+age+"\n-------\n");

                           }


                       }catch (JSONException e){
                           e.printStackTrace();
                           Log.d("test", "exception ");
                       }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test", "error fetching data");
                    }
                });
                requestQueue.add(jsonObjectRequest);
            }
        });


        btnPost = findViewById(R.id.btn_post);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("test", "response recieved");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test", "error inserting data");
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Log.d("test", "get paramns called ");
                        Map<String, String> data = new HashMap<>();
                        String fname = etFirst.getText().toString();
                        String lname = etlast.getText().toString();
                        String age = etAge.getText().toString();
                        data.put("firstname", fname);
                        data.put("lastname", lname);
                        data.put("age", age);
                        return data;

                    }
                };
                requestQueue.add(request);
            }
        });

    }
}
