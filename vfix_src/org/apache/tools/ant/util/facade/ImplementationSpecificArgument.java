package org.apache.tools.ant.util.facade;

import org.apache.tools.ant.types.Commandline;

public class ImplementationSpecificArgument extends Commandline.Argument {
   private String impl;

   public void setImplementation(String impl) {
      this.impl = impl;
   }

   public final String[] getParts(String chosenImpl) {
      return this.impl != null && !this.impl.equals(chosenImpl) ? new String[0] : super.getParts();
   }
}
