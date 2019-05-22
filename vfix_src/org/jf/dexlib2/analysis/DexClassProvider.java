package org.jf.dexlib2.analysis;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;

public class DexClassProvider implements ClassProvider {
   private final DexFile dexFile;
   private Map<String, ClassDef> classMap = Maps.newHashMap();

   public DexClassProvider(DexFile dexFile) {
      this.dexFile = dexFile;
      Iterator var2 = dexFile.getClasses().iterator();

      while(var2.hasNext()) {
         ClassDef classDef = (ClassDef)var2.next();
         this.classMap.put(classDef.getType(), classDef);
      }

   }

   @Nullable
   public ClassDef getClassDef(String type) {
      return (ClassDef)this.classMap.get(type);
   }
}
