package com.gzoltar.shaded.org.pitest.mutationtest.config;

import com.gzoltar.shaded.org.pitest.classinfo.ClassByteArraySource;
import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.classinfo.Repository;
import com.gzoltar.shaded.org.pitest.help.Help;
import com.gzoltar.shaded.org.pitest.help.PitHelpError;
import com.gzoltar.shaded.org.pitest.junit.JUnitCompatibleConfiguration;
import com.gzoltar.shaded.org.pitest.testapi.Configuration;
import com.gzoltar.shaded.org.pitest.testapi.TestGroupConfig;
import com.gzoltar.shaded.org.pitest.testng.TestNGConfiguration;
import java.util.ArrayList;
import java.util.Collection;

class ConfigurationFactory {
   private final ClassByteArraySource source;
   private final TestGroupConfig config;

   public ConfigurationFactory(TestGroupConfig config, ClassByteArraySource source) {
      this.source = source;
      this.config = config;
   }

   public Configuration createConfiguration() {
      Collection<Configuration> configs = new ArrayList();
      Repository classRepository = new Repository(this.source);
      if (classRepository.fetchClass(ClassName.fromString("org.junit.Test")).hasSome()) {
         configs.add(new JUnitCompatibleConfiguration(this.config));
      }

      if (classRepository.fetchClass(ClassName.fromString("org.testng.TestNG")).hasSome()) {
         configs.add(new TestNGConfiguration(this.config));
      }

      if (configs.isEmpty()) {
         throw new PitHelpError(Help.NO_TEST_LIBRARY, new Object[0]);
      } else {
         return new CompoundConfiguration(configs);
      }
   }
}
