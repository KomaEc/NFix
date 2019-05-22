package soot;

import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityRef;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;

public class BriefUnitPrinter extends LabeledUnitPrinter {
   private boolean baf;
   private boolean eatSpace = false;

   public BriefUnitPrinter(Body body) {
      super(body);
   }

   public void startUnit(Unit u) {
      super.startUnit(u);
      if (u instanceof Stmt) {
         this.baf = false;
      } else {
         this.baf = true;
      }

   }

   public void methodRef(SootMethodRef m) {
      this.handleIndent();
      if (!this.baf && m.resolve().isStatic()) {
         this.output.append(m.declaringClass().getName());
         this.literal(".");
      }

      this.output.append(m.name());
   }

   public void fieldRef(SootFieldRef f) {
      this.handleIndent();
      if (this.baf || f.resolve().isStatic()) {
         this.output.append(f.declaringClass().getName());
         this.literal(".");
      }

      this.output.append(f.name());
   }

   public void identityRef(IdentityRef r) {
      this.handleIndent();
      if (r instanceof ThisRef) {
         this.literal("@this");
      } else if (r instanceof ParameterRef) {
         ParameterRef pr = (ParameterRef)r;
         this.literal("@parameter" + pr.getIndex());
      } else {
         if (!(r instanceof CaughtExceptionRef)) {
            throw new RuntimeException();
         }

         this.literal("@caughtexception");
      }

   }

   public void literal(String s) {
      this.handleIndent();
      if (this.eatSpace && s.equals(" ")) {
         this.eatSpace = false;
      } else {
         this.eatSpace = false;
         if (this.baf || !s.equals("staticinvoke") && !s.equals("virtualinvoke") && !s.equals("interfaceinvoke")) {
            this.output.append(s);
         } else {
            this.eatSpace = true;
         }
      }
   }

   public void type(Type t) {
      this.handleIndent();
      this.output.append(t.toString());
   }
}
