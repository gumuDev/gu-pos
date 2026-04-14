package com.pos.storage.infrastructure.adapter;

import com.pos.storage.infrastructure.config.StorageProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
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
        String contentType = file.getContentType() != null ? file.getContentType() : "image/jpeg";

        log.info("Uploading receipt — provider={} bucket={} filename={}", props.getProvider(), props.getBucket(), filename);

        if ("minio".equals(props.getProvider())) {
            return uploadToMinio(file, filename, contentType);
        } else {
            return uploadToSupabase(file, filename, contentType);
        }
    }

    private String uploadToMinio(MultipartFile file, String filename, String contentType) throws Exception {
        MinioClient client = MinioClient.builder()
            .endpoint(props.getUrl())
            .credentials(props.getAccessKey(), props.getSecretKey())
            .build();

        client.putObject(PutObjectArgs.builder()
            .bucket(props.getBucket())
            .object(filename)
            .stream(new ByteArrayInputStream(file.getBytes()), file.getSize(), -1)
            .contentType(contentType)
            .build());

        String publicUrl = props.getPublicUrl() + "/" + props.getBucket() + "/" + filename;
        log.info("Receipt uploaded to Minio — filename={} publicUrl={}", filename, publicUrl);
        return publicUrl;
    }

    private String uploadToSupabase(MultipartFile file, String filename, String contentType) throws Exception {
        String base = props.getUrl().replace("/s3", "");
        String uploadUrl = base + "/object/" + props.getBucket() + "/" + filename;
        String publicUrl = base + "/object/public/" + props.getBucket() + "/" + filename;

        byte[] bytes = file.getBytes();

        HttpURLConnection conn = (HttpURLConnection) URI.create(uploadUrl).toURL().openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setFixedLengthStreamingMode(bytes.length);
        conn.setRequestProperty("Content-Type", contentType);
        conn.setRequestProperty("Authorization", "Bearer " + props.getSecretKey());
        conn.setRequestProperty("x-upsert", "true");

        try (OutputStream out = conn.getOutputStream()) {
            out.write(bytes);
        }

        int status = conn.getResponseCode();
        if (status < 200 || status >= 300) {
            String body = new String(conn.getErrorStream() != null ? conn.getErrorStream().readAllBytes() : new byte[0]);
            log.error("Supabase upload failed — status={} body={}", status, body);
            throw new RuntimeException("storage_upload_failed:" + status);
        }

        log.info("Receipt uploaded to Supabase — filename={} publicUrl={}", filename, publicUrl);
        return publicUrl;
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "jpg";
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }
}
