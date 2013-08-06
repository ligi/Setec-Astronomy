package org.ligi.setec_astronomy;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class SetecAstronomyService extends Service {

	private LogWatcher log_watcher;
	private NotificationManager myNotificationManager;
	  
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		log_watcher.stop();
		myNotificationManager.cancel(R.string.app_name);
		Toast.makeText(this,R.string.setec_astronomy_service_stopped, Toast.LENGTH_LONG).show();
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		myNotificationManager  = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		Toast.makeText(this,R.string.setec_astronomy_service_started, Toast.LENGTH_LONG).show();
		log_watcher=new LogWatcher(this);
		showNotification();
		super.onStart(intent, startId);
	}

	 private void showNotification() {
	        // In this sample, we'll use the same text for the ticker and the expanded notification
	        CharSequence text = getText(R.string.app_name);

	        // Set the icon, scrolling text and timestamp
	        Notification notification = new Notification(R.drawable.ic_launcher, text,
	                System.currentTimeMillis());

	        // The PendingIntent to launch our activity if the user selects this notification
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
	                new Intent(this, SetecAstronomyActivity.class), 0);

	        // Set the info for the views that show in the notification panel.
	        notification.setLatestEventInfo(this, getText(R.string.app_name),
	                       text, contentIntent);

	        // Send the notification.
	        myNotificationManager.notify(R.string.app_name, notification);
	    }
}
