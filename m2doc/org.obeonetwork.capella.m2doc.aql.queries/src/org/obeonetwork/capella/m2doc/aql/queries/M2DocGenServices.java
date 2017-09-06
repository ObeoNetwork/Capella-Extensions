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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.core.data.cs.InterfacePkg;
import org.polarsys.capella.core.data.information.DataPkg;

/**
 * This class contains all utility AQL templates used for M2Doc generation. The
 * goal of this class is to provides simple methods allowing to simplify the
 * M2Doc template.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 * 
 */
public class M2DocGenServices {

	/**
	 * Default constructor.
	 */
	public M2DocGenServices() {
	}

	/**
	 * Return recursively all the interface subpackages of the given interface
	 * package.
	 * 
	 * @param interfacePkg
	 *            The given interface package
	 * @return a list of all interface subpackages
	 */
	public List<InterfacePkg> getInterfaceSubPkg(InterfacePkg interfacePkg) {
		Set<InterfacePkg> packages = new HashSet<InterfacePkg>();
		EList<InterfacePkg> ownedInterfacePkgs = interfacePkg
				.getOwnedInterfacePkgs();
		for (InterfacePkg interfacePkg2 : ownedInterfacePkgs) {
			packages.add(interfacePkg2);
			List<InterfacePkg> allInterfacePackages = getInterfaceSubPkg(interfacePkg2);
			packages.addAll(allInterfacePackages);
		}
		return new ArrayList<InterfacePkg>(packages);
	}

	/**
	 * Compute the amount of contained interface subpackages. This method is
	 * only used to simplify the M2Doc template
	 * 
	 * @param interfacePkg
	 *            The given interface package
	 * @return the number of contained interface subpackages
	 */
	public int getAllSubInterfacesCount(InterfacePkg interfacePkg) {
		List<InterfacePkg> interfaceSubPkg = getInterfaceSubPkg(interfacePkg);
		int interfaceCount = 0;
		for (InterfacePkg interfacePkg2 : interfaceSubPkg) {
			interfaceCount += interfacePkg2.getOwnedInterfaces().size();
		}
		return interfaceCount;
	}

	/**
	 * Return recursively all the data subpackages of the given data package
	 * 
	 * @param dataPkg
	 *            The given data package
	 * @return a list of all data subpackages
	 */
	public List<DataPkg> getAllDataPkgs(DataPkg dataPkg) {
		Set<DataPkg> packages = new HashSet<DataPkg>();
		EList<DataPkg> ownedDataPkgs = dataPkg.getOwnedDataPkgs();
		for (DataPkg dataPkg2 : ownedDataPkgs) {
			packages.add(dataPkg2);
			List<DataPkg> allDataPackages = getAllDataPkgs(dataPkg2);
			packages.addAll(allDataPackages);
		}
		return new ArrayList<DataPkg>(packages);
	}

	/**
	 * Compute a "qualified name" of the given data package. The root of the
	 * qualified name is the upper level data package.
	 * 
	 * @param dataPkg
	 *            The given data package
	 * @return the qualified name of the data package
	 */
	public String getDataPkgQualifiedName(DataPkg dataPkg) {
		EObject eContainer = dataPkg.eContainer();
		String name = "";
		if (eContainer instanceof DataPkg) {
			String containerName = getDataPkgQualifiedName((DataPkg) eContainer);
			name = containerName + ".";
		}
		return name += dataPkg.getName();
	}

}
