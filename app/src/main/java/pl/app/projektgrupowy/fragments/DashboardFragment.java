package pl.app.projektgrupowy.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import pl.app.projektgrupowy.MainActivity;
import pl.app.projektgrupowy.R;

/**
 * Dashboard z tłumaczeniami
 */
public class DashboardFragment extends Fragment {

    private MainActivity mainActivity;
    private TextView helloMsg;

    public DashboardFragment() {
        super(R.layout.fragment_dashboard);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();

        helloMsg = (TextView) view.findViewById(R.id.dashboard_hello);
        helloMsg.setText(mainActivity.getToken());
    }

    @Override
    public void onResume() {
        super.onResume();

        helloMsg.setText(mainActivity.getToken());
    }

    private class GetToken extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            //progressDialog.dismiss();
            try {
                if (!s.equals("")) {
                    mainActivity.setToken(s);
                    mainActivity.replaceLoginFragment();
                    mainActivity.invalidateOptionsMenu();
                    Toast.makeText(mainActivity.getApplication(), getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(mainActivity.getApplication(), getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";


            try {
                String username = strings[0];
                String password = strings[1];
                URL url;
                HttpsURLConnection urlConnection = null;
                try {
                    url = new URL(getString(R.string.REST_API_URL) + "/Auth");
                    urlConnection = (HttpsURLConnection) url.openConnection();

                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "*/*");

                    JSONObject jsonInput = new JSONObject();
                    jsonInput.put("username", username);
                    jsonInput.put("password", password);

                    DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
                    os.writeBytes(jsonInput.toString());
                    os.flush();
                    os.close();

                    if (urlConnection.getResponseCode() == 200) {
                        InputStream response = urlConnection.getInputStream();
                        InputStreamReader responseReader = new InputStreamReader(response, StandardCharsets.UTF_8);
                        JsonReader jsonReader = new JsonReader(responseReader);

                        jsonReader.beginObject();
                        jsonReader.nextName();
                        result = jsonReader.nextString();
                        jsonReader.close();
                    }

                    return result;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return getString(R.string.login_error);
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();/*
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.login_awaiting));
            progressDialog.setCancelable(false);
            progressDialog.show();*/

        }
    }

}
