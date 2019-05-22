package soot.jimple.toolkits.ide.icfg;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import heros.SynchronizedBy;
import heros.solver.IDESolver;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import soot.ArrayType;
import soot.Body;
import soot.FastHierarchy;
import soot.Local;
import soot.Main;
import soot.NullType;
import soot.PackManager;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.SourceLocator;
import soot.Transform;
import soot.Unit;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.toolkits.pointer.LocalMustNotAliasAnalysis;
import soot.options.Options;

public class OnTheFlyJimpleBasedICFG extends AbstractJimpleBasedICFG {
   @SynchronizedBy("by use of synchronized LoadingCache class")
   protected final LoadingCache<Body, LocalMustNotAliasAnalysis> bodyToLMNAA;
   @SynchronizedBy("by use of synchronized LoadingCache class")
   protected final LoadingCache<Unit, Set<SootMethod>> unitToCallees;
   @SynchronizedBy("explicit lock on data structure")
   protected Map<SootMethod, Set<Unit>> methodToCallers;

   public OnTheFlyJimpleBasedICFG(SootMethod... entryPoints) {
      this((Collection)Arrays.asList(entryPoints));
   }

   public OnTheFlyJimpleBasedICFG(Collection<SootMethod> entryPoints) {
      this.bodyToLMNAA = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<Body, LocalMustNotAliasAnalysis>() {
         public LocalMustNotAliasAnalysis load(Body body) throws Exception {
            return new LocalMustNotAliasAnalysis(OnTheFlyJimpleBasedICFG.this.getOrCreateUnitGraph(body), body);
         }
      });
      this.unitToCallees = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<Unit, Set<SootMethod>>() {
         public Set<SootMethod> load(Unit u) throws Exception {
            Stmt stmt = (Stmt)u;
            InvokeExpr ie = stmt.getInvokeExpr();
            FastHierarchy fastHierarchy = Scene.v().getFastHierarchy();
            if (ie instanceof InstanceInvokeExpr) {
               if (ie instanceof SpecialInvokeExpr) {
                  return Collections.singleton(ie.getMethod());
               } else {
                  InstanceInvokeExpr iie = (InstanceInvokeExpr)ie;
                  Local base = (Local)iie.getBase();
                  RefType concreteType = ((LocalMustNotAliasAnalysis)OnTheFlyJimpleBasedICFG.this.bodyToLMNAA.getUnchecked(OnTheFlyJimpleBasedICFG.this.unitToOwner.get(u))).concreteType(base, stmt);
                  if (concreteType != null) {
                     SootMethod singleTargetMethod = fastHierarchy.resolveConcreteDispatch(concreteType.getSootClass(), iie.getMethod());
                     return Collections.singleton(singleTargetMethod);
                  } else {
                     SootClass baseTypeClass;
                     if (base.getType() instanceof RefType) {
                        RefType refType = (RefType)base.getType();
                        baseTypeClass = refType.getSootClass();
                     } else {
                        if (!(base.getType() instanceof ArrayType)) {
                           if (base.getType() instanceof NullType) {
                              return Collections.emptySet();
                           }

                           throw new InternalError("Unexpected base type:" + base.getType());
                        }

                        baseTypeClass = Scene.v().getSootClass("java.lang.Object");
                     }

                     return fastHierarchy.resolveAbstractDispatch(baseTypeClass, iie.getMethod());
                  }
               }
            } else {
               return Collections.singleton(ie.getMethod());
            }
         }
      });
      this.methodToCallers = new HashMap();
      Iterator var2 = entryPoints.iterator();

      while(var2.hasNext()) {
         SootMethod m = (SootMethod)var2.next();
         this.initForMethod(m);
      }

   }

   protected Body initForMethod(SootMethod m) {
      assert Scene.v().hasFastHierarchy();

      Body b = null;
      if (m.isConcrete()) {
         SootClass declaringClass = m.getDeclaringClass();
         this.ensureClassHasBodies(declaringClass);
         synchronized(Scene.v()) {
            b = m.retrieveActiveBody();
         }

         if (b != null) {
            Iterator var4 = b.getUnits().iterator();

            while(var4.hasNext()) {
               Unit u = (Unit)var4.next();
               if (this.unitToOwner.put(u, b) != null) {
                  break;
               }
            }
         }
      }

      assert Scene.v().hasFastHierarchy();

      return b;
   }

   private synchronized void ensureClassHasBodies(SootClass cl) {
      assert Scene.v().hasFastHierarchy();

      if (cl.resolvingLevel() < 3) {
         Scene.v().forceResolve(cl.getName(), 3);
         Scene.v().getOrMakeFastHierarchy();
      }

      assert Scene.v().hasFastHierarchy();

   }

   public Set<SootMethod> getCalleesOfCallAt(Unit u) {
      Set<SootMethod> targets = (Set)this.unitToCallees.getUnchecked(u);
      Iterator var3 = targets.iterator();

      while(var3.hasNext()) {
         SootMethod m = (SootMethod)var3.next();
         this.addCallerForMethod(u, m);
         this.initForMethod(m);
      }

      return targets;
   }

   private void addCallerForMethod(Unit callSite, SootMethod target) {
      synchronized(this.methodToCallers) {
         Set<Unit> callers = (Set)this.methodToCallers.get(target);
         if (callers == null) {
            callers = new HashSet();
            this.methodToCallers.put(target, callers);
         }

         ((Set)callers).add(callSite);
      }
   }

   public Set<Unit> getCallersOf(SootMethod m) {
      Set<Unit> callers = (Set)this.methodToCallers.get(m);
      return callers == null ? Collections.emptySet() : callers;
   }

   public static void loadAllClassesOnClassPathToSignatures() {
      Iterator var0 = SourceLocator.explodeClassPath(Scene.v().getSootClassPath()).iterator();

      while(var0.hasNext()) {
         String path = (String)var0.next();
         Iterator var2 = SourceLocator.v().getClassesUnder(path).iterator();

         while(var2.hasNext()) {
            String cl = (String)var2.next();
            Scene.v().forceResolve(cl, 2);
         }
      }

   }

   public static void main(String[] args) {
      PackManager.v().getPack("wjtp").add(new Transform("wjtp.onflyicfg", new SceneTransformer() {
         protected void internalTransform(String phaseName, Map<String, String> options) {
            if (Scene.v().hasCallGraph()) {
               throw new RuntimeException("call graph present!");
            } else {
               OnTheFlyJimpleBasedICFG.loadAllClassesOnClassPathToSignatures();
               SootMethod mainMethod = Scene.v().getMainMethod();
               OnTheFlyJimpleBasedICFG icfg = new OnTheFlyJimpleBasedICFG(new SootMethod[]{mainMethod});
               Set<SootMethod> worklist = new LinkedHashSet();
               Set<SootMethod> visited = new HashSet();
               worklist.add(mainMethod);
               int monomorphic = 0;
               int polymorphic = 0;

               label57:
               while(true) {
                  Body body;
                  do {
                     if (worklist.isEmpty()) {
                        return;
                     }

                     Iterator<SootMethod> iter = worklist.iterator();
                     SootMethod currMethod = (SootMethod)iter.next();
                     iter.remove();
                     visited.add(currMethod);
                     System.err.println(currMethod);
                     body = currMethod.getActiveBody();
                  } while(body == null);

                  Iterator var12 = body.getUnits().iterator();

                  while(true) {
                     Stmt s;
                     do {
                        if (!var12.hasNext()) {
                           continue label57;
                        }

                        Unit u = (Unit)var12.next();
                        s = (Stmt)u;
                     } while(!s.containsInvokeExpr());

                     Set<SootMethod> calleesOfCallAt = icfg.getCalleesOfCallAt((Unit)s);
                     if (s.getInvokeExpr() instanceof VirtualInvokeExpr || s.getInvokeExpr() instanceof InterfaceInvokeExpr) {
                        if (calleesOfCallAt.size() <= 1) {
                           ++monomorphic;
                        } else {
                           ++polymorphic;
                        }

                        System.err.println("mono: " + monomorphic + "   poly: " + polymorphic);
                     }

                     Iterator var16 = calleesOfCallAt.iterator();

                     while(var16.hasNext()) {
                        SootMethod callee = (SootMethod)var16.next();
                        if (!visited.contains(callee)) {
                           System.err.println(callee);
                        }
                     }
                  }
               }
            }
         }
      }));
      Options.v().set_on_the_fly(true);
      Main.main(args);
   }
}
