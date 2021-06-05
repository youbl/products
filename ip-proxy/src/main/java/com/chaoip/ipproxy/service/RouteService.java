package com.chaoip.ipproxy.service;

import com.chaoip.ipproxy.controller.dto.RouteDto;
import com.chaoip.ipproxy.repository.RouteRepository;
import com.chaoip.ipproxy.repository.entity.Route;
import com.chaoip.ipproxy.util.CityHelper;
import com.mongodb.client.DistinctIterable;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RouteService
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:57
 */
@Service
public class RouteService {
    private final MongoTemplate mongoTemplate;
    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository,
                        MongoTemplate mongoTemplate) {
        this.routeRepository = routeRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public int saveMultiRoute(List<RouteDto> dtos) {
        for (RouteDto dto : dtos) {
            routeRepository.save(dto.mapTo());
        }
        return dtos.size();
    }

    public List<Route> getTop() {
        return routeRepository.findTopRoute(PageRequest.of(0, 8));//, Sort.Direction.DESC, "creationTime"));
    }

    /**
     * 自定义动态条件查询
     *
     * @param dto 条件
     * @return 结果
     */
    public List<Route> find(RouteDto dto) {
        Criteria condition = Criteria.where("id").gt(0); // id大于0，只是为了生成一个条件
        if (!StringUtils.isEmpty(dto.getProtocal())) {
            condition = condition.and("protocal").is(dto.getProtocal());
        }
        if (dto.getExpireTime() > 0) {
            condition = condition.and("expireTime").gte(LocalDateTime.now().plusSeconds(dto.getExpireTime()));
        }
        if (!StringUtils.isEmpty(dto.getProvince())) {
            condition = condition.and("province").is(dto.getProvince());
        }
        if (!StringUtils.isEmpty(dto.getArea())) {
            condition = condition.and("area").is(dto.getArea());
        }
        if (!StringUtils.isEmpty(dto.getOperators())) {
            condition = condition.and("operators").is(dto.getOperators());
        }
        Query query = Query.query(condition).limit(dto.getPageSize()).with(Sort.by(Sort.Direction.ASC, "id"));
        return mongoTemplate.find(query, Route.class);
    }

    public Page<Route> getAll(RouteDto dto) {
        // 不使用的属性，必须要用 withIgnorePaths 忽略，否则会列入条件
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id", "expireTime", "province");
        if (!StringUtils.isEmpty(dto.getArea())) {
            matcher = matcher.withMatcher("area", ExampleMatcher.GenericPropertyMatchers.exact());
        } else {
            matcher = matcher.withIgnorePaths("area");
        }
        if (!StringUtils.isEmpty(dto.getOperators())) {
            matcher = matcher.withMatcher("operators", ExampleMatcher.GenericPropertyMatchers.exact());
        } else {
            matcher = matcher.withIgnorePaths("operators");
        }
        if (!StringUtils.isEmpty(dto.getIp())) {
            matcher = matcher.withMatcher("ip", ExampleMatcher.GenericPropertyMatchers.exact());
        } else {
            matcher = matcher.withIgnorePaths("ip");
        }
        if (dto.getPort() > 0) {
            matcher = matcher.withMatcher("port", ExampleMatcher.GenericPropertyMatchers.exact());
        } else {
            matcher = matcher.withIgnorePaths("port");
        }
        if (!StringUtils.isEmpty(dto.getProtocal())) {
            matcher = matcher.withMatcher("protocal", ExampleMatcher.GenericPropertyMatchers.exact());
        } else {
            matcher = matcher.withIgnorePaths("protocal");
        }
        Example<Route> example = Example.of(dto.mapTo(), matcher);
        return routeRepository.pageSearch(example, dto.getPageNum(), dto.getPageSize(), "creationTime", true);
    }

    public Map<String, String[]> getAllUsingCity() {
        // 所有已有的城市code列表
        DistinctIterable<String> usingCitys = mongoTemplate.getCollection("routes").distinct("area", String.class);
        Map<String, String[]> ret = new HashMap<>();

        for (String code : usingCitys) {
            String[] cityArr = CityHelper.getArrByAreaCode(code);
            if (cityArr != null) {
                ret.put(code, cityArr);
            }
        }
        return ret;
    }

    /**
     * 返回库里的最小id，因为很多过期了
     *
     * @return 最小id
     */
    public long getMinRouteId() {
        // db.routes.find({},{"_id":1}).sort({"_id":1}).limit(1)
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC, "id")).limit(1);
        Route ret = mongoTemplate.findOne(query, Route.class);
        if (ret == null)
            return 0;
        return ret.getId();
    }
}
