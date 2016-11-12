package com.example.saloni.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


//References : Slides from Class Lectures, AndroidDevelopers Official site

public class MainActivity extends AppCompatActivity {


   // EditText urlEditText;
    TextView messageTextView;
    Button downloadBtn;
    TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  urlEditText = (EditText) findViewById(R.id.urlEditText);
        messageTextView = (TextView) findViewById(R.id.MessageTextView);
        errorTextView = (TextView) findViewById(R.id.errorTextView);
        downloadBtn = (Button) findViewById(R.id.downloadBtn);


        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String url = urlEditText.getText().toString();
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
                if (networkinfo != null && networkinfo.isConnected()) {


                    new DownloadTask().execute("https://www.iiitd.ac.in/about");

                }
                else
                    errorTextView.setText("Uanable to download. See Network Connectivity.");
                    messageTextView.setText("");
            }
        });

    }

    private class DownloadTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result)
        {


            Log.d("About", result);
            messageTextView.setText(result);
            errorTextView.setText("");
            Log.d("AboutIIITD",result);

        }



    }

    public String downloadUrl(String myUrl) throws IOException {
        InputStream inputStream=null;
        int dataLen =700;

        try {
//            URL url = new URL(myUrl);
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setReadTimeout(10000);
//            con.setRequestMethod("GET");
//            con.setConnectTimeout(15000);
//            con.setDoInput(true);
//            con.connect();
//            int response = con.getResponseCode();
//            inputStream = con.getInputStream();
//            String contentToString = convertToString(inputStream, dataLen);
//            return contentToString;
            Document document = Jsoup.connect(myUrl).get();
            String content = document.text();
            return content;
        }

        finally
        {
             if(inputStream !=null)
                 try {
                     inputStream.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
        }


    }

    public String convertToString(InputStream stream, int len)
            throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

}



