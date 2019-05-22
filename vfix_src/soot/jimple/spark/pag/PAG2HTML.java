package soot.jimple.spark.pag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import soot.SootMethod;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.util.HashMultiMap;
import soot.util.MultiMap;

public class PAG2HTML {
   protected PAG pag;
   protected String output_dir;
   protected MultiMap mergedNodes = new HashMultiMap();
   protected MultiMap methodToNodes = new HashMultiMap();

   public PAG2HTML(PAG pag, String output_dir) {
      this.pag = pag;
      this.output_dir = output_dir;
   }

   public void dump() {
      Iterator vIt = this.pag.getVarNodeNumberer().iterator();

      SootMethod m;
      while(vIt.hasNext()) {
         VarNode v = (VarNode)vIt.next();
         this.mergedNodes.put(v.getReplacement(), v);
         if (v instanceof LocalVarNode) {
            m = ((LocalVarNode)v).getMethod();
            if (m != null) {
               this.methodToNodes.put(m, v);
            }
         }
      }

      try {
         JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(new File(this.output_dir, "pag.jar")));
         Iterator mIt = this.mergedNodes.keySet().iterator();

         while(mIt.hasNext()) {
            VarNode v = (VarNode)mIt.next();
            this.dumpVarNode(v, jarOut);
         }

         mIt = this.methodToNodes.keySet().iterator();

         while(mIt.hasNext()) {
            m = (SootMethod)mIt.next();
            this.dumpMethod(m, jarOut);
         }

         this.addSymLinks(this.pag.getVarNodeNumberer().iterator(), jarOut);
         jarOut.close();
      } catch (IOException var4) {
         throw new RuntimeException("Couldn't dump html" + var4);
      }
   }

   protected void dumpVarNode(VarNode v, JarOutputStream jarOut) throws IOException {
      jarOut.putNextEntry(new ZipEntry("nodes/n" + v.getNumber() + ".html"));
      final PrintWriter out = new PrintWriter(jarOut);
      out.println("<html>");
      out.println("Green node for:");
      out.println(this.varNodeReps(v));
      out.println("Declared type: " + v.getType());
      out.println("<hr>Reaching blue nodes:");
      out.println("<ul>");
      v.getP2Set().forall(new P2SetVisitor() {
         public final void visit(Node n) {
            out.println("<li>" + PAG2HTML.htmlify(n.toString()));
         }
      });
      out.println("</ul>");
      out.println("<hr>Outgoing edges:");
      Node[] succs = this.pag.simpleLookup(v);
      Node[] var5 = succs;
      int var6 = succs.length;

      int var7;
      Node element;
      VarNode succ;
      for(var7 = 0; var7 < var6; ++var7) {
         element = var5[var7];
         succ = (VarNode)element;
         out.println(this.varNodeReps(succ));
      }

      out.println("<hr>Incoming edges: ");
      succs = this.pag.simpleInvLookup(v);
      var5 = succs;
      var6 = succs.length;

      for(var7 = 0; var7 < var6; ++var7) {
         element = var5[var7];
         succ = (VarNode)element;
         out.println(this.varNodeReps(succ));
      }

      out.println("</html>");
      out.flush();
   }

   protected String varNodeReps(VarNode v) {
      StringBuffer ret = new StringBuffer();
      ret.append("<ul>\n");
      Iterator vvIt = this.mergedNodes.get(v).iterator();

      while(vvIt.hasNext()) {
         VarNode vv = (VarNode)vvIt.next();
         ret.append(this.varNode("", vv));
      }

      ret.append("</ul>\n");
      return ret.toString();
   }

   protected String varNode(String dirPrefix, VarNode vv) {
      StringBuffer ret = new StringBuffer();
      ret.append("<li><a href=\"" + dirPrefix + "n" + vv.getNumber() + ".html\">");
      if (vv.getVariable() != null) {
         ret.append("" + htmlify(vv.getVariable().toString()));
      }

      ret.append("GlobalVarNode");
      ret.append("</a><br>");
      ret.append("<li>Context: ");
      ret.append("" + (vv.context() == null ? "null" : htmlify(vv.context().toString())));
      ret.append("</a><br>");
      if (vv instanceof LocalVarNode) {
         LocalVarNode lvn = (LocalVarNode)vv;
         SootMethod m = lvn.getMethod();
         if (m != null) {
            ret.append("<a href=\"../" + this.toFileName(m.toString()) + ".html\">");
            ret.append(htmlify(m.toString()) + "</a><br>");
         }
      }

      ret.append(htmlify(vv.getType().toString()) + "\n");
      return ret.toString();
   }

   protected static String htmlify(String s) {
      StringBuffer b = new StringBuffer(s);

      for(int i = 0; i < b.length(); ++i) {
         if (b.charAt(i) == '<') {
            b.replace(i, i + 1, "&lt;");
         }

         if (b.charAt(i) == '>') {
            b.replace(i, i + 1, "&gt;");
         }
      }

      return b.toString();
   }

   protected void dumpMethod(SootMethod m, JarOutputStream jarOut) throws IOException {
      jarOut.putNextEntry(new ZipEntry("" + this.toFileName(m.toString()) + ".html"));
      PrintWriter out = new PrintWriter(jarOut);
      out.println("<html>");
      out.println("This is method " + htmlify(m.toString()) + "<hr>");
      Iterator it = this.methodToNodes.get(m).iterator();

      while(it.hasNext()) {
         out.println(this.varNode("nodes/", (VarNode)it.next()));
      }

      out.println("</html>");
      out.flush();
   }

   protected void addSymLinks(Iterator nodes, JarOutputStream jarOut) throws IOException {
      jarOut.putNextEntry(new ZipEntry("symlinks.sh"));
      PrintWriter out = new PrintWriter(jarOut);
      out.println("#!/bin/sh");

      while(nodes.hasNext()) {
         VarNode v = (VarNode)nodes.next();
         VarNode rep = (VarNode)v.getReplacement();
         if (v != rep) {
            out.println("ln -s n" + rep.getNumber() + ".html n" + v.getNumber() + ".html");
         }
      }

      out.flush();
   }

   protected String toFileName(String s) {
      StringBuffer ret = new StringBuffer();

      for(int i = 0; i < s.length(); ++i) {
         char c = s.charAt(i);
         if (c == '<') {
            ret.append('{');
         } else if (c == '>') {
            ret.append('}');
         } else {
            ret.append(c);
         }
      }

      return ret.toString();
   }
}
