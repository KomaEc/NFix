package com.google.inject;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.inject.internal.Errors;
import com.google.inject.spi.Message;
import java.util.Collection;

public class CreationException extends RuntimeException {
   private final ImmutableSet<Message> messages;
   private static final long serialVersionUID = 0L;

   public CreationException(Collection<Message> messages) {
      this.messages = ImmutableSet.copyOf(messages);
      Preconditions.checkArgument(!this.messages.isEmpty());
      this.initCause(Errors.getOnlyCause(this.messages));
   }

   public Collection<Message> getErrorMessages() {
      return this.messages;
   }

   public String getMessage() {
      return Errors.format("Unable to create injector, see the following errors", (Collection)this.messages);
   }
}
