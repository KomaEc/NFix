package polyglot.ast;

import java.util.List;

public interface ClassBody extends Term {
   List members();

   ClassBody members(List var1);

   ClassBody addMember(ClassMember var1);
}
