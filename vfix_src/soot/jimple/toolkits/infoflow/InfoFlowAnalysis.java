package soot.jimple.toolkits.infoflow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.EquivalentValue;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Unit;
import soot.Value;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.HashMutableDirectedGraph;
import soot.toolkits.graph.MutableDirectedGraph;
import soot.util.dot.DotGraph;

public class InfoFlowAnalysis {
   private static final Logger logger = LoggerFactory.getLogger(InfoFlowAnalysis.class);
   boolean includePrimitiveInfoFlow;
   boolean includeInnerFields;
   boolean printDebug;
   Map<SootClass, ClassInfoFlowAnalysis> classToClassInfoFlowAnalysis;
   static int nodecount = 0;

   public InfoFlowAnalysis(boolean includePrimitiveDataFlow, boolean includeInnerFields) {
      this(includePrimitiveDataFlow, includeInnerFields, false);
   }

   public InfoFlowAnalysis(boolean includePrimitiveDataFlow, boolean includeInnerFields, boolean printDebug) {
      this.includePrimitiveInfoFlow = includePrimitiveDataFlow;
      this.includeInnerFields = includeInnerFields;
      this.printDebug = printDebug;
      this.classToClassInfoFlowAnalysis = new HashMap();
   }

   public boolean includesPrimitiveInfoFlow() {
      return this.includePrimitiveInfoFlow;
   }

   public boolean includesInnerFields() {
      return this.includeInnerFields;
   }

   public boolean printDebug() {
      return this.printDebug;
   }

   private ClassInfoFlowAnalysis getClassInfoFlowAnalysis(SootClass sc) {
      if (!this.classToClassInfoFlowAnalysis.containsKey(sc)) {
         ClassInfoFlowAnalysis cdfa = new ClassInfoFlowAnalysis(sc, this);
         this.classToClassInfoFlowAnalysis.put(sc, cdfa);
      }

      return (ClassInfoFlowAnalysis)this.classToClassInfoFlowAnalysis.get(sc);
   }

   public SmartMethodInfoFlowAnalysis getMethodInfoFlowAnalysis(SootMethod sm) {
      ClassInfoFlowAnalysis cdfa = this.getClassInfoFlowAnalysis(sm.getDeclaringClass());
      return cdfa.getMethodInfoFlowAnalysis(sm);
   }

   public HashMutableDirectedGraph<EquivalentValue> getMethodInfoFlowSummary(SootMethod sm) {
      return this.getMethodInfoFlowSummary(sm, true);
   }

   public HashMutableDirectedGraph<EquivalentValue> getMethodInfoFlowSummary(SootMethod sm, boolean doFullAnalysis) {
      ClassInfoFlowAnalysis cdfa = this.getClassInfoFlowAnalysis(sm.getDeclaringClass());
      return cdfa.getMethodInfoFlowSummary(sm, doFullAnalysis);
   }

   public static EquivalentValue getNodeForFieldRef(SootMethod sm, SootField sf) {
      return getNodeForFieldRef(sm, sf, (Local)null);
   }

   public static EquivalentValue getNodeForFieldRef(SootMethod sm, SootField sf, Local realLocal) {
      if (sf.isStatic()) {
         return new CachedEquivalentValue(Jimple.v().newStaticFieldRef(sf.makeRef()));
      } else {
         FakeJimpleLocal fakethis;
         if (sm.isConcrete() && !sm.isStatic() && sm.getDeclaringClass() == sf.getDeclaringClass() && realLocal == null) {
            fakethis = new FakeJimpleLocal("fakethis", sf.getDeclaringClass().getType(), sm.retrieveActiveBody().getThisLocal());
            return new CachedEquivalentValue(Jimple.v().newInstanceFieldRef(fakethis, sf.makeRef()));
         } else {
            fakethis = new FakeJimpleLocal("fakethis", sf.getDeclaringClass().getType(), realLocal);
            return new CachedEquivalentValue(Jimple.v().newInstanceFieldRef(fakethis, sf.makeRef()));
         }
      }
   }

   public static EquivalentValue getNodeForParameterRef(SootMethod sm, int i) {
      return new CachedEquivalentValue(new ParameterRef(sm.getParameterType(i), i));
   }

   public static EquivalentValue getNodeForReturnRef(SootMethod sm) {
      return new CachedEquivalentValue(new ParameterRef(sm.getReturnType(), -1));
   }

   public static EquivalentValue getNodeForThisRef(SootMethod sm) {
      return new CachedEquivalentValue(new ThisRef(sm.getDeclaringClass().getType()));
   }

