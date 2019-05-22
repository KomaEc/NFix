package polyglot.ast;

public interface ArrayTypeNode extends TypeNode {
   TypeNode base();

   ArrayTypeNode base(TypeNode var1);
}
