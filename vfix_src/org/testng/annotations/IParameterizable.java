package org.testng.annotations;

public interface IParameterizable extends IAnnotation {
   /** @deprecated */
   @Deprecated
   String[] getParameters();

   boolean getEnabled();

   void setEnabled(boolean var1);
}
