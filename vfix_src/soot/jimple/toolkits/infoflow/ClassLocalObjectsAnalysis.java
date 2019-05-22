package soot.jimple.toolkits.infoflow;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.EquivalentValue;
import soot.MethodOrMethodContext;
import soot.RefLikeType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.Value;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.ParameterRef;
import soot.jimple.Ref;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.HashMutableDirectedGraph;
import soot.toolkits.scalar.Pair;

public class ClassLocalObjectsAnalysis {
   private static final Logger logger = LoggerFactory.getLogger(ClassLocalObjectsAnalysis.class);
   boolean printdfgs;
   LocalObjectsAnalysis loa;
   InfoFlowAnalysis dfa;
   InfoFlowAnalysis primitiveDfa;
   UseFinder uf;
   SootClass sootClass;
   Map<SootMethod, SmartMethodLocalObjectsAnalysis> methodToMethodLocalObjectsAnalysis;
   Map<SootMethod, CallLocalityContext> methodToContext;
   List<SootMethod> allMethods;
   List<SootMethod> externalMethods;
   List<SootMethod> internalMethods;
   List<SootMethod> entryMethods;
   List<SootField> allFields;
   List<SootField> externalFields;
   List<SootField> internalFields;
   ArrayList<SootField> localFields;
   ArrayList<SootField> sharedFields;
   ArrayList<SootField> localInnerFields;
   ArrayList<SootField> sharedInnerFields;

   public ClassLocalObjectsAnalysis(LocalObjectsAnalysis loa, InfoFlowAnalysis dfa, UseFinder uf, SootClass sootClass) {
      this(loa, dfa, (InfoFlowAnalysis)null, uf, sootClass, (List)null);
   }

   public ClassLocalObjectsAnalysis(LocalObjectsAnalysis loa, InfoFlowAnalysis dfa, InfoFlowAnalysis primitiveDfa, UseFinder uf, SootClass sootClass, List<SootMethod> entryMethods) {
      this.printdfgs = dfa.printDebug();
      this.loa = loa;
      this.dfa = dfa;
      this.primitiveDfa = primitiveDfa;
      this.uf = uf;
      this.sootClass = sootClass;
      this.methodToMethodLocalObjectsAnalysis = new HashMap();
      this.methodToContext = null;
      this.allMethods = null;
      this.externalMethods = null;
      this.internalMethods = null;
      this.entryMethods = entryMethods;
      this.allFields = null;
      this.externalFields = null;
      this.internalFields = null;
      this.localFields = null;
      this.sharedFields = null;
      this.localInnerFields = null;
      this.sharedInnerFields = null;
      logger.debug("[local-objects] Analyzing local objects for " + sootClass);
      logger.debug("[local-objects]   preparing class             " + new Date());
      this.prepare();
      logger.debug("[local-objects]   analyzing class             " + new Date());
      this.doAnalysis();
      logger.debug("[local-objects]   propagating over call graph " + new Date());
      this.propagate();
      logger.debug("[local-objects]   finished at                 " + new Date());
      logger.debug("[local-objects]   (#analyzed/#encountered): " + SmartMethodInfoFlowAnalysis.counter + "/" + ClassInfoFlowAnalysis.methodCount);
   }

   private void prepare() {
      this.allMethods = getAllReachableMethods(this.sootClass);
      this.externalMethods = this.uf.getExtMethods(this.sootClass);
      SootClass superclass = this.sootClass;
      if (superclass.hasSuperclass()) {
         superclass = superclass.getSuperclass();
      }

      for(; superclass.hasSuperclass(); superclass = superclass.getSuperclass()) {
         if (superclass.isApplicationClass()) {
            this.externalMethods.addAll(this.uf.getExtMethods(superclass));
         }
      }

      this.internalMethods = new ArrayList();
      Iterator var2 = this.allMethods.iterator();

      while(var2.hasNext()) {
         SootMethod method = (SootMethod)var2.next();
         if (!this.externalMethods.contains(method)) {
            this.internalMethods.add(method);
         }
      }

      this.allFields = getAllFields(this.sootClass);
      this.externalFields = this.uf.getExtFields(this.sootClass);
      superclass = this.sootClass;
      if (superclass.hasSuperclass()) {
         superclass = superclass.getSuperclass();
      }

      for(; superclass.hasSuperclass(); superclass = superclass.getSuperclass()) {
         if (superclass.isApplicationClass()) {
            this.externalFields.addAll(this.uf.getExtFields(superclass));
         }
      }

      this.internalFields = new ArrayList();
      var2 = this.allFields.iterator();

      while(var2.hasNext()) {
         SootField field = (SootField)var2.next();
         if (!this.externalFields.contains(field)) {
            this.internalFields.add(field);
         }
      }

   }

