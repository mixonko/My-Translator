package com.myapp.test.mytranslator.contracts;

public interface TextRecognizerContract {
    interface View{
        void takePhoto();
        void openPhoto();
        String getFirstLang();
        String getSecondLang();
        void showNoConnection();
        void showError(Throwable t);
        String getText();
        void setText(String result);
        void pickImage();
    }

    interface Presenter{
        void onTranslationButtonWasClicked();
        void onTakePhotoButtonWasClicked();
        void onOpenPhotoButtonWasClicked();
        void onLangWasSelected(); ;
    }
}
