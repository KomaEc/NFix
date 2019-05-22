package soot.jimple.toolkits.thread.mhp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.PAG;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.ExceptionalUnitGraph;

public class StartJoinFinder {
   Set<Stmt> startStatements = new HashSet();
   Set<Stmt> joinStatements = new HashSet();
   Map<Stmt, List<SootMethod>> startToRunMethods = new HashMap();
   Map<Stmt, List<AllocNode>> startToAllocNodes = new HashMap();
   Map<Stmt, Stmt> startToJoin = new HashMap();
   Map<Stmt, SootMethod> startToContainingMethod = new HashMap();

   public StartJoinFinder(CallGraph callGraph, PAG pag) {
      Iterator runAnalysisClassesIt = Scene.v().getApplicationClasses().iterator();

      label54:
      while(runAnalysisClassesIt.hasNext()) {
         SootClass appClass = (SootClass)runAnalysisClassesIt.next();
         Iterator methodsIt = appClass.getMethods().iterator();

         while(true) {
            SootMethod method;
            boolean mayHaveStartStmt;
            do {
               label41:
               do {
                  if (!methodsIt.hasNext()) {
                     continue label54;
                  }

                  method = (SootMethod)methodsIt.next();
                  mayHaveStartStmt = false;
                  Iterator edgesIt = callGraph.edgesOutOf((MethodOrMethodContext)method);

                  while(true) {
                     SootMethod target;
                     do {
                        if (!edgesIt.hasNext()) {
                           continue label41;
                        }

                        target = ((Edge)edgesIt.next()).tgt();
                     } while(!target.getName().equals("start") && !target.getName().equals("run"));

                     mayHaveStartStmt = true;
                  }
               } while(!mayHaveStartStmt);
            } while(!method.isConcrete());

            Body b = method.retrieveActiveBody();
            StartJoinAnalysis sja = new StartJoinAnalysis(new ExceptionalUnitGraph(b), method, callGraph, pag);
            this.startStatements.addAll(sja.getStartStatements());
            this.joinStatements.addAll(sja.getJoinStatements());
            this.startToRunMethods.putAll(sja.getStartToRunMethods());
            this.startToAllocNodes.putAll(sja.getStartToAllocNodes());
            this.startToJoin.putAll(sja.getStartToJoin());
            Iterator startIt = sja.getStartStatements().iterator();

            while(startIt.hasNext()) {
               Stmt start = (Stmt)startIt.next();
               this.startToContainingMethod.put(start, method);
            }
         }
      }

   }

   public Set<Stmt> getStartStatements() {
      return this.startStatements;
   }

   public Set<Stmt> getJoinStatements() {
      return this.joinStatements;
   }

   public Map<Stmt, List<SootMethod>> getStartToRunMethods() {
      return this.startToRunMethods;
   }

   public Map<Stmt, List<AllocNode>> getStartToAllocNodes() {
      return this.startToAllocNodes;
   }

   public Map<Stmt, Stmt> getStartToJoin() {
      return this.startToJoin;
   }

   public Map<Stmt, SootMethod> getStartToContainingMethod() {
      return this.startToContainingMethod;
   }
}
