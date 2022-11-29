package pl.app.projektgrupowy.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    public final static String LOGGED_OUT = "logged_out";

    private MutableLiveData<String> token;


    public MutableLiveData<String> getToken() {
        if (token == null) token = new MutableLiveData<>();
        return token;
    }
}
