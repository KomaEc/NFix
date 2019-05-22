package com.mks.api.ext;

import java.util.Iterator;

public interface CommandOptions {
   Iterator options();

   boolean isOptionSet(String var1);

   String getOptionValue(String var1);
}
