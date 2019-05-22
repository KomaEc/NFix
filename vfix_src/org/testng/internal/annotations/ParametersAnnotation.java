package org.testng.internal.annotations;

import org.testng.annotations.IParametersAnnotation;

public class ParametersAnnotation extends BaseAnnotation implements IParametersAnnotation {
   private String[] m_value = new String[0];

   public String[] getValue() {
      return this.m_value;
   }

   public void setValue(String[] value) {
      this.m_value = value;
   }
}
