package org.testng.reporters.jq;

import org.testng.ISuite;

public interface INavigatorPanel extends IPanel {
   String getPanelName(ISuite var1);

   String getNavigatorLink(ISuite var1);

   String getClassName();

   String getPrefix();
}
