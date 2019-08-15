package com.myapp.test.mytranslator.presenter;

import android.app.Activity;
import com.myapp.test.mytranslator.contracts.CommunicationTextContract;
import com.myapp.test.mytranslator.contracts.TranslateTextContract;
import com.myapp.test.mytranslator.repository.TranslateRepository;

public class CommunicationPresenter extends Activity implements CommunicationTextContract.Presenter{
    private CommunicationTextContract.View view;
    private TranslateTextContract.Repository repository;

    public CommunicationPresenter(CommunicationTextContract.View view) {
        this.view = view;
        repository = new TranslateRepository();
    }

    @Override
    public void onFirstMicButtonWasClicked() {

    }

    @Override
    public void onSecondMicButtonWasClicked() {

    }

    @Override
    public void onFirstSpeakerButtonWasClicked() {

    }

    @Override
    public void onSecondSpeakerButtonWasClicked() {

    }

}
