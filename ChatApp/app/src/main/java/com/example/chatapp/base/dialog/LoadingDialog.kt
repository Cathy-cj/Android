package com.example.chatapp.base.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.chatapp.R
import com.example.chatapp.databinding.DialogLoadingViewBinding

class LoadingDialog(
    context: Context,
) : BaseDialog(context, R.style.TransparentDialog) {

    private val binding: DialogLoadingViewBinding

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading_view, null)
        binding = DialogLoadingViewBinding.bind(view)
        setContentView(view)
    }

    fun setLoadingText(text: String?) {
        if (text == null) {
            binding.root.background = null
            binding.tvLoadingText.visibility = View.GONE
        } else {
            binding.root.setBackgroundResource(R.drawable.background_dialog_loading)
            binding.tvLoadingText.visibility = View.VISIBLE
            binding.tvLoadingText.text = text
        }
    }
}