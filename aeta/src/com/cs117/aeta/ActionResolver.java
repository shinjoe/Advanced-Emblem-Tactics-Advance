package com.cs117.aeta;

public interface ActionResolver<T,U,X> {
	public void showShortToast(CharSequence toastMessage);
	public void showLongToast(CharSequence toastMessage);
}
