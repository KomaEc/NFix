package soot.jimple.toolkits.infoflow;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.EquivalentValue;
import soot.Local;
import soot.RefLikeType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.VoidType;
import soot.jimple.AnyNewExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
import soot.jimple.IdentityRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.ParameterRef;
import soot.jimple.Ref;
import soot.jimple.ReturnStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.UnopExpr;
import soot.jimple.internal.JCaughtExceptionRef;
import soot.toolkits.graph.HashMutableDirectedGraph;
import soot.toolkits.graph.MemoryEfficientGraph;
import soot.toolkits.graph.UnitGraph;

public class SmartMethodInfoFlowAnalysis {
   private static final Logger logger = LoggerFactory.getLogger(SmartMethodInfoFlowAnalysis.class);
   UnitGraph graph;
   SootMethod sm;
   Value thisLocal;
   InfoFlowAnalysis dfa;
   boolean refOnly;
   boolean includeInnerFields;
   HashMutableDirectedGraph<EquivalentValue> abbreviatedInfoFlowGraph;
   HashMutableDirectedGraph<EquivalentValue> infoFlowSummary;
   Ref returnRef;
   boolean printMessages;
   public static int counter = 0;

   public SmartMethodInfoFlowAnalysis(UnitGraph g, InfoFlowAnalysis dfa) {
      this.graph = g;
      this.sm = g.getBody().getMethod();
      if (this.sm.isStatic()) {
         this.thisLocal = null;
      } else {
         this.thisLocal = g.getBody().getThisLocal();
      }

      this.dfa = dfa;
      this.refOnly = !dfa.includesPrimitiveInfoFlow();
      this.includeInnerFields = dfa.includesInnerFields();
      this.abbreviatedInfoFlowGraph = new MemoryEfficientGraph();
      this.infoFlowSummary = new MemoryEfficientGraph();
      this.returnRef = new ParameterRef(g.getBody().getMethod().getReturnType(), -1);
      this.printMessages = false;
      ++counter;

      EquivalentValue thisRefEqVal;
      for(int i = 0; i < this.sm.getParameterCount(); ++i) {
         thisRefEqVal = InfoFlowAnalysis.getNodeForParameterRef(this.sm, i);
         if (!this.infoFlowSummary.containsNode(thisRefEqVal)) {
            this.infoFlowSummary.addNode(thisRefEqVal);
         }
      }

      Iterator it = this.sm.getDeclaringClass().getFields().iterator();

      while(true) {
         SootField sf;
         do {
            if (!it.hasNext()) {
               SootClass superclass = this.sm.getDeclaringClass();
               if (superclass.hasSuperclass()) {
                  superclass = this.sm.getDeclaringClass().getSuperclass();
               }

               label87:
               for(; superclass.hasSuperclass(); superclass = superclass.getSuperclass()) {
                  Iterator var13 = superclass.getFields().iterator();

                  while(true) {
                     SootField scField;
                     do {
                        if (!var13.hasNext()) {
                           continue label87;
                        }

                        scField = (SootField)var13.next();
                     } while(!scField.isStatic() && this.sm.isStatic());

                     EquivalentValue fieldRefEqVal;
                     if (!this.sm.isStatic()) {
                        fieldRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(this.sm, scField, this.sm.retrieveActiveBody().getThisLocal());
                     } else {
                        fieldRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(this.sm, scField);
                     }

                     if (!this.infoFlowSummary.containsNode(fieldRefEqVal)) {
                        this.infoFlowSummary.addNode(fieldRefEqVal);
                     }
                  }
               }

               if (!this.sm.isStatic()) {
                  thisRefEqVal = InfoFlowAnalysis.getNodeForThisRef(this.sm);
                  if (!this.infoFlowSummary.containsNode(thisRefEqVal)) {
                     this.infoFlowSummary.addNode(thisRefEqVal);
                  }
               }

               EquivalentValue returnRefEqVal = new CachedEquivalentValue(this.returnRef);
               if (this.returnRef.getType() != VoidType.v() && !this.infoFlowSummary.containsNode(returnRefEqVal)) {
                  this.infoFlowSummary.addNode(returnRefEqVal);
               }

               Date start = new Date();
               int counterSoFar = counter;
               if (this.printMessages) {
                  logger.debug("STARTING SMART ANALYSIS FOR " + g.getBody().getMethod() + " -----");
               }

               this.generateAbbreviatedInfoFlowGraph();
               this.generateInfoFlowSummary();
               if (this.printMessages) {
                  long longTime = (new Date()).getTime() - start.getTime();
                  float time = (float)longTime / 1000.0F;
                  logger.debug("ENDING   SMART ANALYSIS FOR " + g.getBody().getMethod() + " ----- " + (counter - counterSoFar + 1) + " analyses took: " + time + "s");
                  logger.debug("  AbbreviatedDataFlowGraph:");
                  InfoFlowAnalysis.printInfoFlowSummary(this.abbreviatedInfoFlowGraph);
                  logger.debug("  DataFlowSummary:");
                  InfoFlowAnalysis.printInfoFlowSummary(this.infoFlowSummary);
               }

               return;
            }

            sf = (SootField)it.next();
         } while(!sf.isStatic() && this.sm.isStatic());

         EquivalentValue fieldRefEqVal;
         if (!this.sm.isStatic()) {
            fieldRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(this.sm, sf, this.sm.retrieveActiveBody().getThisLocal());
         } else {
            fieldRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(this.sm, sf);
         }

         if (!this.infoFlowSummary.containsNode(fieldRefEqVal)) {
            this.infoFlowSummary.addNode(fieldRefEqVal);
         }
      }
   }

