package polyglot.ast;

import polyglot.types.FieldInstance;

public interface Field extends Variable {
   FieldInstance fieldInstance();

   Field fieldInstance(FieldInstance var1);

   Receiver target();

   Field target(Receiver var1);

   boolean isTargetImplicit();

   Field targetImplicit(boolean var1);

   String name();

   Field name(String var1);
}
