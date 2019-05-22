package com.gzoltar.shaded.org.pitest.reloc.antlr.common.debug;

public interface ParserMatchListener extends ListenerBase {
   void parserMatch(ParserMatchEvent var1);

   void parserMatchNot(ParserMatchEvent var1);

   void parserMismatch(ParserMatchEvent var1);

   void parserMismatchNot(ParserMatchEvent var1);
}
