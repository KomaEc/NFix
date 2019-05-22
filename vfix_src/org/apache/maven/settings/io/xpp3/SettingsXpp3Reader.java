package org.apache.maven.settings.io.xpp3;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.apache.maven.settings.Activation;
import org.apache.maven.settings.ActivationFile;
import org.apache.maven.settings.ActivationOS;
import org.apache.maven.settings.ActivationProperty;
import org.apache.maven.settings.IdentifiableBase;
import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Profile;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Repository;
import org.apache.maven.settings.RepositoryBase;
import org.apache.maven.settings.RepositoryPolicy;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.TrackableBase;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.MXParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class SettingsXpp3Reader {
   private boolean addDefaultEntities = true;

   private boolean checkFieldWithDuplicate(XmlPullParser parser, String tagName, String alias, Set parsed) throws XmlPullParserException {
      if (!parser.getName().equals(tagName) && !parser.getName().equals(alias)) {
         return false;
      } else if (parsed.contains(tagName)) {
         throw new XmlPullParserException("Duplicated tag: '" + tagName + "'", parser, (Throwable)null);
      } else {
         parsed.add(tagName);
         return true;
      }
   }

   public boolean getAddDefaultEntities() {
      return this.addDefaultEntities;
   }

   private boolean getBooleanValue(String s, String attribute, XmlPullParser parser) throws XmlPullParserException {
      return this.getBooleanValue(s, attribute, parser, (String)null);
   }

   private boolean getBooleanValue(String s, String attribute, XmlPullParser parser, String defaultValue) throws XmlPullParserException {
      if (s != null && s.length() != 0) {
         return Boolean.valueOf(s);
      } else {
         return defaultValue != null ? Boolean.valueOf(defaultValue) : false;
      }
   }

   private byte getByteValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s != null) {
         try {
            return Byte.valueOf(s);
         } catch (NumberFormatException var6) {
            if (strict) {
               throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a byte", parser, (Throwable)null);
            }
         }
      }

      return 0;
   }

   private char getCharacterValue(String s, String attribute, XmlPullParser parser) throws XmlPullParserException {
      return s != null ? s.charAt(0) : '\u0000';
   }

   private Date getDateValue(String s, String attribute, XmlPullParser parser) throws XmlPullParserException {
      return this.getDateValue(s, attribute, (String)null, parser);
   }

   private Date getDateValue(String s, String attribute, String dateFormat, XmlPullParser parser) throws XmlPullParserException {
      if (s != null) {
         String effectiveDateFormat = dateFormat;
         if (dateFormat == null) {
            effectiveDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
         }

         if ("long".equals(effectiveDateFormat)) {
            try {
               return new Date(Long.parseLong(s));
            } catch (NumberFormatException var7) {
               throw new XmlPullParserException(var7.getMessage());
            }
         } else {
            try {
               DateFormat dateParser = new SimpleDateFormat(effectiveDateFormat, Locale.US);
               return dateParser.parse(s);
            } catch (ParseException var8) {
               throw new XmlPullParserException(var8.getMessage());
            }
         }
      } else {
         return null;
      }
   }

   private double getDoubleValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s != null) {
         try {
            return Double.valueOf(s);
         } catch (NumberFormatException var6) {
            if (strict) {
               throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a floating point number", parser, (Throwable)null);
            }
         }
      }

      return 0.0D;
   }

   private float getFloatValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s != null) {
         try {
            return Float.valueOf(s);
         } catch (NumberFormatException var6) {
            if (strict) {
               throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a floating point number", parser, (Throwable)null);
            }
         }
      }

      return 0.0F;
   }

   private int getIntegerValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s != null) {
         try {
            return Integer.valueOf(s);
         } catch (NumberFormatException var6) {
            if (strict) {
               throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be an integer", parser, (Throwable)null);
            }
         }
      }

      return 0;
   }

   private long getLongValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s != null) {
         try {
            return Long.valueOf(s);
         } catch (NumberFormatException var6) {
            if (strict) {
               throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a long integer", parser, (Throwable)null);
            }
         }
      }

      return 0L;
   }

   private String getRequiredAttributeValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s == null && strict) {
         throw new XmlPullParserException("Missing required value for attribute '" + attribute + "'", parser, (Throwable)null);
      } else {
         return s;
      }
   }

   private short getShortValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s != null) {
         try {
            return Short.valueOf(s);
         } catch (NumberFormatException var6) {
            if (strict) {
               throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a short integer", parser, (Throwable)null);
            }
         }
      }

      return 0;
   }

   private String getTrimmedValue(String s) {
      if (s != null) {
         s = s.trim();
      }

      return s;
   }

   private Activation parseActivation(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Activation activation = new Activation();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "activeByDefault", (String)null, parsed)) {
               activation.setActiveByDefault(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "activeByDefault", parser, "false"));
            } else if (this.checkFieldWithDuplicate(parser, "jdk", (String)null, parsed)) {
               activation.setJdk(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "os", (String)null, parsed)) {
               activation.setOs(this.parseActivationOS("os", parser, strict));
            } else if (this.checkFieldWithDuplicate(parser, "property", (String)null, parsed)) {
               activation.setProperty(this.parseActivationProperty("property", parser, strict));
            } else if (this.checkFieldWithDuplicate(parser, "file", (String)null, parsed)) {
               activation.setFile(this.parseActivationFile("file", parser, strict));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return activation;
      }
   }

   private ActivationFile parseActivationFile(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      ActivationFile activationFile = new ActivationFile();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "missing", (String)null, parsed)) {
               activationFile.setMissing(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "exists", (String)null, parsed)) {
               activationFile.setExists(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return activationFile;
      }
   }

   private ActivationOS parseActivationOS(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      ActivationOS activationOS = new ActivationOS();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "name", (String)null, parsed)) {
               activationOS.setName(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "family", (String)null, parsed)) {
               activationOS.setFamily(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "arch", (String)null, parsed)) {
               activationOS.setArch(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "version", (String)null, parsed)) {
               activationOS.setVersion(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return activationOS;
      }
   }

   private ActivationProperty parseActivationProperty(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      ActivationProperty activationProperty = new ActivationProperty();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "name", (String)null, parsed)) {
               activationProperty.setName(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "value", (String)null, parsed)) {
               activationProperty.setValue(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return activationProperty;
      }
   }

   private IdentifiableBase parseIdentifiableBase(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      IdentifiableBase identifiableBase = new IdentifiableBase();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "id", (String)null, parsed)) {
               identifiableBase.setId(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return identifiableBase;
      }
   }

   private Mirror parseMirror(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Mirror mirror = new Mirror();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "mirrorOf", (String)null, parsed)) {
               mirror.setMirrorOf(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "name", (String)null, parsed)) {
               mirror.setName(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "url", (String)null, parsed)) {
               mirror.setUrl(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "id", (String)null, parsed)) {
               mirror.setId(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return mirror;
      }
   }

   private Profile parseProfile(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Profile profile = new Profile();
      HashSet parsed = new HashSet();

      while(true) {
         label84:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "activation", (String)null, parsed)) {
               profile.setActivation(this.parseActivation("activation", parser, strict));
            } else if (this.checkFieldWithDuplicate(parser, "properties", (String)null, parsed)) {
               while(parser.nextTag() == 2) {
                  String key = parser.getName();
                  String value = parser.nextText().trim();
                  profile.addProperty(key, value);
               }
            } else {
               ArrayList pluginRepositories;
               if (this.checkFieldWithDuplicate(parser, "repositories", (String)null, parsed)) {
                  pluginRepositories = new ArrayList();
                  profile.setRepositories(pluginRepositories);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label84;
                        }

                        if (parser.getName().equals("repository")) {
                           pluginRepositories.add(this.parseRepository("repository", parser, strict));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               } else if (!this.checkFieldWithDuplicate(parser, "pluginRepositories", (String)null, parsed)) {
                  if (this.checkFieldWithDuplicate(parser, "id", (String)null, parsed)) {
                     profile.setId(this.getTrimmedValue(parser.nextText()));
                  } else {
                     if (strict) {
                        throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
                     }

                     while(parser.next() != 3) {
                     }
                  }
               } else {
                  pluginRepositories = new ArrayList();
                  profile.setPluginRepositories(pluginRepositories);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label84;
                        }

                        if (parser.getName().equals("pluginRepository")) {
                           pluginRepositories.add(this.parseRepository("pluginRepository", parser, strict));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               }
            }
         }

         return profile;
      }
   }

   private Proxy parseProxy(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Proxy proxy = new Proxy();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "active", (String)null, parsed)) {
               proxy.setActive(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "active", parser, "true"));
            } else if (this.checkFieldWithDuplicate(parser, "protocol", (String)null, parsed)) {
               proxy.setProtocol(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "username", (String)null, parsed)) {
               proxy.setUsername(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "password", (String)null, parsed)) {
               proxy.setPassword(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "port", (String)null, parsed)) {
               proxy.setPort(this.getIntegerValue(this.getTrimmedValue(parser.nextText()), "port", parser, strict));
            } else if (this.checkFieldWithDuplicate(parser, "host", (String)null, parsed)) {
               proxy.setHost(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "nonProxyHosts", (String)null, parsed)) {
               proxy.setNonProxyHosts(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "id", (String)null, parsed)) {
               proxy.setId(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return proxy;
      }
   }

   private Repository parseRepository(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Repository repository = new Repository();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "releases", (String)null, parsed)) {
               repository.setReleases(this.parseRepositoryPolicy("releases", parser, strict));
            } else if (this.checkFieldWithDuplicate(parser, "snapshots", (String)null, parsed)) {
               repository.setSnapshots(this.parseRepositoryPolicy("snapshots", parser, strict));
            } else if (this.checkFieldWithDuplicate(parser, "id", (String)null, parsed)) {
               repository.setId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "name", (String)null, parsed)) {
               repository.setName(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "url", (String)null, parsed)) {
               repository.setUrl(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "layout", (String)null, parsed)) {
               repository.setLayout(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return repository;
      }
   }

   private RepositoryBase parseRepositoryBase(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      RepositoryBase repositoryBase = new RepositoryBase();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "id", (String)null, parsed)) {
               repositoryBase.setId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "name", (String)null, parsed)) {
               repositoryBase.setName(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "url", (String)null, parsed)) {
               repositoryBase.setUrl(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "layout", (String)null, parsed)) {
               repositoryBase.setLayout(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return repositoryBase;
      }
   }

   private RepositoryPolicy parseRepositoryPolicy(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      RepositoryPolicy repositoryPolicy = new RepositoryPolicy();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "enabled", (String)null, parsed)) {
               repositoryPolicy.setEnabled(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "enabled", parser, "true"));
            } else if (this.checkFieldWithDuplicate(parser, "updatePolicy", (String)null, parsed)) {
               repositoryPolicy.setUpdatePolicy(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "checksumPolicy", (String)null, parsed)) {
               repositoryPolicy.setChecksumPolicy(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return repositoryPolicy;
      }
   }

   private Server parseServer(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Server server = new Server();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "username", (String)null, parsed)) {
               server.setUsername(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "password", (String)null, parsed)) {
               server.setPassword(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "privateKey", (String)null, parsed)) {
               server.setPrivateKey(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "passphrase", (String)null, parsed)) {
               server.setPassphrase(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "filePermissions", (String)null, parsed)) {
               server.setFilePermissions(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "directoryPermissions", (String)null, parsed)) {
               server.setDirectoryPermissions(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "configuration", (String)null, parsed)) {
               server.setConfiguration(Xpp3DomBuilder.build(parser));
            } else if (this.checkFieldWithDuplicate(parser, "id", (String)null, parsed)) {
               server.setId(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return server;
      }
   }

   private Settings parseSettings(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Settings settings = new Settings();
      Set parsed = new HashSet();
      int eventType = parser.getEventType();

      label194:
      for(boolean foundRoot = false; eventType != 1; eventType = parser.next()) {
         if (eventType == 2) {
            if (parser.getName().equals(tagName)) {
               foundRoot = true;
            } else {
               if (strict && !foundRoot) {
                  throw new XmlPullParserException("Expected root element '" + tagName + "' but found '" + parser.getName() + "'", parser, (Throwable)null);
               }

               if (this.checkFieldWithDuplicate(parser, "localRepository", (String)null, parsed)) {
                  settings.setLocalRepository(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "interactiveMode", (String)null, parsed)) {
                  settings.setInteractiveMode(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "interactiveMode", parser, "true"));
               } else if (this.checkFieldWithDuplicate(parser, "usePluginRegistry", (String)null, parsed)) {
                  settings.setUsePluginRegistry(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "usePluginRegistry", parser, "false"));
               } else if (this.checkFieldWithDuplicate(parser, "offline", (String)null, parsed)) {
                  settings.setOffline(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "offline", parser, "false"));
               } else {
                  ArrayList pluginGroups;
                  if (this.checkFieldWithDuplicate(parser, "proxies", (String)null, parsed)) {
                     pluginGroups = new ArrayList();
                     settings.setProxies(pluginGroups);

                     while(true) {
                        while(true) {
                           if (parser.nextTag() != 2) {
                              continue label194;
                           }

                           if (parser.getName().equals("proxy")) {
                              pluginGroups.add(this.parseProxy("proxy", parser, strict));
                           } else {
                              if (strict) {
                                 throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                              }

                              while(parser.next() != 3) {
                              }
                           }
                        }
                     }
                  } else if (this.checkFieldWithDuplicate(parser, "servers", (String)null, parsed)) {
                     pluginGroups = new ArrayList();
                     settings.setServers(pluginGroups);

                     while(true) {
                        while(true) {
                           if (parser.nextTag() != 2) {
                              continue label194;
                           }

                           if (parser.getName().equals("server")) {
                              pluginGroups.add(this.parseServer("server", parser, strict));
                           } else {
                              if (strict) {
                                 throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                              }

                              while(parser.next() != 3) {
                              }
                           }
                        }
                     }
                  } else if (this.checkFieldWithDuplicate(parser, "mirrors", (String)null, parsed)) {
                     pluginGroups = new ArrayList();
                     settings.setMirrors(pluginGroups);

                     while(true) {
                        while(true) {
                           if (parser.nextTag() != 2) {
                              continue label194;
                           }

                           if (parser.getName().equals("mirror")) {
                              pluginGroups.add(this.parseMirror("mirror", parser, strict));
                           } else {
                              if (strict) {
                                 throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                              }

                              while(parser.next() != 3) {
                              }
                           }
                        }
                     }
                  } else if (this.checkFieldWithDuplicate(parser, "profiles", (String)null, parsed)) {
                     pluginGroups = new ArrayList();
                     settings.setProfiles(pluginGroups);

                     while(true) {
                        while(true) {
                           if (parser.nextTag() != 2) {
                              continue label194;
                           }

                           if (parser.getName().equals("profile")) {
                              pluginGroups.add(this.parseProfile("profile", parser, strict));
                           } else {
                              if (strict) {
                                 throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                              }

                              while(parser.next() != 3) {
                              }
                           }
                        }
                     }
                  } else if (this.checkFieldWithDuplicate(parser, "activeProfiles", (String)null, parsed)) {
                     pluginGroups = new ArrayList();
                     settings.setActiveProfiles(pluginGroups);

                     while(true) {
                        while(true) {
                           if (parser.nextTag() != 2) {
                              continue label194;
                           }

                           if (parser.getName().equals("activeProfile")) {
                              pluginGroups.add(this.getTrimmedValue(parser.nextText()));
                           } else {
                              if (strict) {
                                 throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                              }

                              while(parser.next() != 3) {
                              }
                           }
                        }
                     }
                  } else if (!this.checkFieldWithDuplicate(parser, "pluginGroups", (String)null, parsed)) {
                     if (strict) {
                        throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
                     }
                  } else {
                     pluginGroups = new ArrayList();
                     settings.setPluginGroups(pluginGroups);

                     while(true) {
                        while(true) {
                           if (parser.nextTag() != 2) {
                              continue label194;
                           }

                           if (parser.getName().equals("pluginGroup")) {
                              pluginGroups.add(this.getTrimmedValue(parser.nextText()));
                           } else {
                              if (strict) {
                                 throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                              }

                              while(parser.next() != 3) {
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return settings;
   }

   private TrackableBase parseTrackableBase(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      TrackableBase trackableBase = new TrackableBase();
      new HashSet();

      while(parser.nextTag() == 2) {
         if (strict) {
            throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
         }

         while(true) {
            if (parser.next() != 3) {
               continue;
            }
         }
      }

      return trackableBase;
   }

   public Settings read(Reader reader, boolean strict) throws IOException, XmlPullParserException {
      XmlPullParser parser = new MXParser();
      parser.setInput(reader);
      if (this.addDefaultEntities) {
         parser.defineEntityReplacementText("nbsp", " ");
         parser.defineEntityReplacementText("iexcl", "¡");
         parser.defineEntityReplacementText("cent", "¢");
         parser.defineEntityReplacementText("pound", "£");
         parser.defineEntityReplacementText("curren", "¤");
         parser.defineEntityReplacementText("yen", "¥");
         parser.defineEntityReplacementText("brvbar", "¦");
         parser.defineEntityReplacementText("sect", "§");
         parser.defineEntityReplacementText("uml", "¨");
         parser.defineEntityReplacementText("copy", "©");
         parser.defineEntityReplacementText("ordf", "ª");
         parser.defineEntityReplacementText("laquo", "«");
         parser.defineEntityReplacementText("not", "¬");
         parser.defineEntityReplacementText("shy", "\u00ad");
         parser.defineEntityReplacementText("reg", "®");
         parser.defineEntityReplacementText("macr", "¯");
         parser.defineEntityReplacementText("deg", "°");
         parser.defineEntityReplacementText("plusmn", "±");
         parser.defineEntityReplacementText("sup2", "²");
         parser.defineEntityReplacementText("sup3", "³");
         parser.defineEntityReplacementText("acute", "´");
         parser.defineEntityReplacementText("micro", "µ");
         parser.defineEntityReplacementText("para", "¶");
         parser.defineEntityReplacementText("middot", "·");
         parser.defineEntityReplacementText("cedil", "¸");
         parser.defineEntityReplacementText("sup1", "¹");
         parser.defineEntityReplacementText("ordm", "º");
         parser.defineEntityReplacementText("raquo", "»");
         parser.defineEntityReplacementText("frac14", "¼");
         parser.defineEntityReplacementText("frac12", "½");
         parser.defineEntityReplacementText("frac34", "¾");
         parser.defineEntityReplacementText("iquest", "¿");
         parser.defineEntityReplacementText("Agrave", "À");
         parser.defineEntityReplacementText("Aacute", "Á");
         parser.defineEntityReplacementText("Acirc", "Â");
         parser.defineEntityReplacementText("Atilde", "Ã");
         parser.defineEntityReplacementText("Auml", "Ä");
         parser.defineEntityReplacementText("Aring", "Å");
         parser.defineEntityReplacementText("AElig", "Æ");
         parser.defineEntityReplacementText("Ccedil", "Ç");
         parser.defineEntityReplacementText("Egrave", "È");
         parser.defineEntityReplacementText("Eacute", "É");
         parser.defineEntityReplacementText("Ecirc", "Ê");
         parser.defineEntityReplacementText("Euml", "Ë");
         parser.defineEntityReplacementText("Igrave", "Ì");
         parser.defineEntityReplacementText("Iacute", "Í");
         parser.defineEntityReplacementText("Icirc", "Î");
         parser.defineEntityReplacementText("Iuml", "Ï");
         parser.defineEntityReplacementText("ETH", "Ð");
         parser.defineEntityReplacementText("Ntilde", "Ñ");
         parser.defineEntityReplacementText("Ograve", "Ò");
         parser.defineEntityReplacementText("Oacute", "Ó");
         parser.defineEntityReplacementText("Ocirc", "Ô");
         parser.defineEntityReplacementText("Otilde", "Õ");
         parser.defineEntityReplacementText("Ouml", "Ö");
         parser.defineEntityReplacementText("times", "×");
         parser.defineEntityReplacementText("Oslash", "Ø");
         parser.defineEntityReplacementText("Ugrave", "Ù");
         parser.defineEntityReplacementText("Uacute", "Ú");
         parser.defineEntityReplacementText("Ucirc", "Û");
         parser.defineEntityReplacementText("Uuml", "Ü");
         parser.defineEntityReplacementText("Yacute", "Ý");
         parser.defineEntityReplacementText("THORN", "Þ");
         parser.defineEntityReplacementText("szlig", "ß");
         parser.defineEntityReplacementText("agrave", "à");
         parser.defineEntityReplacementText("aacute", "á");
         parser.defineEntityReplacementText("acirc", "â");
         parser.defineEntityReplacementText("atilde", "ã");
         parser.defineEntityReplacementText("auml", "ä");
         parser.defineEntityReplacementText("aring", "å");
         parser.defineEntityReplacementText("aelig", "æ");
         parser.defineEntityReplacementText("ccedil", "ç");
         parser.defineEntityReplacementText("egrave", "è");
         parser.defineEntityReplacementText("eacute", "é");
         parser.defineEntityReplacementText("ecirc", "ê");
         parser.defineEntityReplacementText("euml", "ë");
         parser.defineEntityReplacementText("igrave", "ì");
         parser.defineEntityReplacementText("iacute", "í");
         parser.defineEntityReplacementText("icirc", "î");
         parser.defineEntityReplacementText("iuml", "ï");
         parser.defineEntityReplacementText("eth", "ð");
         parser.defineEntityReplacementText("ntilde", "ñ");
         parser.defineEntityReplacementText("ograve", "ò");
         parser.defineEntityReplacementText("oacute", "ó");
         parser.defineEntityReplacementText("ocirc", "ô");
         parser.defineEntityReplacementText("otilde", "õ");
         parser.defineEntityReplacementText("ouml", "ö");
         parser.defineEntityReplacementText("divide", "÷");
         parser.defineEntityReplacementText("oslash", "ø");
         parser.defineEntityReplacementText("ugrave", "ù");
         parser.defineEntityReplacementText("uacute", "ú");
         parser.defineEntityReplacementText("ucirc", "û");
         parser.defineEntityReplacementText("uuml", "ü");
         parser.defineEntityReplacementText("yacute", "ý");
         parser.defineEntityReplacementText("thorn", "þ");
         parser.defineEntityReplacementText("yuml", "ÿ");
         parser.defineEntityReplacementText("OElig", "Œ");
         parser.defineEntityReplacementText("oelig", "œ");
         parser.defineEntityReplacementText("Scaron", "Š");
         parser.defineEntityReplacementText("scaron", "š");
         parser.defineEntityReplacementText("Yuml", "Ÿ");
         parser.defineEntityReplacementText("circ", "ˆ");
         parser.defineEntityReplacementText("tilde", "˜");
         parser.defineEntityReplacementText("ensp", " ");
         parser.defineEntityReplacementText("emsp", " ");
         parser.defineEntityReplacementText("thinsp", " ");
         parser.defineEntityReplacementText("zwnj", "\u200c");
         parser.defineEntityReplacementText("zwj", "\u200d");
         parser.defineEntityReplacementText("lrm", "\u200e");
         parser.defineEntityReplacementText("rlm", "\u200f");
         parser.defineEntityReplacementText("ndash", "–");
         parser.defineEntityReplacementText("mdash", "—");
         parser.defineEntityReplacementText("lsquo", "‘");
         parser.defineEntityReplacementText("rsquo", "’");
         parser.defineEntityReplacementText("sbquo", "‚");
         parser.defineEntityReplacementText("ldquo", "“");
         parser.defineEntityReplacementText("rdquo", "”");
         parser.defineEntityReplacementText("bdquo", "„");
         parser.defineEntityReplacementText("dagger", "†");
         parser.defineEntityReplacementText("Dagger", "‡");
         parser.defineEntityReplacementText("permil", "‰");
         parser.defineEntityReplacementText("lsaquo", "‹");
         parser.defineEntityReplacementText("rsaquo", "›");
         parser.defineEntityReplacementText("euro", "€");
         parser.defineEntityReplacementText("fnof", "ƒ");
         parser.defineEntityReplacementText("Alpha", "Α");
         parser.defineEntityReplacementText("Beta", "Β");
         parser.defineEntityReplacementText("Gamma", "Γ");
         parser.defineEntityReplacementText("Delta", "Δ");
         parser.defineEntityReplacementText("Epsilon", "Ε");
         parser.defineEntityReplacementText("Zeta", "Ζ");
         parser.defineEntityReplacementText("Eta", "Η");
         parser.defineEntityReplacementText("Theta", "Θ");
         parser.defineEntityReplacementText("Iota", "Ι");
         parser.defineEntityReplacementText("Kappa", "Κ");
         parser.defineEntityReplacementText("Lambda", "Λ");
         parser.defineEntityReplacementText("Mu", "Μ");
         parser.defineEntityReplacementText("Nu", "Ν");
         parser.defineEntityReplacementText("Xi", "Ξ");
         parser.defineEntityReplacementText("Omicron", "Ο");
         parser.defineEntityReplacementText("Pi", "Π");
         parser.defineEntityReplacementText("Rho", "Ρ");
         parser.defineEntityReplacementText("Sigma", "Σ");
         parser.defineEntityReplacementText("Tau", "Τ");
         parser.defineEntityReplacementText("Upsilon", "Υ");
         parser.defineEntityReplacementText("Phi", "Φ");
         parser.defineEntityReplacementText("Chi", "Χ");
         parser.defineEntityReplacementText("Psi", "Ψ");
         parser.defineEntityReplacementText("Omega", "Ω");
         parser.defineEntityReplacementText("alpha", "α");
         parser.defineEntityReplacementText("beta", "β");
         parser.defineEntityReplacementText("gamma", "γ");
         parser.defineEntityReplacementText("delta", "δ");
         parser.defineEntityReplacementText("epsilon", "ε");
         parser.defineEntityReplacementText("zeta", "ζ");
         parser.defineEntityReplacementText("eta", "η");
         parser.defineEntityReplacementText("theta", "θ");
         parser.defineEntityReplacementText("iota", "ι");
         parser.defineEntityReplacementText("kappa", "κ");
         parser.defineEntityReplacementText("lambda", "λ");
         parser.defineEntityReplacementText("mu", "μ");
         parser.defineEntityReplacementText("nu", "ν");
         parser.defineEntityReplacementText("xi", "ξ");
         parser.defineEntityReplacementText("omicron", "ο");
         parser.defineEntityReplacementText("pi", "π");
         parser.defineEntityReplacementText("rho", "ρ");
         parser.defineEntityReplacementText("sigmaf", "ς");
         parser.defineEntityReplacementText("sigma", "σ");
         parser.defineEntityReplacementText("tau", "τ");
         parser.defineEntityReplacementText("upsilon", "υ");
         parser.defineEntityReplacementText("phi", "φ");
         parser.defineEntityReplacementText("chi", "χ");
         parser.defineEntityReplacementText("psi", "ψ");
         parser.defineEntityReplacementText("omega", "ω");
         parser.defineEntityReplacementText("thetasym", "ϑ");
         parser.defineEntityReplacementText("upsih", "ϒ");
         parser.defineEntityReplacementText("piv", "ϖ");
         parser.defineEntityReplacementText("bull", "•");
         parser.defineEntityReplacementText("hellip", "…");
         parser.defineEntityReplacementText("prime", "′");
         parser.defineEntityReplacementText("Prime", "″");
         parser.defineEntityReplacementText("oline", "‾");
         parser.defineEntityReplacementText("frasl", "⁄");
         parser.defineEntityReplacementText("weierp", "℘");
         parser.defineEntityReplacementText("image", "ℑ");
         parser.defineEntityReplacementText("real", "ℜ");
         parser.defineEntityReplacementText("trade", "™");
         parser.defineEntityReplacementText("alefsym", "ℵ");
         parser.defineEntityReplacementText("larr", "←");
         parser.defineEntityReplacementText("uarr", "↑");
         parser.defineEntityReplacementText("rarr", "→");
         parser.defineEntityReplacementText("darr", "↓");
         parser.defineEntityReplacementText("harr", "↔");
         parser.defineEntityReplacementText("crarr", "↵");
         parser.defineEntityReplacementText("lArr", "⇐");
         parser.defineEntityReplacementText("uArr", "⇑");
         parser.defineEntityReplacementText("rArr", "⇒");
         parser.defineEntityReplacementText("dArr", "⇓");
         parser.defineEntityReplacementText("hArr", "⇔");
         parser.defineEntityReplacementText("forall", "∀");
         parser.defineEntityReplacementText("part", "∂");
         parser.defineEntityReplacementText("exist", "∃");
         parser.defineEntityReplacementText("empty", "∅");
         parser.defineEntityReplacementText("nabla", "∇");
         parser.defineEntityReplacementText("isin", "∈");
         parser.defineEntityReplacementText("notin", "∉");
         parser.defineEntityReplacementText("ni", "∋");
         parser.defineEntityReplacementText("prod", "∏");
         parser.defineEntityReplacementText("sum", "∑");
         parser.defineEntityReplacementText("minus", "−");
         parser.defineEntityReplacementText("lowast", "∗");
         parser.defineEntityReplacementText("radic", "√");
         parser.defineEntityReplacementText("prop", "∝");
         parser.defineEntityReplacementText("infin", "∞");
         parser.defineEntityReplacementText("ang", "∠");
         parser.defineEntityReplacementText("and", "∧");
         parser.defineEntityReplacementText("or", "∨");
         parser.defineEntityReplacementText("cap", "∩");
         parser.defineEntityReplacementText("cup", "∪");
         parser.defineEntityReplacementText("int", "∫");
         parser.defineEntityReplacementText("there4", "∴");
         parser.defineEntityReplacementText("sim", "∼");
         parser.defineEntityReplacementText("cong", "≅");
         parser.defineEntityReplacementText("asymp", "≈");
         parser.defineEntityReplacementText("ne", "≠");
         parser.defineEntityReplacementText("equiv", "≡");
         parser.defineEntityReplacementText("le", "≤");
         parser.defineEntityReplacementText("ge", "≥");
         parser.defineEntityReplacementText("sub", "⊂");
         parser.defineEntityReplacementText("sup", "⊃");
         parser.defineEntityReplacementText("nsub", "⊄");
         parser.defineEntityReplacementText("sube", "⊆");
         parser.defineEntityReplacementText("supe", "⊇");
         parser.defineEntityReplacementText("oplus", "⊕");
         parser.defineEntityReplacementText("otimes", "⊗");
         parser.defineEntityReplacementText("perp", "⊥");
         parser.defineEntityReplacementText("sdot", "⋅");
         parser.defineEntityReplacementText("lceil", "⌈");
         parser.defineEntityReplacementText("rceil", "⌉");
         parser.defineEntityReplacementText("lfloor", "⌊");
         parser.defineEntityReplacementText("rfloor", "⌋");
         parser.defineEntityReplacementText("lang", "〈");
         parser.defineEntityReplacementText("rang", "〉");
         parser.defineEntityReplacementText("loz", "◊");
         parser.defineEntityReplacementText("spades", "♠");
         parser.defineEntityReplacementText("clubs", "♣");
         parser.defineEntityReplacementText("hearts", "♥");
         parser.defineEntityReplacementText("diams", "♦");
      }

      parser.next();
      return this.parseSettings("settings", parser, strict);
   }

   public Settings read(Reader reader) throws IOException, XmlPullParserException {
      return this.read(reader, true);
   }

   public Settings read(InputStream in, boolean strict) throws IOException, XmlPullParserException {
      Reader reader = ReaderFactory.newXmlReader(in);
      return this.read((Reader)reader, strict);
   }

   public Settings read(InputStream in) throws IOException, XmlPullParserException {
      Reader reader = ReaderFactory.newXmlReader(in);
      return this.read((Reader)reader);
   }

   public void setAddDefaultEntities(boolean addDefaultEntities) {
      this.addDefaultEntities = addDefaultEntities;
   }
}
