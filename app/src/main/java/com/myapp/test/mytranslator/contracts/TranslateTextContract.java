package com.myapp.test.mytranslator.contracts;

public interface TranslateTextContract {
    interface View{
        String getText();
        void deleteUserText();
        void setText(String resultText);
        void showNoConnection();
        void showError(Throwable t);
        void voiceInputText();
        void playUserText();
        void playResultText();
        void showButtons();
        void hideButtons();
        void swapLanguages();
        void copyResyltText();
        String getFirstLang();
        String getSecondLang();
        void startCommunicationActivity();
    }

    interface Presenter{
        void onSwapLangButtonWasClicked();
        void onTextWasChanged();
        void onLangWasSelected();
        void onPlayUserTextwasClicked();
        void onPlayResultTextWasClicked();
        void onButtonRecorderWasClicked();
        void onDeleteTextButtonWasClicked();
        void onCopyButtonWasClicked();
        void onCommunicationButtonWasClicked();
    }

    interface Repository {
        void getTranslatedText(String userText, OnFinishedListener onFinishedListener, String firstLang, String secondLang);

        interface OnFinishedListener {
            void onFinished(String translatedText);
            void onFailure(Throwable t);
            void showNoConnection();
        }
    }
}


