package com.example.inzynierka;

import android.content.ClipData;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemViewModel extends ViewModel {
    private final MutableLiveData<ClipData.Item> selectedItem = new MutableLiveData<ClipData.Item>();
    public void selectItem(ClipData.Item item) {
        selectedItem.setValue(item);
    }
    public LiveData<ClipData.Item> getSelectedItem() {
        return selectedItem;
    }

    public void select(View item) {
        Log.d("itemmodel","Item model opened or smth");
    }
}