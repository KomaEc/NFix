package org.codehaus.groovy.util;

import java.lang.ref.ReferenceQueue;

public class ReferenceBundle {
   private ReferenceManager manager;
   private ReferenceType type;
   private static final ReferenceBundle softReferences;
   private static final ReferenceBundle weakReferences;

   public ReferenceBundle(ReferenceManager manager, ReferenceType type) {
      this.manager = manager;
      this.type = type;
   }

   public ReferenceType getType() {
      return this.type;
   }

   public ReferenceManager getManager() {
      return this.manager;
   }

   public static ReferenceBundle getSoftBundle() {
      return softReferences;
   }

   public static ReferenceBundle getWeakBundle() {
      return weakReferences;
   }

   static {
      ReferenceQueue queue = new ReferenceQueue();
      ReferenceManager callBack = ReferenceManager.createCallBackedManager(queue);
      ReferenceManager manager = ReferenceManager.createThresholdedIdlingManager(queue, callBack, 5000);
      softReferences = new ReferenceBundle(manager, ReferenceType.SOFT);
      weakReferences = new ReferenceBundle(manager, ReferenceType.WEAK);
   }
}
