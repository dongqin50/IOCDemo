package com.zhy.iocdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zhy.inject.InjectView;
import com.zhy.inject_annotation.CBindEventEnum;
import com.zhy.inject_annotation.annotation.CBindEvent;
import com.zhy.inject_annotation.annotation.CSetContentView;
import com.zhy.iocdemo.PluginManager;
import com.zhy.iocdemo.R;
import com.zhy.iocdemo.aop.annotation.Log;
import com.zhy.iocdemo.reflex.annotation.BindView;
import com.zy.skin_update.utils.SkinManager;

import java.io.File;


@CSetContentView(R.layout.activity_aspectjtest)
public class AspectJTestActivity extends AppCompatActivity {

    private String skinPath1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/skin_update_1.skin";
    private String skinPath2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/skin_update_2.skin";

    @BindView(R.id.button1)
    Button button;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectView.init(this);
    }

    @Log("AspectJTestActivity")
    @CBindEvent(listenerType = CBindEventEnum.COnClick,value = {
            R.id.button,
            R.id.button1,R.id.button2,R.id.button3})
    public void clickButton(View view){
        switch (view.getId()){
            case R.id.button:
                //Class.forName只能获取已安装的包的信息
                String activityName = PluginManager.getInstance().loadExtraApk(this,"taopiaopiao.apk",true);
                Intent intent = new Intent(this,ProxyActivity.class);
                intent.putExtra("className",activityName);
                startActivity(intent);

//                registerReceiver()
                break;
            case R.id.button1:
                SkinManager.getInstance().resetSkin();
                break;
            case R.id.button2:
                File file1 = new File(skinPath1);
                if(file1.exists()){
                    SkinManager.getInstance().loadExtraResource(skinPath1);
                }
                break;
            case R.id.button3:
                File file2 = new File(skinPath2);
                if(file2.exists()){
                    SkinManager.getInstance().loadExtraResource(skinPath2);
                }
                break;
        }
//        Toast.makeText(this,"clickButton" + view.getId(),Toast.LENGTH_SHORT).show();
    }



}
