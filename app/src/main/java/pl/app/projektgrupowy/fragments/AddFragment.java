package pl.app.projektgrupowy.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
public class AddFragment extends Fragment {
    private MainActivity mainActivity; // TUTAJ MASZ JUZ REFERENCJE DO ACTIVITY, NIE MUSISZ ZA KAZDYM RAZEM getActivity() wołac

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
    }
}