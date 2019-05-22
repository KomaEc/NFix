package soot.util.cfgcmd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import soot.Body;
import soot.BriefUnitPrinter;
import soot.LabeledUnitPrinter;
import soot.Unit;
import soot.toolkits.exceptions.ThrowableSet;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.DominatorNode;
import soot.toolkits.graph.ExceptionalGraph;
import soot.util.dot.DotGraph;
import soot.util.dot.DotGraphAttribute;
import soot.util.dot.DotGraphEdge;
import soot.util.dot.DotGraphNode;

public class CFGToDotGraph {
   private boolean onePage;
   private boolean isBrief;
   private boolean showExceptions;
   private DotGraphAttribute unexceptionalControlFlowAttr;
   private DotGraphAttribute exceptionalControlFlowAttr;
   private DotGraphAttribute exceptionEdgeAttr;
   private DotGraphAttribute headAttr;
   private DotGraphAttribute tailAttr;

   public CFGToDotGraph() {
      this.setOnePage(true);
      this.setBriefLabels(false);
      this.setShowExceptions(true);
      this.setUnexceptionalControlFlowAttr("color", "black");
      this.setExceptionalControlFlowAttr("color", "red");
      this.setExceptionEdgeAttr("color", "lightgray");
      this.setHeadAttr("fillcolor", "gray");
      this.setTailAttr("fillcolor", "lightgray");
   }

   public void setOnePage(boolean onePage) {
      this.onePage = onePage;
   }

   public void setBriefLabels(boolean useBrief) {
      this.isBrief = useBrief;
   }

   public void setShowExceptions(boolean showExceptions) {
      this.showExceptions = showExceptions;
   }

   public void setUnexceptionalControlFlowAttr(String id, String value) {
      this.unexceptionalControlFlowAttr = new DotGraphAttribute(id, value);
   }

   public void setExceptionalControlFlowAttr(String id, String value) {
      this.exceptionalControlFlowAttr = new DotGraphAttribute(id, value);
   }

   public void setExceptionEdgeAttr(String id, String value) {
      this.exceptionEdgeAttr = new DotGraphAttribute(id, value);
   }

   public void setHeadAttr(String id, String value) {
      this.headAttr = new DotGraphAttribute(id, value);
   }

   public void setTailAttr(String id, String value) {
      this.tailAttr = new DotGraphAttribute(id, value);
   }

   private static <T> Iterator<T> sortedIterator(Collection<T> coll, Comparator<T> comp) {
      if (coll.size() <= 1) {
         return coll.iterator();
      } else {
         ArrayList<T> list = new ArrayList(coll);
         Collections.sort(list, comp);
         return list.iterator();
      }
   }

   public <N> DotGraph drawCFG(DirectedGraph<N> graph, Body body) {
      DotGraph canvas = this.initDotGraph(body);
      CFGToDotGraph.DotNamer<N> namer = new CFGToDotGraph.DotNamer((int)((float)graph.size() / 0.7F), 0.7F);
      CFGToDotGraph.NodeComparator<N> comparator = new CFGToDotGraph.NodeComparator(namer);
      Iterator nodesIt = graph.iterator();

      while(nodesIt.hasNext()) {
         namer.getName(nodesIt.next());
      }

      nodesIt = graph.iterator();

      while(nodesIt.hasNext()) {
         N node = nodesIt.next();
         canvas.drawNode(namer.getName(node));
         Iterator succsIt = sortedIterator(graph.getSuccsOf(node), comparator);

         while(succsIt.hasNext()) {
            N succ = succsIt.next();
            canvas.drawEdge(namer.getName(node), namer.getName(succ));
         }
      }

      this.setStyle(graph.getHeads(), canvas, namer, "filled", this.headAttr);
      this.setStyle(graph.getTails(), canvas, namer, "filled", this.tailAttr);
      if (!this.isBrief) {
         this.formatNodeText(body, canvas, namer);
      }

      return canvas;
   }

