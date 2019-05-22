package com.gzoltar.shaded.org.pitest.mutationtest.build;

import com.gzoltar.shaded.org.pitest.classpath.CodeSource;
import com.gzoltar.shaded.org.pitest.plugin.ToolClasspathPlugin;
import java.util.Properties;

public interface MutationGrouperFactory extends ToolClasspathPlugin {
   MutationGrouper makeFactory(Properties var1, CodeSource var2, int var3, int var4);
}