   public void generateAbbreviatedInfoFlowGraph() {
      Iterator stmtIt = this.graph.iterator();

      while(stmtIt.hasNext()) {
         Stmt s = (Stmt)stmtIt.next();
         this.addFlowToCdfg(s);
      }

   }

   public void generateInfoFlowSummary() {
      Iterator nodeIt = this.infoFlowSummary.iterator();

      while(nodeIt.hasNext()) {
         EquivalentValue node = (EquivalentValue)nodeIt.next();
         List<EquivalentValue> sources = this.sourcesOf(node);
         Iterator sourcesIt = sources.iterator();

         while(sourcesIt.hasNext()) {
            EquivalentValue source = (EquivalentValue)sourcesIt.next();
            if (source.getValue() instanceof Ref) {
               this.infoFlowSummary.addEdge(source, node);
            }
         }
      }

   }

   public List<EquivalentValue> sourcesOf(EquivalentValue node) {
      return this.sourcesOf(node, new HashSet(), new HashSet());
   }

   private List<EquivalentValue> sourcesOf(EquivalentValue node, Set<EquivalentValue> visitedSources, Set<EquivalentValue> visitedSinks) {
      visitedSources.add(node);
      List<EquivalentValue> ret = new LinkedList();
      if (!this.abbreviatedInfoFlowGraph.containsNode(node)) {
         return ret;
      } else {
         Set<EquivalentValue> preds = this.abbreviatedInfoFlowGraph.getPredsOfAsSet(node);
         Iterator predsIt = preds.iterator();

         while(predsIt.hasNext()) {
            EquivalentValue pred = (EquivalentValue)predsIt.next();
            if (!visitedSources.contains(pred)) {
               ret.add(pred);
               ret.addAll(this.sourcesOf(pred, visitedSources, visitedSinks));
            }
         }

         List<EquivalentValue> sinks = this.sinksOf(node, visitedSources, visitedSinks);
         Iterator sinksIt = sinks.iterator();

         while(sinksIt.hasNext()) {
            EquivalentValue sink = (EquivalentValue)sinksIt.next();
            if (!visitedSources.contains(sink)) {
               EquivalentValue flowsToSourcesOf = new CachedEquivalentValue(new AbstractDataSource(sink.getValue()));
               if (this.abbreviatedInfoFlowGraph.getPredsOfAsSet(sink).contains(flowsToSourcesOf)) {
                  ret.addAll(this.sourcesOf(flowsToSourcesOf, visitedSources, visitedSinks));
               }
            }
         }

         return ret;
      }
   }

