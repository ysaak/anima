package ysaak.anima;

import ysaak.anima.converter.ConverterService;
import ysaak.anima.exception.TechnicalException;
import ysaak.anima.service.ValidationService;

public interface IAnimaComponent {
    default ConverterService converters() {
        return SpringContextBean.getContext()
                .orElseThrow(() -> new TechnicalException("No application context available"))
                .getBean(ConverterService.class);
    }

    default ValidationService validator() {
        return SpringContextBean.getContext()
                .orElseThrow(() -> new TechnicalException("No application context available"))
                .getBean(ValidationService.class);
    }
}
