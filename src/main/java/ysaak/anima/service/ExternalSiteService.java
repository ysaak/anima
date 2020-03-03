package ysaak.anima.service;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ysaak.anima.IAnimaComponent;
import ysaak.anima.dao.repository.ExternalSiteRepository;
import ysaak.anima.data.ExternalSite;
import ysaak.anima.exception.DataValidationException;
import ysaak.anima.exception.NoDataFoundException;
import ysaak.anima.exception.NotAllowedOperationException;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.service.validation.ValidationMessages;
import ysaak.anima.utils.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ExternalSiteService implements IAnimaComponent {

    private static final List<String> UNMODIFIABLE_CODE_LIST = Collections.singletonList("ANIDB");

    private final ExternalSiteRepository externalSiteRepository;
    private final TranslationService translationService;

    @Autowired
    public ExternalSiteService(ExternalSiteRepository externalSiteRepository, TranslationService translationService) {
        this.externalSiteRepository = externalSiteRepository;
        this.translationService = translationService;
    }

    public ExternalSite save(ExternalSite externalSite) throws DataValidationException {
        Preconditions.checkNotNull(externalSite);
        validator().validate(externalSite);

        Optional<ExternalSite> siteWithCode = externalSiteRepository.findByCode(externalSite.getCode());
        if (siteWithCode.isPresent()) {
            if (externalSite.getId() == null || !Objects.equals(externalSite.getId(), siteWithCode.get().getId())) {
                throw new DataValidationException(Collections.singletonMap("code", translationService.get(ValidationMessages.UNIQUENESS_KEY)));
            }
        }

        if (externalSite.getId() != null) {
            Optional<ExternalSite> originalSite = externalSiteRepository.findById(externalSite.getId());

            if (originalSite.isPresent() && UNMODIFIABLE_CODE_LIST.contains(originalSite.get().getCode())) {
                externalSite.setCode(originalSite.get().getCode());
            }
        }

        return externalSiteRepository.save(externalSite);
    }

    public ExternalSite findById(String externalSiteId) throws NoDataFoundException {
        return externalSiteRepository.findById(externalSiteId)
                .orElseThrow(() -> new NoDataFoundException("No external site found with id " + externalSiteId));
    }

    public List<ExternalSite> findAll() {
        return CollectionUtils.toList(
                externalSiteRepository.findAll(Sort.by("siteName"))
        );
    }

    public void delete(String externalSiteId) throws NoDataFoundException, NotAllowedOperationException {
        ExternalSite siteToDelete = findById(externalSiteId);

        if (UNMODIFIABLE_CODE_LIST.contains(siteToDelete.getCode())) {
            throw new NotAllowedOperationException("Cannot delete external site with code " + siteToDelete.getCode());
        }

        externalSiteRepository.delete(siteToDelete);
    }
}
