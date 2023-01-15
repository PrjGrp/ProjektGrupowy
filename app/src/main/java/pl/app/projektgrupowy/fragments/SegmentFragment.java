package pl.app.projektgrupowy.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import pl.app.projektgrupowy.R;
import pl.app.projektgrupowy.assets.Segment;
import pl.app.projektgrupowy.assets.Translation;
import pl.app.projektgrupowy.main.MainActivity;
import pl.app.projektgrupowy.main.MainViewModel;

public class SegmentFragment extends Fragment {
    private MainActivity mainActivity;
    private MainViewModel mainViewModel;
    private ProgressDialog progressDialog;
    private TextView source;
    private EditText target;
    private Button sendButton;

    private Boolean sendClicked;
    private Segment editedSegment;

    public SegmentFragment() {
        super(R.layout.fragment_segment);
    }

    public void updateTranslation() {
        Translation editedTranslation = mainViewModel.getEditedTranslation().getValue();
        UpdateTranslation updateTranslation = new UpdateTranslation();
        updateTranslation.execute(Integer.toString(editedTranslation.getId()), editedTranslation.getSourceText(),
                editedTranslation.toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();

        mainActivity.invalidateOptionsMenu();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = mainActivity.mainViewModel;

        Translation editedTranslation = mainViewModel.getEditedTranslation().getValue();
        sendClicked = false;
        editedSegment = editedTranslation.segments[mainViewModel.chosenSegment().getValue()];

        source = (TextView) view.findViewById(R.id.segment_source_textview);
        target = (EditText) view.findViewById(R.id.segment_translation_edittext);
        sendButton = (Button) view.findViewById(R.id.segment_send_button);

        source.setText(editedSegment.getSourceText());
        target.setText(editedSegment.getTargetText() != null ? editedSegment.getTargetText() : "");

        target.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                editedSegment.translate(editable.toString());
            }
        });

        sendButton.setOnClickListener(view1 -> {
            sendClicked = true;
            updateTranslation();
        });
    }
    private class UpdateTranslation extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            try {
                if (!s.equals("")) {
                    if (sendClicked) {
                        mainViewModel.chosenSegment().setValue(null);
                        mainActivity.getSupportFragmentManager().popBackStack();
                        sendClicked = false;
                    }
                    Toast.makeText(mainActivity.getApplication(), getString(R.string.patch_post_async_respond), Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(mainActivity.getApplication(), getString(R.string.patch_post_async_noRespond), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            try {
                int id = Integer.parseInt(strings[0]);
                String text = strings[1];
                String translatedText = strings[2];

                URL url;
                HttpsURLConnection urlConnection = null;
                try {
                    url = new URL(getString(R.string.REST_API_URL) + "/Translation");
                    urlConnection = (HttpsURLConnection) url.openConnection();

                    urlConnection.setRequestMethod("PATCH");
                    urlConnection.setRequestProperty("Accept", "*/*");
                    urlConnection.setRequestProperty("Authorization", "Bearer " + mainActivity.mainViewModel.getToken().getValue());
                    urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);

                    JSONObject jsonInput = new JSONObject();

                    jsonInput.put("id", id);
                    jsonInput.put("text", text);
                    jsonInput.put("translatedText", translatedText);
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

                } catch (Exception e) {
                    e.printStackTrace();
                    return getString(R.string.patch_text_error);
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return getString(R.string.patch_text_error);
            }

            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.patch_dialog_async));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
    }
}
