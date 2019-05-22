package soot.jimple.spark.ondemand;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.spark.ondemand.genericutil.Predicate;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PagToDotDumper;
import soot.jimple.spark.pag.VarNode;

public class DotPointerGraph {
   private static final Logger logger = LoggerFactory.getLogger(DotPointerGraph.class);
   private final Set<String> edges = new HashSet();
   private final Set<Node> nodes = new HashSet();

   public void addAssign(VarNode from, VarNode to) {
      this.addEdge((Node)to, (Node)from, "", "black");
   }

   private void addEdge(Node from, Node to, String edgeLabel, String color) {
      this.nodes.add(from);
      this.nodes.add(to);
      this.addEdge(PagToDotDumper.makeNodeName(from), PagToDotDumper.makeNodeName(to), edgeLabel, color);
   }

   private void addEdge(String from, String to, String edgeLabel, String color) {
      StringBuffer tmp = new StringBuffer();
      tmp.append("    ");
      tmp.append(from);
      tmp.append(" -> ");
      tmp.append(to);
      tmp.append(" [label=\"");
      tmp.append(edgeLabel);
      tmp.append("\", color=");
      tmp.append(color);
      tmp.append("];");
      this.edges.add(tmp.toString());
   }

   public void addNew(AllocNode from, VarNode to) {
      this.addEdge((Node)to, (Node)from, "", "yellow");
   }

   public void addCall(VarNode from, VarNode to, Integer callSite) {
      this.addEdge((Node)to, (Node)from, callSite.toString(), "blue");
   }

   public void addMatch(VarNode from, VarNode to) {
      this.addEdge((Node)to, (Node)from, "", "brown");
   }

   public void addLoad(FieldRefNode from, VarNode to) {
      this.addEdge((Node)to, (Node)from.getBase(), from.getField().toString(), "green");
   }

   public void addStore(VarNode from, FieldRefNode to) {
      this.addEdge((Node)to.getBase(), (Node)from, to.getField().toString(), "red");
   }

   public int numEdges() {
      return this.edges.size();
   }

   public void dump(String filename) {
      PrintWriter pw = null;

      try {
         pw = new PrintWriter(new FileOutputStream(filename));
      } catch (FileNotFoundException var6) {
         logger.error((String)var6.getMessage(), (Throwable)var6);
      }

      pw.println("digraph G {");
      Predicate<Node> falsePred = new Predicate<Node>() {
         public boolean test(Node obj_) {
            return false;
         }
      };
      Iterator var4 = this.nodes.iterator();

      while(var4.hasNext()) {
         Node node = (Node)var4.next();
         pw.println(PagToDotDumper.makeDotNodeLabel(node, falsePred));
      }

      var4 = this.edges.iterator();

      while(var4.hasNext()) {
         String edge = (String)var4.next();
         pw.println(edge);
      }

      pw.println("}");
      pw.close();
   }
}
