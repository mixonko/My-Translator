package com.myapp.test.mytranslator.contracts;

public interface GreetingContract {
    interface View{
        void setText(String result);
        String getGreetingText();
        void showError(Throwable t);
        void showNoConnection();
        String getFirstLang();
        String getSecondLang();
        void closeActivity();
        void playText(String lang);
        void stopTextToSpeech();
    }

    interface Presenter{
        void onActivityWasCreated();
        void onCloseButtonWasClicked();
        void onPlayTextButtonWasClicked();
        void onStopActivity();
    }
}
