/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tinkerpop.gremlin.process.traversal.strategy.optimization;

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.Step;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.TraversalStrategies;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.PathProcessor;
import org.apache.tinkerpop.gremlin.process.traversal.step.TraversalParent;
import org.apache.tinkerpop.gremlin.process.traversal.util.DefaultTraversalStrategies;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.tinkerpop.gremlin.process.traversal.P.gte;
import static org.apache.tinkerpop.gremlin.process.traversal.P.neq;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.as;
import static org.apache.tinkerpop.gremlin.process.traversal.strategy.optimization.PrunePathStrategy.MAX_BARRIER_SIZE;
import static org.junit.Assert.assertEquals;

/**
 * @author Ted Wilmes (http://twilmes.org)
 */
@RunWith(Parameterized.class)
public class PrunePathStrategyTest {

    private final List<TraversalStrategies> strategies = Arrays.asList(
            new DefaultTraversalStrategies().addStrategies(PrunePathStrategy.instance()),
            new DefaultTraversalStrategies().addStrategies(PrunePathStrategy.instance(), PathProcessorStrategy.instance()),
            new DefaultTraversalStrategies().addStrategies(PrunePathStrategy.instance(), PathProcessorStrategy.instance(), MatchPredicateStrategy.instance()),
            new DefaultTraversalStrategies().addStrategies(PrunePathStrategy.instance(), PathProcessorStrategy.instance(), MatchPredicateStrategy.instance(), RepeatUnrollStrategy.instance()),
            TraversalStrategies.GlobalCache.getStrategies(Graph.class));

    @Parameterized.Parameter(value = 0)
    public Traversal.Admin traversal;

    @Parameterized.Parameter(value = 1)
    public List<Set<String>> labels;

    @Parameterized.Parameter(value = 2)
    public Traversal.Admin optimized;

    @Test
    public void doTest() {
        for (final TraversalStrategies currentStrategies : this.strategies) {
            final Traversal.Admin<?, ?> currentTraversal = this.traversal.clone();
            currentTraversal.setStrategies(currentStrategies);
            currentTraversal.applyStrategies();
            final List<Object> keepLabels = getKeepLabels(currentTraversal);
            assertEquals(keepLabels, this.labels);
            if (null != optimized)
                assertEquals(currentTraversal, optimized);
        }
    }

