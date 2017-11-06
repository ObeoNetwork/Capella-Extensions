package org.obeonetwork.capella.aql.queries.tests;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;
import org.obeonetwork.m2doc.parser.DocumentParserException;
import org.obeonetwork.m2doc.sirius.tests.AbstractTemplatesTestSuite;
import org.obeonetwork.m2doc.tests.M2DocTestUtils;

public class IFETEmplateTests extends AbstractTemplatesTestSuite {

	private static final Map<String, String> generatedToZipped = initGeneratedToZipped();

	/**
	 * The buffer size.
	 */
	private static final int BUFFER_SIZE = 1024 * 8;

	private final URI generatedURI;

	private final String zipperTemplateName;

	/**
	 * Constructor.
	 * 
	 * @param testFolder
	 *            the test folder path
	 * @throws IOException
	 *             if the tested template can't be read
	 * @throws DocumentParserException
	 *             if the tested template can't be parsed
	 */
	public IFETEmplateTests(String testFolder) throws IOException, DocumentParserException {
		super(testFolder);
		generatedURI = getExpectedGeneratedURI(new File(testFolder));
		zipperTemplateName = generatedToZipped.get(generatedURI.lastSegment());
	}

	/**
	 * Initializes the mapping from generated to zipped document names.
	 * 
	 * @return the mapping from generated to zipped document names
	 */
	private static Map<String, String> initGeneratedToZipped() {
		final Map<String, String> res = new HashMap<String, String>();

		res.put("LA-Complete-expected-generation.docx", "generated/LA_Complete.docx");
		res.put("SA-Complete-expected-generation.docx", "generated/SA_Complete.docx");

		return res;
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

	@Test
	public void zipComparison() throws IOException {
		try (final ZipFile zipFile = new ZipFile("../org.obeonetwork.capella.m2doc.aql.queries/zips/m2docife.zip");) {
			final ZipEntry templateEntry = zipFile.getEntry(zipperTemplateName);
			final InputStream zippedTemplateInputStream = zipFile.getInputStream(templateEntry);
			final URI tempTEmplateURI = URI.createURI("m2doctests://" + zipperTemplateName);

			// Copy zippedTemplateInputStream to the given URI
			final OutputStream outputStream = URIConverter.INSTANCE.createOutputStream(tempTEmplateURI);
			byte[] buffer = new byte[BUFFER_SIZE];
			int size = zippedTemplateInputStream.read(buffer, 0, buffer.length);
			while (size != -1) {
				outputStream.write(buffer, 0, size);
				size = zippedTemplateInputStream.read(buffer, 0, buffer.length);
			}
			outputStream.flush();

			M2DocTestUtils.assertDocx(generatedURI, tempTEmplateURI);
		}
	}

}
