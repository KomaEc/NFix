package soot.coffi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Instruction_Tableswitch extends Instruction {
   private static final Logger logger = LoggerFactory.getLogger(Instruction_Tableswitch.class);
   public byte pad;
   public int default_offset;
   public int low;
   public int high;
   public int[] jump_offsets;
   public Instruction default_inst;
   public Instruction[] jump_insts;

   public Instruction_Tableswitch() {
      super((byte)-86);
      this.name = "tableswitch";
      this.branches = true;
   }

   public String toString(cp_info[] constant_pool) {
      String args = super.toString(constant_pool) + " " + "(" + Integer.toString(this.pad) + ")";
      args = args + " " + "label_" + Integer.toString(this.default_inst.label);
      args = args + " " + Integer.toString(this.low);
      args = args + " " + Integer.toString(this.high) + ": ";

      for(int i = 0; i < this.high - this.low + 1; ++i) {
         args = args + " " + "label_" + Integer.toString(this.jump_insts[i].label);
      }

      return args;
   }

   public int parse(byte[] bc, int index) {
      int i = index % 4;
      if (i != 0) {
         this.pad = (byte)(4 - i);
      } else {
         this.pad = 0;
      }

      index += this.pad;
      this.default_offset = getInt(bc, index);
      index += 4;
      this.low = getInt(bc, index);
      index += 4;
      this.high = getInt(bc, index);
      index += 4;
      i = this.high - this.low + 1;
      if (i > 0) {
         this.jump_offsets = new int[i];
         int j = 0;

         do {
            this.jump_offsets[j] = getInt(bc, index);
            index += 4;
            ++j;
         } while(j < i);
      }

      return index;
   }

   public int nextOffset(int curr) {
      int siz = 0;
      int i = (curr + 1) % 4;
      if (i != 0) {
         siz = 4 - i;
      }

      return curr + siz + 13 + (this.high - this.low + 1) * 4;
   }

   public int compile(byte[] bc, int index) {
      bc[index++] = this.code;

      int i;
      for(i = 0; i < this.pad; ++i) {
         bc[index++] = 0;
      }

      if (this.default_inst != null) {
         index = intToBytes(this.default_inst.label - this.label, bc, index);
      } else {
         index = intToBytes(this.default_offset, bc, index);
      }

      index = intToBytes(this.low, bc, index);
      index = intToBytes(this.high, bc, index);

      for(i = 0; i <= this.high - this.low; ++i) {
         if (this.jump_insts[i] != null) {
            index = intToBytes(this.jump_insts[i].label - this.label, bc, index);
         } else {
            index = intToBytes(this.jump_offsets[i], bc, index);
         }
      }

      return index;
   }

   public void offsetToPointer(ByteCode bc) {
      this.default_inst = bc.locateInst(this.default_offset + this.label);
      if (this.default_inst == null) {
         logger.warn("can't locate target of instruction");
         logger.debug(" which should be at byte address " + (this.label + this.default_offset));
      } else {
         this.default_inst.labelled = true;
      }

      if (this.high - this.low + 1 > 0) {
         this.jump_insts = new Instruction[this.high - this.low + 1];

         for(int i = 0; i < this.high - this.low + 1; ++i) {
            this.jump_insts[i] = bc.locateInst(this.jump_offsets[i] + this.label);
            if (this.jump_insts[i] == null) {
               logger.warn("can't locate target of instruction");
               logger.debug(" which should be at byte address " + (this.label + this.jump_offsets[i]));
            } else {
               this.jump_insts[i].labelled = true;
            }
         }
      }

   }

   public Instruction[] branchpoints(Instruction next) {
      Instruction[] i = new Instruction[this.high - this.low + 2];
      i[0] = this.default_inst;

      for(int j = 1; j < this.high - this.low + 2; ++j) {
         i[j] = this.jump_insts[j - 1];
      }

      return i;
   }
}
