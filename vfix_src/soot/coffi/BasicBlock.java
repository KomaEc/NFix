package soot.coffi;

import java.util.List;
import java.util.Set;
import java.util.Vector;
import soot.G;
import soot.jimple.Stmt;
import soot.util.ArraySet;

class BasicBlock {
   public int size;
   public Instruction head;
   public Instruction tail;
   public Vector<BasicBlock> succ;
   public Vector<BasicBlock> pred;
   public boolean inq;
   public boolean beginException;
   public boolean beginCode;
   boolean done;
   public BasicBlock next;
   public long id;
   List<Stmt> statements;
   Set addressesToFixup = new ArraySet();

   Stmt getHeadJStmt() {
      return (Stmt)this.statements.get(0);
   }

   Stmt getTailJStmt() {
      return (Stmt)this.statements.get(this.statements.size() - 1);
   }

   public BasicBlock(Instruction insts) {
      this.id = (long)(G.v().coffi_BasicBlock_ids++);
      this.head = insts;
      this.tail = this.head;
      this.size = 0;
      if (this.head != null) {
         ++this.size;

         while(this.tail.next != null) {
            ++this.size;
            this.tail = this.tail.next;
         }
      }

      this.succ = new Vector(2, 10);
      this.pred = new Vector(2, 3);
   }

   public BasicBlock(Instruction headinsn, Instruction tailinsn) {
      this.id = (long)(G.v().coffi_BasicBlock_ids++);
      this.head = headinsn;
      this.tail = tailinsn;
      this.succ = new Vector(2, 10);
      this.pred = new Vector(2, 3);
   }

   public int hashCode() {
      return (new Integer(this.head.label)).hashCode();
   }

   public boolean equals(BasicBlock b) {
      return this == b;
   }

   public String toString() {
      return "BB: " + this.id;
   }
}
