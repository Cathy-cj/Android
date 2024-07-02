package com.example.chatapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.chatapp.base.dialog.LoadingDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding ?: throw NullPointerException("_binding create failed")

    private var loadingDialog: LoadingDialog? = null

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = getViewBinding(inflater, container)
        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun launchIO(block: () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            block()
        }
    }

    fun showToast(content: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), content, Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * 展示加载框
     * @param text 显示文字
     * @param cancelable 是否可取消
     */
    fun showLoadingDialog(text: String? = null, cancelable: Boolean = true) {
        requireActivity().runOnUiThread {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialog(requireContext())
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
        requireActivity().runOnUiThread {
            loadingDialog?.dismiss()
        }
    }
}
