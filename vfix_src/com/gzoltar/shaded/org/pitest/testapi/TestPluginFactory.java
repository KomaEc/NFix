package com.gzoltar.shaded.org.pitest.testapi;

import com.gzoltar.shaded.org.pitest.classinfo.ClassByteArraySource;
import com.gzoltar.shaded.org.pitest.plugin.ClientClasspathPlugin;

public interface TestPluginFactory extends ClientClasspathPlugin {
   Configuration createTestFrameworkConfiguration(TestGroupConfig var1, ClassByteArraySource var2);
}