   public static List<SootMethod> getAllReachableMethods(SootClass sc) {
      ReachableMethods rm = Scene.v().getReachableMethods();
      List<SootMethod> allMethods = new ArrayList();
      Iterator methodsIt = sc.methodIterator();

      while(methodsIt.hasNext()) {
         SootMethod method = (SootMethod)methodsIt.next();
         if (rm.contains(method)) {
            allMethods.add(method);
         }
      }

      SootClass superclass = sc;
      if (sc.hasSuperclass()) {
         superclass = sc.getSuperclass();
      }

      while(superclass.hasSuperclass()) {
         Iterator scMethodsIt = superclass.methodIterator();

         while(scMethodsIt.hasNext()) {
            SootMethod scMethod = (SootMethod)scMethodsIt.next();
            if (rm.contains(scMethod)) {
               allMethods.add(scMethod);
            }
         }

         superclass = superclass.getSuperclass();
      }

      return allMethods;
   }

   public static List<SootField> getAllFields(SootClass sc) {
      List<SootField> allFields = new ArrayList();
      Iterator var2 = sc.getFields().iterator();

      while(var2.hasNext()) {
         SootField field = (SootField)var2.next();
         allFields.add(field);
      }

      SootClass superclass = sc;
      if (sc.hasSuperclass()) {
         superclass = sc.getSuperclass();
      }

      while(superclass.hasSuperclass()) {
         Iterator var6 = superclass.getFields().iterator();

         while(var6.hasNext()) {
            SootField scField = (SootField)var6.next();
            allFields.add(scField);
         }

         superclass = superclass.getSuperclass();
      }

      return allFields;
   }

