package com.meiku.dev.services;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.utils.FileConstant;
import com.meiku.dev.utils.FileHelper;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.Tool;

/**
 * 即时开启的服务，用来后台下载赛事广告图片 ，完成后关闭
 * 
 */
public class LoadImageServices extends Service {

	private String newAdUrl;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			newAdUrl = intent.getStringExtra("url");
			LogUtil.d("hl", "***** ,下载首页广告图url=" + newAdUrl);
			// 下载网页图片到本地
			if (Tool.isEmpty(newAdUrl)) {
				stopSelf();// 下载完成关闭服务
				return 0;
			}
			ImageRequest imageRequest = ImageRequestBuilder
					.newBuilderWithSource(Uri.parse(newAdUrl))
					.setProgressiveRenderingEnabled(true).build();
			ImagePipeline imagePipeline = Fresco.getImagePipeline();
			DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline
					.fetchDecodedImage(imageRequest, this);
			dataSource.subscribe(new BaseBitmapDataSubscriber() {
				@Override
				public void onNewResultImpl(@Nullable Bitmap bitmap) {
					String path = FileConstant.SD_PATH
							+ FileConstant.MATCHAD_PATH + "home_ad.png";
					FileHelper.deleteFile(path);//如果旧的存在就删掉
					FileHelper.savePic(bitmap, path);//存新的图片文件
					PreferHelper.setSharedParam(ConstantKey.SP_TIPAD_URL,
							newAdUrl);
					LogUtil.d("hl", "赛事首页图，下载完成");
					stopSelf();// 下载完成关闭服务
				}

				@Override
				public void onFailureImpl(DataSource dataSource) {
					stopSelf();
				}
			}, CallerThreadExecutor.getInstance());
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
