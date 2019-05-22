package org.apache.maven.plugin.surefire;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.surefire.booterclient.ProviderDetector;
import org.apache.maven.surefire.providerapi.SurefireProvider;

public class ProviderList {
   private final ProviderInfo[] wellKnownProviders;
   private final ConfigurableProviderInfo dynamicProvider;

   public ProviderList(ConfigurableProviderInfo dynamicProviderInfo, ProviderInfo... wellKnownProviders) {
      this.wellKnownProviders = wellKnownProviders;
      this.dynamicProvider = dynamicProviderInfo;
   }

   @Nonnull
   public List<ProviderInfo> resolve(@Nonnull Log log) {
      List<ProviderInfo> providersToRun = new ArrayList();
      Set<String> manuallyConfiguredProviders = this.getManuallyConfiguredProviders();
      if (manuallyConfiguredProviders.size() > 0) {
         Iterator i$ = manuallyConfiguredProviders.iterator();

         while(i$.hasNext()) {
            String name = (String)i$.next();
            ProviderInfo wellKnown = this.findByName(name);
            ProviderInfo providerToAdd = wellKnown != null ? wellKnown : this.dynamicProvider.instantiate(name);
            log.info((CharSequence)("Using configured provider " + providerToAdd.getProviderName()));
            providersToRun.add(providerToAdd);
         }

         return providersToRun;
      } else {
         return this.autoDetectOneProvider();
      }
   }

   @Nonnull
   private List<ProviderInfo> autoDetectOneProvider() {
      List<ProviderInfo> providersToRun = new ArrayList();
      ProviderInfo[] arr$ = this.wellKnownProviders;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ProviderInfo wellKnownProvider = arr$[i$];
         if (wellKnownProvider.isApplicable()) {
            providersToRun.add(wellKnownProvider);
            return providersToRun;
         }
      }

      return providersToRun;
   }

   private Set<String> getManuallyConfiguredProviders() {
      try {
         return ProviderDetector.getServiceNames(SurefireProvider.class, Thread.currentThread().getContextClassLoader());
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   private ProviderInfo findByName(String providerClassName) {
      ProviderInfo[] arr$ = this.wellKnownProviders;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ProviderInfo wellKnownProvider = arr$[i$];
         if (wellKnownProvider.getProviderName().equals(providerClassName)) {
            return wellKnownProvider;
         }
      }

      return null;
   }
}
