package com.ssstsar.zuul.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ssstsar.zuul.model.DynamicRoute;

@Repository
public interface DynamicRouteRedisRepository extends CrudRepository<DynamicRoute, String> {}