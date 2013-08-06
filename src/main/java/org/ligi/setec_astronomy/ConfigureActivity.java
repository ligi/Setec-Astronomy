package org.ligi.setec_astronomy;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ConfigureActivity extends ListActivity {
	
	private Cursor cursor;
	private IntentDatabaseSQLHelper helper;
	private SQLiteDatabase db;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        helper=new IntentDatabaseSQLHelper(this);
        db = helper.getWritableDatabase();
        
        cursor = db.query(IntentDatabaseSQLHelper.TABLE, null, null, null, null,  null, null);
            
        startManagingCursor(cursor);
        this.setListAdapter(new PackagesCursorAdapter(this,cursor));

    }
    
    @Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		cursor.close();
		db.close();
	    helper.close();
		super.onPause();
	}
	
	class PackagesCursorAdapter extends CursorAdapter {

		public PackagesCursorAdapter(Context context, Cursor c) {
			super(context, c);
		}

		@Override
		public void bindView(View view, Context ctx, Cursor cursor) {
			TextView pkg_name_tv = (TextView)view.findViewById(R.id.pkg_name_tv);
			
			final String pkg=cursor.getString(cursor.getColumnIndex(IntentDatabaseSQLHelper.PACKAGE));
			final String cmd=cursor.getString(cursor.getColumnIndex(IntentDatabaseSQLHelper.INTENT2START));
			Log.i("ConfigurerA" ," c:"+cmd + " p:" + pkg);
			
			pkg_name_tv.setText(pkg);
			
		    final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		            ctx, R.array.modes, android.R.layout.simple_spinner_item);
		    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Spinner spinner = (Spinner)view.findViewById(R.id.spinner1);
			
			
			spinner.setAdapter(adapter );
			spinner.setSelection(adapter.getPosition((CharSequence)cmd));
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
					Log.i("Sel","" + adapter.getItem(arg2).toString());
					ContentValues values = new ContentValues();
				    values.put(IntentDatabaseSQLHelper.INTENT2START,adapter.getItem(arg2).toString());

				    int affected=db.update(IntentDatabaseSQLHelper.TABLE, values,IntentDatabaseSQLHelper.PACKAGE+" = '"+pkg+"' AND "
				    		+IntentDatabaseSQLHelper.INTENT2START+" != '"+adapter.getItem(arg2).toString()+"'",null);
				    
				    if (affected>0) {
				    	PackagesCursorAdapter.this.getCursor().requery();
				    	adapter.notifyDataSetChanged();
				    }
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
				
			});
		}
		

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.config_row, parent, false);
			bindView(v, context, cursor);
			return v;
		}
		
	}
}