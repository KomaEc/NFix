package polyglot.ast;

import java.util.List;
import polyglot.types.Type;
import polyglot.util.CodeWriter;
import polyglot.util.Copy;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.NodeVisitor;

public interface Node extends JL, Copy {
   Node del(JL var1);

   JL del();

   Node ext(Ext var1);

   Ext ext();

   Node ext(int var1, Ext var2);

   Ext ext(int var1);

   Position position();

   Node position(Position var1);

   Node visit(NodeVisitor var1);

   Node visitEdge(Node var1, NodeVisitor var2);

   Node visitChild(Node var1, NodeVisitor var2);

   List visitList(List var1, NodeVisitor var2);

   Type childExpectedType(Expr var1, AscriptionVisitor var2);

   void dump(CodeWriter var1);
}
