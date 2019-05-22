package polyglot.types;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import polyglot.main.Report;
import polyglot.util.CollectionUtil;
import polyglot.util.InternalCompilerError;

public class TableResolver extends ClassResolver implements TopLevelResolver {
   protected Map table = new HashMap();
   private static final Collection TOPICS = CollectionUtil.list("types", "resolver");

   public void addNamed(Named type) {
      this.addNamed(type.name(), type);
   }

   public void addNamed(String name, Named type) {
      if (name != null && type != null) {
         if (Report.should_report((Collection)TOPICS, 3)) {
            Report.report(3, "TableCR.addNamed(" + name + ", " + type + ")");
         }

         this.table.put(name, type);
      } else {
         throw new InternalCompilerError("Bad insertion into TableResolver");
      }
   }

   public boolean packageExists(String name) {
      Iterator i = this.table.entrySet().iterator();

      while(i.hasNext()) {
         Entry e = (Entry)i.next();
         Named type = (Named)e.getValue();
         if (type instanceof Importable) {
            Importable im = (Importable)type;
            if (im.package_() != null && im.package_().fullName().startsWith(name)) {
               return true;
            }
         }
      }

      return false;
   }

   public Named find(String name) throws SemanticException {
      if (Report.should_report((Collection)TOPICS, 3)) {
         Report.report(3, "TableCR.find(" + name + ")");
      }

      Named n = (Named)this.table.get(name);
      if (n != null) {
         return n;
      } else {
         throw new NoClassException(name);
      }
   }

   public String toString() {
      return "(table " + this.table + ")";
   }
}
