package soot.jimple.toolkits.infoflow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.EquivalentValue;
import soot.Local;
import soot.RefLikeType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.jimple.FieldRef;
import soot.jimple.IdentityRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.ParameterRef;
import soot.jimple.Ref;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.HashMutableDirectedGraph;
import soot.toolkits.graph.MemoryEfficientGraph;
import soot.toolkits.graph.MutableDirectedGraph;
import soot.toolkits.graph.UnitGraph;

public class ClassInfoFlowAnalysis {
   private static final Logger logger = LoggerFactory.getLogger(ClassInfoFlowAnalysis.class);
   SootClass sootClass;
   InfoFlowAnalysis dfa;
   Map<SootMethod, SmartMethodInfoFlowAnalysis> methodToInfoFlowAnalysis;
   Map<SootMethod, HashMutableDirectedGraph<EquivalentValue>> methodToInfoFlowSummary;
   public static int methodCount = 0;

   public ClassInfoFlowAnalysis(SootClass sootClass, InfoFlowAnalysis dfa) {
      this.sootClass = sootClass;
      this.dfa = dfa;
      this.methodToInfoFlowAnalysis = new HashMap();
      this.methodToInfoFlowSummary = new HashMap();
   }

   public SmartMethodInfoFlowAnalysis getMethodInfoFlowAnalysis(SootMethod method) {
      if (!this.methodToInfoFlowAnalysis.containsKey(method)) {
         ++methodCount;
         if (!this.methodToInfoFlowSummary.containsKey(method)) {
            HashMutableDirectedGraph<EquivalentValue> dataFlowGraph = this.simpleConservativeInfoFlowAnalysis(method);
            this.methodToInfoFlowSummary.put(method, dataFlowGraph);
         }

         if (method.isConcrete()) {
            Body b = method.retrieveActiveBody();
            UnitGraph g = new ExceptionalUnitGraph(b);
            SmartMethodInfoFlowAnalysis smdfa = new SmartMethodInfoFlowAnalysis(g, this.dfa);
            this.methodToInfoFlowAnalysis.put(method, smdfa);
            this.methodToInfoFlowSummary.remove(method);
            this.methodToInfoFlowSummary.put(method, smdfa.getMethodInfoFlowSummary());
            return smdfa;
         }
      }

      return (SmartMethodInfoFlowAnalysis)this.methodToInfoFlowAnalysis.get(method);
   }

   public MutableDirectedGraph<EquivalentValue> getMethodInfoFlowSummary(SootMethod method) {
      return this.getMethodInfoFlowSummary(method, true);
   }

   public HashMutableDirectedGraph<EquivalentValue> getMethodInfoFlowSummary(SootMethod method, boolean doFullAnalysis) {
      if (!this.methodToInfoFlowSummary.containsKey(method)) {
         ++methodCount;
         HashMutableDirectedGraph<EquivalentValue> dataFlowGraph = this.simpleConservativeInfoFlowAnalysis(method);
         this.methodToInfoFlowSummary.put(method, dataFlowGraph);
         if (method.isConcrete() && doFullAnalysis) {
            Body b = method.retrieveActiveBody();
            UnitGraph g = new ExceptionalUnitGraph(b);
            SmartMethodInfoFlowAnalysis smdfa = new SmartMethodInfoFlowAnalysis(g, this.dfa);
            this.methodToInfoFlowAnalysis.put(method, smdfa);
            this.methodToInfoFlowSummary.remove(method);
            this.methodToInfoFlowSummary.put(method, smdfa.getMethodInfoFlowSummary());
         }
      }

      return (HashMutableDirectedGraph)this.methodToInfoFlowSummary.get(method);
   }

