package be.ucll.ip.minor.groep1209.controller;

import be.ucll.ip.minor.groep1209.domain.model.*;
import be.ucll.ip.minor.groep1209.domain.service.ClubService;
import be.ucll.ip.minor.groep1209.domain.service.ClubhuisService;
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
@RequestMapping("/api/clubhuis")
public class ClubhuisRestController {

    @Autowired
    private ClubhuisService service;

    @Autowired
    MessageSource messageSource;

    @GetMapping("/all")
    public Iterable<Clubhuis> getAll (){

        return service.findAll();
    }

    @PutMapping("/add")
    public Iterable<Clubhuis> save (@Valid @RequestBody Clubhuis clubhuis){
        try {
            service.saveClubhuis(clubhuis);
        } catch (DomainException | IllegalArgumentException e)
        {
            throw new ServiceException("add", e.getMessage());
        }
        return service.findAll();
    }

    @PutMapping("/update/{id}")
    public Iterable<Clubhuis> update (@PathVariable("id") long id, @Valid @RequestBody Clubhuis clubhuis) {
        service.getClubhuisRepository().findById(id).orElseThrow(()->new ServiceException("update", "clubhuis.not.exists.delete"));
        clubhuis.setId(id);
        try {
            service.updateHuis(clubhuis);
        } catch (ServiceException exc){
            throw new ServiceException("update", exc.getMessage());
        }
        return getAll();
    }

    @DeleteMapping("/delete")
    public Iterable<Clubhuis> delete(@RequestParam(name = "id") String id){
        if (id == null){
            System.out.println("\n\n\nempty\n\n\n");
        }
        try{
            System.out.println("\n\n\n" + id  + "\n\n\n");

            service.deleteById(Long.parseLong(id));
        } catch (ServiceException |DomainException e){
            throw new ServiceException("delete", e.getMessage());
        }
        return service.findAll();
    }

    @GetMapping("/search/{gemeente}")
    public Iterable<Clubhuis> search(@PathVariable("gemeente") String gemeente){
        return service.findAllIncludingGemeente(gemeente);
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
