package org.apache.maven.profiles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import org.apache.maven.profiles.activation.ProfileActivationException;
import org.apache.maven.profiles.activation.ProfileActivator;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.SettingsUtils;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

public class DefaultProfileManager implements ProfileManager {
   private PlexusContainer container;
   private List activatedIds;
   private List deactivatedIds;
   private List defaultIds;
   private Map profilesById;
   private Properties requestProperties;

   /** @deprecated */
   public DefaultProfileManager(PlexusContainer container) {
      this(container, (Settings)null);
   }

   public DefaultProfileManager(PlexusContainer container, Properties props) {
      this(container, (Settings)null, props);
   }

   /** @deprecated */
   public DefaultProfileManager(PlexusContainer container, Settings settings) {
      this.activatedIds = new ArrayList();
      this.deactivatedIds = new ArrayList();
      this.defaultIds = new ArrayList();
      this.profilesById = new LinkedHashMap();
      this.container = container;
      this.loadSettingsProfiles(settings);
   }

   public DefaultProfileManager(PlexusContainer container, Settings settings, Properties props) {
      this.activatedIds = new ArrayList();
      this.deactivatedIds = new ArrayList();
      this.defaultIds = new ArrayList();
      this.profilesById = new LinkedHashMap();
      this.container = container;
      this.loadSettingsProfiles(settings);
      if (props != null) {
         this.requestProperties = props;
      }

   }

   public Properties getRequestProperties() {
      return this.requestProperties;
   }

   public Map getProfilesById() {
      return this.profilesById;
   }

   public void addProfile(org.apache.maven.model.Profile profile) {
      String profileId = profile.getId();
      org.apache.maven.model.Profile existing = (org.apache.maven.model.Profile)this.profilesById.get(profileId);
      if (existing != null) {
         this.container.getLogger().warn("Overriding profile: '" + profileId + "' (source: " + existing.getSource() + ") with new instance from source: " + profile.getSource());
      }

      this.profilesById.put(profile.getId(), profile);
      org.apache.maven.model.Activation activation = profile.getActivation();
      if (activation != null && activation.isActiveByDefault()) {
         this.activateAsDefault(profileId);
      }

   }

   public void explicitlyActivate(String profileId) {
      if (!this.activatedIds.contains(profileId)) {
         this.container.getLogger().debug("Profile with id: '" + profileId + "' has been explicitly activated.");
         this.activatedIds.add(profileId);
      }

   }

   public void explicitlyActivate(List profileIds) {
      Iterator it = profileIds.iterator();

      while(it.hasNext()) {
         String profileId = (String)it.next();
         this.explicitlyActivate(profileId);
      }

   }

   public void explicitlyDeactivate(String profileId) {
      if (!this.deactivatedIds.contains(profileId)) {
         this.container.getLogger().debug("Profile with id: '" + profileId + "' has been explicitly deactivated.");
         this.deactivatedIds.add(profileId);
      }

   }

   public void explicitlyDeactivate(List profileIds) {
      Iterator it = profileIds.iterator();

      while(it.hasNext()) {
         String profileId = (String)it.next();
         this.explicitlyDeactivate(profileId);
      }

   }

   public List getActiveProfiles() throws ProfileActivationException {
      List activeFromPom = new ArrayList();
      List activeExternal = new ArrayList();
      Iterator it = this.profilesById.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         String profileId = (String)entry.getKey();
         org.apache.maven.model.Profile profile = (org.apache.maven.model.Profile)entry.getValue();
         boolean shouldAdd = false;
         if (this.activatedIds.contains(profileId)) {
            shouldAdd = true;
         } else if (this.isActive(profile)) {
            shouldAdd = true;
         }

         if (!this.deactivatedIds.contains(profileId) && shouldAdd) {
            if ("pom".equals(profile.getSource())) {
               activeFromPom.add(profile);
            } else {
               activeExternal.add(profile);
            }
         }
      }

      if (activeFromPom.isEmpty()) {
         it = this.defaultIds.iterator();

         while(it.hasNext()) {
            String profileId = (String)it.next();
            if (!this.deactivatedIds.contains(profileId)) {
               org.apache.maven.model.Profile profile = (org.apache.maven.model.Profile)this.profilesById.get(profileId);
               activeFromPom.add(profile);
            }
         }
      }

      List allActive = new ArrayList(activeFromPom.size() + activeExternal.size());
      allActive.addAll(activeExternal);
      allActive.addAll(activeFromPom);
      return allActive;
   }

   private boolean isActive(org.apache.maven.model.Profile profile) throws ProfileActivationException {
      List activators = null;
      Properties systemProperties = new Properties(System.getProperties());
      if (this.requestProperties != null) {
         systemProperties.putAll(this.requestProperties);
      }

      this.container.addContextValue("SystemProperties", systemProperties);

      boolean var6;
      try {
         activators = this.container.lookupList(ProfileActivator.ROLE);
         Iterator activatorIterator = activators.iterator();

         ProfileActivator activator;
         do {
            if (!activatorIterator.hasNext()) {
               boolean var18 = false;
               return var18;
            }

            activator = (ProfileActivator)activatorIterator.next();
         } while(!activator.canDetermineActivation(profile) || !activator.isActive(profile));

         var6 = true;
      } catch (ComponentLookupException var16) {
         throw new ProfileActivationException("Cannot retrieve list of profile activators.", var16);
      } finally {
         this.container.getContext().put("SystemProperties", (Object)null);
         if (activators != null) {
            try {
               this.container.releaseAll(activators);
            } catch (ComponentLifecycleException var15) {
               this.container.getLogger().debug("Error releasing profile activators - ignoring.", var15);
            }
         }

      }

      return var6;
   }

   public void addProfiles(List profiles) {
      Iterator it = profiles.iterator();

      while(it.hasNext()) {
         org.apache.maven.model.Profile profile = (org.apache.maven.model.Profile)it.next();
         this.addProfile(profile);
      }

   }

   public void activateAsDefault(String profileId) {
      if (!this.defaultIds.contains(profileId)) {
         this.defaultIds.add(profileId);
      }

   }

   public List getExplicitlyActivatedIds() {
      return this.activatedIds;
   }

   public List getExplicitlyDeactivatedIds() {
      return this.deactivatedIds;
   }

   public List getIdsActivatedByDefault() {
      return this.defaultIds;
   }

   public void loadSettingsProfiles(Settings settings) {
      if (settings != null) {
         List settingsProfiles = settings.getProfiles();
         List settingsActiveProfileIds = settings.getActiveProfiles();
         this.explicitlyActivate(settingsActiveProfileIds);
         if (settingsProfiles != null && !settingsProfiles.isEmpty()) {
            Iterator it = settings.getProfiles().iterator();

            while(it.hasNext()) {
               org.apache.maven.settings.Profile rawProfile = (org.apache.maven.settings.Profile)it.next();
               org.apache.maven.model.Profile profile = SettingsUtils.convertFromSettingsProfile(rawProfile);
               this.addProfile(profile);
            }
         }

      }
   }
}
