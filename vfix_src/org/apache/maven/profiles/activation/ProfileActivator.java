package org.apache.maven.profiles.activation;

import org.apache.maven.model.Profile;

public interface ProfileActivator {
   String ROLE = ProfileActivator.class.getName();

   boolean canDetermineActivation(Profile var1);

   boolean isActive(Profile var1) throws ProfileActivationException;
}
