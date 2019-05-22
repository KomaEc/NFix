package polyglot.ext.jl.ast;

import polyglot.ast.CanonicalTypeNode;
import polyglot.ast.Node;
import polyglot.main.Options;
import polyglot.types.ClassType;
import polyglot.types.Resolver;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;
import polyglot.visit.TypeChecker;

public class CanonicalTypeNode_c extends TypeNode_c implements CanonicalTypeNode {
   public CanonicalTypeNode_c(Position pos, Type type) {
      super(pos);
      this.type = type;
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      if (this.type == null) {
         w.write("<unknown-type>");
      } else {
         w.write(Translator.cScope(this.type.toString()));
      }

   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      if (this.type.isClass()) {
         ClassType ct = this.type.toClass();
         if ((ct.isTopLevel() || ct.isMember()) && !ts.classAccessible(ct, tc.context())) {
            throw new SemanticException("Cannot access class \"" + ct + "\" from the body of \"" + tc.context().currentClass() + "\".", this.position());
         }
      }

      return this;
   }

   public void translate(CodeWriter w, Translator tr) {
      TypeSystem ts = tr.typeSystem();
      if (tr.outerClass() != null) {
         w.write(this.type.translate(ts.classContextResolver(tr.outerClass())));
      } else if (Options.global.fully_qualified_names) {
         w.write(this.type.translate((Resolver)null));
      } else {
         w.write(this.type.translate(tr.context()));
      }

   }

   public String toString() {
      return this.type == null ? "<unknown-type>" : this.type.toString();
   }

   public void dump(CodeWriter w) {
      super.dump(w);
      w.allowBreak(4, " ");
      w.begin(0);
      w.write("(type " + this.type + ")");
      w.end();
   }
}
