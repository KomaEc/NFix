package org.apache.maven.surefire.booter;

import org.apache.maven.surefire.testset.TestRequest;

interface TestRequestAware {
   void setTestRequest(TestRequest var1);
}
