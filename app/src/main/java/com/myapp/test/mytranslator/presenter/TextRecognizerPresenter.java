package com.myapp.test.mytranslator.presenter;


import com.myapp.test.mytranslator.contracts.TextRecognizerContract;
import com.myapp.test.mytranslator.contracts.TranslateTextContract;
import com.myapp.test.mytranslator.repository.TranslateRepository;

public class TextRecognizerPresenter implements TextRecognizerContract.Presenter, TranslateTextContract.Repository.OnFinishedListener{
    private TextRecognizerContract.View view;
    private TranslateTextContract.Repository repository;
    private final String RECOGNIZER_REQUEST_CODE = "recognizer_request_code";

    public TextRecognizerPresenter(TextRecognizerContract.View view) {
        this.view = view;
        repository = new TranslateRepository();
    }


    @Override
    public void onTranslationButtonWasClicked() {
        repository.getTranslatedText(view.getText(), this, view.getFirstLang(), view.getSecondLang(), RECOGNIZER_REQUEST_CODE);
    }

    @Override
    public void onTakePhotoButtonWasClicked() {
        view.takePhoto();
    }

    @Override
    public void onOpenPhotoButtonWasClicked() {
        view.openPhoto();
    }

    @Override
    public void onLangWasSelected() {

    }

    @Override
    public void onFinished(String translatedText, String requestCode) {
        if (RECOGNIZER_REQUEST_CODE.equals(requestCode)) view.setText(translatedText);
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
