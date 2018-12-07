package com.bsoft.hospital.pub.suzhoumh.activity.app.map;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine.DrivingStep;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine.TransitStep;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine.WalkingStep;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.model.RouteStepVo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Tank E-mail:zkljxq@126.com
 * @类说明
 */
public class RouteActivity extends BaseActivity implements
		BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener {
	String titleStr;
	double latitude, longitude;

	View bottom;
	TextView title;
	ImageView back, driving, transit, walking;

	// 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
	// 如果不处理touch事件，则无需继承，直接使用MapView即可
	MapView mMapView = null; // 地图View
	BaiduMap mBaidumap = null;
	// 搜索相关
	RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	OverlayManager routeOverlay = null;
	PlanNode stNode;
	PlanNode enNode;

	// ArrayList<DrivingRouteLine> drivingRouteLines;
	// ArrayList<TransitRouteLine> transitRouteLines;
	// ArrayList<WalkingRouteLine> walkingRouteLines;
	DrivingRouteLine drivingRouteLine;
	TransitRouteLine transitRouteLine;
	WalkingRouteLine walkingRouteLine;

	int current = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seek_route);
		titleStr = getIntent().getStringExtra("titleStr");
		latitude = getIntent().getDoubleExtra("latitude", 0);
		longitude = getIntent().getDoubleExtra("longitude", 0);
		mMapView = (MapView) findViewById(R.id.map);
		mBaidumap = mMapView.getMap();

		MapStatus mMapStatus = new MapStatus.Builder().zoom(18).build();
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaidumap.setMapStatus(mMapStatusUpdate);

		// 地图点击事件处理
		mBaidumap.setOnMapClickListener(this);
		// 初始化搜索模块，注册事件监听
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);
		findView();

		stNode = PlanNode.withLocation(new LatLng(latitude, longitude));
		//enNode = PlanNode.withLocation(new LatLng(Double.parseDouble(loginUser.latitude), Double.parseDouble(loginUser.longitude)));
		//mSearch.drivingSearch((new DrivingRoutePlanOption()).from(
				//stNode).to(enNode));
		enNode = PlanNode.withLocation(new LatLng(31.275998, 120.603982));
		 mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(
		 enNode));
		/*mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode)
				.city("苏州").to(enNode));*/
	}

	public void findView() {
		back = (ImageView) findViewById(R.id.back);
		driving = (ImageView) findViewById(R.id.driving);
		transit = (ImageView) findViewById(R.id.transit);
		walking = (ImageView) findViewById(R.id.walking);
		title = (TextView) findViewById(R.id.title);
		bottom = findViewById(R.id.bottom);
		back.setOnClickListener(clickListener);
		driving.setOnClickListener(clickListener);
		transit.setOnClickListener(clickListener);
		walking.setOnClickListener(clickListener);
		bottom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				switch (current) {
				case 1:
					if (null != drivingRouteLine) {
						Intent intent = new Intent(RouteActivity.this,
								RouteDetailActivity.class);
						intent.putExtra("vo", getRouteStep(drivingRouteLine));
						startActivity(intent);
					} else {
						Toast.makeText(RouteActivity.this, "抱歉，未找到路径",
								Toast.LENGTH_SHORT).show();
					}
					break;
				case 2:
					if (null != transitRouteLine) {
						Intent intent = new Intent(RouteActivity.this,
								RouteDetailActivity.class);
						intent.putExtra("vo", getRouteStep(transitRouteLine));
						startActivity(intent);
					} else {
						Toast.makeText(RouteActivity.this, "抱歉，未找到路径",
								Toast.LENGTH_SHORT).show();
					}
					break;
				case 3:
					if (null != walkingRouteLine) {
						Intent intent = new Intent(RouteActivity.this,
								RouteDetailActivity.class);
						intent.putExtra("vo", getRouteStep(walkingRouteLine));
						startActivity(intent);
					} else {
						Toast.makeText(RouteActivity.this, "抱歉，未找到路径",
								Toast.LENGTH_SHORT).show();
					}
					break;
				default:
					break;
				}
			}
		});
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 重置浏览节点的路线数据
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.driving:
				if (1 != current) {
					mBaidumap.clear();
					mSearch.drivingSearch((new DrivingRoutePlanOption()).from(
							stNode).to(enNode));
					current = 1;
					driving.setImageResource(R.drawable.common_topbar_route_car_pressed);
					transit.setImageResource(R.drawable.common_topbar_route_bus_normal);
					walking.setImageResource(R.drawable.common_topbar_route_foot_normal);
				}
				break;
			case R.id.transit:
				if (2 != current) {
					mBaidumap.clear();
					mSearch.transitSearch((new TransitRoutePlanOption())
							.from(stNode)
							.city("苏州")
							.to(enNode));
					current = 2;
					driving.setImageResource(R.drawable.common_topbar_route_car_normal);
					transit.setImageResource(R.drawable.common_topbar_route_bus_pressed);
					walking.setImageResource(R.drawable.common_topbar_route_foot_normal);
				}
				break;
			case R.id.walking:
				if (3 != current) {
					mBaidumap.clear();
					mSearch.walkingSearch((new WalkingRoutePlanOption()).from(
							stNode).to(enNode));
					current = 3;
					driving.setImageResource(R.drawable.common_topbar_route_car_normal);
					transit.setImageResource(R.drawable.common_topbar_route_bus_normal);
					walking.setImageResource(R.drawable.common_topbar_route_foot_pressed);
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RouteActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			walkingRouteLine = result.getRouteLines().get(0);
			title.setText(getTime(walkingRouteLine.getDuration()) + "   "
					+ getDistance(walkingRouteLine.getDistance()));
			WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
			mBaidumap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}

	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {

		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RouteActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			transitRouteLine = result.getRouteLines().get(0);
			title.setText(getTime(transitRouteLine.getDuration()) + "   "
					+ getDistance(transitRouteLine.getDistance()));
			TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
			mBaidumap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RouteActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			drivingRouteLine = result.getRouteLines().get(0);
			title.setText(getTime(drivingRouteLine.getDuration()) + "   "
					+ getDistance(drivingRouteLine.getDistance()));
			DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
			routeOverlay = overlay;
			mBaidumap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	// 定制RouteOverly
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
		}
	}

	private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

		public MyWalkingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
		}
	}

	private class MyTransitRouteOverlay extends TransitRouteOverlay {

		public MyTransitRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
		}
	}

	@Override
	public void onMapClick(LatLng point) {
		mBaidumap.hideInfoWindow();
	}

	@Override
	public boolean onMapPoiClick(MapPoi poi) {
		return false;
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		mSearch.destroy();
		mMapView.onDestroy();
		super.onDestroy();
	}

	public static String getTime(int t) {
		return t / 60 + "分钟";
	}

	public static String getDistance(int t) {
		return String.format("%.2f", t / 1000.0) + "公里";
	}

	public RouteStepVo getRouteStep(DrivingRouteLine line) {
		RouteStepVo rvo = new RouteStepVo();
		if (null != line && null != line.getAllStep()
				&& line.getAllStep().size() > 0) {
			rvo.endtext = titleStr;
			rvo.distance = getDistance(line.getDistance());
			rvo.time = getTime(line.getDuration());
			for (DrivingStep step : line.getAllStep()) {
				rvo.steps.add(step.getInstructions());
			}
			return rvo;
		}
		return null;
	}

	public RouteStepVo getRouteStep(TransitRouteLine line) {
		RouteStepVo rvo = new RouteStepVo();
		if (null != line && null != line.getAllStep()
				&& line.getAllStep().size() > 0) {
			rvo.endtext = titleStr;
			rvo.distance = getDistance(line.getDistance());
			rvo.time = getTime(line.getDuration());
			for (TransitStep step : line.getAllStep()) {
				rvo.steps.add(step.getInstructions());
			}
			return rvo;
		}
		return null;
	}

	public RouteStepVo getRouteStep(WalkingRouteLine line) {
		RouteStepVo rvo = new RouteStepVo();
		if (null != line && null != line.getAllStep()
				&& line.getAllStep().size() > 0) {
			rvo.endtext = titleStr;
			rvo.distance = getDistance(line.getDistance());
			rvo.time = getTime(line.getDuration());
			for (WalkingStep step : line.getAllStep()) {
				rvo.steps.add(step.getInstructions());
			}
			return rvo;
		}
		return null;
	}

}