   private void doAnalysis() {
      this.localFields = new ArrayList();
      this.sharedFields = new ArrayList();
      Iterator fieldsIt = this.allFields.iterator();

      while(fieldsIt.hasNext()) {
         SootField field = (SootField)fieldsIt.next();
         if (this.fieldIsInitiallyLocal(field)) {
            this.localFields.add(field);
         } else {
            this.sharedFields.add(field);
         }
      }

      this.localInnerFields = new ArrayList();
      this.sharedInnerFields = new ArrayList();
      Iterator methodsIt = this.allMethods.iterator();

      Iterator sharedsToPrintIt;
      while(methodsIt.hasNext()) {
         SootMethod method = (SootMethod)methodsIt.next();
         HashMutableDirectedGraph dataFlowSummary;
         HashMutableDirectedGraph primitiveGraph;
         if (this.primitiveDfa != null) {
            dataFlowSummary = this.primitiveDfa.getMethodInfoFlowSummary(method);
            if (this.printdfgs && method.getDeclaringClass().isApplicationClass()) {
               logger.debug("Attempting to print graphs (will succeed only if ./dfg/ is a valid path)");
               primitiveGraph = this.primitiveDfa.getMethodInfoFlowAnalysis(method).getMethodAbbreviatedInfoFlowGraph();
               InfoFlowAnalysis.printGraphToDotFile("dfg/" + method.getDeclaringClass().getShortName() + "_" + method.getName() + "_primitive", primitiveGraph, method.getName() + "_primitive", false);
               DirectedGraph nonPrimitiveGraph = this.dfa.getMethodInfoFlowAnalysis(method).getMethodAbbreviatedInfoFlowGraph();
               InfoFlowAnalysis.printGraphToDotFile("dfg/" + method.getDeclaringClass().getShortName() + "_" + method.getName(), nonPrimitiveGraph, method.getName(), false);
            }
         } else {
            dataFlowSummary = this.dfa.getMethodInfoFlowSummary(method);
            if (this.printdfgs && method.getDeclaringClass().isApplicationClass()) {
               logger.debug("Attempting to print graph (will succeed only if ./dfg/ is a valid path)");
               primitiveGraph = this.dfa.getMethodInfoFlowAnalysis(method).getMethodAbbreviatedInfoFlowGraph();
               InfoFlowAnalysis.printGraphToDotFile("dfg/" + method.getDeclaringClass().getShortName() + "_" + method.getName(), primitiveGraph, method.getName(), false);
            }
         }

         sharedsToPrintIt = dataFlowSummary.getNodes().iterator();

         while(sharedsToPrintIt.hasNext()) {
            EquivalentValue node = (EquivalentValue)sharedsToPrintIt.next();
            if (node.getValue() instanceof InstanceFieldRef) {
               InstanceFieldRef ifr = (InstanceFieldRef)node.getValue();
               if (!this.localFields.contains(ifr.getField()) && !this.sharedFields.contains(ifr.getField()) && !this.localInnerFields.contains(ifr.getField())) {
                  this.localInnerFields.add(ifr.getField());
               }
            }
         }
      }

      boolean changed = true;

      SootField localField;
      label259:
      while(changed) {
         changed = false;
         methodsIt = this.allMethods.iterator();

         label257:
         while(true) {
            SootMethod method;
            do {
               do {
                  if (!methodsIt.hasNext()) {
                     continue label259;
                  }

                  method = (SootMethod)methodsIt.next();
               } while(method.isStatic());
            } while(!method.isConcrete());

            ListIterator localFieldsIt = this.localFields.listIterator();

            while(true) {
               while(localFieldsIt.hasNext()) {
                  localField = (SootField)localFieldsIt.next();
                  List sourcesAndSinks = new ArrayList();
                  HashMutableDirectedGraph dataFlowSummary;
                  if (this.primitiveDfa != null) {
                     dataFlowSummary = this.primitiveDfa.getMethodInfoFlowSummary(method);
                  } else {
                     dataFlowSummary = this.dfa.getMethodInfoFlowSummary(method);
                  }

                  EquivalentValue node = InfoFlowAnalysis.getNodeForFieldRef(method, localField);
                  if (dataFlowSummary.containsNode(node)) {
                     sourcesAndSinks.addAll(dataFlowSummary.getSuccsOf(node));
                     sourcesAndSinks.addAll(dataFlowSummary.getPredsOf(node));
                  }

                  Iterator sourcesAndSinksIt = sourcesAndSinks.iterator();
                  if (localField.getDeclaringClass().isApplicationClass() && sourcesAndSinksIt.hasNext()) {
                  }

                  while(sourcesAndSinksIt.hasNext()) {
                     EquivalentValue sourceOrSink = (EquivalentValue)sourcesAndSinksIt.next();
                     Ref sourceOrSinkRef = (Ref)sourceOrSink.getValue();
                     boolean fieldBecomesShared = false;
                     if (sourceOrSinkRef instanceof ParameterRef) {
                        fieldBecomesShared = !this.parameterIsLocal(method, sourceOrSink, true);
                     } else if (sourceOrSinkRef instanceof ThisRef) {
                        fieldBecomesShared = !this.thisIsLocal(method, sourceOrSink);
                     } else if (!(sourceOrSinkRef instanceof InstanceFieldRef)) {
                        if (!(sourceOrSinkRef instanceof StaticFieldRef)) {
                           throw new RuntimeException("Unknown type of Ref in Data Flow Graph:");
                        }

                        fieldBecomesShared = true;
                     } else {
                        fieldBecomesShared = this.sharedFields.contains(((FieldRef)sourceOrSinkRef).getField()) || this.sharedInnerFields.contains(((FieldRef)sourceOrSinkRef).getField());
                     }

                     if (fieldBecomesShared) {
                        localFieldsIt.remove();
                        this.sharedFields.add(localField);
                        changed = true;
                        break;
                     }
                  }
               }

               ListIterator localInnerFieldsIt = this.localInnerFields.listIterator();

               while(true) {
                  while(true) {
                     if (changed || !localInnerFieldsIt.hasNext()) {
                        continue label257;
                     }

                     SootField localInnerField = (SootField)localInnerFieldsIt.next();
                     List sourcesAndSinks = new ArrayList();
                     HashMutableDirectedGraph dataFlowSummary;
                     if (this.primitiveDfa != null) {
                        dataFlowSummary = this.primitiveDfa.getMethodInfoFlowSummary(method);
                     } else {
                        dataFlowSummary = this.dfa.getMethodInfoFlowSummary(method);
                     }

                     EquivalentValue node = InfoFlowAnalysis.getNodeForFieldRef(method, localInnerField);
                     if (dataFlowSummary.containsNode(node)) {
                        sourcesAndSinks.addAll(dataFlowSummary.getSuccsOf(node));
                        sourcesAndSinks.addAll(dataFlowSummary.getPredsOf(node));
                     }

                     Iterator sourcesAndSinksIt = sourcesAndSinks.iterator();
                     if (localInnerField.getDeclaringClass().isApplicationClass() && sourcesAndSinksIt.hasNext()) {
                     }

                     while(sourcesAndSinksIt.hasNext()) {
                        EquivalentValue sourceOrSink = (EquivalentValue)sourcesAndSinksIt.next();
                        Ref sourceOrSinkRef = (Ref)sourceOrSink.getValue();
                        boolean fieldBecomesShared = false;
                        if (sourceOrSinkRef instanceof ParameterRef) {
                           fieldBecomesShared = !this.parameterIsLocal(method, sourceOrSink, true);
                        } else if (sourceOrSinkRef instanceof ThisRef) {
                           fieldBecomesShared = !this.thisIsLocal(method, sourceOrSink);
                        } else if (!(sourceOrSinkRef instanceof InstanceFieldRef)) {
                           if (!(sourceOrSinkRef instanceof StaticFieldRef)) {
                              throw new RuntimeException("Unknown type of Ref in Data Flow Graph:");
                           }

                           fieldBecomesShared = true;
                        } else {
                           fieldBecomesShared = this.sharedFields.contains(((FieldRef)sourceOrSinkRef).getField()) || this.sharedInnerFields.contains(((FieldRef)sourceOrSinkRef).getField());
                        }

                        if (fieldBecomesShared) {
                           localInnerFieldsIt.remove();
                           this.sharedInnerFields.add(localInnerField);
                           changed = true;
                           break;
                        }
                     }
                  }
               }
            }
         }
      }

      if (this.dfa.printDebug()) {
         logger.debug("        Found local/shared fields for " + this.sootClass.toString());
         logger.debug("          Local fields: ");
         Iterator localsToPrintIt = this.localFields.iterator();

         while(localsToPrintIt.hasNext()) {
            SootField localToPrint = (SootField)localsToPrintIt.next();
            if (localToPrint.getDeclaringClass().isApplicationClass()) {
               logger.debug("                  " + localToPrint);
            }
         }

         logger.debug("          Shared fields: ");
         sharedsToPrintIt = this.sharedFields.iterator();

         while(sharedsToPrintIt.hasNext()) {
            localField = (SootField)sharedsToPrintIt.next();
            if (localField.getDeclaringClass().isApplicationClass()) {
               logger.debug("                  " + localField);
            }
         }

         logger.debug("          Local inner fields: ");
         localsToPrintIt = this.localInnerFields.iterator();

         while(localsToPrintIt.hasNext()) {
            localField = (SootField)localsToPrintIt.next();
            if (localField.getDeclaringClass().isApplicationClass()) {
               logger.debug("                  " + localField);
            }
         }

         logger.debug("          Shared inner fields: ");
         sharedsToPrintIt = this.sharedInnerFields.iterator();

         while(sharedsToPrintIt.hasNext()) {
            localField = (SootField)sharedsToPrintIt.next();
            if (localField.getDeclaringClass().isApplicationClass()) {
               logger.debug("                  " + localField);
            }
         }
      }

   }

