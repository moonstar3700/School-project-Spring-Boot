package be.ucll.ip.minor.groep1209.domain.service;

import be.ucll.ip.minor.groep1209.domain.model.Clubhuis;
import be.ucll.ip.minor.groep1209.domain.model.MuntCollectie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubhuisRepository extends JpaRepository<Clubhuis, Long> {
    List<Clubhuis> findAll();
    List<Clubhuis> findAllByMaxMembersBefore(int until);
    List<Clubhuis> findAllByMaxMembersAfter(int from);
    List<Clubhuis> findAllByMaxMembersAfterAndMaxMembersBefore(int from, int until);

    boolean existsByNameAndGemeente(String name, String gemeente);
    boolean existsByNameAndGemeenteAndId(String name, String gemeente, long id);


}
