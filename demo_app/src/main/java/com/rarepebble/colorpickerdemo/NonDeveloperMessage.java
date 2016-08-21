package com.rarepebble.colorpickerdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * This is only here for the Play Store demo.
 *
 * To avoid confused reviews by non-developers, we'll show an explanatory message on phones that
 * don't have developer mode enabled.
 */

public class NonDeveloperMessage {
	public static void maybeShow(Context context) {
		if (!userIsDeveloper(context)) {
			showMessage(context);
		}
	}

	private static boolean userIsDeveloper(Context context) {
		if (BuildConfig.DEBUG || Build.VERSION.SDK_INT < 17) {
			// Running a debug build, or API not supported: we'll assume user is a developer.
			return true;
		}
		else {
			// Running a release APK on SKK 17+. Check developer mode:
			int developerModeEnabled = Settings.Secure.getInt(
					context.getContentResolver(),
					Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
					0);
			return developerModeEnabled != 0;
		}
	}

	private static void showMessage(Context context) {
		final Spanned message = Html.fromHtml(MESSAGE);
		final AlertDialog d = new AlertDialog.Builder(context)
				.setTitle(TITLE)
				.setMessage(message)
				.setPositiveButton(android.R.string.ok, null)
				.create();
		d.show();
		((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
	}

	private static final String TITLE = "What is this app?";
	private static final String MESSAGE ="<p>" +
			"If you are not an app developer, this app will do nothing useful for you, " +
			"so please don't leave ratings or reviews. " +
			"</p><p>" +
			"For app developers, it demonstrates a color preference and color picker library. " +
			"For information on how to incorporate it into your app, or to report a bug, visit " +
			"<a href='https://github.com/martin-stone/hsv-alpha-color-picker-android'>the project GitHub page</a>." +
			"</p>";
}
