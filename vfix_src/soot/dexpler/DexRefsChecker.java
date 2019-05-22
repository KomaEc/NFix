package soot.dexpler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.Type;
import soot.Unit;
import soot.jimple.FieldRef;
import soot.jimple.Stmt;

public class DexRefsChecker extends DexTransformer {
   Local l = null;

   public static DexRefsChecker v() {
      return new DexRefsChecker();
   }

   protected void internalTransform(Body body, String phaseName, Map options) {
      Iterator var4 = this.getRefCandidates(body).iterator();

      while(var4.hasNext()) {
         Unit u = (Unit)var4.next();
         Stmt s = (Stmt)u;
         boolean hasField = false;
         FieldRef fr = null;
         SootField sf = null;
         if (!s.containsFieldRef()) {
            throw new RuntimeException("Unit '" + u + "' does not contain array ref nor field ref.");
         }

         fr = s.getFieldRef();
         sf = fr.getField();
         if (sf != null) {
            hasField = true;
         }

         if (!hasField) {
            System.out.println("Warning: add missing field '" + fr + "' to class!");
            SootClass sc = null;
            String frStr = fr.toString();
            if (frStr.contains(".<")) {
               sc = Scene.v().getSootClass(frStr.split(".<")[1].split(" ")[0].split(":")[0]);
            } else {
               sc = Scene.v().getSootClass(frStr.split(":")[0].replaceAll("^<", ""));
            }

            String fname = fr.toString().split(">")[0].split(" ")[2];
            int modifiers = 1;
            Type ftype = fr.getType();
            sc.addField(Scene.v().makeSootField(fname, ftype, modifiers));
         }
      }

   }

   private Set<Unit> getRefCandidates(Body body) {
      Set<Unit> candidates = new HashSet();
      Iterator i = body.getUnits().iterator();

      while(i.hasNext()) {
         Unit u = (Unit)i.next();
         Stmt s = (Stmt)u;
         if (s.containsFieldRef()) {
            candidates.add(u);
         }
      }

      return candidates;
   }
}
