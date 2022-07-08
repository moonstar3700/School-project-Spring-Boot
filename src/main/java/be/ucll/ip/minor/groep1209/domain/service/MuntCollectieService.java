package be.ucll.ip.minor.groep1209.domain.service;

import be.ucll.ip.minor.groep1209.domain.model.Club;
import be.ucll.ip.minor.groep1209.domain.model.DomainException;
import be.ucll.ip.minor.groep1209.domain.model.Munt;
import be.ucll.ip.minor.groep1209.domain.model.MuntCollectie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MuntCollectieService {
    @Autowired
    private MuntCollectieRepository muntCollectieRepository;

    @Autowired
    private MuntRepository muntRepository;

    public void saveMuntCollectie(MuntCollectie muntCollectie)
    {
        if (muntCollectieRepository.existsByTitle(muntCollectie.getTitle()) &&
                !getMuntCollectieRepository().existsByTitleAndId(muntCollectie.getTitle(), muntCollectie.getId())){
            throw new DomainException("title.exists");
        }
        if (muntCollectieRepository.existsByYearAndCountry(muntCollectie.getYear(), muntCollectie.getCountry()) && !getMuntCollectieRepository().existsByYearAndCountryAndId(muntCollectie.getYear(), muntCollectie.getCountry(), muntCollectie.getId())){
            throw new DomainException("country.year.combo.exists");
        }
        muntCollectieRepository.save(muntCollectie);
    }

    public void updateMuntCollectie(MuntCollectie muntCollectie){
        if (muntCollectieRepository.existsByTitle(muntCollectie.getTitle()) &&
                !getMuntCollectieRepository().existsByTitleAndId(muntCollectie.getTitle(), muntCollectie.getId())){
            throw new DomainException("title.exists");
        }
        if (muntCollectieRepository.existsByYearAndCountry(muntCollectie.getYear(), muntCollectie.getCountry()) && !getMuntCollectieRepository().existsByYearAndCountryAndId(muntCollectie.getYear(), muntCollectie.getCountry(), muntCollectie.getId())){
            throw new DomainException("country.year.combo.exists");
        }
        if (muntCollectie.getMunten().size() != 0){
            throw new ServiceException("update", "collection.has.coins");
        }
        muntCollectieRepository.save(muntCollectie);
    }

    public void deleteCollection(MuntCollectie collection)
    {
        if (collection.getMunten().size() == 0) {
            muntCollectieRepository.delete(collection);
        } else {
            throw new ServiceException("delete", "collection.has.coins");
        }
    }

    public Optional<MuntCollectie> findCollection(long id)
    {
        return muntCollectieRepository.findById(id);
    }

    public List<MuntCollectie> findAllMuntCollecties()
    {
        return muntCollectieRepository.findAll();
    }

    public Page<MuntCollectie> findAllMuntCollectiesByPage(Integer pageNo, String sortBy){
        Pageable paging = PageRequest.of(pageNo -1, 5, Sort.by(sortBy));
        Page<MuntCollectie> pageResult = muntCollectieRepository.findAll(paging);

        return pageResult;
    }

    public List<MuntCollectie> findAllMuntcollectiesSorted(String column)
    {
        return muntCollectieRepository.findAll(Sort.by(column));
    }

    public List<MuntCollectie> findAllByCountry(String country)
    {
        return muntCollectieRepository.findAllByCountryIgnoreCase(country.trim());
    }

    public List<MuntCollectie> findAllByYear(int year, boolean until)
    {
        if (until){
            return muntCollectieRepository.findAllByYearLessThanEqual(year);
        } else {
            return muntCollectieRepository.findAllByYear(year);
        }
    }

    public Optional<MuntCollectie> findById(long id){
        return muntCollectieRepository.findById(id);
    }

    public List<MuntCollectie> sortedListByTitle(Boolean bool)
    {
        return bool ? muntCollectieRepository.findAllByOrderByTitle(): muntCollectieRepository.findAllByOrderByTitleDesc();
    }

    public List<MuntCollectie> sortedListByYear(Boolean bool)
    {
        return bool ? muntCollectieRepository.findAllByOrderByYear(): muntCollectieRepository.findAllByOrderByYearDesc();
    }

    public MuntCollectieRepository getMuntCollectieRepository() {
        return muntCollectieRepository;
    }

    public void deleteCollectionById(long id) {
        MuntCollectie collectie = muntCollectieRepository.findById(id).orElseThrow(() -> new DomainException("coins" +
                ".not.exist"));
        deleteCollection(collectie);
    }

    public MuntCollectie deleteMuntFromCollection(long muntCollectieId, long coinId) {
        MuntCollectie muntCollectie = muntCollectieRepository.findById(muntCollectieId).orElseThrow(()->new ServiceException(
                "delete_munt","collection.not.exist"));
        Munt munt = muntRepository.findById(coinId).orElseThrow(()->new ServiceException("search","coin.not.exist"));

        if (!muntCollectie.getMunten().contains(munt))
        {
            throw new ServiceException("delete_munt", "collection.not.contains.coin");
        } else
        {
            muntCollectie.deleteMunt(munt);
            muntRepository.save(munt);
            muntCollectieRepository.save(muntCollectie);
        }

        return muntCollectie;
    }

    public MuntCollectie addMuntToCollection(long muntCollectieId, long coinId) {
        /** zoek de munt eerst op. Dus muntrepository moet hier ook bij?  */
        MuntCollectie collectie = muntCollectieRepository.findById(muntCollectieId).orElseThrow(()->new ServiceException(
                "add_munt","collection.not.exist"));
        Munt munt = muntRepository.findById(coinId).orElseThrow(()->new ServiceException("add_munt","coin.not.exist"));
        List<Munt> lijst = collectie.getMunten();
        /** check */
        if (munt.getCollectie() != null){
            throw new ServiceException("add","coin.has.collection");
        }
        if (!munt.getCountry().equalsIgnoreCase(collectie.getCountry()) || munt.getYear() > collectie.getYear()){
            throw new ServiceException("add","coin.not.suited");
        }
        if (lijst.contains(munt)){
            throw new ServiceException("add", "coin.already.in.collection");}
        for (Munt m: lijst){
            if (m.getYear().equals(munt.getYear()) && m.getValue().equals(munt.getValue())) {
                throw new ServiceException("add","similar.coin.in.collection");
            }
        }

        /** voeg munt toe aan collectie */
        collectie.addMunt(munt);
        /** alles in de database saven */
        muntCollectieRepository.save(collectie);
        muntRepository.save(munt);
        return collectie;
    }

    public List<Munt> findAllCoins(long id) {
        MuntCollectie col = muntCollectieRepository.findById(id).orElseThrow(()->new ServiceException("add","collection.not.exists"));
        return col.getMunten();
    }
}
