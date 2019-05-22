package org.apache.maven.scm.provider.synergy.util;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.log.ScmLogger;

public class SynergyTaskManager {
   private static final short TASK_STATE_NONE = 0;
   private static final short TASK_STATE_CREATED = 1;
   private static final short TASK_STATE_COMPLETED = 2;
   private static final SynergyTaskManager INSTANCE = new SynergyTaskManager();
   private int currentTaskNumber;
   private short currentTaskState = 0;

   public static SynergyTaskManager getInstance() {
      return INSTANCE;
   }

   public int createTask(ScmLogger logger, String synopsis, String release, boolean defaultTask, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering createTask method of SynergyTaskManager");
      }

      switch(this.currentTaskState) {
      case 0:
      case 2:
         this.currentTaskNumber = SynergyUtil.createTask(logger, synopsis, release, defaultTask, ccmAddr);
         this.currentTaskState = 1;
         break;
      case 1:
         if (defaultTask && SynergyUtil.getDefaultTask(logger, ccmAddr) != this.currentTaskNumber) {
            SynergyUtil.setDefaultTask(logger, this.currentTaskNumber, ccmAddr);
         }
         break;
      default:
         throw new IllegalStateException("Programming error: SynergyTaskManager is in unkown state.");
      }

      if (logger.isDebugEnabled()) {
         logger.debug("createTask returns " + this.currentTaskNumber);
      }

      return this.currentTaskNumber;
   }

   public void checkinDefaultTask(ScmLogger logger, String comment, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering checkinDefaultTask method of SynergyTaskManager");
      }

      switch(this.currentTaskState) {
      case 0:
         if (SynergyUtil.getDefaultTask(logger, ccmAddr) == 0) {
            throw new ScmException("Check in not possible: no default task is set and no task has been created with SynergyTaskManager.");
         }

         SynergyUtil.checkinDefaultTask(logger, comment, ccmAddr);
         break;
      case 1:
         SynergyUtil.checkinTask(logger, this.currentTaskNumber, comment, ccmAddr);
         this.currentTaskState = 2;
         break;
      case 2:
         if (SynergyUtil.getDefaultTask(logger, ccmAddr) != 0) {
            SynergyUtil.checkinDefaultTask(logger, comment, ccmAddr);
         } else if (logger.isDebugEnabled()) {
            logger.debug("Synergy : No check in necessary as default task and all tasks created with SynergyTaskManager have already been checked in.");
         }
         break;
      default:
         throw new IllegalStateException("Programming error: SynergyTaskManager is in unkown state.");
      }

   }
}
