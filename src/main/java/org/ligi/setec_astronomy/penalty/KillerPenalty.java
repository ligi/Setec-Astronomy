package org.ligi.setec_astronomy.penalty;

import android.content.Context;
import android.content.Intent;

public class KillerPenalty {
	
	public static void kill(Context ctx,String pkg) {
		Intent i=new Intent(ctx,KillActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("pkg", pkg);
		ctx.startActivity(i);

	}
}
