package com.myapp.test.mytranslator.presenter;

import com.myapp.test.mytranslator.contracts.TranslateTextContract;
import com.myapp.test.mytranslator.repository.network.NetworkRepository;

public class TranslateTextPresenter implements TranslateTextContract.Presenter, TranslateTextContract.Repository.OnFinishedListener {
    private TranslateTextContract.View view;
    private TranslateTextContract.Repository repository;
    private String TRANSLATE_REQUEST_CODE = "translate_request_code";

    public TranslateTextPresenter(TranslateTextContract.View view) {
        this.view = view;
        repository = new NetworkRepository();
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
        view.playUserText(view.getFirstLang());
    }

    @Override
    public void onPlayResultTextWasClicked() {
        view.stopTextToSpeech();
        view.playResultText(view.getSecondLang());

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
    public void onImageButtonWasClicked() {
        view.pickImage();
    }

    @Override
    public void onCameraButtonWasClicked() {
        view.startScanCamera();
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
    public void onFinished(String translatedText, String requestCode) {
        if (TRANSLATE_REQUEST_CODE.equals(requestCode)) view.setText(translatedText);
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
        view.setTextViewLanguage();
        repository.getTranslatedText(view.getText(), this,
                view.getFirstLang(), view.getSecondLang(), TRANSLATE_REQUEST_CODE);
    }
}
