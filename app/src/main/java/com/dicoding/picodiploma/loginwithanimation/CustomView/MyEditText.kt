package com.dicoding.picodiploma.loginwithanimation.CustomView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
// This code for custom view password
class MyEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val parentLayout = parent.parent as? com.google.android.material.textfield.TextInputLayout
                if (s.length < 8) {
                    parentLayout?.error = "Password tidak boleh kurang dari 8 karakter"
                } else {
                    parentLayout?.error = null
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing
            }
        })
    }
}
