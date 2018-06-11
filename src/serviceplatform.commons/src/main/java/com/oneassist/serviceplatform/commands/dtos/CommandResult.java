package com.oneassist.serviceplatform.commands.dtos;

public class CommandResult<T> {
	private boolean canExecute;
	
	private T data;
	
	public boolean getCanExecute() {
		return canExecute;
	}

	public void setCanExecute(boolean canExecute) {
		this.canExecute = canExecute;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
