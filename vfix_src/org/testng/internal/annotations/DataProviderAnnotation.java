package org.testng.internal.annotations;

import org.testng.annotations.IDataProviderAnnotation;

public class DataProviderAnnotation extends BaseAnnotation implements IDataProviderAnnotation {
   private String m_name;
   private boolean m_parallel;

   public boolean isParallel() {
      return this.m_parallel;
   }

   public void setParallel(boolean parallel) {
      this.m_parallel = parallel;
   }

   public String getName() {
      return this.m_name;
   }

   public void setName(String name) {
      this.m_name = name;
   }
}
