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

import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;
import org.obeonetwork.m2doc.sirius.tests.AbstractTemplatesTestSuite;

public class IFETemplateTests extends AbstractTemplatesTestSuite {

	/**
	 * Constructor.
	 * 
	 * @param testFolder the test folder path
	 * @throws Exception if the tested template can't be read
	 */
	public IFETemplateTests(String testFolder) throws Exception {
		super(testFolder);
	}

	/**
	 * Gets the {@link Collection} of test folders.
	 * 
	 * @return the {@link Collection} of test folders
	 */
	@Parameters(name = "{0}")
	public static Collection<Object[]> retrieveTestFolders() {
		return retrieveTestFolders("resources/IFE");
	}

}
