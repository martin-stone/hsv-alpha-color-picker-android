package com.rarepebble.colorpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;

public class ColorPickerView extends FrameLayout {

	private ObservableColor observableColor = new ObservableColor(0);
	private final SwatchView swatchView;

	public ColorPickerView(Context context) {
		this(context, null);
	}

	public ColorPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.picker, this);

		swatchView = (SwatchView)findViewById(R.id.swatchView);
		swatchView.observeColor(observableColor);

		HueSatView hueSatView = (HueSatView)findViewById(R.id.hueSatView);
		hueSatView.observeColor(observableColor);

		ValueView valueView = (ValueView)findViewById(R.id.valueView);
		valueView.observeColor(observableColor);

		AlphaView alphaView = (AlphaView)findViewById(R.id.alphaView);
		alphaView.observeColor(observableColor);

		EditText hexEdit = (EditText)findViewById(R.id.hexEdit);
		HexEdit.setUpListeners(hexEdit, observableColor);
	}

	public int getColor() {
		return observableColor.getColor();
	}

	public void setColor(int color) {
		swatchView.setOldColor(color);
		observableColor.updateColor(color, null);
	}
}
