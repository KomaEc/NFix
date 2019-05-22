package soot.coffi;

import java.util.HashMap;
import soot.G;
import soot.Singletons;

public class CONSTANT_Utf8_collector {
   HashMap<String, CONSTANT_Utf8_info> hash = null;

   public CONSTANT_Utf8_collector(Singletons.Global g) {
   }

   public static CONSTANT_Utf8_collector v() {
      return G.v().soot_coffi_CONSTANT_Utf8_collector();
   }

   synchronized CONSTANT_Utf8_info add(CONSTANT_Utf8_info _Utf8_info) {
      if (this.hash == null) {
         this.hash = new HashMap();
      }

      String Utf8_str_key = _Utf8_info.convert();
      if (this.hash.containsKey(Utf8_str_key)) {
         return (CONSTANT_Utf8_info)this.hash.get(Utf8_str_key);
      } else {
         this.hash.put(Utf8_str_key, _Utf8_info);
         _Utf8_info.fixConversion(Utf8_str_key);
         return _Utf8_info;
      }
   }
}
