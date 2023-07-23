package com.zjx.service;

import org.springframework.data.geo.GeoResult;
import org.springframework.data.redis.connection.RedisGeoCommands;

import java.util.List;

public interface LocationService {

    /**
     * @param locationName
     * @param longitude    经度
     * @param latitude     纬度
     * @return
     */
    Object addLocation(String locationName, double longitude, double latitude);

    /**
     * @param longitude 经度
     * @param latitude  纬度
     * @param radius    半径
     * @return
     */
    List<GeoResult<RedisGeoCommands.GeoLocation<Object>>> findNearbyLocation(double longitude, double latitude, double radius);

    Object batchSave(String locationName, double longitude, double latitude);
}
