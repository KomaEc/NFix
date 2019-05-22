package soot.util.dot;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DotGraph implements Renderable {
   private static final Logger logger = LoggerFactory.getLogger(DotGraph.class);
   private String graphname;
   private boolean isSubGraph;
   private HashMap<String, DotGraphNode> nodes;
   private List<Renderable> drawElements;
   private List<DotGraphAttribute> attributes;
   public static final String DOT_EXTENSION = ".dot";

   public DotGraph(String graphname) {
      this.graphname = graphname;
      this.isSubGraph = false;
      this.nodes = new HashMap(100);
      this.drawElements = new LinkedList();
      this.attributes = new LinkedList();
   }

   public void plot(String filename) {
      try {
         BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filename));
         this.render(out, 0);
         out.close();
      } catch (IOException var3) {
         logger.debug("" + var3.getMessage());
      }

   }

   public DotGraphEdge drawEdge(String from, String to) {
      DotGraphNode src = this.drawNode(from);
      DotGraphNode dst = this.drawNode(to);
      DotGraphEdge edge = new DotGraphEdge(src, dst);
      this.drawElements.add(edge);
      return edge;
   }

   public DotGraphNode drawNode(String name) {
      DotGraphNode node = this.getNode(name);
      if (node == null) {
         throw new RuntimeException("Assertion failed.");
      } else {
         if (!this.drawElements.contains(node)) {
            this.drawElements.add(node);
         }

         return node;
      }
   }

   public DotGraphNode getNode(String name) {
      if (name == null) {
         return null;
      } else {
         DotGraphNode node = (DotGraphNode)this.nodes.get(name);
         if (node == null) {
            node = new DotGraphNode(name);
            this.nodes.put(name, node);
         }

         return node;
      }
   }

   public void setNodeShape(String shape) {
      StringBuffer command = new StringBuffer("node [shape=");
      command.append(shape);
      command.append("];");
      this.drawElements.add(new DotGraphCommand(new String(command)));
   }

   public void setNodeStyle(String style) {
      StringBuffer command = new StringBuffer("node [style=");
      command.append(style);
      command.append("];");
      this.drawElements.add(new DotGraphCommand(new String(command)));
   }

   public void setGraphSize(double width, double height) {
      String size = "\"" + width + "," + height + "\"";
      this.setGraphAttribute("size", size);
   }

   public void setPageSize(double width, double height) {
      String size = "\"" + width + ", " + height + "\"";
      this.setGraphAttribute("page", size);
   }

   public void setOrientation(String orientation) {
      this.setGraphAttribute("orientation", orientation);
   }

   public void setGraphName(String name) {
      this.graphname = name;
   }

   public void setGraphLabel(String label) {
      label = DotGraphUtility.replaceQuotes(label);
      label = DotGraphUtility.replaceReturns(label);
      this.setGraphAttribute("label", "\"" + label + "\"");
   }

   public void setGraphAttribute(String id, String value) {
      this.setGraphAttribute(new DotGraphAttribute(id, value));
   }

   public void setGraphAttribute(DotGraphAttribute attr) {
      this.attributes.add(attr);
   }

   public void drawUndirectedEdge(String label1, String label2) {
   }

   public DotGraph createSubGraph(String label) {
      DotGraph subgraph = new DotGraph(label);
      subgraph.isSubGraph = true;
      this.drawElements.add(subgraph);
      return subgraph;
   }

   public void render(OutputStream out, int indent) throws IOException {
      String graphname = this.graphname;
      if (!this.isSubGraph) {
         DotGraphUtility.renderLine(out, "digraph \"" + graphname + "\" {", indent);
      } else {
         DotGraphUtility.renderLine(out, "subgraph \"" + graphname + "\" {", indent);
      }

      Iterator attrIt = this.attributes.iterator();

      while(attrIt.hasNext()) {
         DotGraphAttribute attr = (DotGraphAttribute)attrIt.next();
         DotGraphUtility.renderLine(out, attr.toString() + ";", indent + 4);
      }

      Iterator elmntsIt = this.drawElements.iterator();

      while(elmntsIt.hasNext()) {
         Renderable element = (Renderable)elmntsIt.next();
         element.render(out, indent + 4);
      }

      DotGraphUtility.renderLine(out, "}", indent);
   }
}
