package com.myapp.test.mytranslator.contracts;

public interface TextRecognizerContract {
    interface View{
        void showTranslate();
        void hideTranslate();
        void takePhoto();
        void openPhoto();
        String getFirstLang();
        void setFirstLang(int position);
        String getSecondLang();
        void showNoConnection();
        void showError(Throwable t);
    }

    interface Presenter{
        void onTranslationButtonWasClicked();
        void onTakePhotoButtonWasClicked();
        void onOpenPhotoButtonWasClicked();
        void onLangWasSelected();
    }
}
