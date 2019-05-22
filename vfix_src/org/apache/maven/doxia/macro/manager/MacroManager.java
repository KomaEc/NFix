package org.apache.maven.doxia.macro.manager;

import org.apache.maven.doxia.macro.Macro;

public interface MacroManager {
   String ROLE = MacroManager.class.getName();

   Macro getMacro(String var1) throws MacroNotFoundException;
}
