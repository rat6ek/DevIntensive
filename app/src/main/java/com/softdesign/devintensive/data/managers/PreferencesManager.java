package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import java.util.ArrayList;
import java.util.List;

public class PreferencesManager {

    private SharedPreferences mSharedPreferences;

    private static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY, ConstantManager.USER_MAIL_KEY, ConstantManager.USER_VK_KEY, ConstantManager.USER_GIT_KEY, ConstantManager.USER_BIO_KEY};

    public PreferencesManager() {
        this.mSharedPreferences = DevintensiveApplication.getSharedPreferences();
    }

    /**
     * Сохраняет данные пользователя в SharedPreferences
     * @param userFileds
     */
    public void saveUserProfileData(List<String> userFileds) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for(int i = 0; i < USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userFileds.get(i));
        }
        editor.apply();
    }

    /**
     * Загружает данные пользователя из SharedPreferences, если они отсутствуют
     * , то возвращает строку со значением null
     * @return
     */
    public List<String> loadUserProfileData() {
        List<String> userFields = new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, "+79920000011"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY, "google@gmail.com"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY, "vk.com"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_GIT_KEY, "github.com"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_BIO_KEY, "Lorem impsum"));
        return userFields;
    }

    /**
     * Сохраняет в SharedPreferences установленное пользователем фото
     * @param uri
     */
    public void saveUserPhoto(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }

    /**
     * Загружает из SharedPreferences установленное пользователем фото
     * @return
     */
    public Uri loadUserPhoto() {
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, "android.resource://com.softdesign.devintensive/drawable/user_photo"));
    }

}
