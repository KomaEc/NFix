package polyglot.visit;

import java.io.IOException;
import polyglot.ast.Node;
import polyglot.util.CodeWriter;

public class PrettyPrinter {
   protected boolean appendSemicolon = true;
   protected boolean printType = true;

   public boolean appendSemicolon() {
      return this.appendSemicolon;
   }

   public boolean appendSemicolon(boolean a) {
      boolean old = this.appendSemicolon;
      this.appendSemicolon = a;
      return old;
   }

   public boolean printType() {
      return this.printType;
   }

   public boolean printType(boolean a) {
      boolean old = this.printType;
      this.printType = a;
      return old;
   }

   public void print(Node parent, Node child, CodeWriter w) {
      if (child != null) {
         child.del().prettyPrint(w, this);
      }

   }

   public void printAst(Node ast, CodeWriter w) {
      this.print((Node)null, ast, w);

      try {
         w.flush();
      } catch (IOException var4) {
      }

   }
}
