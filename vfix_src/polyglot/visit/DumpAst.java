package polyglot.visit;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import polyglot.ast.Node;
import polyglot.util.CodeWriter;

public class DumpAst extends NodeVisitor {
   protected Writer fw;
   protected CodeWriter w;

   public DumpAst(String name, int width) throws IOException {
      this.fw = new FileWriter(name);
      this.w = new CodeWriter(this.fw, width);
   }

   public DumpAst(CodeWriter w) {
      this.w = w;
   }

   public NodeVisitor enter(Node n) {
      this.w.write("(");
      n.dump(this.w);
      this.w.allowBreak(4, " ");
      this.w.begin(0);
      return this;
   }

   public Node leave(Node old, Node n, NodeVisitor v) {
      this.w.end();
      this.w.write(")");
      this.w.allowBreak(0, " ");
      return n;
   }

   public void finish() {
      try {
         this.w.flush();
         if (this.fw != null) {
            this.fw.flush();
            this.fw.close();
         }
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }
}
