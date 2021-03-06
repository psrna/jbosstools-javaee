/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.cdi.seam.faces.core;

import org.jboss.tools.common.log.BaseUIPlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class SeamFacesCorePlugin extends BaseUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.cdi.seam.faces.core"; //$NON-NLS-1$

	// The shared instance
	private static SeamFacesCorePlugin plugin;

	/**
	 * The constructor
	 */
	public SeamFacesCorePlugin() {
		plugin = this;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static SeamFacesCorePlugin getDefault() {
		return plugin;
	}
}