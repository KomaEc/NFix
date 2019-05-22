package com.mks.api;

import java.io.File;

public class FileOption extends Option {
   private static final String REMOTE_PREFIX = "remote://";

   public FileOption(String name, File value) {
      super(name, "remote://" + value.getPath());
   }

   public FileOption(String name, String value) {
      super(name, "remote://" + value);
   }
}
