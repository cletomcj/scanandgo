package com.swcm.scanandgo;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 
 * @author Carlos Martin-Cleto y Antonio Prada
 *
 */
public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		//A–adimos la opcion de cachear las im‡genes en disco y en memoria
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc().build();
		//Cargamos las im‡genes con la configuraci—n por defecto
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(
				defaultOptions).build();
		ImageLoader.getInstance().init(config);
	}
}