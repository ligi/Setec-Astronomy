package org.ligi.setec_astronomy.penalty;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class KillActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras=getIntent().getExtras();
		
		if ((extras!=null) && extras.containsKey("pkg")) {
		
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMain);
			
			String pkg=extras.getString("pkg");
			
			ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

			am.killBackgroundProcesses(pkg);
		}
		else
			Log.e("SetecAstronomy","dunno what to do - exitting");
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		finish();
	}

}
