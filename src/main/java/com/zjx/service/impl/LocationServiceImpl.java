package com.zjx.service.impl;

import com.zjx.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    public static final String LOCATION_KEY = "location_info";
    @Autowired
    private RedisTemplate redisTemplate;

    public Object addLocation(String locationName, double longitude, double latitude) {
        // 添加、更新地址位置
        Point point = new Point(longitude, latitude);
        RedisGeoCommands.GeoLocation<String> location = new RedisGeoCommands.GeoLocation<>(locationName, point);
        redisTemplate.opsForGeo().add(LOCATION_KEY, location);
        return "ok";
    }

    public List<GeoResult<RedisGeoCommands.GeoLocation<Object>>> findNearbyLocation(double longitude, double latitude, double radius) {
        // 标识圆形的查询条件，point坐标，distance；半径为 radius 单位（m,km...）的圆形
        Circle circle = new Circle(new Point(longitude, latitude), new Distance(radius, Metrics.KILOMETERS));
        // 其他参数
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                // 排序字段
                .sortDescending()
                // withdist表示同时返回距离信息
                .includeDistance()
                // withcoord表示同时返回坐标信息
                .includeCoordinates();
        GeoResults<RedisGeoCommands.GeoLocation<Object>> results = redisTemplate.opsForGeo().radius(LOCATION_KEY, circle, args);
        List<GeoResult<RedisGeoCommands.GeoLocation<Object>>> content = results.getContent();
        return content;
    }


    public Object batchSave(String locationName, double longitude, double latitude) {
        // 添加、更新地址位置
        Point point = new Point(longitude, latitude);
        RedisGeoCommands.GeoLocation<String> location = new RedisGeoCommands.GeoLocation<>(locationName, point);
        redisTemplate.opsForGeo().add(LOCATION_KEY, location);

//        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
//        redisTemplate.executePipelined(new RedisCallback<Object>() {
//            @Override
//            public Object doInRedis(RedisConnection connection) throws DataAccessException {
//                Point point = new Point(longitude, latitude);
//                RedisGeoCommands.GeoLocation<byte[]> location = new RedisGeoCommands.GeoLocation<>(serializer.serialize(locationName), point);
//                connection.geoCommands().geoAdd(serializer.serialize(LOCATION_KEY), location);
////                Long aLong = connection.geoAdd(LOCATION_KEY.getBytes(StandardCharsets.UTF_8), location);
////                List<RedisGeoCommands.GeoLocation<byte[]>> list = new ArrayList();
////                list.add(location);
////                Iterator<RedisGeoCommands.GeoLocation<byte[]>> iterator = list.iterator();
////
////                Iterable<RedisGeoCommands.GeoLocation<byte[]>> iterable = new Iterable<RedisGeoCommands.GeoLocation<byte[]>>() {
////                    @Override
////                    public Iterator iterator() {
////                        return iterator;
////                    }
////                };
////                connection.geoAdd(serializer.serialize(LOCATION_KEY), iterable);
//                return null;
//            }
//        }, serializer);
        return "ok";
    }
}
