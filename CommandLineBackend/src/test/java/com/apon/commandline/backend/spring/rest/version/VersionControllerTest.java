package com.apon.commandline.backend.spring.rest.version;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import util.integrationtesting.IntegrationTest;
import util.systemlambda.SystemLambda;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@SuppressWarnings("WeakerAccess")
@IntegrationTest
public class VersionControllerTest {

    @Autowired
    private VersionController versionController;

    @Test
    public void testCorrectReleaseVersionIsReturned() throws Exception {
        String version = "v2018_14_1_CP_NN_31";

        SystemLambda
                .withEnvironmentVariable("HEROKU_RELEASE_VERSION", version)
                .execute(() -> assertEquals(versionController.getVersion().releaseVersion, version,
                        "The version is not equal to the environment variable 'HEROKU_RELEASE_VERSION'."));
    }

    @Test
    public void testCorrectCreatedAtIsReturned() throws Exception {
        String createdAt = "2019-08-05T20:46:35Z";
        SystemLambda
                .withEnvironmentVariable("HEROKU_RELEASE_CREATED_AT", createdAt)
                .execute(() -> assertEquals(versionController.getVersion().tsRelease, createdAt,
                        "The created-at is not equal to the environment variable 'HEROKU_RELEASE_CREATED_AT'."));
    }
}
