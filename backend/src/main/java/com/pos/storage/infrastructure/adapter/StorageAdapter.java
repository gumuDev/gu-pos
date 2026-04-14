package com.pos.storage.infrastructure.adapter;

import com.pos.storage.infrastructure.config.StorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.UUID;

@Component
public class StorageAdapter {

    private static final Logger log = LoggerFactory.getLogger(StorageAdapter.class);

    private final StorageProperties props;

    public StorageAdapter(StorageProperties props) {
        this.props = props;
    }

    public String upload(MultipartFile file) throws Exception {
        String ext = getExtension(file.getOriginalFilename());
        String filename = "receipt_" + UUID.randomUUID() + "." + ext;

        // Supabase REST API uses /storage/v1/object/<bucket>/<filename>
        // Public URL uses /storage/v1/object/public/<bucket>/<filename>
        String uploadUrl;
        String publicUrl;
        if ("supabase".equals(props.getProvider())) {
            String base = props.getUrl().replace("/s3", ""); // strip S3 suffix if present
            uploadUrl = base + "/object/" + props.getBucket() + "/" + filename;
            publicUrl = base + "/object/public/" + props.getBucket() + "/" + filename;
        } else {
            uploadUrl = props.getUrl() + "/" + props.getBucket() + "/" + filename;
            publicUrl = props.getPublicUrl() + "/" + props.getBucket() + "/" + filename;
        }

        log.info("Uploading receipt — provider={} uploadUrl={}", props.getProvider(), uploadUrl);

        byte[] bytes = file.getBytes();

        HttpURLConnection conn = (HttpURLConnection) URI.create(uploadUrl).toURL().openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setFixedLengthStreamingMode(bytes.length);
        conn.setRequestProperty("Content-Type", file.getContentType() != null ? file.getContentType() : "image/jpeg");
        conn.setRequestProperty("Content-Length", String.valueOf(bytes.length));

        if ("supabase".equals(props.getProvider())) {
            conn.setRequestProperty("Authorization", "Bearer " + props.getSecretKey());
            conn.setRequestProperty("x-upsert", "true");
        }
        // minio: bucket is public, no Authorization header needed for PUT

        try (OutputStream out = conn.getOutputStream()) {
            out.write(bytes);
        }

        int status = conn.getResponseCode();
        if (status < 200 || status >= 300) {
            String body = new String(conn.getErrorStream() != null ? conn.getErrorStream().readAllBytes() : new byte[0]);
            log.error("Storage upload failed — status={} body={}", status, body);
            throw new RuntimeException("storage_upload_failed:" + status);
        }

        log.info("Receipt uploaded — filename={} publicUrl={}", filename, publicUrl);
        return publicUrl;
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "jpg";
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }
}