   private void propagate() {
      ArrayList<SootMethod> worklist = new ArrayList();
      worklist.addAll(this.entryMethods);
      this.methodToContext = new HashMap();
      Iterator var2 = worklist.iterator();

      while(var2.hasNext()) {
         SootMethod method = (SootMethod)var2.next();
         this.methodToContext.put(method, this.getContextFor(method));
      }

      Date start = new Date();
      if (this.dfa.printDebug()) {
         logger.debug("CLOA: Starting Propagation at " + start);
      }

      while(worklist.size() > 0) {
         ArrayList<SootMethod> newWorklist = new ArrayList();
         Iterator var4 = worklist.iterator();

         while(var4.hasNext()) {
            SootMethod containingMethod = (SootMethod)var4.next();
            CallLocalityContext containingContext = (CallLocalityContext)this.methodToContext.get(containingMethod);
            if (this.dfa.printDebug()) {
               logger.debug("      " + containingMethod.getName() + " " + containingContext.toShortString());
            }

            Map<Stmt, CallLocalityContext> invokeToContext = new HashMap();
            Iterator edgesIt = Scene.v().getCallGraph().edgesOutOf((MethodOrMethodContext)containingMethod);

            while(edgesIt.hasNext()) {
               Edge e = (Edge)edgesIt.next();
               if (e.src().getDeclaringClass().isApplicationClass() && e.srcStmt() != null) {
                  CallLocalityContext invokeContext;
                  if (!invokeToContext.containsKey(e.srcStmt())) {
                     invokeContext = this.getContextFor(e, containingMethod, containingContext);
                     invokeToContext.put(e.srcStmt(), invokeContext);
                  } else {
                     invokeContext = (CallLocalityContext)invokeToContext.get(e.srcStmt());
                  }

                  if (!this.methodToContext.containsKey(e.tgt())) {
                     this.methodToContext.put(e.tgt(), invokeContext);
                     newWorklist.add(e.tgt());
                  } else {
                     boolean causedChange = ((CallLocalityContext)this.methodToContext.get(e.tgt())).merge(invokeContext);
                     if (causedChange) {
                        newWorklist.add(e.tgt());
                     }
                  }
               }
            }
         }

         worklist = newWorklist;
      }

      long longTime = ((new Date()).getTime() - start.getTime()) / 100L;
      float time = (float)longTime / 10.0F;
      if (this.dfa.printDebug()) {
         logger.debug("CLOA: Ending Propagation after " + time + "s");
      }

   }

