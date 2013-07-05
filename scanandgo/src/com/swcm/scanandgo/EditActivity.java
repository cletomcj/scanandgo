package com.swcm.scanandgo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;
import com.swcm.scanandgo.model.Product;

/**
 * 
 * @author Carlos Martin-Cleto y Antonio Prada
 *
 */
public class EditActivity extends SherlockActivity {
	
	// Para diferenciar activities en el onActivityResult()
	public static final int REQUEST_CODE = 321;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		getSupportActionBar().setTitle(R.string.edit);
		final EditText etTitle = (EditText) findViewById(R.id.editTextTitle);
		final EditText etDesc = (EditText) findViewById(R.id.editTextDesc);
		Button b = (Button) findViewById(R.id.buttonOk);
		final Product p = (Product) getIntent().getSerializableExtra(Product.TAG);
		etTitle.setText(p.getName());
		etDesc.setText(p.getDescription());
		b.setOnClickListener( new OnClickListener() {	
			@Override
			public void onClick(View v) {
				p.setName(etTitle.getText().toString());
				p.setDescription(etDesc.getText().toString());
				Intent data = new Intent();
				data.putExtra(Product.TAG, p);
				setResult(Activity.RESULT_OK, data);
				finish(); //cerramos la activity
			}
		});
	}

	

}
