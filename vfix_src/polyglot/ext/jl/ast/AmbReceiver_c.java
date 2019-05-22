package polyglot.ext.jl.ast;

import polyglot.ast.AmbReceiver;
import polyglot.ast.Node;
import polyglot.ast.Prefix;
import polyglot.ast.Receiver;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.TypeBuilder;

public class AmbReceiver_c extends AmbPrefix_c implements AmbReceiver {
   protected Type type;

   public AmbReceiver_c(Position pos, Prefix prefix, String name) {
      super(pos, prefix, name);
   }

   public Type type() {
      return this.type;
   }

   public AmbReceiver type(Type type) {
      AmbReceiver_c n = (AmbReceiver_c)this.copy();
      n.type = type;
      return n;
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      return this.type(tb.typeSystem().unknownType(this.position()));
   }

   public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
      Node n = super.disambiguate(ar);
      if (n instanceof Receiver) {
         return n;
      } else {
         throw new SemanticException("Could not find type, field, or local variable \"" + (this.prefix == null ? this.name : this.prefix.toString() + "." + this.name) + "\".", this.position());
      }
   }
}
