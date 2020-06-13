package com.example.cafemoa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.cafemoa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;


public class SignupActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "203.237.179.120:7003";
    private static String TAG = "phpsignup";

    private EditText mEditTextID;
    private EditText mEditTextPassword;
    private EditText mEditTextEmail;
    private EditText mEditTextPhone;
    private EditText mEditTextSort;
    private TextView mTextViewResult;
    private boolean validate=false;
    private   AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mEditTextID = (EditText)findViewById(R.id.et_id);
        mEditTextPassword = (EditText)findViewById(R.id.et_pass);
        mEditTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);



        mEditTextEmail = (EditText)findViewById(R.id.et_email);
        mEditTextPhone = (EditText)findViewById(R.id.et_phone);
        mEditTextSort = (EditText)findViewById(R.id.et_sort);
        mTextViewResult = (TextView)findViewById(R.id.textView_result);

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());


        Button buttonInsert = (Button)findViewById(R.id.btn_register);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ID = mEditTextID.getText().toString();
                String Password = mEditTextPassword.getText().toString();
                String Email = mEditTextEmail.getText().toString();
                String Phone = mEditTextPhone.getText().toString();
                String Sort = mEditTextSort.getText().toString();

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/insert.php", ID,Password,Email,Phone,Sort);

                mEditTextID.setText("");
                mEditTextPassword.setText("");
                mEditTextEmail.setText("");
                mEditTextPhone.setText("");
                mEditTextSort.setText("");


                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                dialog = builder.setMessage("축하합니다.회원가입에 성공하셨습니다!")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                SignupActivity.this.startActivity(intent);
                            }
                        })


                        .create();
                dialog.show();

            }
        });

        Button btn_login=(Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent =new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }



    class InsertData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SignupActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);

          /* if (result == "success") {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                dialog = builder.setMessage("축하합니다.회원가입에 성공하셨습니다!")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                SignupActivity.this.startActivity(intent);
                            }
                        })


                        .create();
                dialog.show();

            }


            }
        */
        }

        @Override
        protected String doInBackground(String... params) {

            String userID = (String)params[1];
            String userPassword = (String)params[2];
            String email = (String)params[3];
            String phoneNumber = (String)params[4];
            String userSort = (String)params[5];


            String serverURL = (String)params[0];
            String postParameters = "userID=" + userID + "&userPassword=" + userPassword + "&email=" + email + "&phoneNumber=" + phoneNumber + "&userSort=" + userSort;


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
                String line = null;

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


}