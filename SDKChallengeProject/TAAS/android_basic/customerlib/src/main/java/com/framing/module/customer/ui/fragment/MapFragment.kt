package com.framing.module.customer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.young.aac.base.MvvmBaseFragment
import com.framing.module.customer.databinding.MapFragmentLayoutBinding
import com.framing.module.customer.ui.viewmodel.MapUIVM
import com.framing.module.customer.data.viewmodel.MapDataVM
import com.framing.module.customer.BR
import com.framing.module.customer.R
/**
 * Des 进入首页map 地图
 * Created by VULCAN on 2020-08-13 13:30:55
 */
class MapFragment :  MvvmBaseFragment<MapFragmentLayoutBinding, MapUIVM,MapDataVM>(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(getBinding().mapView)
    }

    override fun getUIViewModel(): MapUIVM {
        return getActivityViewModelProvider(requireActivity()).get(MapUIVM::class.java)
    }
    override fun getDataViewModel(): MapDataVM? {
        return getActivityViewModelProvider(requireActivity()).get(MapDataVM::class.java)!!
    }
    override val bindingVariable: Int
        get() = BR._all
    override val layoutId: Int
        get() = R.layout.map_fragment_layout
}