package com.example.healthcheck.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;

public class DatabaseCleaner implements InitializingBean {

	@PersistenceContext
	private EntityManager entityManager;

	private List<String> tableNames;

	@Override
	public void afterPropertiesSet() {
		tableNames = entityManager.getMetamodel().getEntities().stream()
			.filter(entityType -> entityType.getJavaType().getAnnotation(Entity.class) != null)
			.map(entityType -> entityType.getJavaType().getAnnotation(Table.class).name())
			.collect(Collectors.toList());
	}

	@Transactional
	public void tableClear() {
		entityManager.flush();
		entityManager.clear();

		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

		for (String tableName : tableNames) {
			entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
		}
		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
	}
}

