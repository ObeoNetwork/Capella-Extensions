/*******************************************************************************
 * Copyright (c) 2021 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.obeonetwork.capella.m2doc.aql.queries;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.common.data.activity.ActivityNode;
import org.polarsys.capella.common.data.activity.Pin;
import org.polarsys.capella.common.data.behavior.AbstractEvent;
import org.polarsys.capella.common.data.modellingcore.AbstractConstraint;
import org.polarsys.capella.common.data.modellingcore.AbstractExchangeItem;
import org.polarsys.capella.common.data.modellingcore.AbstractType;
import org.polarsys.capella.common.data.modellingcore.AbstractTypedElement;
import org.polarsys.capella.common.data.modellingcore.IState;
import org.polarsys.capella.common.data.modellingcore.InformationsExchanger;
import org.polarsys.capella.common.data.modellingcore.ModelElement;
import org.polarsys.capella.common.data.modellingcore.TraceableElement;
import org.polarsys.capella.common.data.modellingcore.ValueSpecification;
import org.polarsys.capella.common.re.CatalogElement;
import org.polarsys.capella.common.re.CatalogElementLink;
import org.polarsys.capella.core.data.capellacommon.AbstractState;
import org.polarsys.capella.core.data.capellacommon.CapabilityRealizationInvolvedElement;
import org.polarsys.capella.core.data.capellacommon.Mode;
import org.polarsys.capella.core.data.capellacommon.Pseudostate;
import org.polarsys.capella.core.data.capellacommon.Region;
import org.polarsys.capella.core.data.capellacommon.State;
import org.polarsys.capella.core.data.capellacommon.StateTransition;
import org.polarsys.capella.core.data.capellacore.AbstractDependenciesPkg;
import org.polarsys.capella.core.data.capellacore.AbstractPropertyValue;
import org.polarsys.capella.core.data.capellacore.Allocation;
import org.polarsys.capella.core.data.capellacore.CapellaElement;
import org.polarsys.capella.core.data.capellacore.Constraint;
import org.polarsys.capella.core.data.capellacore.GeneralizableElement;
import org.polarsys.capella.core.data.capellacore.Generalization;
import org.polarsys.capella.core.data.capellacore.InvolvedElement;
import org.polarsys.capella.core.data.capellacore.Involvement;
import org.polarsys.capella.core.data.capellacore.InvolverElement;
import org.polarsys.capella.core.data.capellacore.PropertyValueGroup;
import org.polarsys.capella.core.data.capellacore.Trace;
import org.polarsys.capella.core.data.capellacore.Type;
import org.polarsys.capella.core.data.cs.AbstractPathInvolvedElement;
import org.polarsys.capella.core.data.cs.Component;
import org.polarsys.capella.core.data.cs.ExchangeItemAllocation;
import org.polarsys.capella.core.data.cs.Interface;
import org.polarsys.capella.core.data.cs.InterfaceImplementation;
import org.polarsys.capella.core.data.cs.InterfaceUse;
import org.polarsys.capella.core.data.cs.Part;
import org.polarsys.capella.core.data.cs.PhysicalLink;
import org.polarsys.capella.core.data.cs.PhysicalLinkCategory;
import org.polarsys.capella.core.data.cs.PhysicalPath;
import org.polarsys.capella.core.data.cs.PhysicalPathInvolvement;
import org.polarsys.capella.core.data.cs.PhysicalPort;
import org.polarsys.capella.core.data.ctx.Capability;
import org.polarsys.capella.core.data.ctx.CapabilityExploitation;
import org.polarsys.capella.core.data.ctx.Mission;
import org.polarsys.capella.core.data.ctx.SystemComponent;
import org.polarsys.capella.core.data.ctx.SystemFunction;
import org.polarsys.capella.core.data.epbs.ConfigurationItem;
import org.polarsys.capella.core.data.fa.AbstractFunction;
import org.polarsys.capella.core.data.fa.ComponentExchange;
import org.polarsys.capella.core.data.fa.ComponentExchangeCategory;
import org.polarsys.capella.core.data.fa.ComponentPort;
import org.polarsys.capella.core.data.fa.ControlNode;
import org.polarsys.capella.core.data.fa.ExchangeCategory;
import org.polarsys.capella.core.data.fa.ExchangeSpecification;
import org.polarsys.capella.core.data.fa.FunctionInputPort;
import org.polarsys.capella.core.data.fa.FunctionOutputPort;
import org.polarsys.capella.core.data.fa.FunctionPort;
import org.polarsys.capella.core.data.fa.FunctionalChain;
import org.polarsys.capella.core.data.fa.FunctionalChainInvolvement;
import org.polarsys.capella.core.data.fa.FunctionalChainInvolvementFunction;
import org.polarsys.capella.core.data.fa.FunctionalChainInvolvementLink;
import org.polarsys.capella.core.data.fa.FunctionalChainReference;
import org.polarsys.capella.core.data.fa.FunctionalExchange;
import org.polarsys.capella.core.data.fa.ReferenceHierarchyContext;
import org.polarsys.capella.core.data.fa.SequenceLink;
import org.polarsys.capella.core.data.fa.SequenceLinkEnd;
import org.polarsys.capella.core.data.information.AbstractEventOperation;
import org.polarsys.capella.core.data.information.Association;
import org.polarsys.capella.core.data.information.Collection;
import org.polarsys.capella.core.data.information.ExchangeItem;
import org.polarsys.capella.core.data.information.ExchangeItemElement;
import org.polarsys.capella.core.data.information.ExchangeItemInstance;
import org.polarsys.capella.core.data.information.Operation;
import org.polarsys.capella.core.data.information.Parameter;
import org.polarsys.capella.core.data.information.Port;
import org.polarsys.capella.core.data.information.Property;
import org.polarsys.capella.core.data.information.communication.CommunicationLink;
import org.polarsys.capella.core.data.information.datavalue.DataValue;
import org.polarsys.capella.core.data.interaction.AbstractCapability;
import org.polarsys.capella.core.data.interaction.AbstractCapabilityExtend;
import org.polarsys.capella.core.data.interaction.AbstractCapabilityGeneralization;
import org.polarsys.capella.core.data.interaction.AbstractCapabilityInclude;
import org.polarsys.capella.core.data.interaction.InstanceRole;
import org.polarsys.capella.core.data.interaction.InteractionUse;
import org.polarsys.capella.core.data.interaction.Scenario;
import org.polarsys.capella.core.data.interaction.SequenceMessage;
import org.polarsys.capella.core.data.interaction.StateFragment;
import org.polarsys.capella.core.data.la.CapabilityRealization;
import org.polarsys.capella.core.data.la.LogicalComponent;
import org.polarsys.capella.core.data.la.LogicalFunction;
import org.polarsys.capella.core.data.oa.CommunicationMean;
import org.polarsys.capella.core.data.oa.Entity;
import org.polarsys.capella.core.data.oa.OperationalActivity;
import org.polarsys.capella.core.data.oa.OperationalCapability;
import org.polarsys.capella.core.data.oa.OperationalProcess;
import org.polarsys.capella.core.data.oa.Role;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.core.data.pa.PhysicalFunction;
import org.polarsys.capella.core.data.requirement.Requirement;
import org.polarsys.kitalpha.emde.model.Element;

/**
 * Semantic browser services.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 *
 */
public class SemanticBrowserServices {

