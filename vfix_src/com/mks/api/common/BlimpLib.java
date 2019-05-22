package com.mks.api.common;

public class BlimpLib {
   public static final int CMD_GET_STATUS = 0;
   public static final int CMD_GET_ENV = 1;
   public static final int CMD_GET_ARGS = 2;
   public static final int CMD_GET_CWD = 3;
   public static final int CMD_WRITE_STDOUT = 4;
   public static final int CMD_WRITE_STDERR = 5;
   public static final int CMD_READ_LINE = 6;
   public static final int CMD_READ_MULTILINE = 7;
   public static final int CMD_READ_SECRET = 8;
   public static final int CMD_EXIT = 9;
   public static final int CMD_INTR_RESET = 10;
   public static final int CMD_WRITE_STDOUT_NL = 11;
   public static final int CMD_WRITE_STDERR_NL = 12;
   public static final int CMD_EXEC = 13;
   public static final int CMD_READ_FILE = 14;
   public static final int CMD_WRITE_FILE = 15;
   public static final int CMD_GET_CWD_AND_ARGS = 16;
   public static final byte RESPONSE_OK = 0;
   public static final byte RESPONSE_CTRL_BREAK = 1;
   public static final byte RESPONSE_FILE_NOT_EXIST = 2;
   public static final byte RESPONSE_NO_PERMISSION = 3;
   public static final String PRE_9_6_PROTOCOL_VERSION = "1.1";
   public static final String PROTOCOL_VERSION = "1.2";
   public static final String[] SUPPORTED_PROTOCOLS = new String[]{"1.1", "1.2"};
   public static final String SERVLET_URI = "/icapi";
   public static final String OUT_OF_BAND_MESSAGE_HEADER = "OutOfBandMessage";
   public static final String PROTOCOL_VERSION_HEADER = "Protocol-version";
   public static final String SESSION_RELEASE_HEADER = "AppConnection";
   public static final String CODEPAGE_HEADER = "CodePage";
   public static final String CHARACTERWIDTH_HEADER = "CharacterWidth";
   public static final String NEW_SESSION_HEADER = "AppSession";
   public static final String THREADID_HEADER = "ThreadID";
   public static final String SADCOOKIE_HEADER = "SadCookie";
   public static final String ADVANCED_FEATURES_HEADER = "enableAdvancedFeatures";
   public static final String API_VERSION_HEADER = "APIVersion";
   public static final String API_INVOCATION_VENDOR = "APIVendor";
   public static final String API_TIMEZONE = "TimeZone";
   public static final int MAX_REQUEST_LENGTH = 16777215;
   public static final String MKS_APP_PARAMETER = "mksApp";
   public static final String CONTENT_TYPE = "application/x-mks-blimp";

   public static boolean isProtocolVersionSupported(String version) {
      for(int i = 0; i < SUPPORTED_PROTOCOLS.length; ++i) {
         if (SUPPORTED_PROTOCOLS[i].equals(version.trim())) {
            return true;
         }
      }

      return false;
   }

   public static boolean isCurrentProtocol(String version) {
      return "1.2".equals(version) || version == null;
   }
}
