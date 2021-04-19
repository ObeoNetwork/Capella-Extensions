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

import org.eclipse.acceleo.annotations.api.documentation.Documentation;
import org.eclipse.acceleo.annotations.api.documentation.Example;
import org.eclipse.acceleo.annotations.api.documentation.Param;
import org.eclipse.acceleo.annotations.api.documentation.ServiceProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.description.DAnnotation;
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
 */
//@formatter:off
@ServiceProvider(
value = "Services available for the Capella semantic browser."
)
//@formatter:on
public class SemanticBrowserServices {

    @SuppressWarnings("unchecked")
    private <T> List<T> castList(List<Object> list) {
        final List<T> res = new ArrayList<T>();

        for (Object element : list) {
            res.add((T) element);
        }

        return res;
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of REC source element for the given Element.",
        params = {
            @Param(name = "value", value = "the Element")
        },
        result = "the Sequence of REC source element for the given Element",
        examples = {
            @Example(expression = "myElement.getRECSourceElement()", result = "the Sequence of REC source element for the given Element"),
        }
    )
    // @formatter:on
    public List<EObject> getRECSourceElement(Element value) {
        return castList(
                new org.polarsys.capella.common.re.ui.queries.ReferencingReplicableElementLinks().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of REC for the given Element.",
        params = {
            @Param(name = "value", value = "the Element")
        },
        result = "the Sequence of REC for the given Element",
        examples = {
            @Example(expression = "myElement.getREC()", result = "the Sequence of REC for the given Element"),
        }
    )
    // @formatter:on
    public List<CatalogElement> getREC(Element value) {
        return castList(new org.polarsys.capella.common.re.ui.queries.CatalogElementOrigin().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of RPL for the given Element.",
        params = {
            @Param(name = "value", value = "the Element")
        },
        result = "the Sequence of RPL for the given Element",
        examples = {
            @Example(expression = "myElement.getRPL()", result = "the Sequence of RPL for the given Element"),
        }
    )
    // @formatter:on
    public List<CatalogElement> getRPL(Element value) {
        return castList(new org.polarsys.capella.common.re.ui.queries.ReferencingReplicas().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of expressions for the given ModelElement.",
        params = {
            @Param(name = "value", value = "the ModelElement")
        },
        result = "the Sequence of expression for the given ModelElement",
        examples = {
            @Example(expression = "myModelElement.getExpression()", result = "the Sequence of expression for the given ModelElement"),
        }
    )
    // @formatter:on
    public List<ValueSpecification> getExpression(ModelElement value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.OwnedSpecification().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of guards for the given ModelElement.",
        params = {
            @Param(name = "value", value = "the ModelElement")
        },
        result = "the Sequence of guards for the given ModelElement",
        examples = {
            @Example(expression = "myModelElement.getGuard()", result = "the Sequence of guard for the given ModelElement"),
        }
    )
    // @formatter:on
    public List<AbstractConstraint> getGuard(ModelElement value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ModelElementGuard().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of post consition for the given ModelElement.",
        params = {
            @Param(name = "value", value = "the ModelElement")
        },
        result = "the Sequence of post consition for the given ModelElement",
        examples = {
            @Example(expression = "myModelElement.getPostCondition()", result = "the Sequence of post consition for the given ModelElement"),
        }
    )
    // @formatter:on
    public List<AbstractConstraint> getPostCondition(ModelElement value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ModelElementPostCondition()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of exchange context for the given ModelElement.",
        params = {
            @Param(name = "value", value = "the ModelElement")
        },
        result = "the Sequence of exchange context for the given ModelElement",
        examples = {
            @Example(expression = "myModelElement.getExchangeContext()", result = "the Sequence of exchange context for the given ModelElement"),
        }
    )
    // @formatter:on
    public List<AbstractConstraint> getExchangeContext(ModelElement value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ModelElementExchangeContext()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of pre conditions for the given ModelElement.",
        params = {
            @Param(name = "value", value = "the ModelElement")
        },
        result = "the Sequence of pre conditions for the given ModelElement",
        examples = {
            @Example(expression = "myModelElement.getPreCondition()", result = "the Sequence of pre conditions for the given ModelElement"),
        }
    )
    // @formatter:on
    public List<AbstractConstraint> getPreCondition(ModelElement value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ModelElementPreCondition().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of constraining elements for the given ModelElement.",
        params = {
            @Param(name = "value", value = "the ModelElement")
        },
        result = "the Sequence of constraining elements for the given ModelElement",
        examples = {
            @Example(expression = "myModelElement.getConstrainingElements()", result = "the Sequence of constraining elements for the given ModelElement"),
        }
    )
    // @formatter:on
    public List<AbstractConstraint> getConstrainingElements(ModelElement value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ModelElementConstraints().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of outgoing generic traces for the given TraceableElement.",
        params = {
            @Param(name = "value", value = "the TraceableElement")
        },
        result = "the Sequence of outgoing generic traces for the given TraceableElement",
        examples = {
            @Example(expression = "myTraceableElement.getOutgoingGenericTraces()", result = "the Sequence of outgoing generic traces for the given TraceableElement"),
        }
    )
    // @formatter:on
    public List<TraceableElement> getOutgoingGenericTraces(TraceableElement value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.TraceableElementOutgoingTrace()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of incoming generic traces for the given TraceableElement.",
        params = {
            @Param(name = "value", value = "the TraceableElement")
        },
        result = "the Sequence of incoming generic traces for the given TraceableElement",
        examples = {
            @Example(expression = "myTraceableElement.getIncomingGenericTraces()", result = "the Sequence of incoming generic traces for the given TraceableElement"),
        }
    )
    // @formatter:on
    public List<TraceableElement> getIncomingGenericTraces(TraceableElement value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.TraceableElementIncomingTrace()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of applied property value groups for the given CapellaElement.",
        params = {
            @Param(name = "value", value = "the CapellaElement")
        },
        result = "the Sequence of applied property value groups for the given CapellaElement",
        examples = {
            @Example(expression = "myCapellaElement.getAppliedPropertyValueGroups()", result = "the Sequence of applied property value groups for the given CapellaElement"),
        }
    )
    // @formatter:on
    public List<PropertyValueGroup> getAppliedPropertyValueGroups(CapellaElement value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElement_applied_property_value_groups()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of requirements for the given CapellaElement.",
        params = {
            @Param(name = "value", value = "the CapellaElement")
        },
        result = "the Sequence of requirements for the given CapellaElement",
        examples = {
            @Example(expression = "myCapellaElement.getRequirements()", result = "the Sequence of requirements for the given CapellaElement"),
        }
    )
    // @formatter:on
    public List<TraceableElement> getRequirements(CapellaElement value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElement_requirement()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of applied property values for the given CapellaElement.",
        params = {
            @Param(name = "value", value = "the CapellaElement")
        },
        result = "the Sequence of applied property values for the given CapellaElement",
        examples = {
            @Example(expression = "myCapellaElement.getAppliedPropertyValues()", result = "the Sequence of applied property values for the given CapellaElement"),
        }
    )
    // @formatter:on
    public List<AbstractPropertyValue> getAppliedPropertyValues(CapellaElement value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElement_applied_property_values()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of parents for the given Property.",
        params = {
            @Param(name = "value", value = "the Property")
        },
        result = "the Sequence of parents for the given Property",
        examples = {
            @Example(expression = "myProperty.getParent()", result = "the Sequence of parents for the given Property"),
        }
    )
    // @formatter:on
    public List<EObject> getParent(Property value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PropertyOwner().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of associations for the given Property.",
        params = {
            @Param(name = "value", value = "the Property")
        },
        result = "the Sequence of associations for the given Property",
        examples = {
            @Example(expression = "myProperty.getAssociation()", result = "the Sequence of associations for the given Property"),
        }
    )
    // @formatter:on
    public List<Association> getAssociation(Property value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.PropertyAssociation().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of types for the given Property.",
        params = {
            @Param(name = "value", value = "the Property")
        },
        result = "the Sequence of types for the given Property",
        examples = {
            @Example(expression = "myProperty.getType()", result = "the Sequence of types for the given Property"),
        }
    )
    // @formatter:on
    public List<Type> getType(Property value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PropertyType().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of inherited of typing elements for the given AbstractType.",
        params = {
            @Param(name = "value", value = "the AbstractType")
        },
        result = "the Sequence of inherited of typing elements for the given AbstractType",
        examples = {
            @Example(expression = "myAbstractType.getInheritedOfTypingElements()", result = "the Sequence of inherited of typing elements for the given AbstractType"),
        }
    )
    // @formatter:on
    public List<AbstractTypedElement> getInheritedOfTypingElements(AbstractType value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractTypeAbstractTypedElement()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of parents for the given AbstractFunction.",
        params = {
            @Param(name = "value", value = "the AbstractFunction")
        },
        result = "the Sequence of parents for the given AbstractFunction",
        examples = {
            @Example(expression = "myAbstractFunction.getParent()", result = "the Sequence of parents for the given AbstractFunction"),
        }
    )
    // @formatter:on
    public List<AbstractFunction> getParent(AbstractFunction value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_parentFunction()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of breakdown for the given AbstractFunction.",
        params = {
            @Param(name = "value", value = "the AbstractFunction")
        },
        result = "the Sequence of breakdown for the given AbstractFunction",
        examples = {
            @Example(expression = "myAbstractFunction.getBreakdown()", result = "the Sequence of breakdown for the given AbstractFunction"),
        }
    )
    // @formatter:on
    public List<AbstractFunction> getBreakdown(AbstractFunction value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Function_functionBreakdown()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of active modes for the given AbstractFunction.",
        params = {
            @Param(name = "value", value = "the AbstractFunction")
        },
        result = "the Sequence of active modes for the given AbstractFunction",
        examples = {
            @Example(expression = "myAbstractFunction.getActiveInModes()", result = "the Sequence of active modes for the given AbstractFunction"),
        }
    )
    // @formatter:on
    public List<Mode> getActiveInModes(AbstractFunction value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_activeInModes()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of active states for the given AbstractFunction.",
        params = {
            @Param(name = "value", value = "the AbstractFunction")
        },
        result = "the Sequence of active states for the given AbstractFunction",
        examples = {
            @Example(expression = "myAbstractFunction.getActiveInStates()", result = "the Sequence of active states for the given AbstractFunction"),
        }
    )
    // @formatter:on
    public List<Mode> getActiveInStates(AbstractFunction value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_activeInStates()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of functional chains for the given AbstractFunction.",
        params = {
            @Param(name = "value", value = "the AbstractFunction")
        },
        result = "the Sequence of functional chains for the given AbstractFunction",
        examples = {
            @Example(expression = "myAbstractFunction.getFunctionalChains()", result = "the Sequence of functional chains for the given AbstractFunction"),
        }
    )
    // @formatter:on
    public List<FunctionalChain> getFunctionalChains(AbstractFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionFunctionalChain().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of computed actor components for the given AbstractFunction.",
        params = {
            @Param(name = "value", value = "the AbstractFunction")
        },
        result = "the Sequence of computed actor components for the given AbstractFunction",
        examples = {
            @Example(expression = "myAbstractFunction.getAllocatingActorComponentComputed()", result = "the Sequence of computed actor components for the given AbstractFunction"),
        }
    )
    // @formatter:on
    public List<Component> getAllocatingActorComponentComputed(AbstractFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_mother_function_allocation()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of scenarios for the given AbstractFunction.",
        params = {
            @Param(name = "value", value = "the AbstractFunction")
        },
        result = "the Sequence of scenarios for the given AbstractFunction",
        examples = {
            @Example(expression = "myAbstractFunction.getScenarios()", result = "the Sequence of scenarios for the given AbstractFunction"),
        }
    )
    // @formatter:on
    public List<Scenario> getScenarios(AbstractFunction value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of owned operational processes for the given OperationalActivity.",
        params = {
            @Param(name = "value", value = "the OperationalActivity")
        },
        result = "the Sequence of owned operational processes for the given OperationalActivity",
        examples = {
            @Example(expression = "myOperationalActivity.getOwnedOperationalProcesses()", result = "the Sequence of owned operational processes for the given OperationalActivity"),
        }
    )
    // @formatter:on
    public List<OperationalProcess> getOwnedOperationalProcesses(OperationalActivity value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Function_ownedFunctionalChains()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of internal outgoing interactions for the given OperationalActivity.",
        params = {
            @Param(name = "value", value = "the OperationalActivity")
        },
        result = "the Sequence of internal outgoing interactions for the given OperationalActivity",
        examples = {
            @Example(expression = "myOperationalActivity.getInternalOutgoingInteractions()", result = "the Sequence of internal outgoing interactions for the given OperationalActivity"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getInternalOutgoingInteractions(OperationalActivity value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalOutGoingDataflows()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of outgoing interactions for the given OperationalActivity.",
        params = {
            @Param(name = "value", value = "the OperationalActivity")
        },
        result = "the Sequence of outgoing interactions for the given OperationalActivity",
        examples = {
            @Example(expression = "myOperationalActivity.getOutgoingInteractions()", result = "the Sequence of outgoing interactions for the given OperationalActivity"),
        }
    )
    // @formatter:on
    public List<AbstractFunction> getOutgoingInteractions(OperationalActivity value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_outgoingInteraction()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing system functions for the given OperationalActivity.",
        params = {
            @Param(name = "value", value = "the OperationalActivity")
        },
        result = "the Sequence of realizing system functions for the given OperationalActivity",
        examples = {
            @Example(expression = "myOperationalActivity.getRealizingSystemFunctions()", result = "the Sequence of realizing system functions for the given OperationalActivity"),
        }
    )
    // @formatter:on
    public List<SystemFunction> getRealizingSystemFunctions(OperationalActivity value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_realizingFunctions()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of internal incoming interaction for the given OperationalActivity.",
        params = {
            @Param(name = "value", value = "the OperationalActivity")
        },
        result = "the Sequence of internal incoming interaction for the given OperationalActivity",
        examples = {
            @Example(expression = "myOperationalActivity.getInternalIncomingInteractions()", result = "the Sequence of internal incoming interaction for the given OperationalActivity"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getInternalIncomingInteractions(OperationalActivity value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalInComingDataflows()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating entities for the given OperationalActivity.",
        params = {
            @Param(name = "value", value = "the OperationalActivity")
        },
        result = "the Sequence of allocating entities for the given OperationalActivity",
        examples = {
            @Example(expression = "myOperationalActivity.getAllocatingEntity()", result = "the Sequence of allocating entities for the given OperationalActivity"),
        }
    )
    // @formatter:on
    // TODO AbstractFunctionalBlock and Role
    public List<EObject> getAllocatingEntity(OperationalActivity value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingComponent()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of computed allocating actor entitie roles for the given OperationalActivity.",
        params = {
            @Param(name = "value", value = "the OperationalActivity")
        },
        result = "the Sequence of computed allocating actor entitie roles for the given OperationalActivity",
        examples = {
            @Example(expression = "myOperationalActivity.getAllocatingActorEntityRoleComputed()", result = "the Sequence of computed allocating actor entitie roles for the given OperationalActivity"),
        }
    )
    // @formatter:on
    // TODO Component and Role
    public List<EObject> getAllocatingActorEntityRoleComputed(OperationalActivity value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_mother_activity_allocation()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of operational processes for the given OperationalActivity.",
        params = {
            @Param(name = "value", value = "the OperationalActivity")
        },
        result = "the Sequence of operational processes for the given OperationalActivity",
        examples = {
            @Example(expression = "myOperationalActivity.getOperationalProcesses()", result = "the Sequence of operational processes for the given OperationalActivity"),
        }
    )
    // @formatter:on
    public List<OperationalProcess> getOperationalProcesses(OperationalActivity value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalActivityOperationalProcess()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating roles for the given OperationalActivity.",
        params = {
            @Param(name = "value", value = "the OperationalActivity")
        },
        result = "the Sequence of allocating roles for the given OperationalActivity",
        examples = {
            @Example(expression = "myOperationalActivity.getAllocatingRole()", result = "the Sequence of allocating roles for the given OperationalActivity"),
        }
    )
    // @formatter:on
    public List<Role> getAllocatingRole(OperationalActivity value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.OperationActivityAllocatingRole()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of incoming interactions for the given OperationalActivity.",
        params = {
            @Param(name = "value", value = "the OperationalActivity")
        },
        result = "the Sequence of incoming interactions for the given OperationalActivity",
        examples = {
            @Example(expression = "myOperationalActivity.getIncomingInteractions()", result = "the Sequence of incoming interactions for the given OperationalActivity"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getIncomingInteractions(OperationalActivity value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_incomingInteraction()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating operational actors for the given OperationalActivity.",
        params = {
            @Param(name = "value", value = "the OperationalActivity")
        },
        result = "the Sequence of allocating operational actors for the given OperationalActivity",
        examples = {
            @Example(expression = "myOperationalActivity.getAllocatingOperationalActor()", result = "the Sequence of allocating operational actors for the given OperationalActivity"),
        }
    )
    // @formatter:on
    // TODO Component and Role
    public List<EObject> getAllocatingOperationalActor(OperationalActivity value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingActor().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of owners for the given OperationalActivity.",
        params = {
            @Param(name = "value", value = "the OperationalActivity")
        },
        result = "the Sequence of owners for the given OperationalActivity",
        examples = {
            @Example(expression = "myOperationalActivity.getOwner()", result = "the Sequence of owners for the given OperationalActivity"),
        }
    )
    // @formatter:on
    public List<AbstractFunction> getOwner(FunctionalChain value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChain_owningFunction()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved functions for the given FunctionalChain.",
        params = {
            @Param(name = "value", value = "the FunctionalChain")
        },
        result = "the Sequence of involved functions for the given FunctionalChain",
        examples = {
            @Example(expression = "myFunctionalChain.getInvolvedFunctions()", result = "the Sequence of involved functions for the given FunctionalChain"),
        }
    )
    // @formatter:on
    public List<FunctionalChainInvolvementFunction> getInvolvedFunctions(FunctionalChain value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvementFunctions()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized operational processes for the given FunctionalChain.",
        params = {
            @Param(name = "value", value = "the FunctionalChain")
        },
        result = "the Sequence of realized operational processes for the given FunctionalChain",
        examples = {
            @Example(expression = "myFunctionalChain.getRealizedOperationalProcesses()", result = "the Sequence of realized operational processes for the given FunctionalChain"),
        }
    )
    // @formatter:on
    public List<OperationalProcess> getRealizedOperationalProcesses(FunctionalChain value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainRealizedOperationalProcess()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved functional chains for the given FunctionalChain.",
        params = {
            @Param(name = "value", value = "the FunctionalChain")
        },
        result = "the Sequence of involved functional chains for the given FunctionalChain",
        examples = {
            @Example(expression = "myFunctionalChain.getInvolvedFunctionalChains()", result = "the Sequence of involved functional chains for the given FunctionalChain"),
        }
    )
    // @formatter:on
    public List<FunctionalChainReference> getInvolvedFunctionalChains(FunctionalChain value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainChildren().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized functional chains for the given FunctionalChain.",
        params = {
            @Param(name = "value", value = "the FunctionalChain")
        },
        result = "the Sequence of realized functional chains for the given FunctionalChain",
        examples = {
            @Example(expression = "myFunctionalChain.getRealizedFunctionalChains()", result = "the Sequence of realized functional chains for the given FunctionalChain"),
        }
    )
    // @formatter:on
    public List<OperationalProcess> getRealizedFunctionalChains(FunctionalChain value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainRealizedFunctionalChains()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of active states for the given FunctionalChain.",
        params = {
            @Param(name = "value", value = "the FunctionalChain")
        },
        result = "the Sequence of active states for the given FunctionalChain",
        examples = {
            @Example(expression = "myFunctionalChain.getActiveInStates()", result = "the Sequence of active states for the given FunctionalChain"),
        }
    )
    // @formatter:on
    public List<Mode> getActiveInStates(FunctionalChain value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainAvailableInState()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involvement links for the given FunctionalChain.",
        params = {
            @Param(name = "value", value = "the FunctionalChain")
        },
        result = "the Sequence of involvement links for the given FunctionalChain",
        examples = {
            @Example(expression = "myFunctionalChain.getInvolvementLinks()", result = "the Sequence of involvement links for the given FunctionalChain"),
        }
    )
    // @formatter:on
    public List<FunctionalChainInvolvementLink> getInvolvementLinks(FunctionalChain value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvementLinks()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved components for the given FunctionalChain.",
        params = {
            @Param(name = "value", value = "the FunctionalChain")
        },
        result = "the Sequence of involved components for the given FunctionalChain",
        examples = {
            @Example(expression = "myFunctionalChain.getInvolvedComponents()", result = "the Sequence of involved components for the given FunctionalChain"),
        }
    )
    // @formatter:on
    public List<Component> getInvolvedComponents(FunctionalChain value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChain_enactedComponents()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of active modes for the given FunctionalChain.",
        params = {
            @Param(name = "value", value = "the FunctionalChain")
        },
        result = "the Sequence of active modes for the given FunctionalChain",
        examples = {
            @Example(expression = "myFunctionalChain.getActiveInModes()", result = "the Sequence of active modes for the given FunctionalChain"),
        }
    )
    // @formatter:on
    public List<Mode> getActiveInModes(FunctionalChain value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainAvailableInMode()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involving capability realizations for the given FunctionalChain.",
        params = {
            @Param(name = "value", value = "the FunctionalChain")
        },
        result = "the Sequence of involving capability realizations for the given FunctionalChain",
        examples = {
            @Example(expression = "myFunctionalChain.getInvolvingCapabilityRealizations()", result = "the Sequence of involving capability realizations for the given FunctionalChain"),
        }
    )
    // @formatter:on
    public List<AbstractCapability> getInvolvingCapabilityRealizations(FunctionalChain value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.LAAndPAFunctionalChainInvolvingCapabilityRealization()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involving capabilities for the given FunctionalChain.",
        params = {
            @Param(name = "value", value = "the FunctionalChain")
        },
        result = "the Sequence of involving capabilities for the given FunctionalChain",
        examples = {
            @Example(expression = "myFunctionalChain.getInvolvingCapabilities()", result = "the Sequence of involving capabilities for the given FunctionalChain"),
        }
    )
    // @formatter:on
    public List<AbstractCapability> getInvolvingCapabilities(FunctionalChain value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.SAFunctionalChainInvolvingCapability()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing functional chains for the given FunctionalChain.",
        params = {
            @Param(name = "value", value = "the FunctionalChain")
        },
        result = "the Sequence of realizing functional chains for the given FunctionalChain",
        examples = {
            @Example(expression = "myFunctionalChain.getRealizingFunctionalChains()", result = "the Sequence of realizing functional chains for the given FunctionalChain"),
        }
    )
    // @formatter:on
    public List<TraceableElement> getRealizingFunctionalChains(FunctionalChain value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainRealizingFunctionalChains()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of parent functional chains for the given FunctionalChain.",
        params = {
            @Param(name = "value", value = "the FunctionalChain")
        },
        result = "the Sequence of parent functional chains for the given FunctionalChain",
        examples = {
            @Example(expression = "myFunctionalChain.getParentFunctionalChains()", result = "the Sequence of parent functional chains for the given FunctionalChain"),
        }
    )
    // @formatter:on
    public List<FunctionalChainReference> getParentFunctionalChains(FunctionalChain value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainParent().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved operational processes for the given OperationalProcess.",
        params = {
            @Param(name = "value", value = "the OperationalProcess")
        },
        result = "the Sequence of involved operational processes for the given OperationalProcess",
        examples = {
            @Example(expression = "myOperationalProcess.getInvolvedOperationalProcesses()", result = "the Sequence of involved operational processes for the given OperationalProcess"),
        }
    )
    // @formatter:on
    public List<OperationalProcess> getInvolvedOperationalProcesses(OperationalProcess value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalProcessChildren()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved operational activities for the given OperationalProcess.",
        params = {
            @Param(name = "value", value = "the OperationalProcess")
        },
        result = "the Sequence of involved operational activities for the given OperationalProcess",
        examples = {
            @Example(expression = "myOperationalProcess.getInvolvedOperationalActivities()", result = "the Sequence of involved operational activities for the given OperationalProcess"),
        }
    )
    // @formatter:on
    public List<OperationalActivity> getInvolvedOperationalActivities(OperationalProcess value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalProcessInvolvedOperationalActivities()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved operational capabilities for the given OperationalProcess.",
        params = {
            @Param(name = "value", value = "the OperationalProcess")
        },
        result = "the Sequence of involved operational capabilities for the given OperationalProcess",
        examples = {
            @Example(expression = "myOperationalProcess.getInvolvingOperationalCapabilities()", result = "the Sequence of involved capabilities activities for the given OperationalProcess"),
        }
    )
    // @formatter:on
    public List<OperationalActivity> getInvolvingOperationalCapabilities(OperationalProcess value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvingCapability()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of parent operational processes for the given OperationalProcess.",
        params = {
            @Param(name = "value", value = "the OperationalProcess")
        },
        result = "the Sequence of parent operational processes for the given OperationalProcess",
        examples = {
            @Example(expression = "myOperationalProcess.getParentOperationalProcesses()", result = "the Sequence of parent operational processes for the given OperationalProcess"),
        }
    )
    // @formatter:on
    public List<OperationalProcess> getParentOperationalProcesses(OperationalProcess value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalProcessParent().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of scenarii for the given AbstractCapability.",
        params = {
            @Param(name = "value", value = "the AbstractCapability")
        },
        result = "the Sequence of scenarii for the given AbstractCapability",
        examples = {
            @Example(expression = "myAbstractCapability.getScenarios()", result = "the Sequence of scenarii for the given AbstractCapability"),
        }
    )
    // @formatter:on
    public List<Scenario> getScenarios(AbstractCapability value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.Capability_scenarios().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of generalized elements for the given AbstractCapability.",
        params = {
            @Param(name = "value", value = "the AbstractCapability")
        },
        result = "the Sequence of generalized elements for the given AbstractCapability",
        examples = {
            @Example(expression = "myAbstractCapability.getGeneralizedElements()", result = "the Sequence of generalized elements for the given AbstractCapability"),
        }
    )
    // @formatter:on
    public List<AbstractCapability> getGeneralizedElements(AbstractCapability value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilitySuper().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of active modes for the given AbstractCapability.",
        params = {
            @Param(name = "value", value = "the AbstractCapability")
        },
        result = "the Sequence of active modes for the given AbstractCapability",
        examples = {
            @Example(expression = "myAbstractCapability.getActiveInModes()", result = "the Sequence of active modes for the given AbstractCapability"),
        }
    )
    // @formatter:on
    public List<Mode> getActiveInModes(AbstractCapability value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityAvailableInMode()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved components for the given AbstractCapability.",
        params = {
            @Param(name = "value", value = "the AbstractCapability")
        },
        result = "the Sequence of involved components for the given AbstractCapability",
        examples = {
            @Example(expression = "myAbstractCapability.getInvolvedComponents()", result = "the Sequence of involved components for the given AbstractCapability"),
        }
    )
    // @formatter:on
    public List<CapabilityRealizationInvolvedElement> getInvolvedComponents(AbstractCapability value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Capability_InvolvedComponents()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of extended capabilities for the given AbstractCapability.",
        params = {
            @Param(name = "value", value = "the AbstractCapability")
        },
        result = "the Sequence of extended capabilities for the given AbstractCapability",
        examples = {
            @Example(expression = "myAbstractCapability.getExtendedCapabilities()", result = "the Sequence of extended capabilities for the given AbstractCapability"),
        }
    )
    // @formatter:on
    public List<AbstractCapability> getExtendedCapabilities(AbstractCapability value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Capability_extendedCapabilities()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of refined capabilities for the given AbstractCapability.",
        params = {
            @Param(name = "value", value = "the AbstractCapability")
        },
        result = "the Sequence of refined capabilities for the given AbstractCapability",
        examples = {
            @Example(expression = "myAbstractCapability.getRefinedCapabilities()", result = "the Sequence of refined capabilities for the given AbstractCapability"),
        }
    )
    // @formatter:on
    public List<CapellaElement> getRefinedCapabilities(AbstractCapability value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapability_refinedAbstractCapabilities()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of active states for the given AbstractCapability.",
        params = {
            @Param(name = "value", value = "the AbstractCapability")
        },
        result = "the Sequence of active states for the given AbstractCapability",
        examples = {
            @Example(expression = "myAbstractCapability.getActiveInStates()", result = "the Sequence of active states for the given AbstractCapability"),
        }
    )
    // @formatter:on
    public List<Mode> getActiveInStates(AbstractCapability value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityAvailableInState()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of included capabilities for the given AbstractCapability.",
        params = {
            @Param(name = "value", value = "the AbstractCapability")
        },
        result = "the Sequence of included capabilities for the given AbstractCapability",
        examples = {
            @Example(expression = "myAbstractCapability.getIncludedCapabilities()", result = "the Sequence of included capabilities for the given AbstractCapability"),
        }
    )
    // @formatter:on
    public List<AbstractCapability> getIncludedCapabilities(AbstractCapability value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Capability_includedCapabilities()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of generalizing elements for the given AbstractCapability.",
        params = {
            @Param(name = "value", value = "the AbstractCapability")
        },
        result = "the Sequence of generalizing elements for the given AbstractCapability",
        examples = {
            @Example(expression = "myAbstractCapability.getGeneralizingElements()", result = "the Sequence of generalizing elements for the given AbstractCapability"),
        }
    )
    // @formatter:on
    public List<AbstractCapability> getGeneralizingElements(AbstractCapability value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilitySub().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of refining scenarii for the given AbstractCapability.",
        params = {
            @Param(name = "value", value = "the AbstractCapability")
        },
        result = "the Sequence of refining scenarii for the given AbstractCapability",
        examples = {
            @Example(expression = "myAbstractCapability.getRefiningScenarios()", result = "the Sequence of refining scenarii for the given AbstractCapability"),
        }
    )
    // @formatter:on
    public List<CapellaElement> getRefiningScenarios(AbstractCapability value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapability_refiningAbstractCapabilities()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of including capabilities for the given AbstractCapability.",
        params = {
            @Param(name = "value", value = "the AbstractCapability")
        },
        result = "the Sequence of including capabilities for the given AbstractCapability",
        examples = {
            @Example(expression = "myAbstractCapability.getIncludingCapabilities()", result = "the Sequence of including capabilities for the given AbstractCapability"),
        }
    )
    // @formatter:on
    public List<AbstractCapability> getIncludingCapabilities(AbstractCapability value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Capability_includingCapabilities()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of extending capabilities for the given AbstractCapability.",
        params = {
            @Param(name = "value", value = "the AbstractCapability")
        },
        result = "the Sequence of extending capabilities for the given AbstractCapability",
        examples = {
            @Example(expression = "myAbstractCapability.getExtendingCapabilities()", result = "the Sequence of extending capabilities for the given AbstractCapability"),
        }
    )
    // @formatter:on
    public List<AbstractCapability> getExtendingCapabilities(AbstractCapability value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Capability_extendingCapabilities()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of owned operational processes for the given OperationalCapability.",
        params = {
            @Param(name = "value", value = "the OperationalCapability")
        },
        result = "the Sequence of owned operational processes for the given AbstractCapability",
        examples = {
            @Example(expression = "myOperationalCapability.getOwnedOperationalProcesses()", result = "the Sequence of owned operational processes for the given OperationalCapability"),
        }
    )
    // @formatter:on
    public List<OperationalProcess> getOwnedOperationalProcesses(OperationalCapability value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityOwnedFunctionalChains()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved entities for the given OperationalCapability.",
        params = {
            @Param(name = "value", value = "the OperationalCapability")
        },
        result = "the Sequence of involved entities for the given AbstractCapability",
        examples = {
            @Example(expression = "myOperationalCapability.getInvolvedEntities()", result = "the Sequence of involved entities for the given OperationalCapability"),
        }
    )
    // @formatter:on
    public List<Entity> getInvolvedEntities(OperationalCapability value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalCapability_InvolvedEntity()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved operational process for the given OperationalCapability.",
        params = {
            @Param(name = "value", value = "the OperationalCapability")
        },
        result = "the Sequence of involved operational process for the given AbstractCapability",
        examples = {
            @Example(expression = "myOperationalCapability.getInvolvedOperationalProcesses()", result = "the Sequence of involved operational process for the given OperationalCapability"),
        }
    )
    // @formatter:on
    public List<OperationalProcess> getInvolvedOperationalProcesses(OperationalCapability value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityInvolvedFunctionalChains()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing capabilities for the given OperationalCapability.",
        params = {
            @Param(name = "value", value = "the OperationalCapability")
        },
        result = "the Sequence of realizing capabilities for the given AbstractCapability",
        examples = {
            @Example(expression = "myOperationalCapability.getRealizingCapabilities()", result = "the Sequence of realizing capabilities for the given OperationalCapability"),
        }
    )
    // @formatter:on
    public List<Capability> getRealizingCapabilities(OperationalCapability value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.OCapabilityRealizingCapability()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of target for the given Allocation.",
        params = {
            @Param(name = "value", value = "the Allocation")
        },
        result = "the Sequence of target for the given Allocation",
        examples = {
            @Example(expression = "myAllocation.getTarget()", result = "the Sequence of target for the given Allocation"),
        }
    )
    // @formatter:on
    public List<TraceableElement> getTarget(Allocation value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAllocationTarget()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of source for the given Allocation.",
        params = {
            @Param(name = "value", value = "the Allocation")
        },
        result = "the Sequence of source for the given Allocation",
        examples = {
            @Example(expression = "myAllocation.getSource()", result = "the Sequence of source for the given Allocation"),
        }
    )
    // @formatter:on
    public List<TraceableElement> getSource(Allocation value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAllocationSource()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating operational activities for the given Role.",
        params = {
            @Param(name = "value", value = "the Role")
        },
        result = "the Sequence of allocating operational activities for the given Role",
        examples = {
            @Example(expression = "myAllocation.getAllocatedOperationalActivities()", result = "the Sequence of allocating operational activities for the given Role"),
        }
    )
    // @formatter:on
    public List<OperationalActivity> getAllocatedOperationalActivities(Role value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.Role_AllocatedFunctions().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating entities for the given Role.",
        params = {
            @Param(name = "value", value = "the Role")
        },
        result = "the Sequence of allocating entities for the given Role",
        examples = {
            @Example(expression = "myAllocation.getAllocatingEntities()", result = "the Sequence of entities activities for the given Role"),
        }
    )
    // @formatter:on
    public List<Entity> getAllocatingEntities(Role value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.Role_AllocatingEntity().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of scenarii for the given Role.",
        params = {
            @Param(name = "value", value = "the Role")
        },
        result = "the Sequence of scenarii for the given Role",
        examples = {
            @Example(expression = "myAllocation.getScenarios()", result = "the Sequence of scenarii for the given Role"),
        }
    )
    // @formatter:on
    public List<Scenario> getScenarios(Role value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of exchange item elements for the given Type.",
        params = {
            @Param(name = "value", value = "the Type")
        },
        result = "the Sequence of exchange item elements for the given Type",
        examples = {
            @Example(expression = "myAllocation.getExchangeItemElements()", result = "the Sequence of exchange item elements for the given Type"),
        }
    )
    // @formatter:on
    public List<ExchangeItemElement> getExchangeItemElements(Type value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractTypeExchangeItemElements()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of typing elements for the given Type.",
        params = {
            @Param(name = "value", value = "the Type")
        },
        result = "the Sequence of typing elements for the given Type",
        examples = {
            @Example(expression = "myAllocation.getTypingElements()", result = "the Sequence of typing elements for the given Type"),
        }
    )
    // @formatter:on
    public List<ExchangeItemElement> getTypingElements(Type value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractTypeTypedElements()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of generalized elements for the given GeneralizableElement.",
        params = {
            @Param(name = "value", value = "the GeneralizableElement")
        },
        result = "the Sequence of generalized elements for the given GeneralizableElement",
        examples = {
            @Example(expression = "myAllocation.getGeneralizedElements()", result = "the Sequence of generalized elements for the given GeneralizableElement"),
        }
    )
    // @formatter:on
    public List<GeneralizableElement> getGeneralizedElements(GeneralizableElement value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.GeneralizableElementAllSuperGE()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of generalizing elements for the given GeneralizableElement.",
        params = {
            @Param(name = "value", value = "the GeneralizableElement")
        },
        result = "the Sequence of generalizing elements for the given GeneralizableElement",
        examples = {
            @Example(expression = "myAllocation.getGeneralizingElements()", result = "the Sequence of generalizing elements for the given GeneralizableElement"),
        }
    )
    // @formatter:on
    public List<GeneralizableElement> getGeneralizingElements(GeneralizableElement value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.GeneralizableElementAllSubGE()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of component breakdown for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of component breakdown for the given Component",
        examples = {
            @Example(expression = "myComponent.getComponentBreakdown()", result = "the Sequence of component breakdown for the given Component"),
        }
    )
    // @formatter:on
    public List<Component> getComponentBreakdown(Component value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_componentBreakdown()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of parent for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of parent for the given Component",
        examples = {
            @Example(expression = "myComponent.getParent()", result = "the Sequence of parent for the given Component"),
        }
    )
    // @formatter:on
    public List<Component> getParent(Component value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_parentComponent()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of owned components for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of owned components for the given Component",
        examples = {
            @Example(expression = "myComponent.getOwnedComponents()", result = "the Sequence of owned components for the given Component"),
        }
    )
    // @formatter:on
    public List<Component> getOwnedComponents(Component value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_SubDefinedComponents()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of generalized components for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of generalized components for the given Component",
        examples = {
            @Example(expression = "myComponent.getGeneralizedComponents()", result = "the Sequence of generalized components for the given Component"),
        }
    )
    // @formatter:on
    public List<GeneralizableElement> getGeneralizedComponents(Component value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.GeneralizableElementAllSuperGC()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of outgoing component exchanges for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of outgoing component exchanges for the given Component",
        examples = {
            @Example(expression = "myComponent.getInternalOutgoingComponentExchangesComputed()", result = "the Sequence of outgoing component exchanges for the given Component"),
        }
    )
    // @formatter:on
    public List<ComponentExchange> getInternalOutgoingComponentExchangesComputed(Component value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentInternalOutgoingComponentExchanges()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of outgoing delegations for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of outgoing delegations for the given Component",
        examples = {
            @Example(expression = "myComponent.getOutgoingDelegations()", result = "the Sequence of outgoing delegations for the given Component"),
        }
    )
    // @formatter:on
    public List<ComponentExchange> getOutgoingDelegations(Component value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentOutgoingDelegation()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of provided interfaces for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of provided interfaces for the given Component",
        examples = {
            @Example(expression = "myComponent.getProvidedInterfaces()", result = "the Sequence of provided interfaces for the given Component"),
        }
    )
    // @formatter:on
    public List<Interface> getProvidedInterfaces(Component value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentProvidedInterfaces()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of communication links for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of communication links for the given Component",
        examples = {
            @Example(expression = "myComponent.getCommunicationLink()", result = "the Sequence of communication links for the given Component"),
        }
    )
    // @formatter:on
    public List<CommunicationLink> getCommunicationLink(Component value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentCommunicationLink()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of used interfaces for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of used interfaces for the given Component",
        examples = {
            @Example(expression = "myComponent.getUsedInterfaces()", result = "the Sequence of used interfaces for the given Component"),
        }
    )
    // @formatter:on
    public List<Interface> getUsedInterfaces(Component value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.Component_usedInterfaces().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of required interfaces for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of required interfaces for the given Component",
        examples = {
            @Example(expression = "myComponent.getRequiredInterfaces()", result = "the Sequence of required interfaces for the given Component"),
        }
    )
    // @formatter:on
    public List<Interface> getRequiredInterfaces(Component value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentRequiredInterfaces()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of outgoing component echanges for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of outgoing component echanges for the given Component",
        examples = {
            @Example(expression = "myComponent.getOutgoingComponentExchanges()", result = "the Sequence of outgoing component echanges for the given Component"),
        }
    )
    // @formatter:on
    public List<ComponentExchange> getOutgoingComponentExchanges(Component value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentOutgoingComponentExchange()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of representing parts for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of representing parts for the given Component",
        examples = {
            @Example(expression = "myComponent.getOutgoingComponentExchanges()", result = "the Sequence of representing parts for the given Component"),
        }
    )
    // @formatter:on
    public List<Part> getRepresentingParts(Component value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_representingParts()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of implemented interfaces for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of implemented interfaces for the given Component",
        examples = {
            @Example(expression = "myComponent.getImplementedInterfaces()", result = "the Sequence of implemented interfaces for the given Component"),
        }
    )
    // @formatter:on
    public List<Interface> getImplementedInterfaces(Component value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_implementedInterfaces()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of incoming component exchanges for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of incoming component exchanges for the given Component",
        examples = {
            @Example(expression = "myComponent.getIncomingComponentExchanges()", result = "the Sequence of incoming component exchanges for the given Component"),
        }
    )
    // @formatter:on
    public List<ComponentExchange> getIncomingComponentExchanges(Component value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentIncomingComponentExchange()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of scenarii for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of scenarii for the given Component",
        examples = {
            @Example(expression = "myComponent.getScenarios()", result = "the Sequence of scenarii for the given Component"),
        }
    )
    // @formatter:on
    public List<Scenario> getScenarios(Component value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of component ports for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of component ports for the given Component",
        examples = {
            @Example(expression = "myComponent.getComponentPorts()", result = "the Sequence of component ports for the given Component"),
        }
    )
    // @formatter:on
    public List<ComponentPort> getComponentPorts(Component value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.Component_componentPorts().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of incoming delegations for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of incoming delegations for the given Component",
        examples = {
            @Example(expression = "myComponent.getIncomingDelegations()", result = "the Sequence of incoming delegations for the given Component"),
        }
    )
    // @formatter:on
    public List<ComponentExchange> getIncomingDelegations(Component value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentIncomingDelegation()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of generalizing components for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of generalizing components for the given Component",
        examples = {
            @Example(expression = "myComponent.getGeneralizingComponents()", result = "the Sequence of generalizing components for the given Component"),
        }
    )
    // @formatter:on
    public List<GeneralizableElement> getGeneralizingComponents(Component value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.GeneralizableElementAllSubGC()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of referencing components for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of referencing components for the given Component",
        examples = {
            @Example(expression = "myComponent.getReferencingComponents()", result = "the Sequence of referencing components for the given Component"),
        }
    )
    // @formatter:on
    public List<Component> getReferencingComponents(Component value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_referencingComponent()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of computed incoming component echanges for the given Component.",
        params = {
            @Param(name = "value", value = "the Component")
        },
        result = "the Sequence of computed incoming component echanges for the given Component",
        examples = {
            @Example(expression = "myComponent.getInternalIncomingComponentExchangesComputed()", result = "the Sequence of computed incoming component echanges for the given Component"),
        }
    )
    // @formatter:on
    public List<ComponentExchange> getInternalIncomingComponentExchangesComputed(Component value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentInternalIncomingComponentExchanges()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of breakdown for the given Entity.",
        params = {
            @Param(name = "value", value = "the Entity")
        },
        result = "the Sequence of breakdown for the given Entity",
        examples = {
            @Example(expression = "myEntity.getBreakdown()", result = "the Sequence of breakdown for the given Entity"),
        }
    )
    // @formatter:on
    public List<Entity> getBreakdown(Entity value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalEntity_Breakdown()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocated roles for the given Entity.",
        params = {
            @Param(name = "value", value = "the Entity")
        },
        result = "the Sequence of allocated roles for the given Entity",
        examples = {
            @Example(expression = "myEntity.getAllocatedRoles()", result = "the Sequence of allocated roles for the given Entity"),
        }
    )
    // @formatter:on
    public List<Role> getAllocatedRoles(Entity value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalEntityAllocatedRoles()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocated operational activities for the given Entity.",
        params = {
            @Param(name = "value", value = "the Entity")
        },
        result = "the Sequence of allocated operational activities for the given Entity",
        examples = {
            @Example(expression = "myEntity.getAllocatedOperationalActivities()", result = "the Sequence of allocated operational activities for the given Entity"),
        }
    )
    // @formatter:on
    public List<OperationalActivity> getAllocatedOperationalActivities(Entity value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_AllocatedFunctions()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of outgoing communication means for the given Entity.",
        params = {
            @Param(name = "value", value = "the Entity")
        },
        result = "the Sequence of outgoing communication means for the given Entity",
        examples = {
            @Example(expression = "myEntity.getOutgoingCommunicationMean()", result = "the Sequence of outgoing communication means for the given Entity"),
        }
    )
    // @formatter:on
    public List<CommunicationMean> getOutgoingCommunicationMean(Entity value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalEntity_OutgoingCommunicationMean()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of incoming communication means for the given Entity.",
        params = {
            @Param(name = "value", value = "the Entity")
        },
        result = "the Sequence of incoming communication means for the given Entity",
        examples = {
            @Example(expression = "myEntity.getIncomingCommunicationMean()", result = "the Sequence of incoming communication means for the given Entity"),
        }
    )
    // @formatter:on
    public List<CommunicationMean> getIncomingCommunicationMean(Entity value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalEntity_IncomingCommunicationMean()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involving operation capabilities for the given Entity.",
        params = {
            @Param(name = "value", value = "the Entity")
        },
        result = "the Sequence of involving operation capabilities for the given Entity",
        examples = {
            @Example(expression = "myEntity.getInvolvingOperationalCapabilities()", result = "the Sequence of involving operation capabilities for the given Entity"),
        }
    )
    // @formatter:on
    public List<OperationalCapability> getInvolvingOperationalCapabilities(Entity value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.OperationalEntityInvolgingOperationalCap()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing system components for the given Entity.",
        params = {
            @Param(name = "value", value = "the Entity")
        },
        result = "the Sequence of realizing system components for the given Entity",
        examples = {
            @Example(expression = "myEntity.getRealizingSystemComponents()", result = "the Sequence of realizing system components for the given Entity"),
        }
    )
    // @formatter:on
    public List<Component> getRealizingSystemComponents(Entity value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizingComponents()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized system components for the given Entity.",
        params = {
            @Param(name = "value", value = "the Entity")
        },
        result = "the Sequence of realized system components for the given Entity",
        examples = {
            @Example(expression = "myEntity.getRealizedComponentExchanges()", result = "the Sequence of realized system components for the given Entity"),
        }
    )
    // @formatter:on
    public List<CommunicationMean> getRealizedComponentExchanges(ExchangeSpecification value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeSpecification_realizedDataflow()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of target for the given ExchangeSpecification.",
        params = {
            @Param(name = "value", value = "the ExchangeSpecification")
        },
        result = "the Sequence of target for the given ExchangeSpecification",
        examples = {
            @Example(expression = "myEntity.getTarget()", result = "the Sequence of target for the given ExchangeSpecification"),
        }
    )
    // @formatter:on
    public List<InformationsExchanger> getTarget(ExchangeSpecification value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeSpecification_dataflowTarget()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of source for the given ExchangeSpecification.",
        params = {
            @Param(name = "value", value = "the ExchangeSpecification")
        },
        result = "the Sequence of source for the given ExchangeSpecification",
        examples = {
            @Example(expression = "myEntity.getSource()", result = "the Sequence of source for the given ExchangeSpecification"),
        }
    )
    // @formatter:on
    public List<InformationsExchanger> getSource(ExchangeSpecification value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeSpecification_dataflowSource()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of related data for the given ComponentExchange.",
        params = {
            @Param(name = "value", value = "the ComponentExchange")
        },
        result = "the Sequence of related data for the given ComponentExchange",
        examples = {
            @Example(expression = "myComponentExchange.getRelatedData()", result = "the Sequence of related data for the given ComponentExchange"),
        }
    )
    // @formatter:on
    public List<AbstractType> getRelatedData(ComponentExchange value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeSpecification_relatedData()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of owner for the given ComponentExchange.",
        params = {
            @Param(name = "value", value = "the ComponentExchange")
        },
        result = "the Sequence of owner for the given ComponentExchange",
        examples = {
            @Example(expression = "myComponentExchange.getOwner()", result = "the Sequence of owner for the given ComponentExchange"),
        }
    )
    // @formatter:on
    public List<EObject> getOwner(ComponentExchange value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeSpecification_owner()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of inherited categories for the given ComponentExchange.",
        params = {
            @Param(name = "value", value = "the ComponentExchange")
        },
        result = "the Sequence of inherited categories for the given ComponentExchange",
        examples = {
            @Example(expression = "myComponentExchange.getInheritedCategories()", result = "the Sequence of inherited categories for the given ComponentExchange"),
        }
    )
    // @formatter:on
    public List<ComponentExchangeCategory> getInheritedCategories(ComponentExchange value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentExchangeCategoriesForDelegations()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocated functional exchances for the given ComponentExchange.",
        params = {
            @Param(name = "value", value = "the ComponentExchange")
        },
        result = "the Sequence of allocated functional exchances for the given ComponentExchange",
        examples = {
            @Example(expression = "myComponentExchange.getAllocatedFunctionalExchanges()", result = "the Sequence of allocated functional exchances for the given ComponentExchange"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getAllocatedFunctionalExchanges(ComponentExchange value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentExchangeAllocatedFunctionalExchanges()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of categories for the given ComponentExchange.",
        params = {
            @Param(name = "value", value = "the ComponentExchange")
        },
        result = "the Sequence of categories for the given ComponentExchange",
        examples = {
            @Example(expression = "myComponentExchange.getCategories()", result = "the Sequence of categories for the given ComponentExchange"),
        }
    )
    // @formatter:on
    public List<ComponentExchangeCategory> getCategories(ComponentExchange value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentExchangeCategories()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of connected components for the given ComponentExchange.",
        params = {
            @Param(name = "value", value = "the ComponentExchange")
        },
        result = "the Sequence of connected components for the given ComponentExchange",
        examples = {
            @Example(expression = "myComponentExchange.getConnectedComponents()", result = "the Sequence of connected components for the given ComponentExchange"),
        }
    )
    // @formatter:on
    public List<Component> getConnectedComponents(ComponentExchange value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Connection_connectedComponents()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of exchange items for the given ComponentExchange.",
        params = {
            @Param(name = "value", value = "the ComponentExchange")
        },
        result = "the Sequence of exchange items for the given ComponentExchange",
        examples = {
            @Example(expression = "myComponentExchange.getExchangeItems()", result = "the Sequence of exchange items for the given ComponentExchange"),
        }
    )
    // @formatter:on
    public List<ExchangeItem> getExchangeItems(ComponentExchange value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ConnectionConvoyedInformation()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized communication means for the given ComponentExchange.",
        params = {
            @Param(name = "value", value = "the ComponentExchange")
        },
        result = "the Sequence of realized communication means for the given ComponentExchange",
        examples = {
            @Example(expression = "myComponentExchange.getRealizedCommunicationMean()", result = "the Sequence of realized communication means for the given ComponentExchange"),
        }
    )
    // @formatter:on
    public List<CommunicationMean> getRealizedCommunicationMean(ComponentExchange value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ComponenExchangeRealizedCommunicationMean()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of connected parts for the given ComponentExchange.",
        params = {
            @Param(name = "value", value = "the ComponentExchange")
        },
        result = "the Sequence of connected parts for the given ComponentExchange",
        examples = {
            @Example(expression = "myComponentExchange.getConnectedParts()", result = "the Sequence of connected parts for the given ComponentExchange"),
        }
    )
    // @formatter:on
    public List<Part> getConnectedParts(ComponentExchange value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Connection_connectedParts()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing component exchanges for the given ComponentExchange.",
        params = {
            @Param(name = "value", value = "the ComponentExchange")
        },
        result = "the Sequence of realizing component exchanges for the given ComponentExchange",
        examples = {
            @Example(expression = "myComponentExchange.getRealizingComponentExchanges()", result = "the Sequence of realizing component exchanges for the given ComponentExchange"),
        }
    )
    // @formatter:on
    public List<ComponentExchange> getRealizingComponentExchanges(ComponentExchange value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeSpecification_realizingDataflow()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating physical path for the given ComponentExchange.",
        params = {
            @Param(name = "value", value = "the ComponentExchange")
        },
        result = "the Sequence of allocating physical path for the given ComponentExchange",
        examples = {
            @Example(expression = "myComponentExchange.getAllocatingPhysicalPath()", result = "the Sequence of allocating physical path for the given ComponentExchange"),
        }
    )
    // @formatter:on
    public List<PhysicalPath> getAllocatingPhysicalPath(ComponentExchange value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentExchangeAllocatingPhysicalPath()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating physical link for the given ComponentExchange.",
        params = {
            @Param(name = "value", value = "the ComponentExchange")
        },
        result = "the Sequence of allocating physical link for the given ComponentExchange",
        examples = {
            @Example(expression = "myComponentExchange.getAllocatingPhysicalLink()", result = "the Sequence of allocating physical link for the given ComponentExchange"),
        }
    )
    // @formatter:on
    public List<PhysicalLink> getAllocatingPhysicalLink(ComponentExchange value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentExchangeAllocatingPhysicalLink()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of scenarii for the given ComponentExchange.",
        params = {
            @Param(name = "value", value = "the ComponentExchange")
        },
        result = "the Sequence of scenarii for the given ComponentExchange",
        examples = {
            @Example(expression = "myComponentExchange.getScenarios()", result = "the Sequence of scenarii for the given ComponentExchange"),
        }
    )
    // @formatter:on
    public List<Scenario> getScenarios(ComponentExchange value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocated interactions for the given CommunicationMean.",
        params = {
            @Param(name = "value", value = "the CommunicationMean")
        },
        result = "the Sequence of allocated interactions for the given CommunicationMean",
        examples = {
            @Example(expression = "myCommunicationMean.getAllocatedInteractions()", result = "the Sequence of allocated interactions for the given CommunicationMean"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getAllocatedInteractions(CommunicationMean value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CommunicationMean_AllocatedExchanges()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of target for the given CommunicationMean.",
        params = {
            @Param(name = "value", value = "the CommunicationMean")
        },
        result = "the Sequence of target for the given CommunicationMean",
        examples = {
            @Example(expression = "myCommunicationMean.getTarget()", result = "the Sequence of target for the given CommunicationMean"),
        }
    )
    // @formatter:on
    public List<InformationsExchanger> getTarget(CommunicationMean value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CommunicationMean_Target().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of source for the given CommunicationMean.",
        params = {
            @Param(name = "value", value = "the CommunicationMean")
        },
        result = "the Sequence of source for the given CommunicationMean",
        examples = {
            @Example(expression = "myCommunicationMean.getSource()", result = "the Sequence of source for the given CommunicationMean"),
        }
    )
    // @formatter:on
    public List<InformationsExchanger> getSource(CommunicationMean value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CommunicationMean_Source().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved elements for the given Involvement.",
        params = {
            @Param(name = "value", value = "the Involvement")
        },
        result = "the Sequence of involved elements for the given Involvement",
        examples = {
            @Example(expression = "myInvolvement.getInvolvedElement()", result = "the Sequence of involved elements for the given Involvement"),
        }
    )
    // @formatter:on
    public List<InvolvedElement> getInvolvedElement(Involvement value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsInvolvementTarget()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involving elements for the given Involvement.",
        params = {
            @Param(name = "value", value = "the Involvement")
        },
        result = "the Sequence of involving elements for the given Involvement",
        examples = {
            @Example(expression = "myInvolvement.getInvolvingElement()", result = "the Sequence of involving elements for the given Involvement"),
        }
    )
    // @formatter:on
    public List<InvolverElement> getInvolvingElement(Involvement value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsInvolvementSource()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of owned functional chains for the given PhysicalFunction.",
        params = {
            @Param(name = "value", value = "the PhysicalFunction")
        },
        result = "the Sequence of owned functional chains for the given PhysicalFunction",
        examples = {
            @Example(expression = "myPhysicalFunction.getOwnedFunctionalChains()", result = "the Sequence of owned functional chains for the given PhysicalFunction"),
        }
    )
    // @formatter:on
    public List<FunctionalChain> getOwnedFunctionalChains(PhysicalFunction value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Function_ownedFunctionalChains()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of outgoing functional exchanges for the given PhysicalFunction.",
        params = {
            @Param(name = "value", value = "the PhysicalFunction")
        },
        result = "the Sequence of owned outgoing functional exchanges for the given PhysicalFunction",
        examples = {
            @Example(expression = "myPhysicalFunction.getOutgoingFunctionalExchanges()", result = "the Sequence of outgoing functional exchanges for the given PhysicalFunction"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getOutgoingFunctionalExchanges(PhysicalFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_outgoingInteraction()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of outgoing functional exchanges for the given PhysicalFunction.",
        params = {
            @Param(name = "value", value = "the PhysicalFunction")
        },
        result = "the Sequence of owned outgoing functional exchanges for the given PhysicalFunction",
        examples = {
            @Example(expression = "myPhysicalFunction.getOutFlowPorts()", result = "the Sequence of outgoing functional exchanges for the given PhysicalFunction"),
        }
    )
    // @formatter:on
    public List<FunctionOutputPort> getOutFlowPorts(PhysicalFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.Function_outFlowPorts().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized logical functions for the given PhysicalFunction.",
        params = {
            @Param(name = "value", value = "the PhysicalFunction")
        },
        result = "the Sequence of realized logical functions for the given PhysicalFunction",
        examples = {
            @Example(expression = "myPhysicalFunction.getRealizedLogicalFunctions()", result = "the Sequence of realized logical functions for the given PhysicalFunction"),
        }
    )
    // @formatter:on
    public List<LogicalFunction> getRealizedLogicalFunctions(PhysicalFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_realizedFunctions()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of internal outgoing functional exchanges for the given PhysicalFunction.",
        params = {
            @Param(name = "value", value = "the PhysicalFunction")
        },
        result = "the Sequence of internal outgoing functional exchanges for the given PhysicalFunction",
        examples = {
            @Example(expression = "myPhysicalFunction.getInternalOutgoingFunctionalExchanges()", result = "the Sequence of internal outgoing functional exchanges for the given PhysicalFunction"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getInternalOutgoingFunctionalExchanges(PhysicalFunction value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalOutGoingDataflows()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involving capability realizations for the given PhysicalFunction.",
        params = {
            @Param(name = "value", value = "the PhysicalFunction")
        },
        result = "the Sequence of involving capability realizations for the given PhysicalFunction",
        examples = {
            @Example(expression = "myPhysicalFunction.getInvolvingCapabilityRealizations()", result = "the Sequence of involving capability realizations for the given PhysicalFunction"),
        }
    )
    // @formatter:on
    public List<CapabilityRealization> getInvolvingCapabilityRealizations(PhysicalFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.LogicalAndPhysicalFunctionInvolvingCapabilityRealization()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating physical components for the given PhysicalFunction.",
        params = {
            @Param(name = "value", value = "the PhysicalFunction")
        },
        result = "the Sequence of allocating physical components for the given PhysicalFunction",
        examples = {
            @Example(expression = "myPhysicalFunction.getAllocatingPhysicalComponent()", result = "the Sequence of allocating physical components for the given PhysicalFunction"),
        }
    )
    // @formatter:on
    // TODO AbstractFunctionalBlock and Role
    public List<EObject> getAllocatingPhysicalComponent(PhysicalFunction value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingComponent()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of internal incoming functional exchanges for the given PhysicalFunction.",
        params = {
            @Param(name = "value", value = "the PhysicalFunction")
        },
        result = "the Sequence of internal incoming functional exchanges for the given PhysicalFunction",
        examples = {
            @Example(expression = "myPhysicalFunction.getInternalIncomingFunctionalExchanges()", result = "the Sequence of internal incoming functional exchanges for the given PhysicalFunction"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getInternalIncomingFunctionalExchanges(PhysicalFunction value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalInComingDataflows()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of incoming functional exchanges for the given PhysicalFunction.",
        params = {
            @Param(name = "value", value = "the PhysicalFunction")
        },
        result = "the Sequence of incoming functional exchanges for the given PhysicalFunction",
        examples = {
            @Example(expression = "myPhysicalFunction.getIncomingFunctionalExchanges()", result = "the Sequence of incoming functional exchanges for the given PhysicalFunction"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getIncomingFunctionalExchanges(PhysicalFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_incomingInteraction()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating physical actors for the given PhysicalFunction.",
        params = {
            @Param(name = "value", value = "the PhysicalFunction")
        },
        result = "the Sequence of allocating physical actors for the given PhysicalFunction",
        examples = {
            @Example(expression = "myPhysicalFunction.getAllocatingPhysicalActor()", result = "the Sequence of allocating physical actors for the given PhysicalFunction"),
        }
    )
    // @formatter:on
    // TODO Component and Part
    public List<EObject> getAllocatingPhysicalActor(PhysicalFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingActor().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of in flow ports for the given PhysicalFunction.",
        params = {
            @Param(name = "value", value = "the PhysicalFunction")
        },
        result = "the Sequence of in flow ports for the given PhysicalFunction",
        examples = {
            @Example(expression = "myPhysicalFunction.getInFlowPorts()", result = "the Sequence of in flow ports for the given PhysicalFunction"),
        }
    )
    // @formatter:on
    public List<FunctionInputPort> getInFlowPorts(PhysicalFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.Function_inFlowPorts().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involving capability realizations for the given CapabilityRealizationInvolvedElement.",
        params = {
            @Param(name = "value", value = "the CapabilityRealizationInvolvedElement")
        },
        result = "the Sequence of involving capability realizations for the given CapabilityRealizationInvolvedElement",
        examples = {
            @Example(expression = "myCapabilityRealizationInvolvedElement.getInvolvingCapabilityRealizations()", result = "the Sequence of involving capability realizations for the given CapabilityRealizationInvolvedElement"),
        }
    )
    // @formatter:on
    public List<CapabilityRealization> getInvolvingCapabilityRealizations(CapabilityRealizationInvolvedElement value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityRealizationInvolvedElement_InvolvingCapabilityRealizations()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of deployed physical components for the given PhysicalComponent.",
        params = {
            @Param(name = "value", value = "the PhysicalComponent")
        },
        result = "the Sequence of deployed physical components for the given PhysicalComponent",
        examples = {
            @Example(expression = "myPhysicalComponent.getDeployedPhysicalComponents()", result = "the Sequence of deployed physical components for the given PhysicalComponent"),
        }
    )
    // @formatter:on
    public List<Component> getDeployedPhysicalComponents(PhysicalComponent value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalComponent_deployedPhysicalComponents()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of incoming physical links for the given PhysicalComponent.",
        params = {
            @Param(name = "value", value = "the PhysicalComponent")
        },
        result = "the Sequence of incoming physical links for the given PhysicalComponent",
        examples = {
            @Example(expression = "myPhysicalComponent.getIncomingPhysicalLinks()", result = "the Sequence of incoming physical links for the given PhysicalComponent"),
        }
    )
    // @formatter:on
    public List<PhysicalLink> getIncomingPhysicalLinks(PhysicalComponent value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalComponent_IncomingPhysicalLinks()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocated physical functions for the given PhysicalComponent.",
        params = {
            @Param(name = "value", value = "the PhysicalComponent")
        },
        result = "the Sequence of allocated physical functions for the given PhysicalComponent",
        examples = {
            @Example(expression = "myPhysicalComponent.getAllocatedPhysicalFunctions()", result = "the Sequence of allocated physical functions for the given PhysicalComponent"),
        }
    )
    // @formatter:on
    public List<PhysicalFunction> getAllocatedPhysicalFunctions(PhysicalComponent value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_AllocatedFunctions()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of computed internal physical links for the given PhysicalComponent.",
        params = {
            @Param(name = "value", value = "the PhysicalComponent")
        },
        result = "the Sequence of computed internal physical links for the given PhysicalComponent",
        examples = {
            @Example(expression = "myPhysicalComponent.getInternalPhysicalLinksComputed()", result = "the Sequence of computed internal physical links for the given PhysicalComponent"),
        }
    )
    // @formatter:on
    public List<PhysicalLink> getInternalPhysicalLinksComputed(PhysicalComponent value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalComponent_InternalPhysicalLinks()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized logical components for the given PhysicalComponent.",
        params = {
            @Param(name = "value", value = "the PhysicalComponent")
        },
        result = "the Sequence of realized logical components for the given PhysicalComponent",
        examples = {
            @Example(expression = "myPhysicalComponent.getRealizedLogicalComponent()", result = "the Sequence of realized logical components for the given PhysicalComponent"),
        }
    )
    // @formatter:on
    public List<LogicalComponent> getRealizedLogicalComponent(PhysicalComponent value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizedComponents()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing components for the given PhysicalComponent.",
        params = {
            @Param(name = "value", value = "the PhysicalComponent")
        },
        result = "the Sequence of realizing components for the given PhysicalComponent",
        examples = {
            @Example(expression = "myPhysicalComponent.getRealizingComponents()", result = "the Sequence of realizing components for the given PhysicalComponent"),
        }
    )
    // @formatter:on
    public List<Component> getRealizingComponents(PhysicalComponent value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizingComponents()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of outgoing physical links for the given PhysicalComponent.",
        params = {
            @Param(name = "value", value = "the PhysicalComponent")
        },
        result = "the Sequence of outgoing physical links for the given PhysicalComponent",
        examples = {
            @Example(expression = "myPhysicalComponent.getOutgoingPhysicalLinks()", result = "the Sequence of outgoing physical links for the given PhysicalComponent"),
        }
    )
    // @formatter:on
    public List<PhysicalLink> getOutgoingPhysicalLinks(PhysicalComponent value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalComponent_OutgoingPhysicalLinks()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of deploying physical components for the given PhysicalComponent.",
        params = {
            @Param(name = "value", value = "the PhysicalComponent")
        },
        result = "the Sequence of deploying physical components for the given PhysicalComponent",
        examples = {
            @Example(expression = "myPhysicalComponent.getDeployingPhysicalComponents()", result = "the Sequence of deploying physical components for the given PhysicalComponent"),
        }
    )
    // @formatter:on
    public List<PhysicalComponent> getDeployingPhysicalComponents(PhysicalComponent value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalComponent_deployingPhysicalComponents()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing configuration items for the given PhysicalComponent.",
        params = {
            @Param(name = "value", value = "the PhysicalComponent")
        },
        result = "the Sequence of realizing configuration items for the given PhysicalComponent",
        examples = {
            @Example(expression = "myPhysicalComponent.getRealizingConfigurationItems()", result = "the Sequence of realizing configuration items for the given PhysicalComponent"),
        }
    )
    // @formatter:on
    public List<ConfigurationItem> getRealizingConfigurationItems(PhysicalComponent value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalArtifactsRealizingCI()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of type for the given Part.",
        params = {
            @Param(name = "value", value = "the Part")
        },
        result = "the Sequence of type for the given Part",
        examples = {
            @Example(expression = "myPart.getType()", result = "the Sequence of type for the given Part"),
        }
    )
    // @formatter:on
    public List<AbstractType> getType(Part value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Part_type().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of dependencies for the given AbstractDependenciesPkg.",
        params = {
            @Param(name = "value", value = "the AbstractDependenciesPkg")
        },
        result = "the Sequence of dependencies for the given AbstractDependenciesPkg",
        examples = {
            @Example(expression = "myAbstractDependenciesPkg.getDependencies()", result = "the Sequence of dependencies for the given AbstractDependenciesPkg"),
        }
    )
    // @formatter:on
    public List<AbstractDependenciesPkg> getDependencies(AbstractDependenciesPkg value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractDependenciesPkg_dependencies()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of inverse dependencies for the given AbstractDependenciesPkg.",
        params = {
            @Param(name = "value", value = "the AbstractDependenciesPkg")
        },
        result = "the Sequence of inverse dependencies for the given AbstractDependenciesPkg",
        examples = {
            @Example(expression = "myAbstractDependenciesPkg.getInverseDependencies()", result = "the Sequence of inverse dependencies for the given AbstractDependenciesPkg"),
        }
    )
    // @formatter:on
    public List<AbstractDependenciesPkg> getInverseDependencies(AbstractDependenciesPkg value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractDependenciesPkg_inverse_dependencies()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of exchange items for the given Interface.",
        params = {
            @Param(name = "value", value = "the Interface")
        },
        result = "the Sequence of exchange items for the given Interface",
        examples = {
            @Example(expression = "myInterface.getExchangeItems()", result = "the Sequence of exchange items for the given Interface"),
        }
    )
    // @formatter:on
    public List<ExchangeItem> getExchangeItems(Interface value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.InterfaceExchangesItems().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of refined interfaces for the given Interface.",
        params = {
            @Param(name = "value", value = "the Interface")
        },
        result = "the Sequence of refined interfaces for the given Interface",
        examples = {
            @Example(expression = "myInterface.getRefinedInterfaces()", result = "the Sequence of refined interfaces for the given Interface"),
        }
    )
    // @formatter:on
    public List<Interface> getRefinedInterfaces(Interface value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Interface_provisionedInterfaces()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of inherited exchange items for the given Interface.",
        params = {
            @Param(name = "value", value = "the Interface")
        },
        result = "the Sequence of inherited exchange items for the given Interface",
        examples = {
            @Example(expression = "myInterface.getInheritedExchangeItems()", result = "the Sequence of inherited exchange items for the given Interface"),
        }
    )
    // @formatter:on
    public List<ExchangeItem> getInheritedExchangeItems(Interface value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.InterfaceInheritedExchangesItems()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of users for the given Interface.",
        params = {
            @Param(name = "value", value = "the Interface")
        },
        result = "the Sequence of users for the given Interface",
        examples = {
            @Example(expression = "myInterface.getUsers()", result = "the Sequence of users for the given Interface"),
        }
    )
    // @formatter:on
    public List<Component> getUsers(Interface value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.InterfaceUsers().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of refining interfaces for the given Interface.",
        params = {
            @Param(name = "value", value = "the Interface")
        },
        result = "the Sequence of refining interfaces for the given Interface",
        examples = {
            @Example(expression = "myInterface.getRefiningInterfaces()", result = "the Sequence of refining interfaces for the given Interface"),
        }
    )
    // @formatter:on
    public List<Interface> getRefiningInterfaces(Interface value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Interface_provisioningInterfaces()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involving scenarii for the given Interface.",
        params = {
            @Param(name = "value", value = "the Interface")
        },
        result = "the Sequence of involving scenarii for the given Interface",
        examples = {
            @Example(expression = "myInterface.getInvolvingScenarios()", result = "the Sequence of involving scenarii for the given Interface"),
        }
    )
    // @formatter:on
    public List<Scenario> getInvolvingScenarios(Interface value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Interface_involvingScenarios()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of providers for the given Interface.",
        params = {
            @Param(name = "value", value = "the Interface")
        },
        result = "the Sequence of providers for the given Interface",
        examples = {
            @Example(expression = "myInterface.getProviders()", result = "the Sequence of providers for the given Interface"),
        }
    )
    // @formatter:on
    public List<ComponentPort> getProviders(Interface value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.InterfaceProviders().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of requirers for the given Interface.",
        params = {
            @Param(name = "value", value = "the Interface")
        },
        result = "the Sequence of requirers for the given Interface",
        examples = {
            @Example(expression = "myInterface.getRequirers()", result = "the Sequence of requirers for the given Interface"),
        }
    )
    // @formatter:on
    public List<ComponentPort> getRequirers(Interface value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.InterfaceRequires().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of implementors for the given Interface.",
        params = {
            @Param(name = "value", value = "the Interface")
        },
        result = "the Sequence of implementors for the given Interface",
        examples = {
            @Example(expression = "myInterface.getImplementors()", result = "the Sequence of implementors for the given Interface"),
        }
    )
    // @formatter:on
    public List<Component> getImplementors(Interface value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.InterfaceImplementors().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of targets for the given InterfaceImplementation.",
        params = {
            @Param(name = "value", value = "the InterfaceImplementation")
        },
        result = "the Sequence of targets for the given InterfaceImplementation",
        examples = {
            @Example(expression = "myInterface.getTarget()", result = "the Sequence of targets for the given InterfaceImplementation"),
        }
    )
    // @formatter:on
    public List<Interface> getTarget(InterfaceImplementation value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsInterfaceImplementationTarget()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of sources for the given InterfaceImplementation.",
        params = {
            @Param(name = "value", value = "the InterfaceImplementation")
        },
        result = "the Sequence of sources for the given InterfaceImplementation",
        examples = {
            @Example(expression = "myInterface.getSource()", result = "the Sequence of sources for the given InterfaceImplementation"),
        }
    )
    // @formatter:on
    public List<Component> getSource(InterfaceImplementation value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsInterfaceImplementationSource()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of targets for the given InterfaceUse.",
        params = {
            @Param(name = "value", value = "the InterfaceUse")
        },
        result = "the Sequence of targets for the given InterfaceUse",
        examples = {
            @Example(expression = "myInterfaceUse.getTarget()", result = "the Sequence of targets for the given InterfaceUse"),
        }
    )
    // @formatter:on
    public List<Interface> getTarget(InterfaceUse value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsInterfaceUseTarget()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of sources for the given InterfaceUse.",
        params = {
            @Param(name = "value", value = "the InterfaceUse")
        },
        result = "the Sequence of sources for the given InterfaceUse",
        examples = {
            @Example(expression = "myInterfaceUse.getSource()", result = "the Sequence of sources for the given InterfaceUse"),
        }
    )
    // @formatter:on
    public List<Component> getSource(InterfaceUse value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsInterfaceUseSource()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of exchange items for the given ExchangeItemAllocation.",
        params = {
            @Param(name = "value", value = "the ExchangeItemAllocation")
        },
        result = "the Sequence of exchange items for the given ExchangeItemAllocation",
        examples = {
            @Example(expression = "myExchangeItemAllocation.getExchangeItem()", result = "the Sequence of exchange items for the given ExchangeItemAllocation"),
        }
    )
    // @formatter:on
    public List<AbstractExchangeItem> getExchangeItem(ExchangeItemAllocation value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeItemAllocationExchangeItem()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of scenarii for the given ExchangeItemAllocation.",
        params = {
            @Param(name = "value", value = "the ExchangeItemAllocation")
        },
        result = "the Sequence of scenarii for the given ExchangeItemAllocation",
        examples = {
            @Example(expression = "myExchangeItemAllocation.getScenarios()", result = "the Sequence of scenarii for the given ExchangeItemAllocation"),
        }
    )
    // @formatter:on
    public List<Scenario> getScenarios(ExchangeItemAllocation value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of inherited categories for the given PhysicalLink.",
        params = {
            @Param(name = "value", value = "the PhysicalLink")
        },
        result = "the Sequence of inherited categories for the given PhysicalLink",
        examples = {
            @Example(expression = "myPhysicalLink.getScenarios()", result = "the Sequence of inherited categories for the given PhysicalLink"),
        }
    )
    // @formatter:on
    public List<PhysicalLinkCategory> getInheritedCategories(PhysicalLink value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalLinkCategoriesForDelegations()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocated component exchanges for the given PhysicalLink.",
        params = {
            @Param(name = "value", value = "the PhysicalLink")
        },
        result = "the Sequence of allocated component exchanges for the given PhysicalLink",
        examples = {
            @Example(expression = "myPhysicalLink.getAllocatedComponentExchanges()", result = "the Sequence of allocated component exchanges for the given PhysicalLink"),
        }
    )
    // @formatter:on
    public List<TraceableElement> getAllocatedComponentExchanges(PhysicalLink value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalLinksRealizedConnection()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of categories for the given PhysicalLink.",
        params = {
            @Param(name = "value", value = "the PhysicalLink")
        },
        result = "the Sequence of categories for the given PhysicalLink",
        examples = {
            @Example(expression = "myPhysicalLink.getCategories()", result = "the Sequence of categories for the given PhysicalLink"),
        }
    )
    // @formatter:on
    public List<PhysicalLinkCategory> getCategories(PhysicalLink value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalLinkCategories().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of physical links ends for the given PhysicalLink.",
        params = {
            @Param(name = "value", value = "the PhysicalLink")
        },
        result = "the Sequence of physical links ends for the given PhysicalLink",
        examples = {
            @Example(expression = "myPhysicalLink.getPhysicalLinkEnds()", result = "the Sequence of physical links ends for the given PhysicalLink"),
        }
    )
    // @formatter:on
    public List<Component> getPhysicalLinkEnds(PhysicalLink value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalLinkSourceAndTarget()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing configuration items for the given PhysicalLink.",
        params = {
            @Param(name = "value", value = "the PhysicalLink")
        },
        result = "the Sequence of realizing configuration items for the given PhysicalLink",
        examples = {
            @Example(expression = "myPhysicalLink.getRealizingConfigurationItems()", result = "the Sequence of realizing configuration items for the given PhysicalLink"),
        }
    )
    // @formatter:on
    public List<ConfigurationItem> getRealizingConfigurationItems(PhysicalLink value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalArtifactsRealizingCI()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of physical paths for the given PhysicalLink.",
        params = {
            @Param(name = "value", value = "the PhysicalLink")
        },
        result = "the Sequence of physical paths for the given PhysicalLink",
        examples = {
            @Example(expression = "myPhysicalLink.getPhysicalPaths()", result = "the Sequence of physical paths for the given PhysicalLink"),
        }
    )
    // @formatter:on
    public List<PhysicalPath> getPhysicalPaths(PhysicalLink value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalLinksInvolvedInPhysicalPaths()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of physical links for the given PhysicalLinkCategory.",
        params = {
            @Param(name = "value", value = "the PhysicalLinkCategory")
        },
        result = "the Sequence of physical links for the given PhysicalLinkCategory",
        examples = {
            @Example(expression = "myPhysicalLinkCategory.getPhysicalLinks()", result = "the Sequence of physical links for the given PhysicalLinkCategory"),
        }
    )
    // @formatter:on
    public List<PhysicalLink> getPhysicalLinks(PhysicalLinkCategory value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CategoryPhysicalLink().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved physical paths for the given PhysicalPath.",
        params = {
            @Param(name = "value", value = "the PhysicalPath")
        },
        result = "the Sequence of involved physical paths for the given PhysicalPath",
        examples = {
            @Example(expression = "myPhysicalPath.getInvolvedPhysicalPaths()", result = "the Sequence of involved physical paths for the given PhysicalPath"),
        }
    )
    // @formatter:on
    public List<PhysicalPath> getInvolvedPhysicalPaths(PhysicalPath value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPathInvolvedPhysicalPath()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved physical links for the given PhysicalPath.",
        params = {
            @Param(name = "value", value = "the PhysicalPath")
        },
        result = "the Sequence of involved physical links for the given PhysicalPath",
        examples = {
            @Example(expression = "myPhysicalPath.getInvolvedPhysicalLinks()", result = "the Sequence of involved physical links for the given PhysicalPath"),
        }
    )
    // @formatter:on
    public List<PhysicalLink> getInvolvedPhysicalLinks(PhysicalPath value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPath_PhysicalLinks()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocated component exchanges for the given PhysicalPath.",
        params = {
            @Param(name = "value", value = "the PhysicalPath")
        },
        result = "the Sequence of allocated component exchanges for the given PhysicalPath",
        examples = {
            @Example(expression = "myPhysicalPath.getAllocatedComponentExchanges()", result = "the Sequence of allocated component exchanges for the given PhysicalPath"),
        }
    )
    // @formatter:on
    public List<ComponentExchange> getAllocatedComponentExchanges(PhysicalPath value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPath_RealisedConnection()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of physical paths for the given PhysicalPath.",
        params = {
            @Param(name = "value", value = "the PhysicalPath")
        },
        result = "the Sequence of physical paths for the given PhysicalPath",
        examples = {
            @Example(expression = "myPhysicalPath.getPhysicalPaths()", result = "the Sequence of physical paths for the given PhysicalPath"),
        }
    )
    // @formatter:on
    public List<PhysicalPath> getPhysicalPaths(PhysicalPath value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPathInvolvingPhysicalPath()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved physical paths for the given PhysicalPathInvolvement.",
        params = {
            @Param(name = "value", value = "the PhysicalPathInvolvement")
        },
        result = "the Sequence of involved physical paths for the given PhysicalPathInvolvement",
        examples = {
            @Example(expression = "myPhysicalPathInvolvement.getInvolvedPhysicalPath()", result = "the Sequence of involved physical paths for the given PhysicalPathInvolvement"),
        }
    )
    // @formatter:on
    public List<PhysicalPath> getInvolvedPhysicalPath(PhysicalPathInvolvement value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPathInvolvmentInvolvedPhysicalPath()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved physical components for the given PhysicalPathInvolvement.",
        params = {
            @Param(name = "value", value = "the PhysicalPathInvolvement")
        },
        result = "the Sequence of involved physical components for the given PhysicalPathInvolvement",
        examples = {
            @Example(expression = "myPhysicalPathInvolvement.getInvolvedPhysicalComponent()", result = "the Sequence of involved physical components for the given PhysicalPathInvolvement"),
        }
    )
    // @formatter:on
    public List<AbstractPathInvolvedElement> getInvolvedPhysicalComponent(PhysicalPathInvolvement value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPathInvolvmentPhysicalComp()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved physical links for the given PhysicalPathInvolvement.",
        params = {
            @Param(name = "value", value = "the PhysicalPathInvolvement")
        },
        result = "the Sequence of involved physical links for the given PhysicalPathInvolvement",
        examples = {
            @Example(expression = "myPhysicalPathInvolvement.getInvolvedPhysicalLink()", result = "the Sequence of involved physical links for the given PhysicalPathInvolvement"),
        }
    )
    // @formatter:on
    public List<PhysicalLink> getInvolvedPhysicalLink(PhysicalPathInvolvement value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPathInvolvmentPhysicalLink()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocated function ports links for the given PhysicalPort.",
        params = {
            @Param(name = "value", value = "the PhysicalPort")
        },
        result = "the Sequence of allocated function ports for the given PhysicalPort",
        examples = {
            @Example(expression = "myPhysicalPort.getAllocatedFunctionPorts()", result = "the Sequence of allocated function ports for the given PhysicalPort"),
        }
    )
    // @formatter:on
    public List<FunctionPort> getAllocatedFunctionPorts(PhysicalPort value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPortOutgoingFunctionPorts()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocated component ports links for the given PhysicalPort.",
        params = {
            @Param(name = "value", value = "the PhysicalPort")
        },
        result = "the Sequence of allocated component ports for the given PhysicalPort",
        examples = {
            @Example(expression = "myPhysicalPort.getAllocatedComponentPorts()", result = "the Sequence of allocated component ports for the given PhysicalPort"),
        }
    )
    // @formatter:on
    public List<ComponentPort> getAllocatedComponentPorts(PhysicalPort value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPortAllocatedComponentPorts()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of outgoing delegations links for the given PhysicalPort.",
        params = {
            @Param(name = "value", value = "the PhysicalPort")
        },
        result = "the Sequence of outgoing delegations for the given PhysicalPort",
        examples = {
            @Example(expression = "myPhysicalPort.getOutgoingDelegations()", result = "the Sequence of outgoing delegations for the given PhysicalPort"),
        }
    )
    // @formatter:on
    public List<ComponentExchange> getOutgoingDelegations(PhysicalPort value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPortOutgoingDelgations()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of physical links for the given PhysicalPort.",
        params = {
            @Param(name = "value", value = "the PhysicalPort")
        },
        result = "the Sequence of physical links for the given PhysicalPort",
        examples = {
            @Example(expression = "myPhysicalPort.getPhysicalLinks()", result = "the Sequence of physical links for the given PhysicalPort"),
        }
    )
    // @formatter:on
    public List<PhysicalLink> getPhysicalLinks(PhysicalPort value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalPortIncomingPhysicalLinks()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing configuration items for the given PhysicalPort.",
        params = {
            @Param(name = "value", value = "the PhysicalPort")
        },
        result = "the Sequence of realizing configuration items for the given PhysicalPort",
        examples = {
            @Example(expression = "myPhysicalPort.getRealizingConfigurationItems()", result = "the Sequence of realizing configuration items for the given PhysicalPort"),
        }
    )
    // @formatter:on
    public List<ConfigurationItem> getRealizingConfigurationItems(PhysicalPort value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.PhysicalArtifactsRealizingCI()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of owned functional chains for the given LogicalFunction.",
        params = {
            @Param(name = "value", value = "the LogicalFunction")
        },
        result = "the Sequence of owned functional chains for the given LogicalFunction",
        examples = {
            @Example(expression = "myLogicalFunction.getOwnedFunctionalChains()", result = "the Sequence of owned functional chains for the given LogicalFunction"),
        }
    )
    // @formatter:on
    public List<FunctionalChain> getOwnedFunctionalChains(LogicalFunction value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Function_ownedFunctionalChains()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of outgoing functional exchanges for the given LogicalFunction.",
        params = {
            @Param(name = "value", value = "the LogicalFunction")
        },
        result = "the Sequence of outgoing functional exchanges for the given LogicalFunction",
        examples = {
            @Example(expression = "myLogicalFunction.getOutgoingFunctionalExchanges()", result = "the Sequence of outgoing functional exchanges for the given LogicalFunction"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getOutgoingFunctionalExchanges(LogicalFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_outgoingInteraction()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of out flow ports for the given LogicalFunction.",
        params = {
            @Param(name = "value", value = "the LogicalFunction")
        },
        result = "the Sequence of out flow ports for the given LogicalFunction",
        examples = {
            @Example(expression = "myLogicalFunction.getOutFlowPorts()", result = "the Sequence of out flow ports for the given LogicalFunction"),
        }
    )
    // @formatter:on
    public List<FunctionOutputPort> getOutFlowPorts(LogicalFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.Function_outFlowPorts().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of outgoing functional exchanges for the given LogicalFunction.",
        params = {
            @Param(name = "value", value = "the LogicalFunction")
        },
        result = "the Sequence of outgoing functional exchanges for the given LogicalFunction",
        examples = {
            @Example(expression = "myLogicalFunction.getInternalOutgoingFunctionalExchanges()", result = "the Sequence of outgoing functional exchanges for the given LogicalFunction"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getInternalOutgoingFunctionalExchanges(LogicalFunction value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalOutGoingDataflows()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized system functions for the given LogicalFunction.",
        params = {
            @Param(name = "value", value = "the LogicalFunction")
        },
        result = "the Sequence of realized system functions for the given LogicalFunction",
        examples = {
            @Example(expression = "myLogicalFunction.getRealizedSystemFunctions()", result = "the Sequence of realized system functions for the given LogicalFunction"),
        }
    )
    // @formatter:on
    public List<SystemFunction> getRealizedSystemFunctions(LogicalFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_realizedFunctions()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing physical functions for the given LogicalFunction.",
        params = {
            @Param(name = "value", value = "the LogicalFunction")
        },
        result = "the Sequence of realizing physical functions for the given LogicalFunction",
        examples = {
            @Example(expression = "myLogicalFunction.getRealizingPhysicalFunctions()", result = "the Sequence of realizing physical functions for the given LogicalFunction"),
        }
    )
    // @formatter:on
    public List<PhysicalFunction> getRealizingPhysicalFunctions(LogicalFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_realizingFunctions()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating logical actors for the given LogicalFunction.",
        params = {
            @Param(name = "value", value = "the LogicalFunction")
        },
        result = "the Sequence of allocating logical actors for the given LogicalFunction",
        examples = {
            @Example(expression = "myLogicalFunction.getAllocatingLogicalActor()", result = "the Sequence of allocating logical actors for the given LogicalFunction"),
        }
    )
    // @formatter:on
    // TODO Component and Part
    public List<EObject> getAllocatingLogicalActor(LogicalFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingActor().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of incoming functional exchanges for the given LogicalFunction.",
        params = {
            @Param(name = "value", value = "the LogicalFunction")
        },
        result = "the Sequence of incoming functional exchanges for the given LogicalFunction",
        examples = {
            @Example(expression = "myLogicalFunction.getInternalIncomingFunctionalExchanges()", result = "the Sequence of incoming functional exchanges for the given LogicalFunction"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getInternalIncomingFunctionalExchanges(LogicalFunction value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalInComingDataflows()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of in flow ports for the given LogicalFunction.",
        params = {
            @Param(name = "value", value = "the LogicalFunction")
        },
        result = "the Sequence of in flow ports for the given LogicalFunction",
        examples = {
            @Example(expression = "myLogicalFunction.getInFlowPorts()", result = "the Sequence of in flow ports for the given LogicalFunction"),
        }
    )
    // @formatter:on
    public List<FunctionInputPort> getInFlowPorts(LogicalFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.Function_inFlowPorts().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating logical components for the given LogicalFunction.",
        params = {
            @Param(name = "value", value = "the LogicalFunction")
        },
        result = "the Sequence of allocating logical components for the given LogicalFunction",
        examples = {
            @Example(expression = "myLogicalFunction.getAllocatingLogicalComponent()", result = "the Sequence of allocating logical components for the given LogicalFunction"),
        }
    )
    // @formatter:on
    // TODO Component and Part
    public List<EObject> getAllocatingLogicalComponent(LogicalFunction value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingComponent()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involving capability realizations for the given LogicalFunction.",
        params = {
            @Param(name = "value", value = "the LogicalFunction")
        },
        result = "the Sequence of involving capability realizations for the given LogicalFunction",
        examples = {
            @Example(expression = "myLogicalFunction.getInvolvingCapabilityRealizations()", result = "the Sequence of involving capability realizations for the given LogicalFunction"),
        }
    )
    // @formatter:on
    public List<CapabilityRealization> getInvolvingCapabilityRealizations(LogicalFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.LogicalAndPhysicalFunctionInvolvingCapabilityRealization()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of incoming functional exchanges for the given LogicalFunction.",
        params = {
            @Param(name = "value", value = "the LogicalFunction")
        },
        result = "the Sequence of incoming functional exchanges for the given LogicalFunction",
        examples = {
            @Example(expression = "myLogicalFunction.getIncomingFunctionalExchanges()", result = "the Sequence of incoming functional exchanges for the given LogicalFunction"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getIncomingFunctionalExchanges(LogicalFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_incomingInteraction()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized system components for the given LogicalFunction.",
        params = {
            @Param(name = "value", value = "the LogicalFunction")
        },
        result = "the Sequence of realized system components for the given LogicalFunction",
        examples = {
            @Example(expression = "myLogicalFunction.getRealizedSystemComponents()", result = "the Sequence of realized system components for the given LogicalFunction"),
        }
    )
    // @formatter:on
    public List<SystemComponent> getRealizedSystemComponents(LogicalComponent value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizedComponents()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocated logical functions for the given LogicalFunction.",
        params = {
            @Param(name = "value", value = "the LogicalFunction")
        },
        result = "the Sequence of realized system components for the given LogicalFunction",
        examples = {
            @Example(expression = "myLogicalFunction.getAllocatedLogicalFunctions()", result = "the Sequence of realized system components for the given LogicalFunction"),
        }
    )
    // @formatter:on
    public List<LogicalFunction> getAllocatedLogicalFunctions(LogicalComponent value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_AllocatedFunctions()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing physical components for the given LogicalFunction.",
        params = {
            @Param(name = "value", value = "the LogicalFunction")
        },
        result = "the Sequence of realizing physical components for the given LogicalFunction",
        examples = {
            @Example(expression = "myLogicalFunction.getRealizingPhysicalComponents()", result = "the Sequence of realizing physical components for the given LogicalFunction"),
        }
    )
    // @formatter:on
    public List<PhysicalComponent> getRealizingPhysicalComponents(LogicalComponent value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizingComponents()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of owned functional chains for the given CapabilityRealization.",
        params = {
            @Param(name = "value", value = "the CapabilityRealization")
        },
        result = "the Sequence of owned functional chains for the given CapabilityRealization",
        examples = {
            @Example(expression = "myCapabilityRealization.getOwnedFunctionalChains()", result = "the Sequence of owned functional chains for the given CapabilityRealization"),
        }
    )
    // @formatter:on
    public List<FunctionalChain> getOwnedFunctionalChains(CapabilityRealization value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityOwnedFunctionalChains()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved functional chains for the given CapabilityRealization.",
        params = {
            @Param(name = "value", value = "the CapabilityRealization")
        },
        result = "the Sequence of involved functional chains for the given CapabilityRealization",
        examples = {
            @Example(expression = "myCapabilityRealization.getInvolvedFunctionalChains()", result = "the Sequence of involved functional chains for the given CapabilityRealization"),
        }
    )
    // @formatter:on
    public List<FunctionalChain> getInvolvedFunctionalChains(CapabilityRealization value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityInvolvedFunctionalChains()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized capabilities for the given CapabilityRealization.",
        params = {
            @Param(name = "value", value = "the CapabilityRealization")
        },
        result = "the Sequence of realized capabilities for the given CapabilityRealization",
        examples = {
            @Example(expression = "myCapabilityRealization.getRealizedCapabilities()", result = "the Sequence of realized capabilities for the given CapabilityRealization"),
        }
    )
    // @formatter:on
    public List<Capability> getRealizedCapabilities(CapabilityRealization value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityRealization_RealizedCapability()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized capabilities realizations for the given CapabilityRealization.",
        params = {
            @Param(name = "value", value = "the CapabilityRealization")
        },
        result = "the Sequence of realized capabilities realizations for the given CapabilityRealization",
        examples = {
            @Example(expression = "myCapabilityRealization.getRealizedCapabilityRealizations()", result = "the Sequence of realized capabilities realizations for the given CapabilityRealization"),
        }
    )
    // @formatter:on
    public List<CapabilityRealization> getRealizedCapabilityRealizations(CapabilityRealization value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityRealization_RealizedCapabilityRealization()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved logical functions for the given CapabilityRealization.",
        params = {
            @Param(name = "value", value = "the CapabilityRealization")
        },
        result = "the Sequence of involved logical functions for the given CapabilityRealization",
        examples = {
            @Example(expression = "myCapabilityRealization.getInvolvedLogicalFunctions()", result = "the Sequence of involved logical functions for the given CapabilityRealization"),
        }
    )
    // @formatter:on
    public List<LogicalFunction> getInvolvedLogicalFunctions(CapabilityRealization value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.LAAbstractCapabilityInvolvedFunctions()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved physical functions for the given CapabilityRealization.",
        params = {
            @Param(name = "value", value = "the CapabilityRealization")
        },
        result = "the Sequence of involved physical functions for the given CapabilityRealization",
        examples = {
            @Example(expression = "myCapabilityRealization.getInvolvedPhysicalFunctions()", result = "the Sequence of involved physical functions for the given CapabilityRealization"),
        }
    )
    // @formatter:on
    public List<PhysicalFunction> getInvolvedPhysicalFunctions(CapabilityRealization value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.PAAbstractCapabilityInvolvedFunctions()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing capability realizations for the given CapabilityRealization.",
        params = {
            @Param(name = "value", value = "the CapabilityRealization")
        },
        result = "the Sequence of realizing capability realizations for the given CapabilityRealization",
        examples = {
            @Example(expression = "myCapabilityRealization.getRealizingCapabilityRealizations()", result = "the Sequence of realizing capability realizations for the given CapabilityRealization"),
        }
    )
    // @formatter:on
    public List<CapabilityRealization> getRealizingCapabilityRealizations(CapabilityRealization value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityRealization_RealizingCapabilityRealization()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of values for the given DataValue.",
        params = {
            @Param(name = "value", value = "the DataValue")
        },
        result = "the Sequence of values for the given DataValue",
        examples = {
            @Example(expression = "myDataValue.getValue()", result = "the Sequence of values for the given DataValue"),
        }
    )
    // @formatter:on
    public List<String> getValue(DataValue value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.PropertyValue_applying_valued_element_DataValue()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of referenced properties for the given DataValue.",
        params = {
            @Param(name = "value", value = "the DataValue")
        },
        result = "the Sequence of referenced properties for the given DataValue",
        examples = {
            @Example(expression = "myDataValue.getReferencedProperty()", result = "the Sequence of referenced properties for the given DataValue"),
        }
    )
    // @formatter:on
    public List<Property> getReferencedProperty(DataValue value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.DataValueRefReferencedProperty()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of referenced values for the given DataValue.",
        params = {
            @Param(name = "value", value = "the DataValue")
        },
        result = "the Sequence of referenced values for the given DataValue",
        examples = {
            @Example(expression = "myDataValue.getReferencedValue()", result = "the Sequence of referenced values for the given DataValue"),
        }
    )
    // @formatter:on
    public List<DataValue> getReferencedValue(DataValue value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.DataValueRefReferencedValue()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of referencing values for the given DataValue.",
        params = {
            @Param(name = "value", value = "the DataValue")
        },
        result = "the Sequence of referencing values for the given DataValue",
        examples = {
            @Example(expression = "myDataValue.getReferencingValue()", result = "the Sequence of referencing values for the given DataValue"),
        }
    )
    // @formatter:on
    public List<Object> getReferencingValue(DataValue value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.DataValueReferencingReferencedValue()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of targets for the given Trace.",
        params = {
            @Param(name = "value", value = "the Trace")
        },
        result = "the Sequence of targets for the given Trace",
        examples = {
            @Example(expression = "myTrace.getTarget()", result = "the Sequence of targets for the given Trace"),
        }
    )
    // @formatter:on
    public List<TraceableElement> getTarget(Trace value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsTraceTarget()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of sources for the given Trace.",
        params = {
            @Param(name = "value", value = "the Trace")
        },
        result = "the Sequence of sources for the given Trace",
        examples = {
            @Example(expression = "myTrace.getSource()", result = "the Sequence of sources for the given Trace"),
        }
    )
    // @formatter:on
    public List<TraceableElement> getSource(Trace value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsTraceSource()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of traced elements for the given Requirement.",
        params = {
            @Param(name = "value", value = "the Requirement")
        },
        result = "the Sequence of traced elements for the given Requirement",
        examples = {
            @Example(expression = "myRequirement.getTracedElements()", result = "the Sequence of traced elements for the given Requirement"),
        }
    )
    // @formatter:on
    public List<TraceableElement> getTracedElements(Requirement value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.RequirementTracedElements()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of functional echanges for the given ExchangeCategory.",
        params = {
            @Param(name = "value", value = "the ExchangeCategory")
        },
        result = "the Sequence of functional echanges for the given ExchangeCategory",
        examples = {
            @Example(expression = "myExchangeCategory.getFunctionalExchanges()", result = "the Sequence of functional echanges for the given ExchangeCategory"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getFunctionalExchanges(ExchangeCategory value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CategoryFunctionalExchange()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved functions for the given FunctionalChainReference.",
        params = {
            @Param(name = "value", value = "the FunctionalChainReference")
        },
        result = "the Sequence of involved functions for the given FunctionalChainReference",
        examples = {
            @Example(expression = "myFunctionalChainReference.getInvolvedFunctions()", result = "the Sequence of involved functions for the given FunctionalChainReference"),
        }
    )
    // @formatter:on
    public List<FunctionalChainInvolvementFunction> getInvolvedFunctions(FunctionalChainReference value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvementFunctions()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved functional chains for the given FunctionalChainReference.",
        params = {
            @Param(name = "value", value = "the FunctionalChainReference")
        },
        result = "the Sequence of involved functional chains for the given FunctionalChainReference",
        examples = {
            @Example(expression = "myFunctionalChainReference.getInvolvedFunctionalChains()", result = "the Sequence of involved functional chains for the given FunctionalChainReference"),
        }
    )
    // @formatter:on
    public List<FunctionalChainReference> getInvolvedFunctionalChains(FunctionalChainReference value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainChildren().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved links for the given FunctionalChainReference.",
        params = {
            @Param(name = "value", value = "the FunctionalChainReference")
        },
        result = "the Sequence of involved links for the given FunctionalChainReference",
        examples = {
            @Example(expression = "myFunctionalChainReference.getInvolvementLinks()", result = "the Sequence of involved links for the given FunctionalChainReference"),
        }
    )
    // @formatter:on
    public List<FunctionalChainInvolvementLink> getInvolvementLinks(FunctionalChainReference value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvementLinks()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of parent fuctional chains for the given FunctionalChainReference.",
        params = {
            @Param(name = "value", value = "the FunctionalChainReference")
        },
        result = "the Sequence of parent fuctional chains for the given FunctionalChainReference",
        examples = {
            @Example(expression = "myFunctionalChainReference.getParentFunctionalChains()", result = "the Sequence of parent fuctional chains for the given FunctionalChainReference"),
        }
    )
    // @formatter:on
    public List<FunctionalChainReference> getParentFunctionalChains(FunctionalChainReference value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainParent().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized function ports for the given FunctionPort.",
        params = {
            @Param(name = "value", value = "the FunctionPort")
        },
        result = "the Sequence of realized function ports for the given FunctionPort",
        examples = {
            @Example(expression = "myFunctionPort.getRealizedFunctionPorts()", result = "the Sequence of realized function ports for the given FunctionPort"),
        }
    )
    // @formatter:on
    public List<FunctionPort> getRealizedFunctionPorts(FunctionPort value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionPortRealizedFunctionPort()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of exchange items for the given FunctionPort.",
        params = {
            @Param(name = "value", value = "the FunctionPort")
        },
        result = "the Sequence of exchange items for the given FunctionPort",
        examples = {
            @Example(expression = "myFunctionPort.getExchangeItems()", result = "the Sequence of exchange items for the given FunctionPort"),
        }
    )
    // @formatter:on
    public List<ExchangeItem> getExchangeItems(FunctionPort value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionPortAllocatedExchangeItems()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing function ports for the given FunctionPort.",
        params = {
            @Param(name = "value", value = "the FunctionPort")
        },
        result = "the Sequence of realizing function ports for the given FunctionPort",
        examples = {
            @Example(expression = "myFunctionPort.getRealizingFunctionPorts()", result = "the Sequence of realizing function ports for the given FunctionPort"),
        }
    )
    // @formatter:on
    public List<FunctionPort> getRealizingFunctionPorts(FunctionPort value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionPortRealizingFunctionPort()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating component ports for the given FunctionPort.",
        params = {
            @Param(name = "value", value = "the FunctionPort")
        },
        result = "the Sequence of allocating component ports for the given FunctionPort",
        examples = {
            @Example(expression = "myFunctionPort.getAllocatingComponentPorts()", result = "the Sequence of allocating component ports for the given FunctionPort"),
        }
    )
    // @formatter:on
    public List<ComponentPort> getAllocatingComponentPorts(FunctionPort value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionPortAllocatingCompoentPort()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of type for the given Pin.",
        params = {
            @Param(name = "value", value = "the Pin")
        },
        result = "the Sequence of type for the given Pin",
        examples = {
            @Example(expression = "myPin.getType()", result = "the Sequence of type for the given Pin"),
        }
    )
    // @formatter:on
    public List<AbstractType> getType(Pin value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Pin_type().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of owner for the given Pin.",
        params = {
            @Param(name = "value", value = "the Pin")
        },
        result = "the Sequence of owner for the given Pin",
        examples = {
            @Example(expression = "myPin.getOwner()", result = "the Sequence of owner for the given Pin"),
        }
    )
    // @formatter:on
    public List<EObject> getOwner(Pin value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Pin_owner().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized pins for the given Pin.",
        params = {
            @Param(name = "value", value = "the Pin")
        },
        result = "the Sequence of realized pins for the given Pin",
        examples = {
            @Example(expression = "myPin.getRealizedPin()", result = "the Sequence of realized pins for the given Pin"),
        }
    )
    // @formatter:on
    public List<EObject> getRealizedPin(Pin value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.Pin_realizedFlowPort().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of outgoing functional exchanges for the given Pin.",
        params = {
            @Param(name = "value", value = "the Pin")
        },
        result = "the Sequence of outgoing functional exchanges for the given Pin",
        examples = {
            @Example(expression = "myPin.getOutgoingFunctionalExchanges()", result = "the Sequence of outgoing functional exchanges for the given Pin"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getOutgoingFunctionalExchanges(Pin value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.Pin_outgoingDataflows().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of incoming functional exchanges for the given Pin.",
        params = {
            @Param(name = "value", value = "the Pin")
        },
        result = "the Sequence of incoming functional exchanges for the given Pin",
        examples = {
            @Example(expression = "myPin.getIncomingFunctionalExchanges()", result = "the Sequence of incoming functional exchanges for the given Pin"),
        }
    )
    // @formatter:on
    public List<Object> getIncomingFunctionalExchanges(Pin value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.Pin_incomingDataflows().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing pins for the given Pin.",
        params = {
            @Param(name = "value", value = "the Pin")
        },
        result = "the Sequence of realizing pins for the given Pin",
        examples = {
            @Example(expression = "myPin.getIncomingFunctionalExchanges()", result = "the Sequence of realizing pins for the given Pin"),
        }
    )
    // @formatter:on
    public List<EObject> getRealizingPin(Pin value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.Pin_realizingFlowPort().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of related data for the given FunctionalExchange.",
        params = {
            @Param(name = "value", value = "the FunctionalExchange")
        },
        result = "the Sequence of related data for the given FunctionalExchange",
        examples = {
            @Example(expression = "myFunctionalExchange.getIncomingFunctionalExchanges()", result = "the Sequence of related data for the given FunctionalExchange"),
        }
    )
    // @formatter:on
    public List<Type> getRelatedData(FunctionalExchange value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchange_relatedData()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of owners for the given FunctionalExchange.",
        params = {
            @Param(name = "value", value = "the FunctionalExchange")
        },
        result = "the Sequence of owners for the given FunctionalExchange",
        examples = {
            @Example(expression = "myFunctionalExchange.getOwner()", result = "the Sequence of owners for the given FunctionalExchange"),
        }
    )
    // @formatter:on
    public List<EObject> getOwner(FunctionalExchange value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchange_owner().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of exchange items for the given FunctionalExchange.",
        params = {
            @Param(name = "value", value = "the FunctionalExchange")
        },
        result = "the Sequence of exchange items for the given FunctionalExchange",
        examples = {
            @Example(expression = "myFunctionalExchange.getExchangeItems()", result = "the Sequence of exchange items for the given FunctionalExchange"),
        }
    )
    // @formatter:on
    public List<ExchangeItem> getExchangeItems(FunctionalExchange value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeExchangesItems()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of targets for the given FunctionalExchange.",
        params = {
            @Param(name = "value", value = "the FunctionalExchange")
        },
        result = "the Sequence of targets for the given FunctionalExchange",
        examples = {
            @Example(expression = "myFunctionalExchange.getTarget()", result = "the Sequence of targets for the given FunctionalExchange"),
        }
    )
    // @formatter:on
    public List<ActivityNode> getTarget(FunctionalExchange value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchange_dataflowTarget()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized functional exchanges for the given FunctionalExchange.",
        params = {
            @Param(name = "value", value = "the FunctionalExchange")
        },
        result = "the Sequence of realized functional exchanges for the given FunctionalExchange",
        examples = {
            @Example(expression = "myFunctionalExchange.getRealizedFunctionalExchange()", result = "the Sequence of realized functional exchanges for the given FunctionalExchange"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getRealizedFunctionalExchange(FunctionalExchange value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeRealizedFunctionalExchanges()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized interactions for the given FunctionalExchange.",
        params = {
            @Param(name = "value", value = "the FunctionalExchange")
        },
        result = "the Sequence of realized interactions for the given FunctionalExchange",
        examples = {
            @Example(expression = "myFunctionalExchange.getRealizedInteractions()", result = "the Sequence of realized interactions for the given FunctionalExchange"),
        }
    )
    // @formatter:on
    public List<TraceableElement> getRealizedInteractions(FunctionalExchange value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeRealizedInteractions()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of categories for the given FunctionalExchange.",
        params = {
            @Param(name = "value", value = "the FunctionalExchange")
        },
        result = "the Sequence of categories for the given FunctionalExchange",
        examples = {
            @Example(expression = "myFunctionalExchange.getCategories()", result = "the Sequence of categories for the given FunctionalExchange"),
        }
    )
    // @formatter:on
    public List<ExchangeCategory> getCategories(FunctionalExchange value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeCategory()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing functional exchanges for the given FunctionalExchange.",
        params = {
            @Param(name = "value", value = "the FunctionalExchange")
        },
        result = "the Sequence of realizing functional exchanges for the given FunctionalExchange",
        examples = {
            @Example(expression = "myFunctionalExchange.getRealizingFunctionalExchanges()", result = "the Sequence of realizing functional exchanges for the given FunctionalExchange"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getRealizingFunctionalExchanges(FunctionalExchange value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchange_realizingDataflow()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of functional chains for the given FunctionalExchange.",
        params = {
            @Param(name = "value", value = "the FunctionalExchange")
        },
        result = "the Sequence of functional chains for the given FunctionalExchange",
        examples = {
            @Example(expression = "myFunctionalExchange.getFunctionalChains()", result = "the Sequence of functional chains for the given FunctionalExchange"),
        }
    )
    // @formatter:on
    public List<FunctionalChain> getFunctionalChains(FunctionalExchange value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeFunctionalChain()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of categories for the given FunctionalExchange.",
        params = {
            @Param(name = "value", value = "the FunctionalExchange")
        },
        result = "the Sequence of categories for the given FunctionalExchange",
        examples = {
            @Example(expression = "myFunctionalExchange.getScenarios()", result = "the Sequence of categories for the given FunctionalExchange"),
        }
    )
    // @formatter:on
    public List<Scenario> getScenarios(FunctionalExchange value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of sources for the given FunctionalExchange.",
        params = {
            @Param(name = "value", value = "the FunctionalExchange")
        },
        result = "the Sequence of sources for the given FunctionalExchange",
        examples = {
            @Example(expression = "myFunctionalExchange.getSource()", result = "the Sequence of sources for the given FunctionalExchange"),
        }
    )
    // @formatter:on
    public List<ActivityNode> getSource(FunctionalExchange value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchange_dataflowSource()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating communication means for the given FunctionalExchange.",
        params = {
            @Param(name = "value", value = "the FunctionalExchange")
        },
        result = "the Sequence of allocating communication means for the given FunctionalExchange",
        examples = {
            @Example(expression = "myFunctionalExchange.getAllocatingCommunicationMean()", result = "the Sequence of allocating communication means for the given FunctionalExchange"),
        }
    )
    // @formatter:on
    public List<CommunicationMean> getAllocatingCommunicationMean(FunctionalExchange value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeAllocatingCommunicationMean()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating component exchanges for the given FunctionalExchange.",
        params = {
            @Param(name = "value", value = "the FunctionalExchange")
        },
        result = "the Sequence of allocating component exchanges for the given FunctionalExchange",
        examples = {
            @Example(expression = "myFunctionalExchange.getAllocatingComponentExchange()", result = "the Sequence of allocating component exchanges for the given FunctionalExchange"),
        }
    )
    // @formatter:on
    public List<ComponentExchange> getAllocatingComponentExchange(FunctionalExchange value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeAllocatingComponentExchange()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of operational processes for the given FunctionalExchange.",
        params = {
            @Param(name = "value", value = "the FunctionalExchange")
        },
        result = "the Sequence of operational processes for the given FunctionalExchange",
        examples = {
            @Example(expression = "myFunctionalExchange.getOperationalProcesses()", result = "the Sequence of operational processes for the given FunctionalExchange"),
        }
    )
    // @formatter:on
    public List<OperationalProcess> getOperationalProcesses(FunctionalExchange value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalExchangeOperationalProcess()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of component exchanges for the given ComponentExchangeCategory.",
        params = {
            @Param(name = "value", value = "the ComponentExchangeCategory")
        },
        result = "the Sequence of component exchanges for the given ComponentExchangeCategory",
        examples = {
            @Example(expression = "myComponentExchangeCategory.getComponentExchanges()", result = "the Sequence of component exchanges for the given ComponentExchangeCategory"),
        }
    )
    // @formatter:on
    public List<ComponentExchange> getComponentExchanges(ComponentExchangeCategory value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CategoryComponentExchange()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of types for the given ComponentPort.",
        params = {
            @Param(name = "value", value = "the ComponentPort")
        },
        result = "the Sequence of types for the given ComponentPort",
        examples = {
            @Example(expression = "myComponentPort.getType()", result = "the Sequence of types for the given ComponentPort"),
        }
    )
    // @formatter:on
    public List<Type> getType(ComponentPort value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_type().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of owner for the given ComponentPort.",
        params = {
            @Param(name = "value", value = "the ComponentPort")
        },
        result = "the Sequence of owner for the given ComponentPort",
        examples = {
            @Example(expression = "myComponentPort.getType()", result = "the Sequence of owner for the given ComponentPort"),
        }
    )
    // @formatter:on
    public List<EObject> getOwner(ComponentPort value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_owner().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of provided interfaces for the given ComponentPort.",
        params = {
            @Param(name = "value", value = "the ComponentPort")
        },
        result = "the Sequence of provided interfaces for the given ComponentPort",
        examples = {
            @Example(expression = "myComponentPort.getProvidedInterfaces()", result = "the Sequence of provided interfaces for the given ComponentPort"),
        }
    )
    // @formatter:on
    public List<Interface> getProvidedInterfaces(ComponentPort value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_providedInterfaces()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of required interfaces for the given ComponentPort.",
        params = {
            @Param(name = "value", value = "the ComponentPort")
        },
        result = "the Sequence of required interfaces for the given ComponentPort",
        examples = {
            @Example(expression = "myComponentPort.getRequiredInterfaces()", result = "the Sequence of required interfaces for the given ComponentPort"),
        }
    )
    // @formatter:on
    public List<Interface> getRequiredInterfaces(ComponentPort value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_requiredInterfaces()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized component ports for the given ComponentPort.",
        params = {
            @Param(name = "value", value = "the ComponentPort")
        },
        result = "the Sequence of realized component ports for the given ComponentPort",
        examples = {
            @Example(expression = "myComponentPort.getRealizedComponentPorts()", result = "the Sequence of realized component ports for the given ComponentPort"),
        }
    )
    // @formatter:on
    public List<ComponentPort> getRealizedComponentPorts(ComponentPort value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_realizedComponentPort()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized component ports for the given ComponentPort.",
        params = {
            @Param(name = "value", value = "the ComponentPort")
        },
        result = "the Sequence of realized component ports for the given ComponentPort",
        examples = {
            @Example(expression = "myComponentPort.getRealizedComponentPorts()", result = "the Sequence of realized component ports for the given ComponentPort"),
        }
    )
    // @formatter:on
    public List<Port> getAllocatedFunctionPorts(ComponentPort value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_realizedFunctionPort()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of outgoing delegations for the given ComponentPort.",
        params = {
            @Param(name = "value", value = "the ComponentPort")
        },
        result = "the Sequence of outgoing delegations for the given ComponentPort",
        examples = {
            @Example(expression = "myComponentPort.getOutgoingDelegations()", result = "the Sequence of outgoing delegations for the given ComponentPort"),
        }
    )
    // @formatter:on
    public List<ComponentExchange> getOutgoingDelegations(ComponentPort value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPortOutgoingDeletations()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of outgoing component exchanges for the given ComponentPort.",
        params = {
            @Param(name = "value", value = "the ComponentPort")
        },
        result = "the Sequence of outgoing component exchanges for the given ComponentPort",
        examples = {
            @Example(expression = "myComponentPort.getOutgoingComponentExchanges()", result = "the Sequence of outgoing component exchanges for the given ComponentPort"),
        }
    )
    // @formatter:on
    public List<ComponentExchange> getOutgoingComponentExchanges(ComponentPort value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPortOutgoingComponentExchanges()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating physical ports for the given ComponentPort.",
        params = {
            @Param(name = "value", value = "the ComponentPort")
        },
        result = "the Sequence of allocating physical ports for the given ComponentPort",
        examples = {
            @Example(expression = "myComponentPort.getAllocatingPhysicalPorts()", result = "the Sequence of allocating physical ports for the given ComponentPort"),
        }
    )
    // @formatter:on
    public List<PhysicalPort> getAllocatingPhysicalPorts(ComponentPort value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPortAllocatingPhysicalPorts()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of incoming component exchanges for the given ComponentPort.",
        params = {
            @Param(name = "value", value = "the ComponentPort")
        },
        result = "the Sequence of incoming component exchanges for the given ComponentPort",
        examples = {
            @Example(expression = "myComponentPort.getIncomingComponentExchanges()", result = "the Sequence of incoming component exchanges for the given ComponentPort"),
        }
    )
    // @formatter:on
    public List<ComponentExchange> getIncomingComponentExchanges(ComponentPort value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPortIncomingComponentExchanges()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing component ports for the given ComponentPort.",
        params = {
            @Param(name = "value", value = "the ComponentPort")
        },
        result = "the Sequence of realizing component ports for the given ComponentPort",
        examples = {
            @Example(expression = "myComponentPort.getRealizingComponentPorts()", result = "the Sequence of realizing component ports for the given ComponentPort"),
        }
    )
    // @formatter:on
    public List<Port> getRealizingComponentPorts(ComponentPort value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPort_realizingComponentPort()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of incoming delegations for the given ComponentPort.",
        params = {
            @Param(name = "value", value = "the ComponentPort")
        },
        result = "the Sequence of incoming delegations for the given ComponentPort",
        examples = {
            @Example(expression = "myComponentPort.getIncomingDelegations()", result = "the Sequence of incoming delegations for the given ComponentPort"),
        }
    )
    // @formatter:on
    public List<ComponentExchange> getIncomingDelegations(ComponentPort value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ComponentPortIncomingDeletations()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of source reference hierarchy for the given ReferenceHierarchyContext.",
        params = {
            @Param(name = "value", value = "the ReferenceHierarchyContext")
        },
        result = "the Sequence of source reference hierarchy for the given ReferenceHierarchyContext",
        examples = {
            @Example(expression = "myReferenceHierarchyContext.getSourceReferenceHierachy()", result = "the Sequence of source reference hierarchy for the given ReferenceHierarchyContext"),
        }
    )
    // @formatter:on
    public List<FunctionalChainReference> getSourceReferenceHierachy(ReferenceHierarchyContext value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ReferenceHierarchyContextSource()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of target reference hierarchy for the given ReferenceHierarchyContext.",
        params = {
            @Param(name = "value", value = "the ReferenceHierarchyContext")
        },
        result = "the Sequence of target reference hierarchy for the given ReferenceHierarchyContext",
        examples = {
            @Example(expression = "myReferenceHierarchyContext.getTargetReferenceHierachy()", result = "the Sequence of target reference hierarchy for the given ReferenceHierarchyContext"),
        }
    )
    // @formatter:on
    public List<FunctionalChainReference> getTargetReferenceHierachy(ReferenceHierarchyContext value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ReferenceHierarchyContextTarget()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of exchange context for the given FunctionalChainInvolvementLink.",
        params = {
            @Param(name = "value", value = "the FunctionalChainInvolvementLink")
        },
        result = "the Sequence of exchange context for the given FunctionalChainInvolvementLink",
        examples = {
            @Example(expression = "myFunctionalChainInvolvementLink.getExchangeContext()", result = "the Sequence of exchange context for the given FunctionalChainInvolvementLink"),
        }
    )
    // @formatter:on
    public List<Constraint> getExchangeContext(FunctionalChainInvolvementLink value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvmentLinkExchangeContext()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of target for the given FunctionalChainInvolvementLink.",
        params = {
            @Param(name = "value", value = "the FunctionalChainInvolvementLink")
        },
        result = "the Sequence of target for the given FunctionalChainInvolvementLink",
        examples = {
            @Example(expression = "myFunctionalChainInvolvementLink.getTarget()", result = "the Sequence of target for the given FunctionalChainInvolvementLink"),
        }
    )
    // @formatter:on
    public List<FunctionalChainInvolvementFunction> getTarget(FunctionalChainInvolvementLink value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvmentLinkTarget()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of source for the given FunctionalChainInvolvementLink.",
        params = {
            @Param(name = "value", value = "the FunctionalChainInvolvementLink")
        },
        result = "the Sequence of source for the given FunctionalChainInvolvementLink",
        examples = {
            @Example(expression = "myFunctionalChainInvolvementLink.getSource()", result = "the Sequence of source for the given FunctionalChainInvolvementLink"),
        }
    )
    // @formatter:on
    public List<FunctionalChainInvolvementFunction> getSource(FunctionalChainInvolvementLink value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvmentLinkSource()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of condition for the given SequenceLink.",
        params = {
            @Param(name = "value", value = "the SequenceLink")
        },
        result = "the Sequence of condition for the given SequenceLink",
        examples = {
            @Example(expression = "mySequenceLink.getCondition()", result = "the Sequence of condition for the given SequenceLink"),
        }
    )
    // @formatter:on
    public List<Constraint> getCondition(SequenceLink value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkCondition().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of owner for the given SequenceLink.",
        params = {
            @Param(name = "value", value = "the SequenceLink")
        },
        result = "the Sequence of owner for the given SequenceLink",
        examples = {
            @Example(expression = "mySequenceLink.getOwner()", result = "the Sequence of owner for the given SequenceLink"),
        }
    )
    // @formatter:on
    public List<EObject> getOwner(SequenceLink value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementOwner().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of links for the given SequenceLink.",
        params = {
            @Param(name = "value", value = "the SequenceLink")
        },
        result = "the Sequence of links for the given SequenceLink",
        examples = {
            @Example(expression = "mySequenceLink.getLinks()", result = "the Sequence of links for the given SequenceLink"),
        }
    )
    // @formatter:on
    public List<FunctionalChainInvolvementLink> getLinks(SequenceLink value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkLinks().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of target control node for the given SequenceLink.",
        params = {
            @Param(name = "value", value = "the SequenceLink")
        },
        result = "the Sequence of target control node for the given SequenceLink",
        examples = {
            @Example(expression = "mySequenceLink.getTargetControlNode()", result = "the Sequence of target control node for the given SequenceLink"),
        }
    )
    // @formatter:on
    public List<ControlNode> getTargetControlNode(SequenceLink value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkTargetControlNode()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of target involvement functions for the given SequenceLink.",
        params = {
            @Param(name = "value", value = "the SequenceLink")
        },
        result = "the Sequence of target involvement functions for the given SequenceLink",
        examples = {
            @Example(expression = "mySequenceLink.getTargetInvolvementFunction()", result = "the Sequence of target involvement functions for the given SequenceLink"),
        }
    )
    // @formatter:on
    public List<FunctionalChainInvolvementFunction> getTargetInvolvementFunction(SequenceLink value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkTargetInvolvementFunction()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of source involvement functions for the given SequenceLink.",
        params = {
            @Param(name = "value", value = "the SequenceLink")
        },
        result = "the Sequence of source involvement functions for the given SequenceLink",
        examples = {
            @Example(expression = "mySequenceLink.getSourceInvolvementFunction()", result = "the Sequence of source involvement functions for the given SequenceLink"),
        }
    )
    // @formatter:on
    public List<FunctionalChainInvolvementFunction> getSourceInvolvementFunction(SequenceLink value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkSourceInvolvementFunction()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of source control node for the given SequenceLink.",
        params = {
            @Param(name = "value", value = "the SequenceLink")
        },
        result = "the Sequence of source control node for the given SequenceLink",
        examples = {
            @Example(expression = "mySequenceLink.getSourceControlNode()", result = "the Sequence of source control node for the given SequenceLink"),
        }
    )
    // @formatter:on
    public List<ControlNode> getSourceControlNode(SequenceLink value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkSourceControlNode()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of target sequence links for the given SequenceLinkEnd.",
        params = {
            @Param(name = "value", value = "the SequenceLinkEnd")
        },
        result = "the Sequence of target sequence links for the given SequenceLinkEnd",
        examples = {
            @Example(expression = "mySequenceLinkEnd.getTargetSequenceLinks()", result = "the Sequence of target sequence links for the given SequenceLinkEnd"),
        }
    )
    // @formatter:on
    public List<SequenceLink> getTargetSequenceLinks(SequenceLinkEnd value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkEndTargetSequenceLinks()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of source sequence links for the given SequenceLinkEnd.",
        params = {
            @Param(name = "value", value = "the SequenceLinkEnd")
        },
        result = "the Sequence of source sequence links for the given SequenceLinkEnd",
        examples = {
            @Example(expression = "mySequenceLinkEnd.getTargetSequenceLinks()", result = "the Sequence of source sequence links for the given SequenceLinkEnd"),
        }
    )
    // @formatter:on
    public List<SequenceLink> getSourceSequenceLinks(SequenceLinkEnd value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceLinkEndSourceSequenceLinks()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of owner for the given FunctionalChainInvolvementFunction.",
        params = {
            @Param(name = "value", value = "the FunctionalChainInvolvementFunction")
        },
        result = "the Sequence of owner for the given FunctionalChainInvolvementFunction",
        examples = {
            @Example(expression = "myFunctionalChainInvolvementFunction.getOwner()", result = "the Sequence of owner for the given FunctionalChainInvolvementFunction"),
        }
    )
    // @formatter:on
    public List<EObject> getOwner(FunctionalChainInvolvementFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementOwner().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of outgoing involvement links for the given FunctionalChainInvolvementFunction.",
        params = {
            @Param(name = "value", value = "the FunctionalChainInvolvementFunction")
        },
        result = "the Sequence of outgoing involvement links for the given FunctionalChainInvolvementFunction",
        examples = {
            @Example(expression = "myFunctionalChainInvolvementFunction.getOutgoingInvolvementLinks()", result = "the Sequence of outgoing involvement links for the given FunctionalChainInvolvementFunction"),
        }
    )
    // @formatter:on
    public List<FunctionalChainInvolvement> getOutgoingInvolvementLinks(FunctionalChainInvolvementFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvementFunctionOutgoingInvolvementLinks()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of incoming involvement links for the given FunctionalChainInvolvementFunction.",
        params = {
            @Param(name = "value", value = "the FunctionalChainInvolvementFunction")
        },
        result = "the Sequence of incoming involvement links for the given FunctionalChainInvolvementFunction",
        examples = {
            @Example(expression = "myFunctionalChainInvolvementFunction.getIncomingInvolvementLinks()", result = "the Sequence of incoming involvement links for the given FunctionalChainInvolvementFunction"),
        }
    )
    // @formatter:on
    public List<FunctionalChainInvolvement> getIncomingInvolvementLinks(FunctionalChainInvolvementFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionalChainInvolvementFunctionIncomingInvolvementLinks()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of parent state and mode for the given IState.",
        params = {
            @Param(name = "value", value = "the IState")
        },
        result = "the Sequence of parent state and mode for the given IState",
        examples = {
            @Example(expression = "myIState.getParentStateAndMode()", result = "the Sequence of parent state and mode for the given IState"),
        }
    )
    // @formatter:on
    public List<State> getParentStateAndMode(IState value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.State_ParentState().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved state and mode for the given IState.",
        params = {
            @Param(name = "value", value = "the IState")
        },
        result = "the Sequence of involved state and mode for the given IState",
        examples = {
            @Example(expression = "myIState.getInvolvedStatesAndModes()", result = "the Sequence of involved state and mode for the given IState"),
        }
    )
    // @formatter:on
    public List<IState> getInvolvedStatesAndModes(IState value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.State_InvolvedStates().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of owned state and mode for the given IState.",
        params = {
            @Param(name = "value", value = "the IState")
        },
        result = "the Sequence of owned state and mode for the given IState",
        examples = {
            @Example(expression = "myIState.getOwnedStateAndMode()", result = "the Sequence of owned state and mode for the given IState"),
        }
    )
    // @formatter:on
    public List<AbstractState> getOwnedStateAndMode(IState value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.State_OwnedStates().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of outgoing transitions for the given IState.",
        params = {
            @Param(name = "value", value = "the IState")
        },
        result = "the Sequence of outgoing transitions for the given IState",
        examples = {
            @Example(expression = "myIState.getOutgoingTransition()", result = "the Sequence of outgoing transitions for the given IState"),
        }
    )
    // @formatter:on
    public List<StateTransition> getOutgoingTransition(IState value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.StateAndModeOutGoingTransition()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of incoming transitions for the given IState.",
        params = {
            @Param(name = "value", value = "the IState")
        },
        result = "the Sequence of incoming transitions for the given IState",
        examples = {
            @Example(expression = "myIState.getIncomingTransition()", result = "the Sequence of incoming transitions for the given IState"),
        }
    )
    // @formatter:on
    public List<StateTransition> getIncomingTransition(IState value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.StateAndModeInComingTransition()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involving states and modes for the given IState.",
        params = {
            @Param(name = "value", value = "the IState")
        },
        result = "the Sequence of involving states and modes for the given IState",
        examples = {
            @Example(expression = "myIState.getInvolvingStatesAndModes()", result = "the Sequence of involving states and modes for the given IState"),
        }
    )
    // @formatter:on
    public List<IState> getInvolvingStatesAndModes(IState value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.State_InvolvingStates().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of owned entry exit points for the given Region.",
        params = {
            @Param(name = "value", value = "the Region")
        },
        result = "the Sequence of owned entry exit points for the given Region",
        examples = {
            @Example(expression = "myRegion.getOwnedEntryExitPoints()", result = "the Sequence of owned entry exit points for the given Region"),
        }
    )
    // @formatter:on
    public List<Pseudostate> getOwnedEntryExitPoints(Region value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.State_OwnedEntryExitPoints()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized mode for the given AbstractState.",
        params = {
            @Param(name = "value", value = "the AbstractState")
        },
        result = "the Sequence of realized mode for the given AbstractState",
        examples = {
            @Example(expression = "myAbstractState.getRealizedMode()", result = "the Sequence of realized mode for the given AbstractState"),
        }
    )
    // @formatter:on
    public List<IState> getRealizedMode(AbstractState value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateRealizedMode()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized state for the given AbstractState.",
        params = {
            @Param(name = "value", value = "the AbstractState")
        },
        result = "the Sequence of realized state for the given AbstractState",
        examples = {
            @Example(expression = "myAbstractState.getRealizedState()", result = "the Sequence of realized state for the given AbstractState"),
        }
    )
    // @formatter:on
    public List<IState> getRealizedState(AbstractState value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateRealizedState()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing state for the given AbstractState.",
        params = {
            @Param(name = "value", value = "the AbstractState")
        },
        result = "the Sequence of realizing state for the given AbstractState",
        examples = {
            @Example(expression = "myAbstractState.getRealizingState()", result = "the Sequence of realizing state for the given AbstractState"),
        }
    )
    // @formatter:on
    public List<IState> getRealizingState(AbstractState value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateRealizingState()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing mode for the given AbstractState.",
        params = {
            @Param(name = "value", value = "the AbstractState")
        },
        result = "the Sequence of realizing mode for the given AbstractState",
        examples = {
            @Example(expression = "myAbstractState.getRealizingMode()", result = "the Sequence of realizing mode for the given AbstractState"),
        }
    )
    // @formatter:on
    public List<IState> getRealizingMode(AbstractState value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateRealizingMode()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of do activity for the given State.",
        params = {
            @Param(name = "value", value = "the State")
        },
        result = "the Sequence of do activity for the given State",
        examples = {
            @Example(expression = "myState.getDoActivity()", result = "the Sequence of do activity for the given State"),
        }
    )
    // @formatter:on
    public List<AbstractEvent> getDoActivity(State value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateModeDoActivity()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of active elements for the given State.",
        params = {
            @Param(name = "value", value = "the State")
        },
        result = "the Sequence of active elements for the given State",
        examples = {
            @Example(expression = "myState.getActiveElements()", result = "the Sequence of active elements for the given State"),
        }
    )
    // @formatter:on
    // TODO AbstractFunction and AbstractCapability and OperationalCapability and
    // FunctionalChain
    public List<EObject> getActiveElements(State value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateAvailableElements()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of scenarii for the given State.",
        params = {
            @Param(name = "value", value = "the State")
        },
        result = "the Sequence of scenarii for the given State",
        examples = {
            @Example(expression = "myState.getScenarios()", result = "the Sequence of scenarii for the given State"),
        }
    )
    // @formatter:on
    public List<Scenario> getScenarios(State value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of computed active elements for the given State.",
        params = {
            @Param(name = "value", value = "the State")
        },
        result = "the Sequence of computed active elements for the given State",
        examples = {
            @Example(expression = "myState.getActiveElementsComputed()", result = "the Sequence of computed active elements for the given State"),
        }
    )
    // @formatter:on
    // TODO AbstractFunction and AbstractCapability and OperationalCapability and
    // FunctionalChain
    public List<EObject> getActiveElementsComputed(State value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractStateParentActiveElements()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of effect for the given StateTransition.",
        params = {
            @Param(name = "value", value = "the StateTransition")
        },
        result = "the Sequence of effect for the given StateTransition",
        examples = {
            @Example(expression = "myStateTransition.getEffect()", result = "the Sequence of effect for the given StateTransition"),
        }
    )
    // @formatter:on
    public List<AbstractEvent> getEffect(StateTransition value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.StateTransitionEffect().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of target for the given StateTransition.",
        params = {
            @Param(name = "value", value = "the StateTransition")
        },
        result = "the Sequence of target for the given StateTransition",
        examples = {
            @Example(expression = "myStateTransition.getTarget()", result = "the Sequence of target for the given StateTransition"),
        }
    )
    // @formatter:on
    public List<IState> getTarget(StateTransition value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.StateTransitionOutGoingIState()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of trigger for the given StateTransition.",
        params = {
            @Param(name = "value", value = "the StateTransition")
        },
        result = "the Sequence of trigger for the given StateTransition",
        examples = {
            @Example(expression = "myStateTransition.getTrigger()", result = "the Sequence of trigger for the given StateTransition"),
        }
    )
    // @formatter:on
    public List<AbstractEvent> getTrigger(StateTransition value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.StateTransitionTrigger().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of source for the given StateTransition.",
        params = {
            @Param(name = "value", value = "the StateTransition")
        },
        result = "the Sequence of source for the given StateTransition",
        examples = {
            @Example(expression = "myStateTransition.getSource()", result = "the Sequence of source for the given StateTransition"),
        }
    )
    // @formatter:on
    public List<IState> getSource(StateTransition value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.StateTransitionInComingIState()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of parent region for the given Pseudostate.",
        params = {
            @Param(name = "value", value = "the Pseudostate")
        },
        result = "the Sequence of parent region for the given Pseudostate",
        examples = {
            @Example(expression = "myPseudostate.getParentRegion()", result = "the Sequence of parent region for the given Pseudostate"),
        }
    )
    // @formatter:on
    public List<Region> getParentRegion(Pseudostate value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.EntryExitPoint_ParentRegion()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of owned functional chains for the given SystemFunction.",
        params = {
            @Param(name = "value", value = "the SystemFunction")
        },
        result = "the Sequence of owned functional chains for the given SystemFunction",
        examples = {
            @Example(expression = "mySystemFunction.getOwnedFunctionalChains()", result = "the Sequence of owned functional chains for the given SystemFunction"),
        }
    )
    // @formatter:on
    public List<FunctionalChain> getOwnedFunctionalChains(SystemFunction value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Function_ownedFunctionalChains()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized operational activities for the given SystemFunction.",
        params = {
            @Param(name = "value", value = "the SystemFunction")
        },
        result = "the Sequence of realized operational activities for the given SystemFunction",
        examples = {
            @Example(expression = "mySystemFunction.getRealizedOperationalActivities()", result = "the Sequence of realized operational activities for the given SystemFunction"),
        }
    )
    // @formatter:on
    public List<OperationalActivity> getRealizedOperationalActivities(SystemFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_realizedFunctions()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of internal outgoing functional exchanges for the given SystemFunction.",
        params = {
            @Param(name = "value", value = "the SystemFunction")
        },
        result = "the Sequence of internal outgoing functional exchanges for the given SystemFunction",
        examples = {
            @Example(expression = "mySystemFunction.getInternalOutgoingFunctionalExchanges()", result = "the Sequence of internal outgoing functional exchanges for the given SystemFunction"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getInternalOutgoingFunctionalExchanges(SystemFunction value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalOutGoingDataflows()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of out function ports for the given SystemFunction.",
        params = {
            @Param(name = "value", value = "the SystemFunction")
        },
        result = "the Sequence of out function ports for the given SystemFunction",
        examples = {
            @Example(expression = "mySystemFunction.getOutFlowPorts()", result = "the Sequence of out function ports for the given SystemFunction"),
        }
    )
    // @formatter:on
    public List<FunctionOutputPort> getOutFlowPorts(SystemFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.Function_outFlowPorts().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of outgoing functional exchanges for the given SystemFunction.",
        params = {
            @Param(name = "value", value = "the SystemFunction")
        },
        result = "the Sequence of outgoing functional exchanges for the given SystemFunction",
        examples = {
            @Example(expression = "mySystemFunction.getOutgoingFunctionalExchanges()", result = "the Sequence of outgoing functional exchanges for the given SystemFunction"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getOutgoingFunctionalExchanges(SystemFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_outgoingInteraction()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of incoming functional exchanges for the given SystemFunction.",
        params = {
            @Param(name = "value", value = "the SystemFunction")
        },
        result = "the Sequence of incoming functional exchanges for the given SystemFunction",
        examples = {
            @Example(expression = "mySystemFunction.getIncomingFunctionalExchanges()", result = "the Sequence of incoming functional exchanges for the given SystemFunction"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getIncomingFunctionalExchanges(SystemFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_incomingInteraction()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of internal incoming functional exchanges for the given SystemFunction.",
        params = {
            @Param(name = "value", value = "the SystemFunction")
        },
        result = "the Sequence of internal incoming functional exchanges for the given SystemFunction",
        examples = {
            @Example(expression = "mySystemFunction.getInternalIncomingFunctionalExchanges()", result = "the Sequence of internal incoming functional exchanges for the given SystemFunction"),
        }
    )
    // @formatter:on
    public List<FunctionalExchange> getInternalIncomingFunctionalExchanges(SystemFunction value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionInternalInComingDataflows()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing logical functions for the given SystemFunction.",
        params = {
            @Param(name = "value", value = "the SystemFunction")
        },
        result = "the Sequence of realizing logical functions for the given SystemFunction",
        examples = {
            @Example(expression = "mySystemFunction.getRealizingLogicalFunctions()", result = "the Sequence of realizing logical functions for the given SystemFunction"),
        }
    )
    // @formatter:on
    public List<LogicalFunction> getRealizingLogicalFunctions(SystemFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractFunction_realizingFunctions()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating actors for the given SystemFunction.",
        params = {
            @Param(name = "value", value = "the SystemFunction")
        },
        result = "the Sequence of allocating actors for the given SystemFunction",
        examples = {
            @Example(expression = "mySystemFunction.getAllocatingActor()", result = "the Sequence of allocating actors for the given SystemFunction"),
        }
    )
    // @formatter:on
    // TODO AbstractFunctionalBlock and Role
    public List<EObject> getAllocatingActor(SystemFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingActor().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of in flow ports for the given SystemFunction.",
        params = {
            @Param(name = "value", value = "the SystemFunction")
        },
        result = "the Sequence of in flow ports for the given SystemFunction",
        examples = {
            @Example(expression = "mySystemFunction.getInFlowPorts()", result = "the Sequence of in flow ports for the given SystemFunction"),
        }
    )
    // @formatter:on
    public List<FunctionInputPort> getInFlowPorts(SystemFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.Function_inFlowPorts().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involving capabilities for the given SystemFunction.",
        params = {
            @Param(name = "value", value = "the SystemFunction")
        },
        result = "the Sequence of involving capabilities for the given SystemFunction",
        examples = {
            @Example(expression = "mySystemFunction.getInvolvingCapabilities()", result = "the Sequence of involving capabilities for the given SystemFunction"),
        }
    )
    // @formatter:on
    public List<Capability> getInvolvingCapabilities(SystemFunction value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.SystemFunctionInvolvingCapabilities()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating system for the given SystemFunction.",
        params = {
            @Param(name = "value", value = "the SystemFunction")
        },
        result = "the Sequence of allocating system for the given SystemFunction",
        examples = {
            @Example(expression = "mySystemFunction.getAllocatingSystem()", result = "the Sequence of allocating system for the given SystemFunction"),
        }
    )
    // @formatter:on
    // TODO AbstractFunctionalBlock and Role
    public List<EObject> getAllocatingSystem(SystemFunction value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.FunctionAllocatingComponent()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of exploited capabilities for the given Mission.",
        params = {
            @Param(name = "value", value = "the Mission")
        },
        result = "the Sequence of exploited capabilities for the given Mission",
        examples = {
            @Example(expression = "myMission.getExploitedCapabilities()", result = "the Sequence of exploited capabilities for the given Mission"),
        }
    )
    // @formatter:on
    public List<Capability> getExploitedCapabilities(Mission value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Mission_ExploitedCapabilities()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved system components for the given Mission.",
        params = {
            @Param(name = "value", value = "the Mission")
        },
        result = "the Sequence of involved system components for the given Mission",
        examples = {
            @Example(expression = "myMission.getInvolvedSystemComponents()", result = "the Sequence of involved system components for the given Mission"),
        }
    )
    // @formatter:on
    public List<SystemComponent> getInvolvedSystemComponents(Mission value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Mission_InvolvedSystemComponents()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of owned functional chains for the given Capability.",
        params = {
            @Param(name = "value", value = "the Capability")
        },
        result = "the Sequence of owned functional chains for the given Capability",
        examples = {
            @Example(expression = "myCapability.getOwnedFunctionalChains()", result = "the Sequence of owned functional chains for the given Capability"),
        }
    )
    // @formatter:on
    public List<FunctionalChain> getOwnedFunctionalChains(Capability value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityOwnedFunctionalChains()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized operational capabilities for the given Capability.",
        params = {
            @Param(name = "value", value = "the Capability")
        },
        result = "the Sequence of realized operational capabilities for the given Capability",
        examples = {
            @Example(expression = "myCapability.getRealizedOperationalCapabilities()", result = "the Sequence of realized operational capabilities for the given Capability"),
        }
    )
    // @formatter:on
    public List<OperationalCapability> getRealizedOperationalCapabilities(Capability value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityRealizedOC().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved system functions for the given Capability.",
        params = {
            @Param(name = "value", value = "the Capability")
        },
        result = "the Sequence of involved system functions for the given Capability",
        examples = {
            @Example(expression = "myCapability.getInvolvedSystemFunctions()", result = "the Sequence of involved system functions for the given Capability"),
        }
    )
    // @formatter:on
    public List<SystemFunction> getInvolvedSystemFunctions(Capability value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityInvolvedFunctions()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved functional chains for the given Capability.",
        params = {
            @Param(name = "value", value = "the Capability")
        },
        result = "the Sequence of involved functional chains for the given Capability",
        examples = {
            @Example(expression = "myCapability.getInvolvedFunctionalChains()", result = "the Sequence of involved functional chains for the given Capability"),
        }
    )
    // @formatter:on
    public List<FunctionalChain> getInvolvedFunctionalChains(Capability value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.AbstractCapabilityInvolvedFunctionalChains()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of exploiting missions for the given Capability.",
        params = {
            @Param(name = "value", value = "the Capability")
        },
        result = "the Sequence of exploiting missions for the given Capability",
        examples = {
            @Example(expression = "myCapability.getExploitingMissions()", result = "the Sequence of exploiting missions for the given Capability"),
        }
    )
    // @formatter:on
    public List<Mission> getExploitingMissions(Capability value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Capability_purposeMissions()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing capability realizations for the given Capability.",
        params = {
            @Param(name = "value", value = "the Capability")
        },
        result = "the Sequence of realizing capability realizations for the given Capability",
        examples = {
            @Example(expression = "myCapability.getRealizingCapabilityRealizations()", result = "the Sequence of realizing capability realizations for the given Capability"),
        }
    )
    // @formatter:on
    public List<CapabilityRealization> getRealizingCapabilityRealizations(Capability value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapabilityRealizingCR().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involved elements for the given CapabilityExploitation.",
        params = {
            @Param(name = "value", value = "the CapabilityExploitation")
        },
        result = "the Sequence of involved elements for the given CapabilityExploitation",
        examples = {
            @Example(expression = "myCapabilityExploitation.getInvolvedElement()", result = "the Sequence of involved elements for the given CapabilityExploitation"),
        }
    )
    // @formatter:on
    public List<AbstractCapability> getInvolvedElement(CapabilityExploitation value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsCapabilityExploitationTarget()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of source for the given CapabilityExploitation.",
        params = {
            @Param(name = "value", value = "the CapabilityExploitation")
        },
        result = "the Sequence of source for the given CapabilityExploitation",
        examples = {
            @Example(expression = "myCapabilityExploitation.getSource()", result = "the Sequence of source for the given CapabilityExploitation"),
        }
    )
    // @formatter:on
    public List<Mission> getSource(CapabilityExploitation value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsCapabilityExploitationSource()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocated system functions for the given SystemComponent.",
        params = {
            @Param(name = "value", value = "the SystemComponent")
        },
        result = "the Sequence of allocated system functions for the given SystemComponent",
        examples = {
            @Example(expression = "mySystemComponent.getAllocatedSystemFunctions()", result = "the Sequence of allocated system functions for the given SystemComponent"),
        }
    )
    // @formatter:on
    public List<SystemFunction> getAllocatedSystemFunctions(SystemComponent value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_AllocatedFunctions()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized operation entities for the given SystemComponent.",
        params = {
            @Param(name = "value", value = "the SystemComponent")
        },
        result = "the Sequence of realized operation entities for the given SystemComponent",
        examples = {
            @Example(expression = "mySystemComponent.getRealizedOperationalEntities()", result = "the Sequence of realized operation entities for the given SystemComponent"),
        }
    )
    // @formatter:on
    public List<Component> getRealizedOperationalEntities(SystemComponent value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizedComponents()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing logical components for the given SystemComponent.",
        params = {
            @Param(name = "value", value = "the SystemComponent")
        },
        result = "the Sequence of realizing logical components for the given SystemComponent",
        examples = {
            @Example(expression = "mySystemComponent.getRealizingLogicalComponents()", result = "the Sequence of realizing logical components for the given SystemComponent"),
        }
    )
    // @formatter:on
    public List<LogicalComponent> getRealizingLogicalComponents(SystemComponent value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Component_RealizingComponents()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involving capabilities for the given SystemComponent.",
        params = {
            @Param(name = "value", value = "the SystemComponent")
        },
        result = "the Sequence of involving capabilities for the given SystemComponent",
        examples = {
            @Example(expression = "mySystemComponent.getInvolvingCapabilities()", result = "the Sequence of involving capabilities for the given SystemComponent"),
        }
    )
    // @formatter:on
    public List<Capability> getInvolvingCapabilities(SystemComponent value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.SystemComponent_InvolvingCapabilities()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of involving missions for the given SystemComponent.",
        params = {
            @Param(name = "value", value = "the SystemComponent")
        },
        result = "the Sequence of involving missions for the given SystemComponent",
        examples = {
            @Example(expression = "mySystemComponent.getInvolvingMissions()", result = "the Sequence of involving missions for the given SystemComponent"),
        }
    )
    // @formatter:on
    public List<Mission> getInvolvingMissions(SystemComponent value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SystemComponent_InvolvingMissions()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of constrained elements for the given Constraint.",
        params = {
            @Param(name = "value", value = "the Constraint")
        },
        result = "the Sequence of constrained elements for the given Constraint",
        examples = {
            @Example(expression = "myConstraint.getConstrainedElements()", result = "the Sequence of constrained elements for the given Constraint"),
        }
    )
    // @formatter:on
    public List<ModelElement> getConstrainedElements(Constraint value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ConstraintModelElements().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of target for the given Generalization.",
        params = {
            @Param(name = "value", value = "the Generalization")
        },
        result = "the Sequence of target for the given Generalization",
        examples = {
            @Example(expression = "myGeneralization.getTarget()", result = "the Sequence of target for the given Generalization"),
        }
    )
    // @formatter:on
    public List<GeneralizableElement> getTarget(Generalization value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsGeneralizationTarget()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of source for the given Generalization.",
        params = {
            @Param(name = "value", value = "the Generalization")
        },
        result = "the Sequence of source for the given Generalization",
        examples = {
            @Example(expression = "myGeneralization.getSource()", result = "the Sequence of source for the given Generalization"),
        }
    )
    // @formatter:on
    public List<GeneralizableElement> getSource(Generalization value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsGeneralizationSource()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of value for the given AbstractPropertyValue.",
        params = {
            @Param(name = "value", value = "the AbstractPropertyValue")
        },
        result = "the Sequence of value for the given AbstractPropertyValue",
        examples = {
            @Example(expression = "myAbstractPropertyValue.getValue()", result = "the Sequence of value for the given AbstractPropertyValue"),
        }
    )
    // @formatter:on
    public List<AbstractPropertyValue> getValue(AbstractPropertyValue value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.PropertyValue_applying_valued_element_Primitive()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of valued elements for the given AbstractPropertyValue.",
        params = {
            @Param(name = "value", value = "the AbstractPropertyValue")
        },
        result = "the Sequence of valued elements for the given AbstractPropertyValue",
        examples = {
            @Example(expression = "myAbstractPropertyValue.getValuedElements()", result = "the Sequence of valued elements for the given AbstractPropertyValue"),
        }
    )
    // @formatter:on
    public List<CapellaElement> getValuedElements(AbstractPropertyValue value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.PropertyValue_applying_valued_element()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of valued elements for the given PropertyValueGroup.",
        params = {
            @Param(name = "value", value = "the PropertyValueGroup")
        },
        result = "the Sequence of valued elements for the given PropertyValueGroup",
        examples = {
            @Example(expression = "myPropertyValueGroup.getValuedElements()", result = "the Sequence of valued elements for the given PropertyValueGroup"),
        }
    )
    // @formatter:on
    public List<CapellaElement> getValuedElements(PropertyValueGroup value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.PropertyValueGroup_applying_valued_element()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of referenced element for the given CatalogElementLink.",
        params = {
            @Param(name = "value", value = "the CatalogElementLink")
        },
        result = "the Sequence of referenced element for the given CatalogElementLink",
        examples = {
            @Example(expression = "myCatalogElementLink.getReferencedElement()", result = "the Sequence of referenced element for the given CatalogElementLink"),
        }
    )
    // @formatter:on
    public List<EObject> getReferencedElement(CatalogElementLink value) {
        return castList(
                new org.polarsys.capella.common.re.ui.queries.CatalogElementLinkReferencedElement().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of related elements for the given CatalogElement.",
        params = {
            @Param(name = "value", value = "the CatalogElement")
        },
        result = "the Sequence of related elements for the given CatalogElement",
        examples = {
            @Example(expression = "myCatalogElement.getRelatedElements()", result = "the Sequence of related elements for the given CatalogElement"),
        }
    )
    // @formatter:on
    public List<EObject> getRelatedElements(CatalogElement value) {
        return castList(
                new org.polarsys.capella.common.re.ui.queries.CatalogElementRelatedSemanticElements().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of related replicable elements for the given CatalogElement.",
        params = {
            @Param(name = "value", value = "the CatalogElement")
        },
        result = "the Sequence of related replicable elements for the given CatalogElement",
        examples = {
            @Example(expression = "myCatalogElement.getRelatedReplicableElements()", result = "the Sequence of related replicable elements for the given CatalogElement"),
        }
    )
    // @formatter:on
    public List<CatalogElement> getRelatedReplicableElements(CatalogElement value) {
        return castList(new org.polarsys.capella.common.re.ui.queries.CatalogElementRelatedReplicas().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of RPL for the given CatalogElement.",
        params = {
            @Param(name = "value", value = "the CatalogElement")
        },
        result = "the Sequence of RPL for the given CatalogElement",
        examples = {
            @Example(expression = "myCatalogElement.getRPL()", result = "the Sequence of RPL for the given CatalogElement"),
        }
    )
    // @formatter:on
    public List<CatalogElement> getRPL(CatalogElement value) {
        return castList(new org.polarsys.capella.common.re.ui.queries.CatalogElementReverseOrigin().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized physical components for the given ConfigurationItem.",
        params = {
            @Param(name = "value", value = "the ConfigurationItem")
        },
        result = "the Sequence of realized physical components for the given ConfigurationItem",
        examples = {
            @Example(expression = "myConfigurationItem.getRealizedPhysicalComponents()", result = "the Sequence of realized physical components for the given ConfigurationItem"),
        }
    )
    // @formatter:on
    public List<PhysicalComponent> getRealizedPhysicalComponents(ConfigurationItem value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CIRealizedPhysicalComponents()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized physical links for the given ConfigurationItem.",
        params = {
            @Param(name = "value", value = "the ConfigurationItem")
        },
        result = "the Sequence of realized physical links for the given ConfigurationItem",
        examples = {
            @Example(expression = "myConfigurationItem.getRealizedPhysicalLinks()", result = "the Sequence of realized physical links for the given ConfigurationItem"),
        }
    )
    // @formatter:on
    public List<PhysicalLink> getRealizedPhysicalLinks(ConfigurationItem value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CIRealizedPhysicalLinks().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized physical ports for the given ConfigurationItem.",
        params = {
            @Param(name = "value", value = "the ConfigurationItem")
        },
        result = "the Sequence of realized physical ports for the given ConfigurationItem",
        examples = {
            @Example(expression = "myConfigurationItem.getRealizedPhysicalLinks()", result = "the Sequence of realized physical ports for the given ConfigurationItem"),
        }
    )
    // @formatter:on
    public List<PhysicalPort> getRealizedPhysicalPorts(ConfigurationItem value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CIRealizedPhysicalPorts().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of target for the given CommunicationLink.",
        params = {
            @Param(name = "value", value = "the CommunicationLink")
        },
        result = "the Sequence of target for the given CommunicationLink",
        examples = {
            @Example(expression = "myCommunicationLink.getTarget()", result = "the Sequence of target for the given CommunicationLink"),
        }
    )
    // @formatter:on
    public List<AbstractExchangeItem> getTarget(CommunicationLink value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CommunicationLinkExchangeItem()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of source for the given CommunicationLink.",
        params = {
            @Param(name = "value", value = "the CommunicationLink")
        },
        result = "the Sequence of source for the given CommunicationLink",
        examples = {
            @Example(expression = "myCommunicationLink.getSource()", result = "the Sequence of source for the given CommunicationLink"),
        }
    )
    // @formatter:on
    public List<Component> getSource(CommunicationLink value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CommunicationLinkComponent()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of parent scenario for the given SequenceMessage.",
        params = {
            @Param(name = "value", value = "the SequenceMessage")
        },
        result = "the Sequence of parent scenario for the given SequenceMessage",
        examples = {
            @Example(expression = "mySequenceMessage.getParentScenario()", result = "the Sequence of parent scenario for the given SequenceMessage"),
        }
    )
    // @formatter:on
    public List<EObject> getParentScenario(SequenceMessage value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessage_parentScenario()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of invoked exchange item for the given SequenceMessage.",
        params = {
            @Param(name = "value", value = "the SequenceMessage")
        },
        result = "the Sequence of invoked exchange item for the given SequenceMessage",
        examples = {
            @Example(expression = "mySequenceMessage.getInvokedExchangeItemAllocation()", result = "the Sequence of invoked exchange item for the given SequenceMessage"),
        }
    )
    // @formatter:on
    public List<AbstractEventOperation> getInvokedExchangeItemAllocation(SequenceMessage value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageInvokedExchangeItemAllocation()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of refined sequence message for the given SequenceMessage.",
        params = {
            @Param(name = "value", value = "the SequenceMessage")
        },
        result = "the Sequence of refined sequence message for the given SequenceMessage",
        examples = {
            @Example(expression = "mySequenceMessage.getRefinedSequenceMessage()", result = "the Sequence of refined sequence message for the given SequenceMessage"),
        }
    )
    // @formatter:on
    public List<SequenceMessage> getRefinedSequenceMessage(SequenceMessage value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessage_refiningSequenceMessage()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of invoked component exchange for the given SequenceMessage.",
        params = {
            @Param(name = "value", value = "the SequenceMessage")
        },
        result = "the Sequence of invoked component exchange for the given SequenceMessage",
        examples = {
            @Example(expression = "mySequenceMessage.getInvokedComponentExchange()", result = "the Sequence of invoked component exchange for the given SequenceMessage"),
        }
    )
    // @formatter:on
    public List<AbstractEventOperation> getInvokedComponentExchange(SequenceMessage value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageInvokedComponentExchange()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of invoked communication mean for the given SequenceMessage.",
        params = {
            @Param(name = "value", value = "the SequenceMessage")
        },
        result = "the Sequence of invoked communication mean for the given SequenceMessage",
        examples = {
            @Example(expression = "mySequenceMessage.getInvokedCommunicationMean()", result = "the Sequence of invoked communication mean for the given SequenceMessage"),
        }
    )
    // @formatter:on
    public List<AbstractEventOperation> getInvokedCommunicationMean(SequenceMessage value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageInvokedCommunicationMean()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of exchange items for the given SequenceMessage.",
        params = {
            @Param(name = "value", value = "the SequenceMessage")
        },
        result = "the Sequence of exchange items for the given SequenceMessage",
        examples = {
            @Example(expression = "mySequenceMessage.getExchangeItems()", result = "the Sequence of exchange items for the given SequenceMessage"),
        }
    )
    // @formatter:on
    public List<ExchangeItem> getExchangeItems(SequenceMessage value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageExchangeItems()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of invoked interaction for the given SequenceMessage.",
        params = {
            @Param(name = "value", value = "the SequenceMessage")
        },
        result = "the Sequence of invoked interaction for the given SequenceMessage",
        examples = {
            @Example(expression = "mySequenceMessage.getInvokedInteraction()", result = "the Sequence of invoked interaction for the given SequenceMessage"),
        }
    )
    // @formatter:on
    public List<AbstractEventOperation> getInvokedInteraction(SequenceMessage value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageInvokedInteraction()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of invoked functional exchange for the given SequenceMessage.",
        params = {
            @Param(name = "value", value = "the SequenceMessage")
        },
        result = "the Sequence of invoked functional exchange for the given SequenceMessage",
        examples = {
            @Example(expression = "mySequenceMessage.getInvokedFunctionalExchange()", result = "the Sequence of invoked functional exchange for the given SequenceMessage"),
        }
    )
    // @formatter:on
    public List<AbstractEventOperation> getInvokedFunctionalExchange(SequenceMessage value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessage_AllocatedFunctionalExchange()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of function target for the given SequenceMessage.",
        params = {
            @Param(name = "value", value = "the SequenceMessage")
        },
        result = "the Sequence of function target for the given SequenceMessage",
        examples = {
            @Example(expression = "mySequenceMessage.getFunctionTarget()", result = "the Sequence of function target for the given SequenceMessage"),
        }
    )
    // @formatter:on
    public List<AbstractFunction> getFunctionTarget(SequenceMessage value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageFunctionTarget()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of part target for the given SequenceMessage.",
        params = {
            @Param(name = "value", value = "the SequenceMessage")
        },
        result = "the Sequence of part target for the given SequenceMessage",
        examples = {
            @Example(expression = "mySequenceMessage.getPartTarget()", result = "the Sequence of part target for the given SequenceMessage"),
        }
    )
    // @formatter:on
    public List<Part> getPartTarget(SequenceMessage value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessagePartTarget()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of invoked operation for the given SequenceMessage.",
        params = {
            @Param(name = "value", value = "the SequenceMessage")
        },
        result = "the Sequence of invoked operation for the given SequenceMessage",
        examples = {
            @Example(expression = "mySequenceMessage.getInvokedOperation()", result = "the Sequence of invoked operation for the given SequenceMessage"),
        }
    )
    // @formatter:on
    public List<AbstractEventOperation> getInvokedOperation(SequenceMessage value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessage_invokedOperation()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of part source for the given SequenceMessage.",
        params = {
            @Param(name = "value", value = "the SequenceMessage")
        },
        result = "the Sequence of part source for the given SequenceMessage",
        examples = {
            @Example(expression = "mySequenceMessage.getPartSource()", result = "the Sequence of part source for the given SequenceMessage"),
        }
    )
    // @formatter:on
    public List<Part> getPartSource(SequenceMessage value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessagePartSource()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of refining sequence message for the given SequenceMessage.",
        params = {
            @Param(name = "value", value = "the SequenceMessage")
        },
        result = "the Sequence of refining sequence message for the given SequenceMessage",
        examples = {
            @Example(expression = "mySequenceMessage.getRefiningSequenceMessage()", result = "the Sequence of refining sequence message for the given SequenceMessage"),
        }
    )
    // @formatter:on
    public List<SequenceMessage> getRefiningSequenceMessage(SequenceMessage value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessage_refinedSequenceMessage()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of function source for the given SequenceMessage.",
        params = {
            @Param(name = "value", value = "the SequenceMessage")
        },
        result = "the Sequence of function source for the given SequenceMessage",
        examples = {
            @Example(expression = "mySequenceMessage.getFunctionSource()", result = "the Sequence of function source for the given SequenceMessage"),
        }
    )
    // @formatter:on
    public List<AbstractFunction> getFunctionSource(SequenceMessage value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.SequenceMessageFunctionSource()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of parent for the given Scenario.",
        params = {
            @Param(name = "value", value = "the Scenario")
        },
        result = "the Sequence of parent for the given Scenario",
        examples = {
            @Example(expression = "myScenario.getParent()", result = "the Sequence of parent for the given Scenario"),
        }
    )
    // @formatter:on
    public List<EObject> getParent(Scenario value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ItemQuery_Scenario_getAbstractCapabilityContainer()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of referenced scenario for the given Scenario.",
        params = {
            @Param(name = "value", value = "the Scenario")
        },
        result = "the Sequence of referenced scenario for the given Scenario",
        examples = {
            @Example(expression = "myScenario.getReferencedScenario()", result = "the Sequence of referenced scenario for the given Scenario"),
        }
    )
    // @formatter:on
    public List<Scenario> getReferencedScenario(Scenario value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ItemQuery_Scenario_getReferencedScenarios()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized scenarii for the given Scenario.",
        params = {
            @Param(name = "value", value = "the Scenario")
        },
        result = "the Sequence of realized scenarii for the given Scenario",
        examples = {
            @Example(expression = "myScenario.getRealizedScenarios()", result = "the Sequence of realized scenarii for the given Scenario"),
        }
    )
    // @formatter:on
    public List<Scenario> getRealizedScenarios(Scenario value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Scenario_realizedScenario()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of refined scenarii for the given Scenario.",
        params = {
            @Param(name = "value", value = "the Scenario")
        },
        result = "the Sequence of refined scenarii for the given Scenario",
        examples = {
            @Example(expression = "myScenario.getRefinedScenarios()", result = "the Sequence of refined scenarii for the given Scenario"),
        }
    )
    // @formatter:on
    public List<Scenario> getRefinedScenarios(Scenario value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Scenario_refiningScenarios()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of refining scenarii for the given Scenario.",
        params = {
            @Param(name = "value", value = "the Scenario")
        },
        result = "the Sequence of refining scenarii for the given Scenario",
        examples = {
            @Example(expression = "myScenario.getRefiningScenarios()", result = "the Sequence of refining scenarii for the given Scenario"),
        }
    )
    // @formatter:on
    public List<Scenario> getRefiningScenarios(Scenario value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Scenario_refinedScenarios()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing scenarii for the given Scenario.",
        params = {
            @Param(name = "value", value = "the Scenario")
        },
        result = "the Sequence of realizing scenarii for the given Scenario",
        examples = {
            @Example(expression = "myScenario.getRealizingScenarios()", result = "the Sequence of realizing scenarii for the given Scenario"),
        }
    )
    // @formatter:on
    public List<Scenario> getRealizingScenarios(Scenario value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Scenario_realizingScenario()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of parent scenario for the given InstanceRole.",
        params = {
            @Param(name = "value", value = "the InstanceRole")
        },
        result = "the Sequence of parent scenario for the given InstanceRole",
        examples = {
            @Example(expression = "myInstanceRole.getParentScenario()", result = "the Sequence of parent scenario for the given InstanceRole"),
        }
    )
    // @formatter:on
    public List<Scenario> getParentScenario(InstanceRole value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.InstanceRole_parentScenario()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of represented instance for the given InstanceRole.",
        params = {
            @Param(name = "value", value = "the InstanceRole")
        },
        result = "the Sequence of represented instance for the given InstanceRole",
        examples = {
            @Example(expression = "myInstanceRole.getRepresentedInstance()", result = "the Sequence of represented instance for the given InstanceRole"),
        }
    )
    // @formatter:on
    public List<AbstractType> getRepresentedInstance(InstanceRole value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.InstanceRole_representedInstance()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of target for the given AbstractCapabilityExtend.",
        params = {
            @Param(name = "value", value = "the AbstractCapabilityExtend")
        },
        result = "the Sequence of target for the given AbstractCapabilityExtend",
        examples = {
            @Example(expression = "myAbstractCapabilityExtend.getTarget()", result = "the Sequence of target for the given AbstractCapabilityExtend"),
        }
    )
    // @formatter:on
    public List<AbstractCapability> getTarget(AbstractCapabilityExtend value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAbstractCapabilityExtendTarget()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of source for the given AbstractCapabilityExtend.",
        params = {
            @Param(name = "value", value = "the AbstractCapabilityExtend")
        },
        result = "the Sequence of source for the given AbstractCapabilityExtend",
        examples = {
            @Example(expression = "myAbstractCapabilityExtend.getSource()", result = "the Sequence of source for the given AbstractCapabilityExtend"),
        }
    )
    // @formatter:on
    public List<AbstractCapability> getSource(AbstractCapabilityExtend value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAbstractCapabilityExtendSource()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of target for the given AbstractCapabilityGeneralization.",
        params = {
            @Param(name = "value", value = "the AbstractCapabilityGeneralization")
        },
        result = "the Sequence of target for the given AbstractCapabilityGeneralization",
        examples = {
            @Example(expression = "myAbstractCapabilityGeneralization.getTarget()", result = "the Sequence of target for the given AbstractCapabilityGeneralization"),
        }
    )
    // @formatter:on
    public List<AbstractCapability> getTarget(AbstractCapabilityGeneralization value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAbstractCapabilityGeneralizationTarget()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of source for the given AbstractCapabilityGeneralization.",
        params = {
            @Param(name = "value", value = "the AbstractCapabilityGeneralization")
        },
        result = "the Sequence of source for the given AbstractCapabilityGeneralization",
        examples = {
            @Example(expression = "myAbstractCapabilityGeneralization.getSource()", result = "the Sequence of source for the given AbstractCapabilityGeneralization"),
        }
    )
    // @formatter:on
    public List<AbstractCapability> getSource(AbstractCapabilityGeneralization value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAbstractCapabilityGeneralizationSource()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of target for the given AbstractCapabilityInclude.",
        params = {
            @Param(name = "value", value = "the AbstractCapabilityInclude")
        },
        result = "the Sequence of target for the given AbstractCapabilityInclude",
        examples = {
            @Example(expression = "myAbstractCapabilityInclude.getTarget()", result = "the Sequence of target for the given AbstractCapabilityInclude"),
        }
    )
    // @formatter:on
    public List<AbstractCapability> getTarget(AbstractCapabilityInclude value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAbstractCapabilityIncludeTarget()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of source for the given AbstractCapabilityInclude.",
        params = {
            @Param(name = "value", value = "the AbstractCapabilityInclude")
        },
        result = "the Sequence of source for the given AbstractCapabilityInclude",
        examples = {
            @Example(expression = "myAbstractCapabilityInclude.getSource()", result = "the Sequence of source for the given AbstractCapabilityInclude"),
        }
    )
    // @formatter:on
    public List<Object> getSource(AbstractCapabilityInclude value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAbstractCapabilityIncludeSource()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of referenced senario for the given InteractionUse.",
        params = {
            @Param(name = "value", value = "the InteractionUse")
        },
        result = "the Sequence of referenced senario for the given InteractionUse",
        examples = {
            @Example(expression = "myInteractionUse.getReferencedScenario()", result = "the Sequence of referenced senario for the given InteractionUse"),
        }
    )
    // @formatter:on
    public List<Scenario> getReferencedScenario(InteractionUse value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.InteractionUseReferencedScenario()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of referencing senario for the given InteractionUse.",
        params = {
            @Param(name = "value", value = "the InteractionUse")
        },
        result = "the Sequence of referencing senario for the given InteractionUse",
        examples = {
            @Example(expression = "myInteractionUse.getReferencingScenario()", result = "the Sequence of referencing senario for the given InteractionUse"),
        }
    )
    // @formatter:on
    public List<Scenario> getReferencingScenario(InteractionUse value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ItemQuery_Scenario_getReferencingScenarios()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of related function for the given StateFragment.",
        params = {
            @Param(name = "value", value = "the StateFragment")
        },
        result = "the Sequence of related function for the given StateFragment",
        examples = {
            @Example(expression = "myStateFragment.getRelatedFunction()", result = "the Sequence of related function for the given StateFragment"),
        }
    )
    // @formatter:on
    public List<AbstractFunction> getRelatedFunction(StateFragment value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.StateFragmentRelatedFunctions()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of related stat for the given StateFragment.",
        params = {
            @Param(name = "value", value = "the StateFragment")
        },
        result = "the Sequence of related stat for the given StateFragment",
        examples = {
            @Example(expression = "myStateFragment.getRelatedState()", result = "the Sequence of related stat for the given StateFragment"),
        }
    )
    // @formatter:on
    public List<AbstractState> getRelatedState(StateFragment value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.StateFragmentRelatedStates()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of roles for the given Association.",
        params = {
            @Param(name = "value", value = "the Association")
        },
        result = "the Sequence of roles for the given Association",
        examples = {
            @Example(expression = "myAssociation.getRoles()", result = "the Sequence of roles for the given Association"),
        }
    )
    // @formatter:on
    public List<Property> getRoles(Association value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaRelationshipsAssociationRoles()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized classes for the given Class.",
        params = {
            @Param(name = "value", value = "the Class")
        },
        result = "the Sequence of realized classes for the given Class",
        examples = {
            @Example(expression = "myClass.getRealizedClasses()", result = "the Sequence of realized classes for the given Class"),
        }
    )
    // @formatter:on
    public List<org.polarsys.capella.core.data.information.Class> getRealizedClasses(
            org.polarsys.capella.core.data.information.Class value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ClassRealizedClass().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing classes for the given Class.",
        params = {
            @Param(name = "value", value = "the Class")
        },
        result = "the Sequence of realizing classes for the given Class",
        examples = {
            @Example(expression = "myClass.getRealizingClasses()", result = "the Sequence of realizing classes for the given Class"),
        }
    )
    // @formatter:on
    public List<org.polarsys.capella.core.data.information.Class> getRealizingClasses(
            org.polarsys.capella.core.data.information.Class value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ClassRealizingClass().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of type for the given Collection.",
        params = {
            @Param(name = "value", value = "the Collection")
        },
        result = "the Sequence of type for the given Collection",
        examples = {
            @Example(expression = "myCollection.getType()", result = "the Sequence of type for the given Collection"),
        }
    )
    // @formatter:on
    public List<Type> getType(Collection value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CollectionType().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of scenarii for the given Operation.",
        params = {
            @Param(name = "value", value = "the Operation")
        },
        result = "the Sequence of scenarii for the given Operation",
        examples = {
            @Example(expression = "myOperation.getScenarios()", result = "the Sequence of scenarii for the given Operation"),
        }
    )
    // @formatter:on
    public List<Scenario> getScenarios(Operation value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of type for the given Parameter.",
        params = {
            @Param(name = "value", value = "the Parameter")
        },
        result = "the Sequence of type for the given Parameter",
        examples = {
            @Example(expression = "myParameter.getType()", result = "the Sequence of type for the given Parameter"),
        }
    )
    // @formatter:on
    public List<Type> getType(Parameter value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.Parameter_Type().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized echange items for the given ExchangeItem.",
        params = {
            @Param(name = "value", value = "the ExchangeItem")
        },
        result = "the Sequence of realized echange items for the given ExchangeItem",
        examples = {
            @Example(expression = "myExchangeItem.getRealizedExchangeItems()", result = "the Sequence of realized echange items for the given ExchangeItem"),
        }
    )
    // @formatter:on
    public List<ExchangeItem> getRealizedExchangeItems(ExchangeItem value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeItem_realizedEI().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of echange item elements for the given ExchangeItem.",
        params = {
            @Param(name = "value", value = "the ExchangeItem")
        },
        result = "the Sequence of echange item elements for the given ExchangeItem",
        examples = {
            @Example(expression = "myExchangeItem.getExchangeItemElements()", result = "the Sequence of echange item elements for the given ExchangeItem"),
        }
    )
    // @formatter:on
    public List<ExchangeItemElement> getExchangeItemElements(ExchangeItem value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangesItemExchangeItemElements()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing exchange items for the given ExchangeItem.",
        params = {
            @Param(name = "value", value = "the ExchangeItem")
        },
        result = "the Sequence of realizing exchange items for the given ExchangeItem",
        examples = {
            @Example(expression = "myExchangeItem.getRealizingExchangeItems()", result = "the Sequence of realizing exchange items for the given ExchangeItem"),
        }
    )
    // @formatter:on
    public List<ExchangeItem> getRealizingExchangeItems(ExchangeItem value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeItem_realizingEI().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating function output ports for the given ExchangeItem.",
        params = {
            @Param(name = "value", value = "the ExchangeItem")
        },
        result = "the Sequence of allocating function output ports for the given ExchangeItem",
        examples = {
            @Example(expression = "myExchangeItem.getAllocatingFunctionOutputPorts()", result = "the Sequence of allocating function output ports for the given ExchangeItem"),
        }
    )
    // @formatter:on
    public List<FunctionOutputPort> getAllocatingFunctionOutputPorts(ExchangeItem value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeItemAllocatingOutPutFunctionPorts()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating function input ports for the given ExchangeItem.",
        params = {
            @Param(name = "value", value = "the ExchangeItem")
        },
        result = "the Sequence of allocating function input ports for the given ExchangeItem",
        examples = {
            @Example(expression = "myExchangeItem.getAllocatingFunctionInputPorts()", result = "the Sequence of allocating function input ports for the given ExchangeItem"),
        }
    )
    // @formatter:on
    public List<FunctionInputPort> getAllocatingFunctionInputPorts(ExchangeItem value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeItemAllocatingInputFunctionPorts()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of interfaces for the given ExchangeItem.",
        params = {
            @Param(name = "value", value = "the ExchangeItem")
        },
        result = "the Sequence of interfaces for the given ExchangeItem",
        examples = {
            @Example(expression = "myExchangeItem.getInterfaces()", result = "the Sequence of interfaces for the given ExchangeItem"),
        }
    )
    // @formatter:on
    // TODO ExchangeItemAllocation and ExchangeItemAllocation
    public List<EObject> getInterfaces(ExchangeItem value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangesItemExchangeItemAllocations()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of communication links for the given ExchangeItem.",
        params = {
            @Param(name = "value", value = "the ExchangeItem")
        },
        result = "the Sequence of communication links for the given ExchangeItem",
        examples = {
            @Example(expression = "myExchangeItem.getCommunicationLinks()", result = "the Sequence of communication links for the given ExchangeItem"),
        }
    )
    // @formatter:on
    // TODO ExchangeItemAllocation and CommunicationLink
    public List<EObject> getCommunicationLinks(ExchangeItem value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangesItemCommLink().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of allocating exchanges for the given ExchangeItem.",
        params = {
            @Param(name = "value", value = "the ExchangeItem")
        },
        result = "the Sequence of allocating exchanges for the given ExchangeItem",
        examples = {
            @Example(expression = "myExchangeItem.getAllocatingExchanges()", result = "the Sequence of allocating exchanges for the given ExchangeItem"),
        }
    )
    // @formatter:on
    // AbstractInformationFlow and FunctionalExchange
    public List<Object> getAllocatingExchanges(ExchangeItem value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.EIActiveInConnectionsAndExchanges()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of type for the given ExchangeItemElement.",
        params = {
            @Param(name = "value", value = "the ExchangeItemElement")
        },
        result = "the Sequence of type for the given ExchangeItemElement",
        examples = {
            @Example(expression = "myExchangeItemElement.getType()", result = "the Sequence of type for the given ExchangeItemElement"),
        }
    )
    // @formatter:on
    public List<AbstractType> getType(ExchangeItemElement value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ExchangeItemElementType().compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of scenarii for the given ExchangeItemInstance.",
        params = {
            @Param(name = "value", value = "the ExchangeItemInstance")
        },
        result = "the Sequence of scenarii for the given ExchangeItemInstance",
        examples = {
            @Example(expression = "myExchangeItemInstance.getType()", result = "the Sequence of scenarii for the given ExchangeItemInstance"),
        }
    )
    // @formatter:on
    public List<Scenario> getScenarios(ExchangeItemInstance value) {
        return castList(new org.polarsys.capella.core.semantic.queries.basic.queries.CapellaElementReferencingScenario()
                .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realized state transition for the given StateTransition.",
        params = {
            @Param(name = "value", value = "the StateTransition")
        },
        result = "the Sequence of realized state transition for the given StateTransition",
        examples = {
            @Example(expression = "myStateTransition.getRealizedStateTransition()", result = "the Sequence of realized state transition for the given StateTransition"),
        }
    )
    // @formatter:on
    public List<StateTransition> getRealizedStateTransition(StateTransition value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.StateTransition_RealizedStateTransition()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of realizing state transition for the given StateTransition.",
        params = {
            @Param(name = "value", value = "the StateTransition")
        },
        result = "the Sequence of realizing state transition for the given StateTransition",
        examples = {
            @Example(expression = "myStateTransition.getRealizingStateTransition()", result = "the Sequence of realizing state transition for the given StateTransition"),
        }
    )
    // @formatter:on
    public List<StateTransition> getRealizingStateTransition(StateTransition value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.StateTransition_RealizingStateTransition()
                        .compute(value));
    }

    // TODO uncomment before release
//    // @formatter:off
//    @Documentation(
//        value = "Returns the Sequence of all related diagrams for the given ModelElement.",
//        params = {
//            @Param(name = "value", value = "the ModelElement")
//        },
//        result = "the Sequence of all related diagrams for the given ModelElement",
//        examples = {
//            @Example(expression = "myModelElement.getAllRelatedDiagrams()", result = "the Sequence of all related diagrams for the given ModelElement"),
//        }
//    )
//    // @formatter:on
    // public List<DRepresentationDescriptor> getAllRelatedDiagrams(ModelElement value) {
    // return castList(new org.polarsys.capella.core.semantic.queries.sirius.diagram.ModelElementRelatedDiagramsQuery()
    // .compute(value));
    // }
    //
//    // @formatter:off
//    @Documentation(
//        value = "Returns the Sequence of all related tables for the given ModelElement.",
//        params = {
//            @Param(name = "value", value = "the ModelElement")
//        },
//        result = "the Sequence of all related tables for the given ModelElement",
//        examples = {
//            @Example(expression = "myModelElement.getAllRelatedTables()", result = "the Sequence of all related tables for the given ModelElement"),
//        }
//    )
//    // @formatter:on
    // public List<DRepresentationDescriptor> getAllRelatedTables(ModelElement value) {
    // return castList(new org.polarsys.capella.core.semantic.queries.sirius.diagram.ModelElementRelatedTablesQuery()
    // .compute(value));
    // }
    //
//    // @formatter:off
//    @Documentation(
//        value = "Returns the Sequence of element of interest for diagram for the given Element.",
//        params = {
//            @Param(name = "value", value = "the Element")
//        },
//        result = "the Sequence of element of interest for diagram for the given Element",
//        examples = {
//            @Example(expression = "myModelElement.getElementOfInterestForDiagram()", result = "the Sequence of element of interest for diagram for the given Element"),
//        }
//    )
//    // @formatter:on
    // public List<EObject> getElementOfInterestForDiagram(Element value) {
    // return castList(new org.polarsys.capella.core.semantic.queries.sirius.annotation.eoi.ElementToRepresentation()
    // .compute(value));
    // }
    //
//    // @formatter:off
//    @Documentation(
//        value = "Returns the Sequence of element of interest for the given DRepresentationDescriptor.",
//        params = {
//            @Param(name = "value", value = "the DRepresentationDescriptor")
//        },
//        result = "the Sequence of element of interest for the given DRepresentationDescriptor",
//        examples = {
//            @Example(expression = "myDRepresentationDescriptor.getElementsOfInterest()", result = "the Sequence of element of interest for the given DRepresentationDescriptor"),
//        }
//    )
//    // @formatter:on
    // public List<EObject> getElementsOfInterest(DRepresentationDescriptor value) {
    // return castList(new org.polarsys.capella.core.semantic.queries.sirius.annotation.eoi.RepresentationToElement()
    // .compute(value));
    // }
    //
//    // @formatter:off
//    @Documentation(
//        value = "Returns the Sequence of contextual elements for the given DRepresentationDescriptor.",
//        params = {
//            @Param(name = "value", value = "the DRepresentationDescriptor")
//        },
//        result = "the Sequence of contextual elements for the given DRepresentationDescriptor",
//        examples = {
//            @Example(expression = "myDRepresentationDescriptor.getContextualElements()", result = "the Sequence of contextual elements for the given DRepresentationDescriptor"),
//        }
//    )
//    // @formatter:on
    // public List<EObject> getContextualElements(DRepresentationDescriptor value) {
    // return castList(
    // new org.polarsys.capella.core.semantic.queries.sirius.annotation.eoi.RepresentationToContextualElement()
    // .compute(value));
    // }
    //
//    // @formatter:off
//    @Documentation(
//        value = "Returns the Sequence of target element for the given DRepresentationDescriptor.",
//        params = {
//            @Param(name = "value", value = "the DRepresentationDescriptor")
//        },
//        result = "the Sequence of target element for the given DRepresentationDescriptor",
//        examples = {
//            @Example(expression = "myDRepresentationDescriptor.getTargetElement()", result = "the Sequence of target element for the given DRepresentationDescriptor"),
//        }
//    )
//    // @formatter:on
    // public List<EObject> getTargetElement(DRepresentationDescriptor value) {
    // return castList(
    // new org.polarsys.capella.core.semantic.queries.sirius.diagram.RepresentationDescriptorTargetElement()
    // .compute(value));
    // }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of associated element for the given DAnnotation.",
        params = {
            @Param(name = "value", value = "the DAnnotation")
        },
        result = "the Sequence of associated element for the given DAnnotation",
        examples = {
            @Example(expression = "myDAnnotation.getAssociatedElement()", result = "the Sequence of associated element for the given DAnnotation"),
        }
    )
    // @formatter:on
    public List<EObject> getAssociatedElement(DAnnotation value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.ElementTitleBlockReferencedElements()
                        .compute(value));
    }

    // @formatter:off
    @Documentation(
        value = "Returns the Sequence of associated diagram for the given DAnnotation.",
        params = {
            @Param(name = "value", value = "the DAnnotation")
        },
        result = "the Sequence of associated diagram for the given DAnnotation",
        examples = {
            @Example(expression = "myDAnnotation.getAssociatedDiagram()", result = "the Sequence of associated diagram for the given DAnnotation"),
        }
    )
    // @formatter:on
    public List<DRepresentationDescriptor> getAssociatedDiagram(DAnnotation value) {
        return castList(
                new org.polarsys.capella.core.semantic.queries.basic.queries.DiagramTitleBlockReferencedElements()
                        .compute(value));
    }

}
