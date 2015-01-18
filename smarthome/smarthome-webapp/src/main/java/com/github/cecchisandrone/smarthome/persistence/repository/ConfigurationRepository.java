package com.github.cecchisandrone.smarthome.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.cecchisandrone.smarthome.domain.Configuration;

public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {

}