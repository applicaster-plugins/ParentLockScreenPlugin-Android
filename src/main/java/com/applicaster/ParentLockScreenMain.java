package com.applicaster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.applicaster.hook_screen.HookScreen;
import com.applicaster.hook_screen.HookScreenListener;
import com.applicaster.plugin_manager.screen.PluginScreen;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.applicaster.hook_screen.HookScreenManager.ACTIVITY_HOOK_RESULT_CODE;
import static com.applicaster.hook_screen.HookScreenManager.HOOK_PROPS_EXTRA;

public class ParentLockScreenMain implements PluginScreen, HookScreen {
    HashMap<String, String> hookScreen = new HashMap<>();
    private Map<String, ?> hookProps = new HashMap<>();

    @Override
    public void present(Context context, HashMap<String, Object> screenMap, Serializable dataSource, boolean isActivity) {

    }

    @Override
    public Fragment generateFragment(HashMap<String, Object> screenMap, Serializable dataSource) {
        return null;
    }

    //Hook Screen Interface implementation
    @NotNull
    @Override
    public HashMap<String, String> getHook() {
        return null;
    }

    @Override
    public void setHook(@NotNull HashMap<String, String> hookScreen) {
        this.hookScreen = hookScreen;
    }

    //execute hook
    @Override
    public void executeHook(@NotNull Context context, @NotNull HookScreenListener hookScreenListener, @Nullable Map<String, ?> map) {

        Intent intent = new Intent(context, MainActivity.class);
        startActivityForResult(intent, context, map);
        hookProps = map;
    }

    private void startActivityForResult(Intent intent, Context context, Map<String,?> hookProps) {
        if (hookProps != null) {
            intent.putExtra(HOOK_PROPS_EXTRA, new Gson().toJson(hookProps));
        }
        ((Activity) context).startActivityForResult(intent, ACTIVITY_HOOK_RESULT_CODE);
    }

    //Android specific method to return the hookListener from Screen Hook
    @NotNull
    @Override
    public HookScreenListener getListener() {
        return null;
    }

    //Specifies the logic for cases when user dismissed the hook.
    @Override
    public void hookDismissed() {

    }

    //Determines if failed hook will abort.
    @Override
    public boolean isFlowBlocker() {
        return false;
    }

    //Determines if hook can me presented every time specific screen loads.
    @Override
    public boolean isRecurringHook() {
        return true;
    }

    //Determines if hook screen will be presenting to UI.
    @Override
    public boolean shouldPresent() {
        return true;
    }
}
