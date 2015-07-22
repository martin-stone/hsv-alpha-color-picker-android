package com.rarepebble.colorpicker;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

class ObservableColor {

	public interface Observer {
		void updateColor(ObservableColor observableColor);
	}

	// Store as HSV & A, otherwise round-trip to int causes color drift.
	private float[] hsv = {0, 0, 0};
	private int alpha;
	private List<Observer> observers = new ArrayList<Observer>();

	ObservableColor(int color) {
		Color.colorToHSV(color, hsv);
		alpha = Color.alpha(color);
	}

	public int getColor() {
		return Color.HSVToColor(alpha, hsv);
	}

	public float getHue() {
		return hsv[0];
	}

	public float getSat() {
		return hsv[1];
	}

	public float getValue() {
		return hsv[2];
	}

	public int getAlpha() {
		return alpha;
	}

	public float getLightness() {
		return getLightnessWithValue(hsv[2]);
	}

	public float getLightnessWithValue(float value) {
		float[] hsV = {hsv[0], hsv[1], value};
		final int color = Color.HSVToColor(hsV);
		return (Color.red(color) * 0.2126f + Color.green(color) * 0.7152f + Color.blue(color) * 0.0722f)/0xff;
	}

	void addObserver(Observer observer) {
		observers.add(observer);
	}

	public void updateHueSat(float hue, float sat, Observer sender) {
		hsv[0] = hue;
		hsv[1] = sat;
		notifyOtherObservers(sender);
	}

	public void updateValue(float value, Observer sender) {
		hsv[2] = value;
		notifyOtherObservers(sender);
	}

	public void updateAlpha(int alpha, Observer sender) {
		this.alpha = alpha;
		notifyOtherObservers(sender);
	}

	public void updateColor(int color, Observer sender) {
		Color.colorToHSV(color, hsv);
		alpha = Color.alpha(color);
		notifyOtherObservers(sender);
	}

	public void notifyOtherObservers(Observer sender) {
		for (Observer observer : observers) {
			if (observer != sender) {
				observer.updateColor(this);
			}
		}
	}
}
