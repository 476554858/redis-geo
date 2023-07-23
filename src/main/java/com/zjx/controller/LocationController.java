package com.zjx.controller;

import com.zjx.req.LocationReq;
import com.zjx.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LocationController {

    @Autowired
    private LocationService locationService;

    /**
     * @param req
     * @return
     */
    @PutMapping("location/add")
    Object addLocation(@RequestBody LocationReq req) {
        return locationService.addLocation(req.getLocationName(), req.getLongitude(), req.getLatitude());
    }

    /**
     * 附近地理位置查询
     *
     * @param longitude
     * @param latitude
     * @param radius
     * @return
     */
    @GetMapping("location/query")
    List<GeoResult<RedisGeoCommands.GeoLocation<Object>>> findNearbyLocation(double longitude, double latitude, double radius) {
        return locationService.findNearbyLocation(longitude, latitude, radius);
    }


    /**
     * 地理位置新增
     *
     * @param req
     * @return
     */
    @PutMapping("location/batch/add")
    Object batchAdd(@RequestBody LocationReq req) {
        return locationService.batchSave(req.getLocationName(), req.getLongitude(), req.getLatitude());
    }
}
