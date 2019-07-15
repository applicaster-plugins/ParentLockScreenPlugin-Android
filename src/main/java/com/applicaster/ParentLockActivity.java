package com.applicaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.applicaster.web.plugins.iai.R;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.Map;

import static com.applicaster.hook_screen.HookScreenManager.ACTIVITY_HOOK_COMPLETED;
import static com.applicaster.hook_screen.HookScreenManager.ACTIVITY_HOOK_FAILED;

public class ParentLockActivity extends AppCompatActivity implements ParentLockFragment.ParentLockListener {

    public static final String HOOK_DATA = "screen data";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Map<String, String> hookScreen = (Map<String, String>)getIntent().getSerializableExtra(HOOK_DATA);
        LinkedTreeMap styles = getDefaultStyles();
        LinkedTreeMap generalStyles = getDefaultGeneralStyles();

        if(hookScreen != null){
            String screenMapString = hookScreen.get("screenMap");
            if(screenMapString != null){
                Map screenMap = (new Gson().fromJson(screenMapString, LinkedTreeMap.class));
                if (screenMap != null){
                    styles.putAll( (LinkedTreeMap) screenMap.get("styles"));
                    generalStyles.putAll((LinkedTreeMap) screenMap.get("general"));
                }
            }
        }

        setContentView(com.applicaster.web.plugins.iai.R.layout.activity_m);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.item_detail_container, ParentLockFragment.getInstance(styles, generalStyles), "ff")
                .disallowAddToBackStack()
                .commit();
    }

    @Override
    public void onComplete(boolean isSuccess) {
        sendActivityResult(isSuccess);
    }

    private void sendActivityResult(Boolean isSuccess) {
        Intent returnIntent = getIntent();
        if (isSuccess){
            this.setResult(ACTIVITY_HOOK_COMPLETED, returnIntent);
        }else {
            this.setResult(ACTIVITY_HOOK_FAILED, returnIntent);
        }

        this.finish();
    }

    private LinkedTreeMap getDefaultStyles(){
        LinkedTreeMap map =  new LinkedTreeMap<>();
        map.put("random_numbers_font_size", "14");
        map.put("secondary_random_numbers_font_weight", "SFUIText-LightItalic");
        map.put("number_buttons_selected_background_color", "#ff9261E2");
        map.put("call_for_action_text_color", "#9632F7");
        map.put("family", "FAMILY_1");
        map.put("number_buttons_background_color", "#ff9261E2");
        map.put("force_nav_bar_hidden", true);
        map.put("call_for_action_text_font_weight", "SFUIText-Medium");
        map.put("secondary_random_numbers_font_size", "12");
        map.put("number_color", "#9356E8");
        map.put("number_color_pressed", "#ffffff");
        map.put("call_for_action_text_font_size", "15");
        map.put("number_font_weight", "Billy-Light");
        map.put("random_numbers_color", "#C6A4FF");
        map.put("random_numbers_font_weight", "SFUIText-Regular");
        map.put("presentation", "push");
        map.put("number_font_size", "25");
        map.put("secondary_random_numbers_color", "#9F9F9F");
        return map;
    }

    private LinkedTreeMap getDefaultGeneralStyles(){
        LinkedTreeMap map =  new LinkedTreeMap();
        map.put("validation_flow_type", "3");
        map.put("background_color", "#ff0000");
        map.put("background_type", "image");
        map.put("indicator_normal", "#ffffff");
        map.put("indicator_highlighted", "#9261E2");
        map.put("background_image", "Visible");
        return map;
    }

}
