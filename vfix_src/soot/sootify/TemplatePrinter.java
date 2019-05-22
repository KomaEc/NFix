package soot.sootify;

import java.io.PrintWriter;
import java.util.Iterator;
import soot.Body;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;

public class TemplatePrinter {
   private PrintWriter out;
   private int indentationLevel = 0;

   public TemplatePrinter(Singletons.Global g) {
   }

   public static TemplatePrinter v() {
      return G.v().soot_sootify_TemplatePrinter();
   }

   public void printTo(SootClass c, PrintWriter out) {
      this.out = out;
      this.printTo(c);
   }

   private void printTo(SootClass c) {
      String templateClassName = c.getName().replace('.', '_') + "_Maker";
      this.println("import java.util.*;");
      this.println("import soot.*;");
      this.println("import soot.jimple.*;");
      this.println("import soot.util.*;");
      this.println("");
      this.print("public class ");
      this.print(templateClassName);
      this.println(" {");
      this.println("private static Local localByName(Body b, String name) {");
      this.println("\tfor(Local l: b.getLocals()) {");
      this.println("\t\tif(l.getName().equals(name))");
      this.println("\t\t\treturn l;");
      this.println("\t}");
      this.println("\tthrow new IllegalArgumentException(\"No such local: \"+name);");
      this.println("}");
      this.indent();
      this.println("public void create() {");
      this.indent();
      this.println("SootClass c = new SootClass(\"" + c.getName() + "\");");
      this.println("c.setApplicationClass();");
      this.println("Scene.v().addClass(c);");

      int i;
      for(i = 0; i < c.getMethodCount(); ++i) {
         this.println("createMethod" + i + "(c);");
      }

      this.closeMethod();
      i = 0;
      Iterator var4 = c.getMethods().iterator();

      while(true) {
         SootMethod m;
         do {
            if (!var4.hasNext()) {
               this.println("}");
               return;
            }

            m = (SootMethod)var4.next();
            this.newMethod("createMethod" + i);
            this.println("SootMethod m = new SootMethod(\"" + m.getName() + "\",null,null);");
            this.println("Body b = Jimple.v().newBody(m);");
            this.println("m.setActiveBody(b);");
         } while(!m.hasActiveBody());

         Body b = m.getActiveBody();
         this.println("Chain<Local> locals = b.getLocals();");
         Iterator var7 = b.getLocals().iterator();

         while(var7.hasNext()) {
            Local l = (Local)var7.next();
            this.println("locals.add(Jimple.v().newLocal(\"" + l.getName() + "\", RefType.v(\"" + l.getType() + "\")));");
         }

         this.println("Chain<Unit> units = b.getUnits();");
         StmtTemplatePrinter sw = new StmtTemplatePrinter(this, b.getUnits());
         Iterator var11 = b.getUnits().iterator();

         while(var11.hasNext()) {
            Unit u = (Unit)var11.next();
            u.apply(sw);
         }

         this.closeMethod();
         ++i;
      }
   }

   private void closeMethod() {
      this.unindent();
      this.println("}");
      this.unindent();
      this.println("");
   }

   private void newMethod(String name) {
      this.indent();
      this.println("public void " + name + "(SootClass c) {");
      this.indent();
   }

   public void printlnNoIndent(String s) {
      this.printNoIndent(s);
      this.print("\n");
   }

   public void println(String s) {
      this.print(s);
      this.print("\n");
   }

   public void printNoIndent(String s) {
      this.out.print(s);
   }

   public void print(String s) {
      for(int i = 0; i < this.indentationLevel; ++i) {
         this.out.print("  ");
      }

      this.out.print(s);
   }

   public void indent() {
      ++this.indentationLevel;
   }

   public void unindent() {
      --this.indentationLevel;
   }

   public void openBlock() {
      this.println("{");
      this.indent();
   }

   public void closeBlock() {
      this.unindent();
      this.println("}");
   }
}
