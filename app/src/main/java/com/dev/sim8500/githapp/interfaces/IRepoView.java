package com.dev.sim8500.githapp.interfaces;

import java.util.Date;

/**
 * Created by sbernad on 16.04.16.
 */
public interface IRepoView {

    void setName(CharSequence name);
    void setUrl(CharSequence url);
    void setDescription(CharSequence description);
    void setUserId(CharSequence username, String avatarUrl);
    void setCreatedDate(CharSequence createdAt);
    void setUpdatedDate(CharSequence updatedAt);
    void setListener(IRepoEntryListener listener);
    void showFavButton(boolean doShow);

}
