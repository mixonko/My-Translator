package com.myapp.test.mytranslator.presenter;


import com.myapp.test.mytranslator.contracts.TextRecognizerContract;
import com.myapp.test.mytranslator.contracts.TranslateTextContract;
import com.myapp.test.mytranslator.repository.TranslateRepository;

public class TextRecognizerPresenter implements TextRecognizerContract.Presenter, TranslateTextContract.Repository.OnFinishedListener{
    private TextRecognizerContract.View view;
    private TranslateTextContract.Repository repository;

    public TextRecognizerPresenter(TextRecognizerContract.View view) {
        this.view = view;
        repository = new TranslateRepository();
    }


    @Override
    public void onTranslationButtonWasClicked() {
        view.showTranslate();
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
    public void onFinished(String translatedText, int requestCode) {

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
