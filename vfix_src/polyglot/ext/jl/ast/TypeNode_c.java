package polyglot.ext.jl.ast;

import polyglot.ast.Node;
import polyglot.ast.TypeNode;
import polyglot.types.Qualifier;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;

public abstract class TypeNode_c extends Node_c implements TypeNode {
   protected Type type;

   public TypeNode_c(Position pos) {
      super(pos);
   }

   public Qualifier qualifier() {
      return this.type();
   }

   public Type type() {
      return this.type;
   }

   public TypeNode type(Type type) {
      TypeNode_c n = (TypeNode_c)this.copy();
      n.type = type;
      return n;
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      if (this.type == null) {
         TypeSystem ts = tb.typeSystem();
         return this.type(ts.unknownType(this.position()));
      } else {
         return this;
      }
   }

   public String toString() {
      return this.type != null ? this.type.toString() : "<unknown type>";
   }

   public abstract void prettyPrint(CodeWriter var1, PrettyPrinter var2);
}
