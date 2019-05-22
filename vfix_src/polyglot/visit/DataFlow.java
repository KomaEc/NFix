package polyglot.visit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import polyglot.ast.Binary;
import polyglot.ast.CodeDecl;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Term;
import polyglot.ast.Unary;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.IdentityKey;
import polyglot.util.InternalCompilerError;
import polyglot.util.StringUtil;

public abstract class DataFlow extends ErrorHandlingVisitor {
   protected final boolean forward;
   protected final boolean dataflowOnEntry;
   protected LinkedList flowgraphStack;
   protected static int flowCounter = 0;

   public DataFlow(Job job, TypeSystem ts, NodeFactory nf, boolean forward) {
      this(job, ts, nf, forward, false);
   }

   public DataFlow(Job job, TypeSystem ts, NodeFactory nf, boolean forward, boolean dataflowOnEntry) {
      super(job, ts, nf);
      this.forward = forward;
      this.dataflowOnEntry = dataflowOnEntry;
      if (dataflowOnEntry) {
         this.flowgraphStack = new LinkedList();
      } else {
         this.flowgraphStack = null;
      }

   }

   protected abstract DataFlow.Item createInitialItem(FlowGraph var1, Term var2);

   protected Map flow(DataFlow.Item in, FlowGraph graph, Term n, Set edgeKeys) {
      throw new InternalCompilerError("Unimplemented: should be implemented by subclasses if needed");
   }

   protected Map flow(List inItems, List inItemKeys, FlowGraph graph, Term n, Set edgeKeys) {
      DataFlow.Item inItem = this.safeConfluence(inItems, inItemKeys, n, graph);
      return this.flow(inItem, graph, n, edgeKeys);
   }

   protected final Map flowToBooleanFlow(List inItems, List inItemKeys, FlowGraph graph, Term n, Set edgeKeys) {
      List trueItems = new ArrayList();
      List trueItemKeys = new ArrayList();
      List falseItems = new ArrayList();
      List falseItemKeys = new ArrayList();
      List otherItems = new ArrayList();
      List otherItemKeys = new ArrayList();
      Iterator i = inItems.iterator();
      Iterator j = inItemKeys.iterator();

      DataFlow.Item item;
      while(i.hasNext() || j.hasNext()) {
         item = (DataFlow.Item)i.next();
         FlowGraph.EdgeKey key = (FlowGraph.EdgeKey)j.next();
         if (FlowGraph.EDGE_KEY_TRUE.equals(key)) {
            trueItems.add(item);
            trueItemKeys.add(key);
         } else if (FlowGraph.EDGE_KEY_FALSE.equals(key)) {
            falseItems.add(item);
            falseItemKeys.add(key);
         } else {
            otherItems.add(item);
            otherItemKeys.add(key);
         }
      }

      item = trueItems.isEmpty() ? null : this.safeConfluence(trueItems, trueItemKeys, n, graph);
      DataFlow.Item falseItem = falseItems.isEmpty() ? null : this.safeConfluence(falseItems, falseItemKeys, n, graph);
      DataFlow.Item otherItem = otherItems.isEmpty() ? null : this.safeConfluence(otherItems, otherItemKeys, n, graph);
      return this.flow(item, falseItem, otherItem, graph, n, edgeKeys);
   }

   protected Map flow(DataFlow.Item trueItem, DataFlow.Item falseItem, DataFlow.Item otherItem, FlowGraph graph, Term n, Set edgeKeys) {
      throw new InternalCompilerError("Unimplemented: should be implemented by subclasses if needed");
   }

