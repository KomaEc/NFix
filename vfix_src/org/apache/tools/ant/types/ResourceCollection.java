package org.apache.tools.ant.types;

import java.util.Iterator;

public interface ResourceCollection {
   Iterator iterator();

   int size();

   boolean isFilesystemOnly();
}
