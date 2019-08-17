package com.myapp.test.mytranslator.presenter;

import android.app.Activity;

import com.myapp.test.mytranslator.contracts.CommunicationTextContract;
import com.myapp.test.mytranslator.contracts.TranslateTextContract;
import com.myapp.test.mytranslator.repository.TranslateRepository;

public class CommunicationPresenter extends Activity implements CommunicationTextContract.Presenter, TranslateTextContract.Repository.OnFinishedListener{
    private CommunicationTextContract.View view;
    private TranslateTextContract.Repository repository;
    private static final int FIRST_RESULT_REQUEST_CODE = 1;
    private static final int SECOND_RESULT_REQUEST_CODE = 2;

    public CommunicationPresenter(CommunicationTextContract.View view) {
        this.view = view;
        repository = new TranslateRepository();
    }

    @Override
    public void onFirstMicButtonWasClicked() {
        view.voiceInputFirstText();
    }

    @Override
    public void onSecondMicButtonWasClicked() {
        view.voiceInputSecondText();
    }

    @Override
    public void onFirstTextWasChanged() {
        repository.getTranslatedText(view.getFirstText(), this,
                view.getFirstLang(), view.getSecondLang(), FIRST_RESULT_REQUEST_CODE);
    }

    @Override
    public void onSecondTextWasChanged() {
        repository.getTranslatedText(view.getSecondText(), this,
                view.getSecondLang(), view.getFirstLang(), SECOND_RESULT_REQUEST_CODE);
    }

    @Override
    public void onFirstSpeakerButtonWasClicked() {

    }

    @Override
    public void onSecondSpeakerButtonWasClicked() {

    }

    @Override
    public void firstTextwasInserted() {
        repository.getTranslatedText(view.getFirstText(), this,
                view.getFirstLang(), view.getSecondLang(), FIRST_RESULT_REQUEST_CODE);
    }

    @Override
    public void secondTextwasInserted() {
        repository.getTranslatedText(view.getSecondText(), this,
                view.getSecondLang(), view.getFirstLang(), SECOND_RESULT_REQUEST_CODE);
    }

    @Override
    public void onFinished(String translatedText, int requestCode) {
        switch (requestCode){
            case FIRST_RESULT_REQUEST_CODE:
                view.setSecondText(translatedText);
                view.playSecondText();
                break;
            case SECOND_RESULT_REQUEST_CODE:
                view.setFirstText(translatedText);
                view.playFirstText();
                break;
        }
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