   protected Map flowBooleanConditions(DataFlow.Item trueItem, DataFlow.Item falseItem, DataFlow.Item otherItem, FlowGraph graph, Expr n, Set edgeKeys) {
      if (n.type().isBoolean() && (n instanceof Binary || n instanceof Unary)) {
         if (trueItem != null && falseItem != null) {
            if (n instanceof Unary) {
               Unary u = (Unary)n;
               if (u.operator() == Unary.NOT) {
                  return itemsToMap(falseItem, trueItem, otherItem, edgeKeys);
               }
            } else {
               Binary b = (Binary)n;
               if (b.operator() == Binary.COND_AND) {
                  return itemsToMap(trueItem, falseItem, otherItem, edgeKeys);
               }

               if (b.operator() == Binary.COND_OR) {
                  return itemsToMap(trueItem, falseItem, otherItem, edgeKeys);
               }

               DataFlow.Item bitORTrue;
               if (b.operator() == Binary.BIT_AND) {
                  bitORTrue = this.safeConfluence(trueItem, FlowGraph.EDGE_KEY_TRUE, falseItem, FlowGraph.EDGE_KEY_FALSE, n, graph);
                  return itemsToMap(trueItem, bitORTrue, otherItem, edgeKeys);
               }

               if (b.operator() == Binary.BIT_OR) {
                  bitORTrue = this.safeConfluence(trueItem, FlowGraph.EDGE_KEY_TRUE, falseItem, FlowGraph.EDGE_KEY_FALSE, n, graph);
                  return itemsToMap(bitORTrue, falseItem, otherItem, edgeKeys);
               }
            }

            return null;
         } else {
            throw new IllegalArgumentException("The trueItem and falseItem for flowBooleanConditions must be non-null.");
         }
      } else {
         throw new InternalCompilerError("This method only takes binary or unary operators of boolean type");
      }
   }

   protected abstract DataFlow.Item confluence(List var1, Term var2, FlowGraph var3);

   protected DataFlow.Item confluence(List items, List itemKeys, Term node, FlowGraph graph) {
      return this.confluence(items, node, graph);
   }

   protected DataFlow.Item safeConfluence(List items, List itemKeys, Term node, FlowGraph graph) {
      if (items.isEmpty()) {
         return this.createInitialItem(graph, node);
      } else {
         return items.size() == 1 ? (DataFlow.Item)items.get(0) : this.confluence(items, itemKeys, node, graph);
      }
   }

   protected DataFlow.Item safeConfluence(DataFlow.Item item1, FlowGraph.EdgeKey key1, DataFlow.Item item2, FlowGraph.EdgeKey key2, Term node, FlowGraph graph) {
      return this.safeConfluence(item1, key1, item2, key2, (DataFlow.Item)null, (FlowGraph.EdgeKey)null, node, graph);
   }

   protected DataFlow.Item safeConfluence(DataFlow.Item item1, FlowGraph.EdgeKey key1, DataFlow.Item item2, FlowGraph.EdgeKey key2, DataFlow.Item item3, FlowGraph.EdgeKey key3, Term node, FlowGraph graph) {
      List items = new ArrayList(3);
      List itemKeys = new ArrayList(3);
      if (item1 != null) {
         items.add(item1);
         itemKeys.add(key1);
      }

      if (item2 != null) {
         items.add(item2);
         itemKeys.add(key2);
      }

      if (item3 != null) {
         items.add(item3);
         itemKeys.add(key3);
      }

      return this.safeConfluence(items, itemKeys, node, graph);
   }

   protected abstract void check(FlowGraph var1, Term var2, DataFlow.Item var3, Map var4) throws SemanticException;

   protected void dataflow(CodeDecl cd) throws SemanticException {
      if (cd.body() != null) {
         FlowGraph g = this.initGraph(cd, cd);
         if (g != null) {
            CFGBuilder v = this.createCFGBuilder(this.ts, g);

            try {
               v.visitGraph();
            } catch (CFGBuildError var5) {
               throw new SemanticException(var5.message(), var5.position());
            }

            this.dataflow(g);
            this.post(g, cd);
            if (this.dataflowOnEntry) {
               this.flowgraphStack.addFirst(new DataFlow.FlowGraphSource(g, cd));
            }
         }
      }

   }

