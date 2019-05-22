package soot.coffi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Instruction_Lookupswitch extends Instruction {
   private static final Logger logger = LoggerFactory.getLogger(Instruction_Lookupswitch.class);
   public byte pad;
   public int default_offset;
   public int npairs;
   public int[] match_offsets;
   public Instruction default_inst;
   public Instruction[] match_insts;

   public Instruction_Lookupswitch() {
      super((byte)-85);
      this.name = "lookupswitch";
      this.branches = true;
   }

   public String toString(cp_info[] constant_pool) {
      String args = super.toString(constant_pool) + " " + "(" + Integer.toString(this.pad) + ")";
      args = args + " " + Integer.toString(this.default_inst.label);
      args = args + " " + Integer.toString(this.npairs) + ": ";

      for(int i = 0; i < this.npairs; ++i) {
         args = args + "case " + Integer.toString(this.match_offsets[i * 2]) + ": label_" + Integer.toString(this.match_insts[i].label);
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
      this.npairs = getInt(bc, index);
      index += 4;
      if (this.npairs > 0) {
         this.match_offsets = new int[this.npairs * 2];
         int j = 0;

         do {
            this.match_offsets[j] = getInt(bc, index);
            ++j;
            index += 4;
            this.match_offsets[j] = getInt(bc, index);
            index += 4;
            ++j;
         } while(j < this.npairs * 2);
      }

      return index;
   }

   public int nextOffset(int curr) {
      int siz = 0;
      int i = (curr + 1) % 4;
      if (i != 0) {
         siz = 4 - i;
      }

      return curr + siz + 9 + this.npairs * 8;
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

      index = intToBytes(this.npairs, bc, index);

      for(i = 0; i < this.npairs; ++i) {
         index = intToBytes(this.match_offsets[i * 2], bc, index);
         if (this.match_insts[i] != null) {
            index = intToBytes(this.match_insts[i].label - this.label, bc, index);
         } else {
            index = intToBytes(this.match_offsets[i * 2 + 1], bc, index);
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

      if (this.npairs > 0) {
         this.match_insts = new Instruction[this.npairs];

         for(int i = 0; i < this.npairs; ++i) {
            this.match_insts[i] = bc.locateInst(this.match_offsets[i * 2 + 1] + this.label);
            if (this.match_insts[i] == null) {
               logger.warn("can't locate target of instruction");
               logger.debug(" which should be at byte address " + (this.label + this.match_offsets[i * 2 + 1]));
            } else {
               this.match_insts[i].labelled = true;
            }
         }
      }

   }

   public Instruction[] branchpoints(Instruction next) {
      Instruction[] i = new Instruction[this.npairs + 1];
      i[0] = this.default_inst;

      for(int j = 1; j < this.npairs + 1; ++j) {
         i[j] = this.match_insts[j - 1];
      }

      return i;
   }
}
