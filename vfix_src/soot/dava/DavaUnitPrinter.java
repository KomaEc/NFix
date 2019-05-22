package soot.dava;

import soot.AbstractUnitPrinter;
import soot.ArrayType;
import soot.RefType;
import soot.SootFieldRef;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.dava.toolkits.base.renamer.RemoveFullyQualifiedName;
import soot.jimple.ClassConstant;
import soot.jimple.Constant;
import soot.jimple.IdentityRef;
import soot.jimple.ThisRef;

public class DavaUnitPrinter extends AbstractUnitPrinter {
   DavaBody body;
   private boolean eatSpace = false;

   public DavaUnitPrinter(DavaBody body) {
      this.body = body;
   }

   public void methodRef(SootMethodRef m) {
      this.handleIndent();
      this.output.append(m.name());
   }

   public void fieldRef(SootFieldRef f) {
      this.handleIndent();
      this.output.append(f.name());
   }

   public void identityRef(IdentityRef r) {
      this.handleIndent();
      if (r instanceof ThisRef) {
         this.literal("this");
      } else {
         throw new RuntimeException();
      }
   }

   public void literal(String s) {
      this.handleIndent();
      if (this.eatSpace && s.equals(" ")) {
         this.eatSpace = false;
      } else {
         this.eatSpace = false;
         if (!s.equals("staticinvoke") && !s.equals("virtualinvoke") && !s.equals("interfaceinvoke")) {
            this.output.append(s);
         } else {
            this.eatSpace = true;
         }
      }
   }

   public void type(Type t) {
      this.handleIndent();
      if (t instanceof RefType) {
         String name = ((RefType)t).getSootClass().getJavaStyleName();
         if (!name.equals(((RefType)t).getSootClass().toString())) {
            name = RemoveFullyQualifiedName.getReducedName(this.body.getImportList(), ((RefType)t).getSootClass().toString(), t);
         }

         this.output.append(name);
      } else if (t instanceof ArrayType) {
         ((ArrayType)t).toString(this);
      } else {
         this.output.append(t.toString());
      }

   }

   public void unitRef(Unit u, boolean branchTarget) {
      throw new RuntimeException("Dava doesn't have unit references!");
   }

   public void constant(Constant c) {
      if (c instanceof ClassConstant) {
         this.handleIndent();
         String fullClassName = ((ClassConstant)c).value.replaceAll("/", ".");
         this.output.append(fullClassName + ".class");
      } else {
         super.constant(c);
      }

   }

   public void addNot() {
      this.output.append(" !");
   }

   public void addAggregatedOr() {
      this.output.append(" || ");
   }

   public void addAggregatedAnd() {
      this.output.append(" && ");
   }

   public void addLeftParen() {
      this.output.append(" (");
   }

   public void addRightParen() {
      this.output.append(") ");
   }

   public void printString(String s) {
      this.output.append(s);
   }
}
