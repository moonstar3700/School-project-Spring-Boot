package be.ucll.ip.minor.groep1209.domain.service;

import be.ucll.ip.minor.groep1209.domain.model.Clubhuis;
import be.ucll.ip.minor.groep1209.domain.model.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
