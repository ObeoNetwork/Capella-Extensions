/*******************************************************************************
 *  Copyright (c) 2019 Obeo. 
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *   
 *   Contributors:
 *       Obeo - initial API and implementation
 *  
 *******************************************************************************/
package org.obeonetwork.capella.aql.queries.tests;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EPackage;
import org.junit.Test;
import org.obeonetwork.m2doc.ide.M2DocPlugin;
import org.obeonetwork.m2doc.services.TokenRegistry;
import org.obeonetwork.m2doc.util.IClassProvider;

/**
 * Capella token tests.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 *
 */
public class CapellaTokenTest {

	{
		// make sure M2DocPlugin is started
		M2DocPlugin.INSTANCE.getBaseURL();
	}

	@Test
	public void ePackages() {
		for (String nsuri : TokenRegistry.INSTANCE.getPackages("Capella")) {
			assertNotNull(EPackage.Registry.INSTANCE.getEPackage(nsuri));
		}
	}

	@Test
	public void services() {
		final IClassProvider classProvider = M2DocPlugin.getClassProvider();
		for (Entry<String, List<String>> entry : TokenRegistry.INSTANCE.getServices("Capella").entrySet()) {
			final String bundleName = entry.getKey();
			for (String className : entry.getValue()) {
				try {
					final Class<?> cls = classProvider.getClass(className, bundleName);
					assertNotNull(cls);
				} catch (ClassNotFoundException e) {
					System.err.println("can't load service class: " + className);
				}
			}
		}
	}
}
