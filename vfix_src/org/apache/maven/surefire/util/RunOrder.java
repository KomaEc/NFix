package org.apache.maven.surefire.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class RunOrder {
   public static final RunOrder ALPHABETICAL = new RunOrder("alphabetical");
   public static final RunOrder FILESYSTEM = new RunOrder("filesystem");
   public static final RunOrder HOURLY = new RunOrder("hourly");
   public static final RunOrder RANDOM = new RunOrder("random");
   public static final RunOrder REVERSE_ALPHABETICAL = new RunOrder("reversealphabetical");
   public static final RunOrder BALANCED = new RunOrder("balanced");
   public static final RunOrder FAILEDFIRST = new RunOrder("failedfirst");
   public static final RunOrder[] DEFAULT;
   private final String name;

   public static RunOrder[] valueOfMulti(String values) {
      List<RunOrder> result = new ArrayList();
      if (values != null) {
         StringTokenizer stringTokenizer = new StringTokenizer(values, ",");

         while(stringTokenizer.hasMoreTokens()) {
            result.add(valueOf(stringTokenizer.nextToken()));
         }
      }

      return (RunOrder[])result.toArray(new RunOrder[result.size()]);
   }

   public static RunOrder valueOf(String name) {
      if (name == null) {
         return null;
      } else {
         RunOrder[] runOrders = values();
         RunOrder[] arr$ = runOrders;
         int len$ = runOrders.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            RunOrder runOrder = arr$[i$];
            if (runOrder.matches(name)) {
               return runOrder;
            }
         }

         StringBuffer errorMessage = createMessageForMissingRunOrder(name);
         throw new IllegalArgumentException(errorMessage.toString());
      }
   }

   private static StringBuffer createMessageForMissingRunOrder(String name) {
      RunOrder[] runOrders = values();
      StringBuffer message = new StringBuffer();
      message.append("There's no RunOrder with the name ");
      message.append(name);
      message.append(". Please use one of the following RunOrders: ");

      for(int i = 0; i < runOrders.length; ++i) {
         if (i != 0) {
            message.append(", ");
         }

         message.append(runOrders[i]);
      }

      message.append(".");
      return message;
   }

   private static RunOrder[] values() {
      return new RunOrder[]{ALPHABETICAL, FILESYSTEM, HOURLY, RANDOM, REVERSE_ALPHABETICAL, BALANCED, FAILEDFIRST};
   }

   public static String asString(RunOrder[] runOrder) {
      StringBuilder stringBuffer = new StringBuilder();

      for(int i = 0; i < runOrder.length; ++i) {
         stringBuffer.append(runOrder[i].name);
         if (i < runOrder.length - 1) {
            stringBuffer.append(",");
         }
      }

      return stringBuffer.toString();
   }

   private RunOrder(String name) {
      this.name = name;
   }

   private boolean matches(String anotherName) {
      return this.name.equalsIgnoreCase(anotherName);
   }

   public String name() {
      return this.name;
   }

   public String toString() {
      return this.name;
   }

   static {
      DEFAULT = new RunOrder[]{FILESYSTEM};
   }
}
