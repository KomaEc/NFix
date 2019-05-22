package com.gzoltar.shaded.org.pitest.reloc.asm.commons;

import com.gzoltar.shaded.org.pitest.reloc.asm.Label;

public interface TableSwitchGenerator {
   void generateCase(int var1, Label var2);

   void generateDefault();
}
