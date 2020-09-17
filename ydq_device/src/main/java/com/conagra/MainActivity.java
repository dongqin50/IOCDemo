package com.conagra;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.react.ReactActivity;
import com.joker.annotation.PermissionsGranted;
import com.joker.annotation.PermissionsRequestSync;
import com.joker.api.Permissions4M;
import com.pgyersdk.feedback.PgyFeedback;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;

import static com.conagra.MainActivity.REQUEST_COARSE_LOCATION;
import static com.conagra.MainActivity.REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSIONS;
import static com.conagra.MainActivity.REQUEST_CODE_WRITE_EXTERNAL_STORAGE;

@PermissionsRequestSync(
        permission = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION},
        value = {REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSIONS,
                REQUEST_CODE_WRITE_EXTERNAL_STORAGE,
                REQUEST_COARSE_LOCATION})
public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSIONS = 1;
    public static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 2;
    public static final int REQUEST_COARSE_LOCATION = 3;


    @PermissionsGranted({REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSIONS, REQUEST_CODE_WRITE_EXTERNAL_STORAGE, REQUEST_COARSE_LOCATION})
    public void granted(int code) {
        switch (code) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSIONS:
//                ToastUtil.show("地理位置权限授权成功");
                break;
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE:
//                ToastUtil.show("传感器权限授权成功");
                break;
            case REQUEST_COARSE_LOCATION:
//                ToastUtil.show("读取日历权限授权成功");
                break;
            default:
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPyg();
    }

    private void initPyg() {

//        if(Build.VERSION.SDK_INT > 14) {
        PgyFeedback.getInstance().setMoreParam("tao", "PgyFeedbackShakeManager");
        PgyFeedbackShakeManager.setShakingThreshold(1000);
        Permissions4M.get(this).requestSync();
//        XXPermissions.with(this)
//                .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
////                .permission(Permission.SYSTEM_ALERT_WINDOW, Permission.REQUEST_INSTALL_PACKAGES) //支持请求6.0悬浮窗权限8.0请求安装权限
//                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.REQUEST_INSTALL_PACKAGES) //支持请求6.0悬浮窗权限8.0请求安装权限
//                .permission(Permission.Group.STORAGE, Permission.Group.CALENDAR) //不指定权限则自动获取清单中的危险权限
//                .request(new OnPermission() {
//
//                    @Override
//                    public void hasPermission(List<String> granted, boolean isAll) {
//
//                    }
//
//                    @Override
//                    public void noPermission(List<String> denied, boolean quick) {
//
//                    }
//                });



//        }
        //动态请求权限
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(
//                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
//                        REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSIONS);
//                requestPermissions(
//                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
//            }
//
//            if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(
//                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
//                        REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
//            }
//
//
//        }

//        Permissions4M.get(this)
//                // 是否强制弹出权限申请对话框，建议为 true
//                .requestForce(true)             // 权限
//                .requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION)             // 权限码
//                .requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)             // 权限码
////                .requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)             // 权限码
//                .requestCode(REQUEST_COARSE_LOCATION)             // 如果需要使用 @PermissionNonRationale 注解的话，建议添加如下一行             // 返回的 intent 是跳转至**系统设置页面**
//                .requestPageType(Permissions4M.PageType.MANAGER_PAGE)             // 返回的 intent 是跳转至**手机管家页面**             //
//                .requestPageType(Permissions4M.PageType.ANDROID_SETTING_PAGE)
//                .request();
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        Permissions4M.onRequestPermissionsResult(this, requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE: {
//                for (int i = 0; i < permissions.length; i++) {
//                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//
//
//                    } else {
//                    }
//
//                }
//            }
//            case REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSIONS: {
//                for (int i = 0; i < permissions.length; i++) {
//                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(this, "允许读写存储！", Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        Toast.makeText(this, "未允许读写存储！", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            }
//            break;
//            default: {
//                super.onRequestPermissionsResult(requestCode, permissions,
//                        grantResults);
//            }
//        }
    }
}
