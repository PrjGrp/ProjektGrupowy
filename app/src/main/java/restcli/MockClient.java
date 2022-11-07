package restcli;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MockClient extends AsyncTask<String, String, String> {

    ProgressDialog progressDialog;
    String result = "";
    @Override
    protected String doInBackground(String... strings) {
        URL url;
        HttpsURLConnection urlConnection = null;

        try {
            url = new URL("https://catfact.ninja/fact");

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
            if (urlConnection != null) urlConnection.disconnect();
        }

        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String s) {
        // Code here
        try {

            JSONObject jsonObject = new JSONObject(s);


            return jsonObject.getString("fact");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
