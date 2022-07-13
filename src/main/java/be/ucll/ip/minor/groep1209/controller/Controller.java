package be.ucll.ip.minor.groep1209.controller;

import be.ucll.ip.minor.groep1209.domain.model.*;
import be.ucll.ip.minor.groep1209.domain.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    private ClubService clubService;

    @Autowired
    private MuntCollectieService muntCollectieService;

    @Autowired
    private ClubhuisService clubhuisService;

    @GetMapping("/home")
    public String home(){
        return "index";
    }

    @GetMapping("/club/overview")
    public String redirect (Model model){
    return "redirect:/club/overview/1";
    }

    @GetMapping("/club/overview/{pageNo}")
    public String cluboverview (Model model, @PathVariable(name = "pageNo", required = false) Integer pageNo){

        Page page = clubService.findAllClubs(pageNo, "id");
        int totalPages = page.getTotalPages();
        if(pageNo <= 0){pageNo = 1;}
        if(pageNo > totalPages){pageNo = totalPages;}
        model.addAttribute("pageAmount", totalPages);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("clubs", page.getContent());
        model.addAttribute("pageYes", true);
        if (page.getContent().size() == 0){
            model.addAttribute("message", "no.registered.clubs");
        }
        return "club-overview";
    }

    @GetMapping("/club/overview/sort")
    public String cluboverviewsort (Model model, @RequestParam(name = "sortclub") String sortclub){
        if (sortclub.equals("nameASC")) {
            model.addAttribute("clubs", clubService.sortedListByName(true));
        }
        else if (sortclub.equals("nameDESC")) {
            model.addAttribute("clubs", clubService.sortedListByName(false));
        }
        else if (sortclub.equals("memberASC")) {
            model.addAttribute("clubs", clubService.sortedListByMaxMembers(false));
        }
        else if (sortclub.equals("memberDESC")) {
            model.addAttribute("clubs", clubService.sortedListByMaxMembers(true));
        }
        return "club-overview";
    }

    @GetMapping("/coins/overview")
    public String coinsoverview (Model model){
/*        model.addAttribute("coins", muntCollectieService.findAllMuntCollecties());
        return "coins-overview";*/
        return "redirect:/coins/overview/1";
    }

    @GetMapping("/coins/overview/{pageNo}")
    public String coinsoverviewpage (Model model, @PathVariable(name = "pageNo", required = false) Integer pageNo){
        Page page = muntCollectieService.findAllMuntCollectiesByPage(pageNo, "id");
        int totalPages = page.getTotalPages();
        if(pageNo <= 0){pageNo = 1;}
        if(pageNo > totalPages){pageNo = totalPages;}
        model.addAttribute("pageAmount", totalPages);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("coins", page.getContent());
        model.addAttribute("pageYes", true);
        if (page.getContent().size() == 0){
            model.addAttribute("message", "no.registered.coins");
        }
        return "coins-overview";
    }

    @GetMapping("/coins/overview/sort")
    public String coinsoverviewsort (Model model, @RequestParam(name = "sortmunt") String sortmunt){
        if (sortmunt.equals("titleASC")){
            model.addAttribute("coins", muntCollectieService.sortedListByTitle(true));
        }
        else if (sortmunt.equals("titleDESC")){
            model.addAttribute("coins", muntCollectieService.sortedListByTitle(false));
        }
        else if (sortmunt.equals("yearASC")){
            model.addAttribute("coins", muntCollectieService.sortedListByYear(true));
        }
        else if (sortmunt.equals("yearDESC")){
            model.addAttribute("coins", muntCollectieService.sortedListByYear(false));
        }
        return "coins-overview";
    }

    @GetMapping("/club/add")
    public String add(Club club) {
        return "add-club";
    }

    @PostMapping("/club/add")
    public String add(@Valid Club club, BindingResult result, Model model){
        if (result.hasErrors()) {
            return "add-club";
        }
        try {
            clubService.saveClub(club);
        } catch (DomainException exc){
            result.rejectValue("name", null, exc.getMessage());
            return "add-club";
        }
        return "redirect:/club/overview";
    }

    @GetMapping("/coins/add")
    public String add(MuntCollectie muntCollectie) {
        return "add-coincollection";
    }

    @PostMapping("/coins/add")
    public String add(@Valid MuntCollectie muntCollectie, BindingResult result, Model model){
        if (result.hasErrors()) {
            return "add-coincollection";
        }
        try {
            muntCollectieService.saveMuntCollectie(muntCollectie);
        } catch (DomainException exc){
            result.rejectValue("title", null, exc.getMessage());
            return "add-coincollection";
        }
        return "redirect:/coins/overview";
    }

    @GetMapping("/club/update/{id}")
    public String updateClub(@PathVariable("id") long id, Model model){
        try {
            Club club = clubService.findClub(id).orElseThrow(()->new IllegalArgumentException("club.not.exists"));
            model.addAttribute(club);
        }
        catch (IllegalArgumentException exc) {
            model.addAttribute("error", exc.getMessage());
            return "index";
        }
        return "update-club";
    }

    @PostMapping("/club/update/{id}")
    public String updateClub(@PathVariable("id") long id, @Valid Club club, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("ERRORS UPDATING");
            club.setId(id);
            model.addAttribute("club", club);
            return "update-club";
        }
        try {
            clubService.updateClub(club);
        } catch (DomainException exc){
            result.rejectValue("name", null , exc.getMessage());
            return "update-club";
        } catch (ServiceException e){
            result.rejectValue(e.getAction(), null, e.getMessage());
            return "update-club";
        }
        return "redirect:/club/overview";
    }

    @GetMapping("/club/delete/{id}")
    public String getDelete(@PathVariable("id") Long id, Model model){
        try {
            Club club = clubService.findClub(id).orElseThrow(()->new IllegalArgumentException("club.not.exists"));
            model.addAttribute("club", club);
        }
        catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "index";
        }
        return "delete-club";
    }


    @PostMapping("/club/delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model){
        try {
            clubService.deleteClubById(id);
        }
        catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return getDelete(id, model);
        }
        return "redirect:/club/overview";
    }

