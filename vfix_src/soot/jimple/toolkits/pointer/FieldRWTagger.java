package soot.jimple.toolkits.pointer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.PhaseOptions;
import soot.Scene;
import soot.Singletons;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;

public class FieldRWTagger extends BodyTransformer {
   public int numRWs = 0;
   public int numWRs = 0;
   public int numRRs = 0;
   public int numWWs = 0;
   public int numNatives = 0;
   public Date startTime = null;
   boolean optionDontTag = false;
   boolean optionNaive = false;
   private CallGraph cg;

   public FieldRWTagger(Singletons.Global g) {
   }

   public static FieldRWTagger v() {
      return G.v().soot_jimple_toolkits_pointer_FieldRWTagger();
   }

   protected void initializationStuff(String phaseName) {
      if (G.v().Union_factory == null) {
         G.v().Union_factory = new UnionFactory() {
            public Union newUnion() {
               return FullObjectSet.v();
            }
         };
      }

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
      SideEffectAnalysis sea = new SideEffectAnalysis(DumbPointerAnalysis.v(), Scene.v().getCallGraph());
      sea.findNTRWSets(body.getMethod());
      HashMap<Object, RWSet> stmtToReadSet = new HashMap();
      HashMap<Object, RWSet> stmtToWriteSet = new HashMap();
      FieldRWTagger.UniqueRWSets sets = new FieldRWTagger.UniqueRWSets();
      this.optionDontTag = PhaseOptions.getBoolean(options, "dont-tag");
      boolean justDoTotallyConservativeThing = body.getMethod().getName().equals("<clinit>");
      Iterator stmtIt = body.getUnits().iterator();

      while(stmtIt.hasNext()) {
         Stmt stmt = (Stmt)stmtIt.next();
         if (stmt.containsInvokeExpr()) {
            if (justDoTotallyConservativeThing) {
               stmtToReadSet.put(stmt, sets.getUnique(new FullRWSet()));
               stmtToWriteSet.put(stmt, sets.getUnique(new FullRWSet()));
            } else {
               Object key = this.keyFor(stmt);
               if (!stmtToReadSet.containsKey(key)) {
                  stmtToReadSet.put(key, sets.getUnique(sea.readSet(body.getMethod(), stmt)));
                  stmtToWriteSet.put(key, sets.getUnique(sea.writeSet(body.getMethod(), stmt)));
               }
            }
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
