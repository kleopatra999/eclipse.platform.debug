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
package org.eclipse.debug.internal.ui.preferences;


import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * A boolean field editor that provides access to this editors boolean
 * button.
 */
public class BooleanFieldEditor2 extends BooleanFieldEditor {

	private  Button fChangeControl;

	/**
	 * @see BooleanFieldEditor#BooleanFieldEditor(java.lang.String, java.lang.String, int, org.eclipse.swt.widgets.Composite)
	 */
	public BooleanFieldEditor2(
		String name,
		String labelText,
		int style,
		Composite parent) {
		super(name, labelText, style, parent);
	}

	/**
	 * @see org.eclipse.jface.preference.BooleanFieldEditor#getChangeControl(Composite)
	 */
	@Override
	public Button getChangeControl(Composite parent) {
		if (fChangeControl == null) {
			fChangeControl = super.getChangeControl(parent);
		}
		return fChangeControl;
	}


}

