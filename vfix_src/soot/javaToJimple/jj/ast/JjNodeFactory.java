package soot.javaToJimple.jj.ast;

import polyglot.ast.Expr;
import polyglot.ast.NodeFactory;
import polyglot.util.Position;

public interface JjNodeFactory extends NodeFactory {
   JjComma_c JjComma(Position var1, Expr var2, Expr var3);
}
