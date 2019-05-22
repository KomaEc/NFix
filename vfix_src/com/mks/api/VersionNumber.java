package com.mks.api;

public interface VersionNumber {
   int getMajor();

   int getMinor();

   String toVersionString();
}
