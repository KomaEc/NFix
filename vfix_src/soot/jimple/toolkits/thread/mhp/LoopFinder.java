package soot.jimple.toolkits.thread.mhp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.tagkit.Tag;
import soot.util.Chain;

public class LoopFinder {
   private final Map<Chain, Set<Set<Object>>> chainToLoop = new HashMap();

   LoopFinder(PegGraph peg) {
      Chain chain = peg.getMainPegChain();
      DfsForBackEdge dfsForBackEdge = new DfsForBackEdge(chain, peg);
      Map<Object, Object> backEdges = dfsForBackEdge.getBackEdges();
      LoopBodyFinder lbf = new LoopBodyFinder(backEdges, peg);
      Set<Set<Object>> loopBody = lbf.getLoopBody();
      this.testLoops(loopBody);
      this.chainToLoop.put(chain, loopBody);
   }

   private void testLoops(Set<Set<Object>> loopBody) {
      System.out.println("====loops===");
      Iterator it = loopBody.iterator();

      while(it.hasNext()) {
         Set loop = (Set)it.next();
         Iterator loopIt = loop.iterator();
         System.out.println("---loop---");

         while(loopIt.hasNext()) {
            JPegStmt o = (JPegStmt)loopIt.next();
            Tag tag = (Tag)o.getTags().get(0);
            System.out.println(tag + " " + o);
         }
      }

      System.out.println("===end===loops===");
   }
}