   public CallLocalityContext getMergedContext(SootMethod method) {
      return this.methodToContext.containsKey(method) ? (CallLocalityContext)this.methodToContext.get(method) : null;
   }

   private CallLocalityContext getContextFor(Edge e, SootMethod containingMethod, CallLocalityContext containingContext) {
      InvokeExpr ie;
      if (e.srcStmt().containsInvokeExpr()) {
         ie = e.srcStmt().getInvokeExpr();
      } else {
         ie = null;
      }

      SootMethod callingMethod = e.tgt();
      CallLocalityContext callingContext = new CallLocalityContext(this.dfa.getMethodInfoFlowSummary(callingMethod).getNodes());
      if (callingMethod.isConcrete()) {
         Body b = containingMethod.retrieveActiveBody();
         if (ie != null && ie instanceof InstanceInvokeExpr) {
            InstanceInvokeExpr iie = (InstanceInvokeExpr)ie;
            if (!containingMethod.isStatic() && iie.getBase().equivTo(b.getThisLocal())) {
               Iterator localRefsIt = containingContext.getLocalRefs().iterator();

               while(localRefsIt.hasNext()) {
                  EquivalentValue rEqVal = (EquivalentValue)localRefsIt.next();
                  Ref r = (Ref)rEqVal.getValue();
                  if (r instanceof InstanceFieldRef) {
                     EquivalentValue newRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(callingMethod, ((FieldRef)r).getFieldRef().resolve());
                     if (callingContext.containsField(newRefEqVal)) {
                        callingContext.setFieldLocal(newRefEqVal);
                     }
                  } else if (r instanceof ThisRef) {
                     callingContext.setThisLocal();
                  }
               }
            } else if (SmartMethodLocalObjectsAnalysis.isObjectLocal(this.dfa, containingMethod, containingContext, iie.getBase())) {
               callingContext.setAllFieldsLocal();
               callingContext.setThisLocal();
            } else {
               callingContext.setAllFieldsShared();
               callingContext.setThisShared();
            }
         } else {
            callingContext.setAllFieldsShared();
            callingContext.setThisShared();
         }

         if (ie == null) {
            callingContext.setAllParamsShared();
         } else {
            for(int param = 0; param < ie.getArgCount(); ++param) {
               if (SmartMethodLocalObjectsAnalysis.isObjectLocal(this.dfa, containingMethod, containingContext, ie.getArg(param))) {
                  callingContext.setParamLocal(param);
               } else {
                  callingContext.setParamShared(param);
               }
            }
         }
      } else {
         callingContext.setAllFieldsShared();
         callingContext.setThisShared();
         callingContext.setAllParamsShared();
      }

      return callingContext;
   }

