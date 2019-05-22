package polyglot.ast;

import polyglot.types.SemanticException;
import polyglot.util.Position;
import polyglot.visit.ContextVisitor;

public interface Disamb {
   Node disambiguate(Ambiguous var1, ContextVisitor var2, Position var3, Prefix var4, String var5) throws SemanticException;
}
