package soot.jimple.toolkits.infoflow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.EquivalentValue;
import soot.Local;
import soot.RefLikeType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
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
import soot.toolkits.graph.MemoryEfficientGraph;
import soot.toolkits.graph.MutableDirectedGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.toolkits.scalar.Pair;

public class SimpleMethodInfoFlowAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<Pair<EquivalentValue, EquivalentValue>>> {
   private static final Logger logger = LoggerFactory.getLogger(SimpleMethodInfoFlowAnalysis.class);
   SootMethod sm;
   Value thisLocal;
   InfoFlowAnalysis dfa;
   boolean refOnly;
   MutableDirectedGraph<EquivalentValue> infoFlowGraph;
   Ref returnRef;
   FlowSet<Pair<EquivalentValue, EquivalentValue>> entrySet;
   FlowSet<Pair<EquivalentValue, EquivalentValue>> emptySet;
   boolean printMessages;
   public static int counter = 0;

   public SimpleMethodInfoFlowAnalysis(UnitGraph g, InfoFlowAnalysis dfa, boolean ignoreNonRefTypeFlow) {
      this(g, dfa, ignoreNonRefTypeFlow, true);
      ++counter;

      EquivalentValue thisRefEqVal;
      for(int i = 0; i < this.sm.getParameterCount(); ++i) {
         thisRefEqVal = InfoFlowAnalysis.getNodeForParameterRef(this.sm, i);
         if (!this.infoFlowGraph.containsNode(thisRefEqVal)) {
            this.infoFlowGraph.addNode(thisRefEqVal);
         }
      }

      Iterator var8 = this.sm.getDeclaringClass().getFields().iterator();

      while(var8.hasNext()) {
         SootField sf = (SootField)var8.next();
         EquivalentValue fieldRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(this.sm, sf);
         if (!this.infoFlowGraph.containsNode(fieldRefEqVal)) {
            this.infoFlowGraph.addNode(fieldRefEqVal);
         }
      }

      SootClass superclass = this.sm.getDeclaringClass();
      if (superclass.hasSuperclass()) {
         superclass = this.sm.getDeclaringClass().getSuperclass();
      }

      while(superclass.hasSuperclass()) {
         Iterator var11 = superclass.getFields().iterator();

         while(var11.hasNext()) {
            SootField scField = (SootField)var11.next();
            EquivalentValue fieldRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(this.sm, scField);
            if (!this.infoFlowGraph.containsNode(fieldRefEqVal)) {
               this.infoFlowGraph.addNode(fieldRefEqVal);
            }
         }

         superclass = superclass.getSuperclass();
      }

      thisRefEqVal = InfoFlowAnalysis.getNodeForThisRef(this.sm);
      if (!this.infoFlowGraph.containsNode(thisRefEqVal)) {
         this.infoFlowGraph.addNode(thisRefEqVal);
      }

      EquivalentValue returnRefEqVal = new CachedEquivalentValue(this.returnRef);
      if (!this.infoFlowGraph.containsNode(returnRefEqVal)) {
         this.infoFlowGraph.addNode(returnRefEqVal);
      }

      if (this.printMessages) {
         logger.debug("STARTING ANALYSIS FOR " + g.getBody().getMethod() + " -----");
      }

      this.doFlowInsensitiveAnalysis();
      if (this.printMessages) {
         logger.debug("ENDING   ANALYSIS FOR " + g.getBody().getMethod() + " -----");
      }

   }

   protected SimpleMethodInfoFlowAnalysis(UnitGraph g, InfoFlowAnalysis dfa, boolean ignoreNonRefTypeFlow, boolean dummyDontRunAnalysisYet) {
      super(g);
      this.sm = g.getBody().getMethod();
      if (this.sm.isStatic()) {
         this.thisLocal = null;
      } else {
         this.thisLocal = g.getBody().getThisLocal();
      }

      this.dfa = dfa;
      this.refOnly = ignoreNonRefTypeFlow;
      this.infoFlowGraph = new MemoryEfficientGraph();
      this.returnRef = new ParameterRef(g.getBody().getMethod().getReturnType(), -1);
      this.entrySet = new ArraySparseSet();
      this.emptySet = new ArraySparseSet();
      this.printMessages = false;
   }

