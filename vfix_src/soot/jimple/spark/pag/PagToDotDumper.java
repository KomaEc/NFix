package soot.jimple.spark.pag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootField;
import soot.SootMethod;
import soot.jimple.spark.ondemand.genericutil.Predicate;
import soot.jimple.spark.sets.P2SetVisitor;

public class PagToDotDumper {
   private static final Logger logger = LoggerFactory.getLogger(PagToDotDumper.class);
   public static final int TRACE_MAX_LVL = 99;
   private PAG pag;
   private HashMap<Node, Node[]> vmatches;
   private HashMap<Node, Node[]> invVmatches;
   private static final Predicate<Node> emptyP2SetPred = new Predicate<Node>() {
      public boolean test(Node n) {
         return !(n instanceof AllocNode) && n.getP2Set().isEmpty();
      }
   };

   public PagToDotDumper(PAG pag) {
      this.pag = pag;
      this.vmatches = new HashMap();
      this.invVmatches = new HashMap();
   }

   private void buildVmatchEdges() {
      Iterator iter = this.pag.loadSourcesIterator();

      label45:
      while(iter.hasNext()) {
         FieldRefNode frn1 = (FieldRefNode)iter.next();
         Iterator iter2 = this.pag.storeInvSourcesIterator();

         while(true) {
            FieldRefNode frn2;
            VarNode base1;
            VarNode base2;
            do {
               do {
                  if (!iter2.hasNext()) {
                     continue label45;
                  }

                  frn2 = (FieldRefNode)iter2.next();
                  base1 = frn1.getBase();
                  base2 = frn2.getBase();
               } while(!frn1.getField().equals(frn2.getField()));
            } while(!base1.getP2Set().hasNonEmptyIntersection(base2.getP2Set()));

            Node[] src = this.pag.loadLookup(frn1);
            Node[] dst = this.pag.storeInvLookup(frn2);

            int i;
            for(i = 0; i < src.length; ++i) {
               this.vmatches.put(src[i], dst);
            }

            for(i = 0; i < dst.length; ++i) {
               this.invVmatches.put(dst[i], src);
            }
         }
      }

   }

   private void debug(FieldRefNode frn1, FieldRefNode frn2, VarNode base1, VarNode base2) {
      if (base1 instanceof LocalVarNode && base2 instanceof LocalVarNode) {
         LocalVarNode lvn1 = (LocalVarNode)base1;
         LocalVarNode lvn2 = (LocalVarNode)base2;
         if (lvn1.getMethod().getDeclaringClass().getName().equals("java.util.Hashtable$ValueCollection") && lvn1.getMethod().getName().equals("contains") && lvn2.getMethod().getDeclaringClass().getName().equals("java.util.Hashtable$ValueCollection") && lvn2.getMethod().getName().equals("<init>")) {
            System.err.println("Method: " + lvn1.getMethod().getName());
            System.err.println(makeLabel(frn1));
            System.err.println("Base: " + base1.getVariable());
            System.err.println("Field: " + frn1.getField());
            System.err.println(makeLabel(frn2));
            System.err.println("Base: " + base2.getVariable());
            System.err.println("Field: " + frn2.getField());
            if (frn1.getField().equals(frn2.getField())) {
               System.err.println("field match");
               if (base1.getP2Set().hasNonEmptyIntersection(base2.getP2Set())) {
                  System.err.println("non empty");
               } else {
                  System.err.println("b1: " + base1.getP2Set());
                  System.err.println("b2: " + base2.getP2Set());
               }
            }
         }
      }

   }

   private static String translateEdge(Node src, Node dest, String label) {
      return makeNodeName(src) + " -> " + makeNodeName(dest) + " [label=\"" + label + "\"];";
   }

   public static String makeDotNodeLabel(Node n, Predicate<Node> p) {
      String color = "";
      if (p.test(n)) {
         color = ", color=red";
      }

      String label;
      if (n instanceof LocalVarNode) {
         label = makeLabel((LocalVarNode)n);
      } else if (n instanceof AllocNode) {
         label = makeLabel((AllocNode)n);
      } else if (n instanceof FieldRefNode) {
         label = makeLabel((FieldRefNode)n);
      } else {
         label = n.toString();
      }

      return makeNodeName(n) + "[label=\"" + label + "\"" + color + "];";
   }

   private static String translateLabel(Node n) {
      return makeDotNodeLabel(n, emptyP2SetPred);
   }

   private boolean isDefinedIn(LocalVarNode lvNode, String cName, String mName) {
      return lvNode.getMethod() != null && lvNode.getMethod().getDeclaringClass().getName().equals(cName) && lvNode.getMethod().getName().equals(mName);
   }

