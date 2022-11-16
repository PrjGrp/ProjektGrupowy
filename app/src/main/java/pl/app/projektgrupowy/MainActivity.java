package pl.app.projektgrupowy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import pl.app.projektgrupowy.fragments.AddFragment;
import pl.app.projektgrupowy.fragments.DashboardFragment;
import pl.app.projektgrupowy.fragments.LoginFragment;

/**
 * Klasa realizująca naszą główną Activity, ma tylko miejsce na fragmenty i toolbar.
 */
public class MainActivity extends AppCompatActivity {
    private String token = "";
    private Toolbar toolbar;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void replaceLoginFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, DashboardFragment.class, null)
                .setReorderingAllowed(true)
                .commit();
    }

    private void savePreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.commit();
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadPreferences();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!token.equals("")) replaceLoginFragment();
    }

    @Override
    public void onBackPressed() {
        savePreferences();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_login, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
        switch (currentFragment.getClass().getSimpleName()) {
            case "DashboardFragment": {
                getMenuInflater().inflate(R.menu.toolbar_dashboard, menu);
                toolbar.setTitle(R.string.title_toolbar_dashboard);
                break;
            }
            case "AddFragment": {
                getMenuInflater().inflate(R.menu.toolbar_add, menu);
                toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
                toolbar.setNavigationOnClickListener(view -> {
                    toolbar.setNavigationIcon(null);
                    super.onBackPressed();
                } );
                toolbar.setTitle(R.string.title_toolbar_add);
                break;
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toolbar_dashboard_logout: {
                token = "";
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, LoginFragment.class, null)
                        .setReorderingAllowed(true)
                        .commit();
                toolbar.setTitle(R.string.app_name);
                toolbar.setNavigationIcon(null);
                break;
            }
            case R.id.action_toolbar_dashboard_add: {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, AddFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("AddFragment")
                        .commit();
                invalidateOptionsMenu();
                break;
            }
        }
        return true;
    }
}
