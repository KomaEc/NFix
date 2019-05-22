package polyglot.ast;

public interface AmbTypeNode extends TypeNode, Ambiguous {
   QualifierNode qual();

   AmbTypeNode qual(QualifierNode var1);

   String name();

   AmbTypeNode name(String var1);
}
