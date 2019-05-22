package polyglot.ext.jl.ast;

import polyglot.ast.AmbPrefix;
import polyglot.ast.Node;
import polyglot.ast.Prefix;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;
import polyglot.visit.TypeChecker;

public class AmbPrefix_c extends Node_c implements AmbPrefix {
   protected Prefix prefix;
   protected String name;

   public AmbPrefix_c(Position pos, Prefix prefix, String name) {
      super(pos);
      this.prefix = prefix;
      this.name = name;
   }

   public String name() {
      return this.name;
   }

   public AmbPrefix name(String name) {
      AmbPrefix_c n = (AmbPrefix_c)this.copy();
      n.name = name;
      return n;
   }

   public Prefix prefix() {
      return this.prefix;
   }

   public AmbPrefix prefix(Prefix prefix) {
      AmbPrefix_c n = (AmbPrefix_c)this.copy();
      n.prefix = prefix;
      return n;
   }

   protected AmbPrefix_c reconstruct(Prefix prefix) {
      if (prefix != this.prefix) {
         AmbPrefix_c n = (AmbPrefix_c)this.copy();
         n.prefix = prefix;
         return n;
      } else {
         return this;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Prefix prefix = (Prefix)this.visitChild(this.prefix, v);
      return this.reconstruct(prefix);
   }

   public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
      return ar.nodeFactory().disamb().disambiguate(this, ar, this.position(), this.prefix, this.name);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      throw new InternalCompilerError(this.position(), "Cannot type check ambiguous node " + this + ".");
   }

   public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
      throw new InternalCompilerError(this.position(), "Cannot exception check ambiguous node " + this + ".");
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      if (this.prefix != null) {
         this.print(this.prefix, w, tr);
         w.write(".");
      }

      w.write(this.name);
   }

   public void translate(CodeWriter w, Translator tr) {
      throw new InternalCompilerError(this.position(), "Cannot translate ambiguous node " + this + ".");
   }

   public String toString() {
      return (this.prefix == null ? this.name : this.prefix.toString() + "." + this.name) + "{amb}";
   }
}
