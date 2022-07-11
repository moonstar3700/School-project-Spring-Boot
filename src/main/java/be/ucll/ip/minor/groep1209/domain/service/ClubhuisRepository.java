package be.ucll.ip.minor.groep1209.domain.service;

import be.ucll.ip.minor.groep1209.domain.model.Clubhuis;
import be.ucll.ip.minor.groep1209.domain.model.MuntCollectie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubhuisRepository extends JpaRepository<Clubhuis, Long> {
    List<Clubhuis> findAll();

    boolean existsByNameAndGemeente(String name, String gemeente);
}
