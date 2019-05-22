package polyglot.ast;

import java.util.List;

public interface For extends Loop {
   List inits();

   For inits(List var1);

   For cond(Expr var1);

   List iters();

   For iters(List var1);

   For body(Stmt var1);
}
