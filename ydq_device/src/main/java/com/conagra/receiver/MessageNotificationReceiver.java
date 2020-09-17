package com.conagra.receiver;

import android.content.Context;

import com.conagra.MainApplication;
import com.xiaoye.myutils.utils.BadgeCountUtils;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;


public class MessageNotificationReceiver extends PushMessageReceiver {
    /* push 通知到达事件*/
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
//        AppShortCutUtil.addNumShortCut(context,null,true,MainApplication.messageAmount+"",true);
//        ShortcutBadger.with(context).count(MainApplication.messageAmount); //for 1.1.3
//        BadgeUtils.setBadgeCount(context,MainApplication.messageAmount);

        BadgeCountUtils.setBadgeCount(context,MainApplication.messageAmount);
        return false; // 返回 false, 会弹出融云 SDK 默认通知; 返回 true, 融云 SDK 不会弹通知, 通知需要由您自定义。
    }

    /* push 通知点击事件 */
    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
//        HashMap<String, Boolean> hashMap = new HashMap<>();
//        //会话类型 以及是否聚合显示
//        hashMap.put(Conversation.ConversationType.PRIVATE.getName(),false);
//        hashMap.put(Conversation.ConversationType.PUSH_SERVICE.getName(),true);
//        hashMap.put(Conversation.ConversationType.SYSTEM.getName(),true);
//        RongIM.getInstance().startConversationList(context,hashMap);
        return false; // 返回 false, 会走融云 SDK 默认处理逻辑, 即点击该通知会打开会话列表或会话界面; 返回 true, 则由您自定义处理逻辑。
    }
}