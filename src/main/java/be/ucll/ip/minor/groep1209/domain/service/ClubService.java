package be.ucll.ip.minor.groep1209.domain.service;

import be.ucll.ip.minor.groep1209.domain.model.Club;
import be.ucll.ip.minor.groep1209.domain.model.DomainException;
import be.ucll.ip.minor.groep1209.domain.model.Verzamelaar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Array;
import java.util.*;

@Service
public class ClubService {
    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private VerzamelaarRepository verzamelaarRepository;

    @Autowired
    private VerzamelaarService verzamelaarService;

    public ClubService() {
    }



    public void saveClub(Club club){
        if (clubRepository.existsByName(club.getName())){
            throw new DomainException("clubname.exists");
        }
        if (clubRepository.existsByEmailAndRegion(club.getEmail(), club.getRegion())){
            throw new DomainException("region.email.combo.exists");
        }
        clubRepository.save(club);
    }

    public void updateClub(Club club){
        if (clubRepository.existsByEmailAndRegion(club.getEmail(), club.getRegion()) && !clubRepository.existsByEmailAndRegionAndId(club.getEmail(), club.getRegion(), club.getId())) {
            throw new DomainException("region.email.combo.exists");
        }
        if (clubRepository.existsByName(club.getName()) && !clubRepository.existsByNameAndId(club.getName(), club.getId())){
            throw new DomainException("clubname.exists");
        }
        Club unupdated = clubRepository.findById(club.getId()).get();
        if (unupdated.getCollectors().size() > club.getMaxMembers()){
            throw new ServiceException("maxMembers", "more.members.than.space");
        }
        club.setCollectors(unupdated.getCollectors());
        for (Verzamelaar verzamelaar : club.getCollectors()) {
            if (!club.getRegion().equalsIgnoreCase(verzamelaar.getRegion())){
                throw new DomainException("update.different.region.club");
            }
        }
        clubRepository.save(club);
    }

    public Optional<Club> findClub(long id)
    {
        return clubRepository.findById(id);
    }

    public List<Club> findAllClubsRest(){
        return clubRepository.findAll();
    }

    public Page<Club> findAllClubs(Integer pageNo, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo -1, 5, Sort.by(sortBy));
        Page<Club> pageResult = clubRepository.findAll(paging);


        return pageResult;
    }

    public List<Club> findAllClubsSorted(String column)
    {
        return clubRepository.findAll(Sort.by(column));
    }

    public List<Club> findAllClubsContaining(String name)
    {
        return clubRepository.findAllByNameContainingIgnoreCase(name.trim());
    }

    public List<Club> findAllByMaxMembers(int maxMembers, boolean until)
    {
        if (until){
            return clubRepository.findAllByMaxMembersGreaterThanEqual(maxMembers);
        } else {
            return clubRepository.findAllByMaxMembers(maxMembers);
        }
    }
    public List<Club> sortedListByName(Boolean bool)
    {
        return bool ? clubRepository.findAllByOrderByName(): clubRepository.findAllByOrderByNameDesc();
    }
    public List<Club> sortedListByMaxMembers(Boolean bool)
    {
        return bool ? clubRepository.findAllByOrderByMaxMembers(): clubRepository.findAllByOrderByMaxMembersDesc();
    }

    public ClubRepository getClubRepository() {
        return clubRepository;
    }

    public void deleteClubById(long id){
        Club club = clubRepository.findById(id).orElseThrow(() -> new ServiceException("delete", "club.not.exists"));
        List<Verzamelaar> verzamelaars = verzamelaarRepository.findAll();
        Iterator<Verzamelaar> it = verzamelaars.iterator();
        while (it.hasNext()){
            Verzamelaar verzamelaar = it.next();
            verzamelaar.removeClubWhileIterating(club);
            verzamelaarRepository.save(verzamelaar);
        }
        clubRepository.delete(club);
    }

    public Club addCollectorToClub(long club_id, long collector_id) {
        Club club = clubRepository.findById(club_id).orElseThrow(() -> new ServiceException("add_collector", "club" +
                ".not.exists"));
        Verzamelaar verzamelaar =
                verzamelaarRepository.findById(collector_id).orElseThrow(() -> new ServiceException("add_collector",
                        "verzamelaar.not.exist"));
        if (!club.getRegion().equalsIgnoreCase(verzamelaar.getRegion())) {
            throw new ServiceException("add_collector", "not.same.region");
        } else if (club.getMaxMembers() == club.getCollectors().size()) {
            throw new ServiceException("add_collector", "max.members.reached");
        } else if (club.getCollectors().contains(verzamelaar)) {
            throw new ServiceException("add_collector", "already.member");
        } else {
            club.addCollector(verzamelaar);
            verzamelaarRepository.save(verzamelaar);
            clubRepository.save(club);
            return club;
        }
    }

    public Club removeCollectorFromClub(long club_id, long collector_id) {
        Club club = clubRepository.findById(club_id).orElseThrow(() -> new ServiceException("remove_collector", "club" +
                ".not.exists"));
        Verzamelaar verzamelaar =
                verzamelaarRepository.findById(collector_id).orElseThrow(() -> new ServiceException("delete_collector",
                        "verzamelaar.not.exist"));

        if (!club.getCollectors().contains(verzamelaar))
        {
            throw new ServiceException("delete_collector", "club.not.contains.collector");
        } else
        {
            club.removeCollector(verzamelaar);
            verzamelaarRepository.save(verzamelaar);
            clubRepository.save(club);
            return club;
        }
    }

    public Iterable<Verzamelaar> getAllCollectorsForClub(long id) {
        Club club = clubRepository.findById(id).orElseThrow(() -> new ServiceException("get_collectors_for_club",
                "club.not.exists"));

        return club.getCollectors();
    }


//    public List<Club> findAllByEmail(String email){
//        List<Club> list = clubRepository.findAll();
//        List<Club> emailList = new ArrayList<>();
//        for (Club c: list){
//            if (c.getEmail().equals(email)){
//                emailList.add(c);
//            }
//        }
//        return emailList;
//    }
//
//    public List<Club> findAllByName(String name){
//        List<Club> list = clubRepository.findAll();
//        List<Club> nameList = new ArrayList<>();
//        for (Club c: list){
//            if (c.getName().equals(name)){
//                nameList.add(c);
//            }
//        }
//        return nameList;

//    }
//    public List<Club> findAllByMaxMembers(int maxMembers){
//        List<Club> list = clubRepository.findAll();
//        List<Club> membersList = new ArrayList<>();
//        for (Club c: list){
//            if (c.getMaxMembers() == maxMembers){
//                membersList.add(c);
//            }
//        }
//        return membersList;

//    }
//    public List<Club> sortedListByName(){
//        List<Club> sortedList = clubRepository.findAll();
//        sortedList.sort(Comparator.comparing(Club::getName));
//        return sortedList;

//    }

//    public List<Club> sortedListByMaxMembers(){
//        List<Club> sortedList = clubRepository.findAll();
//        sortedList.sort(Comparator.comparing(Club::getMaxMembers));
//        return sortedList;
//    }
}
