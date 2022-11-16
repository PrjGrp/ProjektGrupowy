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

import com.google.android.material.textfield.TextInputEditText;

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
 * To jest klasa realizująca nasze logowanie do apki.
 */
public class LoginFragment extends Fragment {

    private MainActivity mainActivity;
    private ProgressDialog progressDialog;

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        TextInputEditText textInputEditUsername, textInputEditPassword;

        textInputEditUsername = view.findViewById(R.id.usernameLogin);
        textInputEditPassword = view.findViewById(R.id.passwordLogin);

        mainActivity = (MainActivity) getActivity();
        Button loginButton = (Button) view.findViewById(R.id.login_button);

        loginButton.setOnClickListener(view1 -> {
            GetToken getToken = new GetToken();
            final String usernameString, passwordString;
            usernameString = String.valueOf(textInputEditUsername.getText());
            passwordString = String.valueOf(textInputEditPassword.getText());

            if(!usernameString.equals("") && !passwordString.equals("")) getToken.execute(usernameString, passwordString);
            else Toast.makeText(getActivity().getApplication(), getString(R.string.login_insufficient_data), Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Klasa służąca do asynchronicznego pozyskiwania tokenów, czyli logowania w skrócie.
     */
    private class GetToken extends AsyncTask<String, String, String> {
        /**
         * To, co się dzieje po wykonaniu zadania asynchronicznego
         * @param s To co zwraca metoda doInBackground
         */
        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
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

        /**
         * Instrukcje wykonywane asynchronicznie
         * @param strings Tablica parametrów wejściowych
         * @return To jest przekazywane do onPostExecute
         */
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
                return getString(R.string.mock_exception_1);
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.login_awaiting));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
    }
}