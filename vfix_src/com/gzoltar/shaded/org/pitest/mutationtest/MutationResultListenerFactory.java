package com.gzoltar.shaded.org.pitest.mutationtest;

import com.gzoltar.shaded.org.pitest.plugin.ToolClasspathPlugin;
import java.util.Properties;

public interface MutationResultListenerFactory extends ToolClasspathPlugin {
   MutationResultListener getListener(Properties var1, ListenerArguments var2);

   String name();
}
