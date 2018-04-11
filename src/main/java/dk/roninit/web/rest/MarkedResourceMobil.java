package dk.roninit.web.rest;

import dk.roninit.domain.Marked;
import dk.roninit.domain.Organizer;
import dk.roninit.model.MarkedItemInstanceList;
import dk.roninit.model.MarkedItemView;
import dk.roninit.repository.MarkedRepository;
import dk.roninit.repository.OrganizerRepository;
import io.github.jhipster.web.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mobile")
public class MarkedResourceMobil {

    private final MarkedRepository markedRepository;
    private final OrganizerRepository organizerRepository;

    public MarkedResourceMobil(MarkedRepository markedRepository, OrganizerRepository organizerRepository) {

        this.markedRepository = markedRepository;
        this.organizerRepository = organizerRepository;
    }

    @GetMapping("/markeds")
    public ResponseEntity<MarkedItemInstanceList> list() {
        List<Marked> all = markedRepository.findAll();

        //MarkedItemView
//        if(!all.isEmpty()) {
//            return "Hello "+all +" " +all.get(0).getOrganizer();
//        }

        List<MarkedItemView> views = all.stream().map(e -> createMarkedItemView(e)).collect(Collectors.toList());

        MarkedItemInstanceList list = new MarkedItemInstanceList();
        list.setList(views);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(list));
    }

    @PostMapping("/markeds")
    public ResponseEntity<?> addMarked(@RequestBody MarkedItemView view) {

        Organizer organizer = new Organizer();
        organizer.setEmail(view.getOrganizerEmail());
        organizer.setName(view.getOrganizerName());

        Organizer organizerSaved = organizerRepository.saveAndFlush(organizer);

        Marked marked = new Marked();
        marked.setOrganizer(organizerSaved);
        marked.setAddress(view.getAddress());
        marked.setEnableBooking(true);
        marked.setEntreInfo(view.getEntreInfo());
        marked.setDateExtraInfo(view.getAdditionalOpenTimePeriod());
        marked.setFromDate(view.getFromDate());
        marked.setLatitude(view.getLatitude());
        marked.setLongitude(view.getLongitude());
        marked.setMarkedInformation(view.getMarkedInformation());
        marked.setMarkedRules(view.getMarkedRules());
        marked.setName(view.getName());
        marked.setToDate(view.getToDate());


        markedRepository.saveAndFlush(marked);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private MarkedItemView createMarkedItemView(Marked m) {
        MarkedItemView view = new MarkedItemView();
        view.setId(m.getId());
        view.setAdditionalOpenTimePeriod(m.getDateExtraInfo());
        view.setAddress(m.getAddress());
        view.setEntreInfo(m.getEntreInfo());
        view.setFromDate(m.getFromDate());
        view.setToDate(m.getToDate());
        view.setLatitude(m.getLatitude());
        view.setLongitude(m.getLongitude());
        view.setMarkedInformation(m.getMarkedInformation());
        view.setMarkedRules(m.getMarkedRules());
        view.setName(m.getName());
        view.setOrganizerName(m.getOrganizer().getName());
        view.setOrganizerEmail(m.getOrganizer().getEmail());
        return view;
    }

//    private def fixLngLat(def tal) {
//        if(tal != null && tal != 0.0 && tal > 1000)
//            return tal/1000000
//        return tal
//    }
}
