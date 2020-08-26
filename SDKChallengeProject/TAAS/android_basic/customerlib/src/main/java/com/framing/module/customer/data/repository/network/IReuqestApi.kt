package com.young.businessmvvm.data.repository.network

import com.framing.module.customer.data.bean.SimpleDataBean
import com.framing.module.customer.data.bean.TestBean
import com.young.bean.PageAllBean
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming
import retrofit2.http.Url


/*
 * Des Retrofit 构造请求
 * Author Young
 *
 */interface IReuqestApi {

    @GET("/taas_brain/app/mappage")
    fun getPageAllData( ) : Observable<SimpleDataBean<PageAllBean>>
    @Streaming
    @GET
    fun downloadMap(@Url fileUrl: String?): Call<ResponseBody?>?


}