   private void printOneNode(VarNode node) {
      PrintStream ps = System.err;
      ps.println(makeLabel(node));
      Node[] succs = this.pag.simpleInvLookup(node);
      ps.println("assign");
      ps.println("======");

      int i;
      for(i = 0; i < succs.length; ++i) {
         ps.println(succs[i]);
      }

      succs = this.pag.allocInvLookup(node);
      ps.println("new");
      ps.println("======");

      for(i = 0; i < succs.length; ++i) {
         ps.println(succs[i]);
      }

      succs = this.pag.loadInvLookup(node);
      ps.println("load");
      ps.println("======");

      for(i = 0; i < succs.length; ++i) {
         ps.println(succs[i]);
      }

      succs = this.pag.storeLookup(node);
      ps.println("store");
      ps.println("======");

      for(i = 0; i < succs.length; ++i) {
         ps.println(succs[i]);
      }

   }

   public void dumpP2SetsForLocals(String fName, String mName) throws FileNotFoundException {
      FileOutputStream fos = new FileOutputStream(new File(fName));
      PrintStream ps = new PrintStream(fos);
      ps.println("digraph G {");
      this.dumpLocalP2Set(mName, ps);
      ps.print("}");
   }

   private void dumpLocalP2Set(String mName, PrintStream ps) {
      Iterator iter = this.pag.getVarNodeNumberer().iterator();

      while(iter.hasNext()) {
         VarNode vNode = (VarNode)iter.next();
         if (vNode instanceof LocalVarNode) {
            LocalVarNode lvNode = (LocalVarNode)vNode;
            if (lvNode.getMethod() != null && lvNode.getMethod().getName().equals(mName)) {
               ps.println("\t" + makeNodeName(lvNode) + " [label=\"" + makeLabel(lvNode) + "\"];");
               lvNode.getP2Set().forall(new PagToDotDumper.P2SetToDotPrinter(lvNode, ps));
            }
         }
      }

   }

   public void dumpPAGForMethod(String fName, String cName, String mName) throws FileNotFoundException {
      FileOutputStream fos = new FileOutputStream(new File(fName));
      PrintStream ps = new PrintStream(fos);
      ps.println("digraph G {");
      ps.println("\trankdir=LR;");
      this.dumpLocalPAG(cName, mName, ps);
      ps.print("}");

      try {
         fos.close();
      } catch (IOException var7) {
         logger.error((String)var7.getMessage(), (Throwable)var7);
      }

      ps.close();
   }

   private void dumpLocalPAG(String cName, String mName, PrintStream ps) {
      Iterator iter = this.pag.getVarNodeNumberer().iterator();

      while(iter.hasNext()) {
         Node node = (Node)iter.next();
         if (node instanceof LocalVarNode) {
            LocalVarNode lvNode = (LocalVarNode)node;
            if (this.isDefinedIn(lvNode, cName, mName)) {
               this.dumpForwardReachableNodesFrom(lvNode, ps);
            }
         }
      }

   }

   private void dumpForwardReachableNodesFrom(LocalVarNode lvNode, PrintStream ps) {
      ps.println("\t" + translateLabel(lvNode));
      Node[] succs = this.pag.simpleInvLookup(lvNode);

      int i;
      for(i = 0; i < succs.length; ++i) {
         ps.println("\t" + translateLabel(succs[i]));
         ps.println("\t" + translateEdge(lvNode, succs[i], "assign"));
      }

      succs = this.pag.allocInvLookup(lvNode);

      for(i = 0; i < succs.length; ++i) {
         ps.println("\t" + translateLabel(succs[i]));
         ps.println("\t" + translateEdge(lvNode, succs[i], "new"));
      }

      succs = this.pag.loadInvLookup(lvNode);

      FieldRefNode frNode;
      for(i = 0; i < succs.length; ++i) {
         frNode = (FieldRefNode)succs[i];
         ps.println("\t" + translateLabel(frNode));
         ps.println("\t" + translateLabel(frNode.getBase()));
         ps.println("\t" + translateEdge(lvNode, frNode, "load"));
         ps.println("\t" + translateEdge(frNode, frNode.getBase(), "getBase"));
      }

      succs = this.pag.storeLookup(lvNode);

      for(i = 0; i < succs.length; ++i) {
         frNode = (FieldRefNode)succs[i];
         ps.println("\t" + translateLabel(frNode));
         ps.println("\t" + translateLabel(frNode.getBase()));
         ps.println("\t" + translateEdge(frNode, lvNode, "store"));
         ps.println("\t" + translateEdge(frNode, frNode.getBase(), "getBase"));
      }

   }

