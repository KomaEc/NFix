package polyglot.ext.param.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import polyglot.main.Report;
import polyglot.types.ArrayType;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.FieldInstance;
import polyglot.types.MethodInstance;
import polyglot.types.ReferenceType;
import polyglot.types.Type;
import polyglot.util.CachingTransformingList;
import polyglot.util.InternalCompilerError;
import polyglot.util.Transformation;
import polyglot.util.TypeInputStream;

public class Subst_c implements Subst {
   protected Map subst;
   protected transient Map cache;
   protected transient ParamTypeSystem ts;

   public Subst_c(ParamTypeSystem ts, Map subst, Map cache) {
      this.ts = ts;
      this.subst = subst;
      this.cache = new HashMap();
      this.cache.putAll(cache);
   }

   public ParamTypeSystem typeSystem() {
      return this.ts;
   }

   public Iterator entries() {
      return this.substitutions().entrySet().iterator();
   }

   public Map substitutions() {
      return Collections.unmodifiableMap(this.subst);
   }

   protected Type uncachedSubstType(Type t) {
      if (t.isArray()) {
         ArrayType at = t.toArray();
         return at.base(this.substType(at.base()));
      } else if (!(t instanceof SubstType)) {
         return (Type)(t instanceof ClassType ? this.substClassType((ClassType)t) : t);
      } else {
         Type tbase = ((SubstType)t).base();
         Map tsubst = ((SubstType)t).subst().substitutions();
         Map newSubst = new HashMap();
         Iterator i = tsubst.entrySet().iterator();

         while(i.hasNext()) {
            Entry e = (Entry)i.next();
            Object formal = e.getKey();
            Object actual = e.getValue();
            newSubst.put(formal, this.substSubstValue(actual));
         }

         newSubst.putAll(this.subst);
         return this.ts.subst(tbase, newSubst, this.cache);
      }
   }

   protected Object substSubstValue(Object value) {
      return value;
   }

   public ClassType substClassType(ClassType t) {
      return new SubstClassType_c(this.ts, t.position(), t, this);
   }

   public Type substType(Type t) {
      if (t != null && t != this) {
         Type cached = (Type)this.cache.get(t);
         if (cached == null) {
            cached = this.uncachedSubstType(t);
            this.cache.put(t, cached);
            if (Report.should_report((String)"subst", 2)) {
               Report.report(2, "substType(" + t + ": " + t.getClass().getName() + ") = " + cached + ": " + cached.getClass().getName());
            }
         }

         return cached;
      } else {
         return t;
      }
   }

   public PClass substPClass(PClass pclazz) {
      MuPClass newPclazz = this.ts.mutablePClass(pclazz.position());
      newPclazz.formals(pclazz.formals());
      newPclazz.clazz((ClassType)this.substType(pclazz.clazz()));
      return newPclazz;
   }

   public FieldInstance substField(FieldInstance fi) {
      ReferenceType ct = (ReferenceType)this.substType(fi.container());
      Type t = this.substType(fi.type());
      return fi.type(t).container(ct);
   }

   public MethodInstance substMethod(MethodInstance mi) {
      ReferenceType ct = (ReferenceType)this.substType(mi.container());
      Type rt = this.substType(mi.returnType());
      List formalTypes = mi.formalTypes();
      formalTypes = this.substTypeList(formalTypes);
      List throwTypes = mi.throwTypes();
      throwTypes = this.substTypeList(throwTypes);
      return mi.returnType(rt).formalTypes(formalTypes).throwTypes(throwTypes).container(ct);
   }

   public ConstructorInstance substConstructor(ConstructorInstance ci) {
      ClassType ct = (ClassType)this.substType(ci.container());
      List formalTypes = ci.formalTypes();
      formalTypes = this.substTypeList(formalTypes);
      List throwTypes = ci.throwTypes();
      throwTypes = this.substTypeList(throwTypes);
      return ci.formalTypes(formalTypes).throwTypes(throwTypes).container(ct);
   }

   public List substTypeList(List list) {
      return new CachingTransformingList(list, new Subst_c.TypeXform());
   }

   public List substMethodList(List list) {
      return new CachingTransformingList(list, new Subst_c.MethodXform());
   }

   public List substConstructorList(List list) {
      return new CachingTransformingList(list, new Subst_c.ConstructorXform());
   }

   public List substFieldList(List list) {
      return new CachingTransformingList(list, new Subst_c.FieldXform());
   }

   public boolean equals(Object o) {
      return o instanceof Subst ? this.subst.equals(((Subst)o).substitutions()) : false;
   }

   public int hashCode() {
      return this.subst.hashCode();
   }

   public String toString() {
      String str = "[";
      Iterator iter = this.subst.keySet().iterator();

      while(iter.hasNext()) {
         Object key = iter.next();
         str = str + "<" + key + ": " + this.subst.get(key) + ">";
         if (iter.hasNext()) {
            str = str + ", ";
         }
      }

      return str + "]";
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      if (in instanceof TypeInputStream) {
         this.ts = (ParamTypeSystem)((TypeInputStream)in).getTypeSystem();
      }

      this.cache = new HashMap();
      in.defaultReadObject();
   }

   public class ConstructorXform implements Transformation {
      public Object transform(Object o) {
         if (!(o instanceof ConstructorInstance)) {
            throw new InternalCompilerError(o + " is not a constructor.");
         } else {
            return Subst_c.this.substConstructor((ConstructorInstance)o);
         }
      }
   }

   public class MethodXform implements Transformation {
      public Object transform(Object o) {
         if (!(o instanceof MethodInstance)) {
            throw new InternalCompilerError(o + " is not a method.");
         } else {
            return Subst_c.this.substMethod((MethodInstance)o);
         }
      }
   }

   public class FieldXform implements Transformation {
      public Object transform(Object o) {
         if (!(o instanceof FieldInstance)) {
            throw new InternalCompilerError(o + " is not a field.");
         } else {
            return Subst_c.this.substField((FieldInstance)o);
         }
      }
   }

   public class TypeXform implements Transformation {
      public Object transform(Object o) {
         if (!(o instanceof Type)) {
            throw new InternalCompilerError(o + " is not a type.");
         } else {
            return Subst_c.this.substType((Type)o);
         }
      }
   }
}