   protected LinkedList findSCCs(FlowGraph graph) {
      Collection peers = graph.peers();
      FlowGraph.Peer[] sorted = new FlowGraph.Peer[peers.size()];
      Collection start = graph.peers(graph.startNode());
      int n = 0;
      LinkedList stack = new LinkedList();
      Set reachable = new HashSet();
      Iterator i = start.iterator();

      while(true) {
         FlowGraph.Peer peer;
         do {
            if (!i.hasNext()) {
               FlowGraph.Peer[] by_scc = new FlowGraph.Peer[n];
               int[] scc_head = new int[n];
               Set visited = new HashSet();
               int head = 0;

               int j;
               for(j = n - 1; j >= 0; --j) {
                  if (!visited.contains(sorted[j])) {
                     Set SCC = new HashSet();
                     visited.add(sorted[j]);
                     stack.add(new DataFlow.Frame(sorted[j], false));

                     DataFlow.Frame top;
                     while(stack.size() != 0) {
                        DataFlow.Frame top = (DataFlow.Frame)stack.getFirst();
                        if (top.edges.hasNext()) {
                           FlowGraph.Edge e = (FlowGraph.Edge)top.edges.next();
                           FlowGraph.Peer q = e.getTarget();
                           if (reachable.contains(q) && !visited.contains(q)) {
                              visited.add(q);
                              top = new DataFlow.Frame(q, false);
                              stack.addFirst(top);
                           }
                        } else {
                           stack.removeFirst();
                           SCC.add(top.peer);
                        }
                     }

                     stack.add(new DataFlow.Frame(sorted[j], true));
                     Set revisited = new HashSet();
                     revisited.add(sorted[j]);
                     int scc_size = SCC.size();
                     int nsorted = 0;

                     while(stack.size() != 0) {
                        top = (DataFlow.Frame)stack.getFirst();
                        if (top.edges.hasNext()) {
                           FlowGraph.Edge e = (FlowGraph.Edge)top.edges.next();
                           FlowGraph.Peer q = e.getTarget();
                           if (SCC.contains(q) && !revisited.contains(q)) {
                              revisited.add(q);
                              DataFlow.Frame f = new DataFlow.Frame(q, true);
                              stack.addFirst(f);
                           }
                        } else {
                           stack.removeFirst();
                           int n3 = head + scc_size - nsorted - 1;
                           scc_head[n3] = -2;
                           by_scc[n3] = top.peer;
                           ++nsorted;
                        }
                     }

                     scc_head[head + scc_size - 1] = head;
                     scc_head[head] = -1;
                     head += scc_size;
                  }
               }

               if (Report.should_report((String)"dataflow", 2)) {
                  for(j = 0; j < n; ++j) {
                     switch(scc_head[j]) {
                     case -2:
                        Report.report(2, j + "       : " + by_scc[j]);
                        break;
                     case -1:
                        Report.report(2, j + "[HEAD] : " + by_scc[j]);
                        break;
                     default:
                        Report.report(2, j + " ->" + scc_head[j] + " : " + by_scc[j]);
                     }

                     Iterator i = by_scc[j].succs().iterator();

                     while(i.hasNext()) {
                        Report.report(3, "     successor: " + ((FlowGraph.Edge)i.next()).getTarget());
                     }
                  }
               }

               LinkedList ret = new LinkedList();
               ret.addFirst(scc_head);
               ret.addFirst(by_scc);
               return ret;
            }

            peer = (FlowGraph.Peer)i.next();
         } while(reachable.contains(peer));

         reachable.add(peer);
         stack.addFirst(new DataFlow.Frame(peer, true));

         while(stack.size() != 0) {
            DataFlow.Frame top = (DataFlow.Frame)stack.getFirst();
            if (top.edges.hasNext()) {
               FlowGraph.Edge e = (FlowGraph.Edge)top.edges.next();
               FlowGraph.Peer q = e.getTarget();
               if (!reachable.contains(q)) {
                  reachable.add(q);
                  stack.addFirst(new DataFlow.Frame(q, true));
               }
            } else {
               stack.removeFirst();
               sorted[n++] = top.peer;
            }
         }
      }
   }

