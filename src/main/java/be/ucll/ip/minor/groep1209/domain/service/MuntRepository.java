package be.ucll.ip.minor.groep1209.domain.service;

import be.ucll.ip.minor.groep1209.domain.model.Club;
import be.ucll.ip.minor.groep1209.domain.model.Munt;
import be.ucll.ip.minor.groep1209.domain.model.MuntCollectie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MuntRepository extends JpaRepository<Munt, Long> {
    Munt findByName(String trim);
    boolean existsByName(String name);
    boolean existsByNameAndId(String name, long id);

    List<Munt> findAllByYearLessThanEqual(int year);
    List<Munt> findAllByYear(int year);
    List<Munt> findAllByCountry(String country);
    List<Munt> findAllByOrderByName();
    List<Munt> findAllByOrderByNameDesc();
    List<Munt> findAllByOrderByYear();
    List<Munt> findAllByOrderByYearDesc();
    List<Munt> findAllByCountryContainingIgnoreCase(String name);

}
