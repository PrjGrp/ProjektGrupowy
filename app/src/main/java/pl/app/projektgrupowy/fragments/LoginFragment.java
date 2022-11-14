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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import pl.app.projektgrupowy.MainActivity;
import pl.app.projektgrupowy.R;

/**
 * To jest klasa realizująca nasze logowanie do apki.
 *
 * TODO:
 * @PawełSiwek
 * Docelowo ma mieć kontrolki na login, hasło (z gwiazdkami zamiast znaków) i przycisk zaloguj.
 * Strzał do resta jest już gotowy, trzeba tylko sparametryzować login i hasło, na razie są na sztywno.
 * Gdzie to robić jest opisane poniżej.
 * Działaj tylko na LoginFragment.java, fragment_login.xml i strings.xml w miarę możliwości.
 * Nie tykaj się layoutu, poustawiaj tylko, wyglądem zajmie się Paweł Kiedrzyński.
 */
public class LoginFragment extends Fragment {

    private MainActivity mainActivity;

    private Button loginButton;
    private TextView loginResult;
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
        // TODO: Tutaj pobierasz referencje do kolejnych widoków (jak jakieś dodasz)
        mainActivity = (MainActivity) getActivity();
        loginButton = (Button) view.findViewById(R.id.login_button);
        loginResult = (TextView) view.findViewById(R.id.login_result);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                GetToken getToken = new GetToken();
                // TODO: Tutaj parametryzujesz hasło i login
                getToken.execute("io", "1");
            }
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
                    mainActivity.setToken(s); // To jest ważne, bo ustawia token na mainActivity
                    mainActivity.replaceLoginFragment();
                }
                else {
                    loginResult.setText(getString(R.string.login_failed));
                    loginResult.setVisibility(View.VISIBLE);
                }
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
                        InputStreamReader responseReader = new InputStreamReader(response, "UTF-8");
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