package soot.jimple.toolkits.pointer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.PhaseOptions;
import soot.Scene;
import soot.Singletons;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;

public class SideEffectTagger extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(SideEffectTagger.class);
   public int numRWs = 0;
   public int numWRs = 0;
   public int numRRs = 0;
   public int numWWs = 0;
   public int numNatives = 0;
   public Date startTime = null;
   boolean optionNaive = false;
   private CallGraph cg;

   public SideEffectTagger(Singletons.Global g) {
   }

   public static SideEffectTagger v() {
      return G.v().soot_jimple_toolkits_pointer_SideEffectTagger();
   }

   protected void initializationStuff(String phaseName) {
      G.v().Union_factory = new UnionFactory() {
         public Union newUnion() {
            return new MemoryEfficientRasUnion();
         }
      };
      if (this.startTime == null) {
         this.startTime = new Date();
      }

      this.cg = Scene.v().getCallGraph();
   }

   protected Object keyFor(Stmt s) {
      if (!s.containsInvokeExpr()) {
         return s;
      } else if (this.optionNaive) {
         throw new RuntimeException("shouldn't get here");
      } else {
         Iterator it = this.cg.edgesOutOf((Unit)s);
         if (!it.hasNext()) {
            return Collections.EMPTY_LIST;
         } else {
            ArrayList ret = new ArrayList();

            while(it.hasNext()) {
               ret.add(it.next());
            }

            return ret;
         }
      }
   }

   protected void internalTransform(Body body, String phaseName, Map options) {
      this.initializationStuff(phaseName);
      SideEffectAnalysis sea = Scene.v().getSideEffectAnalysis();
      this.optionNaive = PhaseOptions.getBoolean(options, "naive");
      if (!this.optionNaive) {
         sea.findNTRWSets(body.getMethod());
      }

      HashMap<Object, RWSet> stmtToReadSet = new HashMap();
      HashMap<Object, RWSet> stmtToWriteSet = new HashMap();
      SideEffectTagger.UniqueRWSets sets = new SideEffectTagger.UniqueRWSets();
      boolean justDoTotallyConservativeThing = body.getMethod().getName().equals("<clinit>");
      Iterator stmtIt = body.getUnits().iterator();

      while(true) {
         while(stmtIt.hasNext()) {
            Stmt stmt = (Stmt)stmtIt.next();
            if (!justDoTotallyConservativeThing && (!this.optionNaive || !stmt.containsInvokeExpr())) {
               Object key = this.keyFor(stmt);
               if (!stmtToReadSet.containsKey(key)) {
                  stmtToReadSet.put(key, sets.getUnique(sea.readSet(body.getMethod(), stmt)));
                  stmtToWriteSet.put(key, sets.getUnique(sea.writeSet(body.getMethod(), stmt)));
               }
            } else {
               stmtToReadSet.put(stmt, sets.getUnique(new FullRWSet()));
               stmtToWriteSet.put(stmt, sets.getUnique(new FullRWSet()));
            }
         }

         DependenceGraph graph = new DependenceGraph();
         Iterator stmtIt = sets.iterator();

         RWSet read;
         while(stmtIt.hasNext()) {
            RWSet outer = (RWSet)stmtIt.next();
            Iterator innerIt = sets.iterator();

            while(innerIt.hasNext()) {
               read = (RWSet)innerIt.next();
               if (read == outer) {
                  break;
               }

               if (outer.hasNonEmptyIntersection(read)) {
                  graph.addEdge(sets.indexOf(outer), sets.indexOf(read));
               }
            }
         }

         body.getMethod().addTag(graph);
         stmtIt = body.getUnits().iterator();

         while(true) {
            RWSet write;
            Stmt stmt;
            do {
               if (!stmtIt.hasNext()) {
                  return;
               }

               stmt = (Stmt)stmtIt.next();
               Object key;
               if (this.optionNaive && stmt.containsInvokeExpr()) {
                  key = stmt;
               } else {
                  key = this.keyFor(stmt);
               }

               read = (RWSet)stmtToReadSet.get(key);
               write = (RWSet)stmtToWriteSet.get(key);
            } while(read == null && write == null);

            DependenceTag tag = new DependenceTag();
            if (read != null && read.getCallsNative()) {
               tag.setCallsNative();
               ++this.numNatives;
            } else if (write != null && write.getCallsNative()) {
               tag.setCallsNative();
               ++this.numNatives;
            }

            tag.setRead(sets.indexOf(read));
            tag.setWrite(sets.indexOf(write));
            stmt.addTag(tag);
         }
      }
   }

   protected class UniqueRWSets {
      protected ArrayList<RWSet> l = new ArrayList();

      RWSet getUnique(RWSet s) {
         if (s == null) {
            return s;
         } else {
            Iterator var2 = this.l.iterator();

            RWSet ret;
            do {
               if (!var2.hasNext()) {
                  this.l.add(s);
                  return s;
               }

               ret = (RWSet)var2.next();
            } while(!ret.isEquivTo(s));

            return ret;
         }
      }

      Iterator<RWSet> iterator() {
         return this.l.iterator();
      }

      short indexOf(RWSet s) {
         short i = 0;

         for(Iterator var3 = this.l.iterator(); var3.hasNext(); ++i) {
            RWSet ret = (RWSet)var3.next();
            if (ret.isEquivTo(s)) {
               return i;
            }
         }

         return -1;
      }
   }
}
