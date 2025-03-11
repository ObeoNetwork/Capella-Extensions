/*******************************************************************************
 *  Copyright (c) 2025 Obeo. 
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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.junit.Test;
import org.obeonetwork.capella.m2doc.aql.queries.M2DocGenServices;
import org.polarsys.capella.core.data.capellamodeller.ModelRoot;
import org.polarsys.capella.core.data.capellamodeller.Project;
import org.polarsys.capella.core.data.capellamodeller.SystemEngineering;

public class M2DocGenServicesTests {

	/**
	 * The {@link M2DocGenServices} instance.
	 */
	private final M2DocGenServices services = new M2DocGenServices();

	private final EObject ifePhysicalSystem;

	public M2DocGenServicesTests() {
		final Session session = SessionManager.INSTANCE.getSession(SemanticBrowserServicesTests.SESSION_URI,
				new NullProgressMonitor());
		final ResourceSet resourceSet = session.getTransactionalEditingDomain().getResourceSet();
		final Resource ifeModel = resourceSet.getResource(SemanticBrowserServicesTests.IFE_URI, true);

		EObject foundIFEPhysicalSystem = null;
		found: for (EObject content : ifeModel.getContents()) {
			if (content instanceof Project) {
				for (ModelRoot root : ((Project) content).getOwnedModelRoots()) {
					if (root instanceof SystemEngineering) {
						foundIFEPhysicalSystem = ((SystemEngineering) root).getContainedPhysicalArchitectures().get(0)
								.getOwnedPhysicalComponentPkg().getOwnedPhysicalComponents().get(0);
						break found;
					}
				}
			}
		}
		ifePhysicalSystem = foundIFEPhysicalSystem;
	}

	@Test
	public void availableSBQueriesNull() {
		final List<String> result = services.availableSBQueries(null);

		assertEquals(0, result.size());
	}

	@Test
	public void getSBQueriesNullNull() {
		final List<EObject> result = services.getSBQuery(null, null);

		assertEquals(0, result.size());
	}

	@Test
	public void availableSBQueries() {

		final List<String> result = services.availableSBQueries(ifePhysicalSystem);

		assertEquals(48, result.size());
		assertEquals("All Related Diagrams", result.get(0));
		assertEquals("All Related Tables", result.get(1));
		assertEquals("Allocated Physical Functions", result.get(2));
		assertEquals("Applied Property Value Groups", result.get(3));
		assertEquals("Applied Property Values", result.get(4));
		assertEquals("Communication Link", result.get(5));
		assertEquals("Component Breakdown", result.get(6));
		assertEquals("Component Ports", result.get(7));
		assertEquals("Constraining Elements", result.get(8));
		assertEquals("Deployed Physical Components", result.get(9));
		assertEquals("Deploying Physical Components", result.get(10));
		assertEquals("Element of Interest for Diagram", result.get(11));
		assertEquals("Exchange Context", result.get(12));
		assertEquals("Exchange Item Elements", result.get(13));
		assertEquals("Expression", result.get(14));
		assertEquals("Generalized Components", result.get(15));
		assertEquals("Generalized Elements", result.get(16));
		assertEquals("Generalizing Components", result.get(17));
		assertEquals("Generalizing Elements", result.get(18));
		assertEquals("Guard", result.get(19));
		assertEquals("Implemented Interfaces", result.get(20));
		assertEquals("Incoming Component Exchanges", result.get(21));
		assertEquals("Incoming Delegations", result.get(22));
		assertEquals("Incoming Generic Traces", result.get(23));
		assertEquals("Inherited of Typing Elements", result.get(24));
		assertEquals("Internal Incoming Component Exchanges (computed)", result.get(25));
		assertEquals("Internal Outgoing Component Exchanges (computed)", result.get(26));
		assertEquals("Internal Physical Links (computed)", result.get(27));
		assertEquals("Involving Capability Realizations", result.get(28));
		assertEquals("Outgoing Component Exchanges", result.get(29));
		assertEquals("Outgoing Delegations", result.get(30));
		assertEquals("Outgoing Generic Traces", result.get(31));
		assertEquals("Owned Components", result.get(32));
		assertEquals("Parent", result.get(33));
		assertEquals("Physical Links", result.get(34));
		assertEquals("Post-condition", result.get(35));
		assertEquals("Pre-condition", result.get(36));
		assertEquals("Provided Interfaces", result.get(37));
		assertEquals("REC", result.get(38));
		assertEquals("REC Source Element", result.get(39));
		assertEquals("RPL", result.get(40));
		assertEquals("Realized Logical Component", result.get(41));
		assertEquals("Referencing Components", result.get(42));
		assertEquals("Representing Parts", result.get(43));
		assertEquals("Required Interfaces", result.get(44));
		assertEquals("Scenarios", result.get(45));
		assertEquals("Typing Elements", result.get(46));
		assertEquals("Used Interfaces", result.get(47));
	}

	@Test
	public void getSBQueries() {
		final List<EObject> result = services.getSBQuery(ifePhysicalSystem, "Realized Logical Component");

		assertEquals(1, result.size());
	}

}
