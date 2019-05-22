package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.JL;
import polyglot.ast.Node;
import polyglot.types.Context;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.visit.AddMemberVisitor;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class JL_c extends Ext_c implements JL {
   public JL jl() {
      return this.node();
   }

   public Node visitChildren(NodeVisitor v) {
      return this.jl().visitChildren(v);
   }

   public Context enterScope(Context c) {
      return this.jl().enterScope(c);
   }

   public Context enterScope(Node child, Context c) {
      return this.jl().enterScope(child, c);
   }

   public void addDecls(Context c) {
      this.jl().addDecls(c);
   }

   public NodeVisitor buildTypesEnter(TypeBuilder tb) throws SemanticException {
      return this.jl().buildTypesEnter(tb);
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      return this.jl().buildTypes(tb);
   }

   public NodeVisitor disambiguateEnter(AmbiguityRemover ar) throws SemanticException {
      return this.jl().disambiguateEnter(ar);
   }

   public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
      return this.jl().disambiguate(ar);
   }

   public NodeVisitor addMembersEnter(AddMemberVisitor am) throws SemanticException {
      return this.jl().addMembersEnter(am);
   }

   public Node addMembers(AddMemberVisitor am) throws SemanticException {
      return this.jl().addMembers(am);
   }

   public NodeVisitor typeCheckEnter(TypeChecker tc) throws SemanticException {
      return this.jl().typeCheckEnter(tc);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      return this.jl().typeCheck(tc);
   }

   public NodeVisitor exceptionCheckEnter(ExceptionChecker ec) throws SemanticException {
      return this.jl().exceptionCheckEnter(ec);
   }

   public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
      return this.jl().exceptionCheck(ec);
   }

   public List throwTypes(TypeSystem ts) {
      return this.jl().throwTypes(ts);
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter pp) {
      this.jl().prettyPrint(w, pp);
   }

   public void translate(CodeWriter w, Translator tr) {
      this.jl().translate(w, tr);
   }
}
