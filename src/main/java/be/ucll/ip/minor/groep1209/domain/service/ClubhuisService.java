package be.ucll.ip.minor.groep1209.domain.service;

import be.ucll.ip.minor.groep1209.domain.model.Clubhuis;
import be.ucll.ip.minor.groep1209.domain.model.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class ClubhuisService {
    @Autowired
    private ClubhuisRepository clubhuisRepository;

    public ClubhuisService() { };

    public List<Clubhuis> findAll(){
        return clubhuisRepository.findAll();
    }

    public void saveClubhuis(Clubhuis clubhuis) {
        if (clubhuisRepository.existsByNameAndGemeente(clubhuis.getName(), clubhuis.getGemeente())){
            throw new DomainException("clubhuis.name.gemeente.exists");
        }
        clubhuisRepository.save(clubhuis);
    }

    public Optional<Clubhuis> findClubhuis(Long id) { return clubhuisRepository.findById(id); }

    public void deleteById(Long id) {
        Clubhuis clubhuis = clubhuisRepository.findById(id).orElseThrow(() -> new ServiceException("delete", "clubhuis.not.exists"));
        clubhuisRepository.delete(clubhuis);
    }

    public void updateHuis(Clubhuis clubhuis) {
        if (clubhuisRepository.existsByNameAndGemeente(clubhuis.getName(), clubhuis.getGemeente()) && !clubhuisRepository.existsByNameAndGemeenteAndId(clubhuis.getName(), clubhuis.getGemeente(), clubhuis.getId())){
            throw new DomainException("clubhuis.name.gemeente.exists");
        }
        clubhuisRepository.save(clubhuis);
    }

    public List<Clubhuis> findAllMaxMembersUntil(int until) {
        return clubhuisRepository.findAllByMaxMembersBefore(until);

    }

    public List<Clubhuis> findAllMaxMembersFrom(int from) {
        return clubhuisRepository.findAllByMaxMembersAfter(from);

    }

    public List<Clubhuis> findAllMinAndMaxMembersFromUntil(int from, int until) {
        return clubhuisRepository.findAllByMaxMembersAfterAndMaxMembersBefore(from, until);

    }

}
