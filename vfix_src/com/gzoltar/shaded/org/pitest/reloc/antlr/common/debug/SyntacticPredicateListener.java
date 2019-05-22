package com.gzoltar.shaded.org.pitest.reloc.antlr.common.debug;

public interface SyntacticPredicateListener extends ListenerBase {
   void syntacticPredicateFailed(SyntacticPredicateEvent var1);

   void syntacticPredicateStarted(SyntacticPredicateEvent var1);

   void syntacticPredicateSucceeded(SyntacticPredicateEvent var1);
}
