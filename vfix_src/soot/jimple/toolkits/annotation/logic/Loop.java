package soot.jimple.toolkits.annotation.logic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import soot.Unit;
import soot.jimple.Stmt;
import soot.toolkits.graph.UnitGraph;

public class Loop {
   protected final Stmt header;
   protected final Stmt backJump;
   protected final List<Stmt> loopStatements;
   protected final UnitGraph g;
   protected Collection<Stmt> loopExits;

   Loop(Stmt head, List<Stmt> loopStatements, UnitGraph g) {
      this.header = head;
      this.g = g;
      loopStatements.remove(head);
      loopStatements.add(0, head);
      this.backJump = (Stmt)loopStatements.get(loopStatements.size() - 1);

      assert g.getSuccsOf((Unit)this.backJump).contains(head);

      this.loopStatements = loopStatements;
   }

   public Stmt getHead() {
      return this.header;
   }

   public Stmt getBackJumpStmt() {
      return this.backJump;
   }

   public List<Stmt> getLoopStatements() {
      return this.loopStatements;
   }

   public Collection<Stmt> getLoopExits() {
      if (this.loopExits == null) {
         this.loopExits = new HashSet();
         Iterator var1 = this.loopStatements.iterator();

         while(var1.hasNext()) {
            Stmt s = (Stmt)var1.next();
            Iterator var3 = this.g.getSuccsOf((Unit)s).iterator();

            while(var3.hasNext()) {
               Unit succ = (Unit)var3.next();
               if (!this.loopStatements.contains(succ)) {
                  this.loopExits.add(s);
               }
            }
         }
      }

      return this.loopExits;
   }

   public Collection<Stmt> targetsOfLoopExit(Stmt loopExit) {
      assert this.getLoopExits().contains(loopExit);

      List<Unit> succs = this.g.getSuccsOf((Unit)loopExit);
      Collection<Stmt> res = new HashSet();
      Iterator var4 = succs.iterator();

      while(var4.hasNext()) {
         Unit u = (Unit)var4.next();
         Stmt s = (Stmt)u;
         res.add(s);
      }

      res.removeAll(this.loopStatements);
      return res;
   }

   public boolean loopsForever() {
      return this.getLoopExits().isEmpty();
   }

   public boolean hasSingleExit() {
      return this.getLoopExits().size() == 1;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.header == null ? 0 : this.header.hashCode());
      result = 31 * result + (this.loopStatements == null ? 0 : this.loopStatements.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         Loop other = (Loop)obj;
         if (this.header == null) {
            if (other.header != null) {
               return false;
            }
         } else if (!this.header.equals(other.header)) {
            return false;
         }

         if (this.loopStatements == null) {
            if (other.loopStatements != null) {
               return false;
            }
         } else if (!this.loopStatements.equals(other.loopStatements)) {
            return false;
         }

         return true;
      }
   }
}
