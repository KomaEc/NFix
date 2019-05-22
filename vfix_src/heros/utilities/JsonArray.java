package heros.utilities;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

public class JsonArray {
   private List<String> items = Lists.newLinkedList();

   public void add(String item) {
      this.items.add(item);
   }

   public void write(StringBuilder builder, int tabs) {
      builder.append("[\n");
      Iterator var3 = this.items.iterator();

      while(var3.hasNext()) {
         String item = (String)var3.next();
         JsonDocument.tabs(tabs + 1, builder);
         builder.append("\"" + item + "\",\n");
      }

      if (!this.items.isEmpty()) {
         builder.delete(builder.length() - 2, builder.length() - 1);
      }

      JsonDocument.tabs(tabs, builder);
      builder.append("]");
   }
}
