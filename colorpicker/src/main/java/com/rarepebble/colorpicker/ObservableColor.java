package com.rarepebble.colorpicker;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

class ObservableColor {

	public interface Observer {
		void updateColor(ObservableColor observableColor);
	}

	private int color;
	private float[] hsv = {0, 0, 0};
	private List<Observer> observers = new ArrayList<Observer>();

	ObservableColor(int color) {
		this.color = color;
	}

	public int getColor() {
		return color;
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
		return Color.alpha(color);
	}

	void addObserver(Observer observer) {
		observers.add(observer);
	}

	public void updateHueSat(float hue, float sat, Observer sender) {
		hsv[0] = hue;
		hsv[1] = sat;
		color = Color.HSVToColor(getAlpha(), hsv);
		notifyOtherObservers(color, sender);
	}

	public void updateValue(float value, Observer sender) {
		hsv[2] = value;
		color = Color.HSVToColor(getAlpha(), hsv);
		notifyOtherObservers(color, sender);
	}

	public void updateAlpha(int alpha, Observer sender) {
		color = Color.HSVToColor(alpha, hsv);
		notifyOtherObservers(color, sender);
	}

	public void notifyOtherObservers(int color, Observer sender) {
		this.color = color;
		Color.colorToHSV(color, this.hsv);
		for (Observer observer : observers) {
			if (observer != sender) {
				observer.updateColor(this);
			}
		}
	}
}
