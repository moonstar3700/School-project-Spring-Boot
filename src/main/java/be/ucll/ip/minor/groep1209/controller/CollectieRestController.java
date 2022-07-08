package be.ucll.ip.minor.groep1209.controller;

import be.ucll.ip.minor.groep1209.domain.model.DomainException;
import be.ucll.ip.minor.groep1209.domain.model.Munt;
import be.ucll.ip.minor.groep1209.domain.model.MuntCollectie;
import be.ucll.ip.minor.groep1209.domain.service.MuntCollectieService;
import be.ucll.ip.minor.groep1209.domain.service.MuntService;
import be.ucll.ip.minor.groep1209.domain.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/collection")
public class CollectieRestController {
    @Autowired
    private MuntCollectieService service;

    @Autowired
    MessageSource messageSource;

    @GetMapping("/overview")
    public Iterable<MuntCollectie> getAll(){
        return service.findAllMuntCollecties();
    }


    @PutMapping("/update/{id}")
    public Iterable<MuntCollectie> update (@PathVariable("id") long id, @Valid @RequestBody MuntCollectie collectie) {
        service.getMuntCollectieRepository().findById(id).orElseThrow(()->new ServiceException("update", "coins.not.exist"));
        collectie.setId(id);
        service.updateMuntCollectie(collectie);
        return getAll();
    }

    @GetMapping("/sort/country")
    public Iterable<MuntCollectie> sortByCountry () {
        return service.findAllMuntcollectiesSorted("Country");
    }

    @GetMapping("/search/year/{year}")
    public Iterable<MuntCollectie> findByYear(@PathVariable("year") Integer year){
        return service.findAllByYear(year, false);
    }

    @DeleteMapping("/delete/{id}")
    public Iterable<MuntCollectie> delete(@PathVariable("id") long id){
        try {
            service.deleteCollectionById(id);
        } catch (DomainException e)
        {
            throw new ServiceException("delete", e.getMessage());
        }
        return getAll();
    }

    @GetMapping("/{id}/getCoins")
    public List<Munt> getCoins(@PathVariable("id") long id){
        return service.findAllCoins(id);
    }

    @PutMapping("/addCoin/{id}")
    public MuntCollectie addMunt(@PathVariable("id") long collection_id, @RequestParam(value = "coinId") long coinId ){
        try {
            return service.addMuntToCollection(collection_id, coinId);
        } catch (ServiceException e){
            throw new ServiceException(e.getAction(), e.getMessage());
        }
    }
    @DeleteMapping("/removeCoin/{id}")
    public MuntCollectie delete(@PathVariable("id") long collection_id, @RequestParam(value = "coinId") long coinId ){
        try {
            return service.deleteMuntFromCollection(collection_id, coinId);
        } catch (ServiceException e){
            throw new ServiceException(e.getAction(), e.getMessage());
        }
    }

    // add with not @Valid  is BAD_REQUEST and is redirected to this method (MethodArgumentNotValidException)
    // ServiceException are also redirected to this method
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, ServiceException.class, ResponseStatusException.class})
    public Map<String, String> handleValidationExceptions(Exception ex, Locale locale) {
        Map<String, String> errors = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException)ex).getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = messageSource.getMessage(error.getDefaultMessage(), null, locale);
                errors.put(fieldName, errorMessage);
            });
        }
        else if (ex instanceof ServiceException) {
            String error = messageSource.getMessage(ex.getMessage(), null, locale);
            errors.put(((ServiceException) ex).getAction(), error);
        }
        else {
            String errorMessage = messageSource.getMessage(ex.getCause().getMessage(), null, locale);
            errors.put(((ResponseStatusException)ex).getReason(), errorMessage);
        }
        return errors;
    }
}
