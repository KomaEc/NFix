package corg.vfix.sa.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.jimple.Stmt;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.jimple.toolkits.annotation.logic.LoopFinder;

public class JimpleLoopFinder {
   private static Collection<Loop> loops;
   private static Body mtdBody;

   public static boolean inLoop(Body body, Stmt stmt) {
      if (body.equals(mtdBody)) {
         return inLoop(stmt);
      } else {
         mtdBody = body;
         LoopFinder lf = new LoopFinder();
         lf.transform(body);
         loops = lf.loops();
         return inLoop(stmt);
      }
   }

   public static Loop getMinLoop(Stmt stmt) {
      Loop candidate = null;
      Iterator var3 = loops.iterator();

      while(var3.hasNext()) {
         Loop loop = (Loop)var3.next();
         if (loop.getLoopStatements().contains(stmt)) {
            if (candidate == null) {
               candidate = loop;
            } else if (candidate.getLoopStatements().size() > loop.getLoopStatements().size()) {
               candidate = loop;
            }
         }
      }

      return candidate;
   }

   public static ArrayList<Integer> getMinLoopRange(Stmt stmt) {
      Loop loop = getMinLoop(stmt);
      return loop == null ? null : getLoopRange(loop.getLoopStatements());
   }

   public static boolean inLoop(Stmt stmt) {
      Iterator var2 = loops.iterator();

      while(var2.hasNext()) {
         Loop loop = (Loop)var2.next();
         if (loop.getLoopStatements().contains(stmt)) {
            return true;
         }
      }

      return false;
   }

   public static ArrayList<Integer> getLoopRange(List<Stmt> stmts) {
      ArrayList<Integer> range = new ArrayList();
      Iterator var3 = stmts.iterator();

      while(var3.hasNext()) {
         Stmt stmt = (Stmt)var3.next();
         int line = stmt.getJavaSourceStartLineNumber();
         if (!range.contains(line)) {
            range.add(line);
         }
      }

      range.sort(new Comparator<Integer>() {
         public int compare(Integer o1, Integer o2) {
            return o1 - o2;
         }
      });
      return range;
   }
}
