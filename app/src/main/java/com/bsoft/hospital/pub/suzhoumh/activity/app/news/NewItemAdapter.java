package com.bsoft.hospital.pub.suzhoumh.activity.app.news;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class NewItemAdapter extends BaseAdapter{

	private ArrayList<NewsItem> list;//资讯内容
	private Context context;
	
	public ImageLoader imageLoader = ImageLoader.getInstance();
	
	public NewItemAdapter(Context context,ArrayList<NewsItem> list)
	{
		this.context = context;
		this.list = list;
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}

	/**
	 * 数据刷新
	 * @param list
	 */
	public void refresh(ArrayList<NewsItem> list)
	{
		this.list = list;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.news_list_item, null);
			holder.iv_icon = (ImageView)convertView.findViewById(R.id.iv_icon);
			holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
			holder.tv_content = (TextView)convertView.findViewById(R.id.tv_content);
			holder.tv_date = (TextView)convertView.findViewById(R.id.tv_date);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.tv_title.setText(list.get(position).title);
		holder.tv_content.setText(Html.fromHtml(list.get(position).content).toString());//去掉html标签
		holder.tv_date.setText(DateUtil.getDateTime("yyyy-MM-dd",Long.parseLong(list.get(position).createdate)));
		
		holder.iv_icon.setBackground(context.getResources().getDrawable(R.drawable.bg_default_img));
		if(list.get(position).imgurl!=null&&!list.get(position).imgurl.equals(""))
		{
			holder.iv_icon.setTag(list.get(position).imgurl);
			//显示图片的配置  
	        DisplayImageOptions options = new DisplayImageOptions.Builder()  
	                .cacheInMemory(true)  
	                .cacheOnDisk(true)  
	                .bitmapConfig(Bitmap.Config.RGB_565)  
	                .build();  
			
//			imageLoader.loadImage(Constants.HttpUrl+list.get(position).imgurl,options,new SimpleImageLoadingListener(){
			imageLoader.loadImage(Constants.getHttpUrl()+list.get(position).imgurl,options,new SimpleImageLoadingListener(){

				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					// TODO Auto-generated method stub
					super.onLoadingComplete(imageUri, view, loadedImage);
					if(holder.iv_icon.getTag()!=null&&holder.iv_icon.getTag().equals(list.get(position).imgurl))
					{
						Drawable drawable =new BitmapDrawable(loadedImage);
						holder.iv_icon.setBackgroundDrawable(drawable);
					}
				}
				
			});
		}
		//这个会闪烁错乱
		/*holder.iv_icon.setBackground(context.getResources().getDrawable(R.drawable.bg_default_img));
		if(holder.iv_icon.getTag()!=null&&holder.iv_icon.getTag().equals(list.get(position).imgurl))
		{
			imageLoader.displayImage(Constants.HttpUrl+list.get(position).imgurl, holder.iv_icon, new ImageLoadingListener(){

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onLoadingComplete(String arg0, View arg1,
						Bitmap arg2) {
					// TODO Auto-generated method stub
					Drawable drawable =new BitmapDrawable(arg2);
					arg1.setBackground(drawable);
				}

				@Override
				public void onLoadingFailed(String arg0, View arg1,
						FailReason arg2) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
				}
				
			});
		}*/
		
		return convertView;
	}
	
	static class ViewHolder
	{
		ImageView iv_icon;
		TextView tv_title;
		TextView tv_content;
		TextView tv_date;
	}

}
