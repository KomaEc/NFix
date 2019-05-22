package polyglot.ext.jl.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import polyglot.main.Report;
import polyglot.types.Flags;
import polyglot.types.MethodInstance;
import polyglot.types.ReferenceType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;

public class MethodInstance_c extends ProcedureInstance_c implements MethodInstance {
   protected String name;
   protected Type returnType;

   protected MethodInstance_c() {
   }

   public MethodInstance_c(TypeSystem ts, Position pos, ReferenceType container, Flags flags, Type returnType, String name, List formalTypes, List excTypes) {
      super(ts, pos, container, flags, formalTypes, excTypes);
      this.returnType = returnType;
      this.name = name;
   }

   public MethodInstance flags(Flags flags) {
      if (!flags.equals(this.flags)) {
         MethodInstance_c n = (MethodInstance_c)this.copy();
         n.flags = flags;
         return n;
      } else {
         return this;
      }
   }

   public String name() {
      return this.name;
   }

   public MethodInstance name(String name) {
      if ((name == null || name.equals(this.name)) && (name != null || name == this.name)) {
         return this;
      } else {
         MethodInstance_c n = (MethodInstance_c)this.copy();
         n.name = name;
         return n;
      }
   }

   public Type returnType() {
      return this.returnType;
   }

   public MethodInstance returnType(Type returnType) {
      if (this.returnType != returnType) {
         MethodInstance_c n = (MethodInstance_c)this.copy();
         n.returnType = returnType;
         return n;
      } else {
         return this;
      }
   }

   public MethodInstance formalTypes(List l) {
      if (!CollectionUtil.equals(this.formalTypes, l)) {
         MethodInstance_c n = (MethodInstance_c)this.copy();
         n.formalTypes = new ArrayList(l);
         return n;
      } else {
         return this;
      }
   }

   public MethodInstance throwTypes(List l) {
      if (!CollectionUtil.equals(this.excTypes, l)) {
         MethodInstance_c n = (MethodInstance_c)this.copy();
         n.excTypes = new ArrayList(l);
         return n;
      } else {
         return this;
      }
   }

   public MethodInstance container(ReferenceType container) {
      if (this.container != container) {
         MethodInstance_c n = (MethodInstance_c)this.copy();
         n.container = container;
         return n;
      } else {
         return this;
      }
   }

   public int hashCode() {
      return this.flags.hashCode() + this.name.hashCode();
   }

   public boolean equalsImpl(TypeObject o) {
      if (!(o instanceof MethodInstance)) {
         return false;
      } else {
         MethodInstance i = (MethodInstance)o;
         return this.ts.equals(this.returnType, i.returnType()) && this.name.equals(i.name()) && super.equalsImpl(i);
      }
   }

   public String toString() {
      String s = this.designator() + " " + this.flags.translate() + this.returnType + " " + this.signature();
      if (!this.excTypes.isEmpty()) {
         s = s + " throws " + TypeSystem_c.listToString(this.excTypes);
      }

      return s;
   }

   public String signature() {
      return this.name + "(" + TypeSystem_c.listToString(this.formalTypes) + ")";
   }

   public String designator() {
      return "method";
   }

   public final boolean isSameMethod(MethodInstance m) {
      return this.ts.isSameMethod(this, m);
   }

   public boolean isSameMethodImpl(MethodInstance m) {
      return this.name().equals(m.name()) && this.hasFormals(m.formalTypes());
   }

   public boolean isCanonical() {
      return this.container.isCanonical() && this.returnType.isCanonical() && this.listIsCanonical(this.formalTypes) && this.listIsCanonical(this.excTypes);
   }

   public final boolean methodCallValid(String name, List argTypes) {
      return this.ts.methodCallValid(this, name, argTypes);
   }

   public boolean methodCallValidImpl(String name, List argTypes) {
      return this.name().equals(name) && this.ts.callValid(this, argTypes);
   }

   public List overrides() {
      return this.ts.overrides(this);
   }

