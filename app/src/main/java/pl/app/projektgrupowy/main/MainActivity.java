package pl.app.projektgrupowy.main;

// TODO: dwa problemy 1. cofanie backbuttonem z dodawania 2. ten jebany callback z viewmodel dla dodawania

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.prefs.Preferences;

import pl.app.projektgrupowy.R;
import pl.app.projektgrupowy.adapters.DashboardAdapter;
import pl.app.projektgrupowy.assets.NewTranslation;
import pl.app.projektgrupowy.assets.Segment;
import pl.app.projektgrupowy.assets.Translation;
import pl.app.projektgrupowy.fragments.AddFragment;
import pl.app.projektgrupowy.fragments.DashboardFragment;
import pl.app.projektgrupowy.fragments.LoginFragment;
import pl.app.projektgrupowy.fragments.SegmentFragment;
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
            if (newVal) {
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

        mainViewModel.chosenSegment().observe(this, newVal -> {
            if (newVal != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, SegmentFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("SegmentFragment")
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

    @SuppressLint("DefaultLocale")
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
                    mainViewModel.getDataSet().setValue(null);
                });
                toolbar.setTitle(mainViewModel.getEditedTranslation().getValue().getTitle());
                break;
            }
            case "SegmentFragment": {
                getMenuInflater().inflate(R.menu.toolbar_segment, menu);
                toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
                toolbar.setNavigationOnClickListener(view -> {
                    mainViewModel.chosenSegment().setValue(null);
                    getSupportFragmentManager().popBackStack();
                });
                toolbar.setTitle(String.format("Segment %d",
                        mainViewModel.chosenSegment().getValue() + 1));
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toolbar_dashboard_logout: {
                if (getTopFragmentName().equals("SegmentFragment")) {
                    SegmentFragment segmentFragment =
                            (SegmentFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
                    segmentFragment.updateTranslation();
                }
                mainViewModel.getToken().setValue(MainViewModel.LOGGED_OUT);
                mainViewModel.getDataSet().setValue(null);
                mainViewModel.setNewTranslationData(null);
                mainViewModel.getNewTranslation().setValue(false);
                mainViewModel.getEditedTranslation().setValue(null);
                mainViewModel.chosenSegment().setValue(null);
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
            case R.id.action_toolbar_translation_save: {
                Translation translation = mainViewModel.getEditedTranslation().getValue();
                File file = new File (getExternalFilesDir("Tłumaczenia"),
                        translation.getTitle().replace(" ", "_") + ".xml");
                try {
                    if (!file.exists()) file.createNewFile();
                    FileWriter writer = new FileWriter(file.getAbsolutePath());
                    writer.write(translation.toString());
                    writer.close();
                    Toast.makeText(this, R.string.translation_save_success, Toast.LENGTH_SHORT).show();
                } catch (IOException ex) {
                    Log.e("Błąd zapisywania pliku", ex.toString());
                    Toast.makeText(this, R.string.translation_save_fail, Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.action_toolbar_segment_save: {
                SegmentFragment segmentFragment =
                        (SegmentFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
                segmentFragment.updateTranslation();
                break;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getTopFragmentName().equals("TranslationFragment")) mainViewModel.getDataSet().setValue(null);
        if (mainViewModel.getEditedTranslation().getValue() != null && !getTopFragmentName().equals("SegmentFragment"))
            mainViewModel.getEditedTranslation().setValue(null);

        super.onBackPressed();

        if (mainViewModel.getNewTranslation().getValue() != null) mainViewModel.getNewTranslation().setValue(false);
        if (mainViewModel.chosenSegment().getValue() != null) mainViewModel.chosenSegment().setValue(null);
    }


}
