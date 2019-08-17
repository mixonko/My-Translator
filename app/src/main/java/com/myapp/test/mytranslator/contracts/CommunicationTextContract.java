package com.myapp.test.mytranslator.contracts;

public interface CommunicationTextContract {
    interface View{
        String getFirstText();
        String getSecondText();
        void setFirstText(String resultText);
        void setSecondText(String resultText);
        void playFirstText();
        void playSecondText();
        String getFirstLang();
        String getSecondLang();
        void showNoConnection();
        void showError(Throwable t);
        void voiceInputFirstText();
        void voiceInputSecondText();
    }

    interface Presenter{
        void onFirstMicButtonWasClicked();
        void onSecondMicButtonWasClicked();
        void onFirstTextWasChanged();
        void onSecondTextWasChanged();
        void onFirstSpeakerButtonWasClicked();
        void onSecondSpeakerButtonWasClicked();
        void firstTextwasInserted();
        void secondTextwasInserted();
    }

}
