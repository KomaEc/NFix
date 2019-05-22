package soot.util.dot;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DotGraphEdge implements Renderable {
   private boolean isDirected;
   private DotGraphNode start;
   private DotGraphNode end;
   private List<DotGraphAttribute> attributes;

   public DotGraphEdge(DotGraphNode src, DotGraphNode dst) {
      this.start = src;
      this.end = dst;
      this.isDirected = true;
   }

   public DotGraphEdge(DotGraphNode src, DotGraphNode dst, boolean directed) {
      this.start = src;
      this.end = dst;
      this.isDirected = directed;
   }

   public void setLabel(String label) {
      label = DotGraphUtility.replaceQuotes(label);
      label = DotGraphUtility.replaceReturns(label);
      this.setAttribute("label", "\"" + label + "\"");
   }

   public void setStyle(String style) {
      this.setAttribute("style", style);
   }

   public void setAttribute(String id, String value) {
      this.setAttribute(new DotGraphAttribute(id, value));
   }

   public void setAttribute(DotGraphAttribute attr) {
      if (this.attributes == null) {
         this.attributes = new LinkedList();
      }

      this.attributes.add(attr);
   }

   public void render(OutputStream out, int indent) throws IOException {
      StringBuffer line = new StringBuffer(this.start.getName());
      line.append(this.isDirected ? "->" : "--");
      line.append(this.end.getName());
      if (this.attributes != null) {
         line.append(" [");
         Iterator attrIt = this.attributes.iterator();

         while(attrIt.hasNext()) {
            DotGraphAttribute attr = (DotGraphAttribute)attrIt.next();
            line.append(attr.toString());
            line.append(",");
         }

         line.append("]");
      }

      line.append(";");
      DotGraphUtility.renderLine(out, new String(line), indent);
   }
}
