package mingzuozhibi.coreserver.modules.disc;

import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.commons.support.ReflectUtils;
import mingzuozhibi.coreserver.commons.support.page.PageDefault;
import mingzuozhibi.coreserver.commons.support.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiscController extends BaseController {

    @Autowired
    private DiscRepository discRepository;

    @Autowired
    private ConversionService conversionService;

    @GetMapping("/api/discs")
    public String findAll(PageParams params) {
        return objectResult(discRepository.findAll(params.toPageable()));
    }

    @GetMapping("/api/discs/{id}")
    public String findById(@PathVariable Long id) {
        return discRepository.findById(id, this::objectResult);
    }

    @GetMapping("/api/discs/find/asin/{asin}")
    public String findByAsin(@PathVariable String asin) {
        return discRepository.findByAsin(asin, this::objectResult);
    }

    @GetMapping("/api/discs/find/title/contains/{title}")
    public String findByTitleContains(@PathVariable String title,
                                      @PageDefault("title") PageParams params) {
        return objectResult(discRepository.findByTitleContains(title, params.toPageable()));
    }

    @GetMapping("/api/discs/find/titleCN/contains/{titleCN}")
    public String findByTitleCNContains(@PathVariable String titleCN,
                                        @PageDefault("titleCN") PageParams params) {
        return objectResult(discRepository.findByTitleCNContains(titleCN, params.toPageable()));
    }

    @GetMapping("/api/discs/find/title/specification/{title}")
    public String findByTitleSpecification(@PathVariable String title, PageParams params) {
        Page<Disc> discs = discRepository.findAll((Specification<Disc>)
            (root, query, builder) -> query.where(
                builder.or(
                    builder.like(root.get("title"), "%" + title + "%"),
                    builder.like(root.get("titleCN"), "%" + title + "%")
                )
            ).getRestriction(), params.toPageable());
        return objectResult(discs);
    }

    @GetMapping("/api/discs/find/specification/{key}/{value}")
    public String findBySpecification(@PathVariable String key,
                                      @PathVariable String value,
                                      PageParams params) {
        Class<?> fieldClass = ReflectUtils.getFieldClass(Disc.class, key);
        Page<Disc> discs = discRepository.findAll((Specification<Disc>)
            (root, query, builder) -> query.where(
                builder.equal(root.get(key), conversionService.convert(value, fieldClass))
            ).getRestriction(), params.toPageable());
        return objectResult(discs);
    }

}
