package java_cup.runtime;

public interface SymbolFactory {
   Symbol newSymbol(String var1, int var2, Symbol var3, Symbol var4, Object var5);

   Symbol newSymbol(String var1, int var2, Symbol var3, Symbol var4);

   Symbol newSymbol(String var1, int var2, Symbol var3, Object var4);

   Symbol newSymbol(String var1, int var2, Object var3);

   Symbol newSymbol(String var1, int var2);

   Symbol startSymbol(String var1, int var2, int var3);
}
