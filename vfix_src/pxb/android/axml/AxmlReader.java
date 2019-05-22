package pxb.android.axml;

import java.io.IOException;
import java.util.Stack;

public class AxmlReader {
   public static final NodeVisitor EMPTY_VISITOR = new NodeVisitor() {
      public NodeVisitor child(String ns, String name) {
         return this;
      }
   };
   final AxmlParser parser;

   public AxmlReader(byte[] data) {
      this.parser = new AxmlParser(data);
   }

   public void accept(AxmlVisitor av) throws IOException {
      Stack<NodeVisitor> nvs = new Stack();
      Object tos = av;

      label34:
      while(true) {
         int type = this.parser.next();
         switch(type) {
         case 1:
         case 5:
            break;
         case 2:
            nvs.push(tos);
            tos = ((NodeVisitor)tos).child(this.parser.getNamespaceUri(), this.parser.getName());
            if (tos != null) {
               if (tos == EMPTY_VISITOR) {
                  break;
               }

               ((NodeVisitor)tos).line(this.parser.getLineNumber());
               int i = 0;

               while(true) {
                  if (i >= this.parser.getAttrCount()) {
                     continue label34;
                  }

                  ((NodeVisitor)tos).attr(this.parser.getAttrNs(i), this.parser.getAttrName(i), this.parser.getAttrResId(i), this.parser.getAttrType(i), this.parser.getAttrValue(i));
                  ++i;
               }
            }

            tos = EMPTY_VISITOR;
            break;
         case 3:
            ((NodeVisitor)tos).end();
            tos = (NodeVisitor)nvs.pop();
            break;
         case 4:
            av.ns(this.parser.getNamespacePrefix(), this.parser.getNamespaceUri(), this.parser.getLineNumber());
            break;
         case 6:
            ((NodeVisitor)tos).text(this.parser.getLineNumber(), this.parser.getText());
            break;
         case 7:
            return;
         default:
            System.err.println("AxmlReader: Unsupported tag: " + type);
         }
      }
   }
}
