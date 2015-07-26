/*
 * Copyright (C) 2015 Martin Stone
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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class ColorPreference extends DialogPreference {
	private final String selectNoneButtonText;
	private final Integer defaultColor;
	private final String noneSelectedSummaryText;
	private View thumbnail;

	public ColorPreference(Context context) {
		this(context, null);
	}
	public ColorPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (attrs != null) {
			TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ColorPreference, 0, 0);
			selectNoneButtonText = a.getString(R.styleable.ColorPreference_selectNoneButtonText);
			noneSelectedSummaryText = a.getString(R.styleable.ColorPreference_noneSelectedSummaryText);
			defaultColor = a.hasValue(R.styleable.ColorPreference_defaultColor)
					? a.getColor(R.styleable.ColorPreference_defaultColor, Color.GRAY)
					: null;
		}
		else {
			selectNoneButtonText = null;
			defaultColor = null;
			noneSelectedSummaryText = null;
		}
	}

	@Override
	protected void onBindView(View view) {
		thumbnail = addThumbnail(view);
		showColor(getPersistedIntDefaultOrNull());
		// Only call after showColor sets any summary text:
		super.onBindView(view);
	}

	private View addThumbnail(View view) {
		LinearLayout widgetFrameView = ((LinearLayout)view.findViewById(android.R.id.widget_frame));
		widgetFrameView.setVisibility(View.VISIBLE);
		widgetFrameView.removeAllViews();
		LayoutInflater.from(getContext()).inflate(
				isEnabled()
					? R.layout.color_preference_thumbnail
					: R.layout.color_preference_thumbnail_disabled,
				widgetFrameView);
		return widgetFrameView.findViewById(R.id.thumbnail);
	}

	private Integer getPersistedIntDefaultOrNull() {
		return getSharedPreferences().contains(getKey())
				? new Integer(getPersistedInt(Color.GRAY))
				: defaultColor;
	}

	void showColor(Integer color) {
		if (thumbnail != null) {
			thumbnail.setVisibility(color == null ? View.GONE : View.VISIBLE);
			thumbnail.findViewById(R.id.colorPreview).setBackgroundColor(color == null ? 0 : color);
		}
		if (noneSelectedSummaryText != null) {
			setSummary(color == null ? noneSelectedSummaryText : null);
		}
	}

	@Override
	protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
		super.onPrepareDialogBuilder(builder);
		final ColorPickerView picker = new ColorPickerView(getContext());
		picker.setColor(getPersistedInt(defaultColor == null ? Color.GRAY : defaultColor));
		builder
				.setTitle(null)
				.setView(picker)
				.setPositiveButton(getPositiveButtonText(), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						final int color = picker.getColor();
						persistInt(color);
						showColor(color);
					}
				});
		if (selectNoneButtonText != null) {
			builder.setNeutralButton(selectNoneButtonText, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					removeSetting();
					showColor(null);
				}
			});
		}
	}

	private void removeSetting() {
		getSharedPreferences()
				.edit()
				.remove(getKey())
				.commit();
	}
}
