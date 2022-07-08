package be.ucll.ip.minor.groep1209.controller;

import be.ucll.ip.minor.groep1209.domain.model.Munt;
import be.ucll.ip.minor.groep1209.domain.service.MuntService;
import be.ucll.ip.minor.groep1209.domain.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/coin")
public class MuntRestController {
    @Autowired
    private MuntService service;

    @Autowired
    MessageSource messageSource;

    @PutMapping("/add")
    public Iterable<Munt> save (@Valid @RequestBody Munt munt){
        service.saveMunt(munt);
        return service.findAllMunten();
    }

    @GetMapping("/overview")
    public Iterable<Munt> getAll(){
        return service.findAllMunten();
    }

    @PutMapping("/update/{id}")
    public Iterable<Munt> update (@PathVariable("id") long id, @Valid @RequestBody
                                  Munt munt){
        service.getMuntRepository().findById(id).orElseThrow(()-> new ServiceException("update", "coin.not.exist"));
        munt.setId(id);
        try {
            service.updateMunt(munt);
        } catch (ServiceException exc){
            throw new ServiceException("update", exc.getMessage());
        }
        return getAll();
    }

    @DeleteMapping("/delete/{id}")
    public Iterable<Munt> delete(@PathVariable("id") long id){
        try{
            service.deleteMuntById(id);
        } catch (ServiceException e){
            throw new ServiceException("delete", e.getMessage());
        }
        return getAll();
    }

    @GetMapping("/search/year/{year}")
    public Iterable<Munt> findByYear(@PathVariable("year") Integer year){
        return service.findAllByYear(year, false);
    }

    @GetMapping("/search/country")
    public Iterable<Munt> findByCountry(@RequestParam("value") String country){
        return service.findAllByCountry(country);
    }

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
