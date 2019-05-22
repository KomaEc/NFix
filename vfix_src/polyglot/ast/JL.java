package polyglot.ast;

import polyglot.util.Copy;

public interface JL extends NodeOps, Copy {
   Node node();

   void init(Node var1);
}
