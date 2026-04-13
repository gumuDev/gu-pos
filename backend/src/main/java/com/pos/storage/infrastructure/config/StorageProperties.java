package com.pos.storage.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    private String url;
    private String publicUrl;
    private String bucket;
    private String accessKey;
    private String secretKey;
    private String provider; // minio | supabase

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getPublicUrl() { return publicUrl != null ? publicUrl : url; }
    public void setPublicUrl(String publicUrl) { this.publicUrl = publicUrl; }

    public String getBucket() { return bucket; }
    public void setBucket(String bucket) { this.bucket = bucket; }

    public String getAccessKey() { return accessKey; }
    public void setAccessKey(String accessKey) { this.accessKey = accessKey; }

    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
}
