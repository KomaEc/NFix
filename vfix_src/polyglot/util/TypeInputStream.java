package polyglot.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import polyglot.main.Report;
import polyglot.types.PlaceHolder;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;

public class TypeInputStream extends ObjectInputStream {
   protected TypeSystem ts;
   protected Map cache;

   public TypeInputStream(InputStream in, TypeSystem ts) throws IOException {
      super(in);
      this.enableResolveObject(true);
      this.ts = ts;
      this.cache = new HashMap();
   }

   public TypeSystem getTypeSystem() {
      return this.ts;
   }

   protected Object resolveObject(Object o) {
      String s = "";
      if (Report.should_report((String)"serialize", 2)) {
         try {
            s = o.toString();
         } catch (NullPointerException var5) {
            s = "<NullPointerException thrown>";
         }
      }

      if (o instanceof PlaceHolder) {
         Object k = new IdentityKey(o);
         TypeObject t = (TypeObject)this.cache.get(k);
         if (t == null) {
            t = ((PlaceHolder)o).resolve(this.ts);
            this.cache.put(k, t);
         }

         if (Report.should_report((String)"serialize", 2)) {
            Report.report(2, "- Resolving " + s + " : " + o.getClass() + " to " + t + " : " + t.getClass());
         }

         return t;
      } else if (o instanceof Enum) {
         if (Report.should_report((String)"serialize", 2)) {
            Report.report(2, "- Interning " + s + " : " + o.getClass());
         }

         return ((Enum)o).intern();
      } else {
         if (Report.should_report((String)"serialize", 2)) {
            Report.report(2, "- " + s + " : " + o.getClass());
         }

         return o;
      }
   }
}
