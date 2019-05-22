package org.apache.maven.plugin.surefire;

interface ConfigurableProviderInfo extends ProviderInfo {
   ProviderInfo instantiate(String var1);
}
