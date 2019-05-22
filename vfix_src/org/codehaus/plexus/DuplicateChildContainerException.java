package org.codehaus.plexus;

public class DuplicateChildContainerException extends PlexusContainerException {
   private final String parent;
   private final String child;

   public DuplicateChildContainerException(String parent, String child) {
      super("Cannot create child container, because child named '" + child + "' already exists in parent '" + parent + "'.");
      this.parent = parent;
      this.child = child;
   }

   public String getParent() {
      return this.parent;
   }

   public String getChild() {
      return this.child;
   }
}
