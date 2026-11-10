/*******************************************************************************
 *  Copyright (c) 2019, 2026 Obeo. 
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *   
 *   Contributors:
 *       Obeo - initial API and implementation
 *  
 *******************************************************************************/
package org.obeonetwork.capella.aql.queries.tests;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.map.HashedMap;
import org.eclipse.emf.ecore.EPackage;
import org.junit.Test;
import org.obeonetwork.m2doc.ide.M2DocPlugin;
import org.obeonetwork.m2doc.services.TokenRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

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
			assertNotNull(nsuri, EPackage.Registry.INSTANCE.getEPackage(nsuri));
		}
	}

	@Test
	public void services() {
		BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		Map<String, Bundle> bundleMap = new HashedMap<String, Bundle>();
		for (Bundle bundle : bundleContext.getBundles()) {
			bundleMap.put(bundle.getSymbolicName(), bundle);
		}
		for (Entry<String, List<String>> entry : TokenRegistry.INSTANCE.getServices("Capella").entrySet()) {
			final String bundleName = entry.getKey();
			for (String className : entry.getValue()) {
				try {
					Bundle bundle = bundleMap.get(bundleName);
					assertNotNull("bundle " + bundleName + " not found", bundleName);
					Class<?> cls = bundle.loadClass(className);
					assertNotNull(className + " in bundle " + bundleName + " not found", cls);
				} catch (ClassNotFoundException e) {
					System.err.println("can't load service class: " + className);
				}
			}
		}
	}
}