   public CallLocalityContext getContextFor(SootMethod sm) {
      return this.getContextFor(sm, false);
   }

   private CallLocalityContext getContextFor(SootMethod sm, boolean includePrimitiveDataFlowIfAvailable) {
      CallLocalityContext context;
      if (includePrimitiveDataFlowIfAvailable) {
         context = new CallLocalityContext(this.primitiveDfa.getMethodInfoFlowSummary(sm).getNodes());
      } else {
         context = new CallLocalityContext(this.dfa.getMethodInfoFlowSummary(sm).getNodes());
      }

      for(int i = 0; i < sm.getParameterCount(); ++i) {
         EquivalentValue paramEqVal = InfoFlowAnalysis.getNodeForParameterRef(sm, i);
         if (this.parameterIsLocal(sm, paramEqVal, includePrimitiveDataFlowIfAvailable)) {
            context.setParamLocal(i);
         } else {
            context.setParamShared(i);
         }
      }

      Iterator var7 = this.getLocalFields().iterator();

      EquivalentValue fieldRefEqVal;
      SootField sf;
      while(var7.hasNext()) {
         sf = (SootField)var7.next();
         fieldRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(sm, sf);
         context.setFieldLocal(fieldRefEqVal);
      }

      var7 = this.getSharedFields().iterator();

      while(var7.hasNext()) {
         sf = (SootField)var7.next();
         fieldRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(sm, sf);
         context.setFieldShared(fieldRefEqVal);
      }

      return context;
   }

   public boolean isObjectLocal(Value localOrRef, SootMethod sm) {
      return this.isObjectLocal(localOrRef, sm, false);
   }

   private boolean isObjectLocal(Value localOrRef, SootMethod sm, boolean includePrimitiveDataFlowIfAvailable) {
      if (localOrRef instanceof StaticFieldRef) {
         return false;
      } else {
         if (this.dfa.printDebug()) {
            logger.debug("      CLOA testing if " + localOrRef + " is local in " + sm);
         }

         SmartMethodLocalObjectsAnalysis smloa = this.getMethodLocalObjectsAnalysis(sm, includePrimitiveDataFlowIfAvailable);
         boolean retval;
         if (localOrRef instanceof InstanceFieldRef) {
            InstanceFieldRef ifr = (InstanceFieldRef)localOrRef;
            if (ifr.getBase().equivTo(smloa.getThisLocal())) {
               return this.isFieldLocal(ifr.getFieldRef().resolve());
            } else if (this.isObjectLocal(ifr.getBase(), sm, includePrimitiveDataFlowIfAvailable)) {
               retval = this.loa.isFieldLocalToParent(ifr.getFieldRef().resolve());
               if (this.dfa.printDebug()) {
                  logger.debug("      " + (retval ? "local" : "shared"));
               }

               return retval;
            } else {
               if (this.dfa.printDebug()) {
                  logger.debug("      shared");
               }

               return false;
            }
         } else {
            CallLocalityContext context = this.getContextFor(sm);
            retval = smloa.isObjectLocal(localOrRef, context);
            if (this.dfa.printDebug()) {
               logger.debug("      " + (retval ? "local" : "shared"));
            }

            return retval;
         }
      }
   }

   public SmartMethodLocalObjectsAnalysis getMethodLocalObjectsAnalysis(SootMethod sm) {
      return this.getMethodLocalObjectsAnalysis(sm, false);
   }

   private SmartMethodLocalObjectsAnalysis getMethodLocalObjectsAnalysis(SootMethod sm, boolean includePrimitiveDataFlowIfAvailable) {
      Body b;
      ExceptionalUnitGraph g;
      if (includePrimitiveDataFlowIfAvailable && this.primitiveDfa != null) {
         b = sm.retrieveActiveBody();
         g = new ExceptionalUnitGraph(b);
         return new SmartMethodLocalObjectsAnalysis(g, this.primitiveDfa);
      } else {
         if (!this.methodToMethodLocalObjectsAnalysis.containsKey(sm)) {
            b = sm.retrieveActiveBody();
            g = new ExceptionalUnitGraph(b);
            SmartMethodLocalObjectsAnalysis smloa = new SmartMethodLocalObjectsAnalysis(g, this.dfa);
            this.methodToMethodLocalObjectsAnalysis.put(sm, smloa);
         }

         return (SmartMethodLocalObjectsAnalysis)this.methodToMethodLocalObjectsAnalysis.get(sm);
      }
   }