   protected HashMutableDirectedGraph<EquivalentValue> getInvokeInfoFlowSummary(InvokeExpr ie, Stmt is, SootMethod context) {
      HashMutableDirectedGraph<EquivalentValue> ret = null;
      SootMethodRef methodRef = ie.getMethodRef();
      String subSig = methodRef.resolve().getSubSignature();
      CallGraph cg = Scene.v().getCallGraph();
      Iterator edges = cg.edgesOutOf((Unit)is);

      while(true) {
         while(true) {
            SootMethod target;
            do {
               if (!edges.hasNext()) {
                  return ret;
               }

               Edge e = (Edge)edges.next();
               target = e.getTgt().method();
            } while(!target.getSubSignature().equals(subSig));

            HashMutableDirectedGraph<EquivalentValue> ifs = this.getMethodInfoFlowSummary(target, context.getDeclaringClass().isApplicationClass());
            if (ret == null) {
               ret = ifs;
            } else {
               Iterator var12 = ifs.getNodes().iterator();

               while(var12.hasNext()) {
                  EquivalentValue node = (EquivalentValue)var12.next();
                  if (!ret.containsNode(node)) {
                     ret.addNode(node);
                  }

                  Iterator var14 = ifs.getSuccsOf(node).iterator();

                  while(var14.hasNext()) {
                     EquivalentValue succ = (EquivalentValue)var14.next();
                     ret.addEdge(node, succ);
                  }
               }
            }
         }
      }
   }

   protected MutableDirectedGraph<EquivalentValue> getInvokeAbbreviatedInfoFlowGraph(InvokeExpr ie, SootMethod context) {
      SootMethodRef methodRef = ie.getMethodRef();
      return this.getMethodInfoFlowAnalysis(methodRef.resolve()).getMethodAbbreviatedInfoFlowGraph();
   }

   public static void printInfoFlowSummary(DirectedGraph<EquivalentValue> g) {
      if (g.size() > 0) {
         logger.debug("     --> ");
      }

      Iterator var1 = g.iterator();

      while(true) {
         EquivalentValue node;
         List sources;
         do {
            if (!var1.hasNext()) {
               return;
            }

            node = (EquivalentValue)var1.next();
            sources = g.getPredsOf(node);
         } while(sources.isEmpty());

         logger.debug("    [ ");
         int sourcesnamelength = 0;
         int lastnamelength = 0;
         int idx = 0;
         Iterator var7 = sources.iterator();

         while(var7.hasNext()) {
            EquivalentValue t = (EquivalentValue)var7.next();
            Value v = t.getValue();
            if (v instanceof FieldRef) {
               FieldRef fr = (FieldRef)v;
               String name = fr.getFieldRef().name();
               lastnamelength = name.length();
               if (lastnamelength > sourcesnamelength) {
                  sourcesnamelength = lastnamelength;
               }

               logger.debug("" + name);
            } else if (v instanceof ParameterRef) {
               ParameterRef pr = (ParameterRef)v;
               lastnamelength = 11;
               if (lastnamelength > sourcesnamelength) {
                  sourcesnamelength = lastnamelength;
               }

               logger.debug("@parameter" + pr.getIndex());
            } else {
               String name = v.toString();
               lastnamelength = name.length();
               if (lastnamelength > sourcesnamelength) {
                  sourcesnamelength = lastnamelength;
               }

               logger.debug("" + name);
            }

            if (idx++ < sources.size()) {
               logger.debug("\n      ");
            }
         }

         for(int i = 0; i < sourcesnamelength - lastnamelength; ++i) {
            logger.debug(" ");
         }

         logger.debug(" ] --> " + node.toString());
      }
   }

   public static void printGraphToDotFile(String filename, DirectedGraph<EquivalentValue> graph, String graphname, boolean onePage) {
      nodecount = 0;
      DotGraph canvas = new DotGraph(filename);
      if (!onePage) {
         canvas.setPageSize(8.5D, 11.0D);
      }

      canvas.setNodeShape("box");
      canvas.setGraphLabel(graphname);
      Iterator var5 = graph.iterator();

      while(var5.hasNext()) {
         EquivalentValue node = (EquivalentValue)var5.next();
         canvas.drawNode(getNodeName(node));
         canvas.getNode(getNodeName(node)).setLabel(getNodeLabel(node));
         Iterator var7 = graph.getSuccsOf(node).iterator();

         while(var7.hasNext()) {
            EquivalentValue s = (EquivalentValue)var7.next();
            canvas.drawNode(getNodeName(s));
            canvas.getNode(getNodeName(s)).setLabel(getNodeLabel(s));
            canvas.drawEdge(getNodeName(node), getNodeName(s));
         }
      }

      canvas.plot(filename + ".dot");
   }

   public static String getNodeName(Object o) {
      return getNodeLabel(o);
   }

   public static String getNodeLabel(Object o) {
      Value node = ((EquivalentValue)o).getValue();
      if (node instanceof FieldRef) {
         FieldRef fr = (FieldRef)node;
         return fr.getField().getDeclaringClass().getShortName() + "." + fr.getFieldRef().name();
      } else {
         return node.toString();
      }
   }
}