   public <N> DotGraph drawCFG(ExceptionalGraph<N> graph) {
      Body body = graph.getBody();
      DotGraph canvas = this.initDotGraph(body);
      CFGToDotGraph.DotNamer namer = new CFGToDotGraph.DotNamer((int)((float)graph.size() / 0.7F), 0.7F);
      CFGToDotGraph.NodeComparator nodeComparator = new CFGToDotGraph.NodeComparator(namer);
      Iterator nodesIt = graph.iterator();

      while(nodesIt.hasNext()) {
         namer.getName(nodesIt.next());
      }

      nodesIt = graph.iterator();

      while(true) {
         Object node;
         Iterator destsIt;
         do {
            if (!nodesIt.hasNext()) {
               this.setStyle(graph.getHeads(), canvas, namer, "filled", this.headAttr);
               this.setStyle(graph.getTails(), canvas, namer, "filled", this.tailAttr);
               if (!this.isBrief) {
                  this.formatNodeText(graph.getBody(), canvas, namer);
               }

               return canvas;
            }

            node = nodesIt.next();
            canvas.drawNode(namer.getName(node));
            destsIt = sortedIterator(graph.getUnexceptionalSuccsOf(node), nodeComparator);

            Object succ;
            DotGraphEdge edge;
            while(destsIt.hasNext()) {
               succ = destsIt.next();
               edge = canvas.drawEdge(namer.getName(node), namer.getName(succ));
               edge.setAttribute(this.unexceptionalControlFlowAttr);
            }

            destsIt = sortedIterator(graph.getExceptionalSuccsOf(node), nodeComparator);

            while(destsIt.hasNext()) {
               succ = destsIt.next();
               edge = canvas.drawEdge(namer.getName(node), namer.getName(succ));
               edge.setAttribute(this.exceptionalControlFlowAttr);
            }
         } while(!this.showExceptions);

         destsIt = sortedIterator(graph.getExceptionDests(node), new CFGToDotGraph.ExceptionDestComparator(namer));

         while(destsIt.hasNext()) {
            ExceptionalGraph.ExceptionDest<N> dest = (ExceptionalGraph.ExceptionDest)destsIt.next();
            Object handlerStart = dest.getHandlerNode();
            if (handlerStart == null) {
               handlerStart = new Object() {
                  public String toString() {
                     return "Esc";
                  }
               };
               DotGraphNode escapeNode = canvas.drawNode(namer.getName(handlerStart));
               escapeNode.setStyle("invis");
            }

            DotGraphEdge edge = canvas.drawEdge(namer.getName(node), namer.getName(handlerStart));
            edge.setAttribute(this.exceptionEdgeAttr);
            edge.setLabel(this.formatThrowableSet(dest.getThrowables()));
         }
      }
   }

   private DotGraph initDotGraph(Body body) {
      String graphname = "cfg";
      if (body != null) {
         graphname = body.getMethod().getSubSignature();
      }

      DotGraph canvas = new DotGraph(graphname);
      canvas.setGraphLabel(graphname);
      if (!this.onePage) {
         canvas.setPageSize(8.5D, 11.0D);
      }

      if (this.isBrief) {
         canvas.setNodeShape("circle");
      } else {
         canvas.setNodeShape("box");
      }

      return canvas;
   }

