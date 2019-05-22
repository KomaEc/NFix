package soot.shimple.toolkits.graph;

import soot.Local;

public interface GlobalValueNumberer {
   int getGlobalValueNumber(Local var1);

   boolean areEqual(Local var1, Local var2);
}
