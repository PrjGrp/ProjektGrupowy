package pl.app.projektgrupowy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import restcli.MockClient;

public class MainActivity extends AppCompatActivity {
    private TextView resultsTextView;
    private Button myButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultsTextView = (TextView) findViewById(R.id.results);
        myButton = (Button) findViewById(R.id.button);
    }
}