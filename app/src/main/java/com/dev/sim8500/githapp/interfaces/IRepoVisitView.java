package com.dev.sim8500.githapp.interfaces;

/**
 * Created by sbernad on 25.02.2017.
 */

public interface IRepoVisitView {

    void setName(CharSequence text);

    void setOwner(CharSequence owner);

    void setUrl(CharSequence url);

    void setVisitDate(CharSequence date);

    void setListener(IRepoEntryListener listener);
}