    private List<Object> getKeepLabels(final Traversal.Admin<?, ?> traversal) {
        List<Object> keepLabels = new ArrayList<>();
        for (Step step : traversal.getSteps()) {
            if (step instanceof PathProcessor) {
                final Set<String> keepers = ((PathProcessor) step).getKeepLabels();
                if (keepers != null)
                    keepLabels.add(keepers);
            }
            if (step instanceof TraversalParent) {
                final TraversalParent parent = (TraversalParent) step;
                final List<Traversal.Admin<?, ?>> children = new ArrayList<>();
                children.addAll(parent.getGlobalChildren());
                children.addAll(parent.getLocalChildren());
                for (final Traversal.Admin<?, ?> child : children) {
                    final List<Object> childLabels = getKeepLabels(child);
                    if (childLabels.size() > 0) {
                        keepLabels.add(childLabels);
                    }
                }
            }
        }
        return keepLabels;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> generateTestParameters() {

        return Arrays.asList(new Object[][]{
                {__.out(), Arrays.asList(), null},
                {__.V().as("a").out().as("b").where(neq("a")).out(), Arrays.asList(Collections.emptySet()), null},
                {__.V().as("a").out().where(neq("a")).out().select("a"), Arrays.asList(Collections.singleton("a"), Collections.emptySet()), null},
                {__.V().as("a").out().as("b").where(neq("a")).out().select("a", "b").out().select("b"), Arrays.asList(new HashSet<>(Arrays.asList("a", "b")), Collections.singleton("b"), Collections.emptySet()), null},
                {__.V().match(__.as("a").out().as("b")), Arrays.asList(new HashSet<>(Arrays.asList("a", "b"))), null},
                {__.V().match(__.as("a").out().as("b")).select("a"), Arrays.asList(new HashSet<>(Arrays.asList("a", "b")), Collections.emptySet()), null},
                {__.V().out().out().match(
                        as("a").in("created").as("b"),
                        as("b").in("knows").as("c")).select("c").out("created").where(neq("a")).values("name"),
                        Arrays.asList(new HashSet<>(Arrays.asList("a", "b", "c")), Collections.singleton("a"), Collections.emptySet()), null},
                {__.V().as("a").out().select("a").path(), Arrays.asList(), null},
                {__.V().as("a").out().select("a").subgraph("b"), Arrays.asList(Collections.emptySet()), null},
                {__.V().as("a").out().select("a").subgraph("b").select("a"), Arrays.asList(Collections.singleton("a"), Collections.emptySet()), null},
                {__.V().out().as("a").where(neq("a")).out().where(neq("a")).out(), Arrays.asList(Collections.singleton("a"), Collections.emptySet()), null},
                {__.V().out().as("a").where(__.out().select("a").values("prop").count().is(gte(1))).out().where(neq("a")), Arrays.asList(Arrays.asList(Collections.singleton("a")), Collections.emptySet()), null},
                {__.V().as("a").out().as("b").where(__.out().select("a", "b", "c").values("prop").count().is(gte(1))).out().where(neq("a")).out().select("b"),
                        Arrays.asList(Arrays.asList(new HashSet<>(Arrays.asList("a", "b", "c"))), Collections.singleton("b"), Collections.emptySet()), null},
                {__.outE().inV().group().by(__.inE().outV().groupCount().by(__.both().count().is(P.gt(2)))), Arrays.asList(), null},
                {__.V().as("a").repeat(__.out().where(neq("a"))).emit().select("a").values("test"), Arrays.asList(Arrays.asList(Collections.singleton("a")), Collections.emptySet()), null},
                // given the way this test harness is structured, I have to manual test for RepeatUnrollStrategy (and it works) TODO: add more test parameters
                // {__.V().as("a").repeat(__.out().where(neq("a"))).times(3).select("a").values("test"), Arrays.asList(Collections.singleton("a"), Collections.singleton("a"), Collections.singleton("a"), Collections.emptySet())}
                {__.V().as("a").out().as("b").select("a").out().out(), Arrays.asList(Collections.emptySet()), __.V().as("a").out().as("b").select("a").barrier(MAX_BARRIER_SIZE).out().out()},
                {__.V().as("a").out().as("b").select("a").count(), Arrays.asList(Collections.emptySet()), __.V().as("a").out().as("b").select("a").count()},
                {__.V().as("a").out().as("b").select("a").barrier().count(), Arrays.asList(Collections.emptySet()), __.V().as("a").out().as("b").select("a").barrier().count()},
                {__.V().as("a").out().as("b").where(P.gt("a")).out().out(), Arrays.asList(Collections.emptySet()), __.V().as("a").out().as("b").where(P.gt("a")).barrier(MAX_BARRIER_SIZE).out().out()},
                {__.V().as("a").out().as("b").where(P.gt("a")).count(), Arrays.asList(Collections.emptySet()), __.V().as("a").out().as("b").where(P.gt("a")).count()},
                {__.V().as("a").out().as("b").select("a").as("c").where(P.gt("b")).out(), Arrays.asList(Collections.singleton("b"), Collections.emptySet()), __.V().as("a").out().as("b").select("a").as("c").barrier(MAX_BARRIER_SIZE).where(P.gt("b")).barrier(MAX_BARRIER_SIZE).out()},
                // TODO: why is the local child preserving c and e?
                {__.V().as("a").out().as("b").select("a").select("b").local(as("c").out().as("d", "e").select("c", "e").out().select("c")).out().select("c"),
                        Arrays.asList(new HashSet<>(Arrays.asList("b", "c", "e")), new HashSet<>(Arrays.asList("c", "e")), Arrays.asList(new HashSet<>(Arrays.asList("c", "e")), new HashSet<>(Arrays.asList("c", "e"))), Collections.emptySet()), null},
                // TODO: same as above but note how path() makes things react
                {__.V().as("a").out().as("b").select("a").select("b").path().local(as("c").out().as("d", "e").select("c", "e").out().select("c")).out().select("c"),
                        Arrays.asList(Arrays.asList(new HashSet<>(Arrays.asList("c", "e")), new HashSet<>(Arrays.asList("c", "e")))), null},
        });
    }
}