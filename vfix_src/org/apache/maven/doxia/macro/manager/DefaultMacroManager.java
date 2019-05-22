package org.apache.maven.doxia.macro.manager;

import java.util.Map;
import org.apache.maven.doxia.macro.Macro;

public class DefaultMacroManager implements MacroManager {
   private Map macros;

   public Macro getMacro(String id) throws MacroNotFoundException {
      Macro macro = (Macro)this.macros.get(id);
      if (macro == null) {
         throw new MacroNotFoundException("Cannot find macro with id = " + id);
      } else {
         return macro;
      }
   }
}
