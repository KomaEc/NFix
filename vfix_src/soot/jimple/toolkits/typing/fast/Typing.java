package soot.jimple.toolkits.typing.fast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import soot.Local;
import soot.Type;

public class Typing {
   private HashMap<Local, Type> map;

   public Typing(Collection<Local> vs) {
      this.map = new HashMap(vs.size());
      BottomType bottomType = BottomType.v();
      Iterator var3 = vs.iterator();

      while(var3.hasNext()) {
         Local v = (Local)var3.next();
         this.map.put(v, bottomType);
      }

   }

   public Typing(Typing tg) {
      this.map = new HashMap(tg.map);
   }

   public Type get(Local v) {
      return (Type)this.map.get(v);
   }

   public Type set(Local v, Type t) {
      return (Type)this.map.put(v, t);
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append('{');
      Iterator var2 = this.map.keySet().iterator();

      while(var2.hasNext()) {
         Local v = (Local)var2.next();
         sb.append(v);
         sb.append(':');
         sb.append(this.get(v));
         sb.append(',');
      }

      sb.append('}');
      return sb.toString();
   }

   public static void minimize(List<Typing> tgs, IHierarchy h) {
      ListIterator i = tgs.listIterator();

      while(true) {
         while(i.hasNext()) {
            Typing tgi = (Typing)i.next();
            Iterator var4 = tgs.iterator();

            while(var4.hasNext()) {
               Typing tgj = (Typing)var4.next();
               if (tgi != tgj && compare(tgi, tgj, h) == 1) {
                  i.remove();
                  break;
               }
            }
         }

         return;
      }
   }

   public static int compare(Typing a, Typing b, IHierarchy h) {
      int r = 0;
      Iterator var4 = a.map.keySet().iterator();

      while(var4.hasNext()) {
         Local v = (Local)var4.next();
         Type ta = a.get(v);
         Type tb = b.get(v);
         byte cmp;
         if (TypeResolver.typesEqual(ta, tb)) {
            cmp = 0;
         } else if (h.ancestor(ta, tb)) {
            cmp = 1;
         } else {
            if (!h.ancestor(tb, ta)) {
               return -2;
            }

            cmp = -1;
         }

         if (cmp == 1 && r == -1 || cmp == -1 && r == 1) {
            return 2;
         }

         if (r == 0) {
            r = cmp;
         }
      }

      return r;
   }
}
