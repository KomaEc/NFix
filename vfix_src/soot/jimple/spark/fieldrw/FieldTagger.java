package soot.jimple.spark.fieldrw;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.PhaseOptions;
import soot.Scene;
import soot.Singletons;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.TransitiveTargets;
import soot.util.HashMultiMap;

public class FieldTagger extends BodyTransformer {
   private final HashSet<SootMethod> processedMethods = new HashSet();
   private final HashMultiMap methodToWrite = new HashMultiMap();
   private final HashMultiMap methodToRead = new HashMultiMap();

   public FieldTagger(Singletons.Global g) {
   }

   public static FieldTagger v() {
      return G.v().soot_jimple_spark_fieldrw_FieldTagger();
   }

   protected void ensureProcessed(SootMethod m) {
      if (!this.processedMethods.contains(m)) {
         this.processedMethods.add(m);
         if (m.isConcrete()) {
            if (!m.isPhantom()) {
               Iterator sIt = m.retrieveActiveBody().getUnits().iterator();

               while(sIt.hasNext()) {
                  Stmt s = (Stmt)sIt.next();
                  if (s instanceof AssignStmt) {
                     AssignStmt as = (AssignStmt)s;
                     Value l = as.getLeftOp();
                     if (l instanceof FieldRef) {
                        this.methodToWrite.put(m, ((FieldRef)l).getField());
                     }

                     Value r = as.getRightOp();
                     if (r instanceof FieldRef) {
                        this.methodToRead.put(m, ((FieldRef)r).getField());
                     }
                  }
               }

            }
         }
      }
   }

   protected void internalTransform(Body body, String phaseName, Map options) {
      int threshold = PhaseOptions.getInt(options, "threshold");
      this.ensureProcessed(body.getMethod());
      CallGraph cg = Scene.v().getCallGraph();
      TransitiveTargets tt = new TransitiveTargets(cg);
      Iterator sIt = body.getUnits().iterator();

      while(true) {
         label29:
         while(sIt.hasNext()) {
            Stmt s = (Stmt)sIt.next();
            HashSet read = new HashSet();
            HashSet write = new HashSet();
            Iterator it = tt.iterator((Unit)s);

            while(it.hasNext()) {
               SootMethod target = (SootMethod)it.next();
               this.ensureProcessed(target);
               if (target.isNative() || target.isPhantom()) {
                  continue label29;
               }

               read.addAll(this.methodToRead.get(target));
               write.addAll(this.methodToWrite.get(target));
               if (read.size() + write.size() > threshold) {
                  continue label29;
               }
            }

            s.addTag(new FieldReadTag(read));
            s.addTag(new FieldWriteTag(write));
         }

         return;
      }
   }
}
