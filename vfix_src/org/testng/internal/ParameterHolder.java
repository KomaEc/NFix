package org.testng.internal;

import java.util.Iterator;

public class ParameterHolder {
   public DataProviderHolder dataProviderHolder;
   public Iterator<Object[]> parameters;
   public ParameterHolder.ParameterOrigin origin;

   public ParameterHolder(Iterator<Object[]> parameters, ParameterHolder.ParameterOrigin origin, DataProviderHolder dph) {
      this.parameters = parameters;
      this.origin = origin;
      this.dataProviderHolder = dph;
   }

   public static enum ParameterOrigin {
      ORIGIN_DATA_PROVIDER,
      ORIGIN_XML;
   }
}
