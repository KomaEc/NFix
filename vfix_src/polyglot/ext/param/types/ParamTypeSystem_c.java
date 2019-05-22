package polyglot.ext.param.types;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import polyglot.ext.jl.types.TypeSystem_c;
import polyglot.types.ClassType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;

public abstract class ParamTypeSystem_c extends TypeSystem_c implements ParamTypeSystem {
   public MuPClass mutablePClass(Position pos) {
      return new MuPClass_c(this, pos);
   }

   public ClassType instantiate(Position pos, PClass base, List actuals) throws SemanticException {
      this.checkInstantiation(pos, base, actuals);
      return this.uncheckedInstantiate(pos, base, actuals);
   }

   protected void checkInstantiation(Position pos, PClass base, List actuals) throws SemanticException {
      if (base.formals().size() != actuals.size()) {
         throw new SemanticException("Wrong number of actual parameters for instantiation of \"" + base + "\".", pos);
      }
   }

   protected ClassType uncheckedInstantiate(Position pos, PClass base, List actuals) {
      Map substMap = new HashMap();
      Iterator i = base.formals().iterator();
      Iterator j = actuals.iterator();

      while(i.hasNext() && j.hasNext()) {
         Object formal = i.next();
         Object actual = j.next();
         substMap.put(formal, actual);
      }

      if (!i.hasNext() && !j.hasNext()) {
         Type inst = this.subst(base.clazz(), substMap, new HashMap());
         if (!inst.isClass()) {
            throw new InternalCompilerError("Instantiating a PClass produced something other than a ClassType.", pos);
         } else {
            return inst.toClass();
         }
      } else {
         throw new InternalCompilerError("Wrong number of actual parameters for instantiation of \"" + base + "\".", pos);
      }
   }

   public Type subst(Type t, Map substMap) {
      return this.subst(t, substMap, new HashMap());
   }

   public Type subst(Type t, Map substMap, Map cache) {
      return this.subst(substMap, cache).substType(t);
   }

   public Subst subst(Map substMap, Map cache) {
      return new Subst_c(this, substMap, cache);
   }
}
