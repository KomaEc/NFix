package com.gzoltar.shaded.org.pitest.classinfo;

import com.gzoltar.shaded.org.pitest.reloc.asm.ClassReader;
import com.gzoltar.shaded.org.pitest.reloc.asm.ClassWriter;
import java.util.Map;

public class ComputeClassWriter extends ClassWriter {
   private final ClassByteArraySource bytes;
   private final Map<String, String> cache;

   public ComputeClassWriter(ClassByteArraySource bytes, Map<String, String> cache, int flags) {
      super(flags);
      this.bytes = bytes;
      this.cache = cache;
   }

   protected String getCommonSuperClass(String type1, String type2) {
      String key = type1 + "!_!" + type2;
      String previous = (String)this.cache.get(key);
      if (previous != null) {
         return previous;
      } else {
         ClassReader info1 = this.typeInfo(type1);
         ClassReader info2 = this.typeInfo(type2);
         String result = this.getCommonSuperClass(type1, info1, type2, info2);
         this.cache.put(key, result);
         return result;
      }
   }

   private String getCommonSuperClass(String type1, ClassReader info1, String type2, ClassReader info2) {
      if (this.isInterface(info1)) {
         if (this.typeImplements(type2, info2, type1)) {
            return type1;
         }

         if (this.isInterface(info2)) {
            if (this.typeImplements(type1, info1, type2)) {
               return type2;
            }

            return "java/lang/Object";
         }
      }

      StringBuilder b1 = this.typeAncestors(type1, info1);
      StringBuilder b2 = this.typeAncestors(type2, info2);
      String result = "java/lang/Object";
      int end1 = b1.length();
      int end2 = b2.length();

      while(true) {
         int start1 = b1.lastIndexOf(";", end1 - 1);
         int start2 = b2.lastIndexOf(";", end2 - 1);
         if (start1 == -1 || start2 == -1 || end1 - start1 != end2 - start2) {
            return result;
         }

         String p1 = b1.substring(start1 + 1, end1);
         String p2 = b2.substring(start2 + 1, end2);
         if (!p1.equals(p2)) {
            return result;
         }

         result = p1;
         end1 = start1;
         end2 = start2;
      }
   }

   private boolean isInterface(ClassReader info1) {
      return (info1.getAccess() & 512) != 0;
   }

   private StringBuilder typeAncestors(String type, ClassReader info) {
      StringBuilder b;
      for(b = new StringBuilder(); !"java/lang/Object".equals(type); info = this.typeInfo(type)) {
         b.append(';').append(type);
         type = info.getSuperName();
      }

      return b;
   }

   private boolean typeImplements(String type, ClassReader info, String itf) {
      for(String cleanItf = itf.replace(".", "/"); !"java/lang/Object".equals(type); info = this.typeInfo(type)) {
         String[] itfs = info.getInterfaces();
         String[] arr$ = itfs;
         int len$ = itfs.length;

         int i$;
         String itf2;
         for(i$ = 0; i$ < len$; ++i$) {
            itf2 = arr$[i$];
            if (itf2.equals(cleanItf)) {
               return true;
            }
         }

         arr$ = itfs;
         len$ = itfs.length;

         for(i$ = 0; i$ < len$; ++i$) {
            itf2 = arr$[i$];
            if (this.typeImplements(itf2, this.typeInfo(itf2), cleanItf)) {
               return true;
            }
         }

         type = info.getSuperName();
      }

      return false;
   }

   private ClassReader typeInfo(String type) {
      return new ClassReader((byte[])this.bytes.getBytes(type).value());
   }
}
