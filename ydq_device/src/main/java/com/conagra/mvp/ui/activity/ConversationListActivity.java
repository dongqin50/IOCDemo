package com.conagra.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.conagra.MainActivity;
import com.conagra.R;
import com.conagra.cache.Cache;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by yedongqin on 2017/2/27.
 */

public class ConversationListActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.conversationlist;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHeader("消息");
        Intent intent = getIntent();
//
//        //push
        if (intent.getData().getScheme().equals("rong") && intent.getData().getQueryParameter("push") != null) {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("push").equals("true")) {
                enterActivity();
            }

        } else {//通知过来
            //程序切到后台，收到消息后点击进入,会执行这里
            if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                enterActivity();
            } else {
//                startActivity(new Intent(ConversationListActivity.this, MainActivity.class));
//                finish();
            }
        }
    }

    /**
     * 收到 push 消息后，选择进入哪个 Activity
     * 如果程序缓存未被清理，进入 MainActivity
     * 程序缓存被清理，进入 LoginActivity，重新获取token
     * <p/>
     * 作用：由于在 manifest 中 intent-filter 是配置在 ConversationListActivity 下面，所以收到消息后点击notifacition 会跳转到 DemoActivity。
     * 以跳到 MainActivity 为例：
     * 在 ConversationListActivity 收到消息后，选择进入 MainActivity，这样就把 MainActivity 激活了，当你读完收到的消息点击 返回键 时，程序会退到
     * MainActivity 页面，而不是直接退回到 桌面。
     */
    private void enterActivity() {
        String token = Cache.getToken();
        if (TextUtils.isEmpty(token)) {
            startActivity(new Intent(ConversationListActivity.this, MainActivity.class));
            finish();
        } else {
            reconnect(token);
        }
    }


    private void reconnect(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
//                Log.e(TAG, "---onTokenIncorrect--");
            }

            @Override
            public void onSuccess(String s) {
//                Log.i(TAG, "---onSuccess--" + s);
//                if (mDialog != null)
//                    mDialog.dismiss();
                startActivity(new Intent(ConversationListActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
//                Log.e(TAG, "---onError--" + e);
            }
        });

    }

}
