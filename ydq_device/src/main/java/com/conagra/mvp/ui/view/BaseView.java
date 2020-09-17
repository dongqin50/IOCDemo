package com.conagra.mvp.ui.view;

public interface BaseView {

    void showLoading();

    void hideLoading();

    void showMessage(int msgId);

    void showHint(int msgId);

    void showHint(String msg);

    void showNetworkError();

    void showServerError();

    void showServerError(String msg);

    void showAuthorizedError();

    void showEmpty();


}
