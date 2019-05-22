package soot.jimple.toolkits.annotation.arraycheck;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.Local;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.EqExpr;
import soot.jimple.FieldRef;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.Stmt;
import soot.jimple.SubExpr;
import soot.options.Options;
import soot.toolkits.graph.ArrayRefBlockGraph;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.SlowPseudoTopologicalOrderer;

class ArrayBoundsCheckerAnalysis {
   private static final Logger logger = LoggerFactory.getLogger(ArrayBoundsCheckerAnalysis.class);
   protected Map<Block, WeightedDirectedSparseGraph> blockToBeforeFlow;
   protected Map<Unit, WeightedDirectedSparseGraph> unitToBeforeFlow;
   private final Map<FlowGraphEdge, WeightedDirectedSparseGraph> edgeMap;
   private final Set<FlowGraphEdge> edgeSet;
   private HashMap<Block, Integer> stableRoundOfUnits;
   private ArrayRefBlockGraph graph;
   private final IntContainer zero = new IntContainer(0);
   private boolean fieldin = false;
   private HashMap<Object, HashSet<Value>> localToFieldRef;
   private HashMap<Object, HashSet<Value>> fieldToFieldRef;
   private final int strictness = 2;
   private boolean arrayin = false;
   private boolean csin = false;
   private HashMap<Value, HashSet<Value>> localToExpr;
   private boolean classfieldin = false;
   private ClassFieldAnalysis cfield;
   private boolean rectarray = false;
   private HashSet<Local> rectarrayset;
   private HashSet<Local> multiarraylocals;
   private final ArrayIndexLivenessAnalysis ailanalysis;

   public ArrayBoundsCheckerAnalysis(Body body, boolean takeClassField, boolean takeFieldRef, boolean takeArrayRef, boolean takeCSE, boolean takeRectArray) {
      this.classfieldin = takeClassField;
      this.fieldin = takeFieldRef;
      this.arrayin = takeArrayRef;
      this.csin = takeCSE;
      this.rectarray = takeRectArray;
      SootMethod thismethod = body.getMethod();
      if (Options.v().debug()) {
         logger.debug("ArrayBoundsCheckerAnalysis started on  " + thismethod.getName());
      }

      this.ailanalysis = new ArrayIndexLivenessAnalysis(new ExceptionalUnitGraph(body), this.fieldin, this.arrayin, this.csin, this.rectarray);
      if (this.fieldin) {
         this.localToFieldRef = this.ailanalysis.getLocalToFieldRef();
         this.fieldToFieldRef = this.ailanalysis.getFieldToFieldRef();
      }

      if (this.arrayin && this.rectarray) {
         this.multiarraylocals = this.ailanalysis.getMultiArrayLocals();
         this.rectarrayset = new HashSet();
         RectangularArrayFinder pgbuilder = RectangularArrayFinder.v();
         Iterator localIt = this.multiarraylocals.iterator();

         while(localIt.hasNext()) {
            Local local = (Local)localIt.next();
            MethodLocal mlocal = new MethodLocal(thismethod, local);
            if (pgbuilder.isRectangular(mlocal)) {
               this.rectarrayset.add(local);
            }
         }
      }

      if (this.csin) {
         this.localToExpr = this.ailanalysis.getLocalToExpr();
      }

      if (this.classfieldin) {
         this.cfield = ClassFieldAnalysis.v();
      }

      this.graph = new ArrayRefBlockGraph(body);
      this.blockToBeforeFlow = new HashMap(this.graph.size() * 2 + 1, 0.7F);
      this.edgeMap = new HashMap(this.graph.size() * 2 + 1, 0.7F);
      this.edgeSet = this.buildEdgeSet(this.graph);
      this.doAnalysis();
      this.convertToUnitEntry();
      if (Options.v().debug()) {
         logger.debug("ArrayBoundsCheckerAnalysis finished.");
      }

   }

   private void convertToUnitEntry() {
      this.unitToBeforeFlow = new HashMap();
      Iterator blockIt = this.blockToBeforeFlow.keySet().iterator();

      while(blockIt.hasNext()) {
         Block block = (Block)blockIt.next();
         Unit first = block.getHead();
         this.unitToBeforeFlow.put(first, this.blockToBeforeFlow.get(block));
      }

   }

