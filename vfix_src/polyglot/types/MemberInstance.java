package polyglot.types;

public interface MemberInstance extends TypeObject {
   Flags flags();

   ReferenceType container();
}