//    @GetMapping("/club/search")
//    public String zoekClub(Model model){
//        return "zoek-club";
//    }

    @GetMapping("/club/search/name")
    public String zoekClubOpNaam(@RequestParam("zoekNaam") String naam, Model model){
        if(naam.trim().isEmpty()){
            Page page = clubService.findAllClubs(1, "id");
            model.addAttribute("pageAmount", page.getTotalPages());
            model.addAttribute("currentPage", 1);
            model.addAttribute("error", "name.missing");
            model.addAttribute("clubs", page.getContent());
            model.addAttribute("pageYes", true);
            return "club-overview";
        }
        else{
            model.addAttribute("previousNameValue", naam);
            try {
                model.addAttribute("clubs", clubService.findAllClubsContaining(naam));
                if (clubService.findAllClubsContaining(naam).size() == 0){
                    model.addAttribute("message", "no.clubs.found");
                }
                return "club-overview";
            }catch (ServiceException e){
                model.addAttribute("error",e.getMessage());
                return "club-overview";
            }

        }
    }

    @GetMapping("/club/search/maxmembers")
    public String zoekClubOpMaxMembers(@RequestParam("zoekMaxMembers") String maxMembers, Model model){
        int mm = -1;
        try{
            mm = Integer.parseInt(maxMembers);
        } catch (NumberFormatException e){
            model.addAttribute("membererror", "not.a.number");
            Page page = clubService.findAllClubs(1, "id");
            model.addAttribute("pageAmount", page.getTotalPages());
            model.addAttribute("currentPage", 1);
            model.addAttribute("error", "name.missing");
            model.addAttribute("clubs", page.getContent());
            model.addAttribute("pageYes", true);
            return "club-overview";
        }
        model.addAttribute("previousMaxMembersValue", maxMembers);
        try {
            model.addAttribute("clubs", clubService.findAllByMaxMembers(mm, false));
            if (clubService.findAllByMaxMembers(mm, false).size() == 0){
                model.addAttribute("message", "no.clubs.found");
            }
            return "club-overview";
        }catch (ServiceException e){
            model.addAttribute("membererror",e.getMessage());
            return "club-overview";
        }

    }

    @GetMapping("/club/search/maxmembershigher")
    public String zoekClubOpMaxMembersEnHoger(@RequestParam("zoekMaxMembersEnHoger") String maxMembers, Model model){
        int mm = -1;
        try{
            mm = Integer.parseInt(maxMembers);
        } catch (NumberFormatException e){
            model.addAttribute("maxmembererror", "not.a.number");
            Page page = clubService.findAllClubs(1, "id");
            model.addAttribute("pageAmount", page.getTotalPages());
            model.addAttribute("currentPage", 1);
            model.addAttribute("error", "name.missing");
            model.addAttribute("clubs", page.getContent());
            model.addAttribute("pageYes", true);
            return "club-overview";
        }
        model.addAttribute("previousMaxMembersHigherValue", maxMembers);
        try {
            model.addAttribute("clubs", clubService.findAllByMaxMembers(mm, true));
            if (clubService.findAllByMaxMembers(mm, true).size() == 0){
                model.addAttribute("message", "no.clubs.found");
            }
            return "club-overview";
        }catch (ServiceException e){
            model.addAttribute("maxmembererror",e.getMessage());
            return "club-overview";
        }

    }


    @GetMapping("/coins/update/{id}")
    public String updateMuntCollectie(@PathVariable("id") long id, Model model){
        try {
            MuntCollectie muntCollectie = muntCollectieService.findCollection(id).orElseThrow(()->new IllegalArgumentException("coins.not.exist"));
            model.addAttribute(muntCollectie);
        }
        catch (IllegalArgumentException exc) {
            model.addAttribute("error", exc.getMessage());
            return "index";
        }
        return "update-coincollection";
    }

    @PostMapping("/coins/update/{id}")
    public String updateMuntCollectie(@PathVariable("id") long id, @Valid MuntCollectie muntCollectie, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("ERRORS UPDATING");
            muntCollectie.setId(id);
            model.addAttribute("muntCollectie", muntCollectie);
            return "update-coincollection";
        }
        try {
            muntCollectieService.updateMuntCollectie(muntCollectie);
        } catch (DomainException | ServiceException exc){
            result.rejectValue("title", null, exc.getMessage());
            return "update-coincollection";
        }
        return "redirect:/coins/overview";
    }

    @GetMapping("/coins/search/country")
    public String searchCoinsByCountry(@RequestParam("country") String country, Model model){
        if(country.trim().isEmpty()){
            model.addAttribute("error", "country.missing");
            model.addAttribute("coins", muntCollectieService.findAllMuntCollecties());
            return "redirect:/coins/overview";
        }
        else{
            model.addAttribute("previousCountryValue", country);
            try {
                model.addAttribute("coins", muntCollectieService.findAllByCountry(country));
                if (muntCollectieService.findAllByCountry(country).size() == 0){
                    model.addAttribute("message", "no.coins.found");
                }
                return "coins-overview";
            }catch (ServiceException e){
                model.addAttribute("error",e.getMessage());
                return "redirect:/coins/overview";
            }
        }
    }

    @GetMapping("/coins/search/year")
    public String searchCoinsByYear(@RequestParam("year") String year, Model model){
        int y = -1;
        try{
            y = Integer.parseInt(year);
        } catch (NumberFormatException e){
            model.addAttribute("yearerror", "not.a.number");
            model.addAttribute("coins", muntCollectieService.findAllMuntCollecties());
            return "redirect:/coins/overview";
        }
        model.addAttribute("previousYearValue", year);
        try {
            model.addAttribute("coins", muntCollectieService.findAllByYear(y, false));
            if (muntCollectieService.findAllByYear(y, false).size() == 0){
                model.addAttribute("message", "no.coins.found");
            }
            return "coins-overview";
        }catch (ServiceException e){
            model.addAttribute("yearerror",e.getMessage());
            return "redirect:/coins/overview";
        }
    }

    @GetMapping("/coins/search/untilyear")
    public String searchCoinsUntilYear(@RequestParam("endyear") String year, Model model){
        int y = -1;
        try{
            y = Integer.parseInt(year);
        } catch (NumberFormatException e){
            model.addAttribute("endyearerror", "not.a.number");
            model.addAttribute("coins", muntCollectieService.findAllMuntCollecties());
            return "redirect:/coins/overview";
        }
        model.addAttribute("previousUntilYearValue", year);
        try {
            model.addAttribute("coins", muntCollectieService.findAllByYear(y, true));
            if (muntCollectieService.findAllByYear(y, true).size() == 0){
                model.addAttribute("message", "no.coins.found");
            }
            return "coins-overview";
        }catch (ServiceException e){
            model.addAttribute("endyearerror",e.getMessage());
            return "redirect:/coins/overview";
        }
    }

    @GetMapping("/coins/delete/{id}")
    public String getCoinDelete(@PathVariable("id") Long id, Model model){
        try {
            MuntCollectie muntCollectie = muntCollectieService.findCollection(id).orElseThrow(() -> new IllegalArgumentException("coins.not.exist"));
            model.addAttribute("coin", muntCollectie);
        }
        catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "index";
        }
        return "delete-coins";
    }


    @PostMapping("/coins/delete/{id}")
    public String deleteCoin(@PathVariable("id") Long id, Model model){
        try {
            MuntCollectie muntCollectie = muntCollectieService.findCollection(id).orElseThrow(() -> new IllegalArgumentException("coins.not.exist"));
            muntCollectieService.deleteCollection(muntCollectie);
        }
        catch (IllegalArgumentException | ServiceException e){
            model.addAttribute("error", e.getMessage());
            return getCoinDelete(id, model);
        }
        return "redirect:/coins/overview";
    }

    @GetMapping("/clubhuis/overview")
    public String clubhuisOverview(Model model){
        try {
            List<Clubhuis> list = clubhuisService.findAll();
            if (list.size() == 0){
                model.addAttribute("message", "no.house.found");
            }
            model.addAttribute("clubhuizen", list);
        }
        catch (IllegalArgumentException | ServiceException e){
            model.addAttribute("error", e.getMessage());
        }
        return "clubhuis-overview";
    }

    @GetMapping("/clubhuis/add")
    public String clubhuisAddPage(Clubhuis clubhuis){
        return "clubhuis-add";
    }

    @PostMapping("/clubhuis/add")
    public String clubhuisAdd(@Valid Clubhuis clubhuis, BindingResult result, Model model){
        if (result.hasErrors()){
            return "clubhuis-add";
        }
        try {
            clubhuisService.saveClubhuis(clubhuis);
        } catch (DomainException exc){
            result.rejectValue("name", null, exc.getMessage());
            return "clubhuis-add";
        }
        return "redirect:/clubhuis/overview";
    }

    @GetMapping("/clubhuis/delete/{id}")
    public String getDeleteClubhuis(@PathVariable("id") Long id, Model model){
        try {
            Clubhuis clubhuis = clubhuisService.findClubhuis(id).orElseThrow(()->new IllegalArgumentException("clubhuis.not.exists.delete"));
            model.addAttribute("clubhuis", clubhuis);
        }
        catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return clubhuisOverview(model);
        }
        return "clubhuis-delete";
    }

    @PostMapping("/clubhuis/delete/{id}")
    public String deleteClubhuis(@PathVariable("id") Long id, Model model){
        try {
            clubhuisService.deleteById(id);
        }
        catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return getDeleteClubhuis(id, model);
        }
        return "redirect:/clubhuis/overview";
    }

    @GetMapping("/clubhuis/update/{id}")
    public String updateClubhuis(@PathVariable("id") long id, Model model){
        try {
            Clubhuis clubhuis = clubhuisService.findClubhuis(id).orElseThrow(()->new IllegalArgumentException("clubhuis.not.exists.update"));
            model.addAttribute(clubhuis);
        }
        catch (IllegalArgumentException exc) {
            model.addAttribute("error", exc.getMessage());
            return clubhuisOverview(model);
        }
        return "clubhuis-update";
    }

    @PostMapping("/clubhuis/update/{id}")
    public String updateClubhuis(@PathVariable("id") long id, @Valid Clubhuis clubhuis, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("ERRORS UPDATING");
            clubhuis.setId(id);
            model.addAttribute("clubhuis", clubhuis);
            return "clubhuis-update";
        }
        try {
            clubhuisService.updateHuis(clubhuis);
        } catch (DomainException exc){
            result.rejectValue("name", null , exc.getMessage());
            return "clubhuis-update";
        } catch (ServiceException e){
            result.rejectValue(e.getAction(), null, e.getMessage());
            return "clubhuis-update";
        }
        return "redirect:/clubhuis/overview";
    }

    @GetMapping("/clubhuis/filterPage")
    public String toFilterClubhuis(Model model){

            model.addAttribute("clean", false);
            //model.addAttribute("clubhuizen", new ArrayList<>());

        return "clubhuis-filter";
    }

    @GetMapping("/clubhuis/filter")
    public String filterClubhuis(Model model, @RequestParam(name = "From", required = false) String from, @RequestParam(name = "Until", required = false) String until){
        try {
            if (from.isEmpty() && until.isEmpty()){
                model.addAttribute("filtererror", "clubhuis.filter.error");
                toFilterClubhuis(model);
            }
            else if (from.isEmpty()){
                model.addAttribute("clean", true);
                List<Clubhuis> list = clubhuisService.findAllMaxMembersUntil(Integer.parseInt(until));
                if (list.size() == 0){
                    model.addAttribute("message", "no.house.found.filter");
                }
                model.addAttribute("clubhuizen", list);
                model.addAttribute("previousUntilValue", Integer.parseInt(until));
            }
            else if (until.isEmpty()){
                model.addAttribute("clean", true);
                List<Clubhuis> list = clubhuisService.findAllMaxMembersFrom(Integer.parseInt(from));
                if (list.size() == 0){
                    model.addAttribute("message", "no.house.found.filter");
                }
                model.addAttribute("clubhuizen", list);
                model.addAttribute("previousFromValue", Integer.parseInt(from));
            }
            else {
                model.addAttribute("clean", true);
                List<Clubhuis> list = clubhuisService.findAllMinAndMaxMembersFromUntil(Integer.parseInt(from), Integer.parseInt(until));
                if (list.size() == 0){
                    model.addAttribute("message", "no.house.found.filter");
                }
                model.addAttribute("clubhuizen", list);
                model.addAttribute("previousUntilValue", Integer.parseInt(until));
                model.addAttribute("previousFromValue", Integer.parseInt(from));
            }
        }
        catch (IllegalArgumentException | ServiceException e){
            model.addAttribute("error", e.getMessage());
        }
        return "clubhuis-filter";
    }


}