package com.gzoltar.shaded.org.jacoco.core.internal.flow;

import com.gzoltar.shaded.org.objectweb.asm.tree.AbstractInsnNode;

public class Instruction {
   private final AbstractInsnNode node;
   private final int line;
   private int branches;
   private int coveredBranches;
   private Instruction predecessor;

   public Instruction(AbstractInsnNode node, int line) {
      this.node = node;
      this.line = line;
      this.branches = 0;
      this.coveredBranches = 0;
   }

   public AbstractInsnNode getNode() {
      return this.node;
   }

   public void addBranch() {
      ++this.branches;
   }

   public void setPredecessor(Instruction predecessor) {
      this.predecessor = predecessor;
      predecessor.addBranch();
   }

   public void setCovered() {
      for(Instruction i = this; i != null && i.coveredBranches++ == 0; i = i.predecessor) {
      }

   }

   public int getLine() {
      return this.line;
   }

   public int getBranches() {
      return this.branches;
   }

   public int getCoveredBranches() {
      return this.coveredBranches;
   }
}
