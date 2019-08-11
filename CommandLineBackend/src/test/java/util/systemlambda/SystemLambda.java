package util.systemlambda;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Class.forName;
import static java.lang.System.getenv;
import static java.util.Collections.singletonMap;

/**
 * https://stefanbirkner.github.io/system-rules/
 * Library for setting environment variables during a test. However, it doesn't support JUnit 5.
 * The creator said that you can just copy this code that was work in progress, to make it work for you now.
 * See https://github.com/stefanbirkner/system-rules/issues/55, comment 14-03-2019.
 */
public class SystemLambda {

    /**
     * Executes a statement with the specified environment variables. All
     * changes to environment variables are reverted after the statement has
     * been executed.
     * <pre>
     * &#064;Test
     * public void execute_code_with_environment_variables(
     * ) throws Exception {
     *   withEnvironmentVariable("first", "first value")
     *     .and("second", "second value")
     *     .and("third", null)
     *     .execute(
     *       () -&gt; {
     *         assertEquals(
     *           "first value",
     *           System.getenv("first")
     *         );
     *         assertEquals(
     *           "second value",
     *           System.getenv("second")
     *         );
     *         assertNull(
     *           System.getenv("third")
     *         );
     *       }
     *     );
     * }
     * </pre>
     * <p>You cannot specify the value of an an environment variable twice. An
     * {@code IllegalArgumentException} when you try.
     * <p><b>Warning:</b> This method uses reflection for modifying internals of the
     * environment variables map. It fails if your {@code SecurityManager} forbids
     * such modifications.
     * @param name the name of the environment variable.
     * @param value the value of the environment variable.
     * @return an {@link WithEnvironmentVariables} instance that can be used to
     * set more variables and run a statement with the specified environment
     * variables.
     * @since 1.0.0
     * @see WithEnvironmentVariables#and(String, String)
     * @see WithEnvironmentVariables#execute(Statement)
     */
    public static WithEnvironmentVariables withEnvironmentVariable(
            String name,
            String value
    ) {
        return new WithEnvironmentVariables(
                singletonMap(name, value));
    }

    /**
     * A collection of values for environment variables. New values can be
     * added by {@link #and(String, String)}. The {@code EnvironmentVariables}
     * object is then used to execute an arbitrary piece of code with these
     * environment variables being present.
     */
    public static final class WithEnvironmentVariables {
        private final Map<String, String> variables;

        private WithEnvironmentVariables(
                Map<String, String> variables
        ) {
            this.variables = variables;
        }

        /**
         * Creates a new {@code WithEnvironmentVariables} object that
         * additionally stores the value for an additional environment variable.
         * <p>You cannot specify the value of an environment variable twice. An
         * {@code IllegalArgumentException} when you try.
         * @param name the name of the environment variable.
         * @param value the value of the environment variable.
         * @return a new {@code WithEnvironmentVariables} object.
         * @throws IllegalArgumentException when a value for the environment
         * variable {@code name} is already specified.
         * @see #withEnvironmentVariable(String, String)
         * @see #execute(Statement)
         */
        @SuppressWarnings("WeakerAccess")
        public WithEnvironmentVariables and (
                String name,
                String value
        ) {
            validateNotSet(name, value);
            HashMap<String, String> moreVariables = new HashMap<>(variables);
            moreVariables.put(name, value);
            return new WithEnvironmentVariables(moreVariables);
        }

        private void validateNotSet(
                String name,
                String value
        ) {
            if (variables.containsKey(name)) {
                String currentValue = variables.get(name);
                throw new IllegalArgumentException(
                        "The environment variable '" + name + "' cannot be set to "
                                + format(value) + " because it was already set to "
                                + format(currentValue) + "."
                );
            }
        }

        private String format(
                String text
        ) {
            if (text == null)
                return "null";
            else
                return "'" + text + "'";
        }

