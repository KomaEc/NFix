package org.apache.maven.scm.repository;

import org.apache.maven.scm.provider.ScmProviderRepository;

public class ScmRepository {
   private String provider;
   private ScmProviderRepository providerRepository;

   public ScmRepository(String provider, ScmProviderRepository providerRepository) {
      this.provider = provider;
      this.providerRepository = providerRepository;
   }

   public String getProvider() {
      return this.provider;
   }

   public ScmProviderRepository getProviderRepository() {
      return this.providerRepository;
   }

   public String toString() {
      return this.provider.toString() + ":" + this.providerRepository.toString();
   }
}
