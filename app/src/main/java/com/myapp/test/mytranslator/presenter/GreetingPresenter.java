package com.myapp.test.mytranslator.presenter;

import com.myapp.test.mytranslator.contracts.GreetingContract;
import com.myapp.test.mytranslator.contracts.TranslateTextContract;
import com.myapp.test.mytranslator.repository.network.NetworkRepository;

public class GreetingPresenter implements GreetingContract.Presenter, TranslateTextContract.Repository.OnFinishedListener {
    private GreetingContract.View view;
    private TranslateTextContract.Repository repository;
    private final String GREETING_REQUEST_CODE = "greeting_request_code";

    public GreetingPresenter(GreetingContract.View view) {
        this.view = view;
        repository = new NetworkRepository();
    }

    @Override
    public void onActivityWasCreated() {
        repository.getTranslatedText(view.getGreetingText(), this,
                view.getFirstLang(), view.getSecondLang(), GREETING_REQUEST_CODE);
    }

    @Override
    public void onCloseButtonWasClicked() {
        view.closeActivity();
    }

    @Override
    public void onPlayTextButtonWasClicked() {
        view.stopTextToSpeech();
        view.playText(view.getSecondLang());
    }

    @Override
    public void onStopActivity() {
        view.stopTextToSpeech();
    }

    @Override
    public void onFinished(String translatedText, String requestCode) {
        if (GREETING_REQUEST_CODE.equals(requestCode)) view.setText(translatedText);
    }

    @Override
    public void onFailure(Throwable t) {
        view.showError(t);
    }

    @Override
    public void showNoConnection() {
        view.showNoConnection();
    }
}
