package corg.vfix.pg.jimple;

import corg.vfix.pg.jimple.init.RuleInitOne;
import corg.vfix.pg.jimple.init.RuleInitThree;
import corg.vfix.pg.jimple.init.RuleInitTwo;
import corg.vfix.pg.jimple.skip.RuleSkipOne;
import corg.vfix.pg.jimple.skip.RuleSkipThree;
import corg.vfix.pg.jimple.skip.RuleSkipTwo;
import corg.vfix.sa.vfg.VFGNode;
import java.util.ArrayList;

public class OperationRules {
   public static void filter(VFGNode node) {
      ArrayList<Integer> ops = new ArrayList();
      if (RuleSkipOne.solve(node)) {
         ops.add(21);
      }

      if (RuleSkipTwo.solve(node)) {
         ops.add(22);
      }

      if (RuleSkipThree.solve(node)) {
         ops.add(23);
      }

      if (RuleInitOne.solve(node)) {
         ops.add(11);
      }

      if (RuleInitTwo.solve(node)) {
         ops.add(12);
      }

      if (RuleInitThree.solve(node)) {
         ops.add(13);
      }

      node.setOperations(ops);
   }
}
