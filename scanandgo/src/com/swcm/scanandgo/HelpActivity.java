package com.swcm.scanandgo;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

/**
 * 
 * @author Carlos Martin-Cleto y Antonio Prada
 *
 */
public class HelpActivity extends SherlockActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		getSupportActionBar().setTitle(R.string.help);
	}
}
