package pl.app.projektgrupowy.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import pl.app.projektgrupowy.R;
import pl.app.projektgrupowy.adapters.DashboardAdapter;
import pl.app.projektgrupowy.adapters.TranslationAdapter;
import pl.app.projektgrupowy.assets.Segment;
import pl.app.projektgrupowy.assets.Translation;
import pl.app.projektgrupowy.main.MainActivity;
import pl.app.projektgrupowy.main.MainViewModel;

public class TranslationFragment extends Fragment {
    private MainActivity mainActivity;
    private MainViewModel mainViewModel;
    private ProgressDialog progressDialog;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TranslationAdapter adapter;
    private View root;

    public TranslationFragment() { super(R.layout.fragment_translation); }

    public void setRecyclerViewAdapter(Segment[] dataSet) {
        TranslationAdapter adapter = new TranslationAdapter(dataSet, mainActivity);
        recyclerView.setAdapter(adapter);

    }

    public void delete() {
        DeleteTranslation deleteTranslation = new DeleteTranslation();
        deleteTranslation.execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mainViewModel = mainActivity.mainViewModel;
        mainActivity.invalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_translation, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.translation_recycler_view);
        layoutManager = new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(layoutManager);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { super.onViewCreated(view, savedInstanceState); }

    @Override
    public void onResume() {
        setRecyclerViewAdapter(Objects.requireNonNull(mainViewModel.getEditedTranslation().getValue()).segments);
        super.onResume();
    }

    private class DeleteTranslation extends AsyncTask<String, String, Integer> {
        @Override
        protected void onPostExecute(Integer result) {
            progressDialog.dismiss();
            if (result.intValue() == 200) {
                mainViewModel.getEditedTranslation().setValue(null);
                mainActivity.getSupportFragmentManager().popBackStack();
                Toast.makeText(mainActivity.getApplication(), getString(R.string.translation_delete_async_success), Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(mainActivity.getApplication(), getString(R.string.translation_delete_async_fail), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Integer doInBackground(String ... string) {
            try {
                URL url;
                HttpsURLConnection urlConnection = null;
                try {
                    url = new URL(getString(R.string.REST_API_URL) + "/Translation/" + mainViewModel.getEditedTranslation().getValue().getId());
                    urlConnection = (HttpsURLConnection) url.openConnection();

                    urlConnection.setRequestMethod("DELETE");
                    urlConnection.setRequestProperty("Accept", "*/*");
                    urlConnection.setRequestProperty("Authorization", "Bearer " + mainActivity.mainViewModel.getToken().getValue());
                    urlConnection.setDoInput(true);

                    return urlConnection.getResponseCode();

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
            return 0;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mainActivity);
            progressDialog.setMessage(mainActivity.getString(R.string.translation_delete_async_await));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
    }
}
