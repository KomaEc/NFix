package polyglot.ast;

public interface AmbPrefix extends Prefix, Ambiguous {
   Prefix prefix();

   String name();
}
