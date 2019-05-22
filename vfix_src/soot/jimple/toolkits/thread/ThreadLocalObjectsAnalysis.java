package soot.jimple.toolkits.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.EquivalentValue;
import soot.SootClass;
import soot.SootMethod;
import soot.Value;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.Ref;
import soot.jimple.toolkits.infoflow.CallLocalityContext;
import soot.jimple.toolkits.infoflow.ClassInfoFlowAnalysis;
import soot.jimple.toolkits.infoflow.ClassLocalObjectsAnalysis;
import soot.jimple.toolkits.infoflow.InfoFlowAnalysis;
import soot.jimple.toolkits.infoflow.LocalObjectsAnalysis;
import soot.jimple.toolkits.infoflow.SmartMethodInfoFlowAnalysis;
import soot.jimple.toolkits.infoflow.SmartMethodLocalObjectsAnalysis;
import soot.jimple.toolkits.infoflow.UseFinder;
import soot.jimple.toolkits.thread.mhp.MhpTester;

public class ThreadLocalObjectsAnalysis extends LocalObjectsAnalysis implements IThreadLocalObjectsAnalysis {
   private static final Logger logger = LoggerFactory.getLogger(ThreadLocalObjectsAnalysis.class);
   MhpTester mhp;
   List<AbstractRuntimeThread> threads;
   InfoFlowAnalysis primitiveDfa;
   static boolean printDebug = false;
   Map valueCache;
   Map fieldCache;
   Map invokeCache;

   public ThreadLocalObjectsAnalysis(MhpTester mhp) {
      super(new InfoFlowAnalysis(false, true, printDebug));
      this.mhp = mhp;
      this.threads = mhp.getThreads();
      this.primitiveDfa = new InfoFlowAnalysis(true, true, printDebug);
      this.valueCache = new HashMap();
      this.fieldCache = new HashMap();
      this.invokeCache = new HashMap();
   }

   public void precompute() {
      Iterator var1 = this.threads.iterator();

      while(var1.hasNext()) {
         AbstractRuntimeThread thread = (AbstractRuntimeThread)var1.next();
         Iterator var3 = thread.getRunMethods().iterator();

         while(var3.hasNext()) {
            Object item = var3.next();
            SootMethod runMethod = (SootMethod)item;
            if (runMethod.getDeclaringClass().isApplicationClass()) {
               this.getClassLocalObjectsAnalysis(runMethod.getDeclaringClass());
            }
         }
      }

   }

   protected ClassLocalObjectsAnalysis newClassLocalObjectsAnalysis(LocalObjectsAnalysis loa, InfoFlowAnalysis dfa, UseFinder uf, SootClass sc) {
      List<SootMethod> runMethods = new ArrayList();
      Iterator threadsIt = this.threads.iterator();

      while(threadsIt.hasNext()) {
         AbstractRuntimeThread thread = (AbstractRuntimeThread)threadsIt.next();
         Iterator runMethodsIt = thread.getRunMethods().iterator();

         while(runMethodsIt.hasNext()) {
            SootMethod runMethod = (SootMethod)runMethodsIt.next();
            if (runMethod.getDeclaringClass() == sc) {
               runMethods.add(runMethod);
            }
         }
      }

      return new ClassLocalObjectsAnalysis(loa, dfa, this.primitiveDfa, uf, sc, runMethods);
   }

   public boolean isObjectThreadLocal(Value localOrRef, SootMethod sm) {
      if (this.threads.size() <= 1) {
         return true;
      } else {
         if (printDebug) {
            logger.debug("- " + localOrRef + " in " + sm + " is...");
         }

         Collection<AbstractRuntimeThread> mhpThreads = this.mhp.getThreads();
         if (mhpThreads != null) {
            Iterator var4 = mhpThreads.iterator();

            while(var4.hasNext()) {
               AbstractRuntimeThread thread = (AbstractRuntimeThread)var4.next();
               Iterator var6 = thread.getRunMethods().iterator();

               while(var6.hasNext()) {
                  Object meth = var6.next();
                  SootMethod runMethod = (SootMethod)meth;
                  if (runMethod.getDeclaringClass().isApplicationClass() && !this.isObjectLocalToContext(localOrRef, sm, runMethod)) {
                     if (printDebug) {
                        logger.debug("  THREAD-SHARED (simpledfa " + ClassInfoFlowAnalysis.methodCount + " smartdfa " + SmartMethodInfoFlowAnalysis.counter + " smartloa " + SmartMethodLocalObjectsAnalysis.counter + ")");
                     }

                     return false;
                  }
               }
            }
         }

         if (printDebug) {
            logger.debug("  THREAD-LOCAL (simpledfa " + ClassInfoFlowAnalysis.methodCount + " smartdfa " + SmartMethodInfoFlowAnalysis.counter + " smartloa " + SmartMethodLocalObjectsAnalysis.counter + ")");
         }

         return true;
      }
   }

   public boolean hasNonThreadLocalEffects(SootMethod containingMethod, InvokeExpr ie) {
      return this.threads.size() <= 1 ? true : true;
   }

   public List escapesThrough(Value sharedValue, SootMethod containingMethod) {
      List ret = new ArrayList();
      Iterator var4 = this.mhp.getThreads().iterator();

      label48:
      while(var4.hasNext()) {
         AbstractRuntimeThread thread = (AbstractRuntimeThread)var4.next();
         Iterator var6 = thread.getRunMethods().iterator();

         while(true) {
            SootMethod runMethod;
            do {
               do {
                  if (!var6.hasNext()) {
                     continue label48;
                  }

                  Object meth = var6.next();
                  runMethod = (SootMethod)meth;
               } while(!runMethod.getDeclaringClass().isApplicationClass());
            } while(this.isObjectLocalToContext(sharedValue, containingMethod, runMethod));

            ClassLocalObjectsAnalysis cloa = this.getClassLocalObjectsAnalysis(containingMethod.getDeclaringClass());
            CallLocalityContext clc = cloa.getMergedContext(containingMethod);
            SmartMethodInfoFlowAnalysis smifa = this.dfa.getMethodInfoFlowAnalysis(containingMethod);
            EquivalentValue sharedValueEqVal;
            if (sharedValue instanceof InstanceFieldRef) {
               sharedValueEqVal = InfoFlowAnalysis.getNodeForFieldRef(containingMethod, ((FieldRef)sharedValue).getField());
            } else {
               sharedValueEqVal = new EquivalentValue(sharedValue);
            }

            List<EquivalentValue> sources = smifa.sourcesOf(sharedValueEqVal);
            Iterator var14 = sources.iterator();

            while(var14.hasNext()) {
               EquivalentValue source = (EquivalentValue)var14.next();
               if (source.getValue() instanceof Ref && clc != null && !clc.isFieldLocal(source)) {
                  ret.add(source);
               }
            }
         }
      }

      return ret;
   }
}
