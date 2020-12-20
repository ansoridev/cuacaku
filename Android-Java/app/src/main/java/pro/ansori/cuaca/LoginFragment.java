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
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        view.findViewById(R.id.btnSignup).setOnClickListener(v -> NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.login_to_register));

        view.findViewById(R.id.btnLogin).setOnClickListener(v -> {
            EditText etEmail = view.findViewById(R.id.etEmailReg);
            EditText etPassword = view.findViewById(R.id.etPasswordReg);

            String basicAuth = etEmail.getText().toString() + ":" + etPassword.getText().toString();
            Base64.Encoder encoder = Base64.getEncoder();
            basicAuth = encoder.encodeToString(basicAuth.getBytes(StandardCharsets.UTF_8));

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.api_url) + "/android/api/auth")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", "Basic " + basicAuth)
                    .build();

            try {
                ResponseBody response = client.newCall(request).execute().body();
                JSONObject jsonObject = new JSONObject(response.string());
                Boolean statusResp = jsonObject.getBoolean("status");

                if (statusResp){
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    String namanya = jsonData.getString("name").toString();

                    SharedPreferences session = getActivity().getSharedPreferences("session", getContext().MODE_PRIVATE);
                    SharedPreferences.Editor sessionEdit;
                    sessionEdit = session.edit();
                    sessionEdit.putString("session", basicAuth);
                    sessionEdit.apply();
                    Toast.makeText(getContext(), "Hi " + namanya, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                } else {
                    String statusMsg = jsonObject.getString("message");
                    Toast.makeText(getContext(), statusMsg, Toast.LENGTH_LONG).show();
                }

            } catch (IOException | JSONException e){
                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Error at: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}