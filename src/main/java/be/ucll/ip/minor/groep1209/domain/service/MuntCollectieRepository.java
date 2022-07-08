package be.ucll.ip.minor.groep1209.domain.service;

import be.ucll.ip.minor.groep1209.domain.model.Club;
import be.ucll.ip.minor.groep1209.domain.model.MuntCollectie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MuntCollectieRepository extends JpaRepository<MuntCollectie, Long> {
    List<MuntCollectie> findAllByTitle(String title);
    List<MuntCollectie> findAllByCountry(String country);

    boolean existsByTitle(String title);
    boolean existsByTitleAndId(String title, long id);
    boolean existsByYearAndCountry(int year, String country);
    boolean existsByYearAndCountryAndId(int year, String county, long id);

    List<MuntCollectie> findAllByCountryIgnoreCase(String country);
    List<MuntCollectie> findAllByYear(int year);
    List<MuntCollectie> findAllByYearLessThanEqual(int year);

    List<MuntCollectie> findAllByOrderByTitle();
    List<MuntCollectie> findAllByOrderByTitleDesc();
    List<MuntCollectie> findAllByOrderByYear();
    List<MuntCollectie> findAllByOrderByYearDesc();
}
