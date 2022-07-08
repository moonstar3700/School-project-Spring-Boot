package be.ucll.ip.minor.groep1209.domain.service;

import be.ucll.ip.minor.groep1209.domain.model.Verzamelaar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerzamelaarRepository extends JpaRepository<Verzamelaar, Long> {
    Verzamelaar findByName(String name);
    Verzamelaar findByNameAndFirstname(String name, String firstname);

    boolean existsByName(String name);
    boolean existsByNameAndId(String name, long id);

    List<Verzamelaar> findAllByRegion(String region);
}