   public Set<FlowGraphEdge> buildEdgeSet(DirectedGraph<Block> dg) {
      HashSet<FlowGraphEdge> edges = new HashSet();
      Iterator blockIt = dg.iterator();

      while(true) {
         while(blockIt.hasNext()) {
            Block s = (Block)blockIt.next();
            List<Block> preds = this.graph.getPredsOf(s);
            List<Block> succs = this.graph.getSuccsOf(s);
            if (preds.size() == 0) {
               edges.add(new FlowGraphEdge(s, s));
            }

            if (succs.size() == 0) {
               edges.add(new FlowGraphEdge(s, s));
            } else {
               Iterator succIt = succs.iterator();

               while(succIt.hasNext()) {
                  edges.add(new FlowGraphEdge(s, succIt.next()));
               }
            }
         }

         return edges;
      }
   }

   public Object getFlowBefore(Object s) {
      return this.unitToBeforeFlow.get(s);
   }

   private void mergebunch(Object[] ins, Object s, Object prevOut, Object out) {
      WeightedDirectedSparseGraph prevgraph = (WeightedDirectedSparseGraph)prevOut;
      WeightedDirectedSparseGraph outgraph = (WeightedDirectedSparseGraph)out;
      WeightedDirectedSparseGraph[] ingraphs = new WeightedDirectedSparseGraph[ins.length];

      int i;
      for(i = 0; i < ins.length; ++i) {
         ingraphs[i] = (WeightedDirectedSparseGraph)ins[i];
      }

      outgraph.addBoundedAll(ingraphs[0]);

      for(i = 1; i < ingraphs.length; ++i) {
         outgraph.unionSelf(ingraphs[i]);
         outgraph.makeShortestPathGraph();
      }

      outgraph.widenEdges(prevgraph);
   }

