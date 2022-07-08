package be.ucll.ip.minor.groep1209.domain.service;

import be.ucll.ip.minor.groep1209.domain.model.DomainException;
import be.ucll.ip.minor.groep1209.domain.model.Munt;
import be.ucll.ip.minor.groep1209.domain.model.MuntCollectie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MuntService {
    @Autowired
    private MuntRepository muntRepository;

    public Munt saveMunt(Munt munt){
        if (muntRepository.existsByName(munt.getName()) && !muntRepository.existsByNameAndId(munt.getName(), munt.getId())){
            throw new ServiceException("save", "coin.name.exists");
        } else if (munt.getYear() > Calendar.getInstance().get(Calendar.YEAR)){
            throw new ServiceException("save", "year.too.big");
        }
        return muntRepository.save(munt);
    }

    public Munt updateMunt(Munt munt){
        if (muntRepository.existsByName(munt.getName()) && !muntRepository.existsByNameAndId(munt.getName(), munt.getId())){
            throw new ServiceException("save", "coin.name.exists");
        } else if (munt.getYear() > Calendar.getInstance().get(Calendar.YEAR)){
            throw new ServiceException("save", "year.too.big");
        }
        Munt unupdated = muntRepository.getById(munt.getId());
        if (unupdated.getCollectie() != null){
            throw new ServiceException("update", "coin.in.collection");
        }
        munt.setCollectie(unupdated.getCollectie());
        return muntRepository.save(munt);
    }

    //MUNT DELETEN
    public void deleteMunt(Munt munt){
        if (munt.getCollectieName() == null) {
            muntRepository.delete(munt);
        }
        else {
            throw new ServiceException("delete", "coin.in.collection");
        }
    }

    public Optional<Munt> findMunt(long id){
        return muntRepository.findById(id);
    }

    //ALLE MUNTEN OPVRAGEN
    public List<Munt> findAllMunten(){
        return muntRepository.findAll();
    }

    public List<Munt> findAllMuntenSorted(String column){
        return muntRepository.findAll(Sort.by(column));
    }

    //MUNT ZOEKEN DIE STRING LAND BEVAT
    public List<Munt> findAllByCountry(String country){
        return muntRepository.findAllByCountryContainingIgnoreCase(country);
    }

    public Munt findByName(String name) {return muntRepository.findByName(name.trim());}

    //ALLE MUNTEN VAN GEVRAAGD JAARTAL
    public List<Munt> findAllByYear(int year, boolean until)
    {
        if (until){
            return muntRepository.findAllByYearLessThanEqual(year);
        } else {
            return muntRepository.findAllByYear(year);
        }
    }

    public List<Munt> sortedListByName(Boolean bool)
    {
        return bool ? muntRepository.findAllByOrderByName(): muntRepository.findAllByOrderByNameDesc();
    }

    public List<Munt> sortedListByYear(Boolean bool)
    {
        return bool ? muntRepository.findAllByOrderByYear(): muntRepository.findAllByOrderByYearDesc();
    }

    public MuntRepository getMuntRepository(){
        return muntRepository;
    }

    public void deleteMuntById(long id) {
        Munt munt = muntRepository.findById(id).orElseThrow(() -> new ServiceException("delete", "coin.not.exist"));
        deleteMunt(munt);
    }
}