   public List<EquivalentValue> sinksOf(EquivalentValue node) {
      return this.sinksOf(node, new HashSet(), new HashSet());
   }

   private List<EquivalentValue> sinksOf(EquivalentValue node, Set<EquivalentValue> visitedSources, Set<EquivalentValue> visitedSinks) {
      List<EquivalentValue> ret = new LinkedList();
      visitedSinks.add(node);
      if (!this.abbreviatedInfoFlowGraph.containsNode(node)) {
         return ret;
      } else {
         Set<EquivalentValue> succs = this.abbreviatedInfoFlowGraph.getSuccsOfAsSet(node);
         Iterator succsIt = succs.iterator();

         EquivalentValue succ;
         while(succsIt.hasNext()) {
            succ = (EquivalentValue)succsIt.next();
            if (!visitedSinks.contains(succ)) {
               ret.add(succ);
               ret.addAll(this.sinksOf(succ, visitedSources, visitedSinks));
            }
         }

         succsIt = succs.iterator();

         while(succsIt.hasNext()) {
            succ = (EquivalentValue)succsIt.next();
            if (succ.getValue() instanceof AbstractDataSource) {
               Set vHolder = this.abbreviatedInfoFlowGraph.getSuccsOfAsSet(succ);
               EquivalentValue v = (EquivalentValue)vHolder.iterator().next();
               if (!visitedSinks.contains(v)) {
                  ret.addAll(this.sourcesOf(v, visitedSinks, visitedSinks));
               }
            }
         }

         return ret;
      }
   }

   public HashMutableDirectedGraph<EquivalentValue> getMethodInfoFlowSummary() {
      return this.infoFlowSummary;
   }

   public HashMutableDirectedGraph<EquivalentValue> getMethodAbbreviatedInfoFlowGraph() {
      return this.abbreviatedInfoFlowGraph;
   }

   protected boolean isNonRefType(Type type) {
      return !(type instanceof RefLikeType);
   }

   protected boolean ignoreThisDataType(Type type) {
      return this.refOnly && this.isNonRefType(type);
   }

   protected void handleFlowsToValue(Value sink, Value source) {
      Object sinkEqVal;
      InstanceFieldRef ifr;
      if (sink instanceof InstanceFieldRef) {
         ifr = (InstanceFieldRef)sink;
         sinkEqVal = InfoFlowAnalysis.getNodeForFieldRef(this.sm, ifr.getField(), (Local)ifr.getBase());
      } else {
         sinkEqVal = new CachedEquivalentValue(sink);
      }

      Object sourceEqVal;
      if (source instanceof InstanceFieldRef) {
         ifr = (InstanceFieldRef)source;
         sourceEqVal = InfoFlowAnalysis.getNodeForFieldRef(this.sm, ifr.getField(), (Local)ifr.getBase());
      } else {
         sourceEqVal = new CachedEquivalentValue(source);
      }

      if (source instanceof Ref && !this.infoFlowSummary.containsNode(sourceEqVal)) {
         this.infoFlowSummary.addNode(sourceEqVal);
      }

      if (sink instanceof Ref && !this.infoFlowSummary.containsNode(sinkEqVal)) {
         this.infoFlowSummary.addNode(sinkEqVal);
      }

      if (!this.abbreviatedInfoFlowGraph.containsNode(sinkEqVal)) {
         this.abbreviatedInfoFlowGraph.addNode(sinkEqVal);
      }

      if (!this.abbreviatedInfoFlowGraph.containsNode(sourceEqVal)) {
         this.abbreviatedInfoFlowGraph.addNode(sourceEqVal);
      }

      this.abbreviatedInfoFlowGraph.addEdge(sourceEqVal, sinkEqVal);
   }

