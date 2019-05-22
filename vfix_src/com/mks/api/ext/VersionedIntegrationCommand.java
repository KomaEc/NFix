package com.mks.api.ext;

import com.mks.api.VersionNumber;

public interface VersionedIntegrationCommand extends IntegrationCommand {
   VersionNumber getAPIExecutionVersion();
}
