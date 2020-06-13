package com.example.cafemoa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class InformEditActivity extends AppCompatActivity {

    String loginID;
    String loginSort;

    private static String IP_ADDRESS = "203.237.179.120:7003";
    private static String TAG = "phpinfoadd";

    private EditText  mEditTextName;
    private EditText  mEditTextAddress;
    private EditText  mEditTextHour;
    private EditText  mEditTextInsta;
    private EditText  mEditTextPhone;
    private TextView  mEditTextInform;
    private TextView mTextViewResult;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_edit);

        Intent intent = getIntent();
        loginID = intent.getExtras().getString("loginID");
        loginSort = intent.getExtras().getString("loginSort");

        mEditTextName = (EditText)findViewById(R.id.editName);
        mEditTextAddress = (EditText)findViewById(R.id.editAddress);
        mEditTextHour = (EditText)findViewById(R.id.editHour);
        mEditTextInsta = (EditText)findViewById(R.id.editInsta);
        mEditTextPhone = (EditText)findViewById(R.id.editPhone);
        mEditTextInform = (EditText)findViewById(R.id.editInform);
        mTextViewResult = (TextView)findViewById(R.id.textView_result);

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());


        Button buttonInsert = (Button)findViewById(R.id.editSave);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Name = mEditTextName.getText().toString();
                String Address = mEditTextAddress.getText().toString();
                String Hour = mEditTextHour.getText().toString();
                String Insta = mEditTextInsta.getText().toString();
                String Phone = mEditTextPhone.getText().toString();
                String Inform = mEditTextInform.getText().toString();

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/insertinfo.php", Name,Address,Hour,Insta,Phone,Inform);

                mEditTextName.setText("");
                mEditTextAddress.setText("");
                mEditTextHour.setText("");
                mEditTextInsta.setText("");
                mEditTextPhone.setText("");
                mEditTextInform.setText("");


                AlertDialog.Builder builder = new AlertDialog.Builder(InformEditActivity.this);
                dialog = builder.setMessage("카페 정보를 수정하였습니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(InformEditActivity.this, InformationActivity.class);
                                intent.putExtra("loginID", loginID);
                                intent.putExtra("loginSort", loginSort);
                                InformEditActivity.this.startActivity(intent);
                            }
                        })


                        .create();
                dialog.show();

            }
        });


    }


    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(InformEditActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);

        }

        @Override
        protected String doInBackground(String... params) {

            String name = (String)params[1];
            String address = (String)params[2];
            String hour = (String)params[3];
            String insta = (String)params[4];
            String phone = (String)params[5];
            String inform = (String)params[6];


            String serverURL = (String)params[0];
            String postParameters = "name=" + name + "&address=" + address + "&hour=" + hour + "&insta=" + insta + "&phone=" + phone + "&inform" + inform;


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