package polyglot.ast;

import java.util.List;

public interface Block extends CompoundStmt {
   List statements();

   Block statements(List var1);

   Block append(Stmt var1);

   Block prepend(Stmt var1);
}
