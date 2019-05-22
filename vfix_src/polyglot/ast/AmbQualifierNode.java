package polyglot.ast;

public interface AmbQualifierNode extends Ambiguous, QualifierNode {
   QualifierNode qual();

   String name();
}
