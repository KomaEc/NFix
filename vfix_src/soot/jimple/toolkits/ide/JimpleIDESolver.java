package soot.jimple.toolkits.ide;

import com.google.common.collect.Table;
import heros.IDETabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IDESolver;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;

public class JimpleIDESolver<D, V, I extends InterproceduralCFG<Unit, SootMethod>> extends IDESolver<Unit, D, SootMethod, V, I> {
   private static final Logger logger = LoggerFactory.getLogger(JimpleIDESolver.class);
   private final boolean DUMP_RESULTS;

   public JimpleIDESolver(IDETabulationProblem<Unit, D, SootMethod, V, I> problem) {
      this(problem, false);
   }

   public JimpleIDESolver(IDETabulationProblem<Unit, D, SootMethod, V, I> problem, boolean dumpResults) {
      super(problem);
      this.DUMP_RESULTS = dumpResults;
   }

   public void solve() {
      super.solve();
      if (this.DUMP_RESULTS) {
         this.dumpResults();
      }

   }

   public void dumpResults() {
      try {
         PrintWriter out = new PrintWriter(new FileOutputStream("ideSolverDump" + System.currentTimeMillis() + ".csv"));
         List<String> res = new ArrayList();
         Iterator var3 = this.val.cellSet().iterator();

         while(var3.hasNext()) {
            Table.Cell<Unit, D, V> entry = (Table.Cell)var3.next();
            SootMethod methodOf = (SootMethod)this.icfg.getMethodOf(entry.getRowKey());
            PatchingChain<Unit> units = methodOf.getActiveBody().getUnits();
            int i = 0;
            Iterator var8 = units.iterator();

            while(true) {
               if (var8.hasNext()) {
                  Unit unit = (Unit)var8.next();
                  if (unit != entry.getRowKey()) {
                     ++i;
                     continue;
                  }
               }

               res.add(methodOf + ";" + entry.getRowKey() + "@" + i + ";" + entry.getColumnKey() + ";" + entry.getValue());
               break;
            }
         }

         Collections.sort(res);
         var3 = res.iterator();

         while(var3.hasNext()) {
            String string = (String)var3.next();
            out.println(string);
         }

         out.flush();
         out.close();
      } catch (FileNotFoundException var10) {
         logger.error((String)var10.getMessage(), (Throwable)var10);
      }

   }
}
