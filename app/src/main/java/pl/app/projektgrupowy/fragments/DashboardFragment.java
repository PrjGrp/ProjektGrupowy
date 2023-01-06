package pl.app.projektgrupowy.fragments;

import static java.lang.Thread.currentThread;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;

import javax.net.ssl.HttpsURLConnection;

import pl.app.projektgrupowy.adapters.DashboardAdapter;
import pl.app.projektgrupowy.assets.Translation;
import pl.app.projektgrupowy.main.MainActivity;
import pl.app.projektgrupowy.R;


public class DashboardFragment extends Fragment {

    private MainActivity mainActivity;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DashboardAdapter adapter;
    private View root;

    public DashboardFragment() {
        super(R.layout.fragment_dashboard);
    }

    public void setRecyclerViewAdapter(Translation[] dataSet) {
        DashboardAdapter adapter = new DashboardAdapter(dataSet);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.dashboard_recycler_view);
        layoutManager = new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(layoutManager);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { super.onViewCreated(view, savedInstanceState); }

    @Override
    public void onResume() {
        super.onResume();
        Translation[] dataSet = mainActivity.mainViewModel.getDataSet().getValue();
        if (dataSet == null) {
            LoadData loadData = new LoadData();
            loadData.execute("");
        } else setRecyclerViewAdapter(dataSet);
    }

    private class LoadData extends AsyncTask<String, String, Translation[]> {
        @Override
        protected void onPostExecute(Translation[] result) {
            progressDialog.dismiss();
            mainActivity.mainViewModel.getDataSet().setValue(result);
        }

        @Override
        protected Translation[] doInBackground(String ... string) {
            ArrayList<Translation> translationsTemp = new ArrayList<>();

            try {
                URL url;
                HttpsURLConnection urlConnection = null;
                try {
                    url = new URL(getString(R.string.REST_API_URL) + "/Translation");
                    urlConnection = (HttpsURLConnection) url.openConnection();

                    urlConnection.setRequestMethod("GET");
                    urlConnection.setRequestProperty("Accept", "*/*");
                    urlConnection.setRequestProperty("Authorization", "Bearer " + mainActivity.mainViewModel.getToken().getValue());
                    urlConnection.setDoInput(true);

                    if (urlConnection.getResponseCode() == 200) {
                        InputStream response = urlConnection.getInputStream();
                        InputStreamReader responseReader = new InputStreamReader(response, StandardCharsets.UTF_8);
                        JsonReader jsonReader = new JsonReader(responseReader);

                        jsonReader.beginArray();
                        while (jsonReader.hasNext()) {
                            String xliff = "";
                            int id = 0;
                            jsonReader.beginObject();

                            while (jsonReader.hasNext()) {
                                String name = jsonReader.nextName();
                                switch (name) {
                                    case "translatedText":
                                        xliff = jsonReader.nextString();
                                        break;
                                    case "id":
                                        id = jsonReader.nextInt();
                                        break;
                                    default:
                                        jsonReader.skipValue();
                                }
                            }
                            jsonReader.endObject();
                            translationsTemp.add(Translation.parseXliff(xliff, id));
                        }
                        jsonReader.endArray();
                    }

                    Translation[] result = new Translation[translationsTemp.size()];
                    return translationsTemp.toArray(result);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mainActivity);
            progressDialog.setMessage(mainActivity.getString(R.string.dashboard_progress));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
    }

}
