package com.swcm.scanandgo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;

/**
 * 
 * @author Carlos Martin-Cleto y Antonio Prada
 *
 */
public class AboutActivity extends SherlockActivity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		getSupportActionBar().setTitle(R.string.about);
		Button bc = (Button) findViewById(R.id.buttonCarlos);
		Button ba = (Button) findViewById(R.id.buttonAntonio);
		bc.setOnClickListener(this);
		ba.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.buttonAntonio:
			String url = "http://www.linkedin.com/in/antoniopradablanco";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
			break;
		case R.id.buttonCarlos:
			String url2 = "http://www.linkedin.com/pub/carlos-martin-cleto/60/891/a4b";
			Intent i2 = new Intent(Intent.ACTION_VIEW);
			i2.setData(Uri.parse(url2));
			startActivity(i2);
			break;
		}
		
	}

	
	

}