   private void doAnalysis() {
      Date start = new Date();
      if (Options.v().debug()) {
         logger.debug("Building PseudoTopological order list on " + start);
      }

      LinkedList allUnits = (LinkedList)SlowPseudoTopologicalOrderer.v().newList(this.graph, false);
      BoundedPriorityList changedUnits = new BoundedPriorityList(allUnits);
      Date finish = new Date();
      long runtime;
      if (Options.v().debug()) {
         long runtime = finish.getTime() - start.getTime();
         long mins = runtime / 60000L;
         runtime = runtime % 60000L / 1000L;
         logger.debug("Building PseudoTopological order finished. It took " + mins + " mins and " + runtime + " secs.");
      }

      start = new Date();
      HashSet changedUnitsSet = new HashSet(allUnits);
      FlowGraphEdge tmpEdge = new FlowGraphEdge();
      HashSet<Block> unvisitedNodes = new HashSet(this.graph.size() * 2 + 1, 0.7F);
      Iterator it = this.graph.iterator();

      Block s;
      HashSet livelocals;
      while(it.hasNext()) {
         s = (Block)it.next();
         livelocals = (HashSet)this.ailanalysis.getFlowBefore(s.getHead());
         livelocals.add(this.zero);
      }

      this.stableRoundOfUnits = new HashMap();
      it = this.graph.iterator();

      while(it.hasNext()) {
         s = (Block)it.next();
         unvisitedNodes.add(s);
         this.stableRoundOfUnits.put(s, new Integer(0));
         livelocals = (HashSet)this.ailanalysis.getFlowBefore(s.getHead());
         this.blockToBeforeFlow.put(s, new WeightedDirectedSparseGraph(livelocals, false));
      }

      Iterator headIt = this.edgeSet.iterator();

      while(headIt.hasNext()) {
         FlowGraphEdge edge = (FlowGraphEdge)headIt.next();
         Block target = (Block)edge.to;
         HashSet livelocals = (HashSet)this.ailanalysis.getFlowBefore(target.getHead());
         this.edgeMap.put(edge, new WeightedDirectedSparseGraph(livelocals, false));
      }

      List headlist = this.graph.getHeads();
      headIt = headlist.iterator();

      while(headIt.hasNext()) {
         Object head = headIt.next();
         FlowGraphEdge edge = new FlowGraphEdge(head, head);
         WeightedDirectedSparseGraph initgraph = (WeightedDirectedSparseGraph)this.edgeMap.get(edge);
         initgraph.setTop();
      }

      WeightedDirectedSparseGraph beforeFlow = new WeightedDirectedSparseGraph((HashSet)null, false);

      while(!changedUnits.isEmpty()) {
         s = (Block)changedUnits.removeFirst();
         changedUnitsSet.remove(s);
         WeightedDirectedSparseGraph previousBeforeFlow = (WeightedDirectedSparseGraph)this.blockToBeforeFlow.get(s);
         beforeFlow.setVertexes(previousBeforeFlow.getVertexes());
         List preds = this.graph.getPredsOf(s);
         if (preds.size() == 0) {
            tmpEdge.changeEndUnits(s, s);
            this.copy(this.edgeMap.get(tmpEdge), beforeFlow);
         } else if (preds.size() == 1) {
            tmpEdge.changeEndUnits(preds.get(0), s);
            this.copy(this.edgeMap.get(tmpEdge), beforeFlow);
         } else {
            Object[] predFlows = new Object[preds.size()];
            boolean allUnvisited = true;
            Iterator predIt = preds.iterator();
            int index = 0;

            int lastVisited;
            Object tmp;
            for(lastVisited = 0; predIt.hasNext(); predFlows[index++] = this.edgeMap.get(tmpEdge)) {
               tmp = predIt.next();
               tmpEdge.changeEndUnits(tmp, s);
               if (!unvisitedNodes.contains(tmp)) {
                  allUnvisited = false;
                  lastVisited = index;
               }
            }

            if (allUnvisited) {
               logger.debug("Warning : see all unvisited node");
            } else {
               tmp = predFlows[0];
               predFlows[0] = predFlows[lastVisited];
               predFlows[lastVisited] = tmp;
            }

            this.mergebunch(predFlows, s, previousBeforeFlow, beforeFlow);
         }

         this.copy(beforeFlow, previousBeforeFlow);
         List<Object> changedSuccs = this.flowThrough(beforeFlow, s);

         for(int i = 0; i < changedSuccs.size(); ++i) {
            Object succ = changedSuccs.get(i);
            if (!changedUnitsSet.contains(succ)) {
               changedUnits.add(succ);
               changedUnitsSet.add(succ);
            }
         }

         unvisitedNodes.remove(s);
      }

      finish = new Date();
      if (Options.v().debug()) {
         runtime = finish.getTime() - start.getTime();
         long mins = runtime / 60000L;
         long secs = runtime / 60000L / 1000L;
         logger.debug("Doing analysis finished. It took " + mins + " mins and " + secs + "secs.");
      }

   }

   private List<Object> flowThrough(Object inValue, Object unit) {
      ArrayList<Object> changedSuccs = new ArrayList();
      WeightedDirectedSparseGraph ingraph = (WeightedDirectedSparseGraph)inValue;
      Block block = (Block)unit;
      List succs = block.getSuccs();
      Unit s = block.getHead();

      for(Unit nexts = block.getSuccOf(s); nexts != null; nexts = block.getSuccOf(nexts)) {
         this.assertArrayRef(ingraph, s);
         this.assertNormalExpr(ingraph, s);
         s = nexts;
      }

      if (s instanceof IfStmt) {
         if (!this.assertBranchStmt(ingraph, s, block, succs, changedSuccs)) {
            this.updateOutEdges(ingraph, block, succs, changedSuccs);
         }
      } else {
         this.assertArrayRef(ingraph, s);
         this.assertNormalExpr(ingraph, s);
         this.updateOutEdges(ingraph, block, succs, changedSuccs);
      }

      return changedSuccs;
   }

   private void assertArrayRef(Object in, Unit unit) {
      if (unit instanceof AssignStmt) {
         Stmt s = (Stmt)unit;
         WeightedDirectedSparseGraph ingraph = (WeightedDirectedSparseGraph)in;
         if (s.containsArrayRef()) {
            ArrayRef op = s.getArrayRef();
            Value base = op.getBase();
            Value index = op.getIndex();
            HashSet livelocals = (HashSet)this.ailanalysis.getFlowAfter(s);
            if (livelocals.contains(base) || livelocals.contains(index)) {
               if (index instanceof IntConstant) {
                  int weight = ((IntConstant)index).value;
                  weight = -1 - weight;
                  ingraph.addEdge(base, this.zero, weight);
               } else {
                  ingraph.addEdge(base, index, -1);
                  ingraph.addEdge(index, this.zero, 0);
               }

            }
         }
      }
   }

