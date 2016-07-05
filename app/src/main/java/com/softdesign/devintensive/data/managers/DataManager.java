package com.softdesign.devintensive.data.managers;

public class DataManager {

    private static DataManager INSTANCE = null;
    private PreferencesManager mPreferencesManager;

    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
    }

    /**
     * Инициализирует класс.
     * Вовзращает свой экземпляр.
     * Если он не был ранее создан, то создает его и после возвращает
     * Таким образом всегда будет существовать всего один экземпляр этого класса
     * , следовательно и его данных
     * @return
     */
    public static DataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    /**
     * Возвращает объект класса PreferencesManager
     * @return
     */
    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }
}
