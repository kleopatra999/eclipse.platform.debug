/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.debug.internal.ui.views.variables.details;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * Interface for UI elements that contain detail panes.  Provides access to
 * information about the current detail pane and allows the container to be
 * informed of changes.
 *
 * @since 3.3
 * @see AvailableDetailPanesAction
 * @see DetailPaneProxy
 */
public interface IDetailPaneContainer {

	/**
	 * Returns the string ID of the detail pane currently being displayed.
	 *
	 * @return the ID of the current detail pane
	 */
	public String getCurrentPaneID();

	/**
	 * Returns the selection to be displayed in the detail pane.
	 *
	 * @return the selection to be displayed in the detail pane.
	 */
	public IStructuredSelection getCurrentSelection();

	/**
	 * Returns the composite that detail panes will be added to.
	 *
	 * @return the composite that detail panes will be added to
	 */
	public Composite getParentComposite();

	/**
	 * Returns the workbench part site that the detail pane is in or <code>null</code>
	 * if the detail pane is not in a workbench part site.
	 *
	 * @return the workbench part site the detail pane is in or <code>null</code>
	 */
	public IWorkbenchPartSite getWorkbenchPartSite();

	/**
	 * Refreshes the current detail pane with the current selection.
	 */
	public void refreshDetailPaneContents();

	/**
	 * Informs the container that the type of detail pane being used has changed.
	 *
	 * @param newPaneID ID of the new detail pane
	 */
	public void paneChanged(String newPaneID);

}