   private void assertNormalExpr(Object in, Unit s) {
      WeightedDirectedSparseGraph ingraph = (WeightedDirectedSparseGraph)in;
      Stmt stmt;
      HashSet fieldrefs;
      Iterator refsIt;
      Object base;
      if (this.fieldin) {
         stmt = (Stmt)s;
         if (stmt.containsInvokeExpr()) {
            new HashSet();
            Value expr = stmt.getInvokeExpr();
            List parameters = ((InvokeExpr)expr).getArgs();
            fieldrefs = ingraph.getVertexes();
            refsIt = fieldrefs.iterator();

            while(refsIt.hasNext()) {
               base = refsIt.next();
               if (base instanceof FieldRef) {
                  ingraph.killNode(base);
               }
            }
         }
      }

      if (this.arrayin) {
         stmt = (Stmt)s;
         if (stmt.containsInvokeExpr()) {
            HashSet vertexes = ingraph.getVertexes();
            Iterator nodeIt = vertexes.iterator();

            while(nodeIt.hasNext()) {
               Object node = nodeIt.next();
               if (node instanceof ArrayRef) {
                  ingraph.killNode(node);
               }
            }
         }
      }

      if (s instanceof AssignStmt) {
         Value leftOp = ((AssignStmt)s).getLeftOp();
         Value rightOp = ((AssignStmt)s).getRightOp();
         HashSet livelocals = (HashSet)this.ailanalysis.getFlowAfter(s);
         HashSet exprs;
         SootField field;
         Iterator exprIt;
         Object node;
         if (this.fieldin) {
            if (leftOp instanceof Local) {
               exprs = (HashSet)this.localToFieldRef.get(leftOp);
               if (exprs != null) {
                  exprIt = exprs.iterator();

                  while(exprIt.hasNext()) {
                     node = exprIt.next();
                     if (livelocals.contains(node)) {
                        ingraph.killNode(node);
                     }
                  }
               }
            } else if (leftOp instanceof InstanceFieldRef) {
               field = ((InstanceFieldRef)leftOp).getField();
               fieldrefs = (HashSet)this.fieldToFieldRef.get(field);
               if (fieldrefs != null) {
                  refsIt = fieldrefs.iterator();

                  while(refsIt.hasNext()) {
                     base = refsIt.next();
                     if (livelocals.contains(base)) {
                        ingraph.killNode(base);
                     }
                  }
               }
            }
         }

         if (this.arrayin) {
            if (leftOp instanceof Local) {
               exprs = ingraph.getVertexes();
               exprIt = exprs.iterator();

               while(exprIt.hasNext()) {
                  node = exprIt.next();
                  if (node instanceof ArrayRef) {
                     Value base = ((ArrayRef)node).getBase();
                     Value index = ((ArrayRef)node).getIndex();
                     if (base.equals(leftOp) || index.equals(leftOp)) {
                        ingraph.killNode(node);
                     }
                  }

                  if (this.rectarray && node instanceof Array2ndDimensionSymbol) {
                     base = ((Array2ndDimensionSymbol)node).getVar();
                     if (base.equals(leftOp)) {
                        ingraph.killNode(node);
                     }
                  }
               }
            } else if (leftOp instanceof ArrayRef) {
               exprs = ingraph.getVertexes();
               exprIt = exprs.iterator();

               while(exprIt.hasNext()) {
                  node = exprIt.next();
                  if (node instanceof ArrayRef) {
                     ingraph.killNode(node);
                  }
               }
            }
         }

         if (livelocals.contains(leftOp) || livelocals.contains(rightOp)) {
            if (!rightOp.equals(leftOp)) {
               if (this.csin) {
                  exprs = (HashSet)this.localToExpr.get(leftOp);
                  if (exprs != null) {
                     exprIt = exprs.iterator();

                     while(exprIt.hasNext()) {
                        ingraph.killNode(exprIt.next());
                     }
                  }
               }

               Value size;
               Value op2;
               int inc_w;
               if (rightOp instanceof AddExpr) {
                  size = ((AddExpr)rightOp).getOp1();
                  op2 = ((AddExpr)rightOp).getOp2();
                  if (size == leftOp && op2 instanceof IntConstant) {
                     inc_w = ((IntConstant)op2).value;
                     ingraph.updateWeight(leftOp, inc_w);
                     return;
                  }

                  if (op2 == leftOp && size instanceof IntConstant) {
                     inc_w = ((IntConstant)size).value;
                     ingraph.updateWeight(leftOp, inc_w);
                     return;
                  }
               }

               if (rightOp instanceof SubExpr) {
                  size = ((SubExpr)rightOp).getOp1();
                  op2 = ((SubExpr)rightOp).getOp2();
                  if (size == leftOp && op2 instanceof IntConstant) {
                     inc_w = -((IntConstant)op2).value;
                     ingraph.updateWeight(leftOp, inc_w);
                     return;
                  }
               }

               ingraph.killNode(leftOp);
               if (rightOp instanceof IntConstant) {
                  int inc_w = ((IntConstant)rightOp).value;
                  ingraph.addMutualEdges(this.zero, leftOp, inc_w);
               } else if (rightOp instanceof Local) {
                  ingraph.addMutualEdges(rightOp, leftOp, 0);
               } else if (rightOp instanceof FieldRef) {
                  if (this.fieldin) {
                     ingraph.addMutualEdges(rightOp, leftOp, 0);
                  }

                  if (this.classfieldin) {
                     field = ((FieldRef)rightOp).getField();
                     IntValueContainer flength = (IntValueContainer)this.cfield.getFieldInfo(field);
                     if (flength != null && flength.isInteger()) {
                        ingraph.addMutualEdges(this.zero, leftOp, flength.getValue());
                     }
                  }

               } else if (this.arrayin && rightOp instanceof ArrayRef) {
                  ingraph.addMutualEdges(rightOp, leftOp, 0);
                  if (this.rectarray) {
                     size = ((ArrayRef)rightOp).getBase();
                     if (this.rectarrayset.contains(size)) {
                        ingraph.addMutualEdges(leftOp, Array2ndDimensionSymbol.v(size), 0);
                     }
                  }

               } else {
                  if (this.csin && rightOp instanceof BinopExpr) {
                     op2 = ((BinopExpr)rightOp).getOp1();
                     Value op2 = ((BinopExpr)rightOp).getOp2();
                     if (rightOp instanceof AddExpr) {
                        if (op2 instanceof Local && op2 instanceof Local) {
                           ingraph.addMutualEdges(rightOp, leftOp, 0);
                           return;
                        }
                     } else if (rightOp instanceof MulExpr) {
                        if (op2 instanceof Local || op2 instanceof Local) {
                           ingraph.addMutualEdges(rightOp, leftOp, 0);
                           return;
                        }
                     } else if (rightOp instanceof SubExpr && op2 instanceof Local) {
                        ingraph.addMutualEdges(rightOp, leftOp, 0);
                        return;
                     }
                  }

                  if (rightOp instanceof AddExpr) {
                     size = ((AddExpr)rightOp).getOp1();
                     op2 = ((AddExpr)rightOp).getOp2();
                     if (size instanceof Local && op2 instanceof IntConstant) {
                        inc_w = ((IntConstant)op2).value;
                        ingraph.addMutualEdges(size, leftOp, inc_w);
                        return;
                     }

                     if (op2 instanceof Local && size instanceof IntConstant) {
                        inc_w = ((IntConstant)size).value;
                        ingraph.addMutualEdges(op2, leftOp, inc_w);
                        return;
                     }
                  }

                  if (rightOp instanceof SubExpr) {
                     size = ((SubExpr)rightOp).getOp1();
                     op2 = ((SubExpr)rightOp).getOp2();
                     if (size instanceof Local && op2 instanceof IntConstant) {
                        inc_w = -((IntConstant)op2).value;
                        ingraph.addMutualEdges(size, leftOp, inc_w);
                        return;
                     }
                  }

                  int inc_w;
                  if (rightOp instanceof NewArrayExpr) {
                     size = ((NewArrayExpr)rightOp).getSize();
                     if (size instanceof Local) {
                        ingraph.addMutualEdges(size, leftOp, 0);
                        return;
                     }

                     if (size instanceof IntConstant) {
                        inc_w = ((IntConstant)size).value;
                        ingraph.addMutualEdges(this.zero, leftOp, inc_w);
                        return;
                     }
                  }

                  if (rightOp instanceof NewMultiArrayExpr) {
                     size = ((NewMultiArrayExpr)rightOp).getSize(0);
                     if (size instanceof Local) {
                        ingraph.addMutualEdges(size, leftOp, 0);
                     } else if (size instanceof IntConstant) {
                        inc_w = ((IntConstant)size).value;
                        ingraph.addMutualEdges(this.zero, leftOp, inc_w);
                     }

                     if (this.arrayin && this.rectarray && ((NewMultiArrayExpr)rightOp).getSizeCount() > 1) {
                        size = ((NewMultiArrayExpr)rightOp).getSize(1);
                        if (size instanceof Local) {
                           ingraph.addMutualEdges(size, Array2ndDimensionSymbol.v(leftOp), 0);
                        } else if (size instanceof IntConstant) {
                           inc_w = ((IntConstant)size).value;
                           ingraph.addMutualEdges(this.zero, Array2ndDimensionSymbol.v(leftOp), inc_w);
                        }
                     }

                  } else if (rightOp instanceof LengthExpr) {
                     size = ((LengthExpr)rightOp).getOp();
                     ingraph.addMutualEdges(size, leftOp, 0);
                  }
               }
            }
         }
      }
   }

