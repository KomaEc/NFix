package groovy.util;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.groovy.runtime.InvokerHelper;

public class NodePrinter {
   protected final IndentPrinter out;

   public NodePrinter() {
      this(new IndentPrinter(new PrintWriter(new OutputStreamWriter(System.out))));
   }

   public NodePrinter(PrintWriter out) {
      this(new IndentPrinter(out));
   }

   public NodePrinter(IndentPrinter out) {
      if (out == null) {
         throw new NullPointerException("IndentPrinter 'out' must not be null!");
      } else {
         this.out = out;
      }
   }

   public void print(Node node) {
      this.out.printIndent();
      this.printName(node);
      Map attributes = node.attributes();
      boolean hasAttributes = attributes != null && !attributes.isEmpty();
      if (hasAttributes) {
         this.printAttributes(attributes);
      }

      Object value = node.value();
      if (value instanceof List) {
         if (!hasAttributes) {
            this.out.print("()");
         }

         this.printList((List)value);
      } else if (value instanceof String) {
         this.out.print("('");
         this.out.print((String)value);
         this.out.println("')");
      } else {
         this.out.println("()");
      }

      this.out.flush();
   }

   protected void printName(Node node) {
      Object name = node.name();
      if (name != null) {
         this.out.print(name.toString());
      } else {
         this.out.print("null");
      }

   }

   protected void printList(List list) {
      if (list.isEmpty()) {
         this.out.println("");
      } else {
         this.out.println(" {");
         this.out.incrementIndent();
         Iterator iter = list.iterator();

         while(iter.hasNext()) {
            Object value = iter.next();
            if (value instanceof Node) {
               this.print((Node)value);
            } else {
               this.out.printIndent();
               this.out.print("builder.append(");
               this.out.print(InvokerHelper.toString(value));
               this.out.println(")");
            }
         }

         this.out.decrementIndent();
         this.out.printIndent();
         this.out.println("}");
      }

   }

   protected void printAttributes(Map attributes) {
      this.out.print("(");
      boolean first = true;
      Iterator iter = attributes.entrySet().iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
         if (first) {
            first = false;
         } else {
            this.out.print(", ");
         }

         this.out.print(entry.getKey().toString());
         this.out.print(":");
         if (entry.getValue() instanceof String) {
            this.out.print("'" + entry.getValue() + "'");
         } else {
            this.out.print(InvokerHelper.toString(entry.getValue()));
         }
      }

      this.out.print(")");
   }
}
