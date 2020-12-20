package pro.ansori.cuaca;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLoc;
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
    public String capitalizeFirstLetter(String original) {
        String full = "";
        String[] originalArr = original.split(" ");
        if (original == null || original.length() == 0) {
            return original;
        }
        for (String word : originalArr){
            full += word.substring(0, 1).toUpperCase() + word.substring(1) + " ";
        }
        return full;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences session = getSharedPreferences("session", Context.MODE_PRIVATE);
        Intent intent = new Intent(this, AuthActivity.class);

        if (session.getString("session", "").isEmpty()) {
            startActivity(intent);
        }

        fusedLoc = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    1000
            );
        }

        ListView listview;
        listview = findViewById(R.id.foreCastList);

        fusedLoc.getLastLocation().addOnSuccessListener(this, location -> {
            Toast.makeText(getApplicationContext(), "wes masuk", Toast.LENGTH_SHORT).show();
            if (location != null) {
                Geocoder geocode;
                List<Address> addresses;
                geocode = new Geocoder(this, Locale.getDefault());

                try {
                    TextView tvSuhu, tvDesc, citynya;

                    addresses = geocode.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    citynya = findViewById(R.id.tvCity);
                    citynya.setText(addresses.get(0).getLocality());

                    session.getString("session", "");

                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    Request request = new Request.Builder()
                            .url(getResources().getString(R.string.api_url) +"/android/api/weathers/forecast?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&lang=" + addresses.get(0).getLocale().getCountry().toLowerCase())
                            .method("GET", null)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .addHeader("Authorization", "Basic " + session.getString("session", ""))
                            .build();

                    ResponseBody response = client.newCall(request).execute().body();
                    JSONObject jsonFore = new JSONObject(response.string());
                    JSONObject jsonData = jsonFore.getJSONObject("data");
                    JSONObject currentWeather = jsonData.getJSONObject("current");
                    JSONArray forecastWeather = jsonData.getJSONArray("hourly");

                    tvSuhu = findViewById(R.id.tvSuhu);
                    tvDesc = findViewById(R.id.tvDesc);
                    tvSuhu.setText(String.valueOf(currentWeather.getInt("temp")));
                    tvDesc.setText(capitalizeFirstLetter(currentWeather.getJSONArray("weather").getJSONObject(0).getString("description")));

                    ForeCast_List adapter;
                    JSONObject map;
                    JSONArray listForecast;

                    listForecast = new JSONArray();
                    for (int i = 0; i < 5; i++){
                        Date date = new Date(forecastWeather.getJSONObject(i).getLong("dt") * 1000L);
                        SimpleDateFormat jdf = new SimpleDateFormat("hh:mm a");
                        jdf.setTimeZone(TimeZone.getTimeZone(jsonData.getString("timezone")));
                        String forecastTime = jdf.format(date);
                        map = new JSONObject();
                        map.put("time", forecastTime);
                        map.put("suhu", String.valueOf(forecastWeather.getJSONObject(i).getInt("temp")) + "°C / " + String.valueOf(forecastWeather.getJSONObject(i).getInt("feels_like")) + "°C");
                        map.put("icon", "https://openweathermap.org/img/wn/"+ forecastWeather.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon") +"@2x.png");
                        listForecast.put(map);
                    }
                    adapter = new ForeCast_List(getApplicationContext(), R.layout.forecast_view, listForecast);
                    listview.setAdapter(adapter);

                } catch (IOException | JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error at: " + e, Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(e -> {
            Intent intentlah = new Intent(this, AuthActivity.class);
        });

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            SharedPreferences.Editor sessEditor = session.edit();
            sessEditor.remove("session");
            sessEditor.apply();
            startActivity(intent);
        });

        listview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}