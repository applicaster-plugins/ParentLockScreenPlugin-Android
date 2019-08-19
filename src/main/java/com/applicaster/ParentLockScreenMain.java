package com.applicaster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.applicaster.hook_screen.HookScreen;
import com.applicaster.hook_screen.HookScreenListener;
import com.applicaster.plugin_manager.PluginI;
import com.applicaster.plugin_manager.screen.PluginScreen;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.applicaster.ParentLockActivity.HOOK_DATA;
import static com.applicaster.hook_screen.HookScreenManager.ACTIVITY_HOOK_RESULT_CODE;
import static com.applicaster.hook_screen.HookScreenManager.HOOK_PROPS_EXTRA;

public class ParentLockScreenMain implements PluginScreen, HookScreen, PluginI {

    public static final String USE_LOGIN_PLUGIN = "use_login_plugin";

    HookScreenListener hookListener;
    HashMap<String, String> hookData = new HashMap<>();
    private HashMap<String, String> pluginConfiguration = new HashMap<>();

    //Plugin Screen methods
    @Override
    public void present(Context context, HashMap<String, Object> screenMap, Serializable dataSource, boolean isActivity) {

    }

    @Override
    public Fragment generateFragment(HashMap<String, Object> screenMap, Serializable dataSource) {
        return null;
    }

    //Hook Screen methods

    @Override
    public boolean isFlowBlocker() {
        return true;
    }

    @Override
    public boolean shouldPresent() {
        return true;
    }

    @Override
    public boolean isRecurringHook() {
        return true;
    }

    @Override
    public void hookDismissed() {

    }

    @Override
    public void executeHook(@NotNull Context context, @NotNull HookScreenListener hookListener, @Nullable Map<String, ?> hookProps) {
        this.hookListener = hookListener;
        Intent intent = new Intent(context, ParentLockActivity.class);
        intent.putExtra(USE_LOGIN_PLUGIN, getValue(pluginConfiguration, USE_LOGIN_PLUGIN).equals("1"));
        intent.putExtra(HOOK_DATA, hookData);
        startActivityForResult(intent, (Activity)context, hookProps);
    }

    @NotNull
    @Override
    public HookScreenListener getListener() {
        return hookListener;
    }

    @NotNull
    @Override
    public HashMap<String, String> getHook() {
        return this.hookData;
    }

    @Override
    public void setHook(@NotNull HashMap<String, String> hashMap) {
        this.hookData = hashMap;
    }
    //


    private void startActivityForResult(Intent intent, Activity activity, Map<String,?> hookProps) {
        if (hookProps != null) {
            intent.putExtra(HOOK_PROPS_EXTRA, new Gson().toJson(hookProps));
        }
        activity.startActivityForResult(intent, ACTIVITY_HOOK_RESULT_CODE);
    }

    @Override
    public void setPluginConfigurationParams(Map params) {
        pluginConfiguration.putAll(params);
    }

    private String getValue(Map params, String key) {
        String returnVal = "";
        if (params != null && params.get(key) != null) {
            returnVal = params.get(key).toString();
        }
        return returnVal;
    }
}
