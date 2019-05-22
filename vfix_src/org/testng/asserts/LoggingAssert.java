package org.testng.asserts;

import java.util.List;
import org.testng.collections.Lists;

public class LoggingAssert extends Assertion {
   private List<String> m_messages = Lists.newArrayList();

   public void onBeforeAssert(IAssert<?> a) {
      this.m_messages.add("Test:" + a.getMessage());
   }

   public List<String> getMessages() {
      return this.m_messages;
   }
}
