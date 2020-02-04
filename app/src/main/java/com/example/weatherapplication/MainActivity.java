package com.example.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView cityName;
    Button searchButton;
    TextView result;
    TextView resultCity;
    TextView resultDateTime;

    class Weather extends AsyncTask<String,Void, String>{
        @Override
        protected String doInBackground(String... address) {
            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //Establish connection with address
                connection.connect();

                //retrieve data from url
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                //Retrieve data and return it as String
                int data = isr.read();
                String content = "";
                char ch;
                while (data != -1){
                    ch = (char) data;
                    content = content + ch;
                    data = isr.read();
                }
                Log.i("Content",content);
                return content;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void search(View view){
        cityName = findViewById(R.id.cityName);
        searchButton = findViewById(R.id.searchButton);
        result = findViewById(R.id.result);
        resultCity = findViewById(R.id.result_city);
        resultDateTime = findViewById(R.id.result_datetime);

        String cName = cityName.getText().toString();

        String content;
        Weather weather = new Weather();
        try {
            content = weather.execute("https://openweathermap.org/data/2.5/weather?q="
                    +cName+ "&appid=b6907d289e10d714a6e88b30761fae22").get();
            //checking weather data
            Log.i("contentData",content);

            //JSON
            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("weather");
            String mainTemparature = jsonObject.getString("main"); //this main is not part of weather array, its separate variable like weather
            String cityname = jsonObject.getString("name");

            //weather data is in Array
            JSONArray array = new JSONArray(weatherData);
            String main = "";
            String description = "";
            String temparature;
            String feelslike;
            double visibility;

            for(int i=0; i<array.length(); i++){
                JSONObject weatherPart = array.getJSONObject(i);
                main = weatherPart.getString("main");
                description = weatherPart.getString("description");
            }

            JSONObject mainPart = new JSONObject(mainTemparature);
            temparature = mainPart.getString("temp");
            feelslike = mainPart.getString("feels_like");
            visibility = Double.parseDouble(jsonObject.getString("visibility"));
            //converting visibility in kilometer from meter
            int visibilityInKilometer = (int) (visibility/1000);

            String resultText = temparature +  " \u2103" +" | Feels like " +feelslike+ " \u2103" +
                    "\n" +main + " | " + description +
                    "\nVisibility: "+ visibilityInKilometer;

            //Date and Time
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            Calendar c = Calendar.getInstance();
            String date = sdf.format(c.getTime());

            resultCity.setText(cityname);
            resultDateTime.setText(date);
            result.setText(resultText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
