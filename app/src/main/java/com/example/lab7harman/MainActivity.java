package com.example.lab7harman;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText etCityName;
    private Button btnGetWeather;
    private TextView tvWeatherResult;
    private ImageView ivWeatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCityName = findViewById(R.id.etCityName);
        btnGetWeather = findViewById(R.id.btnGetWeather);
        tvWeatherResult = findViewById(R.id.tvWeatherResult);
        ivWeatherIcon = findViewById(R.id.ivWeatherIcon);

        btnGetWeather.setOnClickListener(view -> {
            String cityName = etCityName.getText().toString();
            if (!cityName.isEmpty()) {
                fetchWeatherData(cityName);
            } else {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchWeatherData(String cityName) {
        String apiKey = "e0435e24d6ba207ce5247ff58cabefb4";
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey + "&units=metric";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extract data from the JSON response
                            JSONObject main = response.getJSONObject("main");
                            double temp = main.getDouble("temp");
                            int humidity = main.getInt("humidity");
                            String weatherDescription = response.getJSONArray("weather")
                                    .getJSONObject(0).getString("description");

                            String iconCode = response.getJSONArray("weather")
                                    .getJSONObject(0).getString("icon");

                            // Display the data in the TextView
                            String result = "Temperature: " + temp + "°C\n" +
                                    "Condition: " + weatherDescription + "\n" +
                                    "Humidity: " + humidity + "%";
                            tvWeatherResult.setText(result);

                            // Load the weather icon
                            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
                            loadWeatherIcon(iconUrl);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue
        queue.add(jsonObjectRequest);
    }

    private void loadWeatherIcon(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        ImageLoader imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
            }
        });

        imageLoader.get(url, ImageLoader.getImageListener(ivWeatherIcon, R.drawable.place, R.drawable.error));
    }
}
