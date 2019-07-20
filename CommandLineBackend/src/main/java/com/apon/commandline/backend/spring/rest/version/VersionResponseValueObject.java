package com.apon.commandline.backend.spring.rest.version;

@SuppressWarnings("WeakerAccess")
public class VersionResponseValueObject {
    public String releaseVersion;
    public String tsRelease;

    public VersionResponseValueObject(String releaseVersion, String tsRelease) {
        this.releaseVersion = releaseVersion;
        this.tsRelease = tsRelease;
    }
}