   private boolean assertBranchStmt(Object in, Unit s, Block current, List succs, List<Object> changedSuccs) {
      IfStmt ifstmt = (IfStmt)s;
      Value cmpcond = ifstmt.getCondition();
      if (!(cmpcond instanceof ConditionExpr)) {
         return false;
      } else if (succs.size() != 2) {
         return false;
      } else {
         Stmt targetUnit = ifstmt.getTarget();
         Block targetBlock = (Block)succs.get(0);
         Block nextBlock = (Block)succs.get(1);
         if (!targetUnit.equals(targetBlock.getHead())) {
            Block swap = targetBlock;
            targetBlock = nextBlock;
            nextBlock = swap;
         }

         Value op1 = ((ConditionExpr)cmpcond).getOp1();
         Value op2 = ((ConditionExpr)cmpcond).getOp2();
         HashSet livelocals = (HashSet)this.ailanalysis.getFlowAfter(s);
         if (!livelocals.contains(op1) && !livelocals.contains(op2)) {
            return false;
         } else {
            WeightedDirectedSparseGraph ingraph = (WeightedDirectedSparseGraph)in;
            WeightedDirectedSparseGraph targetgraph = ingraph.dup();
            Object node1;
            Object node2;
            int weight;
            if (!(cmpcond instanceof EqExpr) && !(cmpcond instanceof NeExpr)) {
               if (!(cmpcond instanceof GtExpr) && !(cmpcond instanceof GeExpr)) {
                  if (!(cmpcond instanceof LtExpr) && !(cmpcond instanceof LeExpr)) {
                     return false;
                  }

                  node1 = op1;
                  node2 = op2;
                  weight = 0;
                  if (op1 instanceof IntConstant) {
                     weight -= ((IntConstant)op1).value;
                     node1 = this.zero;
                  }

                  if (op2 instanceof IntConstant) {
                     weight += ((IntConstant)op2).value;
                     node2 = this.zero;
                  }

                  if (node1 == node2) {
                     return false;
                  }

                  if (cmpcond instanceof LtExpr) {
                     targetgraph.addEdge(node2, node1, weight - 1);
                     ingraph.addEdge(node1, node2, -weight);
                  } else {
                     targetgraph.addEdge(node2, node1, weight);
                     ingraph.addEdge(node1, node2, -weight - 1);
                  }
               } else {
                  node1 = op1;
                  node2 = op2;
                  weight = 0;
                  if (op1 instanceof IntConstant) {
                     weight += ((IntConstant)op1).value;
                     node1 = this.zero;
                  }

                  if (op2 instanceof IntConstant) {
                     weight -= ((IntConstant)op2).value;
                     node2 = this.zero;
                  }

                  if (node1 == node2) {
                     return false;
                  }

                  if (cmpcond instanceof GtExpr) {
                     targetgraph.addEdge(node1, node2, weight - 1);
                     ingraph.addEdge(node2, node1, -weight);
                  } else {
                     targetgraph.addEdge(node1, node2, weight);
                     ingraph.addEdge(node2, node1, -weight - 1);
                  }
               }
            } else {
               node1 = op1;
               node2 = op2;
               weight = 0;
               if (op1 instanceof IntConstant) {
                  weight = ((IntConstant)op1).value;
                  node1 = this.zero;
               }

               if (op2 instanceof IntConstant) {
                  weight = ((IntConstant)op2).value;
                  node2 = this.zero;
               }

               if (node1 == node2) {
                  return false;
               }

               if (cmpcond instanceof EqExpr) {
                  targetgraph.addMutualEdges(node1, node2, weight);
               } else {
                  ingraph.addMutualEdges(node1, node2, weight);
               }
            }

            FlowGraphEdge targetEdge = new FlowGraphEdge(current, targetBlock);
            WeightedDirectedSparseGraph prevtarget = (WeightedDirectedSparseGraph)this.edgeMap.get(targetEdge);
            boolean changed = false;
            targetgraph.makeShortestPathGraph();
            WeightedDirectedSparseGraph tmpgraph = new WeightedDirectedSparseGraph(prevtarget.getVertexes(), true);
            this.copy(targetgraph, tmpgraph);
            if (!tmpgraph.equals(prevtarget)) {
               prevtarget.replaceAllEdges(tmpgraph);
               changed = true;
            }

            if (changed) {
               changedSuccs.add(targetBlock);
            }

            FlowGraphEdge nextEdge = new FlowGraphEdge(current, nextBlock);
            WeightedDirectedSparseGraph prevnext = (WeightedDirectedSparseGraph)this.edgeMap.get(nextEdge);
            changed = false;
            ingraph.makeShortestPathGraph();
            tmpgraph = new WeightedDirectedSparseGraph(prevnext.getVertexes(), true);
            this.copy(ingraph, tmpgraph);
            if (!tmpgraph.equals(prevnext)) {
               prevnext.replaceAllEdges(tmpgraph);
               changed = true;
            }

            if (changed) {
               changedSuccs.add(nextBlock);
            }

            return true;
         }
      }
   }

