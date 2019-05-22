package soot.toolkits.graph;

import java.util.Iterator;
import java.util.Set;
import soot.Body;
import soot.Unit;
import soot.baf.Inst;
import soot.jimple.Stmt;
import soot.util.PhaseDumper;

public class ArrayRefBlockGraph extends BlockGraph {
   public ArrayRefBlockGraph(Body body) {
      this(new BriefUnitGraph(body));
   }

   public ArrayRefBlockGraph(BriefUnitGraph unitGraph) {
      super(unitGraph);
      PhaseDumper.v().dumpGraph(this, this.mBody);
   }

   protected Set<Unit> computeLeaders(UnitGraph unitGraph) {
      Body body = unitGraph.getBody();
      if (body != this.mBody) {
         throw new RuntimeException("ArrayRefBlockGraph.computeLeaders() called with a UnitGraph that doesn't match its mBody.");
      } else {
         Set<Unit> leaders = super.computeLeaders(unitGraph);
         Iterator it = body.getUnits().iterator();

         while(true) {
            Unit unit;
            do {
               if (!it.hasNext()) {
                  return leaders;
               }

               unit = (Unit)it.next();
            } while((!(unit instanceof Stmt) || !((Stmt)unit).containsArrayRef()) && (!(unit instanceof Inst) || !((Inst)unit).containsArrayRef()));

            leaders.add(unit);
         }
      }
   }
}
