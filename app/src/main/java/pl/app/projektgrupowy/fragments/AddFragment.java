
package pl.app.projektgrupowy.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;


import pl.app.projektgrupowy.MainActivity;
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

        TextInputEditText editTextMultiLine = view.findViewById(R.id.editTextMultiLine);

        final Spinner sourceLanguageSpinner = view.findViewById(R.id.sourceLanguageList);
        final Spinner targetLanguageSpinner = view.findViewById(R.id.targetLanguageList);

        // Spinner click listener
        sourceLanguageSpinner.setOnItemSelectedListener(this);
        targetLanguageSpinner.setOnItemSelectedListener(this);


        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("en-GB");
        //categories.add("en-US");
        categories.add("pl-PL");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mainActivity, R.array.add_languages_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sourceLanguageSpinner.setAdapter(dataAdapter);
        targetLanguageSpinner.setAdapter(dataAdapter);


        Button addButton = (Button) view.findViewById(R.id.addButton);

        addButton.setOnClickListener(view1 -> {
            Translation translation;
                 //  ...
        });
    }


    /**
     *
     Metody interfejsu AdapterView.OnItemSelectedListener wywoływane przy rozwinięciu jednej z dwóch list rozwijanych.
     Pierwsza reaguje na zaznaczenie, a druga na rozwinięcie bez zaznaczenia
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;

        if(spinner.getId() == R.id.sourceLanguageList){
            //do this
            sourceLanguageSpinnerValue = parent.getSelectedItem().toString();
        }
        else if(spinner.getId() == R.id.targetLanguageList){
            //do this
            targetLanguageSpinnerValue = parent.getSelectedItem().toString();
        }


        String text = parent.getItemAtPosition(position).toString();

        Toast.makeText(mainActivity, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(mainActivity, getString(R.string.add_nothing_selected), Toast.LENGTH_SHORT).show();
    }
}