package dora.permission.aop;

import android.app.Activity;
import android.support.annotation.NonNull;

import dora.permission.Action;
import dora.permission.PermissionManager;
import dora.permission.annotation.CheckPermission;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

@Aspect
public class CheckPermissionAspect {

    private static final int REQUEST_CODE_SETTING = 0x01;

    @Pointcut("execution(@dora.permission.annotation.CheckPermission * *(..)) && @annotation(permission)")
    public void checkPermission(CheckPermission permission) {
    }

    @Around("checkPermission(permission)")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, final CheckPermission permission) throws Throwable {
        final Activity activity = (Activity) joinPoint.getTarget();
        if (PermissionManager.hasPermissions(activity, permission.value())) {
            joinPoint.proceed();//获得权限，执行原方法
        } else {
            PermissionManager.with(activity)
                    .runtime()
                    .permission(permission.value())
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> permissions) {
                            try {
                                joinPoint.proceed();//获得权限，执行原方法
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    })
                    .onDenied(new Action<List<String>>() {
                        @Override
                        public void onAction(@NonNull List<String> permissions) {
                            if (PermissionManager.hasAlwaysDeniedPermission(activity, permissions)) {
                                // 如果是被永久拒绝就跳转到应用权限系统设置页面，机型适配有待验证
                                PermissionManager.with(activity).runtime().setting().start(REQUEST_CODE_SETTING);
                            }
                        }
                    })
                    .start();
        }
    }
}
