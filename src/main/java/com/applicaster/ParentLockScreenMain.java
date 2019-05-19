package com.applicaster;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.applicaster.plugin_manager.screen.PluginScreen;

import java.io.Serializable;
import java.util.HashMap;

public class ParentLockScreenMain implements PluginScreen {

    @Override
    public void present(Context context, HashMap<String, Object> screenMap, Serializable dataSource, boolean isActivity) {

    }

    @Override
    public Fragment generateFragment(HashMap<String, Object> screenMap, Serializable dataSource) {
        return null;
    }
}
