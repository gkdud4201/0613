package com.example.cafemoa;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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


public class LoginActivity extends AppCompatActivity {

    private static String TAG = "phplogin";

    private static final String TAG_JSON = "user";
    private static final String TAG_ID = "userID";
    private static final String TAG_PASS = "userPassword";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PHONE = "phoneNumber";
    private static final String TAG_SORT = "userSort";

    private TextView mTextViewResult;
    ArrayList<HashMap<String, String>> mArrayList;
    ListView mListViewList;
    private EditText mEditTextID, mEditTextPass;
    Button btn_login, btn_register;
    private String mJsonString;
    private   AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView loginText = (TextView) findViewById(R.id.loginText);
        ImageView loginImage = (ImageView) findViewById(R.id.loginImage);
        CheckBox rememberCheckBox = (CheckBox) findViewById(R.id.rememberCheckBox);
        TextView forgotText = (TextView) findViewById(R.id.forgotText);

        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
        mListViewList = (ListView) findViewById(R.id.listView_main_list);
        mEditTextID = (EditText) findViewById(R.id.et_id);
        mEditTextPass = (EditText) findViewById(R.id.et_pass);

       mEditTextPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });


        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                mArrayList.clear();


                GetData task = new GetData();
                task.execute( mEditTextID.getText().toString(), mEditTextPass.getText().toString());

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

            progressDialog = ProgressDialog.show(LoginActivity.this,
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

            String userID = (String)params[0];
            String userPassword = (String)params[1];

            String serverURL = "http://203.237.179.120:7003/query.php";
            String postParameters = "userID=" + userID + "&userPassword=" + userPassword ;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

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


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

    private void showResult(){

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String userID = item.getString(TAG_ID);
                String userPassword = item.getString(TAG_PASS);
                String email = item.getString(TAG_EMAIL);
                String phoneNumber = item.getString(TAG_PHONE);
                final String userSort = item.getString(TAG_SORT);

                HashMap<String,String> hashMap = new HashMap<>();

                hashMap.put(TAG_ID, userID);
                hashMap.put(TAG_PASS, userPassword);
                hashMap.put(TAG_EMAIL, email);
                hashMap.put(TAG_PHONE, phoneNumber);
                hashMap.put(TAG_SORT, userSort);

                mArrayList.add(hashMap);

                Intent intent = new Intent(LoginActivity.this, Home2Activity.class);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                dialog = builder.setMessage("로그인되었습니다.")
                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(userSort.equals("2")) {
                                    Intent intent = new Intent(LoginActivity.this, Home2Activity.class);
                                intent.putExtra("loginID", mEditTextID.getText().toString());
                                intent.putExtra("loginSort", userSort);
                                startActivity(intent);
                            }
                                else if(userSort.equals("1")) {
                                    Intent intent = new Intent(LoginActivity.this, Home3Activity.class);
                                    intent.putExtra("loginID", mEditTextID.getText().toString());
                                    intent.putExtra("loginSort", userSort);
                                    startActivity(intent);
                                }
                            }
                        })

                        .create();
                dialog.show();


                return;
            }


            ListAdapter adapter = new SimpleAdapter(
                    LoginActivity.this, mArrayList, R.layout.user_list,
                    new String[]{TAG_ID,TAG_PASS,TAG_EMAIL,TAG_PHONE,TAG_SORT},
                    new int[]{R.id.textView_list_id, R.id.textView_list_pass, R.id.textView_list_email,R.id.textView_list_phone,R.id.textView_list_sort}
            );

            mListViewList.setAdapter(adapter);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }


    }

}
