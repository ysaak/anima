package ysaak.anima.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ysaak.anima.service.importer.AnidbImportService;

@Controller
public class HomeController {

    private final AnidbImportService anidbImportService;

    @Autowired
    public HomeController(AnidbImportService anidbImportService) {
        this.anidbImportService = anidbImportService;
    }

    @GetMapping("/")
    public String indexAction(ModelMap model) {
        return "index";
    }


    @GetMapping("/testimport")
    public String testImportAction(ModelMap model) throws Exception {

        anidbImportService.importEntity("1234");

        return "index";
    }
}
