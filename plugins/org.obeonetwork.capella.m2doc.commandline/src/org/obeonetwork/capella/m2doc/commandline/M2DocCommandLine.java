/*******************************************************************************
 * Copyright (c) 2020 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.obeonetwork.capella.m2doc.commandline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.obeonetwork.m2doc.genconf.GenconfUtils;
import org.obeonetwork.m2doc.genconf.Generation;
import org.obeonetwork.m2doc.generator.DocumentGenerationException;
import org.obeonetwork.m2doc.ide.M2DocPlugin;
import org.obeonetwork.m2doc.launcher.internal.CLIUtils;
import org.obeonetwork.m2doc.parser.DocumentParserException;
import org.polarsys.capella.core.commandline.core.CommandLineArgumentHelper;
import org.polarsys.capella.core.commandline.core.CommandLineException;
import org.polarsys.capella.core.commandline.core.DefaultCommandLine;

/**
 * Command line to launch the
 */
public class M2DocCommandLine extends DefaultCommandLine {

	private String[] genconfs = new String[1];

	/**
	* 
	*/
	public M2DocCommandLine() {
		super();
	}

	@Override
	public void printHelp() {
		System.out.println("Capella M2Doc Command Line"); //$NON-NLS-1$
		super.printHelp();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkArgs(IApplicationContext context_p) throws CommandLineException {
		super.checkArgs(context_p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean execute(IApplicationContext context_p) {

		startFakeWorkbench();

		genconfs[0] = "platform:/resource/" + CommandLineArgumentHelper.getInstance().getFilePath();
		Collection<URI> genconfsURIs = validateArguments();

		boolean somethingWentWrong = false;

		System.out.println(CLIUtils.getDecorator()
				.purple(" __          __  _                            _          __  __ ___     _            \n"
						+ " \\ \\        / / | |                          | |        |  \\/  |__ \\   | |           \n"
						+ "  \\ \\  /\\  / /__| | ___ ___  _ __ ___   ___  | |_ ___   | \\  / |  ) |__| | ___   ___ \n"
						+ "   \\ \\/  \\/ / _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\ | __/ _ \\  | |\\/| | / // _` |/ _ \\ / __|\n"
						+ "    \\  /\\  /  __/ | (_| (_) | | | | | |  __/ | || (_) | | |  | |/ /| (_| | (_) | (__ \n"
						+ "     \\/  \\/ \\___|_|\\___\\___/|_| |_| |_|\\___|  \\__\\___/  |_|  |_|____\\__,_|\\___/ \\___|"));

		System.out.println(
				CLIUtils.getDecorator().yellow("The command-line launcher to generate .docx from your models."));

		System.out.println(CLIUtils.RESET);

		Collection<Generation> loadedGenConfs = new ArrayList<Generation>();

		ResourceSet s = new ResourceSetImpl();
		for (URI uri : genconfsURIs) {
			if (s.getURIConverter().exists(uri, Collections.EMPTY_MAP)) {
				try {
					Resource r = s.getResource(uri, true);
					r.load(Collections.EMPTY_MAP);
					for (EObject eObj : r.getContents()) {
						if (eObj instanceof Generation) {
							loadedGenConfs.add((Generation) eObj);
						}
					}
				} catch (IOException e) {
					somethingWentWrong = true;
					M2DocCommandLinePlugin.getPlugin().log(new Status(IStatus.ERROR, M2DocCommandLinePlugin.PLUGIN_ID,
							"Error loading genconf: '" + uri.toString() + "' : " + e.getMessage(), e));
					// CHECKSTYLE:OFF we want to report any error
				} catch (RuntimeException e) {
					// CHECKSTYLE:ON
					somethingWentWrong = true;
					M2DocCommandLinePlugin.getPlugin().log(new Status(IStatus.ERROR, M2DocCommandLinePlugin.PLUGIN_ID,
							"Error loading genconf: '" + uri.toString() + "' : " + e.getMessage(), e));
				}
			} else {
				M2DocCommandLinePlugin.getPlugin().log(new Status(IStatus.ERROR, M2DocCommandLinePlugin.PLUGIN_ID,
						"Error loading genconf: '" + uri.toString() + "' : does not exist or is not accessible."));
			}
		}

		final Monitor monitor = new CLIUtils.ColoredPrinting(System.out);

		monitor.beginTask("Generating .docx documents", loadedGenConfs.size());
		for (Generation generation : loadedGenConfs) {
			try {

				System.out.println("Input: " + generation.eResource().getURI());
				List<URI> generated = GenconfUtils.generate(generation, M2DocPlugin.getClassProvider(), monitor);
				for (URI uri : generated) {
					System.out.println("Output: " + uri.toString());
				}
				monitor.worked(1);

			} catch (DocumentGenerationException e) {
				M2DocCommandLinePlugin.getPlugin()
						.log(new Status(IStatus.ERROR, M2DocCommandLinePlugin.PLUGIN_ID, "Error launching genconf: '"
								+ generation.eResource().getURI().toString() + "' : " + e.getMessage(), e));
			} catch (IOException e) {
				M2DocCommandLinePlugin.getPlugin()
						.log(new Status(IStatus.ERROR, M2DocCommandLinePlugin.PLUGIN_ID, "Error launching genconf: '"
								+ generation.eResource().getURI().toString() + "' : " + e.getMessage(), e));
			} catch (DocumentParserException e) {
				M2DocCommandLinePlugin.getPlugin()
						.log(new Status(IStatus.ERROR, M2DocCommandLinePlugin.PLUGIN_ID, "Error launching genconf: '"
								+ generation.eResource().getURI().toString() + "' : " + e.getMessage(), e));
			}
		}

		return !somethingWentWrong;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws CommandLineException
	 */
	@Override
	public void prepare(IApplicationContext context_p) throws CommandLineException {
		super.prepare(context_p);
	}

	public static void startFakeWorkbench() {
		Display display = PlatformUI.createDisplay();
		PlatformUI.createAndRunWorkbench(display, new WorkbenchAdvisor() {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public boolean openWindows() {
				return false;
			}

			@Override
			public String getInitialWindowPerspectiveId() {
				return null;
			}

		});
	}

	/**
	 * Validate arguments which are mandatory only in some circumstances.
	 */

	private Collection<URI> validateArguments() {
		Collection<URI> result = new ArrayList<URI>();

		if (genconfs == null || genconfs.length == 0) {
			// throw new CmdLineException(parser, "You must specify genconfs models.");
		}
		for (String modelPath : genconfs) {

			URI rawURI = null;
			try {
				rawURI = URI.createURI(modelPath, true);
			} catch (IllegalArgumentException e) {

				// the passed uri is not in the URI format and should be
				// considered as a direct file denotation.

			}

			if (rawURI != null && !rawURI.hasAbsolutePath()) {
				rawURI = URI.createFileURI(modelPath);
			}
			result.add(rawURI);
		}

		return result;

	}

}
