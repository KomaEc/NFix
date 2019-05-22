package soot.util.dot;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DotGraphNode implements Renderable {
   private String name;
   private List<DotGraphAttribute> attributes;

   public DotGraphNode(String name) {
      this.name = "\"" + DotGraphUtility.replaceQuotes(name) + "\"";
   }

   public String getName() {
      return this.name;
   }

   public void setLabel(String label) {
      label = DotGraphUtility.replaceQuotes(label);
      label = DotGraphUtility.replaceReturns(label);
      this.setAttribute("label", "\"" + label + "\"");
   }

   public void setHTMLLabel(String label) {
      label = DotGraphUtility.replaceReturns(label);
      this.setAttribute("label", label);
   }

   public void setShape(String shape) {
      this.setAttribute("shape", shape);
   }

   public void setStyle(String style) {
      this.setAttribute("style", style);
   }

   public void setAttribute(String id, String value) {
      if (this.attributes == null) {
         this.attributes = new LinkedList();
      }

      this.setAttribute(new DotGraphAttribute(id, value));
   }

   public void setAttribute(DotGraphAttribute attr) {
      if (this.attributes == null) {
         this.attributes = new LinkedList();
      }

      this.attributes.add(attr);
   }

   public void render(OutputStream out, int indent) throws IOException {
      StringBuffer line = new StringBuffer(this.getName());
      if (this.attributes != null) {
         line.append(" [");
         Iterator var4 = this.attributes.iterator();

         while(var4.hasNext()) {
            DotGraphAttribute attr = (DotGraphAttribute)var4.next();
            line.append(attr.toString());
            line.append(",");
         }

         line.append("];");
      }

      DotGraphUtility.renderLine(out, new String(line), indent);
   }
}
