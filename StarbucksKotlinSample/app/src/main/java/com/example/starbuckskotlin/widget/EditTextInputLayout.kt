package com.example.starbuckskotlin.widget

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.example.starbuckskotlin.R
import com.example.starbuckskotlin.databinding.EditTextInputLayoutViewBinding

class EditTextInputLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr),
    View.OnFocusChangeListener, TextView.OnEditorActionListener {

    interface OnFocusChangeListener {
        fun onFocusChange(parentView: View?, view: View?, hasFocus: Boolean)
    }

    interface OnEditorActionListener {
        fun onEditorAction(parentView: View?, view: TextView?, actionId: Int, event: KeyEvent?): Boolean
    }

    interface OnValidCheckListener {
        fun checkValidation()
    }

    private lateinit var binding: EditTextInputLayoutViewBinding
    private var hintText: String? = null
    private var emptyTitleText: String? = null
    private var isHintEnabled: Boolean = true
    private var inputType = EditorInfo.TYPE_TEXT_VARIATION_NORMAL

    private var isValid = false
    private var onValidCheckListener: OnValidCheckListener? = null
    private var onFocusChangeListener: OnFocusChangeListener? = null
    private var onEditorActionListener: OnEditorActionListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        attrs?.let {
            initView()
            getAttrs(attrs, defStyleAttr)
        }
    }

    private fun initView() {
        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(li, R.layout.edit_text_input_layout_view, this, false)
        addView(binding.root)

        binding.editInput.onFocusChangeListener = this
        binding.editInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // do nothing
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // do nothing
            }

            override fun afterTextChanged(editable: Editable) {
                if (!binding.editInput.isFocused) {
                    setTitleText()
                }
            }
        })
    }

    private fun getAttrs(attrs: AttributeSet, defStyle: Int) {
        var inputEnable = true
        var imeOptions = EditorInfo.IME_ACTION_DONE
        var maxLength = -1
        var privateImeOptions: String? = null
        var inputTextStyle = Typeface.BOLD
        val a = context.obtainStyledAttributes(attrs, R.styleable.EditTextInputLayout, defStyle, 0)
        val n = a.indexCount
        for (i in 0 until n) {
            when (val attr = a.getIndex(i)) {
                R.styleable.EditTextInputLayout_android_hint -> hintText = a.getString(attr)
                R.styleable.EditTextInputLayout_emptyTitleText -> emptyTitleText = a.getString(attr)
                R.styleable.EditTextInputLayout_hintEnabled -> isHintEnabled = a.getBoolean(attr, true)
                R.styleable.EditTextInputLayout_android_inputType -> inputType = a.getInt(attr, EditorInfo.TYPE_TEXT_VARIATION_NORMAL)
                R.styleable.EditTextInputLayout_android_enabled -> inputEnable = a.getBoolean(attr, true)
                R.styleable.EditTextInputLayout_android_imeOptions -> imeOptions = a.getInt(attr, EditorInfo.IME_ACTION_DONE)
                R.styleable.EditTextInputLayout_android_maxLength -> maxLength = a.getInt(attr, -1)
                R.styleable.EditTextInputLayout_android_privateImeOptions -> privateImeOptions = a.getString(attr)
                R.styleable.EditTextInputLayout_android_textStyle -> inputTextStyle = a.getInt(attr, Typeface.BOLD)
            }
        }
        binding.editInput.setOnEditorActionListener(this)
        binding.textInputLayout.isHintEnabled = isHintEnabled
        setTitleText() // hintText, emptyTitleText 여부에 따라 title 설정
        binding.editInput.inputType = inputType
        binding.editInput.isEnabled = inputEnable
        binding.editInput.imeOptions = imeOptions
        if (0 < maxLength) {
            setMaxLength(maxLength)
        }
        if (null != privateImeOptions) {
            binding.editInput.privateImeOptions = privateImeOptions
        }
        binding.editInput.setTypeface(binding.editInput.typeface, inputTextStyle)
        a.recycle()
    }

    fun setMaxLength(length: Int) {
        val filterArray = arrayOfNulls<InputFilter>(1)
        filterArray[0] = LengthFilter(length)
        binding.editInput.filters = filterArray
    }

    fun setText(@StringRes resId: Int) {
        binding.editInput.setText(resId)
        setTitleText() // hintText, emptyTitleText 여부에 따라 title 설정
    }

    fun setText(text: String?) {
        binding.editInput.setText(text)
        setTitleText() // hintText, emptyTitleText 여부에 따라 title 설정
    }

    fun setHintText(text: String?) {
        if (isHintEnabled) {
            binding.textInputLayout.hint = text
        } else {
            binding.editInput.hint = text
        }
    }

    fun setHintText(hintText: String?, emptyTitleText: String?) {
        this.hintText = hintText
        this.emptyTitleText = emptyTitleText
        setHintText(hintText)
    }

    private fun setTitleText() {
        if (0 == getLength()) {
            // 입력된 값이 없는 경우
            if (TextUtils.isEmpty(emptyTitleText)) {
                // emptyTitleText 값이 없는 경우, hintText값을 title로 지정
                setHintText(hintText)
            } else {
                // emptyTitleText 값이 있는 경우, emptyTitleText 값으로 title 지정
                setHintText(emptyTitleText)
            }
        } else {
            // 입력된 값이 있는 경우, hintText로 title 지정
            setHintText(hintText)
        }
    }

    fun getText(): Editable? {
        return binding.editInput.text
    }

    fun getLength(): Int {
        return if (null != getText()) {
            getText().toString().length
        } else 0
    }

    fun setExternalOnFocusChangeListener(onFocusChangeListener: OnFocusChangeListener?) {
        this.onFocusChangeListener = onFocusChangeListener
    }

    fun setExternalOnEditorActionListener(onEditorActionListener: OnEditorActionListener) {
        this.onEditorActionListener = onEditorActionListener
    }

    fun setValidCheckListener(validCheckListener: OnValidCheckListener) {
        this.onValidCheckListener = validCheckListener
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (hasFocus) {
            // 포커스 뷰 상태로 제공
            showFocusState()
            if (0 == getLength()) {
                // 포커스, 입력된 값이 없는 경우, hintText로 title 지정
                setHintText(hintText)
            }
        } else {
            onValidCheckListener?.checkValidation()
            if (0 == getLength()) {
                // 포커스가 없으며, 입력된 값이 없는 경우
                if (TextUtils.isEmpty(emptyTitleText)) {
                    // emptyTitleText 값이 없는 경우, hintText값을 title로 지정
                    setHintText(hintText)
                } else {
                    // emptyTitleText 값이 있는 경우, emptyTitleText 값으로 title 지정
                    setHintText(emptyTitleText)
                }
            }
        }

        onFocusChangeListener?.onFocusChange(this, view, hasFocus)
    }

    // 포커스시 에러문구 및 에러이미지 제공하지 않음
    private fun showFocusState() {
        binding.errorDesc.visibility = View.GONE
        binding.editWarningImg.visibility = View.GONE
        binding.underline.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.c_00a862, null))
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onValidCheckListener?.checkValidation()
            binding.editInput.clearFocus()
        }

        onEditorActionListener?.onEditorAction(this, v, actionId, event)

        return false
    }

    fun isValid(): Boolean {
        return isValid
    }
}