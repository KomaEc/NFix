package polyglot.ext.jl.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import polyglot.main.Options;
import polyglot.types.ArrayType;
import polyglot.types.FieldInstance;
import polyglot.types.MethodInstance;
import polyglot.types.Resolver;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.Position;

public class ArrayType_c extends ReferenceType_c implements ArrayType {
   protected Type base;
   protected List fields;
   protected List methods;
   protected List interfaces;

   protected ArrayType_c() {
   }

   public ArrayType_c(TypeSystem ts, Position pos, Type base) {
      super(ts, pos);
      this.base = base;
      this.methods = null;
      this.fields = null;
      this.interfaces = null;
   }

   void init() {
      if (this.methods == null) {
         this.methods = new ArrayList(1);
         this.methods.add(this.ts.methodInstance(this.position(), this, this.ts.Public(), this.ts.Object(), "clone", Collections.EMPTY_LIST, Collections.EMPTY_LIST));
      }

      if (this.fields == null) {
         this.fields = new ArrayList(2);
         this.fields.add(this.ts.fieldInstance(this.position(), this, this.ts.Public().Final(), this.ts.Int(), "length"));
      }

      if (this.interfaces == null) {
         this.interfaces = new ArrayList(2);
         this.interfaces.add(this.ts.Cloneable());
         this.interfaces.add(this.ts.Serializable());
      }

   }

   public Type base() {
      return this.base;
   }

   public ArrayType base(Type base) {
      if (base == this.base) {
         return this;
      } else {
         ArrayType_c n = (ArrayType_c)this.copy();
         n.base = base;
         return n;
      }
   }

   public Type ultimateBase() {
      return this.base().isArray() ? this.base().toArray().ultimateBase() : this.base();
   }

   public int dims() {
      return 1 + (this.base().isArray() ? this.base().toArray().dims() : 0);
   }

   public String toString() {
      return this.base().toString() + "[]";
   }

   public String translate(Resolver c) {
      if (Options.global.cppBackend()) {
         String s = "jmatch_array< " + this.base().translate(c);
         if (this.base().isReference()) {
            s = s + "*";
         }

         return s + " > ";
      } else {
         return this.base().translate(c) + "[]";
      }
   }

   public boolean isCanonical() {
      return this.base().isCanonical();
   }

   public boolean isArray() {
      return true;
   }

   public ArrayType toArray() {
      return this;
   }

   public List methods() {
      this.init();
      return Collections.unmodifiableList(this.methods);
   }

   public List fields() {
      this.init();
      return Collections.unmodifiableList(this.fields);
   }

   public MethodInstance cloneMethod() {
      return (MethodInstance)this.methods().get(0);
   }

   public FieldInstance fieldNamed(String name) {
      FieldInstance fi = this.lengthField();
      return name.equals(fi.name()) ? fi : null;
   }

   public FieldInstance lengthField() {
      return (FieldInstance)this.fields().get(0);
   }

   public Type superType() {
      return this.ts.Object();
   }

   public List interfaces() {
      this.init();
      return Collections.unmodifiableList(this.interfaces);
   }

   public int hashCode() {
      return this.base().hashCode() << 1;
   }

   public boolean equalsImpl(TypeObject t) {
      if (t instanceof ArrayType) {
         ArrayType a = (ArrayType)t;
         return this.ts.equals(this.base(), a.base());
      } else {
         return false;
      }
   }

   public boolean isImplicitCastValidImpl(Type toType) {
      if (toType.isArray()) {
         return !this.base().isPrimitive() && !toType.toArray().base().isPrimitive() ? this.ts.isImplicitCastValid(this.base(), toType.toArray().base()) : this.ts.equals(this.base(), toType.toArray().base());
      } else {
         return this.ts.isSubtype(this, toType);
      }
   }

   public boolean isCastValidImpl(Type toType) {
      if (!toType.isReference()) {
         return false;
      } else if (toType.isArray()) {
         Type fromBase = this.base();
         Type toBase = toType.toArray().base();
         if (fromBase.isPrimitive()) {
            return this.ts.equals(toBase, fromBase);
         } else if (toBase.isPrimitive()) {
            return false;
         } else if (fromBase.isNull()) {
            return false;
         } else {
            return toBase.isNull() ? false : this.ts.isCastValid(fromBase, toBase);
         }
      } else {
         return this.ts.isSubtype(this, toType);
      }
   }
}
