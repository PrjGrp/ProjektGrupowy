package pl.app.projektgrupowy.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import pl.app.projektgrupowy.R;
import pl.app.projektgrupowy.fragments.AddFragment;
import pl.app.projektgrupowy.fragments.DashboardFragment;
import pl.app.projektgrupowy.fragments.LoginFragment;

/**
 * Klasa realizująca naszą główną Activity, ma tylko miejsce na fragmenty i toolbar.
 */
public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public MainViewModel mainViewModel;

    private void savePreferences(String token) {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("token", token);
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getPreferences(MODE_PRIVATE).getString("token", "").equals(""))
            savePreferences(MainViewModel.LOGGED_OUT);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getToken().observe(this, newVal -> {
            if (MainViewModel.LOGGED_OUT.equals(newVal)) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, LoginFragment.class, null)
                        .setReorderingAllowed(true)
                        .commit();
                toolbar.setTitle(R.string.app_name);
                toolbar.setNavigationIcon(null);
                savePreferences(newVal);
            } else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, DashboardFragment.class, null)
                        .setReorderingAllowed(true)
                        .commit();
                invalidateOptionsMenu();
                savePreferences(newVal);
            }
        });

        mainViewModel.getToken().setValue(getPreferences(MODE_PRIVATE).getString("token", ""));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        String fragmentName = currentFragment.getClass().getSimpleName();
        switch (fragmentName) {
            case "DashboardFragment": {
                getMenuInflater().inflate(R.menu.toolbar_dashboard, menu);
                toolbar.setTitle(R.string.title_toolbar_dashboard);
                toolbar.setNavigationIcon(null);
                break;
            }
            case "AddFragment": {
                getMenuInflater().inflate(R.menu.toolbar_add, menu);
                toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
                toolbar.setNavigationOnClickListener(view -> { super.onBackPressed(); });
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
                mainViewModel.getToken().setValue(MainViewModel.LOGGED_OUT);
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
