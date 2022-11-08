package pl.app.projektgrupowy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Klasa mockowa, można coś tu przetestować, dopóki główną Activity nie będzie ekran logowania.
 * Aktualnie realizuje klienta http.
 */
public class MainActivity extends AppCompatActivity {
    private final String myUrl = "https://catfact.ninja/fact";

    private TextView resultsTextView;
    private Button myButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultsTextView = (TextView) findViewById(R.id.results);
        myButton = (Button) findViewById(R.id.button);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetSomethingAsync aTask = new GetSomethingAsync();
                aTask.execute();
            }
        });
    }

    /**
     * Wewnętrzna klasa rozszerzająca AsyncTask, do strzałów na resta.
     * Zmieniamy tutaj podejście do komunikacji z bazą. Nie będziemy mieli jednego zbiorczego serwisu
     * dla całej apki, ale każda klasa, w miarę potrzeby będzie miała taką klasę wewnętrzną do strzałów.
     * Jakby na przyszłość ktoś potrzebował, no to, przeciążamy trzy metody: to co się wywołuje przed
     * strzałem (onPreExecute), sam strzał (doInBackground) i to co na koniec z wynikiem ze strzału
     * (onPostExecute).
     * Warto doczytać docsy jakieś o AsyncTask.
     */
    private class GetSomethingAsync extends AsyncTask<String, String, String> {
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                resultsTextView.setVisibility(View.VISIBLE);
                resultsTextView.setText(jsonObject.getString("fact"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                URL url;
                HttpsURLConnection urlConnection = null;
                try {
                    url = new URL(myUrl);
                    urlConnection = (HttpsURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while (data != -1) {
                        result += (char) data;
                        data = isw.read();
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
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage(getString(R.string.mock_dialog_1));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
    }
}