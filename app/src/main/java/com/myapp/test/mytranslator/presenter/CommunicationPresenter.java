package com.myapp.test.mytranslator.presenter;

import android.app.Activity;

import com.myapp.test.mytranslator.contracts.CommunicationTextContract;
import com.myapp.test.mytranslator.contracts.TranslateTextContract;
import com.myapp.test.mytranslator.repository.TranslateRepository;

public class CommunicationPresenter extends Activity implements CommunicationTextContract.Presenter, TranslateTextContract.Repository.OnFinishedListener{
    private CommunicationTextContract.View view;
    private TranslateTextContract.Repository repository;
    private static final int FIRST_VOICE_INPUT_REQUEST_CODE = 1;
    private static final int SECOND_VOICE_INPUT_REQUEST_CODE = 2;
    private static final int FIRST_TEXT_CHANGE_REQUEST_CODE = 3;
    private static final int SECOND_TEXT_CHANGE_REQUEST_CODE = 4;

    public CommunicationPresenter(CommunicationTextContract.View view) {
        this.view = view;
        repository = new TranslateRepository();
    }

    @Override
    public void onFirstMicButtonWasClicked() {
        hideAll();
        view.voiceInputFirstText(view.getFirstLang());
    }

    @Override
    public void onSecondMicButtonWasClicked() {
        hideAll();
        view.voiceInputSecondText(view.getSecondLang());
    }

    @Override
    public void onFirstEditTextWasChanged() {
        view.stopTextToSpeech();
        view.showFirstTextGroup();
        repository.getTranslatedText(view.getFirstText(), this,
                view.getFirstLang(), view.getSecondLang(), FIRST_TEXT_CHANGE_REQUEST_CODE);
    }

    @Override
    public void onSecondEditTextWasChanged() {
        view.stopTextToSpeech();
        view.showSecondTextGroup();
        repository.getTranslatedText(view.getSecondText(), this,
                view.getSecondLang(), view.getFirstLang(), SECOND_TEXT_CHANGE_REQUEST_CODE);
    }

    @Override
    public void firstTextWasInserted() {
        repository.getTranslatedText(view.getFirstText(), this,
                view.getFirstLang(), view.getSecondLang(), FIRST_VOICE_INPUT_REQUEST_CODE);
    }

    @Override
    public void secondTextWasInserted() {
        repository.getTranslatedText(view.getSecondText(), this,
                view.getSecondLang(), view.getFirstLang(), SECOND_VOICE_INPUT_REQUEST_CODE);
    }

    @Override
    public void firstEditTextIsEmpty() {
        clearAll();
    }

    @Override
    public void secondEditTextIsEmpty() {
        clearAll();
    }

    @Override
    public void onPlayFirstButTextWasClicked() {
        onPlayButtonsWasClicked();
        view.playFirstTextView(view.getFirstLang());
    }

    @Override
    public void onPlaySecondTextButWasClicked() {
        onPlayButtonsWasClicked();
        view.playSecondTextView(view.getSecondLang());
    }

    @Override
    public void onDeleteFirstTextButWasClicked() {
        clearAll();
    }

    @Override
    public void onDeleteSecondTextButWasClicked() {
        clearAll();
    }

    @Override
    public void onGreetingButtonWasClicked() {
        view.startGreetingActivity(view.getFirstLang(), view.getSecondLang());
    }

    @Override
    public void onStopActivity() {
        view.stopTextToSpeech();
    }

    @Override
    public void onFirstTextViewWasClicked() {
        view.playFirstTextView(view.getFirstLang());
    }

    @Override
    public void onSecondTextViewWasClicked() {
        view.playSecondTextView(view.getSecondLang());
    }

    @Override
    public void onFinished(String translatedText, int requestCode) {
        switch (requestCode){
            case FIRST_VOICE_INPUT_REQUEST_CODE:
                view.showFirstTextGroup();
                view.setSecondTextView(translatedText);
                view.playSecondTextView(view.getSecondLang());
                break;
            case SECOND_VOICE_INPUT_REQUEST_CODE:
                view.showSecondTextGroup();
                view.setFirstTextView(translatedText);
                view.playFirstTextView(view.getFirstLang());
                break;
            case FIRST_TEXT_CHANGE_REQUEST_CODE:
                view.setSecondTextView(translatedText);
                break;
            case SECOND_TEXT_CHANGE_REQUEST_CODE:
                view.setFirstTextView(translatedText);
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

    private void hideAll(){
        clearAll();
        view.hideAllText();
    }

    private void clearAll(){
        view.deleteTextView();
        view.hideButtons();
        view.stopTextToSpeech();
    }

    private void onPlayButtonsWasClicked(){
        view.stopTextToSpeech();
    }
}