   public void doFlowInsensitiveAnalysis() {
      FlowSet<Pair<EquivalentValue, EquivalentValue>> fs = this.newInitialFlow();
      boolean flowSetChanged = true;

      while(flowSetChanged) {
         int sizebefore = fs.size();
         Iterator unitIt = this.graph.iterator();

         while(unitIt.hasNext()) {
            Unit u = (Unit)unitIt.next();
            this.flowThrough(fs, u, fs);
         }

         if (fs.size() > sizebefore) {
            flowSetChanged = true;
         } else {
            flowSetChanged = false;
         }
      }

   }

   public MutableDirectedGraph<EquivalentValue> getMethodInfoFlowSummary() {
      return this.infoFlowGraph;
   }

   protected void merge(FlowSet<Pair<EquivalentValue, EquivalentValue>> in1, FlowSet<Pair<EquivalentValue, EquivalentValue>> in2, FlowSet<Pair<EquivalentValue, EquivalentValue>> out) {
      in1.union(in2, out);
   }

   protected boolean isNonRefType(Type type) {
      return !(type instanceof RefLikeType);
   }

   protected boolean ignoreThisDataType(Type type) {
      return this.refOnly && this.isNonRefType(type);
   }

   public boolean isInterestingSource(Value source) {
      return source instanceof Ref;
   }

   public boolean isTrackableSource(Value source) {
      return this.isInterestingSource(source) || source instanceof Ref;
   }

   public boolean isInterestingSink(Value sink) {
      return sink instanceof Ref;
   }

   public boolean isTrackableSink(Value sink) {
      return this.isInterestingSink(sink) || sink instanceof Ref || sink instanceof Local;
   }

   private ArrayList<Value> getDirectSources(Value v, FlowSet<Pair<EquivalentValue, EquivalentValue>> fs) {
      ArrayList<Value> ret = new ArrayList();
      EquivalentValue vEqVal = new CachedEquivalentValue(v);
      Iterator fsIt = fs.iterator();

      while(fsIt.hasNext()) {
         Pair<EquivalentValue, EquivalentValue> pair = (Pair)fsIt.next();
         if (((EquivalentValue)pair.getO1()).equals(vEqVal)) {
            ret.add(((EquivalentValue)pair.getO2()).getValue());
         }
      }

      return ret;
   }

   protected void handleFlowsToValue(Value sink, Value initialSource, FlowSet<Pair<EquivalentValue, EquivalentValue>> fs) {
      if (this.isTrackableSink(sink)) {
         List<Value> sources = this.getDirectSources(initialSource, fs);
         if (this.isTrackableSource(initialSource)) {
            sources.add(initialSource);
         }

         Iterator sourcesIt = sources.iterator();

         while(sourcesIt.hasNext()) {
            Value source = (Value)sourcesIt.next();
            EquivalentValue sinkEqVal = new CachedEquivalentValue(sink);
            EquivalentValue sourceEqVal = new CachedEquivalentValue(source);
            if (!sinkEqVal.equals(sourceEqVal)) {
               Pair<EquivalentValue, EquivalentValue> pair = new Pair(sinkEqVal, sourceEqVal);
               if (!fs.contains(pair)) {
                  fs.add(pair);
                  if (this.isInterestingSource(source) && this.isInterestingSink(sink)) {
                     if (!this.infoFlowGraph.containsNode(sinkEqVal)) {
                        this.infoFlowGraph.addNode(sinkEqVal);
                     }

                     if (!this.infoFlowGraph.containsNode(sourceEqVal)) {
                        this.infoFlowGraph.addNode(sourceEqVal);
                     }

                     this.infoFlowGraph.addEdge(sourceEqVal, sinkEqVal);
                     if (this.printMessages) {
                        logger.debug("      Found " + source + " flows to " + sink);
                     }
                  }
               }
            }
         }

      }
   }

