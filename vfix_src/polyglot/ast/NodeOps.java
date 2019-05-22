package polyglot.ast;

import java.util.List;
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

public interface NodeOps {
   Node visitChildren(NodeVisitor var1);

   Context enterScope(Context var1);

   Context enterScope(Node var1, Context var2);

   void addDecls(Context var1);

   NodeVisitor buildTypesEnter(TypeBuilder var1) throws SemanticException;

   Node buildTypes(TypeBuilder var1) throws SemanticException;

   NodeVisitor disambiguateEnter(AmbiguityRemover var1) throws SemanticException;

   Node disambiguate(AmbiguityRemover var1) throws SemanticException;

   NodeVisitor addMembersEnter(AddMemberVisitor var1) throws SemanticException;

   Node addMembers(AddMemberVisitor var1) throws SemanticException;

   NodeVisitor typeCheckEnter(TypeChecker var1) throws SemanticException;

   Node typeCheck(TypeChecker var1) throws SemanticException;

   NodeVisitor exceptionCheckEnter(ExceptionChecker var1) throws SemanticException;

   Node exceptionCheck(ExceptionChecker var1) throws SemanticException;

   List throwTypes(TypeSystem var1);

   void prettyPrint(CodeWriter var1, PrettyPrinter var2);

   void translate(CodeWriter var1, Translator var2);
}
