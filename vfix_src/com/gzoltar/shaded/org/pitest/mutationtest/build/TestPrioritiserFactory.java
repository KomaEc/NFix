package com.gzoltar.shaded.org.pitest.mutationtest.build;

import com.gzoltar.shaded.org.pitest.classpath.CodeSource;
import com.gzoltar.shaded.org.pitest.coverage.CoverageDatabase;
import com.gzoltar.shaded.org.pitest.plugin.ToolClasspathPlugin;
import java.util.Properties;

public interface TestPrioritiserFactory extends ToolClasspathPlugin {
   TestPrioritiser makeTestPrioritiser(Properties var1, CodeSource var2, CoverageDatabase var3);
}
