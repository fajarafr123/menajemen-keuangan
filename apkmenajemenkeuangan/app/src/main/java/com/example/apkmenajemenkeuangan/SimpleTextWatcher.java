package com.example.apkmenajemenkeuangan;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class SimpleTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Tidak digunakan
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Tidak digunakan
    }

    @Override
    public abstract void afterTextChanged(Editable s);
}
