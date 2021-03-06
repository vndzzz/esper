/*
 ***************************************************************************************
 *  Copyright (C) 2006 EsperTech, Inc. All rights reserved.                            *
 *  http://www.espertech.com/esper                                                     *
 *  http://www.espertech.com                                                           *
 *  ---------------------------------------------------------------------------------- *
 *  The software in this package is published under the terms of the GPL license       *
 *  a copy of which has been included with this distribution in the license.txt file.  *
 ***************************************************************************************
 */
package com.espertech.esper.common.internal.epl.agg.access.linear;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.common.internal.bytecodemodel.base.CodegenMethod;
import com.espertech.esper.common.internal.epl.agg.core.AggregationAccessorForgeGetCodegenContext;
import com.espertech.esper.common.internal.epl.expression.codegen.CodegenLegoMethodExpression;

import java.util.Collections;

import static com.espertech.esper.common.internal.bytecodemodel.model.expression.CodegenExpressionBuilder.*;

/**
 * Represents the aggregation accessor that provides the result for the "last" aggregation function without index.
 */
public class AggregationAccessorLastWEval {
    public static void getValueCodegen(AggregationAccessorLastWEvalForge forge, AggregationStateLinearForge factoryLinear, AggregationAccessorForgeGetCodegenContext context) {
        CodegenMethod childExpr = CodegenLegoMethodExpression.codegenExpression(forge.getChildNode(), context.getMethod(), context.getClassScope());
        context.getMethod().getBlock().declareVar(EventBean.class, "bean", factoryLinear.getAggregatorLinear().getLastValueCodegen(context.getClassScope(), context.getMethod(), context.getNamedMethods()))
                .ifRefNullReturnNull("bean")
                .declareVar(EventBean[].class, "eventsPerStreamBuf", newArrayByLength(EventBean.class, constant(forge.getStreamNum() + 1)))
                .assignArrayElement("eventsPerStreamBuf", constant(forge.getStreamNum()), ref("bean"))
                .methodReturn(localMethod(childExpr, ref("eventsPerStreamBuf"), constant(true), constantNull()));
    }

    public static void getEnumerableEventsCodegen(AggregationAccessorLastWEvalForge forge, AggregationStateLinearForge factoryLinear, AggregationAccessorForgeGetCodegenContext context) {
        context.getMethod().getBlock().declareVar(EventBean.class, "bean", factoryLinear.getAggregatorLinear().getLastValueCodegen(context.getClassScope(), context.getMethod(), context.getNamedMethods()))
                .ifRefNullReturnNull("bean")
                .methodReturn(staticMethod(Collections.class, "singletonList", ref("bean")));
    }

    public static void getEnumerableScalarCodegen(AggregationAccessorLastWEvalForge forge, AggregationStateLinearForge stateForge, AggregationAccessorForgeGetCodegenContext context) {
        CodegenMethod childExpr = CodegenLegoMethodExpression.codegenExpression(forge.getChildNode(), context.getMethod(), context.getClassScope());
        context.getMethod().getBlock().declareVar(EventBean.class, "bean", stateForge.getAggregatorLinear().getLastValueCodegen(context.getClassScope(), context.getMethod(), context.getNamedMethods()))
                .ifRefNullReturnNull("bean")
                .declareVar(EventBean[].class, "eventsPerStreamBuf", newArrayByLength(EventBean.class, constant(forge.getStreamNum() + 1)))
                .assignArrayElement("eventsPerStreamBuf", constant(forge.getStreamNum()), ref("bean"))
                .declareVar(Object.class, "value", localMethod(childExpr, ref("eventsPerStreamBuf"), constant(true), constantNull()))
                .ifRefNullReturnNull("value")
                .methodReturn(staticMethod(Collections.class, "singletonList", ref("value")));
    }

    public static void getEnumerableEventCodegen(AggregationAccessorLastWEvalForge forge, AggregationStateLinearForge stateForge, AggregationAccessorForgeGetCodegenContext context) {
        context.getMethod().getBlock().methodReturn(stateForge.getAggregatorLinear().getLastValueCodegen(context.getClassScope(), context.getMethod(), context.getNamedMethods()));
    }
}