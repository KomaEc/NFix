package com.gzoltar.shaded.org.objectweb.asm.commons;

import com.gzoltar.shaded.org.objectweb.asm.Label;

public interface TableSwitchGenerator {
   void generateCase(int var1, Label var2);

   void generateDefault();
}
