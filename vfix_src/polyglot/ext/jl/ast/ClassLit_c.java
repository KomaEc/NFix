package polyglot.ext.jl.ast;

import polyglot.ast.ClassLit;
import polyglot.ast.Node;
import polyglot.ast.TypeNode;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class ClassLit_c extends Lit_c implements ClassLit {
   protected TypeNode typeNode;

   public ClassLit_c(Position pos, TypeNode typeNode) {
      super(pos);
      this.typeNode = typeNode;
   }

   public TypeNode typeNode() {
      return this.typeNode;
   }

   public ClassLit typeNode(TypeNode typeNode) {
      if (this.typeNode == typeNode) {
         return this;
      } else {
         ClassLit_c n = (ClassLit_c)this.copy();
         n.typeNode = typeNode;
         return n;
      }
   }

   public Object objValue() {
      return null;
   }

   public Node visitChildren(NodeVisitor v) {
      TypeNode tn = (TypeNode)this.visitChild(this.typeNode, v);
      return this.typeNode(tn);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      return this.type(tc.typeSystem().Class());
   }

   public String toString() {
      return this.typeNode.toString() + ".class";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.begin(0);
      this.print(this.typeNode, w, tr);
      w.write(".class");
      w.end();
   }

   public boolean isConstant() {
      return false;
   }

   public Object constantValue() {
      return null;
   }
}
