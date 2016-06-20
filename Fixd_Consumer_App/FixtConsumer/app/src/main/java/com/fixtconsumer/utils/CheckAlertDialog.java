package com.fixtconsumer.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class CheckAlertDialog {
	// Dialog with Message with one button"
	public void showcheckAlert(final Activity context,final String title, final String message) {
		// define alert...
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		//set title
		dialog.setTitle(title);
		// set message...
		dialog.setMessage(message);
		// set button status..onclick
		dialog.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

					dialog.dismiss();

			}
		});
		AlertDialog alert = dialog.create();
		alert.setCanceledOnTouchOutside(false);
		alert.setCancelable(false);
		alert.show();
	}

}
