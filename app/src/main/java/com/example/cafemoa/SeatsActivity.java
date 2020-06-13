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

//좌석

public class SeatsActivity extends AppCompatActivity {
    String name;
    String loginID;
    String loginSort;
    TextView mTextViewResult;
    EditText seatsCount;
    private AlertDialog dialog;

    private static String IP_ADDRESS = "203.237.179.120:7003";
    private static String TAG = "phpseats";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seats);

        mTextViewResult = (TextView) findViewById(R.id.textView_result);
        seatsCount = (EditText) findViewById(R.id.seatsCount);

        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        loginID = intent.getExtras().getString("loginID");
        loginSort = intent.getExtras().getString("loginSort");

        setup();

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());


        Button buttonInsert = (Button) findViewById(R.id.seatssave);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/Seats.php", name, seatsCount.getText().toString());

                AlertDialog.Builder builder = new AlertDialog.Builder(SeatsActivity.this);
                dialog = builder.setMessage("좌석 수가 변경되었습니다.")
                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })

                        .create();
                dialog.show();


                return;

            }
        });
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SeatsActivity.this,
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

            String name = (String) params[1];
            String count = (String) params[2];


            String serverURL = (String) params[0];
            String postParameters = "name=" + name + "&seats=" + count;


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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
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

    private void setup() {
        Button seatsUp = (Button) findViewById(R.id.seatsUp);
        Button seatsDown = (Button) findViewById(R.id.seatsDown);
        Button seatsinitial = (Button) findViewById(R.id.seatsinitial);
        final TextView seatsCount = (TextView) findViewById(R.id.seatsCount);

        seatsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer count = Integer.parseInt("" + seatsCount.getText());
                count++;
                seatsCount.setText("" + count);
            }
        });
        seatsDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer count = Integer.parseInt("" + seatsCount.getText());
                count--;
                seatsCount.setText("" + count);
            }
        });

        seatsinitial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seatsCount.setText("" + 0);
            }
        });

    }
}
