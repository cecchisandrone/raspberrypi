package it.cecchi.smarthome.persistence.repository;

import it.cecchi.smarthome.domain.Camera;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CameraRepository extends JpaRepository<Camera, Integer> {
   
       
}