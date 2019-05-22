package polyglot.types;

public interface Resolver {
   Named find(String var1) throws SemanticException;
}