   private boolean fieldIsInitiallyLocal(SootField field) {
      if (field.isStatic()) {
         return false;
      } else if (field.isPrivate()) {
         return true;
      } else {
         return !this.externalFields.contains(field);
      }
   }

   protected List<SootField> getSharedFields() {
      return (List)this.sharedFields.clone();
   }

   protected List<SootField> getLocalFields() {
      return (List)this.localFields.clone();
   }

   public List<SootField> getInnerSharedFields() {
      return this.sharedInnerFields;
   }

   protected boolean isFieldLocal(SootField field) {
      return this.localFields.contains(field);
   }

   protected boolean isFieldLocal(EquivalentValue fieldRef) {
      return this.localFields.contains(((SootFieldRef)fieldRef.getValue()).resolve());
   }

   public boolean parameterIsLocal(SootMethod method, EquivalentValue parameterRef) {
      return this.parameterIsLocal(method, parameterRef, false);
   }

   protected boolean parameterIsLocal(SootMethod method, EquivalentValue parameterRef, boolean includePrimitiveDataFlowIfAvailable) {
      if (this.dfa.printDebug() && method.getDeclaringClass().isApplicationClass()) {
         logger.debug("        Checking PARAM " + parameterRef + " for " + method);
      }

      ParameterRef param = (ParameterRef)parameterRef.getValue();
      if (!(param.getType() instanceof RefLikeType) && (!this.dfa.includesPrimitiveInfoFlow() || method.getName().equals("<init>"))) {
         if (this.dfa.printDebug() && method.getDeclaringClass().isApplicationClass()) {
            logger.debug("          PARAM is local (primitive)");
         }

         return true;
      } else {
         List extClassCalls = this.uf.getExtCalls(this.sootClass);
         Iterator extClassCallsIt = extClassCalls.iterator();

         Stmt s;
         do {
            if (!extClassCallsIt.hasNext()) {
               List intClassCalls = this.uf.getIntCalls(this.sootClass);
               Iterator intClassCallsIt = intClassCalls.iterator();

               while(intClassCallsIt.hasNext()) {
                  Pair intCall = (Pair)intClassCallsIt.next();
                  SootMethod containingMethod = (SootMethod)intCall.getO1();
                  Stmt s = (Stmt)intCall.getO2();
                  InvokeExpr ie = s.getInvokeExpr();
                  if (ie.getMethodRef().resolve() == method) {
                     if (((ParameterRef)parameterRef.getValue()).getIndex() >= 0) {
                        if (!this.isObjectLocal(ie.getArg(((ParameterRef)parameterRef.getValue()).getIndex()), containingMethod, includePrimitiveDataFlowIfAvailable)) {
                           if (this.dfa.printDebug() && method.getDeclaringClass().isApplicationClass()) {
                              logger.debug("          PARAM is shared (internal propagation)");
                           }

                           return false;
                        }
                     } else if (s instanceof DefinitionStmt) {
                        Value obj = ((DefinitionStmt)s).getLeftOp();
                        if (!this.isObjectLocal(obj, containingMethod, includePrimitiveDataFlowIfAvailable)) {
                           if (this.dfa.printDebug() && method.getDeclaringClass().isApplicationClass()) {
                              logger.debug("          PARAM is shared (internal propagation)");
                           }

                           return false;
                        }
                     }
                  }
               }

               if (this.dfa.printDebug() && method.getDeclaringClass().isApplicationClass()) {
                  logger.debug("          PARAM is local SO FAR (internal propagation)");
               }

               return true;
            }

            Pair extCall = (Pair)extClassCallsIt.next();
            s = (Stmt)extCall.getO2();
         } while(s.getInvokeExpr().getMethodRef().resolve() != method);

         if (this.dfa.printDebug() && method.getDeclaringClass().isApplicationClass()) {
            logger.debug("          PARAM is shared (external access)");
         }

         return false;
      }
   }

   protected boolean thisIsLocal(SootMethod method, EquivalentValue thisRef) {
      return true;
   }
}
