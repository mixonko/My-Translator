package com.myapp.test.mytranslator.presenter;

import com.myapp.test.mytranslator.contracts.MainContract;

public class MainPresenter implements MainContract.MainPresenter {
    private MainContract.View view;

    public MainPresenter(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void onButtonTranslateTextWasClicked() {
        view.goToTranslateTextActivity();
    }

    @Override
    public void onButtonTranslateCameraTextWasClicked() {
        view.goToTranslateCameraTextActivity();
    }

    @Override
    public void onButtonTranslateVoiceTextWasClicked() {
        view.goToTranslateVoiceTextActivity();
    }
}