   private HashMutableDirectedGraph<EquivalentValue> simpleConservativeInfoFlowAnalysis(SootMethod sm) {
      if (!sm.isConcrete()) {
         return this.triviallyConservativeInfoFlowAnalysis(sm);
      } else {
         Body b = sm.retrieveActiveBody();
         UnitGraph g = new ExceptionalUnitGraph(b);
         HashSet<EquivalentValue> fieldsStaticsParamsAccessed = new HashSet();
         Iterator var5 = g.iterator();

         while(var5.hasNext()) {
            Unit u = (Unit)var5.next();
            Stmt s = (Stmt)u;
            if (s instanceof IdentityStmt) {
               IdentityStmt is = (IdentityStmt)s;
               IdentityRef ir = (IdentityRef)is.getRightOp();
               if (ir instanceof ParameterRef) {
                  ParameterRef pr = (ParameterRef)ir;
                  fieldsStaticsParamsAccessed.add(InfoFlowAnalysis.getNodeForParameterRef(sm, pr.getIndex()));
               }
            }

            if (s.containsFieldRef()) {
               FieldRef ref = s.getFieldRef();
               if (ref instanceof StaticFieldRef) {
                  StaticFieldRef sfr = (StaticFieldRef)ref;
                  fieldsStaticsParamsAccessed.add(InfoFlowAnalysis.getNodeForFieldRef(sm, sfr.getField()));
               } else if (ref instanceof InstanceFieldRef) {
                  InstanceFieldRef ifr = (InstanceFieldRef)ref;
                  Value base = ifr.getBase();
                  if (base instanceof Local && (this.dfa.includesInnerFields() || !sm.isStatic() && base.equivTo(b.getThisLocal()))) {
                     fieldsStaticsParamsAccessed.add(InfoFlowAnalysis.getNodeForFieldRef(sm, ifr.getField()));
                  }
               }
            }
         }

         HashMutableDirectedGraph<EquivalentValue> dataFlowGraph = new MemoryEfficientGraph();
         Iterator accessedIt1 = fieldsStaticsParamsAccessed.iterator();

         while(accessedIt1.hasNext()) {
            EquivalentValue o = (EquivalentValue)accessedIt1.next();
            dataFlowGraph.addNode(o);
         }

         for(int i = 0; i < sm.getParameterCount(); ++i) {
            EquivalentValue parameterRefEqVal = InfoFlowAnalysis.getNodeForParameterRef(sm, i);
            if (!dataFlowGraph.containsNode(parameterRefEqVal)) {
               dataFlowGraph.addNode(parameterRefEqVal);
            }
         }

         Iterator var19 = sm.getDeclaringClass().getFields().iterator();

         while(true) {
            SootField sf;
            EquivalentValue r;
            do {
               if (!var19.hasNext()) {
                  SootClass superclass = sm.getDeclaringClass();
                  if (superclass.hasSuperclass()) {
                     superclass = sm.getDeclaringClass().getSuperclass();
                  }

                  label148:
                  for(; superclass.hasSuperclass(); superclass = superclass.getSuperclass()) {
                     Iterator var24 = superclass.getFields().iterator();

                     while(true) {
                        SootField scField;
                        do {
                           if (!var24.hasNext()) {
                              continue label148;
                           }

                           scField = (SootField)var24.next();
                        } while(!scField.isStatic() && sm.isStatic());

                        EquivalentValue fieldRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(sm, scField);
                        if (!dataFlowGraph.containsNode(fieldRefEqVal)) {
                           dataFlowGraph.addNode(fieldRefEqVal);
                        }
                     }
                  }

                  ParameterRef returnValueRef = null;
                  if (sm.getReturnType() != VoidType.v()) {
                     returnValueRef = new ParameterRef(sm.getReturnType(), -1);
                     dataFlowGraph.addNode(InfoFlowAnalysis.getNodeForReturnRef(sm));
                  }

                  if (!sm.isStatic()) {
                     dataFlowGraph.addNode(InfoFlowAnalysis.getNodeForThisRef(sm));
                     fieldsStaticsParamsAccessed.add(InfoFlowAnalysis.getNodeForThisRef(sm));
                  }

                  accessedIt1 = fieldsStaticsParamsAccessed.iterator();

                  while(true) {
                     do {
                        label124:
                        do {
                           Ref rRef;
                           do {
                              if (!accessedIt1.hasNext()) {
                                 return dataFlowGraph;
                              }

                              r = (EquivalentValue)accessedIt1.next();
                              rRef = (Ref)r.getValue();
                           } while(!(rRef.getType() instanceof RefLikeType) && !this.dfa.includesPrimitiveInfoFlow());

                           Iterator accessedIt2 = fieldsStaticsParamsAccessed.iterator();

                           while(true) {
                              EquivalentValue s;
                              Ref sRef;
                              do {
                                 do {
                                    do {
                                       if (!accessedIt2.hasNext()) {
                                          continue label124;
                                       }

                                       s = (EquivalentValue)accessedIt2.next();
                                       sRef = (Ref)s.getValue();
                                    } while(rRef instanceof ThisRef && sRef instanceof InstanceFieldRef);
                                 } while(sRef instanceof ThisRef && rRef instanceof InstanceFieldRef);
                              } while(sRef instanceof ParameterRef && this.dfa.includesInnerFields());

                              if (sRef.getType() instanceof RefLikeType) {
                                 dataFlowGraph.addEdge(r, s);
                              }
                           }
                        } while(returnValueRef == null);
                     } while(!(returnValueRef.getType() instanceof RefLikeType) && !this.dfa.includesPrimitiveInfoFlow());

                     dataFlowGraph.addEdge(r, InfoFlowAnalysis.getNodeForReturnRef(sm));
                  }
               }

               sf = (SootField)var19.next();
            } while(!sf.isStatic() && sm.isStatic());

            r = InfoFlowAnalysis.getNodeForFieldRef(sm, sf);
            if (!dataFlowGraph.containsNode(r)) {
               dataFlowGraph.addNode(r);
            }
         }
      }
   }

