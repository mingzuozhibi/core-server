package mingzuozhibi.coreserver.commons.support.page;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PageParamsResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(PageParams.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        var request = webRequest.getNativeRequest(HttpServletRequest.class);
        assert request != null;
        var page = parameter.getParameterAnnotation(Page.class);
        var pageParams = new PageParams();

        Optional.ofNullable(request.getParameter("page"))
            .map(Integer::valueOf)
            .or(() -> Optional.ofNullable(page).map(Page::page))
            .ifPresent(pageParams::setPage);

        Optional.ofNullable(request.getParameter("size"))
            .map(Integer::valueOf)
            .or(() -> Optional.ofNullable(page).map(Page::size))
            .ifPresent(pageParams::setSize);

        var sorts = Optional.ofNullable(request.getParameterValues("sort"))
            .or(() -> Optional.ofNullable(page).map(Page::sort))
            .orElse(new String[0]);
        var orders = Arrays.stream(sorts)
            .map(this::parseOrder)
            .collect(Collectors.toList());
        pageParams.setSort(Sort.by(orders));

        return pageParams;
    }

    private Pattern pattern = Pattern.compile("^" +
        "(?<Property>[^,]+)" +
        "(,(?<Direction>ASC|DESC))?" +
        "$", Pattern.CASE_INSENSITIVE);

    private Order parseOrder(String string) {
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            Order order = Order.by(matcher.group("Property"));

            var direction = Optional.ofNullable(matcher.group("Direction"))
                .map(String::toUpperCase)
                .map(Direction::valueOf);
            if (direction.isPresent()) {
                order = order.with(direction.get());
            }

            return order;
        }
        throw new IllegalArgumentException("非法的sort参数: " + string);
    }

}
