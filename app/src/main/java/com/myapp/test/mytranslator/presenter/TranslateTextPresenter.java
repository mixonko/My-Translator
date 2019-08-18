package com.myapp.test.mytranslator.presenter;

import com.myapp.test.mytranslator.contracts.TranslateTextContract;
import com.myapp.test.mytranslator.repository.TranslateRepository;

public class TranslateTextPresenter implements TranslateTextContract.Presenter, TranslateTextContract.Repository.OnFinishedListener {
    private TranslateTextContract.View view;
    private TranslateTextContract.Repository repository;
    private int RESULT_REQUEST_CODE = 5;

    public TranslateTextPresenter(TranslateTextContract.View view) {
        this.view = view;
        repository = new TranslateRepository();
    }

    @Override
    public void onTextWasChanged() {
        view.showButtons();
        getTranslatedText();
    }

    @Override
    public void onLangWasSelected() {
        getTranslatedText();
    }

    @Override
    public void onPlayUserTextwasClicked() {
        view.stopTextToSpeech();
        view.playUserText();
    }

    @Override
    public void onPlayResultTextWasClicked() {
        view.stopTextToSpeech();
        view.playResultText();

    }

    @Override
    public void onVoiceInputButtonWasClicked() {
        view.voiceInputText(view.getFirstLang());
    }

    @Override
    public void onDeleteTextButtonWasClicked() {
        view.deleteAllText();
        view.hideButtons();
        view.stopTextToSpeech();
    }

    @Override
    public void onCopyButtonWasClicked() {
        view.copyResultText();
    }

    @Override
    public void onCommunicationButtonWasClicked() {
        view.startCommunicationActivity();
    }

    @Override
    public void userTextIsEmpty() {
        view.deleteResultText();
        view.hideButtons();
    }

    @Override
    public void onStopActivity() {
        view.stopTextToSpeech();
    }

    @Override
    public void onSwapLangButtonWasClicked() {
        view.swapLanguages();
    }

    @Override
    public void onFinished(String translatedText, int requestCode) {
        if (RESULT_REQUEST_CODE == requestCode) view.setText(translatedText);
    }

    @Override
    public void onFailure(Throwable t) {
        view.showError(t);
    }

    @Override
    public void showNoConnection() {
        view.showNoConnection();
    }

    private void getTranslatedText() {
        view.stopTextToSpeech();
        view.setResultLanguage();
        repository.getTranslatedText(view.getText(), this,
                view.getFirstLang(), view.getSecondLang(), RESULT_REQUEST_CODE);
    }
}
