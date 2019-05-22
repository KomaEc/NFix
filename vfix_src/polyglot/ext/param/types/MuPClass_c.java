package polyglot.ext.param.types;

import java.util.LinkedList;
import java.util.List;
import polyglot.types.ClassType;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.util.TypedList;

public class MuPClass_c extends PClass_c implements MuPClass {
   protected List formals;
   protected ClassType clazz;
   // $FF: synthetic field
   static Class class$polyglot$ext$param$types$Param;

   protected MuPClass_c() {
   }

   public MuPClass_c(TypeSystem ts) {
      this(ts, (Position)null);
   }

   public MuPClass_c(TypeSystem ts, Position pos) {
      super(ts, pos);
      this.formals = new TypedList(new LinkedList(), class$polyglot$ext$param$types$Param == null ? (class$polyglot$ext$param$types$Param = class$("polyglot.ext.param.types.Param")) : class$polyglot$ext$param$types$Param, false);
   }

   public List formals() {
      return this.formals;
   }

   public ClassType clazz() {
      return this.clazz;
   }

   public void formals(List formals) {
      this.formals = formals;
   }

   public void addFormal(Param param) {
      this.formals().add(param);
   }

   public void clazz(ClassType clazz) {
      this.clazz = clazz;
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
