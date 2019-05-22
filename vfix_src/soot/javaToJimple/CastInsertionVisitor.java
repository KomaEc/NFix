package soot.javaToJimple;

import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.NodeVisitor;

public class CastInsertionVisitor extends AscriptionVisitor {
   public CastInsertionVisitor(Job job, TypeSystem ts, NodeFactory nf) {
      super(job, ts, nf);
   }

   public Expr ascribe(Expr e, Type toType) {
      Type fromType = e.type();
      if (toType == null) {
         return e;
      } else if (toType.isVoid()) {
         return e;
      } else {
         Position p = e.position();
         if (toType.equals(fromType)) {
            return e;
         } else if (toType.isPrimitive() && fromType.isPrimitive()) {
            Expr newExpr;
            if (!fromType.isFloat() && !fromType.isLong() && !fromType.isDouble()) {
               newExpr = this.nf.Cast(p, this.nf.CanonicalTypeNode(p, toType), e).type(toType);
            } else if (!toType.isFloat() && !toType.isLong() && !toType.isDouble() && !toType.isInt()) {
               newExpr = this.nf.Cast(p, this.nf.CanonicalTypeNode(p, toType), this.nf.Cast(p, this.nf.CanonicalTypeNode(p, this.ts.Int()), e).type(this.ts.Int())).type(toType);
            } else {
               newExpr = this.nf.Cast(p, this.nf.CanonicalTypeNode(p, toType), e).type(toType);
            }

            return newExpr;
         } else {
            return e;
         }
      }
   }

   public Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
      n = super.leaveCall(old, n, v);
      return n;
   }
}
