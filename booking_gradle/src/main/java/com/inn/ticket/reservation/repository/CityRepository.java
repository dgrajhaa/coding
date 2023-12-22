package com.inn.ticket.reservation.repository;

import com.inn.ticket.reservation.domain.City;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the City entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Query(value = "select * from city where city_name=:cityName", nativeQuery = true)
    public City findByCityName(String cityName);
}