   protected void handleFlowsToDataStructure(Value base, Value source) {
      EquivalentValue sourcesOfBaseEqVal = new CachedEquivalentValue(new AbstractDataSource(base));
      EquivalentValue baseEqVal = new CachedEquivalentValue(base);
      Object sourceEqVal;
      if (source instanceof InstanceFieldRef) {
         InstanceFieldRef ifr = (InstanceFieldRef)source;
         sourceEqVal = InfoFlowAnalysis.getNodeForFieldRef(this.sm, ifr.getField(), (Local)ifr.getBase());
      } else {
         sourceEqVal = new CachedEquivalentValue(source);
      }

      if (source instanceof Ref && !this.infoFlowSummary.containsNode(sourceEqVal)) {
         this.infoFlowSummary.addNode(sourceEqVal);
      }

      if (!this.abbreviatedInfoFlowGraph.containsNode(baseEqVal)) {
         this.abbreviatedInfoFlowGraph.addNode(baseEqVal);
      }

      if (!this.abbreviatedInfoFlowGraph.containsNode(sourceEqVal)) {
         this.abbreviatedInfoFlowGraph.addNode(sourceEqVal);
      }

      if (!this.abbreviatedInfoFlowGraph.containsNode(sourcesOfBaseEqVal)) {
         this.abbreviatedInfoFlowGraph.addNode(sourcesOfBaseEqVal);
      }

      this.abbreviatedInfoFlowGraph.addEdge(sourceEqVal, sourcesOfBaseEqVal);
      this.abbreviatedInfoFlowGraph.addEdge(sourcesOfBaseEqVal, baseEqVal);
   }

   protected void handleInnerField(Value innerFieldRef) {
   }

