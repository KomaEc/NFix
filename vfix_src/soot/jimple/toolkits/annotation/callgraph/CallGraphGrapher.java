package soot.jimple.toolkits.annotation.callgraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Body;
import soot.G;
import soot.MethodOrMethodContext;
import soot.MethodToContexts;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.VoidType;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.options.CGGOptions;
import soot.options.Options;
import soot.toolkits.graph.interaction.InteractionHandler;

public class CallGraphGrapher extends SceneTransformer {
   private static final Logger logger = LoggerFactory.getLogger(CallGraphGrapher.class);
   private MethodToContexts methodToContexts;
   private CallGraph cg;
   private boolean showLibMeths;
   private SootMethod nextMethod;

   public CallGraphGrapher(Singletons.Global g) {
   }

   public static CallGraphGrapher v() {
      return G.v().soot_jimple_toolkits_annotation_callgraph_CallGraphGrapher();
   }

   private ArrayList<MethInfo> getTgtMethods(SootMethod method, boolean recurse) {
      if (!method.hasActiveBody()) {
         return new ArrayList();
      } else {
         Body b = method.getActiveBody();
         ArrayList<MethInfo> list = new ArrayList();
         Iterator sIt = b.getUnits().iterator();

         while(sIt.hasNext()) {
            Stmt s = (Stmt)sIt.next();
            Iterator edges = this.cg.edgesOutOf((Unit)s);

            while(edges.hasNext()) {
               Edge e = (Edge)edges.next();
               SootMethod sm = e.tgt();
               if (sm.getDeclaringClass().isLibraryClass()) {
                  if (this.isShowLibMeths()) {
                     if (recurse) {
                        list.add(new MethInfo(sm, this.hasTgtMethods(sm) | this.hasSrcMethods(sm), e.kind()));
                     } else {
                        list.add(new MethInfo(sm, true, e.kind()));
                     }
                  }
               } else if (recurse) {
                  list.add(new MethInfo(sm, this.hasTgtMethods(sm) | this.hasSrcMethods(sm), e.kind()));
               } else {
                  list.add(new MethInfo(sm, true, e.kind()));
               }
            }
         }

         return list;
      }
   }

   private boolean hasTgtMethods(SootMethod meth) {
      ArrayList<MethInfo> list = this.getTgtMethods(meth, false);
      return !list.isEmpty();
   }

   private boolean hasSrcMethods(SootMethod meth) {
      ArrayList<MethInfo> list = this.getSrcMethods(meth, false);
      return list.size() > 1;
   }

   private ArrayList<MethInfo> getSrcMethods(SootMethod method, boolean recurse) {
      ArrayList<MethInfo> list = new ArrayList();
      Iterator momcIt = this.methodToContexts.get(method).iterator();

      while(momcIt.hasNext()) {
         MethodOrMethodContext momc = (MethodOrMethodContext)momcIt.next();
         Iterator callerEdges = this.cg.edgesInto(momc);

         while(callerEdges.hasNext()) {
            Edge callEdge = (Edge)callerEdges.next();
            SootMethod methodCaller = callEdge.src();
            if (methodCaller.getDeclaringClass().isLibraryClass()) {
               if (this.isShowLibMeths()) {
                  if (recurse) {
                     list.add(new MethInfo(methodCaller, this.hasTgtMethods(methodCaller) | this.hasSrcMethods(methodCaller), callEdge.kind()));
                  } else {
                     list.add(new MethInfo(methodCaller, true, callEdge.kind()));
                  }
               }
            } else if (recurse) {
               list.add(new MethInfo(methodCaller, this.hasTgtMethods(methodCaller) | this.hasSrcMethods(methodCaller), callEdge.kind()));
            } else {
               list.add(new MethInfo(methodCaller, true, callEdge.kind()));
            }
         }
      }

      return list;
   }

   protected void internalTransform(String phaseName, Map options) {
      CGGOptions opts = new CGGOptions(options);
      if (opts.show_lib_meths()) {
         this.setShowLibMeths(true);
      }

      this.cg = Scene.v().getCallGraph();
      if (Options.v().interactive_mode()) {
         this.reset();
      }

   }

   public void reset() {
      if (this.methodToContexts == null) {
         this.methodToContexts = new MethodToContexts(Scene.v().getReachableMethods().listener());
      }

      if (Scene.v().hasCallGraph()) {
         SootClass sc = Scene.v().getMainClass();
         SootMethod sm = this.getFirstMethod(sc);
         ArrayList<MethInfo> tgts = this.getTgtMethods(sm, true);
         ArrayList<MethInfo> srcs = this.getSrcMethods(sm, true);
         CallGraphInfo info = new CallGraphInfo(sm, tgts, srcs);
         InteractionHandler.v().handleCallGraphStart(info, this);
      }

   }

   private SootMethod getFirstMethod(SootClass sc) {
      ArrayList paramTypes = new ArrayList();
      paramTypes.add(ArrayType.v(RefType.v("java.lang.String"), 1));
      SootMethod sm = sc.getMethodUnsafe("main", paramTypes, VoidType.v());
      return sm != null ? sm : (SootMethod)sc.getMethods().get(0);
   }

   public void handleNextMethod() {
      if (this.getNextMethod().hasActiveBody()) {
         ArrayList<MethInfo> tgts = this.getTgtMethods(this.getNextMethod(), true);
         ArrayList<MethInfo> srcs = this.getSrcMethods(this.getNextMethod(), true);
         CallGraphInfo info = new CallGraphInfo(this.getNextMethod(), tgts, srcs);
         InteractionHandler.v().handleCallGraphPart(info);
      }
   }

   public void setNextMethod(SootMethod m) {
      this.nextMethod = m;
   }

   public SootMethod getNextMethod() {
      return this.nextMethod;
   }

   public void setShowLibMeths(boolean b) {
      this.showLibMeths = b;
   }

   public boolean isShowLibMeths() {
      return this.showLibMeths;
   }
}
