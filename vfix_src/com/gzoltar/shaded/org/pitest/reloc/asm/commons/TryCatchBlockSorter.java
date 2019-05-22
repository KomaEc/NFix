package com.gzoltar.shaded.org.pitest.reloc.asm.commons;

import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.tree.MethodNode;
import com.gzoltar.shaded.org.pitest.reloc.asm.tree.TryCatchBlockNode;
import java.util.Collections;

public class TryCatchBlockSorter extends MethodNode {
   public TryCatchBlockSorter(MethodVisitor var1, int var2, String var3, String var4, String var5, String[] var6) {
      this(327680, var1, var2, var3, var4, var5, var6);
   }

   protected TryCatchBlockSorter(int var1, MethodVisitor var2, int var3, String var4, String var5, String var6, String[] var7) {
      super(var1, var3, var4, var5, var6, var7);
      this.mv = var2;
   }

   public void visitEnd() {
      TryCatchBlockSorter$1 var1 = new TryCatchBlockSorter$1(this);
      Collections.sort(this.tryCatchBlocks, var1);

      for(int var2 = 0; var2 < this.tryCatchBlocks.size(); ++var2) {
         ((TryCatchBlockNode)this.tryCatchBlocks.get(var2)).updateIndex(var2);
      }

      if (this.mv != null) {
         this.accept(this.mv);
      }

   }
}
