package com.cs117.connection;

public interface ActionResolver {
	public void showShortToast(CharSequence toastMessage);
	public void showLongToast(CharSequence toastMessage);
	public void sendCoordinates(int prevX, int prevY, int newX, int newY);
}
