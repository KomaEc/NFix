package org.apache.maven.profiles;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.maven.profiles.activation.ProfileActivationException;
import org.apache.maven.settings.Settings;

public interface ProfileManager {
   void addProfile(org.apache.maven.model.Profile var1);

   void explicitlyActivate(String var1);

   void explicitlyActivate(List var1);

   void explicitlyDeactivate(String var1);

   void explicitlyDeactivate(List var1);

   void activateAsDefault(String var1);

   List getActiveProfiles() throws ProfileActivationException;

   void addProfiles(List var1);

   Map getProfilesById();

   List getExplicitlyActivatedIds();

   List getExplicitlyDeactivatedIds();

   List getIdsActivatedByDefault();

   void loadSettingsProfiles(Settings var1);

   Properties getRequestProperties();
}
