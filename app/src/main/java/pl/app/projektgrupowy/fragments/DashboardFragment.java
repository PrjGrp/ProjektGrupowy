package pl.app.projektgrupowy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import pl.app.projektgrupowy.MainActivity;
import pl.app.projektgrupowy.R;

public class DashboardFragment extends Fragment {

    private MainActivity mainActivity;

    private TextView helloMsg;

    public DashboardFragment() {
        super(R.layout.fragment_dashboard);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();

        helloMsg = (TextView) view.findViewById(R.id.dashboard_hello);
        helloMsg.setText(mainActivity.getToken());
    }

}
