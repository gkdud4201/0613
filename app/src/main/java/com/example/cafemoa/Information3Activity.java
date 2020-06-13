package com.example.cafemoa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

//고객 로그인 시 정보확인

public class Information3Activity extends AppCompatActivity {

    String loginID;
    String loginSort;

    private static String TAG = "phpinfo";

    private static final String TAG_JSON="user";
    private static final String TAG_NAME = "name";
    private static final String TAG_address ="address";
    private static final String TAG_businessHour ="businessHour";
    private static final String TAG_emptySeats ="emptySeats";
    private static final String TAG_instargram ="instargram";
    private static final String TAG_phone ="phone";
    private static final String TAG_inform ="inform";



    private TextView mTextViewResult;
    ArrayList<HashMap<String, String>> mArrayList;
    ListView mListViewList;
    EditText mEditTextSearchKeyword1;
    String mJsonString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        loginID = intent.getExtras().getString("loginID");
        loginSort = intent.getExtras().getString("loginSort");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information3);

        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
        mListViewList = (ListView) findViewById(R.id.listView_main_list);
        mEditTextSearchKeyword1 = (EditText) findViewById(R.id.cafe_search);


        Button button_search = (Button) findViewById(R.id.button_main_search);

        button_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mArrayList.clear();


                GetData task = new GetData();
                task.execute( mEditTextSearchKeyword1.getText().toString());

            }
        });


        mArrayList = new ArrayList<>();


    }


    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Information3Activity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){

                mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];




            String serverURL = "http://203.237.179.120:7003/query_info.php";
            String postParameters = "name=" + searchKeyword1;



            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String name = item.getString(TAG_NAME);
                String address = item.getString(TAG_address);
                String businessHour = item.getString(TAG_businessHour);
                String emptySeats = item.getString(TAG_emptySeats);
                String instargram = item.getString(TAG_instargram);
                String phone = item.getString(TAG_phone);
                String inform = item.getString(TAG_inform);


                HashMap<String,String> hashMap = new HashMap<>();

                hashMap.put(TAG_NAME, name);
                hashMap.put(TAG_address,address);
                hashMap.put(TAG_businessHour,businessHour);
                hashMap.put(TAG_emptySeats,emptySeats);
                hashMap.put(TAG_instargram,instargram);
                hashMap.put(TAG_phone,phone);
                hashMap.put(TAG_inform,inform);



                mArrayList.add(hashMap);
            }

            ListAdapter adapter = new SimpleAdapter(
                    Information3Activity.this, mArrayList, R.layout.cafe_list,
                    new String[]{TAG_NAME,TAG_address,TAG_businessHour,TAG_emptySeats,TAG_instargram,TAG_phone,TAG_inform},
                    new int[]{R.id.cafename, R.id.cafeaddress, R.id.cafehour,R.id.cafeseats,R.id.cafeinsta,R.id.cafephone,R.id.cafeinform}
            );

            mListViewList.setAdapter(adapter);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }



    }



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.review3menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){

            case R.id.see3Review:
                Intent intent1=new Intent(Information3Activity.this,Review2Activity.class);
                intent1.putExtra("name", mEditTextSearchKeyword1.getText().toString());
                intent1.putExtra("loginID", loginID);
                intent1.putExtra("loginSort", loginSort);
                Information3Activity.this.startActivity(intent1);

                break;
            case R.id.write3Review:
                Intent intent2=new Intent(Information3Activity.this,ReviewActivity.class);
                intent2.putExtra("name", mEditTextSearchKeyword1.getText().toString());
                intent2.putExtra("loginID", loginID);
                intent2.putExtra("loginSort", loginSort);
                Information3Activity.this.startActivity(intent2);
                break;

        }
        return  true;
    }

}