   protected List<Value> handleInvokeExpr(InvokeExpr ie, Stmt is) {
      HashMutableDirectedGraph<EquivalentValue> dataFlowSummary = this.dfa.getInvokeInfoFlowSummary(ie, is, this.sm);
      List<Value> returnValueSources = new ArrayList();
      Iterator nodeIt = dataFlowSummary.getNodes().iterator();

      label152:
      while(nodeIt.hasNext()) {
         EquivalentValue nodeEqVal = (EquivalentValue)nodeIt.next();
         if (!(nodeEqVal.getValue() instanceof Ref)) {
            throw new RuntimeException("Illegal node type in data flow summary:" + nodeEqVal.getValue() + " should be an object of type Ref.");
         }

         Ref node = (Ref)nodeEqVal.getValue();
         List<Value> sources = new ArrayList();
         if (node instanceof ParameterRef) {
            ParameterRef param = (ParameterRef)node;
            if (param.getIndex() == -1) {
               continue;
            }

            sources.add(ie.getArg(param.getIndex()));
         } else if (node instanceof StaticFieldRef) {
            sources.add(node);
         } else {
            InstanceInvokeExpr iie;
            if (node instanceof InstanceFieldRef && ie instanceof InstanceInvokeExpr) {
               iie = (InstanceInvokeExpr)ie;
               if (iie.getBase() == this.thisLocal) {
                  sources.add(node);
               } else if (this.includeInnerFields) {
                  InstanceFieldRef ifr = (InstanceFieldRef)node;
                  if (!(ifr.getBase() instanceof FakeJimpleLocal)) {
                     sources.add(ifr.getBase());
                  }

                  sources.add(node);
               } else {
                  sources.add(iie.getBase());
               }
            } else if (node instanceof InstanceFieldRef && this.includeInnerFields) {
               InstanceFieldRef ifr = (InstanceFieldRef)node;
               if (!(ifr.getBase() instanceof FakeJimpleLocal)) {
                  sources.add(ifr.getBase());
               }

               sources.add(node);
            } else {
               if (!(node instanceof ThisRef) || !(ie instanceof InstanceInvokeExpr)) {
                  throw new RuntimeException("Unknown Node Type in Data Flow Graph: node " + node + " in InvokeExpr " + ie);
               }

               iie = (InstanceInvokeExpr)ie;
               sources.add(iie.getBase());
            }
         }

         Iterator sinksIt = dataFlowSummary.getSuccsOfAsSet(nodeEqVal).iterator();

         while(true) {
            while(true) {
               while(true) {
                  if (!sinksIt.hasNext()) {
                     continue label152;
                  }

                  EquivalentValue sinkEqVal = (EquivalentValue)sinksIt.next();
                  Ref sink = (Ref)sinkEqVal.getValue();
                  Value source;
                  Iterator sourcesIt;
                  if (sink instanceof ParameterRef) {
                     ParameterRef param = (ParameterRef)sink;
                     if (param.getIndex() == -1) {
                        returnValueSources.addAll(sources);
                     } else {
                        sourcesIt = sources.iterator();

                        while(sourcesIt.hasNext()) {
                           source = (Value)sourcesIt.next();
                           this.handleFlowsToDataStructure(ie.getArg(param.getIndex()), source);
                        }
                     }
                  } else {
                     Iterator sourcesIt;
                     Value source;
                     if (sink instanceof StaticFieldRef) {
                        sourcesIt = sources.iterator();

                        while(sourcesIt.hasNext()) {
                           source = (Value)sourcesIt.next();
                           this.handleFlowsToValue(sink, source);
                        }
                     } else if (sink instanceof InstanceFieldRef && ie instanceof InstanceInvokeExpr) {
                        InstanceInvokeExpr iie = (InstanceInvokeExpr)ie;
                        if (iie.getBase() == this.thisLocal) {
                           sourcesIt = sources.iterator();

                           while(sourcesIt.hasNext()) {
                              source = (Value)sourcesIt.next();
                              this.handleFlowsToValue(sink, source);
                           }
                        } else if (this.includeInnerFields) {
                           sourcesIt = sources.iterator();

                           while(sourcesIt.hasNext()) {
                              source = (Value)sourcesIt.next();
                              this.handleFlowsToValue(sink, source);
                              this.handleInnerField(sink);
                           }
                        } else {
                           sourcesIt = sources.iterator();

                           while(sourcesIt.hasNext()) {
                              source = (Value)sourcesIt.next();
                              this.handleFlowsToDataStructure(iie.getBase(), source);
                           }
                        }
                     } else if (sink instanceof InstanceFieldRef && this.includeInnerFields) {
                        sourcesIt = sources.iterator();

                        while(sourcesIt.hasNext()) {
                           source = (Value)sourcesIt.next();
                           this.handleFlowsToValue(sink, source);
                           this.handleInnerField(sink);
                        }
                     }
                  }
               }
            }
         }
      }

      return returnValueSources;
   }