   protected void dataflow(FlowGraph graph) {
      if (Report.should_report((String)"dataflow", 1)) {
         Report.report(1, "Finding strongly connected components");
      }

      LinkedList pair = this.findSCCs(graph);
      FlowGraph.Peer[] by_scc = (FlowGraph.Peer[])pair.getFirst();
      int[] scc_head = (int[])pair.getLast();
      int npeers = by_scc.length;
      if (Report.should_report((String)"dataflow", 1)) {
         Report.report(1, "Iterating dataflow equations");
      }

      int current = 0;
      boolean change = false;

      while(true) {
         while(current < npeers) {
            FlowGraph.Peer p = by_scc[current];
            if (scc_head[current] == -1) {
               change = false;
            }

            List inItems = new ArrayList(p.preds.size());
            List inItemKeys = new ArrayList(p.preds.size());
            Iterator i = p.preds.iterator();

            while(i.hasNext()) {
               FlowGraph.Edge e = (FlowGraph.Edge)i.next();
               FlowGraph.Peer o = e.getTarget();
               if (o.outItems != null) {
                  if (!o.outItems.keySet().contains(e.getKey())) {
                     throw new InternalCompilerError("There should have an out Item with edge key " + e.getKey() + "; instead there were only " + o.outItems.keySet());
                  }

                  DataFlow.Item it = (DataFlow.Item)o.outItems.get(e.getKey());
                  if (it != null) {
                     inItems.add(it);
                     inItemKeys.add(e.getKey());
                  }
               }
            }

            Map oldOutItems = p.outItems;
            p.inItem = this.safeConfluence(inItems, inItemKeys, p.node, graph);
            p.outItems = this.flow(inItems, inItemKeys, graph, p.node, p.succEdgeKeys());
            if (!p.succEdgeKeys().equals(p.outItems.keySet())) {
               throw new InternalCompilerError("The flow only defined outputs for " + p.outItems.keySet() + "; needs to " + "define outputs for all of: " + p.succEdgeKeys());
            }

            if (oldOutItems != p.outItems && (oldOutItems == null || !oldOutItems.equals(p.outItems))) {
               change = true;
            }

            if (change && scc_head[current] >= 0) {
               current = scc_head[current];
            } else {
               ++current;
            }
         }

         if (Report.should_report((String)"dataflow", 1)) {
            Report.report(1, "Done.");
         }

         return;
      }
   }

   protected FlowGraph initGraph(CodeDecl code, Term root) {
      return new FlowGraph(root, this.forward);
   }

   protected CFGBuilder createCFGBuilder(TypeSystem ts, FlowGraph g) {
      return new CFGBuilder(ts, g, this);
   }

   protected NodeVisitor enterCall(Node n) throws SemanticException {
      if (this.dataflowOnEntry && n instanceof CodeDecl) {
         this.dataflow((CodeDecl)n);
      }

      return this;
   }

   public Node leave(Node parent, Node old, Node n, NodeVisitor v) {
      if (old != n && this.dataflowOnEntry && this.currentFlowGraph() != null) {
         Object o = this.currentFlowGraph().peerMap.get(new IdentityKey(old));
         if (o != null) {
            this.currentFlowGraph().peerMap.put(new IdentityKey(n), o);
         }
      }

      return super.leave(parent, old, n, v);
   }

   protected Node leaveCall(Node n) throws SemanticException {
      if (n instanceof CodeDecl) {
         if (!this.dataflowOnEntry) {
            this.dataflow((CodeDecl)n);
         } else if (this.dataflowOnEntry && !this.flowgraphStack.isEmpty()) {
            DataFlow.FlowGraphSource fgs = (DataFlow.FlowGraphSource)this.flowgraphStack.getFirst();
            if (fgs.source.equals(n)) {
               this.flowgraphStack.removeFirst();
            }
         }
      }

      return n;
   }

