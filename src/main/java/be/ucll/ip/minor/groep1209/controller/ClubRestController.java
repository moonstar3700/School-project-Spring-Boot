package be.ucll.ip.minor.groep1209.controller;

import be.ucll.ip.minor.groep1209.domain.model.Club;
import be.ucll.ip.minor.groep1209.domain.model.MuntCollectie;
import be.ucll.ip.minor.groep1209.domain.model.Verzamelaar;
import be.ucll.ip.minor.groep1209.domain.service.ClubRepository;
import be.ucll.ip.minor.groep1209.domain.service.ClubService;
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
@RequestMapping("/api/club")
public class ClubRestController {
    @Autowired
    private ClubService service;

    @Autowired
    MessageSource messageSource;

    @GetMapping("/overview")
    public Iterable<Club> getAll (){

        return service.findAllClubsRest();
    }

    @GetMapping("/sortByName")
    public Iterable<Club> sortByName () {
        return service.findAllClubsSorted("name");
    }

    @GetMapping("/search/maxNumber/{number}")
    public Iterable<Club> findByMaxMembers(@PathVariable("number") int number)
    {
        return service.findAllByMaxMembers(number, false);
    }

    @DeleteMapping("/delete/{id}")
    public Iterable<Club> delete(@Valid @PathVariable("id") Long id){
        try {
            service.deleteClubById(id);
        } catch (ServiceException e){
            throw new ServiceException("delete", e.getMessage());
        }
        return service.findAllClubsRest();
    }

    @PutMapping("/{club_id}/addCollector/{collector_id}")
    public Club addCollectorToClub(@PathVariable("club_id") long club_id,
                                   @PathVariable("collector_id") long collector_id)
    {
        try {
            return service.addCollectorToClub(club_id, collector_id);
        } catch (ServiceException e){
            throw new ServiceException(e.getAction(), e.getMessage());
        }
    }

    @DeleteMapping("/{club_id}/removeCollector/{collector_id}")
    public Club removeCollectorFromClub(@PathVariable("club_id") long club_id,
                                                  @PathVariable("collector_id") long collector_id)
    {
        try {
            return service.removeCollectorFromClub(club_id, collector_id);
        } catch (ServiceException e){
            throw new ServiceException(e.getAction(), e.getMessage());
        }
    }

    @GetMapping("/getCollectors")
    public Iterable<Verzamelaar> getCollectorForClub(@RequestParam("clubId") long id)
    {
        try {
            return service.getAllCollectorsForClub(id);
        } catch (ServiceException e)
        {
            throw new ServiceException(e.getAction(), e.getMessage());
        }
    }

    // add with not @Valid patient is BAD_REQUEST and is redirected to this method (MethodArgumentNotValidException)
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
        else if (ex instanceof be.ucll.ip.minor.groep1209.domain.service.ServiceException) {
            String error = messageSource.getMessage(ex.getMessage(), null, locale);
            errors.put(((be.ucll.ip.minor.groep1209.domain.service.ServiceException) ex).getAction(), error);
        }
        else {
            String errorMessage = messageSource.getMessage(ex.getCause().getMessage(), null, locale);
            errors.put(((ResponseStatusException)ex).getReason(), errorMessage);
        }
        return errors;
    }
}
