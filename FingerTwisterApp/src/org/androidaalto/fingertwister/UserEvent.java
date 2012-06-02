package org.androidaalto.fingertwister;

import java.util.EventObject;

public class UserEvent extends EventObject {
	private boolean success;
	
	public UserEvent(Object source, boolean success)	{
		super(source);
		this.success = success;
	}
	
	public boolean getSuccess()	{
		return this.success;
	}
}
