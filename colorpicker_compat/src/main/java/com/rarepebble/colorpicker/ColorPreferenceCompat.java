/*
 * Copyright (C) 2015 Martin Stone
 * Copyright (C) 2017 Laszlo Kustra
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rarepebble.colorpicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ColorPreferenceCompat extends DialogPreference {

	final String selectNoneButtonText;
	Integer defaultColor;
	final boolean showAlpha;
	final boolean showHex;
	final boolean showPreview;

	final CharSequence originalSummaryText;
	final String noneSelectedSummaryText;

	public ColorPreferenceCompat(Context context) {
		this(context, null);
	}

	public ColorPreferenceCompat(Context context, AttributeSet attrs) {
		super(context, attrs);
		originalSummaryText = super.getSummary();

		if (attrs != null) {
			TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ColorPicker, 0, 0);
			selectNoneButtonText = a.getString(R.styleable.ColorPicker_colorpicker_selectNoneButtonText);
			noneSelectedSummaryText = a.getString(R.styleable.ColorPicker_colorpicker_noneSelectedSummaryText);
			showAlpha = a.getBoolean(R.styleable.ColorPicker_colorpicker_showAlpha, true);
			showHex = a.getBoolean(R.styleable.ColorPicker_colorpicker_showHex, true);
			showPreview = a.getBoolean(R.styleable.ColorPicker_colorpicker_showPreview, true);
		}
		else {
			selectNoneButtonText = null;
			noneSelectedSummaryText = null;
			showAlpha = true;
			showHex = true;
			showPreview = true;
		}
	}

	@Override
	public void onBindViewHolder(PreferenceViewHolder holder) {
		super.onBindViewHolder(holder);
		View thumbnail = addThumbnail(holder);

		Integer color = getPersistedIntDefaultOrNull();
		Integer thumbColor = color == null ? defaultColor : color;
		if (thumbnail != null) {
			thumbnail.setVisibility(thumbColor == null ? View.GONE : View.VISIBLE);
			thumbnail.findViewById(R.id.colorPreview).setBackgroundColor(thumbColor == null ? 0 : thumbColor);
		}

		if (noneSelectedSummaryText != null) {
			TextView summary = (TextView) holder.findViewById(android.R.id.summary);
			summary.setText(thumbColor == null ? noneSelectedSummaryText : originalSummaryText);
		}
	}

	private View addThumbnail(PreferenceViewHolder holder) {
		LinearLayout widgetFrameView = ((LinearLayout)holder.findViewById(android.R.id.widget_frame));
		widgetFrameView.setVisibility(View.VISIBLE);
		widgetFrameView.removeAllViews();
		LayoutInflater.from(getContext()).inflate(
				isEnabled()
						? R.layout.color_preference_thumbnail
						: R.layout.color_preference_thumbnail_disabled,
				widgetFrameView);
		return widgetFrameView.findViewById(R.id.thumbnail);
	}

	@Override
	protected Integer onGetDefaultValue(TypedArray a, int index) {
		if (a.peekValue(index) != null && a.peekValue(index).type == TypedValue.TYPE_STRING) {
			defaultColor = Color.parseColor(standardiseColorDigits(a.getString(index)));
		}
		else {
			defaultColor = a.getColor(index, Color.GRAY);
		}
		return defaultColor;
	}

	private static String standardiseColorDigits(String s) {
		if (s.charAt(0) == '#' && s.length() <= "#argb".length()) {
			// Convert #[a]rgb to #[aa]rrggbb
			String ss = "#";
			for (int i = 1; i < s.length(); ++i) {
				ss += s.charAt(i);
				ss += s.charAt(i);
			}
			return ss;
		}
		else {
			return s;
		}
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		setColor(restorePersistedValue ? getColor() : defaultColor);
	}

	@SuppressLint("ApplySharedPref")
	private void removeSetting() {
		if (shouldPersist()) {
			getSharedPreferences()
					.edit()
					.remove(getKey())
					.commit();
		}
	}

	public void setColor(Integer color) {
		if (color == null) {
			removeSetting();
		}
		else {
			persistInt(color);
		}

		notifyChanged();
	}

	public Integer getColor() {
		return getPersistedIntDefaultOrNull();
	}

	private Integer getPersistedIntDefaultOrNull() {
		return getSharedPreferences().contains(getKey())
				? Integer.valueOf(getPersistedInt(Color.GRAY))
				: defaultColor;
	}
}
