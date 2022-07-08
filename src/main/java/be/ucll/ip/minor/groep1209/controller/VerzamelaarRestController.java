package be.ucll.ip.minor.groep1209.controller;

import be.ucll.ip.minor.groep1209.domain.model.DomainException;
import be.ucll.ip.minor.groep1209.domain.model.Munt;
import be.ucll.ip.minor.groep1209.domain.model.MuntCollectie;
import be.ucll.ip.minor.groep1209.domain.model.Verzamelaar;
import be.ucll.ip.minor.groep1209.domain.service.MuntCollectieService;
import be.ucll.ip.minor.groep1209.domain.service.ServiceException;
import be.ucll.ip.minor.groep1209.domain.service.VerzamelaarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
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
@RequestMapping("/api/collector")
public class VerzamelaarRestController {
    @Autowired
    private VerzamelaarService service;

    @Autowired
    MessageSource messageSource;

    @PutMapping("/add")
    public Iterable<Verzamelaar> add (@Valid @RequestBody Verzamelaar verzamelaar) {
        service.addVerzamelaar(verzamelaar);
        return service.findAllVerzamelaars();
    }

    @GetMapping("/overview")
    public Iterable<Verzamelaar> getAll(){
        return service.findAllVerzamelaars();
    }


    @PutMapping("/update/{id}")
    public Iterable<Verzamelaar> update (@PathVariable("id") long id, @Valid @RequestBody Verzamelaar verzamelaar) {
        service.getVerzamelaarRepository().findById(id).orElseThrow(()-> new ServiceException("update", "verzamelaar.not.exist"));
        verzamelaar.setId(id);
        try {
            service.updateVerzamelaar(verzamelaar);
        } catch (ServiceException exc) {
            throw new ServiceException("update", exc.getMessage());
        }
        return getAll();
    }

    @DeleteMapping("/delete/{id}")
    public Iterable<Verzamelaar> delete(@PathVariable("id") long id){
        try {
            service.deleteVerzamelaarById(id);
        } catch (ServiceException e)
        {
            throw new ServiceException("delete", e.getMessage());
        }
        return getAll();
    }

    @GetMapping("/search/region")
    public Iterable<Verzamelaar> findByRegion(@RequestParam("value") String region){
        return service.findAllByRegion(region);
    }

    @GetMapping("/search/nameAndFirstname")
    public Verzamelaar findByNameAndFirstname(@RequestParam("name") String name, @RequestParam("firstname") String firstname){
        return service.findVerzamelaarByNameAndFirstname(name, firstname);
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

