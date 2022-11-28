package pl.app.projektgrupowy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private MutableLiveData<String> topFragment;

    public LiveData<String> getTopFragment() {
        if (topFragment == null) {
            topFragment = new MutableLiveData<String>();
            readTopFragment();
        }
        return topFragment;
    }

    private void readTopFragment() {
        
    }
}
