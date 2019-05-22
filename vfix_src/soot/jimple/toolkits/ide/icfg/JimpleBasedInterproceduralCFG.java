package soot.jimple.toolkits.ide.icfg;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import heros.DontSynchronize;
import heros.SynchronizedBy;
import heros.ThreadSafe;
import heros.solver.IDESolver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.MethodOrMethodContext;
import soot.PatchingChain;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.EdgePredicate;
import soot.jimple.toolkits.callgraph.Filter;
import soot.util.queue.QueueReader;

@ThreadSafe
public class JimpleBasedInterproceduralCFG extends AbstractJimpleBasedICFG {
   protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);
   protected boolean includeReflectiveCalls;
   protected boolean includePhantomCallees;
   @DontSynchronize("readonly")
   protected final CallGraph cg;
   protected CacheLoader<Unit, Collection<SootMethod>> loaderUnitToCallees;
   @SynchronizedBy("by use of synchronized LoadingCache class")
   protected final LoadingCache<Unit, Collection<SootMethod>> unitToCallees;
   protected CacheLoader<SootMethod, Collection<Unit>> loaderMethodToCallers;
   @SynchronizedBy("by use of synchronized LoadingCache class")
   protected final LoadingCache<SootMethod, Collection<Unit>> methodToCallers;

   public JimpleBasedInterproceduralCFG() {
      this(true);
   }

   public JimpleBasedInterproceduralCFG(boolean enableExceptions) {
      this(enableExceptions, false);
   }

   public JimpleBasedInterproceduralCFG(boolean enableExceptions, boolean includeReflectiveCalls) {
      super(enableExceptions);
      this.includeReflectiveCalls = false;
      this.includePhantomCallees = false;
      this.loaderUnitToCallees = new CacheLoader<Unit, Collection<SootMethod>>() {
         public Collection<SootMethod> load(Unit u) throws Exception {
            ArrayList<SootMethod> res = null;
            Iterator edgeIter = (JimpleBasedInterproceduralCFG.this.new EdgeFilter()).wrap(JimpleBasedInterproceduralCFG.this.cg.edgesOutOf(u));

            while(true) {
               while(edgeIter.hasNext()) {
                  Edge edge = (Edge)edgeIter.next();
                  SootMethod m = edge.getTgt().method();
                  if (!JimpleBasedInterproceduralCFG.this.includePhantomCallees && !m.hasActiveBody()) {
                     if (IDESolver.DEBUG) {
                        JimpleBasedInterproceduralCFG.logger.error(String.format("Method %s is referenced but has no body!", m.getSignature(), new Exception()));
                     }
                  } else {
                     if (res == null) {
                        res = new ArrayList();
                     }

                     res.add(m);
                  }
               }

               if (res != null) {
                  res.trimToSize();
                  return res;
               }

               return Collections.emptySet();
            }
         }
      };
      this.unitToCallees = IDESolver.DEFAULT_CACHE_BUILDER.build(this.loaderUnitToCallees);
      this.loaderMethodToCallers = new CacheLoader<SootMethod, Collection<Unit>>() {
         public Collection<Unit> load(SootMethod m) throws Exception {
            ArrayList<Unit> res = new ArrayList();
            Iterator edgeIter = (JimpleBasedInterproceduralCFG.this.new EdgeFilter()).wrap(JimpleBasedInterproceduralCFG.this.cg.edgesInto(m));

            while(edgeIter.hasNext()) {
               Edge edge = (Edge)edgeIter.next();
               res.add(edge.srcUnit());
            }

            res.trimToSize();
            return res;
         }
      };
      this.methodToCallers = IDESolver.DEFAULT_CACHE_BUILDER.build(this.loaderMethodToCallers);
      this.includeReflectiveCalls = includeReflectiveCalls;
      this.cg = Scene.v().getCallGraph();
      this.initializeUnitToOwner();
   }

   protected void initializeUnitToOwner() {
      QueueReader iter = Scene.v().getReachableMethods().listener();

      while(iter.hasNext()) {
         SootMethod m = ((MethodOrMethodContext)iter.next()).method();
         this.initializeUnitToOwner(m);
      }

   }

   public void initializeUnitToOwner(SootMethod m) {
      if (m.hasActiveBody()) {
         Body b = m.getActiveBody();
         PatchingChain<Unit> units = b.getUnits();
         Iterator var4 = units.iterator();

         while(var4.hasNext()) {
            Unit unit = (Unit)var4.next();
            this.unitToOwner.put(unit, b);
         }
      }

   }

   public Collection<SootMethod> getCalleesOfCallAt(Unit u) {
      return (Collection)this.unitToCallees.getUnchecked(u);
   }

   public Collection<Unit> getCallersOf(SootMethod m) {
      return (Collection)this.methodToCallers.getUnchecked(m);
   }

   public void setIncludePhantomCallees(boolean includePhantomCallees) {
      this.includePhantomCallees = includePhantomCallees;
   }

   public class EdgeFilter extends Filter {
      protected EdgeFilter() {
         super(new EdgePredicate() {
            public boolean want(Edge e) {
               return e.kind().isExplicit() || e.kind().isThread() || e.kind().isExecutor() || e.kind().isAsyncTask() || e.kind().isClinit() || e.kind().isPrivileged() || JimpleBasedInterproceduralCFG.this.includeReflectiveCalls && e.kind().isReflection();
            }
         });
      }
   }
}