   private <N> void formatNodeText(Body body, DotGraph canvas, CFGToDotGraph.DotNamer<N> namer) {
      LabeledUnitPrinter printer = null;
      if (body != null) {
         printer = new BriefUnitPrinter(body);
         printer.noIndent();
      }

      DotGraphNode dotnode;
      String nodeLabel;
      for(Iterator nodesIt = namer.keySet().iterator(); nodesIt.hasNext(); dotnode.setLabel(nodeLabel)) {
         N node = nodesIt.next();
         dotnode = canvas.getNode(namer.getName(node));
         nodeLabel = null;
         if (node instanceof DominatorNode) {
            node = ((DominatorNode)node).getGode();
         }

         if (printer == null) {
            nodeLabel = node.toString();
         } else if (node instanceof Unit) {
            ((Unit)node).toString(printer);
            String targetLabel = (String)printer.labels().get(node);
            if (targetLabel == null) {
               nodeLabel = printer.toString();
            } else {
               nodeLabel = targetLabel + ": " + printer.toString();
            }
         } else if (!(node instanceof Block)) {
            nodeLabel = node.toString();
         } else {
            Iterator<Unit> units = ((Block)node).iterator();
            StringBuffer buffer = new StringBuffer();

            while(units.hasNext()) {
               Unit unit = (Unit)units.next();
               String targetLabel = (String)printer.labels().get(unit);
               if (targetLabel != null) {
                  buffer.append(targetLabel).append(":\\n");
               }

               unit.toString(printer);
               buffer.append(printer.toString()).append("\\l");
            }

            nodeLabel = buffer.toString();
         }
      }

   }

   private <N> void setStyle(Collection<? extends N> objects, DotGraph canvas, CFGToDotGraph.DotNamer<N> namer, String style, DotGraphAttribute attrib) {
      Iterator var6 = objects.iterator();

      while(var6.hasNext()) {
         N object = var6.next();
         DotGraphNode objectNode = canvas.getNode(namer.getName(object));
         objectNode.setStyle(style);
         objectNode.setAttribute(attrib);
      }

   }

   private String formatThrowableSet(ThrowableSet set) {
      String input = set.toAbbreviatedString();
      int inputLength = input.length();
      StringBuffer result = new StringBuffer(inputLength + 5);

      for(int i = 0; i < inputLength; ++i) {
         char c = input.charAt(i);
         if (c == '+' || c == '-') {
            result.append("\\l");
         }

         result.append(c);
      }

      return result.toString();
   }

   private static class DotNamer<N> extends HashMap<N, Integer> {
      private int nodecount = 0;

      DotNamer(int initialCapacity, float loadFactor) {
         super(initialCapacity, loadFactor);
      }

      String getName(N node) {
         Integer index = (Integer)this.get(node);
         if (index == null) {
            index = this.nodecount++;
            this.put(node, index);
         }

         return index.toString();
      }

      int getNumber(N node) {
         Integer index = (Integer)this.get(node);
         if (index == null) {
            index = this.nodecount++;
            this.put(node, index);
         }

         return index;
      }
   }

   private static class ExceptionDestComparator<T> implements Comparator<ExceptionalGraph.ExceptionDest<T>> {
      private CFGToDotGraph.DotNamer<T> namer;

      ExceptionDestComparator(CFGToDotGraph.DotNamer<T> namer) {
         this.namer = namer;
      }

      private int getValue(ExceptionalGraph.ExceptionDest<T> o) {
         T handler = o.getHandlerNode();
         return handler == null ? Integer.MAX_VALUE : this.namer.getNumber(handler);
      }

      public int compare(ExceptionalGraph.ExceptionDest<T> o1, ExceptionalGraph.ExceptionDest<T> o2) {
         return this.getValue(o1) - this.getValue(o2);
      }

      public boolean equal(ExceptionalGraph.ExceptionDest<T> o1, ExceptionalGraph.ExceptionDest<T> o2) {
         return this.getValue(o1) == this.getValue(o2);
      }
   }

   private static class NodeComparator<T> implements Comparator<T> {
      private CFGToDotGraph.DotNamer<T> namer;

      NodeComparator(CFGToDotGraph.DotNamer<T> namer) {
         this.namer = namer;
      }

      public int compare(T o1, T o2) {
         return this.namer.getNumber(o1) - this.namer.getNumber(o2);
      }

      public boolean equal(T o1, T o2) {
         return this.namer.getNumber(o1) == this.namer.getNumber(o2);
      }
   }
}
