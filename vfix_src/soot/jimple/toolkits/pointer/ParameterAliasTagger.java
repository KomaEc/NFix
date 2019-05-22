package soot.jimple.toolkits.pointer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.RefLikeType;
import soot.Scene;
import soot.Singletons;
import soot.ValueBox;
import soot.jimple.IdentityStmt;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;
import soot.tagkit.ColorTag;

public class ParameterAliasTagger extends BodyTransformer {
   public ParameterAliasTagger(Singletons.Global g) {
   }

   public static ParameterAliasTagger v() {
      return G.v().soot_jimple_toolkits_pointer_ParameterAliasTagger();
   }

   protected void internalTransform(Body b, String phaseName, Map options) {
      PointsToAnalysis pa = Scene.v().getPointsToAnalysis();
      Set<IdentityStmt> parms = new HashSet();
      Iterator sIt = b.getUnits().iterator();

      while(sIt.hasNext()) {
         Stmt s = (Stmt)sIt.next();
         if (s instanceof IdentityStmt) {
            IdentityStmt is = (IdentityStmt)s;
            ValueBox vb = is.getRightOpBox();
            if (vb.getValue() instanceof ParameterRef) {
               ParameterRef pr = (ParameterRef)vb.getValue();
               if (pr.getType() instanceof RefLikeType) {
                  parms.add(is);
               }
            }
         }
      }

      int var11 = 0;

      while(!parms.isEmpty()) {
         this.fill(parms, (IdentityStmt)parms.iterator().next(), var11++, pa);
      }

   }

   private void fill(Set<IdentityStmt> parms, IdentityStmt parm, int colour, PointsToAnalysis pa) {
      if (parms.contains(parm)) {
         parm.getRightOpBox().addTag(new ColorTag(colour, "Parameter Alias"));
         parms.remove(parm);
         PointsToSet ps = pa.reachingObjects((Local)parm.getLeftOp());
         Iterator parm2It = (new LinkedList(parms)).iterator();

         while(parm2It.hasNext()) {
            IdentityStmt parm2 = (IdentityStmt)parm2It.next();
            if (ps.hasNonEmptyIntersection(pa.reachingObjects((Local)parm2.getLeftOp()))) {
               this.fill(parms, parm2, colour, pa);
            }
         }

      }
   }
}
