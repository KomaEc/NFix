package polyglot.ext.jl.ast;

import polyglot.ast.AmbTypeNode;
import polyglot.ast.Node;
import polyglot.ast.QualifierNode;
import polyglot.ast.TypeNode;
import polyglot.types.SemanticException;
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

public class AmbTypeNode_c extends TypeNode_c implements AmbTypeNode {
   protected QualifierNode qual;
   protected String name;

   public AmbTypeNode_c(Position pos, QualifierNode qual, String name) {
      super(pos);
      this.qual = qual;
      this.name = name;
   }

   public String name() {
      return this.name;
   }

   public AmbTypeNode name(String name) {
      AmbTypeNode_c n = (AmbTypeNode_c)this.copy();
      n.name = name;
      return n;
   }

   public QualifierNode qual() {
      return this.qual;
   }

   public AmbTypeNode qual(QualifierNode qual) {
      AmbTypeNode_c n = (AmbTypeNode_c)this.copy();
      n.qual = qual;
      return n;
   }

   protected AmbTypeNode_c reconstruct(QualifierNode qual) {
      if (qual != this.qual) {
         AmbTypeNode_c n = (AmbTypeNode_c)this.copy();
         n.qual = qual;
         return n;
      } else {
         return this;
      }
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      return this.type(tb.typeSystem().unknownType(this.position()));
   }

   public Node visitChildren(NodeVisitor v) {
      QualifierNode qual = (QualifierNode)this.visitChild(this.qual, v);
      return this.reconstruct(qual);
   }

   public Node disambiguate(AmbiguityRemover sc) throws SemanticException {
      Node n = sc.nodeFactory().disamb().disambiguate(this, sc, this.position(), this.qual, this.name);
      if (n instanceof TypeNode) {
         return n;
      } else {
         throw new SemanticException("Could not find type \"" + (this.qual == null ? this.name : this.qual.toString() + "." + this.name) + "\".", this.position());
      }
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      throw new InternalCompilerError(this.position(), "Cannot type check ambiguous node " + this + ".");
   }

   public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
      throw new InternalCompilerError(this.position(), "Cannot exception check ambiguous node " + this + ".");
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      if (this.qual != null) {
         this.print(this.qual, w, tr);
         w.write(".");
      }

      w.write(this.name);
   }

   public void translate(CodeWriter w, Translator tr) {
      throw new InternalCompilerError(this.position(), "Cannot translate ambiguous node " + this + ".");
   }

   public String toString() {
      return (this.qual == null ? this.name : this.qual.toString() + "." + this.name) + "{amb}";
   }

   public void dump(CodeWriter w) {
      super.dump(w);
      w.allowBreak(4, " ");
      w.begin(0);
      w.write("(name \"" + this.name + "\")");
      w.end();
   }
}
