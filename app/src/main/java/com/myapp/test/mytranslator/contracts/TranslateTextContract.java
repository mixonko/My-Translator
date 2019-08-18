package com.myapp.test.mytranslator.contracts;

public interface TranslateTextContract {
    interface View{
        String getText();
        void deleteAllText();
        void setText(String resultText);
        void showNoConnection();
        void showError(Throwable t);
        void voiceInputText(String lang);
        void playUserText();
        void playResultText();
        void showButtons();
        void hideButtons();
        void swapLanguages();
        void copyResultText();
        String getFirstLang();
        String getSecondLang();
        void startCommunicationActivity();
        void setResultLanguage();
        void deleteResultText();
        void stopTextToSpeech();
    }

    interface Presenter{
        void onSwapLangButtonWasClicked();
        void onTextWasChanged();
        void onLangWasSelected();
        void onPlayUserTextwasClicked();
        void onPlayResultTextWasClicked();
        void onVoiceInputButtonWasClicked();
        void onDeleteTextButtonWasClicked();
        void onCopyButtonWasClicked();
        void onCommunicationButtonWasClicked();
        void userTextIsEmpty();
        void onStopActivity();
    }

    interface Repository {
        void getTranslatedText(String userText, OnFinishedListener onFinishedListener,
                               String firstLang, String secondLang, int requestCode);

        interface OnFinishedListener {
            void onFinished(String translatedText, int requestCode);
            void onFailure(Throwable t);
            void showNoConnection();
        }
    }
}


