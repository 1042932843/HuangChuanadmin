package com.admin.huangchuan.activity;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.admin.huangchuan.R;
import com.admin.huangchuan.model.Images;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class ImageActivity extends Activity implements OnPageChangeListener {

	/**
	 * 用于管理图片的滑动
	 */
	private ViewPager viewPager;

	/**
	 * 显示当前图片的页数
	 */
	private TextView pageText;

	private ImageView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_layout1);
		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
//		int imagePosition = getIntent().getIntExtra("image_position", 0);

		ImageView onlyImageView = (ImageView) findViewById(R.id.image_view);
		if (Images.imageUrls.get(0) != null) {
			Log.d("reg","Images.imageUrls.get(position):"+Images.imageUrls.get(0));
			Picasso.with(ImageActivity.this).load(Images.imageUrls.get(0)).into(onlyImageView);
		}

//		pageText = (TextView) findViewById(R.id.page_text);
//		viewPager = (ViewPager) findViewById(R.id.view_pager);
//		ViewPagerAdapter adapter = new ViewPagerAdapter();
//		viewPager.setAdapter(adapter);
//		viewPager.setCurrentItem(imagePosition);
//		viewPager.setOnPageChangeListener(this);
//		viewPager.setEnabled(false);
		// 设定当前的页数和总页数
//		pageText.setText((imagePosition + 1) + "/" + Images.imageUrls.size());
	}

	/**
	 *
	 * TODO<ViewPager的适配器>
	 *
	 * @author ZhuZiQiang
	 * @data: 2014-4-4 下午3:54:43
	 * @version: V1.0
	 */
	class ViewPagerAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = LayoutInflater.from(ImageActivity.this).inflate(
					R.layout.image_layout1, null);
			ImageView onlyImageView = (ImageView) view
					.findViewById(R.id.image_view);
			if (Images.imageUrls.get(position) != null) {
				Log.d("reg","Images.imageUrls.get(position):"+Images.imageUrls.get(position));
				Picasso.with(ImageActivity.this).load(Images.imageUrls.get(position)).into(onlyImageView);
			}else {
				onlyImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
			}
			container.addView(view);
			return view;
		}

		@Override
		public int getCount() {
			return Images.imageUrls.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			container.removeView(view);
		}

	}

	/**
	 * 获取图片的本地存储路径。
	 *
	 * @param imageUrl
	 *            图片的URL地址。
	 * @return 图片的本地存储路径。
	 */
	private String getImagePath(String imageUrl) {
		int lastSlashIndex = imageUrl.lastIndexOf("/");
		String imageName = imageUrl.substring(lastSlashIndex + 1);
		String imageDir = Environment.getExternalStorageDirectory().getPath()
				+ "/PhotoWallFalls/";
		File file = new File(imageDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		String imagePath = imageDir + imageName;
		return imagePath;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int currentPage) {
		// 每当页数发生改变时重新设定一遍当前的页数和总页数
		pageText.setText((currentPage + 1) + "/" + Images.imageUrls.size());
	}
	/**
	 * 通过读取Assets目录下面的文件,将其转为Bitmap对象
	 * @param fileName
	 * @return
	 */
	private static Bitmap getImageFromAssetsFile(Context context, String fileName){
		Bitmap image = null;
		AssetManager manager = context.getResources().getAssets();
		try {
			InputStream inputStream = manager.open(fileName);
			image = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
}
