package org.testng.internal;

import org.testng.IInstanceInfo;

public class InstanceInfo implements IInstanceInfo {
   private Class m_instanceClass = null;
   private Object m_instance = null;

   public InstanceInfo(Class cls, Object instance) {
      this.m_instanceClass = cls;
      this.m_instance = instance;
   }

   public Object getInstance() {
      return this.m_instance;
   }

   public Class getInstanceClass() {
      return this.m_instanceClass;
   }
}
