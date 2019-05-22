package soot.coffi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class Instruction_branch extends Instruction {
   private static final Logger logger = LoggerFactory.getLogger(Instruction_branch.class);
   public int arg_i;
   public Instruction target;

   public Instruction_branch(byte c) {
      super(c);
      this.branches = true;
   }

   public String toString(cp_info[] constant_pool) {
      return super.toString(constant_pool) + " " + "[label_" + Integer.toString(this.target.label) + "]";
   }

   public void offsetToPointer(ByteCode bc) {
      this.target = bc.locateInst(this.arg_i + this.label);
      if (this.target == null) {
         logger.warn("can't locate target of instruction");
         logger.debug(" which should be at byte address " + (this.label + this.arg_i));
      } else {
         this.target.labelled = true;
      }

   }

   public Instruction[] branchpoints(Instruction next) {
      Instruction[] i = new Instruction[]{this.target, next};
      return i;
   }

   public String toString() {
      return super.toString() + "\t" + this.target.label;
   }
}
