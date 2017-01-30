package com.android.encypher.justtrackme.common;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.LruCache;
import android.widget.TextView;


public class SmartHomeFont extends TextView {

	private final static String NAME = "smarthome";
	private static LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(18);


	public SmartHomeFont(Context context) {
		super(context);
		init();
	}

	public SmartHomeFont(Context context, AttributeSet attrs) {
		super(context,attrs);
		init();
	}
	public SmartHomeFont(Context context, AttributeSet attrs, int defStyle){
		super(context,attrs,defStyle);
		init();	
	}
	
	public void init(){
		Typeface typeface = sTypefaceCache.get(NAME);

		if (typeface == null) {

			typeface = Typeface.createFromAsset(getContext().getAssets(), "smarthome.ttf");
			sTypefaceCache.put(NAME, typeface);

		}

		setTypeface(typeface);
	}
}