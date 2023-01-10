package pl.app.projektgrupowy.main;

// TODO: dwa problemy 1. cofanie backbuttonem z dodawania 2. ten jebany callback z viewmodel dla dodawania

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.prefs.Preferences;

import pl.app.projektgrupowy.R;
import pl.app.projektgrupowy.adapters.DashboardAdapter;
import pl.app.projektgrupowy.assets.NewTranslation;
import pl.app.projektgrupowy.assets.Translation;
import pl.app.projektgrupowy.fragments.AddFragment;
import pl.app.projektgrupowy.fragments.DashboardFragment;
import pl.app.projektgrupowy.fragments.LoginFragment;
import pl.app.projektgrupowy.fragments.TranslationFragment;

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

    public String getTopFragmentName() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container_view).getClass().getSimpleName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getToken().observe(this, newVal -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();

            if (newVal.equals(MainViewModel.LOGGED_OUT) || newVal.equals("") || newVal == null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, LoginFragment.class, null)
                        .setReorderingAllowed(true)
                        .commit();

                toolbar.setTitle(R.string.app_name);
                toolbar.setNavigationIcon(null);
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, DashboardFragment.class, null)
                        .setReorderingAllowed(true)
                        .commit();
            }
            savePreferences(newVal);
        });

        mainViewModel.getDataSet().observe(this, newVal -> {
            if (getTopFragmentName().equals("DashboardFragment") && newVal != null) {
                DashboardFragment dashboardFragment = (DashboardFragment)
                        getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
                dashboardFragment.setRecyclerViewAdapter(newVal);
            }
        });

        mainViewModel.getNewTranslation().observe(this, newVal -> {
            if (newVal.booleanValue()) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, AddFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("AddFragment")
                        .commit();

            }
        });

        mainViewModel.getEditedTranslation().observe(this, newVal -> {
            if (newVal != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, TranslationFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("TranslationFragment")
                        .commit();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        mainViewModel.getToken().setValue(sharedPreferences.getString("token", ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_login, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        String fragmentName = getTopFragmentName();
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
                toolbar.setNavigationOnClickListener(view -> {
                    getSupportFragmentManager().popBackStack();
                    mainViewModel.setNewTranslationData(null);
                    mainViewModel.getNewTranslation().setValue(false);
                });
                toolbar.setTitle(R.string.title_toolbar_add);
                break;
            }
            case "TranslationFragment": {
                getMenuInflater().inflate(R.menu.toolbar_translation, menu);
                toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
                toolbar.setNavigationOnClickListener(view -> {
                    getSupportFragmentManager().popBackStack();
                    mainViewModel.getEditedTranslation().setValue(null);
                });
                toolbar.setTitle(mainViewModel.getEditedTranslation().getValue().getTitle());
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
                mainViewModel.getDataSet().setValue(null);
                mainViewModel.setNewTranslationData(null);
                mainViewModel.getNewTranslation().setValue(false);
                break;
            }
            case R.id.action_toolbar_dashboard_add: {
                mainViewModel.getNewTranslation().setValue(true);
                break;
            }
            case R.id.action_toolbar_translation_delete: {
                mainViewModel.getDataSet().setValue(null);
                TranslationFragment translationFragment =
                        (TranslationFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
                translationFragment.delete();
                break;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mainViewModel.getNewTranslation().getValue() != null) mainViewModel.getNewTranslation().setValue(false);
        if (mainViewModel.getEditedTranslation().getValue() != null) mainViewModel.getEditedTranslation().setValue(null);
    }


}
