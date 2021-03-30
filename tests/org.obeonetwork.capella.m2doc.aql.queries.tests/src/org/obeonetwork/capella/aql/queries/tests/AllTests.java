/*******************************************************************************
 *  Copyright (c) 2017 Obeo. 
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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Aggregates tests for the org.obeonetwork.capella.aql.queries.tests plug-in.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
@RunWith(Suite.class)
@SuiteClasses(value = { CapellaTokenTest.class, IFETEmplateTests.class, SemanticBrowserServicesTests.class, })
public class AllTests {

}
