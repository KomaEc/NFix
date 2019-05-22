package org.testng.internal;

import java.util.List;
import org.testng.IExtraOutput;
import org.testng.collections.Lists;

public class ExtraOutput implements IExtraOutput {
   private static final long serialVersionUID = 8195388419611912192L;
   private List<String> m_parameterOutput = Lists.newArrayList();

   public List<String> getParameterOutput() {
      return this.m_parameterOutput;
   }
}
