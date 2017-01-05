package com.meiku.dev.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.R;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.ResumeAttachData;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.morefun.MrrckResumeAttachActivity;
import com.meiku.dev.ui.morefun.TestAudioActivity;
import com.meiku.dev.ui.morefun.TestVideoActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Util;

/**
 * Created by zjh on 2015/8/28.
 */
public class MrrckResumeAttachAdapter extends BaseAdapter {

    private Context mContext;
    
    public final static int REQUESTCODE_UPDATEMKRESUME = 10008;

    private List<ResumeAttachData> mList = new ArrayList<ResumeAttachData>();
    int attachType;
    ViewHolder holder = null;

    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
    private boolean flag = false;

    public MrrckResumeAttachAdapter(Context context) {
        this.mContext = context;
    }

    public void setDatas(List<ResumeAttachData> list, int attachType) {
        this.mList = list;
        initDate();
        this.attachType = attachType;
    }

    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < mList.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // 获得ViewHolder对象
            holder = new ViewHolder();
            // 导入布局并赋值给convertview
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_resume_video, null);
            holder.nameTXT = (TextView) convertView.findViewById(R.id.nameTXT);
            holder.resumeTXT = (TextView) convertView.findViewById(R.id.resumeTXT);
            holder.selVideoCB = (CheckBox) convertView.findViewById(R.id.selVideoCB);

            holder.release_time = (TextView) convertView.findViewById(R.id.release_time);
            holder.duration_time = (TextView) convertView.findViewById(R.id.duration_time);
            holder.video_imageview = (ImageView) convertView.findViewById(R.id.video_imageview);
            holder.view_stop_image = (ImageView) convertView.findViewById(R.id.view_stop_image);
            holder.img_vice = (ImageView) convertView.findViewById(R.id.img_vice);

            holder.video_Layout = (RelativeLayout) convertView.findViewById(R.id.video_Layout);
            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }

        final ResumeAttachData data = mList.get(position);

        // 设置list中TextView的显示
        String title = data.title;
        holder.nameTXT.setText(TextUtils.isEmpty(title) ? "未命名" : title);
        if ("1".equals(data.isResume)) {
            if (null != holder.resumeTXT) {
                holder.resumeTXT.setText("已设置为简历");
            } else {
                holder.resumeTXT.setText("设置为简历");
            }

        } else {
            holder.resumeTXT.setText("设置为简历");
        }
        holder.resumeTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resumeId = AppContext.getInstance().getUserInfo().getResumeId();
                		//AppData.getInstance().getLoginUser().getResumeId();
                if(-1 != resumeId){
                    updateResume(data);
                }else{
                    ToastUtil.showShortToast("请先创建简历");
                }
            }
        });

        if (flag) {

            if("1".equals(data.isResume)){
                holder.selVideoCB.setVisibility(View.GONE);
                getIsSelected().put(position, false);
            }else{
                holder.selVideoCB.setVisibility(View.VISIBLE);
            }
        } else {
            holder.selVideoCB.setVisibility(View.GONE);
        }
        // 根据isSelected来设置checkbox的选中状况
        holder.selVideoCB.setChecked(getIsSelected().get(position));

        holder.release_time.setText(data.createDate.substring(0, 16));
        holder.duration_time.setText(data.fileSeconds + "秒");
        BitmapUtils bitmapUtils=new BitmapUtils(mContext);
        bitmapUtils.display(holder.video_imageview,Util.getFileThumbImage(data.clientFileUrl));
