package org.apache.maven.profiles.activation;

import org.apache.maven.model.Profile;
import org.apache.maven.profiles.AlwaysOnActivation;

public class AlwaysOnProfileActivator implements ProfileActivator {
   public boolean canDetermineActivation(Profile profile) {
      return profile.getActivation() != null && profile.getActivation() instanceof AlwaysOnActivation;
   }

   public boolean isActive(Profile profile) {
      return true;
   }
}
