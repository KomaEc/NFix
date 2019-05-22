package polyglot.types;

public interface Package extends Qualifier, Named {
   Package prefix();

   String translate(Resolver var1);
}
