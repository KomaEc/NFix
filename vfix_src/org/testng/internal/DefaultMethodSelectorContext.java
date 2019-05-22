package org.testng.internal;

import java.util.Map;
import org.testng.IMethodSelectorContext;
import org.testng.collections.Maps;

public class DefaultMethodSelectorContext implements IMethodSelectorContext {
   private Map<Object, Object> m_userData = Maps.newHashMap();
   private boolean m_isStopped = false;

   public Map<Object, Object> getUserData() {
      return this.m_userData;
   }

   public boolean isStopped() {
      return this.m_isStopped;
   }

   public void setStopped(boolean stopped) {
      this.m_isStopped = stopped;
   }
}
