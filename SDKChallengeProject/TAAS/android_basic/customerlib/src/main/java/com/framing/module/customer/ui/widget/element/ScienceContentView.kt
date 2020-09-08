package com.framing.module.customer.ui.widget.element

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.framing.commonlib.base.IBindingClickEvent
import com.framing.commonlib.widget.BaseObserverView
import com.framing.module.customer.R
import com.framing.module.customer.databinding.ScicenceContentParentLayoutBinding

/*
 * Des  科幻载体
 * Author Young
 * Date 
 */open class ScienceContentView :
    BaseObserverView {
    constructor(context: Context) : super(context){
        initView(null)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        initView(attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    var binding:ScicenceContentParentLayoutBinding?=null
    var click:IBindingClickEvent<Any>?=null

    open fun initView(attrs: AttributeSet?){
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.scicence_content_parent_layout,this,true);
        binding?.confirmBtn?.visibility= View.GONE
    }
    /*
    * 根据外层执行进度
    * */
    open fun progress(progress:Float){
    }
    /*
    * 处理title内容 贴边居右 居中居中
    * */
    open fun titleLogic(progress:Float){
    }
    open fun clickLisen(click:IBindingClickEvent<Any>){
        this.click=click
    }
}