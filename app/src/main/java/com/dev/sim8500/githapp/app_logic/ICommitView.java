package com.dev.sim8500.githapp.app_logic;

/**
 * Created by sbernad on 15.03.16.
 */
public interface ICommitView {

    void setMessage(CharSequence text);

    void setAuthor(CharSequence author);

    void setCommitDate(CharSequence date);

}
