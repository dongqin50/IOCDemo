package com.zy.hook_login;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HookManager {

    private static HookManager mHookManager;

    public static void init() {
        if (mHookManager == null) {
            synchronized (HookManager.class) {
                if (mHookManager == null) {
                    mHookManager = new HookManager();
                }
            }
        }
    }

    public static HookManager getInstance() {
        return mHookManager;
    }

    public void hookManager(Context context) {
        hookLoginPoint(context);
        hookDealLoginPointIntentData(context);
//        hookDealLoginPointIntentDataMethod2(context);
    }

    private void hookLoginPoint(Context context) {
//        IActivityManagerSingleton
//        Class clazz = null;
        try {
            Class clazz = Class.forName(ActivityManager.class.getName());
            Field field = clazz.getDeclaredField("IActivityManagerSingleton");
            field.setAccessible(true);
            Object activityManagerSingleton = field.get(null);

            Class singleClass = Class.forName("android.util.Singleton");
            Field instance = singleClass.getDeclaredField("mInstance");
            instance.setAccessible(true);

            Object instanceObject = instance.get(activityManagerSingleton);

            Object proxy = Proxy.newProxyInstance(instanceObject.getClass().getClassLoader(),
                    instanceObject.getClass().getInterfaces(), new LoginPointInvocationHandler(instanceObject, context));
            instance.set(activityManagerSingleton, proxy);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static class LoginPointInvocationHandler implements InvocationHandler {

        private Context mContext;
        private Object target;

        public LoginPointInvocationHandler(Object target, Context context) {
            this.target = target;
            this.mContext = context;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.v("HookManagerInfo", " invoke : " + method.getName());
            if ("startActivity".equals(method.getName())) {
                launchStartActivity(mContext, args);
            }

            return method.invoke(target, args);
        }

        private void launchStartActivity(Context context, Object[] args) {

            Intent intent = null;
            int index = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    intent = (Intent) args[i];
                    index = i;
                    break;
                }
            }
            if (intent == null) return;
            Intent newIntent = new Intent(context,ScrollingActivity.class);
            newIntent.putExtra("realIntent", intent);
            args[index] = newIntent;
        }
    }

    //出来hookLoginPoint传递的数据，由于9.0版本hide的太多，无法反射，所以无法用
    private static void hookDealLoginPointIntentDataMethod2(Context context){

        try {

            Class activityThread = Class.forName("android.app.ActivityThread");
            Field  sCurrentActivityThreadField = activityThread.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            Object sCurrentActivityThread = sCurrentActivityThreadField.get(null);

            Field transactionExecutorField = activityThread.getDeclaredField("mTransactionExecutor");
            transactionExecutorField.setAccessible(true);
            final Object transactionExecutor = transactionExecutorField.get(sCurrentActivityThread);

            Object transactionExecutorProxy = Proxy.newProxyInstance(transactionExecutor.getClass().getClassLoader(),
                    transactionExecutor.getClass().getInterfaces(),
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            Log.v("fdfdfrrrrsss"," method : " + method.getName() );
                            if(method.getName().equals("performLifecycleSequence")){

                                Object activityClientRecord = args[0];
                                Class activityClientRecordClass = Class.forName("android.app.ActivityThread$$ActivityClientRecord");
                                Field intentField = activityClientRecordClass.getDeclaredField("intent");
                                intentField.setAccessible(true);
                                Intent intent = (Intent) intentField.get(activityClientRecord);
                                Intent realIntent = intent.getParcelableExtra("realIntent");
                                if(realIntent != null){
                                    if(SharedPreferencesUtils.getInstance().isLogin()){
                                        intent.setComponent(realIntent.getComponent());
                                    }else {
                                        intent.setComponent(new ComponentName(intent.getComponent().getClassName(),LoginActivity.class.getName()));
                                    }
//                                    intentField.set(activityClientRecord,intent);
                                }

                            }

                            return method.invoke(transactionExecutor,args);
                        }
                    });

            transactionExecutorField.set(sCurrentActivityThread,transactionExecutorProxy);
//
//            Class singletonClass = Class.forName("android.util.Singleton");
//
//            Field instanceField = singletonClass.getDeclaredField("mInstance");
//            instanceField.setAccessible(true);
//            Object activityManagerService = instanceField.get(iActivityManagerSingleton);
//            Method iActivityManagerMethod = singletonClass.getDeclaredMethod("get",new Class[]{});
//            final Object iActivityManager = iActivityManagerMethod.invoke(iActivityManagerSingleton);
//            Object iActivityManagerProxy = Proxy.newProxyInstance(iActivityManager.getClass().getClassLoader(),
//                    iActivityManager.getClass().getInterfaces(),
//                    new InvocationHandler() {
//                        @Override
//                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                           Log.v("fdfdfrrrrsss"," method : " + method.getName() );
//                            return method.invoke(iActivityManager,args);
//                        }
//                    });
//
//            instanceField.set(iActivityManagerSingleton,iActivityManagerProxy);
//            Method activityStartControllerMethod = iActivityManager.getClass().getMethod("getActivityStartController",new Class[]{});
//            final Object activityStartController = activityStartControllerMethod.invoke(iActivityManager);
////            final Object activityStartController = activityStartControllerField.get(iActivityManager);
//            Object activityStartControllerProxy = Proxy.newProxyInstance(activityStartController.getClass().getClassLoader(), activityManagerService.getClass().getInterfaces(), new InvocationHandler() {
//                @Override
//                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//
//                    if(method.getName().equals("obtainStarter")){
//                        for(int i = 0; i < args.length; i++){
//                            if(args[i] instanceof Intent){
//                                Intent  intent = (Intent) args[i];
//                                Intent realIntent = intent.getParcelableExtra("realIntent");
//                                if(realIntent != null){
//                                    if(!SharedPreferencesUtils.getInstance().isLogin()){
//                                        ComponentName componentName = new ComponentName(intent.getComponent().getClassName(),LoginActivity.class.getName());
//                                        intent.setComponent(componentName);
//                                    }else {
//                                        args[i] = realIntent;
//                                    }
//                                }
//                                break;
//                            }
//                        }
//
//                    }
//
//                    return method.invoke(activityStartController,args);
//
//                }
//            });
//            activityStartControllerField.set(activityManagerService,activityStartControllerProxy);


        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }




    private static void hookDealLoginPointIntentData(Context context) {
        try {
            Class activityThread = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadField = activityThread.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            Object sCurrentActivityThread = sCurrentActivityThreadField.get(null);
            Field mHField = activityThread.getDeclaredField("mH");
            mHField.setAccessible(true);
            Object mH = mHField.get(sCurrentActivityThread);
            Field mCallbackField = Handler.class.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);
            mCallbackField.set(mH, new ActivityThreadHandler(context, (Handler) mH));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static class ActivityThreadHandler implements Handler.Callback {

        private Handler mHandler;
//        private Context mContext;

        public ActivityThreadHandler(Context context, Handler mHandler) {
            this.mHandler = mHandler;
//            this.mContext = context;
        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    if (msg.obj != null) {
                        try {
                            Field intentField = msg.obj.getClass().getDeclaredField("intent");
                            intentField.setAccessible(true);
                            Intent intent = (Intent) intentField.get(msg.obj);
                            Intent oldIntent = intent.getParcelableExtra("oldIntent");
                            if (oldIntent != null) {
                                Log.v("HookManagerInfo1", " msg : " + msg.what);
                                ComponentName componentName = intent.getComponent();
                                if (!SharedPreferencesUtils.getInstance().isLogin() && !componentName.getClassName().equals(LoginActivity.class.getName())) {
                                    Intent newIntent = new Intent();

                                    Field mClass =componentName.getClass().getDeclaredField("mClass");
                                    mClass.setAccessible(true);
                                    mClass.set(componentName, Main2Activity.class.getName());
                                    newIntent.setComponent(componentName);
                                    Log.v("dfd", "fdfd");
                                    intentField.set(msg.obj, newIntent);
                                } else {
                                    intentField.set(msg.obj, oldIntent);
                                }
                            }

                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            mHandler.handleMessage(msg);
            return true;
        }
    }
}
