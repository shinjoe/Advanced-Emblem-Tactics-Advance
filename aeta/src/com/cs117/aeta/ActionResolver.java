package com.cs117.aeta;

public interface ActionResolver {
	public void showShortToast(CharSequence toastMessage);
	public void showLongToast(CharSequence toastMessage);
	public void sendCoordinates(CharSequence coordinates);
}
