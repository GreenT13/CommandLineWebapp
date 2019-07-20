package com.apon.commandline.backend.spring.rest.version;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController {

    @GetMapping("/version")
    public VersionResponseValueObject getVersion() {
        return new VersionResponseValueObject(
                getReleaseVersion(),
                getReleaseTimestamp()
        );
    }

    private String getReleaseVersion() {
        return coalesce(System.getenv("HEROKU_RELEASE_VERSION"), "vX");
    }

    private String getReleaseTimestamp() {
        return coalesce(System.getenv("HEROKU_RELEASE_CREATED_AT"),"1970-01-01T00:00:00Z");

    }

    private String coalesce(String a, String b) {
        if (a != null) {
            return a;
        }

        return b;
    }
}
