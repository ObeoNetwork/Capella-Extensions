/*******************************************************************************
 *  Copyright (c) 2021 Obeo. 
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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.junit.Test;
import org.obeonetwork.capella.m2doc.aql.queries.SemanticBrowserServices;

/**
 * Tests {@link SemanticBrowserServices}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 *
 */
public class SemanticBrowserServicesTests {

	/**
	 * The Sirius {@link Session} {@link URI}.
	 */
	private static final URI SESSION_URI = URI
			.createFileURI(new File("resources/IFE/LA-Complete/LA-Complete.aird").getAbsolutePath());

	/**
	 * The IFE model {@link URI}.
	 */
	private static final URI IFE_URI = URI.createFileURI(
			new File("resources/IFE/LA-Complete/In-Flight Entertainment System.capella").getAbsolutePath());

	/**
	 * Mapping from {@link Class} of the parameter to the {@link List} of compatible
	 * {@link Method}.
	 */
	private Map<Class<?>, List<Method>> typeToMethods = new HashMap<>();

	/**
	 * This test only brutforce each service on each compatible element of the IFE
	 * model looking for exceptions.
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@Test
	public void servicesTests() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Session session = SessionManager.INSTANCE.getSession(SESSION_URI, new NullProgressMonitor());
		final ResourceSet resourceSet = session.getTransactionalEditingDomain().getResourceSet();
		final Resource ifeModel = resourceSet.getResource(IFE_URI, true);

		final List<Method> methods = new ArrayList<Method>();
		for (Method method : SemanticBrowserServices.class.getDeclaredMethods()) {
			if (method.isAccessible() && method.getParameterCount() == 1) {
				methods.add(method);
			}
		}

		final SemanticBrowserServices serviceInstance = new SemanticBrowserServices();
		for (EObject root : ifeModel.getContents()) {
			 callComplatibleMethods(serviceInstance, methods, root);
			final Iterator<EObject> it = root.eAllContents();
			while (it.hasNext()) {
				 callComplatibleMethods(serviceInstance, methods, it.next());
			}
		}
	}

	/**
	 * Calls compatible {@link Method} for the given parameter.
	 * 
	 * @param serviceInstance
	 *            the instance of {@link SemanticBrowserServices}
	 * 
	 * @param methods
	 *            the {@link List} of possible {@link Method} to call
	 * @param parameter
	 *            the parameter
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void callComplatibleMethods(SemanticBrowserServices serviceInstance, List<Method> methods, Object parameter)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final List<Method> knownMethods = typeToMethods.get(parameter.getClass());
		if (knownMethods != null) {
			for (Method method : new ArrayList<Method>(knownMethods)) {
				method.invoke(serviceInstance, parameter);
			}
		} else {
			final List<Method> cachedMethods = new ArrayList<Method>();
			for (Method method : methods) {
				if (method.getParameters()[0].getType().isInstance(parameter)) {
					cachedMethods.add(method);
				}
			}
			typeToMethods.put(parameter.getClass(), cachedMethods);
			callComplatibleMethods(serviceInstance, methods, parameter);
		}
	}

}
