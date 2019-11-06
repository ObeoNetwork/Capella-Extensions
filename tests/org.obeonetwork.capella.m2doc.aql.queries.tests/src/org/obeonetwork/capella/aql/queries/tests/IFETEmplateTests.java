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
import org.obeonetwork.m2doc.tests.TestMemoryURIHandler;

public class IFETEmplateTests extends AbstractTemplatesTestSuite {

	private static final Map<String, String> generatedToZipped = initGeneratedToZipped();

	/**
	 * The buffer size.
	 */
	private static final int BUFFER_SIZE = 1024 * 8;

	private final URI templateURI;

	private final URI generatedURI;

	private final String zippedTemplateName;

	private final String zippedGeneratedName;

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
		templateURI = getTemplateURI(new File(testFolder));
		generatedURI = getExpectedGeneratedURI(new File(testFolder));
		zippedTemplateName = generatedToZipped.get(templateURI.lastSegment());
		zippedGeneratedName = generatedToZipped.get(generatedURI.lastSegment());
	}

	/**
	 * Initializes the mapping from generated to zipped document names.
	 * 
	 * @return the mapping from generated to zipped document names
	 */
	private static Map<String, String> initGeneratedToZipped() {
		final Map<String, String> res = new HashMap<String, String>();

		res.put("LA-Complete-expected-generation.docx", "generated/LA_Complete.docx");
		res.put("LA-Complete-template.docx", "template/Template LA Complete.docx");

		res.put("SA-Complete-expected-generation.docx", "generated/SA_Complete.docx");
		res.put("SA-Complete-template.docx", "template/Template SA Complete.docx");

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
	public void zipTemplateComparison() throws IOException {
		zipComparison(zippedTemplateName, templateURI);
	}

	@Test
	public void zipGeneratedComparison() throws IOException {
		zipComparison(zippedGeneratedName, generatedURI);
	}

	protected void zipComparison(String entryName, URI expectedURI) throws IOException {
		try (final ZipFile zipFile = new ZipFile("../../plugins/org.obeonetwork.capella.m2doc.aql.queries/zips/m2docife.zip");) {
			final ZipEntry templateEntry = zipFile.getEntry(entryName);
			final InputStream zippedTemplateInputStream = zipFile.getInputStream(templateEntry);
			final TestMemoryURIHandler handler = new TestMemoryURIHandler();
			try {
				final URI tempURI = URI.createURI(TestMemoryURIHandler.PROTOCOL +"://" + entryName);

				// Copy zippedTemplateInputStream to the given URI
				URIConverter.INSTANCE.getURIHandlers().add(0, handler);
				final OutputStream outputStream = URIConverter.INSTANCE.createOutputStream(tempURI);
				byte[] buffer = new byte[BUFFER_SIZE];
				int size = zippedTemplateInputStream.read(buffer, 0, buffer.length);
				while (size != -1) {
					outputStream.write(buffer, 0, size);
					size = zippedTemplateInputStream.read(buffer, 0, buffer.length);
				}
				outputStream.flush();

				M2DocTestUtils.assertDocx(expectedURI, tempURI);
			} finally {
				handler.clear();
				URIConverter.INSTANCE.getURIHandlers().remove(handler);
			}
		}
	}

}
