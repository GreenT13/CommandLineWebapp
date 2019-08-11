package util.integrationtesting;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Condition to skip the test if "skipIntegrationTests" is given.
 */
public class IntegrationTestCondition implements ExecutionCondition {
    private static final String COMMAND_LINE_PARAMETER_SKIP_INTEGRATION_TESTS = "skipIntegrationTests";

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        String skipIntegrationTests = getVariable();

        if(skipIntegrationTests != null && skipIntegrationTests.equalsIgnoreCase("true")) {
            return ConditionEvaluationResult.disabled("Integration test disabled.");
        } else {
            return ConditionEvaluationResult.enabled("Integration test enabled");
        }
    }

    private String getVariable() {
        String skipIntegrationTests = System.getProperty(COMMAND_LINE_PARAMETER_SKIP_INTEGRATION_TESTS);
        if (skipIntegrationTests != null) {
            return skipIntegrationTests;
        }

        return System.getenv(COMMAND_LINE_PARAMETER_SKIP_INTEGRATION_TESTS);
    }
}

