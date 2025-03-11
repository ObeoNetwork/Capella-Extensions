/*******************************************************************************
 * Copyright (c) 2017, 2025 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.obeonetwork.capella.m2doc.aql.queries;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.acceleo.annotations.api.documentation.Documentation;
import org.eclipse.acceleo.annotations.api.documentation.Example;
import org.eclipse.acceleo.annotations.api.documentation.Param;
import org.eclipse.acceleo.annotations.api.documentation.ServiceProvider;
import org.eclipse.acceleo.query.ast.Call;
import org.eclipse.acceleo.query.runtime.IReadOnlyQueryEnvironment;
import org.eclipse.acceleo.query.runtime.IService;
import org.eclipse.acceleo.query.runtime.IValidationResult;
import org.eclipse.acceleo.query.runtime.impl.AbstractServiceProvider;
import org.eclipse.acceleo.query.runtime.impl.JavaMethodService;
import org.eclipse.acceleo.query.runtime.impl.ValidationServices;
import org.eclipse.acceleo.query.services.FilterService;
import org.eclipse.acceleo.query.validation.type.EClassifierLiteralType;
import org.eclipse.acceleo.query.validation.type.EClassifierSetLiteralType;
import org.eclipse.acceleo.query.validation.type.EClassifierType;
import org.eclipse.acceleo.query.validation.type.IType;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.query.DRepresentationQuery;
import org.eclipse.sirius.business.api.query.EObjectQuery;
import org.eclipse.sirius.business.api.resource.ResourceDescriptor;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.common.tools.api.util.StringUtil;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.obeonetwork.m2doc.element.MElement;
import org.obeonetwork.m2doc.element.MHyperLink;
import org.obeonetwork.m2doc.element.MImage;
import org.obeonetwork.m2doc.element.MList;
import org.obeonetwork.m2doc.element.MParagraph;
import org.obeonetwork.m2doc.element.MTable;
import org.obeonetwork.m2doc.element.MTable.MCell;
import org.obeonetwork.m2doc.element.MTable.MRow;
import org.polarsys.capella.common.data.modellingcore.ModelElement;
import org.polarsys.capella.common.ui.massactions.core.shared.helper.SemanticBrowserHelper;
import org.polarsys.capella.common.ui.toolkit.browser.category.ICategory;
import org.polarsys.capella.core.data.cs.Component;
import org.polarsys.capella.core.data.cs.InterfacePkg;
import org.polarsys.capella.core.data.fa.ComponentExchange;
import org.polarsys.capella.core.data.fa.ComponentPort;
import org.polarsys.capella.core.data.information.DataPkg;
import org.polarsys.capella.core.data.information.Port;
import org.polarsys.capella.core.diagram.helpers.RepresentationAnnotationHelper;
import org.polarsys.kitalpha.emde.model.ElementExtension;
import org.polarsys.kitalpha.emde.model.ExtensibleElement;

/**
 * This class contains all utility AQL templates used for M2Doc generation. The
 * goal of this class is to provides simple methods allowing to simplify the
 * M2Doc template.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
//@formatter:off
@ServiceProvider(
value = "Services available for Capella extensions."
)
//@formatter:on
public class M2DocGenServices extends AbstractServiceProvider {

    /**
     * EContents {@link IService}.
     * 
     * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
     */
    private static final class ExtensionsService extends FilterService {

        /**
         * Creates a new service instance given a method and an instance.
         * 
         * @param serviceMethod
         *            the method that realizes the service
         * @param serviceInstance
         *            the instance on which the service must be called
         */
        private ExtensionsService(Method serviceMethod, Object serviceInstance) {
            super(serviceMethod, serviceInstance);
        }

        @Override
        public Set<IType> getType(Call call, ValidationServices services, IValidationResult validationResult,
                IReadOnlyQueryEnvironment queryEnvironment, List<IType> argTypes) {
            final Set<IType> result = new LinkedHashSet<IType>();

            if (argTypes.size() == 2) {
                if (argTypes.get(1) instanceof EClassifierSetLiteralType) {
                    for (EClassifier eClassifier : ((EClassifierSetLiteralType) argTypes.get(1)).getEClassifiers()) {
                        result.add(new EClassifierType(queryEnvironment, eClassifier));
                    }
                } else if (argTypes.get(1) instanceof EClassifierLiteralType) {
                    result.add(argTypes.get(1));
                } else {
                    final Collection<EClassifier> eObjectEClasses = queryEnvironment.getEPackageProvider()
                            .getTypes("ecore", "EObject");
                    for (EClassifier eObjectEClass : eObjectEClasses) {
                        result.add(new EClassifierType(queryEnvironment, eObjectEClass));
                    }
                }
            }

            return result;
        }

    }

    /**
     * The {@link Pattern} to match hlinks with UUID.
     */
    private static final Pattern HLINK_WITH_UUID_PATTERN = Pattern
            .compile("hlink://_?([a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12})/?");

    /**
     * The UUID group in {@link #HLINK_WITH_UUID_PATTERN}.
     */
    private static final int UUID_GROUP = 1;

    /**
     * Default constructor.
     */
    public M2DocGenServices() {
    }

    // @formatter:off
    @Documentation(
        value = "Return recursively all the interface subpackages of the given interface packages.",
        params = {
            @Param(name = "interfacePkg", value = "The given interface package")
        },
        result = "a list of all interface subpackages",
        examples = {
            @Example(expression = "myInterfacePkg.getInterfaceSubPkg()", result = "a list of all interface subpackages"),
        }
    )
    // @formatter:on
    public List<InterfacePkg> getInterfaceSubPkg(InterfacePkg interfacePkg) {
        Set<InterfacePkg> packages = new HashSet<InterfacePkg>();
        EList<InterfacePkg> ownedInterfacePkgs = interfacePkg.getOwnedInterfacePkgs();
        for (InterfacePkg interfacePkg2 : ownedInterfacePkgs) {
            packages.add(interfacePkg2);
            List<InterfacePkg> allInterfacePackages = getInterfaceSubPkg(interfacePkg2);
            packages.addAll(allInterfacePackages);
        }
        return new ArrayList<InterfacePkg>(packages);
    }

    // @formatter:off
    @Documentation(
        value = "Compute the amount of contained interface subpackages.",
        params = {
            @Param(name = "interfacePkg", value = "The given interface package")
        },
        result = "the number of contained interface subpackages",
        examples = {
            @Example(expression = "myInterfacePkg.getAllSubInterfacesCount()", result = "the number of contained interface subpackages"),
        }
    )
    // @formatter:on
    public int getAllSubInterfacesCount(InterfacePkg interfacePkg) {
        List<InterfacePkg> interfaceSubPkg = getInterfaceSubPkg(interfacePkg);
        int interfaceCount = 0;
        for (InterfacePkg interfacePkg2 : interfaceSubPkg) {
            interfaceCount += interfacePkg2.getOwnedInterfaces().size();
        }
        return interfaceCount;
    }

    // @formatter:off
    @Documentation(
        value = "Return recursively all the data subpackages of the given data package.",
        params = {
            @Param(name = "dataPkg", value = "The given data package")
        },
        result = "a list of all data subpackages",
        examples = {
            @Example(expression = "myDataPkg.getAllDataPkgs()", result = "a list of all data subpackages"),
        }
    )
    // @formatter:on
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

    // @formatter:off
    @Documentation(
        value = "Compute a \"qualified name\" of the given data package. The root of the qualified name is the upper level data package.",
        params = {
            @Param(name = "dataPkg", value = "The given data package")
        },
        result = "the qualified name of the data package",
        examples = {
            @Example(expression = "myDataPkg.getDataPkgQualifiedName()", result = "the qualified name of the data package"),
        }
    )
    // @formatter:on
    public String getDataPkgQualifiedName(DataPkg dataPkg) {
        EObject eContainer = dataPkg.eContainer();
        String name = "";
        if (eContainer instanceof DataPkg) {
            String containerName = getDataPkgQualifiedName((DataPkg) eContainer);
            name = containerName + ".";
        }
        return name += dataPkg.getName();
    }

    // @formatter:off
    @Documentation(
        value = "Return the component exchange direction (UNSET/IN/OUT/INOUT) for the given component.",
        params = {
            @Param(name = "ce", value = "The given component exchange"),
            @Param(name = "component", value = "The given component")
        },
        result = "the component exchange direction (UNSET/IN/OUT/INOUT)",
        examples = {
            @Example(expression = "myDataPkg.getDataPkgQualifiedName(myComponent)", result = "'UNSET' or 'IN' or 'OUT' or 'INOUT'"),
        }
    )
    // @formatter:on
    public String getCeDirection(ComponentExchange ce, Component component) {
        Component sourceCeComponent = (Component) ce.getSourcePort().eContainer();
        Component targetCeComponent = (Component) ce.getTargetPort().eContainer();

        Port port = null;
        if (sourceCeComponent == component)
            port = ce.getSourcePort();
        else if (targetCeComponent == component)
            port = ce.getTargetPort();

        if (port instanceof ComponentPort) {
            ComponentPort cp = (ComponentPort) port;
            return cp.getOrientation().toString();
        }
        return "";
    }

    // @formatter:off
    @Documentation(
        value = "Return the component at the other end of the given component exchange and component.",
        params = {
            @Param(name = "ce", value = "The given component exchange"),
            @Param(name = "component", value = "The given component")
        },
        result = "the component at the other end of the given component exchange and component",
        examples = {
            @Example(expression = "myDataPkg.getDataPkgQualifiedName(myComponent)", result = "the component at the other end of the given component exchange and component"),
        }
    )
    // @formatter:on
    public String getDestinationComponent(ComponentExchange ce, Component component) {
        Component sourceCeComponent = (Component) ce.getSourcePort().eContainer();
        Component targetCeComponent = (Component) ce.getTargetPort().eContainer();

        Port port = null;
        if (sourceCeComponent == component)
            port = ce.getTargetPort();
        else if (targetCeComponent == component)
            port = ce.getSourcePort();

        return ((Component) port.eContainer()).getName();
    }

    // @formatter:off
    @Documentation(
        value = "Replace the MLink uri from the given MElement to reference document bookmarks.",
        params = {
            @Param(name = "element", value = "The given MElement"),
            @Param(name = "reference", value = "a reference EObject to reteive the Sirius Session")
        },
        result = "replaced the MLink uri from the given MElement to reference document bookmarks",
        examples = {
            @Example(expression = "myMElement.replaceLink(eObj)", result = "replaced the MLink uri from the given MElement to reference document bookmarks"),
        }
    )
    // @formatter:on
    public MElement replaceLink(MElement element, EObject reference) {
        final MElement res;

        if (element instanceof MList) {
            final List<MElement> newElements = new ArrayList<MElement>();
            for (MElement e : (MList) element) {
                newElements.add(replaceLink(e, reference));
            }
            ((MList) element).clear();
            ((MList) element).addAll(newElements);
            res = element;
        } else if (element instanceof MParagraph) {
            final MElement newContent = replaceLink(((MParagraph) element).getContents(), reference);
            if (newContent != ((MParagraph) element).getContents()) {
                ((MParagraph) element).setContents(newContent);
            }
            res = element;
        } else if (element instanceof MTable) {
            for (MRow row : ((MTable) element).getRows()) {
                for (MCell cell : row.getCells()) {
                    final MElement newContent = replaceLink(cell.getContents(), reference);
                    if (newContent != cell.getContents()) {
                        cell.setContents(newContent);
                    }
                }
            }
            res = element;
        } else if (element instanceof MHyperLink) {
            res = internalReplaceLink((MHyperLink) element, reference);
        } else {
            res = element;
        }

        return res;
    }

    /**
     * Replaces the given {@link MHyperLink}.
     * 
     * @param link
     *            the {@link MHyperLink}
     * @param reference
     *            a reference {@link EObject} to reteive the Sirius {@link Session}
     * @return the replaced {@link MHyperLink}
     */
    private MHyperLink internalReplaceLink(MHyperLink link, EObject reference) {
        final MHyperLink res;

        if (link.getUrl().startsWith("hlink://_") && link.getUrl().endsWith("/")) {
            final String repID = link.getUrl().substring("hlink://".length(), link.getUrl().length() - 1);
            final EObject repEObject = getLinkTargetFromId(reference, repID);
            if (repEObject instanceof DSemanticDecorator) {
                final DSemanticDecorator decorator = (DSemanticDecorator) repEObject;
                EObject eObject = decorator.getTarget();
                res = internalReplaceLinkWithModelElementId(link, eObject);
            } else {
                res = link;
            }
        } else {
            final Matcher matcher = HLINK_WITH_UUID_PATTERN.matcher(link.getUrl());
            if (matcher.matches()) {
                final String id = matcher.group(UUID_GROUP);
                final EObject repEObject = getLinkTargetFromId(reference, id);
                if (repEObject instanceof DSemanticDecorator) {
                    final DSemanticDecorator decorator = (DSemanticDecorator) repEObject;
                    EObject eObject = decorator.getTarget();
                    res = internalReplaceLinkWithModelElementId(link, eObject);
                } else {
                    final Resource capellaResource = getCapellaResource(reference);
                    if (capellaResource != null) {
                        final EObject eObject = capellaResource.getEObject(id);
                        res = internalReplaceLinkWithModelElementId(link, eObject);
                    } else {
                        res = link;
                    }
                }
            } else {
                res = link;
            }
        }

        return res;
    }

    private EObject getLinkTargetFromId(EObject reference, final String id) {
        Session session = new EObjectQuery(reference).getSession();
        EObject repEObject = null;
        if (session != null && !StringUtil.isEmpty(id)) {
            final Resource airResource = session.getSessionResource();
            repEObject = airResource.getEObject(id);

            // Aird can be fragmented or manage several airds.
            if (repEObject == null) {
                for (Resource otherAirds : session.getReferencedSessionResources()) {
                    repEObject = otherAirds.getEObject(id);
                    if (repEObject != null) {
                        break;
                    }
                }
            }

            // Aird can be split into several resource, representations can be stored in their own .srm resources.
            if (repEObject == null) {
                for (Resource srmResource : session.getSrmResources()) {
                    repEObject = srmResource.getEObject(id);
                    if (repEObject != null) {
                        break;
                    }
                }
            }

            // Some srm resource might not be loaded yet (lazy loading of DRepresentation content)
            if (repEObject == null) {
                for (DRepresentationDescriptor repDesc : DialectManager.INSTANCE
                        .getAllRepresentationDescriptors(session)) {
                    ResourceDescriptor representationPath = repDesc.getRepPath();
                    if (representationPath.getResourceURI().toString().contains(id)) {
                        // Load representation if repPath contains the searched id
                        DRepresentation rep = repDesc.getRepresentation();
                        if (rep != null && id.equals(rep.getUid())) {
                            repEObject = rep;
                            break;
                        }
                    }
                }
            }
        }

        return repEObject;
    }

    private MHyperLink internalReplaceLinkWithModelElementId(MHyperLink link, EObject eObject) {
        final MHyperLink res;
        if (eObject instanceof ModelElement) {
            final ModelElement modelElement = (ModelElement) eObject;
            link.setUrl("#" + modelElement.getId());
            res = link;
        } else {
            res = link;
        }
        return res;
    }

    /**
     * Gets the Capella {@link Resource} from the given reference {@link EObject}.
     * 
     * @param reference
     *            a reference {@link EObject} to reteive the Sirius {@link Session}
     * @return the Capella {@link Resource} from the given reference {@link EObject}
     */
    private Resource getCapellaResource(EObject reference) {
        Resource res = null;

        final Session session = new EObjectQuery(reference).getSession();
        for (Resource resource : session.getSemanticResources()) {
            if ("capella".equals(resource.getURI().fileExtension())) {
                res = resource;
                break;
            }
        }

        return res;
    }

    // @formatter:off
    @Documentation(
        value = "Reduces all MImages from the given MElement to a maximum of the given size. Smaller images will not be changed.",
        params = {
            @Param(name = "element", value = "The given MElement"),
            @Param(name = "width", value = "The maximum width"),
            @Param(name = "height", value = "The maximum height"),
        },
        result = "reduced all MImages from the given MElement to a maximum of the given size. Smaller images will not be changed",
        examples = {
            @Example(expression = "myMElement.reduceAllImages(200, 300)", result = "all reduced MImages from the given MElement"),
        }
    )
    // @formatter:on
    public MElement reduceAllImages(MElement element, Integer width, Integer height) {
        final MElement res;

        if (element instanceof MList) {
            final List<MElement> newElements = new ArrayList<MElement>();
            for (MElement e : (MList) element) {
                newElements.add(reduceAllImages(e, width, height));
            }
            ((MList) element).clear();
            ((MList) element).addAll(newElements);
            res = element;
        } else if (element instanceof MParagraph) {
            final MElement newContent = reduceAllImages(((MParagraph) element).getContents(), width, height);
            if (newContent != ((MParagraph) element).getContents()) {
                ((MParagraph) element).setContents(newContent);
            }
            res = element;
        } else if (element instanceof MTable) {
            for (MRow row : ((MTable) element).getRows()) {
                for (MCell cell : row.getCells()) {
                    final MElement newContent = reduceAllImages(cell.getContents(), width, height);
                    if (newContent != cell.getContents()) {
                        cell.setContents(newContent);
                    }
                }
            }
            res = element;
        } else if (element instanceof MImage) {
            final MImage image = (MImage) element;
            if (image.getWidth() > width) {
                image.setWidth(width);
                if (!image.conserveRatio() || image.getHeight() > height) {
                    image.setHeight(height);
                }
            } else if (image.getHeight() > height) {
                image.setHeight(height);
                if (!image.conserveRatio()) {
                    image.setWidth(width);
                }
            }
            res = image;
        } else {
            res = element;
        }

        return res;
    }

    // @formatter:off
    @Documentation(
        value = "Return the list of extension of the given type for the given extensible element.",
        params = {
            @Param(name = "element", value = "The given extensible element"),
            @Param(name = "eCls", value = "the extension EClass")
        },
        result = "the list of extension of the given type for the given extensible element",
        examples = {
            @Example(expression = "myExtensibleElement.getExtensions(some::Extension)", result = "the list of extension of the given type for the given extensible element"),
        }
    )
    // @formatter:on
    public List<EObject> getExtensions(ExtensibleElement element, EClass eCls) {
        final Set<EClass> eClasses = new HashSet<EClass>();

        eClasses.add(eCls);

        return getExtensions(element, eClasses);
    }

    // @formatter:off
    @Documentation(
        value = "Return the list of extension of the given types for the given extensible element.",
        params = {
            @Param(name = "element", value = "The given extensible element"),
            @Param(name = "eClasses", value = "the extension EClasses")
        },
        result = "the list of extension of the given types for the given extensible element",
        examples = {
            @Example(expression = "myExtensibleElement.getExtensions({some::Extension1 | some::Extension2})", result = "the list of extension of the given types for the given extensible element"),
        }
    )
    // @formatter:on
    public List<EObject> getExtensions(ExtensibleElement element, Set<EClass> eClasses) {
        final List<EObject> res = new ArrayList<EObject>();

        for (ElementExtension extension : element.getOwnedExtensions()) {
            for (EClass eCls : eClasses) {
                if (eCls.isInstance(extension)) {
                    res.add(extension);
                    break;
                }
            }
        }

        return res;
    }

    // @formatter:off
    @Documentation(
        value = "Tell if the given DRepresentation is visible in documentation.",
        params = {
            @Param(name = "rep", value = "The given DRepresentation")
        },
        result = "true if the given DRepresentation is visible in documentation, false otherwise",
        examples = {
            @Example(expression = "myDRepresentation.isVisibleInDoc({some::Extension1 | some::Extension2})", result = "true if the given DRepresentation is visible in documentation, false otherwise"),
        }
    )
    // @formatter:on
    public boolean isVisibleInDoc(DRepresentation rep) {
        final boolean res;

        final DRepresentationDescriptor descriptor = new DRepresentationQuery(rep).getRepresentationDescriptor();
        if (descriptor != null) {
            res = RepresentationAnnotationHelper.isVisibleInDoc(descriptor);
        } else {
            res = true;
        }

        return res;
    }

    // @formatter:off
    @Documentation(
        value = "Tell if the given DRepresentation is visible in LM.",
        params = {
            @Param(name = "rep", value = "The given DRepresentation")
        },
        result = "true if the given DRepresentation is visible in LM, false otherwise",
        examples = {
            @Example(expression = "myDRepresentation.isVisibleInDoc({some::Extension1 | some::Extension2})", result = "true if the given DRepresentation is visible in LM, false otherwise"),
        }
    )
    // @formatter:on
    public boolean isVisibleInLM(DRepresentation rep) {
        final boolean res;

        final DRepresentationDescriptor descriptor = new DRepresentationQuery(rep).getRepresentationDescriptor();
        if (descriptor != null) {
            res = RepresentationAnnotationHelper.isVisibleInLM(descriptor);
        } else {
            res = true;
        }

        return res;
    }

    // @formatter:off
    @Documentation(
        value = "Gets the list of available semantic browser query names for the given EObject.",
        params = {
            @Param(name = "eObj", value = "The EObject")
        },
        result = "the list of available semantic browser queries for the given EObject",
        examples = {
            @Example(expression = "myCapellaElement.availableSBQueries()", result = "Sequence{'query1', 'query2', ...}"),
        }
    )
    // @formatter:on
    public List<String> availableSBQueries(EObject eObj) {
        final List<String> res = new ArrayList<String>();

        final Set<ICategory> queries = SemanticBrowserHelper.getCommonObjectCategories(Collections.singleton(eObj));
        for (ICategory query : queries) {
            res.add(query.getName());
        }
        Collections.sort(res);

        return res;
    }

    // @formatter:off
    @Documentation(
        value = "Gets the semantic browser query result for the given query name on the given EObject.",
        params = {
            @Param(name = "eObj", value = "The EObject"),
            @Param(name = "queryName", value = "The semantic query name")
        },
        result = "the list of EObject",
        examples = {
            @Example(expression = "myCapellaElement.getSBQuery('query1')", result = "Sequence{eObject1, eObject1, ...}"),
        }
    )
    // @formatter:on
    public List<EObject> getSBQuery(EObject eObj, String queryName) {
        List<EObject> res = new ArrayList<EObject>();

        final Set<ICategory> queries = SemanticBrowserHelper.getCommonObjectCategories(Collections.singleton(eObj));
        ICategory foundQuery = null;
        for (ICategory query : queries) {
            if (query.getName().equals(queryName)) {
                foundQuery = query;
                break;
            }
        }
        if (foundQuery != null) {
            for (Object object : foundQuery.compute(eObj)) {
                if (object instanceof EObject) {
                    res.add((EObject) object);
                }
            }
        }

        return res;
    }

    @Override
    protected IService getService(Method publicMethod) {
        final IService result;

        if ("getExtensions".equals(publicMethod.getName())) {
            result = new ExtensionsService(publicMethod, this);
        } else {
            result = new JavaMethodService(publicMethod, this);
        }

        return result;
    }

}