   public List overridesImpl() {
      List l = new LinkedList();

      ReferenceType sup;
      for(ReferenceType rt = this.container(); rt != null; rt = sup) {
         l.addAll(rt.methods(this.name, this.formalTypes));
         sup = null;
         if (rt.superType() != null && rt.superType().isReference()) {
            sup = (ReferenceType)rt.superType();
         }
      }

      return l;
   }

   public final boolean canOverride(MethodInstance mj) {
      return this.ts.canOverride(this, mj);
   }

   public final void checkOverride(MethodInstance mj) throws SemanticException {
      this.ts.checkOverride(this, mj);
   }

   public final boolean canOverrideImpl(MethodInstance mj) throws SemanticException {
      throw new RuntimeException("canOverrideImpl(MethodInstance mj) should not be called.");
   }

   public boolean canOverrideImpl(MethodInstance mj, boolean quiet) throws SemanticException {
      if (this.name().equals(mj.name()) && this.hasFormals(mj.formalTypes())) {
         if (!this.ts.equals(this.returnType(), mj.returnType())) {
            if (Report.should_report((String)"types", 3)) {
               Report.report(3, "return type " + this.returnType() + " != " + mj.returnType());
            }

            if (quiet) {
               return false;
            } else {
               throw new SemanticException(this.signature() + " in " + this.container() + " cannot override " + mj.signature() + " in " + mj.container() + "; attempting to use incompatible " + "return type\n" + "found: " + this.returnType() + "\n" + "required: " + mj.returnType(), this.position());
            }
         } else if (!this.ts.throwsSubset(this, mj)) {
            if (Report.should_report((String)"types", 3)) {
               Report.report(3, this.throwTypes() + " not subset of " + mj.throwTypes());
            }

            if (quiet) {
               return false;
            } else {
               throw new SemanticException(this.signature() + " in " + this.container() + " cannot override " + mj.signature() + " in " + mj.container() + "; the throw set is not a subset of the " + "overridden method's throw set", this.position());
            }
         } else if (this.flags().moreRestrictiveThan(mj.flags())) {
            if (Report.should_report((String)"types", 3)) {
               Report.report(3, this.flags() + " more restrictive than " + mj.flags());
            }

            if (quiet) {
               return false;
            } else {
               throw new SemanticException(this.signature() + " in " + this.container() + " cannot override " + mj.signature() + " in " + mj.container() + "; attempting to assign weaker " + "access privileges", this.position());
            }
         } else if (this.flags().isStatic() != mj.flags().isStatic()) {
            if (Report.should_report((String)"types", 3)) {
               Report.report(3, this.signature() + " is " + (this.flags().isStatic() ? "" : "not") + " static but " + mj.signature() + " is " + (mj.flags().isStatic() ? "" : "not") + " static");
            }

            if (quiet) {
               return false;
            } else {
               throw new SemanticException(this.signature() + " in " + this.container() + " cannot override " + mj.signature() + " in " + mj.container() + "; overridden method is " + (mj.flags().isStatic() ? "" : "not") + "static", this.position());
            }
         } else if (this != mj && !this.equals(mj) && mj.flags().isFinal()) {
            if (Report.should_report((String)"types", 3)) {
               Report.report(3, mj.flags() + " final");
            }

            if (quiet) {
               return false;
            } else {
               throw new SemanticException(this.signature() + " in " + this.container() + " cannot override " + mj.signature() + " in " + mj.container() + "; overridden method is final", this.position());
            }
         } else {
            return true;
         }
      } else if (quiet) {
         return false;
      } else {
         throw new SemanticException("Arguments are different", this.position());
      }
   }

   public List implemented() {
      return this.ts.implemented(this);
   }

   public List implementedImpl(ReferenceType rt) {
      if (rt == null) {
         return Collections.EMPTY_LIST;
      } else {
         List l = new LinkedList();
         l.addAll(rt.methods(this.name, this.formalTypes));
         Type superType = rt.superType();
         if (superType != null) {
            l.addAll(this.implementedImpl(superType.toReference()));
         }

         List ints = rt.interfaces();
         Iterator i = ints.iterator();

         while(i.hasNext()) {
            ReferenceType rt2 = (ReferenceType)i.next();
            l.addAll(this.implementedImpl(rt2));
         }

         return l;
      }
   }
}