        /**
         * Executes a statement with environment variable values according to
         * what was set before. All changes to environment variables are
         * reverted after the statement has been executed.
         * <pre>
         * &#064;Test
         * public void execute_code_with_environment_variables(
         * ) throws Exception {
         *   withEnvironmentVariable("first", "first value")
         *     .and("second", "second value")
         *     .and("third", null)
         *     .execute(
         *       () -&gt; {
         *         assertEquals(
         *           "first value",
         *           System.getenv("first")
         *         );
         *         assertEquals(
         *           "second value",
         *           System.getenv("second")
         *         );
         *         assertNull(
         *           System.getenv("third")
         *         );
         *       }
         *     );
         * }
         * </pre>
         * <p><b>Warning:</b> This method uses reflection for modifying internals of the
         * environment variables map. It fails if your {@code SecurityManager} forbids
         * such modifications.
         * @throws Exception any exception thrown by the statement.
         * @since 1.0.0
         * @see #withEnvironmentVariable(String, String)
         * @see WithEnvironmentVariables#and(String, String)
         */
        public void execute(
                Statement statement
        ) throws Exception {
            Map<String, String> originalVariables = new HashMap<>(getenv());
            try {
                setEnvironmentVariables();
                statement.execute();
            } finally {
                restoreOriginalVariables(originalVariables);
            }
        }

        private void setEnvironmentVariables() {
            overrideVariables(
                    getEditableMapOfVariables()
            );
            overrideVariables(
                    getTheCaseInsensitiveEnvironment()
            );
        }

        private void overrideVariables(
                Map<String, String> existingVariables
        ) {
            if (existingVariables != null) //theCaseInsensitiveEnvironment may be null
                variables.forEach(
                        (name, value) -> set(existingVariables, name, value)
                );
        }

        private void set(
                Map<String, String> variables,
                String name,
                String value
        ) {
            if (value == null)
                variables.remove(name);
            else
                variables.put(name, value);
        }

        void restoreOriginalVariables(
                Map<String, String> originalVariables
        ) {
            restoreVariables(
                    getEditableMapOfVariables(),
                    originalVariables
            );
            restoreVariables(
                    getTheCaseInsensitiveEnvironment(),
                    originalVariables
            );
        }

        void restoreVariables(
                Map<String, String> variables,
                Map<String, String> originalVariables
        ) {
            if (variables != null) {//theCaseInsensitiveEnvironment may be null
                variables.clear();
                variables.putAll(originalVariables);
            }
        }

        private static Map<String, String> getEditableMapOfVariables() {
            Class<?> classOfMap = getenv().getClass();
            try {
                return getFieldValue(classOfMap, getenv(), "m");
            } catch (IllegalAccessException e) {
                throw new RuntimeException("System Rules cannot access the field"
                        + " 'm' of the map System.getenv().", e);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("System Rules expects System.getenv() to"
                        + " have a field 'm' but it has not.", e);
            }
        }

        /*
         * The names of environment variables are case-insensitive in Windows.
         * Therefore it stores the variables in a TreeMap named
         * theCaseInsensitiveEnvironment.
         */
        private static Map<String, String> getTheCaseInsensitiveEnvironment() {
            try {
                Class<?> processEnvironment = forName("java.lang.ProcessEnvironment");
                return getFieldValue(
                        processEnvironment, null, "theCaseInsensitiveEnvironment");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("System Rules expects the existence of"
                        + " the class java.lang.ProcessEnvironment but it does not"
                        + " exist.", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("System Rules cannot access the static"
                        + " field 'theCaseInsensitiveEnvironment' of the class"
                        + " java.lang.ProcessEnvironment.", e);
            } catch (NoSuchFieldException e) {
                //this field is only available for Windows
                return null;
            }
        }

        private static Map<String, String> getFieldValue(
                Class<?> klass,
                Object object,
                String name
        ) throws NoSuchFieldException, IllegalAccessException {
            Field field = klass.getDeclaredField(name);
            field.setAccessible(true);
            //noinspection unchecked
            return (Map<String, String>) field.get(object);
        }
    }

}
