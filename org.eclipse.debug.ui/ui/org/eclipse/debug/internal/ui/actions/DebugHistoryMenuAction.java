/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.debug.internal.ui.actions;


import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.actions.AbstractLaunchHistoryAction;

/**
 * Debug history menu in the top-level "Run" menu.
 */
public class DebugHistoryMenuAction extends AbstractLaunchHistoryAction {

	public DebugHistoryMenuAction() {
		super(IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
	}

}
