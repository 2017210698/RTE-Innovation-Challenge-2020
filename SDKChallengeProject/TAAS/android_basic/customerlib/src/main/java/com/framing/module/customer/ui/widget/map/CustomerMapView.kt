package com.framing.module.customer.ui.widget.map

import android.R
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.MotionEvent
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.framing.baselib.TLog
import com.framing.commonlib.map.SimpleMapView
import com.framing.commonlib.utils.FileUtils
import com.framing.commonlib.utils.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


/*
 * Des  负载客户端业务的地图view
 * 父类SimpleMapView 负载生命周期
 * 祖父类FramingMapView 负载高德mapview
 * 维护对应分层
 * Author Young
 * Date 
 */
class CustomerMapView : SimpleMapView {
    constructor(p0: Context?) : super(p0)
    constructor(p0: Context?, p1: AttributeSet?) : super(p0, p1)
    constructor(p0: Context?, p1: AttributeSet?, p2: Int) : super(p0, p1, p2)

    private val TEST_AREA_BR=LatLng(37.30299398093054,112.02101053883912)//底左
    private val TEST_AREA_BL=LatLng(37.30313052744131,112.01696442660936)//底右
    private val TEST_AREA_TL=LatLng(37.30569072861245,112.01740833219374)//上左
    private  val TEST_AREA_TR=LatLng(37.30567792782341,112.02136056710957)//上右
    private  val TEST_AREA_CENTER=LatLng(37.303396,112.019825)//聚焦点 //17级

    private val localPath="/storage/emulated/0/TAAS_C/mapCache"
    private val googleUrl="http://mt2.google.cn/vt/lyrs=y&scale=2&hl=zh-CN&gl=cn&x=%d&s=&y=%d&z=%d"
    private var mFileDirName:String?=null
    private var mFileName:String?=null


    override fun urlFilePath(x: Int, y: Int, zoom: Int): String {
        mFileDirName = String.format("L%02d/", zoom + 1)
        mFileName = String.format(
            "%s",
            MapUtils.tileXYToQuadKey(x, y, zoom)
        ) //为了不在手机的图片中显示,取消jpg后缀,文件名自己定义,写入和读取一致即可,由于有自己的bingmap图源服务,所以此处我用的bingmap的文件名
        val LJ: String =
            localPath+File.separator + mFileDirName + mFileName
        TLog.log("customermapview",LJ+"___"+FileUtils.isFileExists(LJ))
        if(FileUtils.isFileExists(LJ)){
            //判断本地是否有图片文件,如果有返回本地url,如果没有,缓存到本地并返回googleurl
            return "file://$LJ"
        }else{
            val filePath = String.format(googleUrl, x, y, zoom)
            val mBitmap: Bitmap
            mBitmap = ImageUtils.getBitmap(getImageStream(filePath))
            try {
                var file= File(localPath +File.separator+ mFileDirName + mFileName);
                var a= ImageUtils.save(mBitmap,
                    file,Bitmap.CompressFormat.JPEG
                )
                TLog.log("customermapview",a.toString()+mBitmap+"__${file.path}")
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return filePath
        }
        return ""
    }

    @Throws(Exception::class)
    fun getImageStream(path: String?): InputStream? {
        val url = URL(path)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 5 * 1000
        conn.requestMethod = "GET"
        return if (conn.responseCode == HttpURLConnection.HTTP_OK) {
            conn.inputStream
        } else null
    }
    override val diskCacheDir: String
        get() =localPath

    override fun drawOverlay() {
        GlobalScope.launch {
            Thread.sleep(3000)
            withContext(Dispatchers.IO){
                Thread.sleep(3000)
                withContext(Dispatchers.Main){
                    //创建测试区域
                    BaseMapDrawBuild().with(map)
                        .style(BaseMapDrawBuild.DrawMapStye.DRAW_RECTANGLE)
                        .targetLatLng(listOf(TEST_AREA_TL,TEST_AREA_TR,TEST_AREA_BR,TEST_AREA_BL),1)
                        .create()
                }
            }
        }
    }

    override fun addMaker(mAmap:AMap) {
        val markerOption = MarkerOptions()
        markerOption.position(TEST_AREA_CENTER)
        markerOption.title("test").snippet("test：${TEST_AREA_CENTER.latitude}, ${TEST_AREA_CENTER.longitude}")

        markerOption.draggable(true) //设置Marker可拖动
        markerOption.icon(
            BitmapDescriptorFactory.fromBitmap(
                BitmapFactory
                    .decodeResource(resources, R.mipmap.sym_def_app_icon)
            )
        )
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.isFlat = true //设置marker平贴地图效果
        mAmap.addMarker(markerOption)
        TLog.log("maker_x",""+markerOption.infoWindowOffsetX)
    }

    override fun moveCamera(mAmap: AMap) {
        GlobalScope.launch {
            Thread.sleep(3000)
            withContext(Dispatchers.IO){
                Thread.sleep(1000)
                withContext(Dispatchers.Main){
                    map.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            TEST_AREA_CENTER, 17f
                        )//放大级别
                    )
                }
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {//搞掉
        return true
    }
}