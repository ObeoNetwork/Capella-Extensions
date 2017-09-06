/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.obeonetwork.capella.m2doc.aql.queries;

import org.obeonetwork.m2doc.services.IServiceHolder;

/**
 * M2Doc {@link IServiceHolder} for {@link ExtendedImageService}.
 * 
 * @author <a href="mailto:yann.mortier@obeo.fr">Yann Mortier</a>
 */
public class ExtendedImageServiceHolder implements IServiceHolder {

	@Override
	public Class<?> getServiceClass() {
		return ExtendedImageService.class;
	}

}
