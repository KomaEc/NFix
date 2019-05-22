package com.gzoltar.shaded.org.pitest.mutationtest.config;

import com.gzoltar.shaded.org.pitest.mutationtest.MutationEngineFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListenerFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.build.MutationGrouperFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.build.TestPrioritiserFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.filter.MutationFilterFactory;
import com.gzoltar.shaded.org.pitest.plugin.ClientClasspathPlugin;
import com.gzoltar.shaded.org.pitest.plugin.ToolClasspathPlugin;
import com.gzoltar.shaded.org.pitest.testapi.TestPluginFactory;
import com.gzoltar.shaded.org.pitest.util.IsolationUtils;
import com.gzoltar.shaded.org.pitest.util.ServiceLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PluginServices {
   private final ClassLoader loader;

   public PluginServices(ClassLoader loader) {
      this.loader = loader;
   }

   public static PluginServices makeForContextLoader() {
      return new PluginServices(IsolationUtils.getContextClassLoader());
   }

   public Iterable<? extends ToolClasspathPlugin> findToolClasspathPlugins() {
      List<ToolClasspathPlugin> l = new ArrayList();
      l.addAll(this.findListeners());
      l.addAll(this.findGroupers());
      l.addAll(this.findFilters());
      l.addAll(this.findTestPrioritisers());
      return l;
   }

   public Iterable<? extends ClientClasspathPlugin> findClientClasspathPlugins() {
      List<ClientClasspathPlugin> l = new ArrayList();
      l.addAll(this.findMutationEngines());
      l.addAll(this.findTestFrameworkPlugins());
      l.addAll(this.nullPlugins());
      return l;
   }

   Collection<? extends TestPluginFactory> findTestFrameworkPlugins() {
      return ServiceLoader.load(TestPluginFactory.class, this.loader);
   }

   Collection<? extends MutationGrouperFactory> findGroupers() {
      return ServiceLoader.load(MutationGrouperFactory.class, this.loader);
   }

   Collection<? extends MutationFilterFactory> findFilters() {
      return ServiceLoader.load(MutationFilterFactory.class, this.loader);
   }

   Collection<? extends MutationResultListenerFactory> findListeners() {
      return ServiceLoader.load(MutationResultListenerFactory.class, this.loader);
   }

   Collection<? extends MutationEngineFactory> findMutationEngines() {
      return ServiceLoader.load(MutationEngineFactory.class, this.loader);
   }

   Collection<? extends TestPrioritiserFactory> findTestPrioritisers() {
      return ServiceLoader.load(TestPrioritiserFactory.class, this.loader);
   }

   private Collection<ClientClasspathPlugin> nullPlugins() {
      return ServiceLoader.load(ClientClasspathPlugin.class, this.loader);
   }
}