   protected void post(FlowGraph graph, Term root) throws SemanticException {
      if (Report.should_report((String)"cfg", 2)) {
         this.dumpFlowGraph(graph, root);
      }

      Set uncheckedPeers = new HashSet(graph.peers());
      LinkedList peersToCheck = new LinkedList(graph.peers(graph.startNode()));

      while(!peersToCheck.isEmpty()) {
         FlowGraph.Peer p = (FlowGraph.Peer)peersToCheck.removeFirst();
         uncheckedPeers.remove(p);
         this.check(graph, p.node, p.inItem, p.outItems);
         Iterator i = p.succs.iterator();

         while(i.hasNext()) {
            FlowGraph.Peer q = ((FlowGraph.Edge)i.next()).getTarget();
            if (uncheckedPeers.contains(q) && !peersToCheck.contains(q)) {
               peersToCheck.addLast(q);
            }
         }

         if (peersToCheck.isEmpty() && !uncheckedPeers.isEmpty()) {
            i = uncheckedPeers.iterator();
            peersToCheck.add(i.next());
            i.remove();
         }
      }

   }

   protected FlowGraph currentFlowGraph() {
      if (!this.dataflowOnEntry) {
         throw new InternalCompilerError("currentFlowGraph() cannot be called when dataflow is not performed on entry");
      } else {
         return this.flowgraphStack.isEmpty() ? null : ((DataFlow.FlowGraphSource)this.flowgraphStack.getFirst()).flowgraph;
      }
   }

   protected static final Map itemToMap(DataFlow.Item i, Set edgeKeys) {
      Map m = new HashMap();
      Iterator iter = edgeKeys.iterator();

      while(iter.hasNext()) {
         Object o = iter.next();
         m.put(o, i);
      }

      return m;
   }

   protected static final Map itemsToMap(DataFlow.Item trueItem, DataFlow.Item falseItem, DataFlow.Item remainingItem, Set edgeKeys) {
      Map m = new HashMap();
      Iterator iter = edgeKeys.iterator();

      while(iter.hasNext()) {
         FlowGraph.EdgeKey k = (FlowGraph.EdgeKey)iter.next();
         if (FlowGraph.EDGE_KEY_TRUE.equals(k)) {
            m.put(k, trueItem);
         } else if (FlowGraph.EDGE_KEY_FALSE.equals(k)) {
            m.put(k, falseItem);
         } else {
            m.put(k, remainingItem);
         }
      }

      return m;
   }

   protected final List filterItemsNonError(List items, List itemKeys) {
      List filtered = new ArrayList(items.size());
      Iterator i = items.iterator();
      Iterator j = itemKeys.iterator();

      while(i.hasNext() && j.hasNext()) {
         DataFlow.Item item = (DataFlow.Item)i.next();
         FlowGraph.EdgeKey key = (FlowGraph.EdgeKey)j.next();
         if (!(key instanceof FlowGraph.ExceptionEdgeKey) || !((FlowGraph.ExceptionEdgeKey)key).type().isSubtype(this.ts.Error())) {
            filtered.add(item);
         }
      }

      if (!i.hasNext() && !j.hasNext()) {
         return filtered;
      } else {
         throw new InternalCompilerError("item and item key lists have different sizes.");
      }
   }

   protected final List filterItemsNonException(List items, List itemKeys) {
      List filtered = new ArrayList(items.size());
      Iterator i = items.iterator();
      Iterator j = itemKeys.iterator();

      while(i.hasNext() && j.hasNext()) {
         DataFlow.Item item = (DataFlow.Item)i.next();
         FlowGraph.EdgeKey key = (FlowGraph.EdgeKey)j.next();
         if (!(key instanceof FlowGraph.ExceptionEdgeKey)) {
            filtered.add(item);
         }
      }

      if (!i.hasNext() && !j.hasNext()) {
         return filtered;
      } else {
         throw new InternalCompilerError("item and item key lists have different sizes.");
      }
   }

   protected final List filterItemsExceptionSubclass(List items, List itemKeys, Type excType) {
      List filtered = new ArrayList(items.size());
      Iterator i = items.iterator();
      Iterator j = itemKeys.iterator();

      while(i.hasNext() && j.hasNext()) {
         DataFlow.Item item = (DataFlow.Item)i.next();
         FlowGraph.EdgeKey key = (FlowGraph.EdgeKey)j.next();
         if (key instanceof FlowGraph.ExceptionEdgeKey) {
            FlowGraph.ExceptionEdgeKey eek = (FlowGraph.ExceptionEdgeKey)key;
            if (eek.type().isImplicitCastValid(excType)) {
               filtered.add(item);
            }
         }
      }

      if (!i.hasNext() && !j.hasNext()) {
         return filtered;
      } else {
         throw new InternalCompilerError("item and item key lists have different sizes.");
      }
   }

