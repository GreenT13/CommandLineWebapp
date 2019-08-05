package com.apon.commandline.backend.spring.rest.version;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class VersionControllerTest {

    @Autowired
    private VersionController versionController;

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Test
    public void testCorrectReleaseVersionIsReturned() {
        String version = "v2018_14_1_CP_NN_31";
        environmentVariables.set("HEROKU_RELEASE_VERSION", version);

        assertEquals("The version is not equal to the environment variable 'HEROKU_RELEASE_VERSION'.",
                versionController.getVersion().releaseVersion, version);
    }

    @Test
    public void testCorrectCreatedAtIsReturned() {
        String createdAt = "2019-08-05T20:46:35Z";
        environmentVariables.set("HEROKU_RELEASE_CREATED_AT", createdAt);

        assertEquals("The created-at is not equal to the environment variable 'HEROKU_RELEASE_CREATED_AT'.",
                versionController.getVersion().tsRelease, createdAt);
    }
}
