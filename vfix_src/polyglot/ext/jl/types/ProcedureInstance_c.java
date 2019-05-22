package polyglot.ext.jl.types;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import polyglot.types.Flags;
import polyglot.types.ProcedureInstance;
import polyglot.types.ReferenceType;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.util.SubtypeSet;
import polyglot.util.TypedList;

public abstract class ProcedureInstance_c extends TypeObject_c implements ProcedureInstance {
   protected ReferenceType container;
   protected Flags flags;
   protected List formalTypes;
   protected List excTypes;
   // $FF: synthetic field
   static Class class$polyglot$types$Type;

   protected ProcedureInstance_c() {
   }

   public ProcedureInstance_c(TypeSystem ts, Position pos, ReferenceType container, Flags flags, List formalTypes, List excTypes) {
      super(ts, pos);
      this.container = container;
      this.flags = flags;
      this.formalTypes = TypedList.copyAndCheck(formalTypes, class$polyglot$types$Type == null ? (class$polyglot$types$Type = class$("polyglot.types.Type")) : class$polyglot$types$Type, true);
      this.excTypes = TypedList.copyAndCheck(excTypes, class$polyglot$types$Type == null ? (class$polyglot$types$Type = class$("polyglot.types.Type")) : class$polyglot$types$Type, true);
   }

   public ReferenceType container() {
      return this.container;
   }

   public Flags flags() {
      return this.flags;
   }

   public List formalTypes() {
      return Collections.unmodifiableList(this.formalTypes);
   }

   public List throwTypes() {
      return Collections.unmodifiableList(this.excTypes);
   }

   public int hashCode() {
      return this.container.hashCode() + this.flags.hashCode();
   }

   public boolean equalsImpl(TypeObject o) {
      if (!(o instanceof ProcedureInstance)) {
         return false;
      } else {
         ProcedureInstance i = (ProcedureInstance)o;
         return this.flags.equals(i.flags()) && this.ts.equals(this.container, i.container()) && this.ts.hasFormals(this, i.formalTypes());
      }
   }

   protected boolean listIsCanonical(List l) {
      Iterator i = l.iterator();

      TypeObject o;
      do {
         if (!i.hasNext()) {
            return true;
         }

         o = (TypeObject)i.next();
      } while(o.isCanonical());

      return false;
   }

   public final boolean moreSpecific(ProcedureInstance p) {
      return this.ts.moreSpecific(this, p);
   }

   public boolean moreSpecificImpl(ProcedureInstance p) {
      ReferenceType t1 = this.container();
      ReferenceType t2 = p.container();
      if (t1.isClass() && t2.isClass()) {
         if (!t1.isSubtype(t2) && !t1.toClass().isEnclosed(t2.toClass())) {
            return false;
         }
      } else if (!t1.isSubtype(t2)) {
         return false;
      }

      return p.callValid(this.formalTypes());
   }

   public final boolean hasFormals(List formalTypes) {
      return this.ts.hasFormals(this, formalTypes);
   }

   public boolean hasFormalsImpl(List formalTypes) {
      List l1 = this.formalTypes();
      Iterator i1 = l1.iterator();
      Iterator i2 = formalTypes.iterator();

      while(i1.hasNext() && i2.hasNext()) {
         Type t1 = (Type)i1.next();
         Type t2 = (Type)i2.next();
         if (!this.ts.equals(t1, t2)) {
            return false;
         }
      }

      return !i1.hasNext() && !i2.hasNext();
   }

   public final boolean throwsSubset(ProcedureInstance p) {
      return this.ts.throwsSubset(this, p);
   }

   public boolean throwsSubsetImpl(ProcedureInstance p) {
      SubtypeSet s1 = new SubtypeSet(this.ts.Throwable());
      SubtypeSet s2 = new SubtypeSet(this.ts.Throwable());
      s1.addAll(this.throwTypes());
      s2.addAll(p.throwTypes());
      Iterator i = s1.iterator();

      Type t;
      do {
         if (!i.hasNext()) {
            return true;
         }

         t = (Type)i.next();
      } while(this.ts.isUncheckedException(t) || s2.contains(t));

      return false;
   }

   public final boolean callValid(List argTypes) {
      return this.ts.callValid(this, argTypes);
   }

   public boolean callValidImpl(List argTypes) {
      List l1 = this.formalTypes();
      Iterator i1 = l1.iterator();
      Iterator i2 = argTypes.iterator();

      while(i1.hasNext() && i2.hasNext()) {
         Type t1 = (Type)i1.next();
         Type t2 = (Type)i2.next();
         if (!this.ts.isImplicitCastValid(t2, t1)) {
            return false;
         }
      }

      return !i1.hasNext() && !i2.hasNext();
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
