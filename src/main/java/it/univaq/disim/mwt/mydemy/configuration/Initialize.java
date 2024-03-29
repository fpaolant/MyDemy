package it.univaq.disim.mwt.mydemy.configuration;


import javax.transaction.Transactional;

import it.univaq.disim.mwt.mydemy.business.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class Initialize implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    AdminUtenteService utenteService;
    @Autowired
    RuoloService ruoloService;
    @Autowired
    WebLinkService weblinkService;
    @Autowired
    CategoriaService categoriaService;
    @Autowired
    CorsoService corsoService;
    @Autowired
    TagService tagService;
    @Autowired
    IscrizioneService iscrizioneService;
    @Autowired
    RecensioneService recensioneService;
    @Autowired
    CreatoreInfoService creatoreInfoService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {


        /*
        // insert dummy data
        // String nome, @NotBlank String cognome, @Email String email, @NotBlank String username,@NotBlank String password, Boolean enabled, Set<Ruolo> ruoli
        Ruolo adminRole = new Ruolo("admin", "ADMIN", "amministratore del sistema");
        Ruolo creatorRole = new Ruolo("creator", "CREATOR", "creatore di corsi");
        ruoloService.create(adminRole);
        ruoloService.create(creatorRole);

        // set admin default
        Utente fabio = new Utente("Fabio", "Paolantonio", "fabiopaolantonio@gmail.com", "admin", "admin", true);
        fabio.setCreatoreInfo(new CreatoreInfo("Software Developer", ""));
        fabio.addRuolo(adminRole);
        fabio.addRuolo(creatorRole);
        utenteService.create(fabio);

        // set other users
        Utente marco = new Utente("Marco", "Giarrusso", "marco.giarrusso@univaq.it", "magiar", "magiar", true);
        utenteService.create(marco);

        Utente mario = new Utente("Mario", "Rossi", "fabiopaolantonio@gmail.com", "mario", "mario", true);
        mario.setCreatoreInfo(new CreatoreInfo("Finance Director", ""));
        mario.addRuolo(adminRole);
        mario.addRuolo(creatorRole);
        utenteService.create(mario);

        // create corso
        Categoria cat = new Categoria("senza categoria", null);
        categoriaService.create(cat);

        Categoria cat2 = new Categoria("Informatica", null);
        categoriaService.create(cat2);

        Categoria cat21 = new Categoria("Sviluppo", cat2);
        categoriaService.create(cat21);


        Categoria cat211 = new Categoria("Java", cat21);
        categoriaService.create(cat211);

        Categoria cat4 = new Categoria("Economia", null);
        categoriaService.create(cat4);

        Tag tag = new Tag("java");
        tagService.create(tag);


        // corso 1 java spring
        Corso corso = new Corso("Java spring boot",
                "",
                LocalDateTime.of(2023, 9, 27, 12, 30),
                LocalDateTime.of(2023, 9, 30, 12, 30),
                10, 12, 1000.5f,
                true, mario, 15, true);

        corso.setDescrizione("Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.");
        corso.setLiveUrl("https://www.youtube.com/embed/9SGDpanrc8U");
        corsoService.create(corso);

        corso.addWebLink(new WebLink("http://www.google.com", "google"));
        corso.addWebLink(new WebLink("http://www.youtube.com", "youtube"));
        corso.addTag(tag);
        corso.addCategoria(cat211);
        try {
            corsoService.update(corso);
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }

        // corso 2 java
        Corso corso2 = new Corso("Java introduzione",
                "",
                LocalDateTime.of(2023, 9, 29, 12, 30),
                LocalDateTime.of(2023, 9, 30, 12, 30),
                10, 12, 1000.5f,
                true, fabio, 20, true);

        corso2.setDescrizione("Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.");
        corso2.setLiveUrl("https://www.youtube.com/embed/hfl3n1RqLQE");
        corsoService.create(corso2);
        corso2.addWebLink(new WebLink("http://www.youtube.com", "youtube1"));
        corso2.addTag(tag);
        corso2.addCategoria(cat211);
        try {
            corsoService.update(corso2);
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }


        // corso 3 C++
        Corso corso3 = new Corso("C++",
                "",
                LocalDateTime.of(2023, 9, 9, 12, 30),
                LocalDateTime.of(2023, 9, 20, 12, 30),
                10, 12, 1000.5f,
                true, fabio, 20, true);

        corso3.setDescrizione("Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.");
        corso3.setLiveUrl("https://www.youtube.com/embed/32jPsN3C9cw");
        corsoService.create(corso3);
        corso3.addCategoria(cat21);
        try {
            corsoService.update(corso3);
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }


        // corso 4 C#
        Corso corso4 = new Corso("C#",
                "",
                LocalDateTime.of(2023, 9, 18, 12, 30),
                LocalDateTime.of(2023, 9, 30, 12, 30),
                10, 12, 1000.5f,
                true, mario, 20, true);

        corso4.setDescrizione("Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.Lorem ipsum represents a long-held tradition for\n"
                + "												designers, typographers and the like. Some people hate it\n"
                + "												and argue for its demise, but others ignore.");
        corso4.setLiveUrl("https://www.youtube.com/embed/5OzKY_thSJU");
        corsoService.create(corso4);
        corso4.addCategoria(cat21);
        try {
            corsoService.update(corso4);
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }


        // iscrizioni
        Iscrizione is = new Iscrizione();
        is.setCorso(corso3);
        is.setSuperato(false);
        is.setUtente(marco);
        is.setData(LocalDateTime.of(2023, 9, 29, 12, 30));
        iscrizioneService.create(is);

        Iscrizione is0 = new Iscrizione();
        is0.setCorso(corso4);
        is0.setSuperato(true);
        is0.setUtente(marco);
        is0.setData(LocalDateTime.of(2023, 11, 29, 12, 30));
        iscrizioneService.create(is0);


        Iscrizione is1 = new Iscrizione();
        is1.setCorso(corso4);
        is1.setSuperato(true);
        is1.setUtente(fabio);
        is1.setData(LocalDateTime.of(2023, 9, 21, 12, 30));
        iscrizioneService.create(is1);

        //recensioneService.create(new Recensione("Ottimo", "corso perfetto", 2, LocalDateTime.now(), marco.getUsername(), marco.getId(), corso4.getId()));
        //recensioneService.create(new Recensione("Ottimo", "corso perfetto", 2, LocalDateTime.now(), "mario", new Long(3), new Long(1)));
*/


    }


}
