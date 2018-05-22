package com.consort.util;

public enum EnvironmentProps {
    GitLabAdapterUrl("gitlab_adapter_url"), AccessKey("accesskey_s3"), SecretKey("secretkey_s3"), JwkKid("jwk_kid"),
    JwkUrl("jwk_url"), JwkAlg("jwk_alg"), BucketName("bucket_name");

    private final String text;

    EnvironmentProps(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}