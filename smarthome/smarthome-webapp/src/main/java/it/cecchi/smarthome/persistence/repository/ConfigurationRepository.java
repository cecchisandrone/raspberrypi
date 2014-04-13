package it.cecchi.smarthome.persistence.repository;

import it.cecchi.smarthome.domain.Configuration;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {

}