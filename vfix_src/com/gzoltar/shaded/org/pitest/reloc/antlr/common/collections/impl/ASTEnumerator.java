package com.gzoltar.shaded.org.pitest.reloc.antlr.common.collections.impl;

import com.gzoltar.shaded.org.pitest.reloc.antlr.common.collections.AST;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.collections.ASTEnumeration;
import java.util.NoSuchElementException;

public class ASTEnumerator implements ASTEnumeration {
   VectorEnumerator nodes;
   int i = 0;

   public ASTEnumerator(Vector var1) {
      this.nodes = new VectorEnumerator(var1);
   }

   public boolean hasMoreNodes() {
      synchronized(this.nodes) {
         return this.i <= this.nodes.vector.lastElement;
      }
   }

   public AST nextNode() {
      synchronized(this.nodes) {
         if (this.i <= this.nodes.vector.lastElement) {
            return (AST)this.nodes.vector.data[this.i++];
         } else {
            throw new NoSuchElementException("ASTEnumerator");
         }
      }
   }
}
