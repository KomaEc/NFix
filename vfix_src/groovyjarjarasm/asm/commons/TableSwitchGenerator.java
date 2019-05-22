package groovyjarjarasm.asm.commons;

import groovyjarjarasm.asm.Label;

public interface TableSwitchGenerator {
   void generateCase(int var1, Label var2);

   void generateDefault();
}
