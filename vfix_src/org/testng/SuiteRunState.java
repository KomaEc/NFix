package org.testng;

import java.io.Serializable;

public class SuiteRunState implements Serializable {
   private static final long serialVersionUID = -2716934905049123874L;
   private boolean m_hasFailures;

   public synchronized void failed() {
      this.m_hasFailures = true;
   }

   public synchronized boolean isFailed() {
      return this.m_hasFailures;
   }
}
