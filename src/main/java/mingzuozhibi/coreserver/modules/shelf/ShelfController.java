package mingzuozhibi.coreserver.modules.shelf;

import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.commons.support.page.PageDefault;
import mingzuozhibi.coreserver.commons.support.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShelfController extends BaseController {

    @Autowired
    private ShelfRepository shelfRepository;

    @GetMapping("/api/shelfs")
    public String findAll(@PageDefault(size = 20) PageParams params) {
        return objectResult(shelfRepository.findAll(params.toPageable()));
    }

}
