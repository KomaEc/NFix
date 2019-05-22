package polyglot.ast;

import polyglot.types.Type;

public interface TypeNode extends Receiver, QualifierNode {
   TypeNode type(Type var1);
}
