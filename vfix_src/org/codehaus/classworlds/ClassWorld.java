package org.codehaus.classworlds;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClassWorld {
   private Map realms;

   public ClassWorld(String realmId, ClassLoader classLoader) {
      this();

      try {
         this.newRealm(realmId, classLoader);
      } catch (DuplicateRealmException var4) {
      }

   }

   public ClassWorld() {
      this.realms = new HashMap();
   }

   public ClassRealm newRealm(String id) throws DuplicateRealmException {
      return this.newRealm(id, (ClassLoader)null);
   }

   public ClassRealm newRealm(String id, ClassLoader classLoader) throws DuplicateRealmException {
      if (this.realms.containsKey(id)) {
         throw new DuplicateRealmException(this, id);
      } else {
         ClassRealm realm = null;
         if (classLoader != null) {
            realm = new DefaultClassRealm(this, id, classLoader);
            this.realms.put(id, realm);
         } else {
            realm = new DefaultClassRealm(this, id);
         }

         this.realms.put(id, realm);
         return realm;
      }
   }

   public void disposeRealm(String id) throws NoSuchRealmException {
      this.realms.remove(id);
   }

   public ClassRealm getRealm(String id) throws NoSuchRealmException {
      if (this.realms.containsKey(id)) {
         return (ClassRealm)this.realms.get(id);
      } else {
         throw new NoSuchRealmException(this, id);
      }
   }

   public Collection getRealms() {
      return this.realms.values();
   }

   Class loadClass(String name) throws ClassNotFoundException {
      return this.getClass().getClassLoader().loadClass(name);
   }
}
