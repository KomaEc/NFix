package polyglot.ast;

import polyglot.types.LocalInstance;

public interface Local extends Variable {
   String name();

   Local name(String var1);

   LocalInstance localInstance();

   Local localInstance(LocalInstance var1);
}