   protected final List filterItems(List items, List itemKeys, FlowGraph.EdgeKey filterEdgeKey) {
      List filtered = new ArrayList(items.size());
      Iterator i = items.iterator();
      Iterator j = itemKeys.iterator();

      while(i.hasNext() && j.hasNext()) {
         DataFlow.Item item = (DataFlow.Item)i.next();
         FlowGraph.EdgeKey key = (FlowGraph.EdgeKey)j.next();
         if (filterEdgeKey.equals(key)) {
            filtered.add(item);
         }
      }

      if (!i.hasNext() && !j.hasNext()) {
         return filtered;
      } else {
         throw new InternalCompilerError("item and item key lists have different sizes.");
      }
   }

   protected static final boolean hasTrueFalseBranches(Set edgeKeys) {
      return edgeKeys.contains(FlowGraph.EDGE_KEY_FALSE) && edgeKeys.contains(FlowGraph.EDGE_KEY_TRUE);
   }

   protected static Map constructItemsFromCondition(Expr booleanCond, DataFlow.Item startingItem, Set succEdgeKeys, DataFlow.ConditionNavigator navigator) {
      if (!booleanCond.type().isBoolean()) {
         throw new IllegalArgumentException("booleanCond must be a boolean expression");
      } else if (!hasTrueFalseBranches(succEdgeKeys)) {
         throw new IllegalArgumentException("succEdgeKeys does not have true and false branches.");
      } else {
         DataFlow.BoolItem results = navigator.navigate(booleanCond, startingItem);
         Map m = new HashMap();
         m.put(FlowGraph.EDGE_KEY_TRUE, results.trueItem);
         m.put(FlowGraph.EDGE_KEY_FALSE, results.falseItem);
         Iterator iter = succEdgeKeys.iterator();

         while(iter.hasNext()) {
            FlowGraph.EdgeKey e = (FlowGraph.EdgeKey)iter.next();
            if (!FlowGraph.EDGE_KEY_TRUE.equals(e) && !FlowGraph.EDGE_KEY_FALSE.equals(e)) {
               m.put(e, startingItem);
            }
         }

         return m;
      }
   }

   protected void dumpFlowGraph(FlowGraph graph, Term root) {
      String name = StringUtil.getShortNameComponent(this.getClass().getName());
      name = name + flowCounter++;
      String rootName = "";
      if (graph.root() instanceof CodeDecl) {
         CodeDecl cd = (CodeDecl)graph.root();
         rootName = cd.codeInstance().toString() + " in " + cd.codeInstance().container().toString();
      }

      Report.report(2, "digraph DataFlow" + name + " {");
      Report.report(2, "  label=\"Dataflow: " + name + "\\n" + rootName + "\"; fontsize=20; center=true; ratio=auto; size = \"8.5,11\";");
      Iterator iter = graph.peers().iterator();

      while(iter.hasNext()) {
         FlowGraph.Peer p = (FlowGraph.Peer)iter.next();
         Report.report(2, p.hashCode() + " [ label = \"" + StringUtil.escape(p.node.toString()) + "\\n(" + StringUtil.escape(StringUtil.getShortNameComponent(p.node.getClass().getName())) + ")\" ];");

         FlowGraph.Edge q;
         String label;
         for(Iterator iter2 = p.succs.iterator(); iter2.hasNext(); Report.report(2, p.hashCode() + " -> " + q.getTarget().hashCode() + " [label=\"" + label + "\"];")) {
            q = (FlowGraph.Edge)iter2.next();
            Report.report(2, q.getTarget().hashCode() + " [ label = \"" + StringUtil.escape(q.getTarget().node.toString()) + " (" + StringUtil.escape(StringUtil.getShortNameComponent(q.getTarget().node.getClass().getName())) + ")\" ];");
            label = q.getKey().toString();
            if (p.outItems != null) {
               label = label + "\\n" + p.outItems.get(q.getKey());
            } else {
               label = label + "\\n[no dataflow available]";
            }
         }
      }

      Report.report(2, "}");
   }