   protected void handleFlowsToDataStructure(Value base, Value initialSource, FlowSet<Pair<EquivalentValue, EquivalentValue>> fs) {
      List<Value> sinks = this.getDirectSources(base, fs);
      if (this.isTrackableSink(base)) {
         sinks.add(base);
      }

      List<Value> sources = this.getDirectSources(initialSource, fs);
      if (this.isTrackableSource(initialSource)) {
         sources.add(initialSource);
      }

      Iterator sourcesIt = sources.iterator();

      while(sourcesIt.hasNext()) {
         Value source = (Value)sourcesIt.next();
         EquivalentValue sourceEqVal = new CachedEquivalentValue(source);
         Iterator sinksIt = sinks.iterator();

         while(sinksIt.hasNext()) {
            Value sink = (Value)sinksIt.next();
            if (this.isTrackableSink(sink)) {
               EquivalentValue sinkEqVal = new CachedEquivalentValue(sink);
               if (!sinkEqVal.equals(sourceEqVal)) {
                  Pair<EquivalentValue, EquivalentValue> pair = new Pair(sinkEqVal, sourceEqVal);
                  if (!fs.contains(pair)) {
                     fs.add(pair);
                     if (this.isInterestingSource(source) && this.isInterestingSink(sink)) {
                        if (!this.infoFlowGraph.containsNode(sinkEqVal)) {
                           this.infoFlowGraph.addNode(sinkEqVal);
                        }

                        if (!this.infoFlowGraph.containsNode(sourceEqVal)) {
                           this.infoFlowGraph.addNode(sourceEqVal);
                        }

                        this.infoFlowGraph.addEdge(sourceEqVal, sinkEqVal);
                        if (this.printMessages) {
                           logger.debug("      Found " + source + " flows to " + sink);
                        }
                     }
                  }
               }
            }
         }
      }

   }

   protected List<Value> handleInvokeExpr(InvokeExpr ie, Stmt is, FlowSet<Pair<EquivalentValue, EquivalentValue>> fs) {
      MutableDirectedGraph<EquivalentValue> dataFlowGraph = this.dfa.getInvokeInfoFlowSummary(ie, is, this.sm);
      List<Value> returnValueSources = new ArrayList();
      Iterator nodeIt = dataFlowGraph.getNodes().iterator();

      while(true) {
         EquivalentValue nodeEqVal;
         Object source;
         while(true) {
            if (!nodeIt.hasNext()) {
               return returnValueSources;
            }

            nodeEqVal = (EquivalentValue)nodeIt.next();
            if (!(nodeEqVal.getValue() instanceof Ref)) {
               throw new RuntimeException("Illegal node type in data flow graph:" + nodeEqVal.getValue() + " should be an object of type Ref.");
            }

            Ref node = (Ref)nodeEqVal.getValue();
            source = null;
            if (node instanceof ParameterRef) {
               ParameterRef param = (ParameterRef)node;
               if (param.getIndex() == -1) {
                  continue;
               }

               source = ie.getArg(param.getIndex());
               break;
            }

            if (node instanceof StaticFieldRef) {
               source = node;
            } else if (ie instanceof InstanceInvokeExpr && node instanceof InstanceFieldRef) {
               InstanceInvokeExpr iie = (InstanceInvokeExpr)ie;
               source = iie.getBase();
            }
            break;
         }

         Iterator sinksIt = dataFlowGraph.getSuccsOf(nodeEqVal).iterator();

         while(sinksIt.hasNext()) {
            EquivalentValue sinkEqVal = (EquivalentValue)sinksIt.next();
            Ref sink = (Ref)sinkEqVal.getValue();
            if (sink instanceof ParameterRef) {
               ParameterRef param = (ParameterRef)sink;
               if (param.getIndex() == -1) {
                  returnValueSources.add(source);
               } else {
                  this.handleFlowsToDataStructure(ie.getArg(param.getIndex()), (Value)source, fs);
               }
            } else if (sink instanceof StaticFieldRef) {
               this.handleFlowsToValue(sink, (Value)source, fs);
            } else if (ie instanceof InstanceInvokeExpr && sink instanceof InstanceFieldRef) {
               InstanceInvokeExpr iie = (InstanceInvokeExpr)ie;
               this.handleFlowsToDataStructure(iie.getBase(), (Value)source, fs);
            }
         }
      }
   }

