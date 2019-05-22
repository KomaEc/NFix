package corg.vfix.sa.plot;

import corg.vfix.sa.ddg.DDG;
import corg.vfix.sa.ddg.DDGEdge;
import corg.vfix.sa.ddg.DDGNode;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class DDGPlot {
   private static String file_dot;

   public static void init(String dotPath) {
      file_dot = dotPath + "/ddg.dot";
   }

   public static void plot(DDG ddg) throws IOException {
      if (file_dot != null && ddg != null) {
         File file = new File(file_dot);
         if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
         }

         FileWriter writer = new FileWriter(file);
         plotHead(writer);
         plotNodes(writer, ddg.getNodes());
         plotEdges(writer, ddg.getEdges());
         plotTail(writer);
         writer.close();
      }
   }

   public static void main(String[] args) throws IOException {
      String projectDir = System.getProperty("user.dir") + "/";
      String dotPath = projectDir + "ddgOutput/";
      init(dotPath);
      plot((DDG)null);
   }

   private static void plotNodes(FileWriter writer, ArrayList<DDGNode> nodes) throws IOException {
      Iterator var3 = nodes.iterator();

      while(var3.hasNext()) {
         DDGNode node = (DDGNode)var3.next();
         plotNode(writer, node);
         writer.write("\n");
      }

   }

   private static void plotNode(FileWriter writer, DDGNode node) throws IOException {
      String nodeID = "\"";
      nodeID = nodeID + node.getUnit().getJavaSourceStartLineNumber() + ": ";
      nodeID = nodeID + node.getUnit().toString();
      nodeID = nodeID + "\"";
      writer.write(nodeID);
   }

   private static void plotEdges(FileWriter writer, ArrayList<DDGEdge> edges) throws IOException {
      Iterator var3 = edges.iterator();

      while(var3.hasNext()) {
         DDGEdge edge = (DDGEdge)var3.next();
         plotEdge(writer, edge);
         writer.write("\n");
      }

   }

   private static void plotEdge(FileWriter writer, DDGEdge edge) throws IOException {
      DDGNode from = edge.getFrom();
      DDGNode to = edge.getTo();
      plotNode(writer, from);
      writer.write("->");
      plotNode(writer, to);
      writer.write("[label=\"" + edge.getValue().toString() + "\"];\n");
   }

   private static void plotHead(FileWriter writer) throws IOException {
      writer.write("digraph \"ddg\" {\n");
      writer.write("    label=\"ddg\";\n");
      writer.write("node [shape=box];\n");
   }

   private static void plotTail(FileWriter writer) throws IOException {
      writer.write("}");
   }
}
