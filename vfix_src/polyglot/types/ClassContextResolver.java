package polyglot.types;

import java.util.Collection;
import polyglot.main.Report;
import polyglot.util.CollectionUtil;
import polyglot.util.InternalCompilerError;
import polyglot.util.StringUtil;

public class ClassContextResolver extends ClassResolver {
   TypeSystem ts;
   ClassType type;
   private static final Collection TOPICS = CollectionUtil.list("types", "resolver");

   public ClassContextResolver(TypeSystem ts, ClassType type) {
      this.ts = ts;
      this.type = type;
   }

   public String toString() {
      return "(class-context " + this.type + ")";
   }

   public Named find(String name) throws SemanticException {
      if (Report.should_report((Collection)TOPICS, 2)) {
         Report.report(2, "Looking for " + name + " in " + this);
      }

      if (!StringUtil.isNameShort(name)) {
         throw new InternalCompilerError("Cannot lookup qualified name " + name);
      } else {
         ClassType inner = this.ts.findMemberClass(this.type, name);
         if (inner != null) {
            if (Report.should_report((Collection)TOPICS, 2)) {
               Report.report(2, "Found member class " + inner);
            }

            return inner;
         } else {
            throw new NoClassException(name, this.type);
         }
      }
   }

   public ClassType classType() {
      return this.type;
   }
}
