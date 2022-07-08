package be.ucll.ip.minor.groep1209.domain.service;

import be.ucll.ip.minor.groep1209.domain.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findAllByEmail(String email);
    List<Club> findAllByName(String name);
    boolean existsByEmailAndRegion(String email, String region);
    boolean existsByEmailAndRegionAndId(String email, String region, long id);
    boolean existsByName(String name);
    boolean existsByNameAndId(String name, long id);
    List<Club> findAllByMaxMembers(int maxMembers);
    List<Club> findAllByNameContainingIgnoreCase(String name);
    List<Club> findAllByMaxMembersGreaterThanEqual(int maxMembers);

    List<Club> findAllByOrderByMaxMembers();
    List<Club> findAllByOrderByMaxMembersDesc();
    List<Club> findAllByOrderByName();
    List<Club> findAllByOrderByNameDesc();
}
