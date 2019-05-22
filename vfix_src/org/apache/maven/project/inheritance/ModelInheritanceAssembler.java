package org.apache.maven.project.inheritance;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;

public interface ModelInheritanceAssembler {
   String ROLE = ModelInheritanceAssembler.class.getName();

   void assembleModelInheritance(Model var1, Model var2, String var3);

   void assembleModelInheritance(Model var1, Model var2);

   void assembleBuildInheritance(Build var1, Build var2, boolean var3);

   void copyModel(Model var1, Model var2);
}
