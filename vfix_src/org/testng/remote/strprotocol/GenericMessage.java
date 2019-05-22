package org.testng.remote.strprotocol;

public class GenericMessage implements IStringMessage {
   protected final int m_messageType;
   private int m_suiteCount;
   private int m_testCount;

   public GenericMessage(int type) {
      this.m_messageType = type;
   }

   public int getSuiteCount() {
      return this.m_suiteCount;
   }

   public void setSuiteCount(int suiteCount) {
      this.m_suiteCount = suiteCount;
   }

   public int getTestCount() {
      return this.m_testCount;
   }

   public void setTestCount(int testCount) {
      this.m_testCount = testCount;
   }

   public GenericMessage addProperty(String propName, int propValue) {
      return this.addProperty(propName, propValue);
   }

   public String getMessageAsString() {
      StringBuffer buf = new StringBuffer();
      buf.append(this.m_messageType);
      buf.append('\u0001').append("testCount").append(this.getTestCount()).append('\u0001').append("suiteCount").append(this.getSuiteCount());
      return buf.toString();
   }

   public String toString() {
      return "[GenericMessage suiteCount:" + this.m_suiteCount + " testCount:" + this.m_testCount + "]";
   }
}
