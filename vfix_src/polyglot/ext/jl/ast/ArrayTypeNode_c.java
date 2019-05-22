package polyglot.ext.jl.ast;

import polyglot.ast.ArrayTypeNode;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.TypeNode;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class ArrayTypeNode_c extends TypeNode_c implements ArrayTypeNode {
   protected TypeNode base;

   public ArrayTypeNode_c(Position pos, TypeNode base) {
      super(pos);
      this.base = base;
   }

   public TypeNode base() {
      return this.base;
   }

   public ArrayTypeNode base(TypeNode base) {
      ArrayTypeNode_c n = (ArrayTypeNode_c)this.copy();
      n.base = base;
      return n;
   }

   protected ArrayTypeNode_c reconstruct(TypeNode base) {
      if (base != this.base) {
         ArrayTypeNode_c n = (ArrayTypeNode_c)this.copy();
         n.base = base;
         return n;
      } else {
         return this;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      TypeNode base = (TypeNode)this.visitChild(this.base, v);
      return this.reconstruct(base);
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      TypeSystem ts = tb.typeSystem();
      return this.type(ts.arrayOf(this.position(), this.base.type()));
   }

   public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
      TypeSystem ts = ar.typeSystem();
      NodeFactory nf = ar.nodeFactory();
      Type baseType = this.base.type();
      if (!baseType.isCanonical()) {
         throw new SemanticException("Base type " + baseType + " of array could not be resolved.", this.base.position());
      } else {
         return nf.CanonicalTypeNode(this.position(), ts.arrayOf(this.position(), baseType));
      }
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      throw new InternalCompilerError(this.position(), "Cannot type check ambiguous node " + this + ".");
   }

   public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
      throw new InternalCompilerError(this.position(), "Cannot exception check ambiguous node " + this + ".");
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      this.print(this.base, w, tr);
      w.write("[]");
   }

   public void translate(CodeWriter w, Translator tr) {
      throw new InternalCompilerError(this.position(), "Cannot translate ambiguous node " + this + ".");
   }

   public String toString() {
      return this.base.toString() + "[]";
   }
}
