package com.app.donteatalone.utils;

import com.orhanobut.hawk.Hawk;

/**
 * -> Created by LeHoangHan on 4/8/2017.
 */

public class PrefManager {
    private static final String IS_TUTORIAL_LAUNCHED = "IsFirstTimeLaunch";
    private static PrefManager instance = new PrefManager();

    public static PrefManager getInstance() {
        return instance;
    }

    public boolean isTutorialLaunched() {
        return Hawk.get(IS_TUTORIAL_LAUNCHED, true);
    }

    public void setTutorialLaunched(boolean isFirstTime) {
        Hawk.put(IS_TUTORIAL_LAUNCHED, isFirstTime);
    }
}
