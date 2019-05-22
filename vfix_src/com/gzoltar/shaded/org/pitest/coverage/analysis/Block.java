package com.gzoltar.shaded.org.pitest.coverage.analysis;

import java.util.Set;

public final class Block {
   private final int firstInstruction;
   private final int lastInstruction;
   private final Set<Integer> lines;

   public Block(int firstInstruction, int lastInstruction, Set<Integer> lines) {
      this.firstInstruction = firstInstruction;
      this.lastInstruction = lastInstruction;
      this.lines = lines;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.firstInstruction;
      result = 31 * result + this.lastInstruction;
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
         Block other = (Block)obj;
         if (this.firstInstruction != other.firstInstruction) {
            return false;
         } else {
            return this.lastInstruction == other.lastInstruction;
         }
      }
   }

   public String toString() {
      return "Block [firstInstruction=" + this.firstInstruction + ", lastInstruction=" + this.lastInstruction + "]";
   }

   public boolean firstInstructionIs(int ins) {
      return this.firstInstruction == ins;
   }

   public Set<Integer> getLines() {
      return this.lines;
   }
}
