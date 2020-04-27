package mingzuozhibi.coreserver.modules.disc;

import mingzuozhibi.coreserver.commons.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiscController extends BaseController {

    @Autowired
    private DiscRepository discRepository;

    @GetMapping("/api/discs")
    public String findAll(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "50") int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        return objectResult(discRepository.findAll(pageRequest));
    }

    @GetMapping("/api/discs/{id}")
    public String findById(@PathVariable Long id) {
        return discRepository.findById(id, this::objectResult);
    }

    @GetMapping("/api/discs/findByAsin/{asin}")
    public String findByAsin(@PathVariable String asin) {
        return discRepository.findByAsin(asin, this::objectResult);
    }

    @GetMapping("/api/discs/findByTitleContains/{title}")
    public String findByTitleContains(@PathVariable String title,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "50") int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        return objectResult(discRepository.findByTitleContains(title, pageRequest));
    }

}
