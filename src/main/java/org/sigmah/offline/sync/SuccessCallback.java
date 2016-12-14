package org.sigmah.offline.sync;

/*
 * #%L
 * Sigmah
 * %%
 * Copyright (C) 2010 - 2016 URD
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Defines a callback handling only the success case.
 * The failure case can be delegated to another callback.
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public abstract class SuccessCallback<T> implements AsyncCallback<T> {
	
	private final AsyncCallback<?> failureCallback;

	public SuccessCallback() {
		this.failureCallback = null;
	}

	public SuccessCallback(AsyncCallback<?> failureCallback) {
		this.failureCallback = failureCallback;
	}
	
	@Override
	public void onFailure(Throwable caught) {
		if (failureCallback != null) {
			failureCallback.onFailure(caught);
		}
	}
	
}
