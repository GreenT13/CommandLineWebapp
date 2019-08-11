package util.integrationtesting;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate that the test is an integration test. Integration tests can be skipped (see {@link IntegrationTestCondition}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(IntegrationTestCondition.class)
public @interface IntegrationTest {

}
