package zs.com.supremeapp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import zs.com.supremeapp.R;
import zs.com.supremeapp.api.ChatApi;
import zs.com.supremeapp.manager.Platform;
import zs.com.supremeapp.model.NearbyDO;
import zs.com.supremeapp.model.NearbyResultDO;
import zs.com.supremeapp.network.INetWorkCallback;
import zs.com.supremeapp.utils.DataUtils;
import zs.com.supremeapp.utils.TDFPermissionUtils;

/**
 * 附近的人界面
 * Created by liujian on 2018/8/10.
 */

public class NearbyPeopleActivity extends BaseActivity implements View.OnClickListener{

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 88;

    @BindView(R.id.mmap)
    MapView mapView;
    @BindView(R.id.backLayout)
    View backLayout;

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private BaiduMap mBaiduMap;
    private boolean isFirstLoc = true;
    private List<Marker> mMarkers = Collections.synchronizedList(new ArrayList<Marker>());
    private boolean isSettingBack;

    //需要进行检测的权限数组
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE
            //   Manifest.permission.READ_PHONE_STATE
    };

    private List<NearbyDO> nearbyDOList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.initActivity(R.layout.activity_nearby_people);
        super.onCreate(savedInstanceState);

        backLayout.setOnClickListener(this);
        initBaiduMap();
        getNearby();
    }

    private void getNearby(){
        showProcessDialog(true);
        Map<String, String> params = new HashMap<>();
        params.put("userid", Platform.getInstance().getUsrId());
        new ChatApi().getNearby(params, new INetWorkCallback<NearbyResultDO>() {
            @Override
            public void success(NearbyResultDO nearbyResultDO, Object... objects) {
                showProcessDialog(false);
                if(nearbyResultDO != null && !DataUtils.isListEmpty(nearbyResultDO.getList())){
                    nearbyDOList = nearbyResultDO.getList();
                }
                checkLocationPermission();
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(NearbyPeopleActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initBaiduMap(){
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        // BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.delet_zhaopian_1x);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null);
        mBaiduMap.setMyLocationConfiguration(config);

        // 当不需要定位图层时关闭定位图层
        //mBaiduMap.setMyLocationEnabled(false);

        // 点击Marker事件响应
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                NearbyDO nearbyDO = nearbyDOList.get(marker.getZIndex());
                String url = "http://app.cw2009.com/s/" + nearbyDO.getUser_id() + ".html";
                Intent intent = new Intent(NearbyPeopleActivity.this, WebActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                return false;
            }
        });

        //地图点击事件响应
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //点击地图某个位置获取经纬度latLng.latitude、latLng.longitude
                Log.d("latLng", latLng.toString());
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                //点击地图上的poi图标获取描述信息：mapPoi.getName()，经纬度：mapPoi.getPosition()
                return false;
            }
        });
    }

    private void location() {
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();

        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedAddress(true);

        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
        option.setCoorType("bd09ll");

        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效
        option.setScanSpan(1000);

        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setOpenGps(true);

        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setLocationNotify(true);

        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(false);

        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.SetIgnoreCacheException(false);

        //可选，7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位
        option.setWifiCacheTimeOut(5 * 60 * 1000);

        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setEnableSimulateGps(false);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 动态检查定位所需权限
     */
    private void checkLocationPermission() {
        TDFPermissionUtils.needPermission(this, LOCATION_PERMISSION_REQUEST_CODE, needPermissions, new TDFPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                location();
            }

            @Override
            public void onPermissionDenied() {
                new AlertDialog.Builder(NearbyPeopleActivity.this)
                        .setTitle("提示")
                        .setMessage("定位服务未开启，请点击【立即开启】去设置")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                location();
                            }
                        })
                        .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                                isSettingBack = true;
                            }
                        }).create().show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLocationClient != null){
            mLocationClient.stop();
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
        if(isSettingBack){
            location();
            isSettingBack = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            /**
             *当首次定位时，记得要放大地图，便于观察具体的位置
             * LatLng是缩放的中心点，这里注意一定要和上面设置给地图的经纬度一致；
             * MapStatus.Builder 地图状态构造器
             */
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                //设置缩放中心点；缩放比例；
                builder.target(ll).zoom(18.0f);
                //给地图设置状态
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            /*
             * 绘制Marker，地图上常见的类似气球形状的图层
             */
            for(int i = 0; i<nearbyDOList.size();i++){
                NearbyDO nearbyDO = nearbyDOList.get(i);
                if(!TextUtils.isEmpty(nearbyDO.getLatitude()) && !TextUtils.isEmpty(nearbyDO.getLongitude())){
                    try{
                        makeMarker(Double.parseDouble(nearbyDO.getLatitude()), Double.parseDouble(nearbyDO.getLongitude()), nearbyDO.getUser_avatar(),i);
                        builder.include(new LatLng(Double.parseDouble(nearbyDO.getLatitude()), Double.parseDouble(nearbyDO.getLongitude())));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
            builder.include(new LatLng(location.getLatitude(), location.getLongitude()));
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(builder.build());
            mBaiduMap.setMapStatus(u);
        }
    }

    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }

    private void makeMarker(double latitude, double longitude, String url, final int index){
        final MarkerOptions markerOptions = new MarkerOptions();//参数设置类
        LatLng latLng = new LatLng(latitude, longitude);
        markerOptions.position(latLng);//marker坐标位置

        final ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).setProgressiveRenderingEnabled(true).build();
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline()
                .fetchDecodedImage(imageRequest, this);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {

            @Override
            public void onNewResultImpl(Bitmap bitmap) {
                if (bitmap != null){
                    View view = LayoutInflater.from(NearbyPeopleActivity.this).inflate(R.layout.view_map_marker, null);
                    ((CircleImageView)view.findViewById(R.id.imageView)).setImageBitmap(bitmap);
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view));
//                    BitmapDescriptor icon = BitmapDescriptorFactory
//                            .fromResource(R.drawable.delet_zhaopian_1x);

                    markerOptions.icon(icon);//marker图标，可以自定义
                    markerOptions.draggable(false);//是否可拖拽，默认不可拖拽
                    markerOptions.anchor(0.5f, 1.0f);//设置 marker覆盖物与位置点的位置关系，默认（0.5f, 1.0f）水平居中，垂直下对齐
                    markerOptions.alpha(0.8f);//marker图标透明度，0~1.0，默认为1.0
                    markerOptions.animateType(MarkerOptions.MarkerAnimateType.drop);//marker出现的方式，从天上掉下
                    markerOptions.flat(false);//marker突变是否平贴地面
                    markerOptions.zIndex(index);//index

                    // Marker动画效果
                    // markerOptions.icons(bitmapList);//如果需要显示动画，可以设置多张图片轮番显示
                    // markerOptions.period(10);//每个10ms显示bitmapList里面的图片

                    Marker marker = (Marker) mBaiduMap.addOverlay(markerOptions);//在地图上增加mMarker图层
                    mMarkers.add(marker);
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {

            }
        }, CallerThreadExecutor.getInstance());
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.backLayout){
            finish();
        }
    }
}