   public void traceNode(int id) {
      this.buildVmatchEdges();
      String fName = "trace." + id + ".dot";

      try {
         FileOutputStream fos = new FileOutputStream(new File(fName));
         PrintStream ps = new PrintStream(fos);
         ps.println("digraph G {");
         Iterator iter = this.pag.getVarNodeNumberer().iterator();

         while(iter.hasNext()) {
            VarNode n = (VarNode)iter.next();
            if (n.getNumber() == id) {
               LocalVarNode lvn = (LocalVarNode)n;
               this.printOneNode(lvn);
               this.trace(lvn, ps, new HashSet(), 99);
            }
         }

         ps.print("}");
         ps.close();
         fos.close();
      } catch (IOException var8) {
         logger.error((String)var8.getMessage(), (Throwable)var8);
      }

   }

   public void traceNode(String cName, String mName, String varName) {
      String mName2 = mName;
      if (mName.indexOf(60) == 0) {
         mName2 = mName.substring(1, mName.length() - 1);
      }

      this.traceLocalVarNode("trace." + cName + "." + mName2 + "." + varName + ".dot", cName, mName, varName);
   }

   public void traceLocalVarNode(String fName, String cName, String mName, String varName) {
      this.buildVmatchEdges();

      try {
         FileOutputStream fos = new FileOutputStream(new File(fName));
         PrintStream ps = new PrintStream(fos);
         ps.println("digraph G {");
         Iterator iter = this.pag.getVarNodeNumberer().iterator();

         while(iter.hasNext()) {
            VarNode n = (VarNode)iter.next();
            if (n instanceof LocalVarNode) {
               LocalVarNode lvn = (LocalVarNode)n;
               if (lvn.getMethod() != null && this.isDefinedIn(lvn, cName, mName) && lvn.getVariable().toString().equals(varName)) {
                  this.trace(lvn, ps, new HashSet(), 10);
               }
            }
         }

         ps.print("}");
         ps.close();
         fos.close();
      } catch (IOException var10) {
         logger.error((String)var10.getMessage(), (Throwable)var10);
      }

   }

   private void trace(VarNode node, PrintStream ps, HashSet<Node> visitedNodes, int level) {
      if (level >= 1) {
         ps.println("\t" + translateLabel(node));
         Node[] succs = this.pag.simpleInvLookup(node);

         int i;
         for(i = 0; i < succs.length; ++i) {
            if (!visitedNodes.contains(succs[i])) {
               ps.println("\t" + translateLabel(succs[i]));
               ps.println("\t" + translateEdge(node, succs[i], "assign"));
               visitedNodes.add(succs[i]);
               this.trace((VarNode)succs[i], ps, visitedNodes, level - 1);
            }
         }

         succs = this.pag.allocInvLookup(node);

         for(i = 0; i < succs.length; ++i) {
            if (!visitedNodes.contains(succs[i])) {
               ps.println("\t" + translateLabel(succs[i]));
               ps.println("\t" + translateEdge(node, succs[i], "new"));
            }
         }

         succs = (Node[])this.vmatches.get(node);
         if (succs != null) {
            for(i = 0; i < succs.length; ++i) {
               if (!visitedNodes.contains(succs[i])) {
                  ps.println("\t" + translateLabel(succs[i]));
                  ps.println("\t" + translateEdge(node, succs[i], "vmatch"));
                  this.trace((VarNode)succs[i], ps, visitedNodes, level - 1);
               }
            }
         }

      }
   }

   public static String makeNodeName(Node n) {
      return "node_" + n.getNumber();
   }

   public static String makeLabel(AllocNode n) {
      return n.getNewExpr().toString();
   }

   public static String makeLabel(LocalVarNode n) {
      SootMethod sm = n.getMethod();
      return "LV " + n.getVariable().toString() + " " + n.getNumber() + "\\n" + sm.getDeclaringClass() + "\\n" + sm.getName();
   }

   public static String makeLabel(FieldRefNode node) {
      if (node.getField() instanceof SootField) {
         SootField sf = (SootField)node.getField();
         return "FNR " + makeLabel(node.getBase()) + "." + sf.getName();
      } else {
         return "FNR " + makeLabel(node.getBase()) + "." + node.getField();
      }
   }

   public static String makeLabel(VarNode base) {
      return base instanceof LocalVarNode ? makeLabel((LocalVarNode)base) : base.toString();
   }

   class P2SetToDotPrinter extends P2SetVisitor {
      private final Node curNode;
      private final PrintStream ps;

      P2SetToDotPrinter(Node curNode, PrintStream ps) {
         this.curNode = curNode;
         this.ps = ps;
      }

      public void visit(Node n) {
         this.ps.println("\t" + PagToDotDumper.makeNodeName(n) + " [label=\"" + PagToDotDumper.makeLabel((AllocNode)n) + "\"];");
         this.ps.print("\t" + PagToDotDumper.makeNodeName(this.curNode) + " -> ");
         this.ps.println(PagToDotDumper.makeNodeName(n) + ";");
      }
   }
}
