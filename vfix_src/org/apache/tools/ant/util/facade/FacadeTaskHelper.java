package org.apache.tools.ant.util.facade;

import java.util.Enumeration;
import java.util.Vector;

public class FacadeTaskHelper {
   private Vector args;
   private String userChoice;
   private String magicValue;
   private String defaultValue;

   public FacadeTaskHelper(String defaultValue) {
      this(defaultValue, (String)null);
   }

   public FacadeTaskHelper(String defaultValue, String magicValue) {
      this.args = new Vector();
      this.defaultValue = defaultValue;
      this.magicValue = magicValue;
   }

   public void setMagicValue(String magicValue) {
      this.magicValue = magicValue;
   }

   public void setImplementation(String userChoice) {
      this.userChoice = userChoice;
   }

   public String getImplementation() {
      return this.userChoice != null ? this.userChoice : (this.magicValue != null ? this.magicValue : this.defaultValue);
   }

   public String getExplicitChoice() {
      return this.userChoice;
   }

   public void addImplementationArgument(ImplementationSpecificArgument arg) {
      this.args.addElement(arg);
   }

   public String[] getArgs() {
      Vector tmp = new Vector(this.args.size());
      Enumeration e = this.args.elements();

      while(e.hasMoreElements()) {
         ImplementationSpecificArgument arg = (ImplementationSpecificArgument)e.nextElement();
         String[] curr = arg.getParts(this.getImplementation());

         for(int i = 0; i < curr.length; ++i) {
            tmp.addElement(curr[i]);
         }
      }

      String[] res = new String[tmp.size()];
      tmp.copyInto(res);
      return res;
   }

   public boolean hasBeenSet() {
      return this.userChoice != null || this.magicValue != null;
   }
}
