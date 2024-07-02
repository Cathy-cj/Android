package com.example.chatapp.base.dialog

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.StyleRes

abstract class BaseDialog(context: Context, @StyleRes theme: Int) : Dialog(context, theme),
    Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        if (context == activity) {
            dismiss()
        }
    }

    override fun dismiss() {
        super.dismiss()
        // 注销生命周期回调
        (context as? Activity)?.application?.unregisterActivityLifecycleCallbacks(this)
    }

    override fun show() {
        try {
            if (context is Activity) {
                val activity = context as Activity
                if (!activity.isFinishing && !activity.isDestroyed) {
                    super.show()
                    // 注册生命周期回调
                    activity.application.registerActivityLifecycleCallbacks(this)
                }
            } else {
                super.show()
            }
        } catch (e: WindowManager.BadTokenException) {
            // 处理窗口没有附加的异常
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            // 处理活动处于无效状态的异常
            e.printStackTrace()
        }
    }
}


