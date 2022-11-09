package pl.app.projektgrupowy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Logging extends AppCompatActivity {

    TextInputEditText textInputEditUsername, textInputEditPassword;
    Button buttonLogging;
    Button buttonWithoutLog;
    TextView textViewSign;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);

        textInputEditUsername = findViewById(R.id.usernameLogging);
        textInputEditPassword = findViewById(R.id.passwordLogging);
        buttonLogging = findViewById(R.id.buttonLogin);
        buttonWithoutLog = findViewById(R.id.buttonWithoutLogin);
        textViewSign = findViewById(R.id.signUpText);
        progressBar = findViewById(R.id.progressLogging);

        /*
        textViewSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), Sign.class);
                startActivity(intent2);
                finish();
            }
        });
        */
        buttonWithoutLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



        buttonLogging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String usernameString, passwordString;

                usernameString = String.valueOf(textInputEditUsername.getText());
                passwordString = String.valueOf(textInputEditPassword.getText());

                if(!usernameString.equals("") && !passwordString.equals("")) {

                    progressBar.setVisibility(View.VISIBLE);

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            String[] field = new String[2];
                            field[0] = "username";
                            field[1] = "password";
                            String[] data = new String[2];
                            data[0] = usernameString;
                            data[1] = passwordString;
                           /* PutData putData = new PutData("http://192.168.1.101/LogInSignUp/login.php",
                                    "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {

                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    // porównuje echo z PHP
                                    if(result.equals("Zalogowano")){
                                        // Wyświetlenie dymku z echo PHP po udanym zalogowaniu
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            */
                        }
                    });
                }
                else  {
                    Toast.makeText(getApplicationContext(), "Wszystkie pola wymagane", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
