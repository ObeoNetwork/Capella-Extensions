package org.obeonetwork.capella.m2doc.aql.queries;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SemanticBrowserUtils {

    private static class Query {

        private String name;
        private String parameterType;
        private String queryClass;

    }

    //@formatter:off
    // grep new /home/development/git/Capella-Extensions/plugins/org.obeonetwork.capella.m2doc.aql.queries/src/org/obeonetwork/capella/m2doc/aql/queries/SemanticBrowserServices.java | sed --expression='s/new /["/g' | sed --expression='s/()/",]/g' | cut -d"[" -f2 | cut -d"]" -f1
	private static final String[] KNOWN_QUERIES = new String[] {
	    "org.polarsys.capella.common.re.ui.queries.ReferencingReplicableElementLinks",
	    "org.polarsys.capella.common.re.ui.queries.CatalogElementOrigin",
	    "org.polarsys.capella.common.re.ui.queries.ReferencingReplicas",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.OwnedSpecification",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ModelElementGuard",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ModelElementPostCondition",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ModelElementExchangeContext",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ModelElementPreCondition",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ModelElementConstraints",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.TraceableElementOutgoingTrace",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.TraceableElementIncomingTrace",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElement_applied_property_value_groups",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElement_requirement",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElement_applied_property_values",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PropertyOwner",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PropertyAssociation",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PropertyType",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractTypeAbstractTypedElement",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_parentFunction",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Function_functionBreakdown",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_activeInModes",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_activeInStates",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionFunctionalChain",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_mother_function_allocation",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Function_ownedFunctionalChains",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalOutGoingDataflows",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_outgoingInteraction",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_realizingFunctions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalInComingDataflows",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingComponent",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_mother_activity_allocation",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.OperationalActivityOperationalProcess",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.OperationActivityAllocatingRole",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_incomingInteraction",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingActor",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChain_owningFunction",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvementFunctions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainRealizedOperationalProcess",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainChildren",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainRealizedFunctionalChains",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainAvailableInState",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvementLinks",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChain_enactedComponents",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainAvailableInMode",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.LAAndPAFunctionalChainInvolvingCapabilityRealization",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SAFunctionalChainInvolvingCapability",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainRealizingFunctionalChains",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainParent",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.OperationalProcessChildren",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.OperationalProcessInvolvedOperationalActivities",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvingCapability",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.OperationalProcessParent",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Capability_scenarios",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilitySuper",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityAvailableInMode",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Capability_InvolvedComponents",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Capability_extendedCapabilities",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapability_refinedAbstractCapabilities",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityAvailableInState",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Capability_includedCapabilities",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilitySub",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapability_refiningAbstractCapabilities",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Capability_includingCapabilities",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Capability_extendingCapabilities",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityOwnedFunctionalChains",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.OperationalCapability_InvolvedEntity",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityInvolvedFunctionalChains",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.OCapabilityRealizingCapability",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAllocationTarget",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAllocationSource",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Role_AllocatedFunctions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Role_AllocatingEntity",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractTypeExchangeItemElements",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractTypeTypedElements",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.GeneralizableElementAllSuperGE",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.GeneralizableElementAllSubGE",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_componentBreakdown",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_parentComponent",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_SubDefinedComponents",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.GeneralizableElementAllSuperGC",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentInternalOutgoingComponentExchanges",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentOutgoingDelegation",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentProvidedInterfaces",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentCommunicationLink",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_usedInterfaces",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentRequiredInterfaces",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentOutgoingComponentExchange",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_representingParts",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_implementedInterfaces",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentIncomingComponentExchange",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_componentPorts",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentIncomingDelegation",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.GeneralizableElementAllSubGC",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_referencingComponent",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentInternalIncomingComponentExchanges",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.OperationalEntity_Breakdown",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.OperationalEntityAllocatedRoles",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_AllocatedFunctions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.OperationalEntity_OutgoingCommunicationMean",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.OperationalEntity_IncomingCommunicationMean",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.OperationalEntityInvolgingOperationalCap",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizingComponents",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeSpecification_realizedDataflow",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeSpecification_dataflowTarget",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeSpecification_dataflowSource",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeSpecification_relatedData",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeSpecification_owner",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentExchangeCategoriesForDelegations",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentExchangeAllocatedFunctionalExchanges",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentExchangeCategories",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Connection_connectedComponents",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ConnectionConvoyedInformation",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponenExchangeRealizedCommunicationMean",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Connection_connectedParts",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeSpecification_realizingDataflow",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentExchangeAllocatingPhysicalPath",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentExchangeAllocatingPhysicalLink",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CommunicationMean_AllocatedExchanges",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CommunicationMean_Target",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CommunicationMean_Source",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsInvolvementTarget",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsInvolvementSource",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Function_ownedFunctionalChains",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_outgoingInteraction",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Function_outFlowPorts",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_realizedFunctions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalOutGoingDataflows",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.LogicalAndPhysicalFunctionInvolvingCapabilityRealization",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingComponent",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalInComingDataflows",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_incomingInteraction",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingActor",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Function_inFlowPorts",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityRealizationInvolvedElement_InvolvingCapabilityRealizations",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalComponent_deployedPhysicalComponents",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalComponent_IncomingPhysicalLinks",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_AllocatedFunctions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalComponent_InternalPhysicalLinks",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizedComponents",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizingComponents",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalComponent_OutgoingPhysicalLinks",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalComponent_deployingPhysicalComponents",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalArtifactsRealizingCI",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Part_type",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractDependenciesPkg_dependencies",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractDependenciesPkg_inverse_dependencies",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.InterfaceExchangesItems",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Interface_provisionedInterfaces",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.InterfaceInheritedExchangesItems",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.InterfaceUsers",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Interface_provisioningInterfaces",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Interface_involvingScenarios",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.InterfaceProviders",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.InterfaceRequires",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.InterfaceImplementors",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsInterfaceImplementationTarget",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsInterfaceImplementationSource",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsInterfaceUseTarget",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsInterfaceUseSource",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeItemAllocationExchangeItem",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalLinkCategoriesForDelegations",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalLinksRealizedConnection",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalLinkCategories",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalLinkSourceAndTarget",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalArtifactsRealizingCI",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalLinksInvolvedInPhysicalPaths",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CategoryPhysicalLink",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPathInvolvedPhysicalPath",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPath_PhysicalLinks",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPath_RealisedConnection",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPathInvolvingPhysicalPath",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPathInvolvmentInvolvedPhysicalPath",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPathInvolvmentPhysicalComp",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPathInvolvmentPhysicalLink",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPortOutgoingFunctionPorts",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPortAllocatedComponentPorts",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPortOutgoingDelgations",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPortIncomingPhysicalLinks",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalArtifactsRealizingCI",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Function_ownedFunctionalChains",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_outgoingInteraction",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Function_outFlowPorts",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalOutGoingDataflows",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_realizedFunctions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_realizingFunctions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingActor",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalInComingDataflows",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Function_inFlowPorts",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingComponent",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.LogicalAndPhysicalFunctionInvolvingCapabilityRealization",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_incomingInteraction",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizedComponents",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_AllocatedFunctions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizingComponents",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityOwnedFunctionalChains",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityInvolvedFunctionalChains",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityRealization_RealizedCapability",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityRealization_RealizedCapabilityRealization",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.LAAbstractCapabilityInvolvedFunctions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PAAbstractCapabilityInvolvedFunctions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityRealization_RealizingCapabilityRealization",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PropertyValue_applying_valued_element_DataValue",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.DataValueRefReferencedProperty",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.DataValueRefReferencedValue",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.DataValueReferencingReferencedValue",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsTraceTarget",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsTraceSource",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.RequirementTracedElements",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CategoryFunctionalExchange",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvementFunctions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainChildren",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvementLinks",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainParent",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionPortRealizedFunctionPort",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionPortAllocatedExchangeItems",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionPortRealizingFunctionPort",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionPortAllocatingCompoentPort",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Pin_type",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Pin_owner",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Pin_realizedFlowPort",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Pin_outgoingDataflows",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Pin_incomingDataflows",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Pin_realizingFlowPort",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchange_relatedData",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchange_owner",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeExchangesItems",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchange_dataflowTarget",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeRealizedFunctionalExchanges",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeRealizedInteractions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeCategory",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchange_realizingDataflow",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeFunctionalChain",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchange_dataflowSource",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeAllocatingCommunicationMean",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeAllocatingComponentExchange",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeOperationalProcess",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CategoryComponentExchange",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_type",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_owner",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_providedInterfaces",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_requiredInterfaces",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_realizedComponentPort",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_realizedFunctionPort",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPortOutgoingDeletations",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPortOutgoingComponentExchanges",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPortAllocatingPhysicalPorts",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPortIncomingComponentExchanges",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_realizingComponentPort",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPortIncomingDeletations",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ReferenceHierarchyContextSource",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ReferenceHierarchyContextTarget",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvmentLinkExchangeContext",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvmentLinkTarget",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvmentLinkSource",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkCondition",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementOwner",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkLinks",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkTargetControlNode",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkTargetInvolvementFunction",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkSourceInvolvementFunction",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkSourceControlNode",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkEndTargetSequenceLinks",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkEndSourceSequenceLinks",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementOwner",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvementFunctionOutgoingInvolvementLinks",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvementFunctionIncomingInvolvementLinks",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.State_ParentState",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.State_InvolvedStates",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.State_OwnedStates",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.StateAndModeOutGoingTransition",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.StateAndModeInComingTransition",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.State_InvolvingStates",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.State_OwnedEntryExitPoints",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateRealizedMode",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateRealizedState",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateRealizingState",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateRealizingMode",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateModeDoActivity",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateAvailableElements",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateParentActiveElements",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.StateTransitionEffect",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.StateTransitionOutGoingIState",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.StateTransitionTrigger",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.StateTransitionInComingIState",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.EntryExitPoint_ParentRegion",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Function_ownedFunctionalChains",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_realizedFunctions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalOutGoingDataflows",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Function_outFlowPorts",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_outgoingInteraction",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_incomingInteraction",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalInComingDataflows",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_realizingFunctions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingActor",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Function_inFlowPorts",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SystemFunctionInvolvingCapabilities",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingComponent",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Mission_ExploitedCapabilities",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Mission_InvolvedSystemComponents",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityOwnedFunctionalChains",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityRealizedOC",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityInvolvedFunctions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityInvolvedFunctionalChains",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Capability_purposeMissions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityRealizingCR",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsCapabilityExploitationTarget",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsCapabilityExploitationSource",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_AllocatedFunctions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizedComponents",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizingComponents",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SystemComponent_InvolvingCapabilities",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SystemComponent_InvolvingMissions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ConstraintModelElements",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsGeneralizationTarget",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsGeneralizationSource",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PropertyValue_applying_valued_element_Primitive",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PropertyValue_applying_valued_element",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.PropertyValueGroup_applying_valued_element",
	    "org.polarsys.capella.common.re.ui.queries.CatalogElementLinkReferencedElement",
	    "org.polarsys.capella.common.re.ui.queries.CatalogElementRelatedSemanticElements",
	    "org.polarsys.capella.common.re.ui.queries.CatalogElementRelatedReplicas",
	    "org.polarsys.capella.common.re.ui.queries.CatalogElementReverseOrigin",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CIRealizedPhysicalComponents",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CIRealizedPhysicalLinks",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CIRealizedPhysicalPorts",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CommunicationLinkExchangeItem",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CommunicationLinkComponent",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessage_parentScenario",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageInvokedExchangeItemAllocation",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessage_refiningSequenceMessage",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageInvokedComponentExchange",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageInvokedCommunicationMean",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageExchangeItems",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageInvokedInteraction",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessage_AllocatedFunctionalExchange",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageFunctionTarget",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessagePartTarget",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessage_invokedOperation",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessagePartSource",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessage_refinedSequenceMessage",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageFunctionSource",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ItemQuery_Scenario_getAbstractCapabilityContainer",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ItemQuery_Scenario_getReferencedScenarios",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Scenario_realizedScenario",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Scenario_refiningScenarios",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Scenario_refinedScenarios",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Scenario_realizingScenario",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.InstanceRole_parentScenario",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.InstanceRole_representedInstance",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAbstractCapabilityExtendTarget",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAbstractCapabilityExtendSource",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAbstractCapabilityGeneralizationTarget",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAbstractCapabilityGeneralizationSource",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAbstractCapabilityIncludeTarget",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAbstractCapabilityIncludeSource",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.InteractionUseReferencedScenario",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ItemQuery_Scenario_getReferencingScenarios",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.StateFragmentRelatedFunctions",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.StateFragmentRelatedStates",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAssociationRoles",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ClassRealizedClass",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ClassRealizingClass",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CollectionType",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.Parameter_Type",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeItem_realizedEI",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ExchangesItemExchangeItemElements",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeItem_realizingEI",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeItemAllocatingOutPutFunctionPorts",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeItemAllocatingInputFunctionPorts",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ExchangesItemExchangeItemAllocations",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ExchangesItemCommLink",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.EIActiveInConnectionsAndExchanges",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeItemElementType",
	    "org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario",
	};
	//@formatter:on

    private static final XPath XPATH = XPathFactory.newInstance().newXPath();

    private static final XPathExpression CATEGORIES_EXPRESSION = initCategoriesExpression();

    private static final XPathExpression PARAMETER_TYPE_EXPRESSION = initParameterTypeExpression();

    private static final XPathExpression QUERY_CLASS_BASIC_EXPRESSION = initQueryClassBasicExpression();

    private static final XPathExpression QUERY_CLASS_NAVIGATION_EXPRESSION = initQueryClassNavigationExpression();

    public static void main(String[] args)
            throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        final Set<String> knwonQueries = new HashSet<String>(Arrays.asList(KNOWN_QUERIES));

        final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = builderFactory.newDocumentBuilder();

        final List<Query> queries = new ArrayList<Query>();
        for (File pluginXmlFile : getPluginXmlFiles(new File("/mnt/development/git/capella"))) {
            final FileInputStream fileIS = new FileInputStream(pluginXmlFile);
            final Document xmlDocument = builder.parse(fileIS);
            final NodeList categories = (NodeList) CATEGORIES_EXPRESSION.evaluate(xmlDocument, XPathConstants.NODESET);
            for (int i = 0; i < categories.getLength(); i++) {
                final Query query = createQuery(categories.item(i));
                if (query != null && !knwonQueries.contains(query.queryClass)) {
                    queries.add(query);
                }
            }
        }

        for (Query query : queries) {
            final String[] splited = query.parameterType.split("\\.");
            System.out.println(
                    "public List<Object> get" + getCategoryJavaName(query.name) + "(" + splited[splited.length - 1]
                        + " value) { return castList(new " + query.queryClass + "().compute(value));}");
        }
    }

    private static String getCategoryJavaName(String name) {
        final StringBuilder res = new StringBuilder();

        boolean toUpper = true;
        for (int i = 0; i < name.length(); i++) {
            final char c = name.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                if (toUpper) {
                    res.append(Character.toUpperCase(c));
                } else {
                    res.append(c);
                }
                toUpper = false;
            } else {
                toUpper = true;
            }
        }

        if (res.charAt(res.length() - 1) == '_') {
            return res.substring(0, res.length() - 1).trim();
        } else {
            return res.toString().trim();
        }
    }

    private static XPathExpression initCategoriesExpression() {
        try {
            return XPATH.compile(
                    "//extension[@point=\"org.polarsys.capella.common.ui.toolkit.browser.contentProviderCategory\"]/category");
        } catch (XPathExpressionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    private static XPathExpression initParameterTypeExpression() {
        try {
            return XPATH.compile("availableForType");
        } catch (XPathExpressionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    private static XPathExpression initQueryClassBasicExpression() {
        try {
            return XPATH.compile("categoryQuery/basicQuery");
        } catch (XPathExpressionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    private static XPathExpression initQueryClassNavigationExpression() {
        try {
            return XPATH.compile("categoryQuery/navigationQuery");
        } catch (XPathExpressionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    private static Query createQuery(Node node) {

        try {
            if (node.getAttributes().getNamedItem("name") != null) {
                final Query res = new Query();
                res.name = node.getAttributes().getNamedItem("name").getNodeValue();
                res.parameterType = ((Node) PARAMETER_TYPE_EXPRESSION.evaluate(node, XPathConstants.NODE))
                        .getAttributes().getNamedItem("class").getNodeValue();
                final Node queryClassBasicNode = (Node) QUERY_CLASS_BASIC_EXPRESSION.evaluate(node,
                        XPathConstants.NODE);
                if (queryClassBasicNode != null) {
                    res.queryClass = queryClassBasicNode.getAttributes().getNamedItem("class").getNodeValue();
                } else {
                    res.queryClass = ((Node) QUERY_CLASS_NAVIGATION_EXPRESSION.evaluate(node, XPathConstants.NODE))
                            .getAttributes().getNamedItem("class").getNodeValue();
                }
                return res;
            } else {
                return null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    static String node2String(Node node) throws TransformerFactoryConfigurationError, TransformerException {
        // you may prefer to use single instances of Transformer, and
        // StringWriter rather than create each time. That would be up to your
        // judgement and whether your app is single threaded etc
        final StreamResult xmlOutput = new StreamResult(new StringWriter());
        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new DOMSource(node), xmlOutput);
        return xmlOutput.getWriter().toString();
    }

    private static List<File> getPluginXmlFiles(File root) {
        final List<File> res = new ArrayList<File>();

        for (File child : root.listFiles()) {
            if (child.isDirectory()) {
                res.addAll(getPluginXmlFiles(child));
            } else if ("plugin.xml".equals(child.getName())) {
                res.add(child);
            }
        }

        return res;
    }

}
