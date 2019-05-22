package org.apache.maven.profiles;

import java.util.Iterator;
import java.util.List;

public class ProfilesConversionUtils {
   private ProfilesConversionUtils() {
   }

   public static org.apache.maven.model.Profile convertFromProfileXmlProfile(Profile profileXmlProfile) {
      org.apache.maven.model.Profile profile = new org.apache.maven.model.Profile();
      profile.setId(profileXmlProfile.getId());
      profile.setSource("profiles.xml");
      Activation profileActivation = profileXmlProfile.getActivation();
      if (profileActivation != null) {
         org.apache.maven.model.Activation activation = new org.apache.maven.model.Activation();
         activation.setActiveByDefault(profileActivation.isActiveByDefault());
         activation.setJdk(profileActivation.getJdk());
         ActivationProperty profileProp = profileActivation.getProperty();
         if (profileProp != null) {
            org.apache.maven.model.ActivationProperty prop = new org.apache.maven.model.ActivationProperty();
            prop.setName(profileProp.getName());
            prop.setValue(profileProp.getValue());
            activation.setProperty(prop);
         }

         ActivationOS profileOs = profileActivation.getOs();
         if (profileOs != null) {
            org.apache.maven.model.ActivationOS os = new org.apache.maven.model.ActivationOS();
            os.setArch(profileOs.getArch());
            os.setFamily(profileOs.getFamily());
            os.setName(profileOs.getName());
            os.setVersion(profileOs.getVersion());
            activation.setOs(os);
         }

         ActivationFile profileFile = profileActivation.getFile();
         if (profileFile != null) {
            org.apache.maven.model.ActivationFile file = new org.apache.maven.model.ActivationFile();
            file.setExists(profileFile.getExists());
            file.setMissing(profileFile.getMissing());
            activation.setFile(file);
         }

         profile.setActivation(activation);
      }

      profile.setProperties(profileXmlProfile.getProperties());
      List repos = profileXmlProfile.getRepositories();
      if (repos != null) {
         Iterator it = repos.iterator();

         while(it.hasNext()) {
            profile.addRepository(convertFromProfileXmlRepository((Repository)it.next()));
         }
      }

      List pluginRepos = profileXmlProfile.getPluginRepositories();
      if (pluginRepos != null) {
         Iterator it = pluginRepos.iterator();

         while(it.hasNext()) {
            profile.addPluginRepository(convertFromProfileXmlRepository((Repository)it.next()));
         }
      }

      return profile;
   }

   private static org.apache.maven.model.Repository convertFromProfileXmlRepository(Repository profileXmlRepo) {
      org.apache.maven.model.Repository repo = new org.apache.maven.model.Repository();
      repo.setId(profileXmlRepo.getId());
      repo.setLayout(profileXmlRepo.getLayout());
      repo.setName(profileXmlRepo.getName());
      repo.setUrl(profileXmlRepo.getUrl());
      if (profileXmlRepo.getSnapshots() != null) {
         repo.setSnapshots(convertRepositoryPolicy(profileXmlRepo.getSnapshots()));
      }

      if (profileXmlRepo.getReleases() != null) {
         repo.setReleases(convertRepositoryPolicy(profileXmlRepo.getReleases()));
      }

      return repo;
   }

   private static org.apache.maven.model.RepositoryPolicy convertRepositoryPolicy(RepositoryPolicy profileXmlRepo) {
      org.apache.maven.model.RepositoryPolicy policy = new org.apache.maven.model.RepositoryPolicy();
      policy.setEnabled(profileXmlRepo.isEnabled());
      policy.setUpdatePolicy(profileXmlRepo.getUpdatePolicy());
      policy.setChecksumPolicy(profileXmlRepo.getChecksumPolicy());
      return policy;
   }
}
