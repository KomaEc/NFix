package polyglot.ast;

import polyglot.util.CodeWriter;
import polyglot.util.Copy;

public interface Ext extends Copy {
   Node node();

   void init(Node var1);

   Ext ext();

   Ext ext(Ext var1);

   void dump(CodeWriter var1);
}
