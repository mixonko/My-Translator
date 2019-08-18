package com.myapp.test.mytranslator.contracts;

public interface CommunicationTextContract {
    interface View{
        String getFirstText();
        String getSecondText();
        void setFirstTextView(String resultText);
        void setSecondTextView(String resultText);
        void playFirstTextView(String lang);
        void playSecondTextView(String lang);
        String getFirstLang();
        String getSecondLang();
        void showNoConnection();
        void showError(Throwable t);
        void voiceInputFirstText(String lang);
        void voiceInputSecondText(String lang);
        void showFirstTextGroup();
        void showSecondTextGroup();
        void deleteFirstGroup();
        void deleteSecondGroup();
        void hideAllText();
        void hideButtons();
        void stopTextToSpeech();
        void startGreetingActivity(String firstLang, String secondLang);
    }

    interface Presenter{
        void onFirstMicButtonWasClicked();
        void onSecondMicButtonWasClicked();
        void onFirstEditTextWasChanged();
        void onSecondEditTextWasChanged();
        void firstTextWasInserted();
        void secondTextWasInserted();
        void firstEditTextIsEmpty();
        void secondEditTextIsEmpty();
        void onPlayFirstButTextWasClicked();
        void onPlaySecondTextButWasClicked();
        void onDeleteFirstTextButWasClicked();
        void onDeleteSecondTextButWasClicked();
        void onGreetingButtonWasClicked();
        void onStopActivity();
        void onFirstTextViewWasClicked();
        void onSecondTextViewWasClicked();
    }

}
