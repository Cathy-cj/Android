package com.example.chatapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.chatapp.base.dialog.LoadingDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    val binding get() = _binding ?: throw NullPointerException("_binding create failed")

    private var loadingDialog: LoadingDialog? = null

    abstract fun viewBound()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = getViewBinding()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(_binding!!.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewBound()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getViewBinding(): VB {
        val superclass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method: Method = superclass.getMethod("inflate", LayoutInflater::class.java)
        return method.invoke(null, layoutInflater) as VB
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /**
     * 协程 IO 调度
     */
    fun launchIO(block: () -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            block()
        }
    }

    fun showToast(content: String) {
        runOnUiThread {
            Toast.makeText(this.applicationContext, content, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 展示加载框
     * @param text 显示文字
     * @param cancelable 是否可取消
     */
    fun showLoadingDialog(text: String? = null, cancelable: Boolean = true) {
        runOnUiThread {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialog(this)
            }
            with(loadingDialog!!) {
                setLoadingText(text)
                setCancelable(cancelable)
                show()
            }
        }
    }

    /**
     * 关闭加载框
     */
    fun closeLoadingDialog() {
        runOnUiThread {
            loadingDialog?.dismiss()
        }
    }
}
