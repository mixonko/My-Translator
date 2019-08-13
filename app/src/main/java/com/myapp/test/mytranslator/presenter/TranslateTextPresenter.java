package com.myapp.test.mytranslator.presenter;

import com.myapp.test.mytranslator.contracts.TranslateTextContract;
import com.myapp.test.mytranslator.repository.TranslateRepository;

public class TranslateTextPresenter implements TranslateTextContract.Presenter, TranslateTextContract.Repository.OnFinishedListener {
    private TranslateTextContract.View view;
    private TranslateTextContract.Repository repository;
    public TranslateTextPresenter(TranslateTextContract.View view) {
        this.view = view;
        repository = new TranslateRepository();
    }

    @Override
    public void onTextWasChanged() {
        repository.getTranslatedText(view.getText(), this);

    }

    @Override
    public void onButtonRecorderWasClicked() {
        view.recordVoice();
    }

    @Override
    public void onDeleteTextButtonWasClicked() {
        view.deleteUserText();
    }

    @Override
    public void onSwapLangButtonWasClicked() {

    }

    @Override
    public void onFinished(String translatedText) {
        view.setText(translatedText);
    }

    @Override
    public void onFailure(Throwable t) {
        view.showError(t);
    }

    @Override
    public void onNoInternetConnection() {
        view.showNoConnection();
    }
}
