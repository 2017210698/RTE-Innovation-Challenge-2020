package com.framing.module.customer.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.os.HandlerCompat.postDelayed
import androidx.core.os.postDelayed
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.framing.baselib.TLog
import com.framing.commonlib.inject.policy.PermissionPolicy
import com.framing.commonlib.network.HeaderInfoInit
import com.framing.commonlib.network.RequestBuild
import com.framing.commonlib.utils.BarUtils
import com.young.aac.base.MvvmBaseActivity
import com.framing.module.customer.databinding.CContainerActivityBinding
import com.framing.module.customer.ui.viewmodel.CContainerUIVM
import com.framing.module.customer.data.viewmodel.CContainerDataVM
import com.framing.module.customer.BR
import com.framing.module.customer.CustomerShareVM
import com.framing.module.customer.R
import com.framing.module.customer.data.bean.SimpleDataBean
import com.young.bean.PageAllBean
import com.young.businessmvvm.data.repository.network.NetRequestManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Des 容器类 控制分发 控制业务 相当于app controller
 * Created by Young on 2020-08-11 19:15:07
 */
class CContainerActivity :  MvvmBaseActivity<CContainerActivityBinding, CContainerUIVM,CContainerDataVM>(){

    private var appShareVM:CustomerShareVM?=null//app共享 CContainerActivity dataVM 目前也是相当于app
    private var contentNavCtrl: NavController?=null//主内容分发控制器
    private var startNavCtrl: NavController?=null//启动页分发控制器

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appShareVM=getAppViewModelProvider()?.get(CustomerShareVM::class.java)
        contentNavCtrl=findNavController(R.id.content_fragment_host)
        startNavCtrl=findNavController(R.id.launch_fragment_host)
        NetRequestManager.get().setNetworkRequestInfo(HeaderInfoInit())
        uiLogic()
        getUIViewModel().isDialogShow.postValue(true)
        permissionRun(PermissionPolicy.P_LOC){
            //权限请求
        }
        GlobalScope?.launch{
            delay(5000)
            appShareVM?.isStartHide?.postValue(true) //执行完了
            getUIViewModel().isDialogShow.postValue(false)
        }
        getDataViewModel()?.requestData()
    }

    /*
    * UI 执行逻辑是由 app share vm 控制分发 启动页相关 还是 主内容相关
    * */
    @SuppressLint("ResourceAsColor")
    private fun uiLogic() {
        //UI 状态 和 data 不一致才处理 启动页展示与否
        appShareVM?.isStartHide?.observe(this, Observer {
            if(getUIViewModel().isStartHide.value!=it) {
                getUIViewModel().isStartHide.postValue(true)
            }
        })
        //通知UIload状态
        getDataViewModel()?.loadType?.observe(this, Observer {
            getUIViewModel().loadType.postValue(it)
        })
        //观察 做dialog 显示逻辑
        getUIViewModel().isDialogShow.observe(this, Observer {
            if(getUIViewModel().isStartHide.value!!) {
                //展示弹窗 且在非启动页逻辑情况
                if(it) {
                    getBinding().motionLayout?.run {
                        setTransition(R.id.to_dialog)
                        transitionToEnd()
                    }
                }else{
                    getBinding().motionLayout?.run {
                        setTransition(R.id.to_dialog)
                        transitionToStart()
                    }
                }
            }
        })
        //监听动效流程 配置逻辑
        getBinding().motionLayout.addTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                TLog.log("container_motionLayout","onTransitionTrigger")
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                TLog.log("container_motionLayout","onTransitionStarted")
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                TLog.log("container_motionLayout","onTransitionChange")
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                TLog.log("container_motionLayout","onTransitionCompleted$p1  ${R.id.dialog}")
                if(p1==R.id.dialog){//id 在container动销执行完成再执行dialogstyle 动画
                    getBinding().dialogView?.progress(1f)
                }
            }
        })
    }

    override fun getUIViewModel(): CContainerUIVM {
        return getActivityViewModelProvider(this).get(CContainerUIVM::class.java)
    }
    override fun getDataViewModel(): CContainerDataVM? {
            return getActivityViewModelProvider(this).get(CContainerDataVM::class.java)!!
    }
    override val bindingVariable: Int
        get() = BR.cContainerUIVM
    override val layoutId: Int
        get() = R.layout.c_container_activity
}
