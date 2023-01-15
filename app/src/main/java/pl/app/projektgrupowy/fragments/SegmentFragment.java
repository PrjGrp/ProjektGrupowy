package pl.app.projektgrupowy.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pl.app.projektgrupowy.R;
import pl.app.projektgrupowy.main.MainActivity;
import pl.app.projektgrupowy.main.MainViewModel;

public class SegmentFragment extends Fragment {

    private MainActivity mainActivity;
    private MainViewModel mainViewModel;
    private ProgressDialog progressDialog;

    public SegmentFragment() {
        super(R.layout.fragment_segment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mainViewModel = mainActivity.mainViewModel;

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
    }
}
