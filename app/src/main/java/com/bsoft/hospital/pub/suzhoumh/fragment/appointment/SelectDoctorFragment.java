package com.bsoft.hospital.pub.suzhoumh.fragment.appointment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.bitmap.view.RoundImageView;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.app.appoint.AppointDoctorInfoActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.appoint.AppointSelectDateActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.appoint.AppointSelectDoctorActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.fragment.index.BaseFragment;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDeptVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDoctorVo;
import com.bsoft.hospital.pub.suzhoumh.util.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.bsoft.hospital.pub.suzhoumh.AppApplication.loginUser;

/**
 * @author :lizhengcao
 * @date :2017/2/16
 * E-mail:lizc@bsoft.com.cn
 * @类说明
 */

public class SelectDoctorFragment extends Fragment {

    private ListView lv_doctor;
    private List<AppointDoctorVo> list;
    private DoctorAdapter adapter;
    private String ksdm;
    private GetDataTask task;
    private AppointDeptVo dept;
    public ImageLoader imageLoader = ImageLoader.getInstance();
    private EditText et_search;
    private ImageView ib_search_clear;
    private List<AppointDoctorVo> searchlist = new ArrayList<AppointDoctorVo>();

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if(intent.getAction().equals(Constants.ACTION_APPOINT_CLOSE))
            {
                getActivity().finish();
            }
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_select,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView(view);
        initData();
    }

    private void initData() {
        list = new ArrayList<>();
        adapter = new DoctorAdapter(list);
        dept = (AppointDeptVo) getActivity().getIntent().getSerializableExtra("dept");
        lv_doctor.setAdapter(adapter);

        task=new GetDataTask();
        task.execute();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_APPOINT_CLOSE);
        getActivity().registerReceiver(receiver, filter);

        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                if(content!=null&&!content.equals(""))
                {
                    searchlist.clear();
                    for(int i=0;i<list.size();i++)
                    {
                        if(list.get(i).ygxm.contains(content))
                        {
                            searchlist.add(list.get(i));
                        }
                    }
                    adapter.refresh(searchlist);
                }
                else
                {
                    adapter.refresh(list);
                }
            }
        });

        ib_search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_search.getText() != null && !et_search.getText().toString().equals("")) {
                    et_search.setText("");
                    adapter.refresh(list);
                }
            }
        });
    }

    private void initView(View view) {
        lv_doctor = (ListView) view.findViewById(R.id.lv_dept);
        et_search = (EditText) view.findViewById(R.id.et_search);
        ib_search_clear = (ImageView) view.findViewById(R.id.ib_search_clear);
    }

    class GetDataTask extends AsyncTask<Void, Void, ResultModel<ArrayList<AppointDoctorVo>>>
    {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//            actionBar.startTextRefresh();
        }
        @Override
        protected void onPostExecute(ResultModel<ArrayList<AppointDoctorVo>> result) {
            // TODO Auto-generated method stub
//            actionBar.endTextRefresh();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        list = result.list;
                        adapter.refresh(list);
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "当前科室无医生", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    result.showToast(getActivity());
                }
            }
        }

        @Override
        protected ResultModel<ArrayList<AppointDoctorVo>> doInBackground(
                Void... params) {
            // TODO Auto-generated method stub
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("method","listys");
            map.put("as_ksdm", dept.ksdm);
            return HttpApi.getInstance().parserArray_His(AppointDoctorVo.class,
                    "hiss/ser",map,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }
    }

    class DoctorAdapter extends BaseAdapter
    {

        private List<AppointDoctorVo> mlist;

        public DoctorAdapter(List<AppointDoctorVo> list)
        {
            mlist = list;
        }

        public void refresh(List<AppointDoctorVo> list)
        {
            mlist = list;
            adapter.notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mlist.get(position);
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.select_doctor_item_appoint, null);
                holder.iv_head = (RoundImageView)convertView.findViewById(R.id.iv_head);
                holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
                holder.tv_intro = (TextView)convertView.findViewById(R.id.tv_intro);
                holder.tv_dept = (TextView)convertView.findViewById(R.id.tv_dept);
                holder.tv_profession = (TextView)convertView.findViewById(R.id.tv_profession);
                holder.btn_consult = (Button)convertView.findViewById(R.id.btn_consult);
                holder.tv_see_all = (TextView)convertView.findViewById(R.id.tv_see_all);
                holder.btn_appoint = (Button)convertView.findViewById(R.id.btn_appoint);
                holder.btn_see_docinfo = (Button)convertView.findViewById(R.id.btn_see_doctinfo);
                holder.tv_zjfy = (TextView)convertView.findViewById(R.id.tv_zjfy);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.tv_name.setText(mlist.get(position).ygxm);
            holder.tv_intro.setText(mlist.get(position).ysjj);
            holder.tv_profession.setText(mlist.get(position).ygjb);
            if(!mlist.get(position).zjfy.equals(""))
            {
                holder.tv_zjfy.setText("专家费 "+mlist.get(position).zjfy);
            }
            else
            {
                holder.tv_zjfy.setText("");
            }
            //holder.tv_dept.setText(dept.ksmc);
            holder.btn_consult.setVisibility(View.GONE);
            holder.btn_see_docinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),AppointDoctorInfoActivity.class);
                    intent.putExtra("ygdm",mlist.get(position).ygdm);
                    startActivity(intent);
                }
            });
            holder.btn_appoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),AppointSelectDateActivity.class);
                    intent.putExtra("doctor", mlist.get(position));
                    intent.putExtra("dept", dept);
                    intent.putExtra("type", 1);//专家挂号
                    startActivity(intent);
                }
            });
            if(!mlist.get(position).ysjj.equals(""))
            {
                holder.tv_see_all.setVisibility(View.VISIBLE);
                holder.tv_see_all.setOnClickListener(new View.OnClickListener() {
                    Boolean flag = true;

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        // Log.i("tv.getLineCount()",tv.getHeight()+"");
                        if (flag) {

                            flag = false;
                            holder.tv_intro.setEllipsize(null); // 展开
                            holder.tv_intro.setSingleLine(flag);
                            Drawable drawable = getResources().getDrawable(
                                    R.drawable.info_up);
                            // / 这一步必须要做,否则不会显示.
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                                    drawable.getMinimumHeight());
                            holder.tv_see_all.setCompoundDrawables(null, null,
                                    drawable, null);
                        } else {
                            flag = true;
                            holder.tv_intro.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                            holder.tv_intro.setMaxLines(2);
                            Drawable drawable = getResources().getDrawable(
                                    R.drawable.info_down);
                            // / 这一步必须要做,否则不会显示.
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                                    drawable.getMinimumHeight());
                            holder.tv_see_all.setCompoundDrawables(null, null,
                                    drawable, null);
                            // tv.setSingleLine(flag);
                        }
                    }
                });
            }
            else
            {
                holder.tv_see_all.setVisibility(View.GONE);
            }
			/*if(list.get(position).sex.equals("男"))
			{
				holder.iv_head.setImageResource(R.drawable.doc_male);
			}
			else if(list.get(position).sex.equals("女"))
			{
				holder.iv_head.setImageResource(R.drawable.doc_female);
			}*/
            holder.iv_head.setImageResource(R.drawable.doc_header);
            final String url = Constants.getHeadUrl()+Constants.getHospitalID()+"/"+mlist.get(position).ygdm+"_150x150"+".png";
            holder.iv_head.setTag(url);
            // 显示图片的配置
            try
            {
                new Thread() {
                    public void run() {
                        //这儿是耗时操作，完成之后更新UI；
                        if(Utility.checkURL(url))
                        {
                            getActivity().runOnUiThread(new Runnable(){

                                @Override
                                public void run() {
                                    //更新UI
                                    DisplayImageOptions options = new DisplayImageOptions.Builder()
                                            .cacheInMemory(true).cacheOnDisk(true)
                                            // .displayer(new RoundedBitmapDisplayer(20))
                                            .bitmapConfig(Bitmap.Config.RGB_565).build();
                                    imageLoader.loadImage(url, options,
                                            new SimpleImageLoadingListener() {

                                                @Override
                                                public void onLoadingComplete(String imageUri,
                                                                              View view, Bitmap loadedImage) {
                                                    // TODO Auto-generated method stub
                                                    super.onLoadingComplete(imageUri, view,
                                                            loadedImage);
                                                    if (holder.iv_head.getTag() != null
                                                            && holder.iv_head.getTag().equals(url)) {
                                                        Drawable drawable = new BitmapDrawable(
                                                                loadedImage);
                                                        //holder.iv_head.setBackground(drawable);
                                                        holder.iv_head.setImageDrawable(drawable);
                                                    }
                                                }

                                            });
                                }

                            });
                        }
                    }
                }.start();
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            return convertView;
        }

        class ViewHolder
        {
            RoundImageView iv_head;
            TextView tv_name;
            TextView tv_intro;
            TextView tv_profession;
            TextView tv_dept;
            TextView tv_see_all;
            TextView tv_zjfy;
            Button btn_appoint;
            Button btn_see_docinfo;
            Button btn_consult;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(task);
        if(receiver!=null)
        {
            getActivity().unregisterReceiver(receiver);
        }
        if(imageLoader!=null)
        {
            imageLoader.clearMemoryCache();
        }
    }
}
