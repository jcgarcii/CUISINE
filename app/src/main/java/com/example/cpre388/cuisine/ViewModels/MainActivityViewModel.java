package com.example.cpre388.cuisine.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cpre388.cuisine.Activities.Filters;

/**
 * ViewModel for {@link com.example.cpre388.cuisine.Activities.MainActivity}.
 */

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<Boolean> settings_changed = new MutableLiveData<>();

    private boolean mIsSigningIn;
    private Filters mFilters;

    public MainActivityViewModel() {
        mIsSigningIn = false;
        mFilters = Filters.getDefault();
    }

    public void set_settings(boolean i){
        settings_changed.setValue(i);
    }

    public MutableLiveData<Boolean> settings_changed(){
        return settings_changed;
    }

    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }

    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }

    public Filters getFilters() {
        return mFilters;
    }

    public void setFilters(Filters mFilters) {
        this.mFilters = mFilters;
    }
}
