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
    }

    interface Presenter{
        void onFirstMicButtonWasClicked();
        void onSecondMicButtonWasClicked();
        void onFirstSpeakerButtonWasClicked();
        void onSecondSpeakerButtonWasClicked();
    }

}
