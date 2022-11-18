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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import pl.app.projektgrupowy.MainActivity;
import pl.app.projektgrupowy.R;

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

        Spinner entryLanguageSpinner = view.findViewById(R.id.entryLanguageList);
        Spinner receivedLanguageSpinner = view.findViewById(R.id.receivedLanguageListLanguageList);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mainActivity, R.array.languagesList, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        entryLanguageSpinner.setAdapter(adapter);
        entryLanguageSpinner.setOnItemSelectedListener(this);
        receivedLanguageSpinner.setAdapter(adapter);
        receivedLanguageSpinner.setOnItemSelectedListener(this);



        Button addButton = (Button) view.findViewById(R.id.addButton);

        addButton.setOnClickListener(view1 -> {

                 //  ...
        });
    }


    /**
     *
     Metody interfejsu wywoływane przy rozwinięciu jednej z dwóch list rozwijanych.
     Pierwsza reaguje na zaznaczenie, a druga na rozwinięcie bez zaznaczenia
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getActivity().getApplication(), getString(R.string.nothingSelected), Toast.LENGTH_SHORT).show();
    }
}