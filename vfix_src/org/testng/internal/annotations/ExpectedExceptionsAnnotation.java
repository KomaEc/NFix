package org.testng.internal.annotations;

import org.testng.annotations.IExpectedExceptionsAnnotation;

public class ExpectedExceptionsAnnotation extends BaseAnnotation implements IExpectedExceptionsAnnotation {
   private Class[] m_value = new Class[0];

   public Class[] getValue() {
      return this.m_value;
   }

   public void setValue(Class[] value) {
      this.m_value = value;
   }
}