//        ImageLoader.getInstance().displayImage(Util.getFileThumbImage(data.fileUrl), holder.video_imageview);
        if (2 == attachType) {
            holder.view_stop_image.setVisibility(View.GONE);
            holder.video_Layout.setVisibility(View.GONE);
            holder.img_vice.setVisibility(View.VISIBLE);
        } else {
            holder.view_stop_image.setVisibility(View.VISIBLE);
            holder.img_vice.setVisibility(View.GONE);
            holder.video_Layout.setVisibility(View.VISIBLE);
        }
        holder.view_stop_image.setOnClickListener(new View.OnClickListener() {//点击播放
            @Override
            public void onClick(View v) {
                if(1 == attachType){


                if (null != data.clientFileUrl) {
                    Intent intent = new Intent();
                    intent.putExtra("mrrck_videoPath", data.clientFileUrl);
                    intent.setClass(mContext, TestVideoActivity.class);
                    mContext.startActivity(intent);
                } else {
                    ToastUtil.showShortToast("文件不存在");
                }
            }
            }
        });
        holder.img_vice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 声音的播放
                 */
                if (2 == attachType) {
                    if (null != data.clientFileUrl) {
                        Intent intent = new Intent(mContext, TestAudioActivity.class);
                        String filePath = data.clientFileUrl.substring(0, data.clientFileUrl.lastIndexOf("."));
                        intent.putExtra("filePath", filePath);
                        intent.putExtra("recordingTime", data.fileSeconds + "");
                        if (!TextUtils.isEmpty(data.isPublic)) {
                            intent.putExtra(TestAudioActivity.BUNDLE_IS_PUBLISH, data.isPublic);
                        }
                        mContext.startActivity(intent);
                    } else {
                        ToastUtil.showShortToast("文件不存在");
                    }
                }

            }
        });
        holder.selVideoCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox)v;
                if (cb.isChecked()) {
                    getIsSelected().put(position, true);
                } else {
                    getIsSelected().put(position, false);
                }

            }
        });

        return convertView;
    }

    /**
     * 设为简历
     *
     * @param data
     */
    private void updateResume( final  ResumeAttachData data) {
        JSONObject paramObj = new JSONObject();

        try {
            //美库简历ID
            paramObj.put("mrrckResumeId", "" + 
            AppContext.getInstance().getUserInfo().getResumeId());

            if (attachType == 1) { // 视频简历信息
                //语音简历路径
                paramObj.put("voiceResume", "");
                //语音简历长度
                paramObj.put("voiceSeconds", "0");
                //语音简历ID
                paramObj.put("voiceId", "0");

                //视频简历截图路径
                paramObj.put("videoPhoto", Util.getFileThumbImage(data.fileUrl));
                //视频简历路径
                paramObj.put("videoResume", data.fileUrl);
                //视频简历长度
                paramObj.put("videoSeconds", data.fileSeconds);
                //视频简历ID
                paramObj.put("videoId", data.id);
            } else if (attachType == 2) { // 音频简历信息
                //美库简历ID
                paramObj.put("mrrckResumeId", "" + 
                AppContext.getInstance().getUserInfo().getResumeId());

                //语音简历路径
                paramObj.put("voiceResume", data.fileUrl);
                //语音简历长度
                paramObj.put("voiceSeconds", data.fileSeconds);
                //语音简历ID
                paramObj.put("voiceId", data.id);

                //视频简历截图路径
                paramObj.put("videoPhoto", "");
                //视频简历路径
                paramObj.put("videoResume", "");
                //视频简历长度
                paramObj.put("videoSeconds", "0");
                //视频简历ID
                paramObj.put("videoId", "0");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    	ReqBase reqBase = new ReqBase();
        reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_UPDATE_MK_RESUME));
        reqBase.setBody(JsonUtil.String2Object(paramObj.toString()));
        MrrckResumeAttachActivity tempActivity = (MrrckResumeAttachActivity) mContext;
        tempActivity.httpPost(REQUESTCODE_UPDATEMKRESUME, AppConfig.RESUME_REQUEST_MAPPING, reqBase,true);
//        ResumeDataLogic.getInstance().updateMKResumeWithBodyDict(paramObj, new HttpCallback() {
//            @Override
//            public void onSuccess(Object body) {
//                ((MrrckResumeAttachActivity)mContext).initData();
//                notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailed(String error) {
//                ToastUtil.showShortToast(error);
//            }
//        });
    }

    class ViewHolder {
        TextView nameTXT;
        TextView resumeTXT;
        CheckBox selVideoCB;

        TextView release_time;
        TextView duration_time;
        ImageView video_imageview;
        ImageView view_stop_image;
        ImageView img_vice;
        RelativeLayout video_Layout;
    }

    public void setShowCheckBox(boolean flag) {
        this.flag = flag;
        this.notifyDataSetChanged();
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        MrrckResumeAttachAdapter.isSelected = isSelected;
    }
}
