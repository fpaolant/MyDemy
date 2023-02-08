package it.univaq.disim.mwt.mydemy.configuration;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import it.univaq.disim.mwt.mydemy.business.*;
import it.univaq.disim.mwt.mydemy.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import it.univaq.disim.mwt.mydemy.domain.CreatoreInfo;

@Component
public class Initialize implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    UtenteBO utenteService;
    @Autowired
    RuoloBO ruoloService;
    @Autowired
    WebLinkBO weblinkService;
    @Autowired
    CategoriaBO categoriaService;
    @Autowired
    CorsoBO corsoService;
    @Autowired
    TagBo tagService;
    @Autowired
    IscrizioneBO iscrizioneService;
    @Autowired
    CreatoreInfoBO creatoreInfoService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // insert dummy data
        // String nome, @NotBlank String cognome, @Email String email, @NotBlank String username,@NotBlank String password, Boolean enabled, Set<Ruolo> ruoli
        Ruolo adminRole = new Ruolo("admin", "ADMIN", "amministratore del sistema");
        Ruolo userRole = new Ruolo("user", "USER", "utente del sistema");
        Ruolo creatorRole = new Ruolo("creator", "CREATOR", "creatore di corsi");
        ruoloService.save(adminRole);
        ruoloService.save(userRole);
        ruoloService.save(creatorRole);

        // set admin default
        Utente u = new Utente("Fabio", "Paolantonio", "fabiopaolantonio@gmail.com", "admin", passwordEncoder.encode("admin"), true);
        u.setCreatoreInfo(new CreatoreInfo("Software Developer", ""));
        u.addRuolo(adminRole);
        u.addRuolo(userRole);
        u.addRuolo(creatorRole);
        utenteService.save(u);

        // set other users
        Utente marco = new Utente("Marco", "Giarrusso", "marco.giarrusso@univaq.it", "magiar", passwordEncoder.encode("magiar"), true);
        marco.addRuolo(userRole);
        utenteService.save(marco);

        Utente a = new Utente("Antonio", "Paolo", "fabiopaolantonio@gmail.com", "antpaol", passwordEncoder.encode("antpaol"), true);
        a.setCreatoreInfo(new CreatoreInfo("Software Developer", ""));
        a.addRuolo(adminRole);
        a.addRuolo(userRole);
        a.addRuolo(creatorRole);
        utenteService.save(a);

        // create corso
        Categoria cat = new Categoria("senza categoria", null, "fas fa-th");
        categoriaService.save(cat);

        Categoria cat2 = new Categoria("informatica", null, "fas fa-desktop");
        categoriaService.save(cat2);


        Categoria cat3 = new Categoria("java", cat2, "fas fa-coffee");
        categoriaService.save(cat3);

        Categoria cat4 = new Categoria("Economia", null, "fas fa-dollar-sign");
        categoriaService.save(cat4);

        Tag tag = new Tag("java");
        tagService.save(tag);


        // corso 1 java spring
        Corso corso = new Corso("Java spring boot",
                "",
                LocalDateTime.of(2023, 9, 29, 12, 30),
                LocalDateTime.of(2023, 9, 30, 12, 30),
                10, 12, 1000.5f,
                true, a, 15, true);

        corso.setDescrizione("Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.");
        corsoService.save(corso);

        corso.addWebLink(new WebLink("http://www.google.com", "google"));
        corso.addWebLink(new WebLink("http://www.youtube.com", "youtube"));
        corso.addTag(tag);
        corso.addCategoria(cat3);
        corsoService.save(corso);

        // corso 2 java
        Corso corso2 = new Corso("Java introduzione",
                "",
                LocalDateTime.of(2023, 9, 29, 12, 30),
                LocalDateTime.of(2023, 9, 30, 12, 30),
                10, 12, 1000.5f,
                true, u, 20, true);

        corso2.setDescrizione("Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.");
        corsoService.save(corso2);
        corso2.addWebLink(new WebLink("http://www.youtube.com", "youtube1"));
        corso2.addTag(tag);
        corso2.addCategoria(cat3);
        corsoService.save(corso2);


        // corso 3 C++
        Corso corso3 = new Corso("C++",
                "",
                LocalDateTime.of(2023, 9, 29, 12, 30),
                LocalDateTime.of(2023, 9, 30, 12, 30),
                10, 12, 1000.5f,
                true, u, 20, true);

        corso3.setDescrizione("Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.");
        corsoService.save(corso3);
        corso3.addCategoria(cat3);
        corsoService.save(corso3);




        // corso 4 C#
        Corso corso4 = new Corso("C#",
                "",
                LocalDateTime.of(2023, 9, 29, 12, 30),
                LocalDateTime.of(2023, 9, 30, 12, 30),
                10, 12, 1000.5f,
                true, u, 20, false);

        corso3.setDescrizione("Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.");
        corsoService.save(corso4);
        corso4.addCategoria(cat3);
        corsoService.save(corso4);


        // iscrizioni
        Iscrizione is = new Iscrizione();
        is.setCorso(corso3);
        is.setSuperato(false);
        is.setUtente(u);
        is.setData(LocalDateTime.of(2023, 9, 29, 12, 30));
        iscrizioneService.save(is);

    }


}
