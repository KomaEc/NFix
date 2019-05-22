package polyglot.types;

import java.util.List;
import polyglot.util.Enum;

public interface ClassType extends Importable, ReferenceType, MemberInstance {
   ClassType.Kind TOP_LEVEL = new ClassType.Kind("top-level");
   ClassType.Kind MEMBER = new ClassType.Kind("member");
   ClassType.Kind LOCAL = new ClassType.Kind("local");
   ClassType.Kind ANONYMOUS = new ClassType.Kind("anonymous");

   ClassType.Kind kind();

   boolean isTopLevel();

   /** @deprecated */
   boolean isInner();

   boolean isNested();

   boolean isInnerClass();

   boolean isMember();

   boolean isLocal();

   boolean isAnonymous();

   boolean inStaticContext();

   List constructors();

   List memberClasses();

   ClassType memberClassNamed(String var1);

   FieldInstance fieldNamed(String var1);

   boolean isEnclosed(ClassType var1);

   boolean isEnclosedImpl(ClassType var1);

   boolean hasEnclosingInstance(ClassType var1);

   boolean hasEnclosingInstanceImpl(ClassType var1);

   ClassType outer();

   public static class Kind extends Enum {
      public Kind(String name) {
         super(name);
      }
   }
}
