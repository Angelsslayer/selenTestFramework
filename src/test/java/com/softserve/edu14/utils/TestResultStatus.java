package com.softserve.edu14.utils;

import com.softserve.edu14.test.TestsRunner;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestResultStatus implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        TestsRunner.isTestFailed.set(context.getExecutionException().isPresent());
    }

}