   public HashMutableDirectedGraph<EquivalentValue> triviallyConservativeInfoFlowAnalysis(SootMethod sm) {
      HashSet<EquivalentValue> fieldsStaticsParamsAccessed = new HashSet();

      for(int i = 0; i < sm.getParameterCount(); ++i) {
         EquivalentValue parameterRefEqVal = InfoFlowAnalysis.getNodeForParameterRef(sm, i);
         fieldsStaticsParamsAccessed.add(parameterRefEqVal);
      }

      Iterator it = sm.getDeclaringClass().getFields().iterator();

      while(true) {
         SootField sf;
         do {
            if (!it.hasNext()) {
               SootClass superclass = sm.getDeclaringClass();
               if (superclass.hasSuperclass()) {
                  superclass = sm.getDeclaringClass().getSuperclass();
               }

               EquivalentValue fieldRefEqVal;
               label116:
               for(; superclass.hasSuperclass(); superclass = superclass.getSuperclass()) {
                  Iterator scFieldsIt = superclass.getFields().iterator();

                  while(true) {
                     SootField scField;
                     do {
                        if (!scFieldsIt.hasNext()) {
                           continue label116;
                        }

                        scField = (SootField)scFieldsIt.next();
                     } while(!scField.isStatic() && sm.isStatic());

                     fieldRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(sm, scField);
                     fieldsStaticsParamsAccessed.add(fieldRefEqVal);
                  }
               }

               HashMutableDirectedGraph<EquivalentValue> dataFlowGraph = new MemoryEfficientGraph();
               Iterator accessedIt1 = fieldsStaticsParamsAccessed.iterator();

               while(accessedIt1.hasNext()) {
                  fieldRefEqVal = (EquivalentValue)accessedIt1.next();
                  dataFlowGraph.addNode(fieldRefEqVal);
               }

               ParameterRef returnValueRef = null;
               if (sm.getReturnType() != VoidType.v()) {
                  returnValueRef = new ParameterRef(sm.getReturnType(), -1);
                  dataFlowGraph.addNode(InfoFlowAnalysis.getNodeForReturnRef(sm));
               }

               if (!sm.isStatic()) {
                  dataFlowGraph.addNode(InfoFlowAnalysis.getNodeForThisRef(sm));
                  fieldsStaticsParamsAccessed.add(InfoFlowAnalysis.getNodeForThisRef(sm));
               }

               accessedIt1 = fieldsStaticsParamsAccessed.iterator();

               while(true) {
                  EquivalentValue r;
                  do {
                     label86:
                     do {
                        Ref rRef;
                        do {
                           if (!accessedIt1.hasNext()) {
                              return dataFlowGraph;
                           }

                           r = (EquivalentValue)accessedIt1.next();
                           rRef = (Ref)r.getValue();
                        } while(!(rRef.getType() instanceof RefLikeType) && !this.dfa.includesPrimitiveInfoFlow());

                        Iterator accessedIt2 = fieldsStaticsParamsAccessed.iterator();

                        while(true) {
                           EquivalentValue s;
                           Ref sRef;
                           do {
                              do {
                                 if (!accessedIt2.hasNext()) {
                                    continue label86;
                                 }

                                 s = (EquivalentValue)accessedIt2.next();
                                 sRef = (Ref)s.getValue();
                              } while(rRef instanceof ThisRef && sRef instanceof InstanceFieldRef);
                           } while(sRef instanceof ThisRef && rRef instanceof InstanceFieldRef);

                           if (sRef.getType() instanceof RefLikeType) {
                              dataFlowGraph.addEdge(r, s);
                           }
                        }
                     } while(returnValueRef == null);
                  } while(!(returnValueRef.getType() instanceof RefLikeType) && !this.dfa.includesPrimitiveInfoFlow());

                  dataFlowGraph.addEdge(r, InfoFlowAnalysis.getNodeForReturnRef(sm));
               }
            }

            sf = (SootField)it.next();
         } while(!sf.isStatic() && sm.isStatic());

         EquivalentValue fieldRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(sm, sf);
         fieldsStaticsParamsAccessed.add(fieldRefEqVal);
      }
   }
}
