package polyglot.ast;

public interface AmbReceiver extends Ambiguous, Receiver {
   Prefix prefix();

   String name();
}
