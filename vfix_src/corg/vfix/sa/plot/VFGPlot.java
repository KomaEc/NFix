package corg.vfix.sa.plot;

import corg.vfix.sa.vfg.VFG;
import corg.vfix.sa.vfg.VFGEdge;
import corg.vfix.sa.vfg.VFGNode;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VFGPlot {
   private static String file_dot;
   private static String file_path;

   public static void init(String dotPath) {
      file_path = dotPath;
      file_dot = dotPath + "/vfg.dot";
   }

   public static void plot(VFG vfg, String fileName) throws IOException {
      file_dot = file_path + "/" + fileName;
      plot(vfg);
      file_dot = file_path + "/vfg.dot";
   }

   public static void plot(VFG vfg) throws IOException {
      if (file_dot != null && vfg != null) {
         File file = new File(file_dot);
         if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
         }

         FileWriter writer = new FileWriter(file);
         plotHead(writer);
         plotNodes(writer, vfg.getNodes());
         plotEdges(writer, vfg.getEdges());
         plotTail(writer);
         writer.close();
      }
   }

   public static void main(String[] args) throws IOException {
      String projectDir = System.getProperty("user.dir") + "/";
      String dotPath = projectDir + "vfgOutput/";
      init(dotPath);
      plot((VFG)null);
   }

   private static void plotNodes(FileWriter writer, ArrayList<VFGNode> nodes) throws IOException {
      for(Iterator var3 = nodes.iterator(); var3.hasNext(); writer.write("\n")) {
         VFGNode node = (VFGNode)var3.next();
         plotNode(writer, node);
         if (node.isExecuted()) {
            writer.write(" [style=bold]");
         }

         if (node.getNodeType() == 2) {
            writer.write(" [color=red]");
         }

         if (node.getNodeType() == 0) {
            writer.write(" [color=gold]");
         }
      }

   }

   private static void plotNode(FileWriter writer, VFGNode node) throws IOException {
      String nodeID = "\"";
      int lineNumber = node.getStmt().getJavaSourceStartLineNumber();
      if (lineNumber == -1) {
         nodeID = nodeID + node.getMethod().getJavaSourceStartLineNumber() + ":";
      } else {
         nodeID = nodeID + lineNumber + ": ";
      }

      nodeID = nodeID + node.getStmt().toString();
      nodeID = nodeID + "\"";
      writer.write(nodeID);
   }

   private static void plotEdges(FileWriter writer, List<VFGEdge> edges) throws IOException {
      Iterator var3 = edges.iterator();

      while(var3.hasNext()) {
         VFGEdge edge = (VFGEdge)var3.next();
         plotEdge(writer, edge);
         writer.write("\n");
      }

   }

   private static void plotEdge(FileWriter writer, VFGEdge edge) throws IOException {
      VFGNode from = edge.getFrom();
      VFGNode to = edge.getTo();
      plotNode(writer, from);
      writer.write("->");
      plotNode(writer, to);
      if (!from.isExecuted() || !to.isExecuted()) {
         writer.write(" [style=dotted]");
      }

      writer.write(";\n");
   }

   private static void plotHead(FileWriter writer) throws IOException {
      writer.write("digraph \"vfg\" {\n");
      writer.write("    label=\"vfg\";\n");
      writer.write("node [shape=box];\n");
   }

   private static void plotTail(FileWriter writer) throws IOException {
      writer.write("}");
   }
}
