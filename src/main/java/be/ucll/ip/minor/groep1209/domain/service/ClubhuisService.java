package be.ucll.ip.minor.groep1209.domain.service;

import be.ucll.ip.minor.groep1209.domain.model.Clubhuis;
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

}
