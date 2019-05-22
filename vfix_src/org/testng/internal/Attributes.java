package org.testng.internal;

import java.util.Map;
import java.util.Set;
import org.testng.IAttributes;
import org.testng.collections.Maps;

public class Attributes implements IAttributes {
   private static final long serialVersionUID = 6701159979281335152L;
   private Map<String, Object> m_attributes = Maps.newHashMap();

   public Object getAttribute(String name) {
      return this.m_attributes.get(name);
   }

   public Set<String> getAttributeNames() {
      return this.m_attributes.keySet();
   }

   public void setAttribute(String name, Object value) {
      this.m_attributes.put(name, value);
   }

   public Object removeAttribute(String name) {
      return this.m_attributes.remove(name);
   }
}
