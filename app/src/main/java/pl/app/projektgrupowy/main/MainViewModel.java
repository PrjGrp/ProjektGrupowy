package pl.app.projektgrupowy.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pl.app.projektgrupowy.assets.NewTranslation;
import pl.app.projektgrupowy.assets.Translation;

public class MainViewModel extends ViewModel {
    public final static String LOGGED_OUT = "logged_out";

    private MutableLiveData<String> token;
    private MutableLiveData<Translation[]> dataSet;
    private MutableLiveData<Boolean> newTranslation;
    private MutableLiveData<Translation> editedTranslation;
    private MutableLiveData<Integer> chosenSegment;

    private NewTranslation newTranslationData;


    public MutableLiveData<String> getToken() {
        if (token == null) token = new MutableLiveData<>();
        return token;
    }

    public MutableLiveData<Translation[]> getDataSet() {
        if (dataSet == null) dataSet = new MutableLiveData<>();
        return dataSet;
    }

    public MutableLiveData<Boolean> getNewTranslation() {
        if (newTranslation == null) newTranslation = new MutableLiveData<>();
        return newTranslation;
    }

    public MutableLiveData<Translation> getEditedTranslation() {
        if (editedTranslation == null) editedTranslation = new MutableLiveData<>();
        return editedTranslation;
    }

    public MutableLiveData<Integer> chosenSegment() {
        if (chosenSegment == null) chosenSegment = new MutableLiveData<>();
        return chosenSegment;
    }

    public NewTranslation getNewTranslationData() {
        return newTranslationData;
    }

    public void setNewTranslationData(NewTranslation newTranslationData) {
        this.newTranslationData = newTranslationData;
    }
}
