package be.ucll.ip.minor.groep1209.domain.service;

import be.ucll.ip.minor.groep1209.domain.model.Club;
import be.ucll.ip.minor.groep1209.domain.model.DomainException;
import be.ucll.ip.minor.groep1209.domain.model.Verzamelaar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class VerzamelaarService {

    @Autowired
    VerzamelaarRepository verzamelaarRepository;

    @Autowired
    ClubRepository clubRepository;


    public Verzamelaar addVerzamelaar(Verzamelaar verzamelaar) throws ServiceException {
        if (verzamelaarRepository.findByName(verzamelaar.getName()) != null) {
            throw new ServiceException("add", "name.already.used");
        }
        return verzamelaarRepository.save(verzamelaar);
    }

    public Iterable<Verzamelaar> findAllVerzamelaars() {
        return verzamelaarRepository.findAll();
    }

    public Verzamelaar findVerzamelaarByNameAndFirstname(String name, String firstname){
        return verzamelaarRepository.findByNameAndFirstname(name, firstname);
    }

    public VerzamelaarRepository getVerzamelaarRepository() {
        return verzamelaarRepository;
    }

    public Verzamelaar saveVerzamelaar(Verzamelaar verzamelaar) {
        if (verzamelaarRepository.existsByName(verzamelaar.getName()) && !verzamelaarRepository.existsByNameAndId(verzamelaar.getName(), verzamelaar.getId())) {
            throw new ServiceException("add", "name.already.used");
        }
        return verzamelaarRepository.save(verzamelaar);
    }

    public Verzamelaar updateVerzamelaar(Verzamelaar verzamelaar){
        if (verzamelaarRepository.existsByName(verzamelaar.getName()) && !verzamelaarRepository.existsByNameAndId(verzamelaar.getName(), verzamelaar.getId())) {
            throw new ServiceException("update", "name.already.used");
        }
        Verzamelaar unupdated = verzamelaarRepository.getById(verzamelaar.getId());
        verzamelaar.setClubs(unupdated.getClubs());
        for (Club club : verzamelaar.getClubs()){
            if (!club.getRegion().equalsIgnoreCase(verzamelaar.getRegion())){
                throw new ServiceException("update", "update.different.region");
            }
        }
        return verzamelaarRepository.save(verzamelaar);
    }

    public void deleteVerzamelaarById(long id) {
        Verzamelaar verzamelaar = verzamelaarRepository.findById(id).orElseThrow(() -> new ServiceException("delete",
                "verzamelaar.not.exist"));
        List<Club> clubs = clubRepository.findAll();
        Iterator<Club> iterator = clubs.iterator();
        while (iterator.hasNext()){
            Club club = iterator.next();
            club.removeCollectorWhileIterating(verzamelaar);
            clubRepository.save(club);
        }
        verzamelaarRepository.delete(verzamelaar);
    }

    public Iterable<Verzamelaar> findAllByRegion(String region) {
        return verzamelaarRepository.findAllByRegion(region);
    }
}
