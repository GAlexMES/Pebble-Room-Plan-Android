package de.brennecke.timetableroomplan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.brennecke.timetableroomplan.model.Exchange;

/**
 * Created by Alexander on 29.10.2015.
 */
public class UpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Exchange.getInstance().updateClock(context);
    }
}
