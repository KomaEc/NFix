package pxb.android.arsc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class BagValue {
   public List<Entry<Integer, Value>> map = new ArrayList();
   public final int parent;

   public BagValue(int parent) {
      this.parent = parent;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (!(obj instanceof BagValue)) {
         return false;
      } else {
         BagValue other = (BagValue)obj;
         if (this.map == null) {
            if (other.map != null) {
               return false;
            }
         } else if (!this.map.equals(other.map)) {
            return false;
         }

         return this.parent == other.parent;
      }
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.map == null ? 0 : this.map.hashCode());
      result = 31 * result + this.parent;
      return result;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(String.format("{bag%08x", this.parent));
      Iterator var2 = this.map.iterator();

      while(var2.hasNext()) {
         Entry<Integer, Value> e = (Entry)var2.next();
         sb.append(",").append(String.format("0x%08x", e.getKey()));
         sb.append("=");
         sb.append(e.getValue());
      }

      return sb.append("}").toString();
   }
}
