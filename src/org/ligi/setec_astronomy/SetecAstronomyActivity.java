package org.ligi.setec_astronomy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SetecAstronomyActivity extends Activity {
	
	private Intent service_intent;
	private static boolean service_running=false;
	
	public void label_service_btn() {
		if (service_running) 
			 ((Button)this.findViewById(R.id.start)).setText("Stop");
		else 
			 ((Button)this.findViewById(R.id.start)).setText("Start");
	}
	
    @Override
	protected void onResume() {
    	label_service_btn();
		super.onResume();
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	service_intent=new Intent(this,SetecAstronomyService.class);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);

        ((Button)this.findViewById(R.id.configure)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				startActivity(new Intent(SetecAstronomyActivity.this,ConfigureActivity.class));
			}
        });

        ((Button)this.findViewById(R.id.start)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				if (service_running)
					stopService(service_intent);
				else
					startService(service_intent);
				
				service_running=!service_running;
				
				label_service_btn();
			}

			
        	
        });
    }
    
}