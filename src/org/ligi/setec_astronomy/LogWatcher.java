package org.ligi.setec_astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.ligi.setec_astronomy.penalty.KillerPenalty;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

public class LogWatcher implements Runnable {

	private boolean running;
	private Context ctx;
	
	public LogWatcher(Context ctx) {
		this.ctx = ctx;
		
		running=true;
		new Thread(this).start();
	}	
 
	
	@Override
	public void run() {
		long started=System.currentTimeMillis();
		try {
			
			while (running) {
				Process process=Runtime.getRuntime().exec("logcat");
				InputStream ins=process.getInputStream();
				/* looks nicer than the time based solution, but doesnt work as available returns 0 in that case :-( 
				ins.skip(ins.available()); 
				*/
				
				BufferedReader reader=new BufferedReader(new InputStreamReader(ins));
		        	
				String line;
				
				while(running&&((line=reader.readLine())!=null)) {
					if (started+1000<System.currentTimeMillis())
					{
						if (line.startsWith("I/ActivityManager") // thats the interesting stuff
							||line.startsWith("I/WindowManager")) {
							String pkg=line.substring(line.indexOf(":")+2);
							CommandAndPackageParser parser=new CommandAndPackageParser(pkg);

							Log.i("IntentAlert"," found pkg:" + parser.getPackage() + " cmd:" + parser.getCommand() + " out:" + pkg);
							if (!parser.getCommand().equals("INVALID")) {
								
								IntentDatabaseSQLHelper intentDB=new IntentDatabaseSQLHelper(ctx);
								SQLiteDatabase db = intentDB.getWritableDatabase();
								
								
							
							    if (parser.getCommand().startsWith("Displayed") 
								    	|| parser.getCommand().startsWith("Start") 
								    	|| parser.getCommand().startsWith("CREATE")) {
							    	Cursor c=	db.query(IntentDatabaseSQLHelper.TABLE, new String[] { IntentDatabaseSQLHelper.INTENT2START },IntentDatabaseSQLHelper.PACKAGE+" = '"+parser.getPackage()+"'" ,null,null,null,null);
							    	
							    	
							    	if ((c.getCount()>0)) {
								    	c.moveToFirst();
								    	String act_cmd=c.getString(c.getColumnIndex(IntentDatabaseSQLHelper.INTENT2START));
								    	if (act_cmd.contains("KILL")) {
				
										    KillerPenalty.kill(ctx, parser.getPackage());
									    } 
								    	if (act_cmd.contains("SOUND")) {
									    	Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
										    Ringtone r = RingtoneManager.getRingtone(ctx.getApplicationContext(), notification);
										    r.play();
									    
									    }
							    	}
							    	else {
							    		ContentValues values = new ContentValues();
										values.put(IntentDatabaseSQLHelper.LAST_SEEN, System.currentTimeMillis());
									    values.put(IntentDatabaseSQLHelper.PACKAGE, parser.getPackage());
									    values.put(IntentDatabaseSQLHelper.LAST_CMD, parser.getCommand());
									    values.put(IntentDatabaseSQLHelper.INTENT2START,"IGNORE");
									    db.insert(IntentDatabaseSQLHelper.TABLE, null, values);
							    	}
							    	
							    }
							    db.close();
							    intentDB.close();
							    
								}
						    }				
					}
				}
			
			}
			
		}
		catch (IOException e) {
			Log.e("IntentWatcher","IO Problem reading log" + e.toString());
	    }
	
	}

	public void stop() {
		running = false;		
	}
}
