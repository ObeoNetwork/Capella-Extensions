/*******************************************************************************
 * Copyright (c) 2017, 2021 Obeo.
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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
import org.eclipse.sirius.business.api.query.DRepresentationQuery;
import org.eclipse.sirius.business.api.query.EObjectQuery;
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
            final MHyperLink link = (MHyperLink) element;
            if (link.getUrl().startsWith("hlink://_") && link.getUrl().endsWith("/")) {
                final Resource airResource = new EObjectQuery(reference).getSession().getSessionResource();
                final String repID = link.getUrl().substring("hlink://".length(), link.getUrl().length() - 1);
                final EObject repEObject = airResource.getEObject(repID);
                if (repEObject instanceof DSemanticDecorator) {
                    final DSemanticDecorator decorator = (DSemanticDecorator) repEObject;
                    if (decorator.getTarget() instanceof ModelElement) {
                        final ModelElement modelElement = (ModelElement) decorator.getTarget();
                        link.setUrl("#" + modelElement.getId());
                        res = link;
                    } else {
                        res = link;
                    }
                } else {
                    res = link;
                }
            } else {
                res = link;
            }
        } else {
            res = element;
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

    /**
     * {@inheritDoc}
     *
     * @see org.eclipse.acceleo.query.runtime.impl.AbstractServiceProvider#getService(java.lang.reflect.Method)
     */
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
