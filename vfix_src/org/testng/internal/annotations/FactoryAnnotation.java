package org.testng.internal.annotations;

import org.testng.annotations.IFactoryAnnotation;

public class FactoryAnnotation extends BaseAnnotation implements IFactoryAnnotation {
   private String[] m_parameters = new String[0];
   private String m_dataProvider = null;
   private Class<?> m_dataProviderClass;
   private boolean m_enabled = true;

   public String getDataProvider() {
      return this.m_dataProvider;
   }

   public void setDataProvider(String dataProvider) {
      this.m_dataProvider = dataProvider;
   }

   public String[] getParameters() {
      return this.m_parameters;
   }

   public void setParameters(String[] parameters) {
      this.m_parameters = parameters;
   }

   public void setDataProviderClass(Class<?> dataProviderClass) {
      this.m_dataProviderClass = dataProviderClass;
   }

   public Class<?> getDataProviderClass() {
      return this.m_dataProviderClass;
   }

   public boolean getEnabled() {
      return this.m_enabled;
   }

   public void setEnabled(boolean enabled) {
      this.m_enabled = enabled;
   }
}
