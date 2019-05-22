package bsh;

import java.io.Serializable;

interface Node extends Serializable {
   void jjtOpen();

   void jjtClose();

   void jjtSetParent(Node var1);

   Node jjtGetParent();

   void jjtAddChild(Node var1, int var2);

   Node jjtGetChild(int var1);

   int jjtGetNumChildren();
}
