package com.tracking.vehicle_gps_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.tracking.vehicle_gps_service.DTO.VehicleLocation;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> void set(String key, T value) {
        try {

            String json = objectMapper.writeValueAsString(value);

            redisTemplate.opsForValue().set(key, json);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(e);
        }
    }

    public <T> void addMember(String key, T mem) {
        try {
            String json = objectMapper.writeValueAsString(mem);
            redisTemplate.opsForSet().add(key, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T get(String key, Class<T> clazz) {

        try {

            String json = redisTemplate.opsForValue().get(key);

            if (json == null) {
                return null;
            }

            return objectMapper.readValue(json, clazz);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> getMembers(String key, Class<T> clazz) {

        Set<String> items = redisTemplate.opsForSet().members(key);

        if (items == null || items.isEmpty()) {
            return List.of();
        }

        return items.stream().map(item -> {
            try {
                return objectMapper.readValue(item, clazz);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).filter(Objects::nonNull).toList();
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}