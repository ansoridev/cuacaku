package pro.ansori.cuaca;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ForeCast_List extends BaseAdapter {
    Context context;
    JSONArray dataforecast;
    int layout;
    LayoutInflater inflater;

    public ForeCast_List(Context context, int layout, JSONArray dataforecast){
        this.context = context;
        this.layout = layout;
        this.dataforecast = dataforecast;

        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataforecast.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public Bitmap drawable_from_url(String url) throws java.net.MalformedURLException, java.io.IOException {

        HttpURLConnection connection = (HttpURLConnection)new URL(url) .openConnection();
        connection.setRequestProperty("User-agent","Mozilla/4.0");

        connection.connect();
        InputStream input = connection.getInputStream();

        return BitmapFactory.decodeStream(input);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(this.layout, null);
        TextView tvTime = convertView.findViewById(R.id.tvTime);
        TextView tvSuhuList = convertView.findViewById(R.id.tvSuhuList);
        ImageView iconnya = convertView.findViewById(R.id.iconCuaca);

        try {
            JSONObject objeknya = this.dataforecast.getJSONObject(position);

            tvTime.setText(objeknya.getString("time"));
            tvSuhuList.setText(objeknya.getString("suhu"));
            iconnya.setImageBitmap(this.drawable_from_url(objeknya.getString("icon")));

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
