package pl.app.projektgrupowy.fragments;

import static pl.app.projektgrupowy.assets.Translation.*;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import pl.app.projektgrupowy.assets.NewTranslation;
import pl.app.projektgrupowy.main.MainActivity;
import pl.app.projektgrupowy.R;
import pl.app.projektgrupowy.assets.Translation;
import pl.app.projektgrupowy.main.MainViewModel;

public class AddFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private MainActivity mainActivity;// TUTAJ MASZ JUZ REFERENCJE DO ACTIVITY, NIE MUSISZ ZA KAZDYM RAZEM getActivity() wołac
    private MainViewModel mainViewModel;

    private ProgressDialog progressDialog;
    NewTranslation translation; // Nowy obiekt ad hoc

    public AddFragment() { super(R.layout.fragment_add); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();

        mainActivity.invalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = mainActivity.mainViewModel;
        if (mainViewModel.getNewTranslationData() == null) mainViewModel.setNewTranslationData(new NewTranslation());

        EditText addTextTitle = (EditText) view.findViewById(R.id.editTextTranslateTitle);
        EditText editTextMultiLine = (EditText) view.findViewById(R.id.editTextMultiLine);
        final Spinner sourceLanguageSpinner = view.findViewById(R.id.sourceLanguageList);
        final Spinner targetLanguageSpinner = view.findViewById(R.id.targetLanguageList);
        Button addButton = (Button) view.findViewById(R.id.addButton);

        // Spinner click listener
        sourceLanguageSpinner.setOnItemSelectedListener(this);
        targetLanguageSpinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add(AMERICAN_ENGLISH);
        categories.add(BRITISH_ENGLISH);
        categories.add(POLISH);
        categories.add(GERMAN);
        categories.add(AUSTRIAN);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sourceLanguageSpinner.setAdapter(dataAdapter);
        targetLanguageSpinner.setAdapter(dataAdapter);

        // Jesli fragment odtwarzany, nie tworzony na nowo, to ustawiamy odpowiednio dane
        translation = mainViewModel.getNewTranslationData();
        if (translation != null) {
            for (String category : categories)
                if (category.equals(translation.sourceLanguage))
                    sourceLanguageSpinner.setSelection(categories.indexOf(category));

            for (String category : categories)
                if (category.equals(translation.targetLanguage))
                    targetLanguageSpinner.setSelection(categories.indexOf(category));

            editTextMultiLine.setText(translation.sourceText);
            addTextTitle.setText(translation.title);
        }
        // Nasłuchiwacz dla tekstu źródłowego
        editTextMultiLine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                translation.sourceText = editable.toString();
                mainViewModel.setNewTranslationData(translation);
            }
        });
        
        addTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                translation.title = editable.toString();
                mainViewModel.setNewTranslationData(translation);
            }
        });
        addButton.setOnClickListener(view1 -> {
            if(translation.validate()) {

                Translation translationProper = new Translation(translation.title, translation.sourceText, translation.sourceLanguage, translation.targetLanguage);

                SendToDB sendToDB = new SendToDB();
                sendToDB.execute(translationProper.getSourceText(), translationProper.toString());
            }else Toast.makeText(getActivity().getApplication(), getString(R.string.add_text_invalid), Toast.LENGTH_SHORT).show();
        });

    }

    /**
     Metody interfejsu AdapterView.OnItemSelectedListener wywoływane przy rozwinięciu jednej z dwóch list rozwijanych.
     Pierwsza reaguje na zaznaczenie, a druga na rozwinięcie bez zaznaczenia
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;
        String newVal = parent.getSelectedItem().toString();

        if (spinner.getId() == R.id.sourceLanguageList)
            translation.sourceLanguage = newVal;
        else if (spinner.getId() == R.id.targetLanguageList)
            translation.targetLanguage = newVal;

        mainViewModel.setNewTranslationData(translation);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(mainActivity, getString(R.string.add_nothing_selected), Toast.LENGTH_SHORT).show();
    }

     private class SendToDB extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            try {
                if (!s.equals("")) {
                    mainViewModel.getDataSet().setValue(null);
                    mainViewModel.setNewTranslationData(new NewTranslation());
                    mainViewModel.getNewTranslation().setValue(false);
                    mainActivity.getSupportFragmentManager().popBackStack();
                    Toast.makeText(mainActivity.getApplication(), getString(R.string.add_post_async_respond), Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(mainActivity.getApplication(), getString(R.string.add_post_async_noRespond), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            try {
                String text = strings[0];
                String translatedText = strings[1];

                URL url;
                HttpsURLConnection urlConnection = null;
                try {
                    url = new URL(getString(R.string.REST_API_URL) + "/Translation");
                    urlConnection = (HttpsURLConnection) url.openConnection();

                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Accept", "*/*");
                    urlConnection.setRequestProperty("Authorization", "Bearer " + mainActivity.mainViewModel.getToken().getValue());
                    urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);

                    JSONObject jsonInput = new JSONObject();

                    jsonInput.put("text", text);
                    jsonInput.put("translatedText", translatedText);

                    OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                    BufferedWriter os = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                    os.write(jsonInput.toString());
                    os.flush();
                    os.close();
                    out.close();

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
                    return getString(R.string.add_text_error);
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return getString(R.string.add_text_error);
            }

            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.add_dialog_async));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
    }

}