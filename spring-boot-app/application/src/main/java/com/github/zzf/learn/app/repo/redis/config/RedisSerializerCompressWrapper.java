package com.github.zzf.learn.app.repo.redis.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-23
 */
public class RedisSerializerCompressWrapper implements RedisSerializer<Object> {

    final CompressorStreamFactory csf = new CompressorStreamFactory();

    final RedisSerializer<Object> delegate;
    final String compressAlgorithm;

    public RedisSerializerCompressWrapper(RedisSerializer<Object> delegate, String compressAlgorithm) {
        this.delegate = delegate;
        if (!csf.getInputStreamCompressorNames().contains(compressAlgorithm)
            || !csf.getOutputStreamCompressorNames().contains(compressAlgorithm)) {
            throw new IllegalArgumentException("Unsupported compress algorithm: " + compressAlgorithm);
        }
        this.compressAlgorithm = compressAlgorithm;
    }

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        byte[] bytes = delegate.serialize(o);
        if (bytes == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (OutputStream compressor = csf.createCompressorOutputStream(compressAlgorithm, new BufferedOutputStream(out))) {
            compressor.write(bytes); // CompressorOutputStream will auto flush() before close()
        } catch (Exception e) {
            throw new SerializationException("Failed to compress byte[] with gzip", e);
        }
        return out.toByteArray();
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        byte[] decompressed;
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try (InputStream compressor = csf.createCompressorInputStream(compressAlgorithm, new BufferedInputStream(in))) {
            decompressed = compressor.readAllBytes();
        } catch (Exception e) {
            throw new SerializationException("Failed to compress byte[] with gzip", e);
        }
        return delegate.deserialize(decompressed);
    }
}
