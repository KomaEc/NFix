package polyglot.ast;

import java.util.List;
import polyglot.types.Flags;
import polyglot.types.ParsedClassType;

public interface ClassDecl extends Term, TopLevelDecl, ClassMember {
   ParsedClassType type();

   ClassDecl type(ParsedClassType var1);

   Flags flags();

   ClassDecl flags(Flags var1);

   String name();

   ClassDecl name(String var1);

   TypeNode superClass();

   ClassDecl superClass(TypeNode var1);

   List interfaces();

   ClassDecl interfaces(List var1);

   ClassBody body();

   ClassDecl body(ClassBody var1);
}
