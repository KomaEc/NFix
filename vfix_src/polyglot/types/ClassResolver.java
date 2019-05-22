package polyglot.types;

public abstract class ClassResolver implements Resolver {
   public abstract Named find(String var1) throws SemanticException;
}
