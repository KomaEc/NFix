package polyglot.ext.jl.ast;

import polyglot.ast.AmbQualifierNode;
import polyglot.ast.Node;
import polyglot.ast.QualifierNode;
import polyglot.types.Qualifier;
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

public class AmbQualifierNode_c extends Node_c implements AmbQualifierNode {
   protected Qualifier qualifier;
   protected QualifierNode qual;
   protected String name;

   public AmbQualifierNode_c(Position pos, QualifierNode qual, String name) {
      super(pos);
      this.qual = qual;
      this.name = name;
   }

   public Qualifier qualifier() {
      return this.qualifier;
   }

   public String name() {
      return this.name;
   }

   public AmbQualifierNode name(String name) {
      AmbQualifierNode_c n = (AmbQualifierNode_c)this.copy();
      n.name = name;
      return n;
   }

   public QualifierNode qual() {
      return this.qual;
   }

   public AmbQualifierNode qual(QualifierNode qual) {
      AmbQualifierNode_c n = (AmbQualifierNode_c)this.copy();
      n.qual = qual;
      return n;
   }

   public AmbQualifierNode qualifier(Qualifier qualifier) {
      AmbQualifierNode_c n = (AmbQualifierNode_c)this.copy();
      n.qualifier = qualifier;
      return n;
   }

   protected AmbQualifierNode_c reconstruct(QualifierNode qual) {
      if (qual != this.qual) {
         AmbQualifierNode_c n = (AmbQualifierNode_c)this.copy();
         n.qual = qual;
         return n;
      } else {
         return this;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      QualifierNode qual = (QualifierNode)this.visitChild(this.qual, v);
      return this.reconstruct(qual);
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      return this.qualifier(tb.typeSystem().unknownQualifier(this.position()));
   }

   public Node disambiguate(AmbiguityRemover sc) throws SemanticException {
      Node n = sc.nodeFactory().disamb().disambiguate(this, sc, this.position(), this.qual, this.name);
      if (n instanceof QualifierNode) {
         return n;
      } else {
         throw new SemanticException("Could not find type or package \"" + (this.qual == null ? this.name : this.qual.toString() + "." + this.name) + "\".", this.position());
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
