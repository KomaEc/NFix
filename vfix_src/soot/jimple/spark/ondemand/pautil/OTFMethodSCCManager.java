package soot.jimple.spark.ondemand.pautil;

import java.util.Iterator;
import java.util.Set;
import soot.Scene;
import soot.SootMethod;
import soot.jimple.spark.ondemand.genericutil.DisjointSets;

public final class OTFMethodSCCManager {
   private DisjointSets disj;

   public OTFMethodSCCManager() {
      int size = Scene.v().getMethodNumberer().size();
      this.disj = new DisjointSets(size + 1);
   }

   public boolean inSameSCC(SootMethod m1, SootMethod m2) {
      return this.disj.find(m1.getNumber()) == this.disj.find(m2.getNumber());
   }

   public void makeSameSCC(Set<SootMethod> methods) {
      SootMethod prevMethod = null;

      SootMethod method;
      for(Iterator var3 = methods.iterator(); var3.hasNext(); prevMethod = method) {
         method = (SootMethod)var3.next();
         if (prevMethod != null) {
            int prevMethodRep = this.disj.find(prevMethod.getNumber());
            int methodRep = this.disj.find(method.getNumber());
            if (prevMethodRep != methodRep) {
               this.disj.union(prevMethodRep, methodRep);
            }
         }
      }

   }
}
