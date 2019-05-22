package com.gzoltar.shaded.org.pitest.mutationtest.config;

import com.gzoltar.shaded.org.pitest.classinfo.ClassByteArraySource;
import com.gzoltar.shaded.org.pitest.testapi.Configuration;
import com.gzoltar.shaded.org.pitest.testapi.TestGroupConfig;
import com.gzoltar.shaded.org.pitest.testapi.TestPluginFactory;

public class LegacyTestFrameworkPlugin implements TestPluginFactory {
   public String description() {
      return "Default test framework support";
   }

   public Configuration createTestFrameworkConfiguration(TestGroupConfig config, ClassByteArraySource source) {
      ConfigurationFactory configFactory = new ConfigurationFactory(config, source);
      return configFactory.createConfiguration();
   }
}
