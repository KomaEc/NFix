package polyglot.ast;

public interface AmbExpr extends Expr, Ambiguous {
   String name();

   AmbExpr name(String var1);
}
