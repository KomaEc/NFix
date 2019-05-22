package soot;

import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityRef;
import soot.jimple.ParameterRef;
import soot.jimple.ThisRef;

public class NormalUnitPrinter extends LabeledUnitPrinter {
   public NormalUnitPrinter(Body body) {
      super(body);
   }

   public void type(Type t) {
      this.handleIndent();
      String s = t == null ? "<null>" : t.toQuotedString();
      this.output.append(s);
   }

   public void methodRef(SootMethodRef m) {
      this.handleIndent();
      this.output.append(m.getSignature());
   }

   public void fieldRef(SootFieldRef f) {
      this.handleIndent();
      this.output.append(f.getSignature());
   }

   public void identityRef(IdentityRef r) {
      this.handleIndent();
      if (r instanceof ThisRef) {
         this.literal("@this: ");
         this.type(r.getType());
      } else if (r instanceof ParameterRef) {
         ParameterRef pr = (ParameterRef)r;
         this.literal("@parameter" + pr.getIndex() + ": ");
         this.type(r.getType());
      } else {
         if (!(r instanceof CaughtExceptionRef)) {
            throw new RuntimeException();
         }

         this.literal("@caughtexception");
      }

   }

   public void literal(String s) {
      this.handleIndent();
      this.output.append(s);
   }
}
