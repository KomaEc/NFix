package corg.vfix.sa.plot;

import corg.vfix.sa.cg.CG;
import corg.vfix.sa.cg.CGEdge;
import corg.vfix.sa.cg.CGNode;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.SootMethod;

public class CGPlot {
   private static String file_dot;

   public static void init(String dotPath) {
      file_dot = dotPath + "/cg.dot";
   }

   public static void plot(CG cg) throws IOException {
      if (file_dot != null && cg != null) {
         File file = new File(file_dot);
         if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
         }

         FileWriter writer = new FileWriter(file);
         plotHead(writer);
         plotNodes(writer, cg.getNodes(), cg);
         plotEdges(writer, cg.getEdges());
         plotTail(writer);
         writer.close();
      }
   }

   private static void plotNodes(FileWriter writer, ArrayList<CGNode> nodes, CG cg) throws IOException {
      ArrayList<CGNode> plotted = new ArrayList();
      Iterator var5 = nodes.iterator();

      while(var5.hasNext()) {
         CGNode node = (CGNode)var5.next();
         if (!isPlotted(plotted, node)) {
            plotNode(writer, node);
            if (cg.isMainNode(node)) {
               writer.write(" [color=gold]");
            }

            if (cg.isSinkNode(node)) {
               writer.write(" [color=red]");
            }

            if (node.isExecuted()) {
               writer.write(" [style=bold]");
            }

            writer.write("\n");
         }
      }

   }

   private static boolean isPlotted(ArrayList<CGNode> nodes, CGNode node) {
      if (node.getMtd() == null) {
         return true;
      } else {
         Iterator var3 = nodes.iterator();

         while(var3.hasNext()) {
            CGNode n = (CGNode)var3.next();
            if (node.getMtd().equals(n.getMtd())) {
               return true;
            }
         }

         nodes.add(node);
         return false;
      }
   }

   private static boolean isPlotted(ArrayList<CGEdge> edges, CGEdge edge) {
      Iterator var3 = edges.iterator();

      CGEdge e;
      do {
         if (!var3.hasNext()) {
            edges.add(edge);
            return false;
         }

         e = (CGEdge)var3.next();
      } while(!e.getSrc().equals(edge.getSrc()) || !e.getTgt().equals(edge.getTgt()));

      return true;
   }

   private static void plotNode(FileWriter writer, CGNode node) throws IOException {
      String nodeID = "\"";
      SootMethod mtd = node.getMtd();
      if (mtd != null) {
         nodeID = nodeID + mtd.getJavaSourceStartLineNumber() + ": ";
      } else {
         nodeID = nodeID + "-1: ";
      }

      if (mtd != null) {
         if (mtd.isConstructor()) {
            nodeID = nodeID + mtd.getDeclaration();
         } else {
            nodeID = nodeID + mtd.getName();
         }
      } else {
         nodeID = nodeID + "UnknownMethod";
      }

      nodeID = nodeID + "\"";
      writer.write(nodeID);
   }

   private static void plotEdges(FileWriter writer, List<CGEdge> edges) throws IOException {
      ArrayList<CGEdge> plotted = new ArrayList();
      Iterator var4 = edges.iterator();

      while(var4.hasNext()) {
         CGEdge edge = (CGEdge)var4.next();
         if (!isPlotted(plotted, edge)) {
            plotEdge(writer, edge);
            writer.write("\n");
         }
      }

   }

   private static void plotEdge(FileWriter writer, CGEdge edge) throws IOException {
      CGNode from = edge.getSrc();
      CGNode to = edge.getTgt();
      plotNode(writer, from);
      writer.write("->");
      plotNode(writer, to);
      if (!to.isExecuted() || !from.isExecuted()) {
         writer.write(" [style=dotted]");
      }

      writer.write(";\n");
   }

   private static void plotHead(FileWriter writer) throws IOException {
      writer.write("digraph \"CallGraph\" {\n");
      writer.write("    label=\"CallGraph\";\n");
      writer.write("node [shape=box];\n");
   }

   private static void plotTail(FileWriter writer) throws IOException {
      writer.write("}");
   }
}
