package org.apache.maven.doxia.site.decoration.inheritance;

import org.apache.maven.doxia.site.decoration.DecorationModel;

public interface DecorationModelInheritanceAssembler {
   String ROLE = DecorationModelInheritanceAssembler.class.getName();

   void assembleModelInheritance(String var1, DecorationModel var2, DecorationModel var3, String var4, String var5);

   void resolvePaths(DecorationModel var1, String var2);
}
