package org.ligi.setec_astronomy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ServiceStartActivity extends Activity {

	private Intent service_intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.startService(service_intent);
		super.onCreate(savedInstanceState);
	}

}
