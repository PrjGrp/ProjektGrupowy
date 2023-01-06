package pl.app.projektgrupowy.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pl.app.projektgrupowy.assets.NewTranslation;
import pl.app.projektgrupowy.assets.Translation;

public class MainViewModel extends ViewModel {
    public final static String LOGGED_OUT = "logged_out";

    private MutableLiveData<String> token;
    private MutableLiveData<Translation[]> dataSet;
    private MutableLiveData<NewTranslation> newTranslation;


    public MutableLiveData<String> getToken() {
        if (token == null) token = new MutableLiveData<>();
        return token;
    }

    public MutableLiveData<Translation[]> getDataSet() {
        if (dataSet == null) dataSet = new MutableLiveData<>();
        return dataSet;
    }

    public MutableLiveData<NewTranslation> getNewTranslation() {
        if (newTranslation == null) newTranslation = new MutableLiveData<>();
        return newTranslation;
    }
}
