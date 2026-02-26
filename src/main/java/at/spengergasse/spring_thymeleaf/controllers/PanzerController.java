package at.spengergasse.spring_thymeleaf.controllers;


import at.spengergasse.spring_thymeleaf.entities.Panzer;
import at.spengergasse.spring_thymeleaf.entities.PanzerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/panzer")
public class PanzerController {
    private final PanzerRepository panzerRepository;

    public PanzerController(PanzerRepository panzerRepository) {
        this.panzerRepository = panzerRepository;

    }

    @GetMapping("/list")
    public String panzer(Model model) {
        model.addAttribute("panzer", panzerRepository.findAll());
        return "panzerlist";
    }

    @GetMapping("/add")
    public String addPanzer(Model model) {
        model.addAttribute("panzer", new Panzer());
        return "add_panzer";
        // Hier wird ein neues Panzer Objekt erstellt und an das Model übergeben
    }
    @PostMapping("/add")
    public String addPanzer(@ModelAttribute("panzer") Panzer panzer) {
        panzerRepository.save(panzer);
        return  "redirect:/panzer/list";
        // Hier wird der Panzer gespreichert
    }

    @GetMapping("/delete")
    public String deletePanzer(int id) {
        panzerRepository.deleteById(id);
        return "redirect:/panzer/list";
        // Hier wird die Änderung vorgenommen, um die ID als Parameter zu akzeptieren und den Panzer zu löschen
    }
    @GetMapping("/edit")
    public String editPanzer( Model model,@RequestParam int id) {
        Panzer p = panzerRepository.findById(id).get();
        model.addAttribute("panzer", p);
        return "add_panzer";
        // Hier wird die Änderung vergenommen, um die ID als Parameter zu akzeptieren, den Panzer zu finden und Daten zu ändern
    }

}
