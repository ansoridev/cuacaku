package pro.ansori.cuaca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class RegisterFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        view.findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RegisterFragment.this)
                        .navigate(R.id.register_to_login);
            }
        });
        view.findViewById(R.id.btnRegister).setOnClickListener(v -> {
            EditText etNama = view.findViewById(R.id.etNameReg);
            EditText etEmail = view.findViewById(R.id.etEmailReg);
            EditText etPassword = view.findViewById(R.id.etPasswordReg);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"name\": \"" + etNama.getText() + "\",\r\n    \"email\": \"" + etEmail.getText() + "\",\r\n    \"password\": \"" + etPassword.getText() + "\"\r\n}");
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.api_url) + "/android/api/users")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            try {
                ResponseBody response = client.newCall(request).execute().body();
                JSONObject jsonObj = new JSONObject(response.string());
                Boolean statusRes = jsonObj.getBoolean("status");
                if (statusRes){
                    SharedPreferences session = getActivity().getSharedPreferences("session", getContext().MODE_PRIVATE);
                    SharedPreferences.Editor sesEdit = session.edit();
                    sesEdit.putString("session", jsonObj.getJSONObject("data").getString("authorization"));
                    sesEdit.apply();
                    Toast.makeText(getActivity(), "Hi " + jsonObj.getJSONObject("data").getString("name"), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), jsonObj.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (IOException | JSONException e) {
                Toast.makeText(getActivity(), "Error at: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}