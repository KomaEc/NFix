package com.gzoltar.shaded.org.pitest.reloc.antlr.common.collections.impl;

import com.gzoltar.shaded.org.pitest.reloc.antlr.common.collections.AST;

public class ASTArray {
   public int size = 0;
   public AST[] array;

   public ASTArray(int var1) {
      this.array = new AST[var1];
   }

   public ASTArray add(AST var1) {
      this.array[this.size++] = var1;
      return this;
   }
}
