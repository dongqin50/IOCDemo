package com.conagra;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.RNFetchBlob.RNFetchBlobPackage;
import com.conagra.di.component.ApplicationComponent;
import com.conagra.di.component.DaggerApplicationComponent;
import com.conagra.di.module.ApplicationModule;
import com.conagra.di.module.NetModule;
import com.imagepicker.ImagePickerPackage;
import com.pgyersdk.crash.PgyCrashManager;
import java.util.Arrays;
import java.util.List;


public class MainApplication extends Application  {
  public static int messageAmount;
  private ApplicationComponent mApplicationComponent;

  @Override
  public void onCreate() {
    super.onCreate();
    if (Build.VERSION.SDK_INT > 18) {
      PgyCrashManager.register(getApplicationContext());
    }

//    if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
////      Logger.init("LiuLei");
//      RongIM.setServerInfo("nav.cn.ronghub.com", "up.qbox.me");
//      RongIM.init(this);
////            SealAppContext.init(this);
////    SharedPreferencesContextUtils.init(this);
////    SharedPreferencesUtils.init(getSharePreferences(),this);
//
//      Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));
//      try {
////      SharedPreferencesUtils.init(this);
//        RongIM.registerMessageType(GroupNotificationMessage.class);
//        RongIM.registerMessageType(FileMessage.class);
////      SealAppContext.init(this);
////            RongIM.setUserInfoProvider(this,true)
////                RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());
//        RongIM.registerMessageTemplate(new RealTimeLocationMessageProvider());
//        RongIM.registerMessageTemplate(new GroupNotificationMessageItemProvider());
//        RongIM.registerMessageTemplate(new FileMessageItemProvider());
////      RongIM.registerMessageTemplate(new MessageNotificationReceiver());
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//      SoLoader.init(this, /* native exopackage */ false);
//    }
    initializeInjector();
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

//  private void initIM() {
//    if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
//
////            LeakCanary.install(this);//内存泄露检测
//      RongPushClient.registerHWPush(this);
//      RongPushClient.registerMiPush(this, "2882303761517473625", "5451747338625");
//      RongPushClient.registerMZPush(this, "112988", "2fa951a802ac4bd5843d694517307896");
//      try {
//        RongPushClient.registerFCM(this);
//      } catch (RongException e) {
//        e.printStackTrace();
//      }
//
//      /**
//       * 注意：
//       *
//       * IMKit SDK调用第一步 初始化
//       *
//       * context上下文
//       *
//       * 只有两个进程需要初始化，主进程和 push 进程
//       */
//      RongIM.setServerInfo("nav.cn.ronghub.com", "up.qbox.me");
//      RongIM.init(this);
////            NLog.setDebug(true);//Seal Module Log 开关
////            SealAppContext.init(this);
////            SharedPreferencesContext.init(this);
//      Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));
//
//      try {
////                RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());
//        RongIM.registerMessageTemplate(new RealTimeLocationMessageProvider());
////                RongIM.registerMessageType(TestMessage.class);
////                RongIM.registerMessageTemplate(new TestMessageProvider());
//
//
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
////            openSealDBIfHasCachedToken();
//      RongIM.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
//        @Override
//        public void onChanged(RongIMClient.ConnectionStatusListener.ConnectionStatus status) {
//          if (status == ConnectionStatus.TOKEN_INCORRECT) {
//            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
//            final String cacheToken = sp.getString("loginToken", "");
//            if (!TextUtils.isEmpty(cacheToken)) {
//              RongIM.connect(cacheToken, new RongIMClient.ConnectCallback() {
//                @Override
//                public void onTokenIncorrect() {
//
//                }
//
//                @Override
//                public void onSuccess(String s) {
//
//                }
//
//                @Override
//                public void onError(RongIMClient.ErrorCode errorCode) {
//
//                }
//              });
//            } else {
//              Log.e("seal", "token is empty, can not reconnect");
//            }
//          }
//        }
//      });

//            options = new DisplayImageOptions.Builder()
//                    .showImageForEmptyUri(cn.rongcloud.im.R.drawable.de_default_portrait)
//                    .showImageOnFail(cn.rongcloud.im.R.drawable.de_default_portrait)
//                    .showImageOnLoading(cn.rongcloud.im.R.drawable.de_default_portrait)
//                    .displayer(new FadeInBitmapDisplayer(300))
//                    .cacheInMemory(true)
//                    .cacheOnDisk(true)
//                    .build();

//            RongExtensionManager.getInstance().registerExtensionModule(new PTTExtensionModule(this, true, 1000 * 60));
//            RongExtensionManager.getInstance().registerExtensionModule(new ContactCardExtensionModule(new IContactCardInfoProvider() {
//                @Override
//                public void getContactAllInfoProvider(final IContactCardInfoCallback contactInfoCallback) {
//                    SealUserInfoManager.getInstance().getFriends(new SealUserInfoManager.ResultCallback<List<Friend>>() {
//                        @Override
//                        public void onSuccess(List<Friend> friendList) {
//                            contactInfoCallback.getContactCardInfoCallback(friendList);
//                        }
//
//                        @Override
//                        public void onError(String errString) {
//                            contactInfoCallback.getContactCardInfoCallback(null);
//                        }
//                    });
//                }
//
//                @Override
//                public void getContactAppointedInfoProvider(String userId, String name, String portrait, final IContactCardInfoCallback contactInfoCallback) {
//                    SealUserInfoManager.getInstance().getFriendByID(userId, new SealUserInfoManager.ResultCallback<Friend>() {
//                        @Override
//                        public void onSuccess(Friend friend) {
//                            List<UserInfo> list = new ArrayList<>();
//                            list.add(friend);
//                            contactInfoCallback.getContactCardInfoCallback(list);
//                        }
//
//                        @Override
//                        public void onError(String errString) {
//                            contactInfoCallback.getContactCardInfoCallback(null);
//                        }
//                    });
//                }
//
//            }, new IContactCardClickListener() {
//                @Override
//                public void onContactCardClick(View view, ContactMessage content) {
//                    Intent intent = new Intent(view.getContext(), UserDetailActivity.class);
//                    Friend friend = SealUserInfoManager.getInstance().getFriendByID(content.getId());
//                    if (friend == null) {
//                        UserInfo userInfo = new UserInfo(content.getId(), content.getName(),
//                                Uri.parse(TextUtils.isEmpty(content.getImgUrl()) ? RongGenerate.generateDefaultAvatar(content.getName(), content.getId()) : content.getImgUrl()));
//                        friend = CharacterParser.getInstance().generateFriendFromUserInfo(userInfo);
//                    }
//                    intent.putExtra("friend", friend);
//                    view.getContext().startActivity(intent);
//                }
//            }));
//            RongExtensionManager.getInstance().registerExtensionModule(new RecognizeExtensionModule());
//    }
//  }

  public ApplicationComponent getApplicationComponent() {
    return mApplicationComponent;
  }


  private void initializeInjector() {
    mApplicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .netModule(new NetModule())
            .build();
  }


  public static String getCurProcessName(Context context) {
    int pid = android.os.Process.myPid();
    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
      if (appProcess.pid == pid) {
        return appProcess.processName;
      }
    }
    return null;
  }
}
