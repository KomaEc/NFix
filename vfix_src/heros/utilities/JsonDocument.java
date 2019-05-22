package heros.utilities;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class JsonDocument {
   private DefaultValueMap<String, JsonDocument> documents = new DefaultValueMap<String, JsonDocument>() {
      protected JsonDocument createItem(String key) {
         return new JsonDocument();
      }
   };
   private DefaultValueMap<String, JsonArray> arrays = new DefaultValueMap<String, JsonArray>() {
      protected JsonArray createItem(String key) {
         return new JsonArray();
      }
   };
   private Map<String, String> keyValuePairs = Maps.newHashMap();

   public JsonDocument doc(String key) {
      return (JsonDocument)this.documents.getOrCreate(key);
   }

   public JsonDocument doc(String key, JsonDocument doc) {
      if (this.documents.containsKey(key)) {
         throw new IllegalArgumentException("There is already a document registered for key: " + key);
      } else {
         this.documents.put(key, doc);
         return doc;
      }
   }

   public JsonArray array(String key) {
      return (JsonArray)this.arrays.getOrCreate(key);
   }

   public void keyValue(String key, String value) {
      this.keyValuePairs.put(key, value);
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      this.write(builder, 0);
      return builder.toString();
   }

   public void write(StringBuilder builder, int tabs) {
      builder.append("{\n");
      Iterator var3 = this.keyValuePairs.entrySet().iterator();

      Entry entry;
      while(var3.hasNext()) {
         entry = (Entry)var3.next();
         tabs(tabs + 1, builder);
         builder.append("\"" + (String)entry.getKey() + "\": \"" + (String)entry.getValue() + "\",\n");
      }

      var3 = this.arrays.entrySet().iterator();

      while(var3.hasNext()) {
         entry = (Entry)var3.next();
         tabs(tabs + 1, builder);
         builder.append("\"" + (String)entry.getKey() + "\": ");
         ((JsonArray)entry.getValue()).write(builder, tabs + 1);
         builder.append(",\n");
      }

      var3 = this.documents.entrySet().iterator();

      while(var3.hasNext()) {
         entry = (Entry)var3.next();
         tabs(tabs + 1, builder);
         builder.append("\"" + (String)entry.getKey() + "\": ");
         ((JsonDocument)entry.getValue()).write(builder, tabs + 1);
         builder.append(",\n");
      }

      if (!this.keyValuePairs.isEmpty() || !this.arrays.isEmpty() || !this.documents.isEmpty()) {
         builder.delete(builder.length() - 2, builder.length() - 1);
      }

      tabs(tabs, builder);
      builder.append("}");
   }

   static void tabs(int tabs, StringBuilder builder) {
      for(int i = 0; i < tabs; ++i) {
         builder.append("\t");
      }

   }
}
