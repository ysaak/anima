package ysaak.anima;

import ysaak.anima.converter.ConverterService;
import ysaak.anima.exception.TechnicalException;

public interface IAnimaComponent {
    default ConverterService converters() {
        return SpringContextBean.getContext()
                .orElseThrow(() -> new TechnicalException("No application context available"))
                .getBean(ConverterService.class);
    }
}
