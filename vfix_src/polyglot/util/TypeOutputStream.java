package polyglot.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import polyglot.main.Report;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;

public class TypeOutputStream extends ObjectOutputStream {
   protected TypeSystem ts;
   protected Set roots;
   protected Map placeHolders;

   public TypeOutputStream(OutputStream out, TypeSystem ts, Type root) throws IOException {
      super(out);
      this.ts = ts;
      this.roots = ts.getTypeEncoderRootSet(root);
      this.placeHolders = new HashMap();
      if (Report.should_report((String)"serialize", 2)) {
         Report.report(2, "Began TypeOutputStream with roots: " + this.roots);
      }

      this.enableReplaceObject(true);
   }

   protected Object placeHolder(TypeObject o) {
      Object k = new IdentityKey(o);
      Object p = this.placeHolders.get(k);
      if (p == null) {
         p = this.ts.placeHolder(o, this.roots);
         this.placeHolders.put(k, p);
      }

      return p;
   }

   protected Object replaceObject(Object o) throws IOException {
      if (this.roots.contains(o)) {
         if (Report.should_report((String)"serialize", 2)) {
            Report.report(2, "+ In roots: " + o + " : " + o.getClass());
         }

         return o;
      } else if (o instanceof TypeObject) {
         Object r = this.placeHolder((TypeObject)o);
         if (Report.should_report((String)"serialize", 2)) {
            if (r != o) {
               Report.report(2, "+ Replacing: " + o + " : " + o.getClass() + " with " + r);
            } else {
               Report.report(2, "+ " + o + " : " + o.getClass());
            }
         }

         return r;
      } else {
         if (Report.should_report((String)"serialize", 2)) {
            Report.report(2, "+ " + o + " : " + o.getClass());
         }

         return o;
      }
   }
}
