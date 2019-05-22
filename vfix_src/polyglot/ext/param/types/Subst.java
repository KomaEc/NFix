package polyglot.ext.param.types;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import polyglot.types.ConstructorInstance;
import polyglot.types.FieldInstance;
import polyglot.types.MethodInstance;
import polyglot.types.Type;

public interface Subst extends Serializable {
   Iterator entries();

   ParamTypeSystem typeSystem();

   Map substitutions();

   Type substType(Type var1);

   PClass substPClass(PClass var1);

   FieldInstance substField(FieldInstance var1);

   MethodInstance substMethod(MethodInstance var1);

   ConstructorInstance substConstructor(ConstructorInstance var1);

   List substTypeList(List var1);

   List substMethodList(List var1);

   List substConstructorList(List var1);

   List substFieldList(List var1);
}
