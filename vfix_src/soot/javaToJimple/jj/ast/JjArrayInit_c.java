package soot.javaToJimple.jj.ast;

import java.util.Iterator;
import java.util.List;
import polyglot.ast.Expr;
import polyglot.ext.jl.ast.ArrayInit_c;
import polyglot.types.Type;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;

public class JjArrayInit_c extends ArrayInit_c {
   public JjArrayInit_c(Position pos, List elements) {
      super(pos, elements);
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      if (this.elements.isEmpty()) {
         return child.type();
      } else {
         Type t = av.toType();
         if (t == null) {
            return child.type();
         } else if (!t.isArray()) {
            throw new InternalCompilerError("Type of array initializer must be an array.", this.position());
         } else {
            t = t.toArray().base();
            Iterator i = this.elements.iterator();

            Expr e;
            do {
               if (!i.hasNext()) {
                  return child.type();
               }

               e = (Expr)i.next();
            } while(e != child);

            return t;
         }
      }
   }
}
