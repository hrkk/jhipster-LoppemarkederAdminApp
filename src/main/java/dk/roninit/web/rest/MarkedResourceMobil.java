package dk.roninit.web.rest;

import dk.roninit.domain.Marked;
import dk.roninit.domain.Organizer;
import dk.roninit.model.MarkedItemInstanceList;
import dk.roninit.model.MarkedItemView;
import dk.roninit.repository.MarkedRepository;
import dk.roninit.repository.OrganizerRepository;
import dk.roninit.service.MailService;
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
    private final MailService mailService;

    public MarkedResourceMobil(MarkedRepository markedRepository, OrganizerRepository organizerRepository, MailService mailService) {

        this.markedRepository = markedRepository;
        this.organizerRepository = organizerRepository;
        this.mailService = mailService;
    }

    @GetMapping("/markeds")
    public ResponseEntity<MarkedItemInstanceList> list() {
        List<Marked> all = markedRepository.findAll();

        List<MarkedItemView> views = all.stream().filter(e-> e.isEnableBooking()).map(e -> createMarkedItemView(e)).collect(Collectors.toList());
        views = views.stream().filter(e -> e.getToDate().isEqual(LocalDate.now()) || e.getToDate().isAfter(LocalDate.now()))
            .collect(Collectors.toList());

        MarkedItemInstanceList list = new MarkedItemInstanceList();
        list.setList(views);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(list));
    }

    @PostMapping("/markeds")
    public ResponseEntity<?> addMarked(@RequestBody MarkedItemView view) {

        if(view.getLatitude()==null || view.getLongitude()==null) {
            mailService.sendEmail("markedsbooking@gmail.com", "FRA NY PLATFORM - Nyt marked til MANUEL oprettelse - Adresse kan ikke findes automatisk!!!"+ view.getName(), mailContent(view, -1L), false, true);
            mailService.sendEmail("info@markedsbooking.dk", "FRA NY PLATFORM - Nyt marked til MANUEL oprettelse - Adresse kan ikke findes automatisk!!!"+ view.getName(), mailContent(view, -1L), false, true);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        Organizer organizer = new Organizer();
        organizer.setEmail(view.getOrganizerEmail());
        organizer.setName(danishCharFix(view.getOrganizerName()));

        Optional<Organizer> organizerO = organizerRepository.findAll().stream().filter(e -> e.getEmail().equalsIgnoreCase(organizer.getEmail())).findFirst();

        Organizer organizerSaved;
        organizerSaved = organizerO.orElseGet(() -> organizerRepository.saveAndFlush(organizer));

        Marked marked = new Marked();
        marked.setOrganizer(organizerSaved);
        marked.setAddress(danishCharFix(view.getAddress()));
        marked.setEnableBooking(true);
        marked.setEntreInfo(danishCharFix(view.getEntreInfo()));
        marked.setDateExtraInfo(danishCharFix(view.getAdditionalOpenTimePeriod()));
        marked.setFromDate(view.getFromDate());
        marked.setLatitude(view.getLatitude());
        marked.setLongitude(view.getLongitude());
        marked.setMarkedInformation(danishCharFix(view.getMarkedInformation()));
        marked.setMarkedRules(danishCharFix(view.getMarkedRules()));
        marked.setName(danishCharFix(view.getName()));
        marked.setToDate(view.getToDate());


        marked = markedRepository.saveAndFlush(marked);

        // new marked added
        mailService.sendEmail("info@markedsbooking.dk", "FRA NY PLATFORM - Nyt marked oprettet", mailContent(view, marked.getId()), false, true);
        mailService.sendEmail("markedsbooking@gmail.com", "FRA NY PLATFORM - Nyt marked oprettet", mailContent(view, marked.getId()), false, true);

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

    private String danishCharFix(String w) {
        if(w!= null) {
            return w.replaceAll("::OE::", "Ø").replaceAll("::AE::", "Æ").replaceAll("::AA::", "Å").replaceAll("::oe::", "ø").replaceAll("::ae::", "æ").replaceAll("::aa::", "å");
        }
        return w;
    }


    private String mailContent(MarkedItemView marked, Long id) {



        String content= "<table>\n" +
            "    <tr>\n" +
            "        <td colspan=\"2\">\n" +
            "        Marked <a href=\"http://roninit.dk/LoppemarkederAdminApp/coreMarkedItem/show/" +
                    + id +
            "        \">" +
            danishCharFix(marked.getName()) +
            "        </a> er oprettet fra mobil appen. Markedet er aktiveret og kan se i markeds oversigten.\n" +
            "        Hvis markedet ikke skal vises, skal du selv fjerne det fra listen ved at disable det og evt. sende mail til markeds opretteren.\n" +
            "        </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td colspan=\"2\"> </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td colspan=\"2\"><b>Markeds Arrangør</b></td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td>Navn</td>\n" +
            "        <td>" +
            danishCharFix( marked.getOrganizerName()) +
            "       </td>\n" +
            "    </tr>\n" +
            "\n" +
            "    <tr>\n" +
            "        <td>E-mail</td>\n" +
            "        <td>" +
                    marked.getOrganizerEmail() +
            "       </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td colspan=\"2\"><b>Markeds data</b></td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td>Markedsnavn</td>\n" +
            "        <td>" +
            danishCharFix( marked.getName()) +
            "       </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td>Adresse</td>\n" +
            "        <td>" +
            danishCharFix( marked.getAddress()) +
            "       </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td>Latitude</td>\n" +
            "        <td>" +
                        marked.getLatitude() +
            "        </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td>Longitude</td>\n" +
            "        <td>" +
            marked.getLongitude() +
            "        </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td>Fra dato</td>\n" +
            "        <td>" +
            marked.getFromDate() +
            "       </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td>Til dato</td>\n" +
            "        <td>" +
            marked.getToDate() +
            "       </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td>Evt. åbent tidsrum</td>\n" +
            "        <td>" +
            danishCharFix(marked.getAdditionalOpenTimePeriod()) +
            "       </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td>Entre pris</td>\n" +
            "        <td>" +
            danishCharFix(marked.getEntreInfo()) +
            "       </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td>Regler</td>\n" +
            "        <td>" +
            danishCharFix(marked.getMarkedRules() )+
            "   </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td>Markeds information</td>\n" +
            "        <td>" +
            danishCharFix(marked.getMarkedInformation()) +
            "</td>\n" +
            "    </tr>\n" +
            "</table>";
        return content;
    }
}