   /** @deprecated */
   protected abstract static class ConditionNavigator {
      public DataFlow.BoolItem navigate(Expr expr, DataFlow.Item startingItem) {
         if (expr.type().isBoolean()) {
            DataFlow.BoolItem leftRes;
            if (expr instanceof Binary) {
               Binary b = (Binary)expr;
               DataFlow.Item rightResStart;
               DataFlow.BoolItem rightRes;
               if (!Binary.COND_AND.equals(b.operator()) && !Binary.BIT_AND.equals(b.operator())) {
                  if (!Binary.COND_OR.equals(b.operator()) && !Binary.BIT_OR.equals(b.operator())) {
                     return this.handleExpression(expr, startingItem);
                  }

                  leftRes = this.navigate(b.left(), startingItem);
                  rightResStart = startingItem;
                  if (Binary.COND_OR.equals(b.operator())) {
                     rightResStart = leftRes.falseItem;
                  }

                  rightRes = this.navigate(b.right(), rightResStart);
                  return this.orResults(leftRes, rightRes, startingItem);
               }

               leftRes = this.navigate(b.left(), startingItem);
               rightResStart = startingItem;
               if (Binary.COND_AND.equals(b.operator())) {
                  rightResStart = leftRes.trueItem;
               }

               rightRes = this.navigate(b.right(), rightResStart);
               return this.andResults(leftRes, rightRes, startingItem);
            } else if (expr instanceof Unary) {
               Unary u = (Unary)expr;
               if (Unary.NOT.equals(u.operator())) {
                  leftRes = this.navigate(u.expr(), startingItem);
                  return this.notResult(leftRes);
               }
            }
         }

         return this.handleExpression(expr, startingItem);
      }

      public DataFlow.BoolItem andResults(DataFlow.BoolItem left, DataFlow.BoolItem right, DataFlow.Item startingItem) {
         return new DataFlow.BoolItem(this.combine(left.trueItem, right.trueItem), startingItem);
      }

      public DataFlow.BoolItem orResults(DataFlow.BoolItem left, DataFlow.BoolItem right, DataFlow.Item startingItem) {
         return new DataFlow.BoolItem(startingItem, this.combine(left.falseItem, right.falseItem));
      }

      public DataFlow.BoolItem notResult(DataFlow.BoolItem results) {
         return new DataFlow.BoolItem(results.falseItem, results.trueItem);
      }

      public abstract DataFlow.Item combine(DataFlow.Item var1, DataFlow.Item var2);

      public abstract DataFlow.BoolItem handleExpression(Expr var1, DataFlow.Item var2);
   }

   protected static class BoolItem {
      DataFlow.Item trueItem;
      DataFlow.Item falseItem;

      public BoolItem(DataFlow.Item trueItem, DataFlow.Item falseItem) {
         this.trueItem = trueItem;
         this.falseItem = falseItem;
      }

      public DataFlow.Item trueItem() {
         return this.trueItem;
      }

      public DataFlow.Item falseItem() {
         return this.falseItem;
      }

      public String toString() {
         return "[ true: " + this.trueItem + "; false: " + this.falseItem + " ]";
      }
   }

   private static class Frame {
      FlowGraph.Peer peer;
      Iterator edges;

      Frame(FlowGraph.Peer p, boolean forward) {
         this.peer = p;
         if (forward) {
            this.edges = p.succs().iterator();
         } else {
            this.edges = p.preds().iterator();
         }

      }
   }

   public abstract static class Item {
      public abstract boolean equals(Object var1);

      public abstract int hashCode();
   }

   protected static class FlowGraphSource {
      FlowGraph flowgraph;
      CodeDecl source;

      FlowGraphSource(FlowGraph g, CodeDecl s) {
         this.flowgraph = g;
         this.source = s;
      }

      public FlowGraph flowGraph() {
         return this.flowgraph;
      }

      public CodeDecl source() {
         return this.source;
      }
   }
}