   private void updateOutEdges(Object in, Block current, List succs, List<Object> changedSuccs) {
      WeightedDirectedSparseGraph ingraph = (WeightedDirectedSparseGraph)in;
      ingraph.makeShortestPathGraph();

      for(int i = 0; i < succs.size(); ++i) {
         Object next = succs.get(i);
         FlowGraphEdge nextEdge = new FlowGraphEdge(current, next);
         WeightedDirectedSparseGraph prevs = (WeightedDirectedSparseGraph)this.edgeMap.get(nextEdge);
         boolean changed = false;
         WeightedDirectedSparseGraph tmpgraph = new WeightedDirectedSparseGraph(prevs.getVertexes(), true);
         this.copy(ingraph, tmpgraph);
         if (!tmpgraph.equals(prevs)) {
            prevs.replaceAllEdges(tmpgraph);
            changed = true;
         }

         if (changed) {
            changedSuccs.add(next);
         }
      }

   }

   protected void copy(Object from, Object to) {
      WeightedDirectedSparseGraph fromgraph = (WeightedDirectedSparseGraph)from;
      WeightedDirectedSparseGraph tograph = (WeightedDirectedSparseGraph)to;
      tograph.clear();
      tograph.addBoundedAll(fromgraph);
   }

   protected void widenGraphs(Object current, Object before) {
      WeightedDirectedSparseGraph curgraphs = (WeightedDirectedSparseGraph)current;
      WeightedDirectedSparseGraph pregraphs = (WeightedDirectedSparseGraph)before;
      curgraphs.widenEdges(pregraphs);
      curgraphs.makeShortestPathGraph();
   }
}