	@SuppressWarnings("unchecked")
	private <T> List<T> castList(List<Object> list) {
		final List<T> res = new ArrayList<T>();

		for (Object element : list) {
			res.add((T) element);
		}

		return res;
	}

	public List<EObject> getRECSourceElement(Element value) {
		return castList(
				new org.polarsys.capella.common.re.ui.queries.ReferencingReplicableElementLinks().compute(value));
	}

	public List<CatalogElement> getREC(Element value) {
		return castList(new org.polarsys.capella.common.re.ui.queries.CatalogElementOrigin().compute(value));
	}

	public List<CatalogElement> getRPL(Element value) {
		return castList(new org.polarsys.capella.common.re.ui.queries.ReferencingReplicas().compute(value));
	}

	public List<ValueSpecification> getExpression(ModelElement value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.OwnedSpecification().compute(value));
	}

	public List<AbstractConstraint> getGuard(ModelElement value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ModelElementGuard().compute(value));
	}

	public List<AbstractConstraint> getPostCondition(ModelElement value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ModelElementPostCondition()
				.compute(value));
	}

	public List<AbstractConstraint> getExchangeContext(ModelElement value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ModelElementExchangeContext()
				.compute(value));
	}

	public List<AbstractConstraint> getPreCondition(ModelElement value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ModelElementPreCondition().compute(value));
	}

	public List<AbstractConstraint> getConstrainingElements(ModelElement value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ModelElementConstraints().compute(value));
	}

	public List<TraceableElement> getOutgoingGenericTraces(TraceableElement value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.TraceableElementOutgoingTrace()
				.compute(value));
	}

	public List<TraceableElement> getIncomingGenericTraces(TraceableElement value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.TraceableElementIncomingTrace()
				.compute(value));
	}

	public List<PropertyValueGroup> getAppliedPropertyValueGroups(CapellaElement value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElement_applied_property_value_groups()
						.compute(value));
	}

	public List<TraceableElement> getRequirements(CapellaElement value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElement_requirement()
				.compute(value));
	}

	public List<AbstractPropertyValue> getAppliedPropertyValues(CapellaElement value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElement_applied_property_values()
						.compute(value));
	}

	public List<EObject> getParent(Property value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PropertyOwner().compute(value));
	}

	public List<Association> getAssociation(Property value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.PropertyAssociation().compute(value));
	}

	public List<Type> getType(Property value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PropertyType().compute(value));
	}

	public List<AbstractTypedElement> getInheritedOfTypingElements(AbstractType value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractTypeAbstractTypedElement()
				.compute(value));
	}

	public List<AbstractFunction> getParent(AbstractFunction value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_parentFunction()
				.compute(value));
	}

	public List<AbstractFunction> getBreakdown(AbstractFunction value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Function_functionBreakdown()
				.compute(value));
	}

	public List<Mode> getActiveInModes(AbstractFunction value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_activeInModes()
				.compute(value));
	}

	public List<Mode> getActiveInStates(AbstractFunction value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_activeInStates()
				.compute(value));
	}

	public List<FunctionalChain> getFunctionalChains(AbstractFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionFunctionalChain().compute(value));
	}

	public List<Component> getAllocatingActorComponentComputed(AbstractFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_mother_function_allocation()
						.compute(value));
	}

	public List<Scenario> getScenarios(AbstractFunction value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario()
				.compute(value));
	}

	public List<OperationalProcess> getOwnedOperationalProcesses(OperationalActivity value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Function_ownedFunctionalChains()
				.compute(value));
	}

	public List<FunctionalExchange> getInternalOutgoingInteractions(OperationalActivity value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalOutGoingDataflows()
				.compute(value));
	}

	public List<AbstractFunction> getOutgoingInteractions(OperationalActivity value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_outgoingInteraction()
						.compute(value));
	}

	public List<SystemFunction> getRealizingSystemFunctions(OperationalActivity value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_realizingFunctions()
						.compute(value));
	}

	public List<FunctionalExchange> getInternalIncomingInteractions(OperationalActivity value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalInComingDataflows()
				.compute(value));
	}

	// TODO AbstractFunctionalBlock and Role
	public List<EObject> getAllocatingEntity(OperationalActivity value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingComponent()
				.compute(value));
	}

	// TODO Component and Role
	public List<EObject> getAllocatingActorEntityRoleComputed(OperationalActivity value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_mother_activity_allocation()
						.compute(value));
	}

	public List<OperationalProcess> getOperationalProcesses(OperationalActivity value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalActivityOperationalProcess()
						.compute(value));
	}

	public List<Role> getAllocatingRole(OperationalActivity value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.OperationActivityAllocatingRole()
				.compute(value));
	}

	public List<FunctionalExchange> getIncomingInteractions(OperationalActivity value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_incomingInteraction()
						.compute(value));
	}

	// TODO Component and Role
	public List<EObject> getAllocatingOperationalActor(OperationalActivity value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingActor().compute(value));
	}

	public List<AbstractFunction> getOwner(FunctionalChain value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChain_owningFunction()
				.compute(value));
	}

	public List<FunctionalChainInvolvementFunction> getInvolvedFunctions(FunctionalChain value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvementFunctions()
						.compute(value));
	}

	public List<OperationalProcess> getRealizedOperationalProcesses(FunctionalChain value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainRealizedOperationalProcess()
						.compute(value));
	}

	public List<FunctionalChainReference> getInvolvedFunctionalChains(FunctionalChain value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainChildren().compute(value));
	}

	public List<OperationalProcess> getRealizedFunctionalChains(FunctionalChain value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainRealizedFunctionalChains()
						.compute(value));
	}

	public List<Mode> getActiveInStates(FunctionalChain value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainAvailableInState()
				.compute(value));
	}

	public List<FunctionalChainInvolvementLink> getInvolvementLinks(FunctionalChain value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvementLinks()
				.compute(value));
	}

	public List<Component> getInvolvedComponents(FunctionalChain value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChain_enactedComponents()
				.compute(value));
	}

	public List<Mode> getActiveInModes(FunctionalChain value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainAvailableInMode()
				.compute(value));
	}

	public List<AbstractCapability> getInvolvingCapabilityRealizations(FunctionalChain value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.LAAndPAFunctionalChainInvolvingCapabilityRealization()
						.compute(value));
	}

	public List<AbstractCapability> getInvolvingCapabilities(FunctionalChain value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.SAFunctionalChainInvolvingCapability()
						.compute(value));
	}

	public List<TraceableElement> getRealizingFunctionalChains(FunctionalChain value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainRealizingFunctionalChains()
						.compute(value));
	}

	public List<FunctionalChainReference> getParentFunctionalChains(FunctionalChain value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainParent().compute(value));
	}

	public List<OperationalProcess> getInvolvedOperationalProcesses(OperationalProcess value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalProcessChildren()
				.compute(value));
	}

	public List<OperationalActivity> getInvolvedOperationalActivities(OperationalProcess value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalProcessInvolvedOperationalActivities()
						.compute(value));
	}

	public List<OperationalActivity> getInvolvingOperationalCapabilities(OperationalProcess value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvingCapability()
						.compute(value));
	}

	public List<OperationalProcess> getParentOperationalProcesses(OperationalProcess value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalProcessParent().compute(value));
	}

	public List<Scenario> getScenarios(AbstractCapability value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.Capability_scenarios().compute(value));
	}

	public List<AbstractCapability> getGeneralizedElements(AbstractCapability value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilitySuper().compute(value));
	}

	public List<Mode> getActiveInModes(AbstractCapability value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityAvailableInMode()
				.compute(value));
	}

	public List<CapabilityRealizationInvolvedElement> getInvolvedComponents(AbstractCapability value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Capability_InvolvedComponents()
				.compute(value));
	}

	public List<AbstractCapability> getExtendedCapabilities(AbstractCapability value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Capability_extendedCapabilities()
				.compute(value));
	}

	public List<CapellaElement> getRefinedCapabilities(AbstractCapability value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapability_refinedAbstractCapabilities()
						.compute(value));
	}

	public List<Mode> getActiveInStates(AbstractCapability value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityAvailableInState()
				.compute(value));
	}

	public List<AbstractCapability> getIncludedCapabilities(AbstractCapability value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Capability_includedCapabilities()
				.compute(value));
	}

	public List<AbstractCapability> getGeneralizingElements(AbstractCapability value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilitySub().compute(value));
	}

	public List<CapellaElement> getRefiningScenarios(AbstractCapability value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapability_refiningAbstractCapabilities()
						.compute(value));
	}

	public List<AbstractCapability> getIncludingCapabilities(AbstractCapability value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Capability_includingCapabilities()
				.compute(value));
	}

	public List<AbstractCapability> getExtendingCapabilities(AbstractCapability value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Capability_extendingCapabilities()
				.compute(value));
	}

	public List<OperationalProcess> getOwnedOperationalProcesses(OperationalCapability value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityOwnedFunctionalChains()
						.compute(value));
	}

	public List<Entity> getInvolvedEntities(OperationalCapability value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalCapability_InvolvedEntity()
						.compute(value));
	}

	public List<OperationalProcess> getInvolvedOperationalProcesses(OperationalCapability value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityInvolvedFunctionalChains()
						.compute(value));
	}

	public List<Capability> getRealizingCapabilities(OperationalCapability value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.OCapabilityRealizingCapability()
				.compute(value));
	}

	public List<TraceableElement> getTarget(Allocation value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAllocationTarget()
						.compute(value));
	}

	public List<TraceableElement> getSource(Allocation value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAllocationSource()
						.compute(value));
	}

	public List<OperationalActivity> getAllocatedOperationalActivities(Role value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.Role_AllocatedFunctions().compute(value));
	}

	public List<Entity> getAllocatingEntities(Role value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.Role_AllocatingEntity().compute(value));
	}

	public List<Scenario> getScenarios(Role value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario()
				.compute(value));
	}

	public List<ExchangeItemElement> getExchangeItemElements(Type value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractTypeExchangeItemElements()
				.compute(value));
	}

	public List<ExchangeItemElement> getTypingElements(Type value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractTypeTypedElements()
				.compute(value));
	}

	public List<GeneralizableElement> getGeneralizedElements(GeneralizableElement value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.GeneralizableElementAllSuperGE()
				.compute(value));
	}

	public List<GeneralizableElement> getGeneralizingElements(GeneralizableElement value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.GeneralizableElementAllSubGE()
				.compute(value));
	}

	public List<Component> getComponentBreakdown(Component value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_componentBreakdown()
				.compute(value));
	}

	public List<Component> getParent(Component value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_parentComponent()
				.compute(value));
	}

	public List<Component> getOwnedComponents(Component value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_SubDefinedComponents()
				.compute(value));
	}

	public List<GeneralizableElement> getGeneralizedComponents(Component value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.GeneralizableElementAllSuperGC()
				.compute(value));
	}

	public List<ComponentExchange> getInternalOutgoingComponentExchangesComputed(Component value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentInternalOutgoingComponentExchanges()
						.compute(value));
	}

	public List<ComponentExchange> getOutgoingDelegations(Component value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentOutgoingDelegation()
				.compute(value));
	}

	public List<Interface> getProvidedInterfaces(Component value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentProvidedInterfaces()
				.compute(value));
	}

	public List<CommunicationLink> getCommunicationLink(Component value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentCommunicationLink()
				.compute(value));
	}

	public List<Interface> getUsedInterfaces(Component value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.Component_usedInterfaces().compute(value));
	}

	public List<Interface> getRequiredInterfaces(Component value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentRequiredInterfaces()
				.compute(value));
	}

	public List<ComponentExchange> getOutgoingComponentExchanges(Component value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentOutgoingComponentExchange()
						.compute(value));
	}

	public List<Part> getRepresentingParts(Component value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_representingParts()
				.compute(value));
	}

	public List<Interface> getImplementedInterfaces(Component value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_implementedInterfaces()
				.compute(value));
	}

	public List<ComponentExchange> getIncomingComponentExchanges(Component value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentIncomingComponentExchange()
						.compute(value));
	}

	public List<Scenario> getScenarios(Component value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario()
				.compute(value));
	}

	public List<ComponentPort> getComponentPorts(Component value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.Component_componentPorts().compute(value));
	}

	public List<ComponentExchange> getIncomingDelegations(Component value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentIncomingDelegation()
				.compute(value));
	}

	public List<GeneralizableElement> getGeneralizingComponents(Component value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.GeneralizableElementAllSubGC()
				.compute(value));
	}

	public List<Component> getReferencingComponents(Component value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_referencingComponent()
				.compute(value));
	}

	public List<ComponentExchange> getInternalIncomingComponentExchangesComputed(Component value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentInternalIncomingComponentExchanges()
						.compute(value));
	}

	public List<Entity> getBreakdown(Entity value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalEntity_Breakdown()
				.compute(value));
	}

	public List<Role> getAllocatedRoles(Entity value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalEntityAllocatedRoles()
				.compute(value));
	}

	public List<OperationalActivity> getAllocatedOperationalActivities(Entity value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_AllocatedFunctions()
				.compute(value));
	}

	public List<CommunicationMean> getOutgoingCommunicationMean(Entity value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalEntity_OutgoingCommunicationMean()
						.compute(value));
	}

	public List<CommunicationMean> getIncomingCommunicationMean(Entity value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalEntity_IncomingCommunicationMean()
						.compute(value));
	}

	public List<OperationalCapability> getInvolvingOperationalCapabilities(Entity value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalEntityInvolgingOperationalCap()
						.compute(value));
	}

	public List<Component> getRealizingSystemComponents(Entity value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizingComponents()
				.compute(value));
	}

	public List<CommunicationMean> getRealizedComponentExchanges(ExchangeSpecification value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeSpecification_realizedDataflow()
						.compute(value));
	}

	public List<InformationsExchanger> getTarget(ExchangeSpecification value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeSpecification_dataflowTarget()
						.compute(value));
	}

	public List<InformationsExchanger> getSource(ExchangeSpecification value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeSpecification_dataflowSource()
						.compute(value));
	}

	public List<AbstractType> getRelatedData(ComponentExchange value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeSpecification_relatedData()
				.compute(value));
	}

	public List<EObject> getOwner(ComponentExchange value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeSpecification_owner()
				.compute(value));
	}

	public List<ComponentExchangeCategory> getInheritedCategories(ComponentExchange value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentExchangeCategoriesForDelegations()
						.compute(value));
	}

	public List<FunctionalExchange> getAllocatedFunctionalExchanges(ComponentExchange value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentExchangeAllocatedFunctionalExchanges()
						.compute(value));
	}

	public List<ComponentExchangeCategory> getCategories(ComponentExchange value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentExchangeCategories()
				.compute(value));
	}

	public List<Component> getConnectedComponents(ComponentExchange value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Connection_connectedComponents()
				.compute(value));
	}

	public List<ExchangeItem> getExchangeItems(ComponentExchange value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ConnectionConvoyedInformation()
				.compute(value));
	}

	public List<CommunicationMean> getRealizedCommunicationMean(ComponentExchange value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ComponenExchangeRealizedCommunicationMean()
						.compute(value));
	}

	public List<Part> getConnectedParts(ComponentExchange value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Connection_connectedParts()
				.compute(value));
	}

	public List<ComponentExchange> getRealizingComponentExchanges(ComponentExchange value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeSpecification_realizingDataflow()
						.compute(value));
	}

	public List<PhysicalPath> getAllocatingPhysicalPath(ComponentExchange value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentExchangeAllocatingPhysicalPath()
						.compute(value));
	}

	public List<PhysicalLink> getAllocatingPhysicalLink(ComponentExchange value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentExchangeAllocatingPhysicalLink()
						.compute(value));
	}

	public List<Scenario> getScenarios(ComponentExchange value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario()
				.compute(value));
	}

	public List<FunctionalExchange> getAllocatedInteractions(CommunicationMean value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CommunicationMean_AllocatedExchanges()
						.compute(value));
	}

	public List<InformationsExchanger> getTarget(CommunicationMean value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CommunicationMean_Target().compute(value));
	}

	public List<InformationsExchanger> getSource(CommunicationMean value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CommunicationMean_Source().compute(value));
	}

	public List<InvolvedElement> getInvolvedElement(Involvement value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsInvolvementTarget()
						.compute(value));
	}

	public List<InvolverElement> getInvolvingElement(Involvement value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsInvolvementSource()
						.compute(value));
	}

	public List<FunctionalChain> getOwnedFunctionalChains(PhysicalFunction value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Function_ownedFunctionalChains()
				.compute(value));
	}

	public List<FunctionalExchange> getOutgoingFunctionalExchanges(PhysicalFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_outgoingInteraction()
						.compute(value));
	}

	public List<FunctionOutputPort> getOutFlowPorts(PhysicalFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.Function_outFlowPorts().compute(value));
	}

	public List<LogicalFunction> getRealizedLogicalFunctions(PhysicalFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_realizedFunctions()
						.compute(value));
	}

	public List<FunctionalExchange> getInternalOutgoingFunctionalExchanges(PhysicalFunction value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalOutGoingDataflows()
				.compute(value));
	}

	public List<CapabilityRealization> getInvolvingCapabilityRealizations(PhysicalFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.LogicalAndPhysicalFunctionInvolvingCapabilityRealization()
						.compute(value));
	}

	// TODO AbstractFunctionalBlock and Role
	public List<EObject> getAllocatingPhysicalComponent(PhysicalFunction value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingComponent()
				.compute(value));
	}

	public List<FunctionalExchange> getInternalIncomingFunctionalExchanges(PhysicalFunction value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalInComingDataflows()
				.compute(value));
	}

	public List<FunctionalExchange> getIncomingFunctionalExchanges(PhysicalFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_incomingInteraction()
						.compute(value));
	}

	// TODO Component and Part
	public List<EObject> getAllocatingPhysicalActor(PhysicalFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingActor().compute(value));
	}

	public List<FunctionInputPort> getInFlowPorts(PhysicalFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.Function_inFlowPorts().compute(value));
	}

	public List<CapabilityRealization> getInvolvingCapabilityRealizations(CapabilityRealizationInvolvedElement value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityRealizationInvolvedElement_InvolvingCapabilityRealizations()
						.compute(value));
	}

	public List<Component> getDeployedPhysicalComponents(PhysicalComponent value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalComponent_deployedPhysicalComponents()
						.compute(value));
	}

	public List<PhysicalLink> getIncomingPhysicalLinks(PhysicalComponent value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalComponent_IncomingPhysicalLinks()
						.compute(value));
	}

	public List<PhysicalFunction> getAllocatedPhysicalFunctions(PhysicalComponent value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_AllocatedFunctions()
				.compute(value));
	}

	public List<PhysicalLink> getInternalPhysicalLinksComputed(PhysicalComponent value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalComponent_InternalPhysicalLinks()
						.compute(value));
	}

	public List<LogicalComponent> getRealizedLogicalComponent(PhysicalComponent value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizedComponents()
				.compute(value));
	}

	public List<Component> getRealizingComponents(PhysicalComponent value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizingComponents()
				.compute(value));
	}

	public List<PhysicalLink> getOutgoingPhysicalLinks(PhysicalComponent value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalComponent_OutgoingPhysicalLinks()
						.compute(value));
	}

	public List<PhysicalComponent> getDeployingPhysicalComponents(PhysicalComponent value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalComponent_deployingPhysicalComponents()
						.compute(value));
	}

	public List<ConfigurationItem> getRealizingConfigurationItems(PhysicalComponent value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalArtifactsRealizingCI()
				.compute(value));
	}

	public List<AbstractType> getType(Part value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Part_type().compute(value));
	}

	public List<AbstractDependenciesPkg> getDependencies(AbstractDependenciesPkg value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractDependenciesPkg_dependencies()
						.compute(value));
	}

	public List<AbstractDependenciesPkg> getInverseDependencies(AbstractDependenciesPkg value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractDependenciesPkg_inverse_dependencies()
						.compute(value));
	}

	public List<ExchangeItem> getExchangeItems(Interface value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.InterfaceExchangesItems().compute(value));
	}

	public List<Interface> getRefinedInterfaces(Interface value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Interface_provisionedInterfaces()
				.compute(value));
	}

	public List<ExchangeItem> getInheritedExchangeItems(Interface value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.InterfaceInheritedExchangesItems()
				.compute(value));
	}

	public List<Component> getUsers(Interface value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.InterfaceUsers().compute(value));
	}

	public List<Interface> getRefiningInterfaces(Interface value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Interface_provisioningInterfaces()
				.compute(value));
	}

	public List<Scenario> getInvolvingScenarios(Interface value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Interface_involvingScenarios()
				.compute(value));
	}

	public List<ComponentPort> getProviders(Interface value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.InterfaceProviders().compute(value));
	}

	public List<ComponentPort> getRequirers(Interface value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.InterfaceRequires().compute(value));
	}

	public List<Component> getImplementors(Interface value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.InterfaceImplementors().compute(value));
	}

	public List<Interface> getTarget(InterfaceImplementation value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsInterfaceImplementationTarget()
						.compute(value));
	}

	public List<Component> getSource(InterfaceImplementation value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsInterfaceImplementationSource()
						.compute(value));
	}

	public List<Interface> getTarget(InterfaceUse value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsInterfaceUseTarget()
						.compute(value));
	}

	public List<Component> getSource(InterfaceUse value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsInterfaceUseSource()
						.compute(value));
	}

	public List<AbstractExchangeItem> getExchangeItem(ExchangeItemAllocation value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeItemAllocationExchangeItem()
						.compute(value));
	}

	public List<Scenario> getScenarios(ExchangeItemAllocation value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario()
				.compute(value));
	}

	public List<PhysicalLinkCategory> getInheritedCategories(PhysicalLink value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalLinkCategoriesForDelegations()
						.compute(value));
	}

	public List<TraceableElement> getAllocatedComponentExchanges(PhysicalLink value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalLinksRealizedConnection()
				.compute(value));
	}

	public List<PhysicalLinkCategory> getCategories(PhysicalLink value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalLinkCategories().compute(value));
	}

	public List<Component> getPhysicalLinkEnds(PhysicalLink value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalLinkSourceAndTarget()
				.compute(value));
	}

	public List<ConfigurationItem> getRealizingConfigurationItems(PhysicalLink value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalArtifactsRealizingCI()
				.compute(value));
	}

	public List<PhysicalPath> getPhysicalPaths(PhysicalLink value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalLinksInvolvedInPhysicalPaths()
						.compute(value));
	}

	public List<PhysicalLink> getPhysicalLinks(PhysicalLinkCategory value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CategoryPhysicalLink().compute(value));
	}

	public List<PhysicalPath> getInvolvedPhysicalPaths(PhysicalPath value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPathInvolvedPhysicalPath()
				.compute(value));
	}

	public List<PhysicalLink> getInvolvedPhysicalLinks(PhysicalPath value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPath_PhysicalLinks()
				.compute(value));
	}

	public List<ComponentExchange> getAllocatedComponentExchanges(PhysicalPath value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPath_RealisedConnection()
				.compute(value));
	}

	public List<PhysicalPath> getPhysicalPaths(PhysicalPath value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPathInvolvingPhysicalPath()
				.compute(value));
	}

	public List<PhysicalPath> getInvolvedPhysicalPath(PhysicalPathInvolvement value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPathInvolvmentInvolvedPhysicalPath()
						.compute(value));
	}

	public List<AbstractPathInvolvedElement> getInvolvedPhysicalComponent(PhysicalPathInvolvement value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPathInvolvmentPhysicalComp()
						.compute(value));
	}

	public List<PhysicalLink> getInvolvedPhysicalLink(PhysicalPathInvolvement value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPathInvolvmentPhysicalLink()
						.compute(value));
	}

	public List<FunctionPort> getAllocatedFunctionPorts(PhysicalPort value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPortOutgoingFunctionPorts()
				.compute(value));
	}

	public List<ComponentPort> getAllocatedComponentPorts(PhysicalPort value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPortAllocatedComponentPorts()
						.compute(value));
	}

	public List<ComponentExchange> getOutgoingDelegations(PhysicalPort value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPortOutgoingDelgations()
				.compute(value));
	}

	public List<PhysicalLink> getPhysicalLinks(PhysicalPort value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPortIncomingPhysicalLinks()
				.compute(value));
	}

	public List<ConfigurationItem> getRealizingConfigurationItems(PhysicalPort value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalArtifactsRealizingCI()
				.compute(value));
	}

	public List<FunctionalChain> getOwnedFunctionalChains(LogicalFunction value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Function_ownedFunctionalChains()
				.compute(value));
	}

	public List<FunctionalExchange> getOutgoingFunctionalExchanges(LogicalFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_outgoingInteraction()
						.compute(value));
	}

	public List<FunctionOutputPort> getOutFlowPorts(LogicalFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.Function_outFlowPorts().compute(value));
	}

	public List<FunctionalExchange> getInternalOutgoingFunctionalExchanges(LogicalFunction value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalOutGoingDataflows()
				.compute(value));
	}

	public List<SystemFunction> getRealizedSystemFunctions(LogicalFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_realizedFunctions()
						.compute(value));
	}

	public List<PhysicalFunction> getRealizingPhysicalFunctions(LogicalFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_realizingFunctions()
						.compute(value));
	}

	// TODO Component and Part
	public List<EObject> getAllocatingLogicalActor(LogicalFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingActor().compute(value));
	}

	public List<FunctionalExchange> getInternalIncomingFunctionalExchanges(LogicalFunction value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalInComingDataflows()
				.compute(value));
	}

	public List<FunctionInputPort> getInFlowPorts(LogicalFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.Function_inFlowPorts().compute(value));
	}

	// TODO Component and Part
	public List<EObject> getAllocatingLogicalComponent(LogicalFunction value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingComponent()
				.compute(value));
	}

	public List<CapabilityRealization> getInvolvingCapabilityRealizations(LogicalFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.LogicalAndPhysicalFunctionInvolvingCapabilityRealization()
						.compute(value));
	}

	public List<FunctionalExchange> getIncomingFunctionalExchanges(LogicalFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_incomingInteraction()
						.compute(value));
	}

	public List<SystemComponent> getRealizedSystemComponents(LogicalComponent value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizedComponents()
				.compute(value));
	}

	public List<LogicalFunction> getAllocatedLogicalFunctions(LogicalComponent value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_AllocatedFunctions()
				.compute(value));
	}

	public List<PhysicalComponent> getRealizingPhysicalComponents(LogicalComponent value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizingComponents()
				.compute(value));
	}

	public List<FunctionalChain> getOwnedFunctionalChains(CapabilityRealization value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityOwnedFunctionalChains()
						.compute(value));
	}

	public List<FunctionalChain> getInvolvedFunctionalChains(CapabilityRealization value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityInvolvedFunctionalChains()
						.compute(value));
	}

	public List<Capability> getRealizedCapabilities(CapabilityRealization value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityRealization_RealizedCapability()
						.compute(value));
	}

	public List<CapabilityRealization> getRealizedCapabilityRealizations(CapabilityRealization value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityRealization_RealizedCapabilityRealization()
						.compute(value));
	}

	public List<LogicalFunction> getInvolvedLogicalFunctions(CapabilityRealization value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.LAAbstractCapabilityInvolvedFunctions()
						.compute(value));
	}

	public List<PhysicalFunction> getInvolvedPhysicalFunctions(CapabilityRealization value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.PAAbstractCapabilityInvolvedFunctions()
						.compute(value));
	}

	public List<CapabilityRealization> getRealizingCapabilityRealizations(CapabilityRealization value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityRealization_RealizingCapabilityRealization()
						.compute(value));
	}

	public List<String> getValue(DataValue value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.PropertyValue_applying_valued_element_DataValue()
						.compute(value));
	}

	public List<Property> getReferencedProperty(DataValue value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.DataValueRefReferencedProperty()
				.compute(value));
	}

	public List<DataValue> getReferencedValue(DataValue value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.DataValueRefReferencedValue()
				.compute(value));
	}

	public List<Object> getReferencingValue(DataValue value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.DataValueReferencingReferencedValue()
						.compute(value));
	}

	public List<TraceableElement> getTarget(Trace value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsTraceTarget()
				.compute(value));
	}

	public List<TraceableElement> getSource(Trace value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsTraceSource()
				.compute(value));
	}

	public List<TraceableElement> getTracedElements(Requirement value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.RequirementTracedElements()
				.compute(value));
	}

	public List<FunctionalExchange> getFunctionalExchanges(ExchangeCategory value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CategoryFunctionalExchange()
				.compute(value));
	}

	public List<FunctionalChainInvolvementFunction> getInvolvedFunctions(FunctionalChainReference value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvementFunctions()
						.compute(value));
	}

	public List<FunctionalChainReference> getInvolvedFunctionalChains(FunctionalChainReference value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainChildren().compute(value));
	}

	public List<FunctionalChainInvolvementLink> getInvolvementLinks(FunctionalChainReference value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvementLinks()
				.compute(value));
	}

	public List<FunctionalChainReference> getParentFunctionalChains(FunctionalChainReference value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainParent().compute(value));
	}

	public List<FunctionPort> getRealizedFunctionPorts(FunctionPort value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionPortRealizedFunctionPort()
				.compute(value));
	}

	public List<ExchangeItem> getExchangeItems(FunctionPort value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionPortAllocatedExchangeItems()
						.compute(value));
	}

	public List<FunctionPort> getRealizingFunctionPorts(FunctionPort value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionPortRealizingFunctionPort()
				.compute(value));
	}

	public List<ComponentPort> getAllocatingComponentPorts(FunctionPort value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionPortAllocatingCompoentPort()
						.compute(value));
	}

	public List<AbstractType> getType(Pin value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Pin_type().compute(value));
	}

	public List<EObject> getOwner(Pin value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Pin_owner().compute(value));
	}

	public List<EObject> getRealizedPin(Pin value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.Pin_realizedFlowPort().compute(value));
	}

	public List<FunctionalExchange> getOutgoingFunctionalExchanges(Pin value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.Pin_outgoingDataflows().compute(value));
	}

	public List<Object> getIncomingFunctionalExchanges(Pin value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.Pin_incomingDataflows().compute(value));
	}

	public List<EObject> getRealizingPin(Pin value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.Pin_realizingFlowPort().compute(value));
	}

	public List<Type> getRelatedData(FunctionalExchange value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchange_relatedData()
				.compute(value));
	}

	public List<EObject> getOwner(FunctionalExchange value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchange_owner().compute(value));
	}

	public List<ExchangeItem> getExchangeItems(FunctionalExchange value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeExchangesItems()
				.compute(value));
	}

	public List<ActivityNode> getTarget(FunctionalExchange value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchange_dataflowTarget()
				.compute(value));
	}

	public List<FunctionalExchange> getRealizedFunctionalExchange(FunctionalExchange value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeRealizedFunctionalExchanges()
						.compute(value));
	}

	public List<TraceableElement> getRealizedInteractions(FunctionalExchange value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeRealizedInteractions()
						.compute(value));
	}

	public List<ExchangeCategory> getCategories(FunctionalExchange value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeCategory()
				.compute(value));
	}

	public List<FunctionalExchange> getRealizingFunctionalExchanges(FunctionalExchange value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchange_realizingDataflow()
						.compute(value));
	}

	public List<FunctionalChain> getFunctionalChains(FunctionalExchange value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeFunctionalChain()
				.compute(value));
	}

	public List<Scenario> getScenarios(FunctionalExchange value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario()
				.compute(value));
	}

	public List<ActivityNode> getSource(FunctionalExchange value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchange_dataflowSource()
				.compute(value));
	}

	public List<CommunicationMean> getAllocatingCommunicationMean(FunctionalExchange value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeAllocatingCommunicationMean()
						.compute(value));
	}

	public List<ComponentExchange> getAllocatingComponentExchange(FunctionalExchange value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeAllocatingComponentExchange()
						.compute(value));
	}

	public List<OperationalProcess> getOperationalProcesses(FunctionalExchange value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeOperationalProcess()
						.compute(value));
	}

	public List<ComponentExchange> getComponentExchanges(ComponentExchangeCategory value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CategoryComponentExchange()
				.compute(value));
	}

	public List<Object> getType(ComponentPort value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_type().compute(value));
	}

	public List<EObject> getOwner(ComponentPort value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_owner().compute(value));
	}

	public List<Interface> getProvidedInterfaces(ComponentPort value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_providedInterfaces()
				.compute(value));
	}

	public List<Interface> getRequiredInterfaces(ComponentPort value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_requiredInterfaces()
				.compute(value));
	}

	public List<ComponentPort> getRealizedComponentPorts(ComponentPort value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_realizedComponentPort()
						.compute(value));
	}

	public List<Port> getAllocatedFunctionPorts(ComponentPort value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_realizedFunctionPort()
						.compute(value));
	}

	public List<ComponentExchange> getOutgoingDelegations(ComponentPort value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPortOutgoingDeletations()
				.compute(value));
	}

	public List<ComponentExchange> getOutgoingComponentExchanges(ComponentPort value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPortOutgoingComponentExchanges()
						.compute(value));
	}

	public List<PhysicalPort> getAllocatingPhysicalPorts(ComponentPort value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPortAllocatingPhysicalPorts()
						.compute(value));
	}

	public List<ComponentExchange> getIncomingComponentExchanges(ComponentPort value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPortIncomingComponentExchanges()
						.compute(value));
	}

	public List<Port> getRealizingComponentPorts(ComponentPort value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_realizingComponentPort()
						.compute(value));
	}

	public List<ComponentExchange> getIncomingDelegations(ComponentPort value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPortIncomingDeletations()
				.compute(value));
	}

	public List<FunctionalChainReference> getSourceReferenceHierachy(ReferenceHierarchyContext value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ReferenceHierarchyContextSource()
				.compute(value));
	}

	public List<FunctionalChainReference> getTargetReferenceHierachy(ReferenceHierarchyContext value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ReferenceHierarchyContextTarget()
				.compute(value));
	}

	public List<Constraint> getExchangeContext(FunctionalChainInvolvementLink value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvmentLinkExchangeContext()
						.compute(value));
	}

	public List<FunctionalChainInvolvementFunction> getTarget(FunctionalChainInvolvementLink value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvmentLinkTarget()
						.compute(value));
	}

	public List<FunctionalChainInvolvementFunction> getSource(FunctionalChainInvolvementLink value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvmentLinkSource()
						.compute(value));
	}

	public List<Constraint> getCondition(SequenceLink value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkCondition().compute(value));
	}

	public List<EObject> getOwner(SequenceLink value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementOwner().compute(value));
	}

	public List<FunctionalChainInvolvementLink> getLinks(SequenceLink value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkLinks().compute(value));
	}

	public List<ControlNode> getTargetControlNode(SequenceLink value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkTargetControlNode()
				.compute(value));
	}

	public List<FunctionalChainInvolvementFunction> getTargetInvolvementFunction(SequenceLink value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkTargetInvolvementFunction()
						.compute(value));
	}

	public List<FunctionalChainInvolvementFunction> getSourceInvolvementFunction(SequenceLink value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkSourceInvolvementFunction()
						.compute(value));
	}

	public List<ControlNode> getSourceControlNode(SequenceLink value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkSourceControlNode()
				.compute(value));
	}

	public List<SequenceLink> getTargetSequenceLinks(SequenceLinkEnd value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkEndTargetSequenceLinks()
						.compute(value));
	}

	public List<SequenceLink> getSourceSequenceLinks(SequenceLinkEnd value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkEndSourceSequenceLinks()
						.compute(value));
	}

	public List<EObject> getOwner(FunctionalChainInvolvementFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementOwner().compute(value));
	}

	public List<FunctionalChainInvolvement> getOutgoingInvolvementLinks(FunctionalChainInvolvementFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvementFunctionOutgoingInvolvementLinks()
						.compute(value));
	}

	public List<FunctionalChainInvolvement> getIncomingInvolvementLinks(FunctionalChainInvolvementFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvementFunctionIncomingInvolvementLinks()
						.compute(value));
	}

	public List<State> getParentStateAndMode(IState value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.State_ParentState().compute(value));
	}

	public List<IState> getInvolvedStatesAndModes(IState value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.State_InvolvedStates().compute(value));
	}

	public List<AbstractState> getOwnedStateAndMode(IState value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.State_OwnedStates().compute(value));
	}

	public List<StateTransition> getOutgoingTransition(IState value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.StateAndModeOutGoingTransition()
				.compute(value));
	}

	public List<StateTransition> getIncomingTransition(IState value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.StateAndModeInComingTransition()
				.compute(value));
	}

	public List<IState> getInvolvingStatesAndModes(IState value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.State_InvolvingStates().compute(value));
	}

	public List<Pseudostate> getOwnedEntryExitPoints(Region value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.State_OwnedEntryExitPoints()
				.compute(value));
	}

	public List<IState> getRealizedMode(AbstractState value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateRealizedMode()
				.compute(value));
	}

	public List<IState> getRealizedState(AbstractState value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateRealizedState()
				.compute(value));
	}

	public List<IState> getRealizingState(AbstractState value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateRealizingState()
				.compute(value));
	}

	public List<IState> getRealizingMode(AbstractState value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateRealizingMode()
				.compute(value));
	}

	public List<AbstractEvent> getDoActivity(State value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateModeDoActivity()
				.compute(value));
	}

	// TODO AbstractFunction and AbstractCapability and OperationalCapability and
	// FunctionalChain
	public List<EObject> getActiveElements(State value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateAvailableElements()
				.compute(value));
	}

	public List<Scenario> getScenarios(State value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario()
				.compute(value));
	}

	// TODO AbstractFunction and AbstractCapability and OperationalCapability and
	// FunctionalChain
	public List<EObject> getActiveElementsComputed(State value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateParentActiveElements()
				.compute(value));
	}

	public List<AbstractEvent> getEffect(StateTransition value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.StateTransitionEffect().compute(value));
	}

	public List<IState> getTarget(StateTransition value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.StateTransitionOutGoingIState()
				.compute(value));
	}

	public List<AbstractEvent> getTrigger(StateTransition value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.StateTransitionTrigger().compute(value));
	}

	public List<IState> getSource(StateTransition value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.StateTransitionInComingIState()
				.compute(value));
	}

	public List<Region> getParentRegion(Pseudostate value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.EntryExitPoint_ParentRegion()
				.compute(value));
	}

	public List<FunctionalChain> getOwnedFunctionalChains(SystemFunction value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Function_ownedFunctionalChains()
				.compute(value));
	}

	public List<OperationalActivity> getRealizedOperationalActivities(SystemFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_realizedFunctions()
						.compute(value));
	}

	public List<FunctionalExchange> getInternalOutgoingFunctionalExchanges(SystemFunction value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalOutGoingDataflows()
				.compute(value));
	}

	public List<FunctionOutputPort> getOutFlowPorts(SystemFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.Function_outFlowPorts().compute(value));
	}

	public List<FunctionalExchange> getOutgoingFunctionalExchanges(SystemFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_outgoingInteraction()
						.compute(value));
	}

	public List<FunctionalExchange> getIncomingFunctionalExchanges(SystemFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_incomingInteraction()
						.compute(value));
	}

	public List<FunctionalExchange> getInternalIncomingFunctionalExchanges(SystemFunction value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalInComingDataflows()
				.compute(value));
	}

	public List<LogicalFunction> getRealizingLogicalFunctions(SystemFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_realizingFunctions()
						.compute(value));
	}

	// TODO AbstractFunctionalBlock and Role
	public List<EObject> getAllocatingActor(SystemFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingActor().compute(value));
	}

	public List<FunctionInputPort> getInFlowPorts(SystemFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.Function_inFlowPorts().compute(value));
	}

	public List<Capability> getInvolvingCapabilities(SystemFunction value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.SystemFunctionInvolvingCapabilities()
						.compute(value));
	}

	// TODO AbstractFunctionalBlock and Role
	public List<EObject> getAllocatingSystem(SystemFunction value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingComponent()
				.compute(value));
	}

	public List<Capability> getExploitedCapabilities(Mission value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Mission_ExploitedCapabilities()
				.compute(value));
	}

	public List<SystemComponent> getInvolvedSystemComponents(Mission value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Mission_InvolvedSystemComponents()
				.compute(value));
	}

	public List<FunctionalChain> getOwnedFunctionalChains(Capability value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityOwnedFunctionalChains()
						.compute(value));
	}

	public List<OperationalCapability> getRealizedOperationalCapabilities(Capability value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityRealizedOC().compute(value));
	}

	public List<SystemFunction> getInvolvedSystemFunctions(Capability value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityInvolvedFunctions()
						.compute(value));
	}

	public List<FunctionalChain> getInvolvedFunctionalChains(Capability value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityInvolvedFunctionalChains()
						.compute(value));
	}

	public List<Mission> getExploitingMissions(Capability value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Capability_purposeMissions()
				.compute(value));
	}

	public List<CapabilityRealization> getRealizingCapabilityRealizations(Capability value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityRealizingCR().compute(value));
	}

	public List<AbstractCapability> getInvolvedElement(CapabilityExploitation value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsCapabilityExploitationTarget()
						.compute(value));
	}

	public List<Mission> getSource(CapabilityExploitation value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsCapabilityExploitationSource()
						.compute(value));
	}

	public List<SystemFunction> getAllocatedSystemFunctions(SystemComponent value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_AllocatedFunctions()
				.compute(value));
	}

	public List<Component> getRealizedOperationalEntities(SystemComponent value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizedComponents()
				.compute(value));
	}

	public List<LogicalComponent> getRealizingLogicalComponents(SystemComponent value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizingComponents()
				.compute(value));
	}

	public List<Capability> getInvolvingCapabilities(SystemComponent value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.SystemComponent_InvolvingCapabilities()
						.compute(value));
	}

	public List<Mission> getInvolvingMissions(SystemComponent value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SystemComponent_InvolvingMissions()
				.compute(value));
	}

	public List<ModelElement> getConstrainedElements(Constraint value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ConstraintModelElements().compute(value));
	}

	public List<GeneralizableElement> getTarget(Generalization value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsGeneralizationTarget()
						.compute(value));
	}

	public List<GeneralizableElement> getSource(Generalization value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsGeneralizationSource()
						.compute(value));
	}

	public List<AbstractPropertyValue> getValue(AbstractPropertyValue value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.PropertyValue_applying_valued_element_Primitive()
						.compute(value));
	}

	public List<CapellaElement> getValuedElements(AbstractPropertyValue value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.PropertyValue_applying_valued_element()
						.compute(value));
	}

	public List<CapellaElement> getValuedElements(PropertyValueGroup value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.PropertyValueGroup_applying_valued_element()
						.compute(value));
	}

	public List<EObject> getReferencedElement(CatalogElementLink value) {
		return castList(
				new org.polarsys.capella.common.re.ui.queries.CatalogElementLinkReferencedElement().compute(value));
	}

	public List<EObject> getRelatedElements(CatalogElement value) {
		return castList(
				new org.polarsys.capella.common.re.ui.queries.CatalogElementRelatedSemanticElements().compute(value));
	}

	public List<CatalogElement> getRelatedReplicableElements(CatalogElement value) {
		return castList(new org.polarsys.capella.common.re.ui.queries.CatalogElementRelatedReplicas().compute(value));
	}

	public List<CatalogElement> getRPL(CatalogElement value) {
		return castList(new org.polarsys.capella.common.re.ui.queries.CatalogElementReverseOrigin().compute(value));
	}

	public List<PhysicalComponent> getRealizedPhysicalComponents(ConfigurationItem value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CIRealizedPhysicalComponents()
				.compute(value));
	}

	public List<PhysicalLink> getRealizedPhysicalLinks(ConfigurationItem value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CIRealizedPhysicalLinks().compute(value));
	}

	public List<PhysicalPort> getRealizedPhysicalPorts(ConfigurationItem value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CIRealizedPhysicalPorts().compute(value));
	}

	public List<AbstractExchangeItem> getTarget(CommunicationLink value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CommunicationLinkExchangeItem()
				.compute(value));
	}

	public List<Component> getSource(CommunicationLink value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CommunicationLinkComponent()
				.compute(value));
	}

	public List<EObject> getParentScenario(SequenceMessage value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessage_parentScenario()
				.compute(value));
	}

	public List<AbstractEventOperation> getInvokedExchangeItemAllocation(SequenceMessage value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageInvokedExchangeItemAllocation()
						.compute(value));
	}

	public List<SequenceMessage> getRefinedSequenceMessage(SequenceMessage value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessage_refiningSequenceMessage()
						.compute(value));
	}

	public List<AbstractEventOperation> getInvokedComponentExchange(SequenceMessage value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageInvokedComponentExchange()
						.compute(value));
	}

	public List<AbstractEventOperation> getInvokedCommunicationMean(SequenceMessage value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageInvokedCommunicationMean()
						.compute(value));
	}

	public List<ExchangeItem> getExchangeItems(SequenceMessage value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageExchangeItems()
				.compute(value));
	}

	public List<AbstractEventOperation> getInvokedInteraction(SequenceMessage value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageInvokedInteraction()
				.compute(value));
	}

	public List<AbstractEventOperation> getInvokedFunctionalExchange(SequenceMessage value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessage_AllocatedFunctionalExchange()
						.compute(value));
	}

	public List<AbstractFunction> getFunctionTarget(SequenceMessage value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageFunctionTarget()
				.compute(value));
	}

	public List<Part> getPartTarget(SequenceMessage value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessagePartTarget()
				.compute(value));
	}

	public List<AbstractEventOperation> getInvokedOperation(SequenceMessage value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessage_invokedOperation()
				.compute(value));
	}

	public List<Part> getPartSource(SequenceMessage value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessagePartSource()
				.compute(value));
	}

	public List<SequenceMessage> getRefiningSequenceMessage(SequenceMessage value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessage_refinedSequenceMessage()
						.compute(value));
	}

	public List<AbstractFunction> getFunctionSource(SequenceMessage value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageFunctionSource()
				.compute(value));
	}

	public List<EObject> getParent(Scenario value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ItemQuery_Scenario_getAbstractCapabilityContainer()
						.compute(value));
	}

	public List<Scenario> getReferencedScenario(Scenario value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ItemQuery_Scenario_getReferencedScenarios()
						.compute(value));
	}

	public List<Scenario> getRealizedScenarios(Scenario value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Scenario_realizedScenario()
				.compute(value));
	}

	public List<Scenario> getRefinedScenarios(Scenario value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Scenario_refiningScenarios()
				.compute(value));
	}

	public List<Scenario> getRefiningScenarios(Scenario value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Scenario_refinedScenarios()
				.compute(value));
	}

	public List<Scenario> getRealizingScenarios(Scenario value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Scenario_realizingScenario()
				.compute(value));
	}

	public List<Scenario> getParentScenario(InstanceRole value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.InstanceRole_parentScenario()
				.compute(value));
	}

	public List<AbstractType> getRepresentedInstance(InstanceRole value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.InstanceRole_representedInstance()
				.compute(value));
	}

	public List<AbstractCapability> getTarget(AbstractCapabilityExtend value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAbstractCapabilityExtendTarget()
						.compute(value));
	}

	public List<AbstractCapability> getSource(AbstractCapabilityExtend value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAbstractCapabilityExtendSource()
						.compute(value));
	}

	public List<AbstractCapability> getTarget(AbstractCapabilityGeneralization value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAbstractCapabilityGeneralizationTarget()
						.compute(value));
	}

	public List<AbstractCapability> getSource(AbstractCapabilityGeneralization value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAbstractCapabilityGeneralizationSource()
						.compute(value));
	}

	public List<AbstractCapability> getTarget(AbstractCapabilityInclude value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAbstractCapabilityIncludeTarget()
						.compute(value));
	}

	public List<Object> getSource(AbstractCapabilityInclude value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAbstractCapabilityIncludeSource()
						.compute(value));
	}

	public List<Scenario> getReferencedScenario(InteractionUse value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.InteractionUseReferencedScenario()
				.compute(value));
	}

	public List<Scenario> getReferencingScenario(InteractionUse value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ItemQuery_Scenario_getReferencingScenarios()
						.compute(value));
	}

	public List<AbstractFunction> getRelatedFunction(StateFragment value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.StateFragmentRelatedFunctions()
				.compute(value));
	}

	public List<AbstractState> getRelatedState(StateFragment value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.StateFragmentRelatedStates()
				.compute(value));
	}

	public List<Property> getRoles(Association value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAssociationRoles()
						.compute(value));
	}

	public List<org.polarsys.capella.core.data.information.Class> getRealizedClasses(
			org.polarsys.capella.core.data.information.Class value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ClassRealizedClass().compute(value));
	}

	public List<org.polarsys.capella.core.data.information.Class> getRealizingClasses(
			org.polarsys.capella.core.data.information.Class value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ClassRealizingClass().compute(value));
	}

	public List<Type> getType(Collection value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CollectionType().compute(value));
	}

	public List<Scenario> getScenarios(Operation value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario()
				.compute(value));
	}

	public List<Type> getType(Parameter value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Parameter_Type().compute(value));
	}

	public List<ExchangeItem> getRealizedExchangeItems(ExchangeItem value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeItem_realizedEI().compute(value));
	}

	public List<ExchangeItemElement> getExchangeItemElements(ExchangeItem value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangesItemExchangeItemElements()
				.compute(value));
	}

	public List<ExchangeItem> getRealizingExchangeItems(ExchangeItem value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeItem_realizingEI().compute(value));
	}

	public List<FunctionOutputPort> getAllocatingFunctionOutputPorts(ExchangeItem value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeItemAllocatingOutPutFunctionPorts()
						.compute(value));
	}

	public List<FunctionInputPort> getAllocatingFunctionInputPorts(ExchangeItem value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeItemAllocatingInputFunctionPorts()
						.compute(value));
	}

	// TODO ExchangeItemAllocation and ExchangeItemAllocation
	public List<EObject> getInterfaces(ExchangeItem value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangesItemExchangeItemAllocations()
						.compute(value));
	}

	// TODO ExchangeItemAllocation and CommunicationLink
	public List<EObject> getCommunicationLinks(ExchangeItem value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangesItemCommLink().compute(value));
	}

	// AbstractInformationFlow and FunctionalExchange
	public List<Object> getAllocatingExchanges(ExchangeItem value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.EIActiveInConnectionsAndExchanges()
				.compute(value));
	}

	public List<AbstractType> getType(ExchangeItemElement value) {
		return castList(
				new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeItemElementType().compute(value));
	}

	public List<Scenario> getScenarios(ExchangeItemInstance value) {
		return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario()
				.compute(value));
	}

}
