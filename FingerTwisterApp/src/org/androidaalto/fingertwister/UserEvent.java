package org.androidaalto.fingertwister;

import java.util.EventObject;

public class UserEvent extends EventObject {
	
	private static final long serialVersionUID = -673160077033374452L;



	public enum UserState{Proceeding, Win, Lose};
	

	private UserState userState;


	public UserEvent(Object source, UserState userState)	{
		super(source);
		this.userState = userState;
	}
	

	
	public UserState getUserState() {
		return userState;
	}


}