   protected void addFlowToCdfg(Stmt stmt) {
      if (stmt instanceof IdentityStmt) {
         IdentityStmt is = (IdentityStmt)stmt;
         IdentityRef ir = (IdentityRef)is.getRightOp();
         if (!(ir instanceof JCaughtExceptionRef)) {
            if (ir instanceof ParameterRef) {
               if (!this.ignoreThisDataType(ir.getType())) {
                  this.handleFlowsToValue(is.getLeftOp(), ir);
               }
            } else if (ir instanceof ThisRef && !this.ignoreThisDataType(ir.getType())) {
               this.handleFlowsToValue(is.getLeftOp(), ir);
            }
         }
      } else {
         Value lv;
         if (stmt instanceof ReturnStmt) {
            ReturnStmt rs = (ReturnStmt)stmt;
            lv = rs.getOp();
            if (!(lv instanceof Constant) && lv instanceof Local && !this.ignoreThisDataType(lv.getType())) {
               this.handleFlowsToValue(this.returnRef, lv);
            }
         } else if (stmt instanceof AssignStmt) {
            AssignStmt as = (AssignStmt)stmt;
            lv = as.getLeftOp();
            Value rv = as.getRightOp();
            Value sink = null;
            boolean flowsToDataStructure = false;
            if (lv instanceof Local) {
               sink = lv;
            } else if (lv instanceof ArrayRef) {
               ArrayRef ar = (ArrayRef)lv;
               sink = ar.getBase();
               flowsToDataStructure = true;
            } else if (lv instanceof StaticFieldRef) {
               sink = lv;
            } else if (lv instanceof InstanceFieldRef) {
               InstanceFieldRef ifr = (InstanceFieldRef)lv;
               if (ifr.getBase() == this.thisLocal) {
                  sink = lv;
               } else if (this.includeInnerFields) {
                  sink = lv;
                  this.handleInnerField(lv);
               } else {
                  sink = ifr.getBase();
                  flowsToDataStructure = true;
               }
            }

            List sources = new ArrayList();
            boolean interestingFlow = true;
            if (rv instanceof Local) {
               sources.add(rv);
               interestingFlow = !this.ignoreThisDataType(rv.getType());
            } else if (rv instanceof Constant) {
               sources.add(rv);
               interestingFlow = !this.ignoreThisDataType(rv.getType());
            } else if (rv instanceof ArrayRef) {
               ArrayRef ar = (ArrayRef)rv;
               sources.add(ar.getBase());
               interestingFlow = !this.ignoreThisDataType(ar.getType());
            } else if (rv instanceof StaticFieldRef) {
               sources.add(rv);
               interestingFlow = !this.ignoreThisDataType(rv.getType());
            } else if (rv instanceof InstanceFieldRef) {
               InstanceFieldRef ifr = (InstanceFieldRef)rv;
               if (ifr.getBase() == this.thisLocal) {
                  sources.add(rv);
                  interestingFlow = !this.ignoreThisDataType(rv.getType());
               } else if (this.includeInnerFields) {
                  sources.add(ifr.getBase());
                  sources.add(rv);
                  this.handleInnerField(rv);
                  interestingFlow = !this.ignoreThisDataType(rv.getType());
               } else {
                  sources.add(ifr.getBase());
                  interestingFlow = !this.ignoreThisDataType(ifr.getType());
               }
            } else if (rv instanceof AnyNewExpr) {
               sources.add(rv);
               interestingFlow = !this.ignoreThisDataType(rv.getType());
            } else if (rv instanceof BinopExpr) {
               BinopExpr be = (BinopExpr)rv;
               sources.add(be.getOp1());
               sources.add(be.getOp2());
               interestingFlow = !this.ignoreThisDataType(be.getType());
            } else if (rv instanceof CastExpr) {
               CastExpr ce = (CastExpr)rv;
               sources.add(ce.getOp());
               interestingFlow = !this.ignoreThisDataType(ce.getType());
            } else if (rv instanceof InstanceOfExpr) {
               InstanceOfExpr ioe = (InstanceOfExpr)rv;
               sources.add(ioe.getOp());
               interestingFlow = !this.ignoreThisDataType(ioe.getType());
            } else if (rv instanceof UnopExpr) {
               UnopExpr ue = (UnopExpr)rv;
               sources.add(ue.getOp());
               interestingFlow = !this.ignoreThisDataType(ue.getType());
            } else if (rv instanceof InvokeExpr) {
               InvokeExpr ie = (InvokeExpr)rv;
               sources.addAll(this.handleInvokeExpr(ie, as));
               interestingFlow = !this.ignoreThisDataType(ie.getType());
            }

            if (interestingFlow) {
               Value source;
               Iterator sourcesIt;
               if (flowsToDataStructure) {
                  sourcesIt = sources.iterator();

                  while(sourcesIt.hasNext()) {
                     source = (Value)sourcesIt.next();
                     this.handleFlowsToDataStructure(sink, source);
                  }
               } else {
                  sourcesIt = sources.iterator();

                  while(sourcesIt.hasNext()) {
                     source = (Value)sourcesIt.next();
                     this.handleFlowsToValue(sink, source);
                  }
               }
            }
         } else if (stmt.containsInvokeExpr()) {
            this.handleInvokeExpr(stmt.getInvokeExpr(), stmt);
         }
      }

   }

   public Value getThisLocal() {
      return this.thisLocal;
   }
}
