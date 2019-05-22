package org.testng.internal;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.testng.collections.Maps;
import org.testng.xml.XmlClass;

public class ClassInfoMap {
   private Map<Class<?>, XmlClass> m_map;
   private boolean includeNestedClasses;

   public ClassInfoMap() {
      this.m_map = Maps.newLinkedHashMap();
   }

   public ClassInfoMap(List<XmlClass> classes) {
      this(classes, true);
   }

   public ClassInfoMap(List<XmlClass> classes, boolean includeNested) {
      this.m_map = Maps.newLinkedHashMap();
      this.includeNestedClasses = includeNested;
      Iterator i$ = classes.iterator();

      while(i$.hasNext()) {
         XmlClass xmlClass = (XmlClass)i$.next();

         try {
            Class c = xmlClass.getSupportClass();
            this.registerClass(c, xmlClass);
         } catch (NoClassDefFoundError var6) {
            Utils.log("[ClassInfoMap]", 1, "Unable to open class " + xmlClass.getName() + " - unable to resolve class reference " + var6.getMessage());
            if (xmlClass.loadClasses()) {
               throw var6;
            }
         }
      }

   }

   private void registerClass(Class cl, XmlClass xmlClass) {
      this.m_map.put(cl, xmlClass);
      if (this.includeNestedClasses) {
         Class[] arr$ = cl.getClasses();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class c = arr$[i$];
            if (!this.m_map.containsKey(c)) {
               this.registerClass(c, xmlClass);
            }
         }
      }

   }

   public void addClass(Class<?> cls) {
      this.m_map.put(cls, (Object)null);
   }

   public XmlClass getXmlClass(Class<?> cls) {
      return (XmlClass)this.m_map.get(cls);
   }

   public void put(Class<?> cls, XmlClass xmlClass) {
      this.m_map.put(cls, xmlClass);
   }

   public Set<Class<?>> getClasses() {
      return this.m_map.keySet();
   }

   public int getSize() {
      return this.m_map.size();
   }
}
