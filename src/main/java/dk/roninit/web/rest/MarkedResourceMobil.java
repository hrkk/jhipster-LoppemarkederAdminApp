package dk.roninit.web.rest;

import dk.roninit.domain.Marked;
import dk.roninit.model.MarkedItemView;
import dk.roninit.repository.MarkedRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mobile")
public class MarkedResourceMobil {

    private final MarkedRepository markedRepository;

    public MarkedResourceMobil(MarkedRepository markedRepository) {
        this.markedRepository = markedRepository;
    }

    @GetMapping("/markeds")
    public List<MarkedItemView> list() {
        List<Marked> all = markedRepository.findAll();

        //MarkedItemView
//        if(!all.isEmpty()) {
//            return "Hello "+all +" " +all.get(0).getOrganizer();
//        }

        List<MarkedItemView> views = all.stream().map(e -> createMarkedItemView(e)).collect(Collectors.toList());
        return views;
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