   protected void flowThrough(FlowSet<Pair<EquivalentValue, EquivalentValue>> in, Unit unit, FlowSet<Pair<EquivalentValue, EquivalentValue>> out) {
      Stmt stmt = (Stmt)unit;
      if (in != out) {
         in.copy(out);
      }

      FlowSet<Pair<EquivalentValue, EquivalentValue>> changedFlow = out;
      if (stmt instanceof IdentityStmt) {
         IdentityStmt is = (IdentityStmt)stmt;
         IdentityRef ir = (IdentityRef)is.getRightOp();
         if (!(ir instanceof JCaughtExceptionRef)) {
            if (ir instanceof ParameterRef) {
               if (!this.ignoreThisDataType(ir.getType())) {
                  this.handleFlowsToValue(is.getLeftOp(), ir, out);
               }
            } else if (ir instanceof ThisRef && !this.ignoreThisDataType(ir.getType())) {
               this.handleFlowsToValue(is.getLeftOp(), ir, out);
            }
         }
      } else {
         Value lv;
         if (stmt instanceof ReturnStmt) {
            ReturnStmt rs = (ReturnStmt)stmt;
            lv = rs.getOp();
            if (!(lv instanceof Constant) && lv instanceof Local && !this.ignoreThisDataType(lv.getType())) {
               this.handleFlowsToValue(this.returnRef, lv, out);
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
               } else {
                  sink = ifr.getBase();
                  flowsToDataStructure = true;
               }
            }

            List<Value> sources = new ArrayList();
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
               sources.addAll(this.handleInvokeExpr(ie, as, out));
               interestingFlow = !this.ignoreThisDataType(ie.getType());
            }

            if (interestingFlow) {
               Value source;
               Iterator var26;
               if (flowsToDataStructure) {
                  var26 = sources.iterator();

                  while(var26.hasNext()) {
                     source = (Value)var26.next();
                     this.handleFlowsToDataStructure(sink, source, changedFlow);
                  }
               } else {
                  var26 = sources.iterator();

                  while(var26.hasNext()) {
                     source = (Value)var26.next();
                     this.handleFlowsToValue(sink, source, changedFlow);
                  }
               }
            }
         } else if (stmt.containsInvokeExpr()) {
            this.handleInvokeExpr(stmt.getInvokeExpr(), stmt, out);
         }
      }

   }

   protected void copy(FlowSet<Pair<EquivalentValue, EquivalentValue>> source, FlowSet<Pair<EquivalentValue, EquivalentValue>> dest) {
      source.copy(dest);
   }

   protected FlowSet<Pair<EquivalentValue, EquivalentValue>> entryInitialFlow() {
      return this.entrySet.clone();
   }

   protected FlowSet<Pair<EquivalentValue, EquivalentValue>> newInitialFlow() {
      return this.emptySet.clone();
   }

   public void addToEntryInitialFlow(Value source, Value sink) {
      EquivalentValue sinkEqVal = new CachedEquivalentValue(sink);
      EquivalentValue sourceEqVal = new CachedEquivalentValue(source);
      if (!sinkEqVal.equals(sourceEqVal)) {
         Pair<EquivalentValue, EquivalentValue> pair = new Pair(sinkEqVal, sourceEqVal);
         if (!this.entrySet.contains(pair)) {
            this.entrySet.add(pair);
         }

      }
   }

   public void addToNewInitialFlow(Value source, Value sink) {
      EquivalentValue sinkEqVal = new CachedEquivalentValue(sink);
      EquivalentValue sourceEqVal = new CachedEquivalentValue(source);
      if (!sinkEqVal.equals(sourceEqVal)) {
         Pair<EquivalentValue, EquivalentValue> pair = new Pair(sinkEqVal, sourceEqVal);
         if (!this.emptySet.contains(pair)) {
            this.emptySet.add(pair);
         }

      }
   }

   public Value getThisLocal() {
      return this.thisLocal;
   }
}
