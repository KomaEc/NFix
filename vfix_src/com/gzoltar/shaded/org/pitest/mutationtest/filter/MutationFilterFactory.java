package com.gzoltar.shaded.org.pitest.mutationtest.filter;

import com.gzoltar.shaded.org.pitest.classpath.CodeSource;
import com.gzoltar.shaded.org.pitest.plugin.ToolClasspathPlugin;
import java.util.Properties;

public interface MutationFilterFactory extends ToolClasspathPlugin {
   MutationFilter createFilter(Properties var1, CodeSource var2, int var3);
}
