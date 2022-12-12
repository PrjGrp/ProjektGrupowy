
package pl.app.projektgrupowy.fragments;


import static pl.app.projektgrupowy.assets.Translation.*;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import pl.app.projektgrupowy.main.MainActivity;
import pl.app.projektgrupowy.R;
import pl.app.projektgrupowy.assets.Translation;

/**
 * Klasa realizująca dodawanie fragmentów
 *
 * TODO:
 * Masz tutaj pole do działania, zrób proszę tak jak pisałem w zgłoszeniu, jest tam też screenshot.
 * WAŻNE: Nie rób na razie komunikacji z backendem, po prostu poustawiaj ładnie pola, i zrób walidację
 * Ogranicz się proszę do zmian w plikach fragment_add.xml i AddFragment.java, jak będziesz chciał coś innego ruszać to daj znać, nie chce mi się później na tych mergach działać
 */

public class AddFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private MainActivity mainActivity;// TUTAJ MASZ JUZ REFERENCJE DO ACTIVITY, NIE MUSISZ ZA KAZDYM RAZEM getActivity() wołac

    private String sourceLanguageSpinnerValue = "";
    private String targetLanguageSpinnerValue = "";
    private static int count = 0;
    Translation translation;

    public AddFragment() { super(R.layout.fragment_add); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();

        EditText editTextMultiLine = (EditText) view.findViewById(R.id.editTextMultiLine);

        final Spinner sourceLanguageSpinner = view.findViewById(R.id.sourceLanguageList);
        final Spinner targetLanguageSpinner = view.findViewById(R.id.targetLanguageList);

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

        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mainActivity, R.array.add_languages_list, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sourceLanguageSpinner.setAdapter(dataAdapter);
        targetLanguageSpinner.setAdapter(dataAdapter);


        Button addButton = (Button) view.findViewById(R.id.addButton);

        addButton.setOnClickListener(view1 -> {


            // wczytanie tekstu z ustawieniami języka źródłowego i docelowego
            translation = new Translation(Integer.toString(count++), String.valueOf(editTextMultiLine.getText()), sourceLanguageSpinnerValue, targetLanguageSpinnerValue);

            // prezentacja wyniku obiektu translate (na razie w formie xliffa)
            //editTextMultiLine.setText(translation.toString());
            editTextMultiLine.setText(translation.getSourceText());


            SendToTranslate sendToTranslate = new SendToTranslate();

            //Czy jest poprawnie ??:
            if(!translation.getSourceText().equals("")) sendToTranslate.execute(Integer.toString(count++), "Tomek", translation.toString());

            else Toast.makeText(getActivity().getApplication(), getString(R.string.add_text_invalid), Toast.LENGTH_SHORT).show();
        });
    }


    /**
     Metody interfejsu AdapterView.OnItemSelectedListener wywoływane przy rozwinięciu jednej z dwóch list rozwijanych.
     Pierwsza reaguje na zaznaczenie, a druga na rozwinięcie bez zaznaczenia
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;

        if(spinner.getId() == R.id.sourceLanguageList){
            //jeśli zmiany zachodzą na lewym spinnerze
            sourceLanguageSpinnerValue = parent.getSelectedItem().toString();
        }
        else if(spinner.getId() == R.id.targetLanguageList){
            //jeśli zmiany zachodzą na prawym spinnerze
            targetLanguageSpinnerValue = parent.getSelectedItem().toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(mainActivity, getString(R.string.add_nothing_selected), Toast.LENGTH_SHORT).show();
    }

     private class SendToTranslate extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            //progressDialog.dismiss();

            // czy wprowadzać tu toast po wczytaniu ?
            /*
            try {
                if (!s.equals("")) {
                    mainActivity.invalidateOptionsMenu();     //zmienic toast
                    Toast.makeText(mainActivity.getApplication(), getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(mainActivity.getApplication(), getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            */
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";


            try {
                String id = strings[0];
                String userId = strings[1];
                String text = strings[2];
                URL url;
                HttpsURLConnection urlConnection = null;
                try {
                    url = new URL(getString(R.string.REST_API_URL) + "/Translation");
                    urlConnection = (HttpsURLConnection) url.openConnection();

                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "*/*");

                    JSONObject jsonInput = new JSONObject();
                    jsonInput.put("id", id);
                    jsonInput.put("userId", userId); // można puste
                    jsonInput.put("text", text);
                    //jsonInput.put("translatedText", username);  // w tym fragmencie pusty wysłać
                    // gdzie wczytać ma się tekst i czy na pewno put
                                                // gdzie klase abstrakcyją użyć - moze wzorowac sie na aktiwitis

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
                return getString(R.string.add_text_error);
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