package com.zhy.iocdemo.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhy.inject.InjectView;
import com.zhy.inject_annotation.CBindEventEnum;
import com.zhy.inject_annotation.annotation.CBindEvent;
import com.zhy.inject_annotation.annotation.CSetContentView;
import com.zhy.iocdemo.R;
import com.zy.skin_update.utils.SkinManager;

@CSetContentView(R.layout.activity_skin_list)
public class SkinListActivity extends AppCompatActivity {

    private String skinPath1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/skin_update_1.skin";
    private String skinPath2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/skin_update_2.skin";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectView.init(this);

    }

    @CBindEvent(listenerType = CBindEventEnum.COnClick,
            value = {R.id.defaultSkin,R.id.skin1,R.id.skin2})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.defaultSkin:
                SkinManager.getInstance().resetSkin();
                break;
            case R.id.skin1:
                SkinManager.getInstance().loadExtraResource(skinPath1);
                break;
            case R.id.skin2:
                SkinManager.getInstance().loadExtraResource(skinPath2);
                break;

        }
    }


}
