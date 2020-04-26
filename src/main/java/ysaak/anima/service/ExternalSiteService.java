package ysaak.anima.service;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ysaak.anima.IAnimaComponent;
import ysaak.anima.dao.repository.ExternalSiteRepository;
import ysaak.anima.data.ElementRemoteId;
import ysaak.anima.data.ExternalSite;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.exception.error.ExternalSiteErrorCode;
import ysaak.anima.rules.ExternalSiteRules;
import ysaak.anima.service.importer.anidb.AnidbConstants;
import ysaak.anima.utils.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class ExternalSiteService implements IAnimaComponent {

    private static final List<String> UNMODIFIABLE_CODE_LIST = Collections.singletonList(
            AnidbConstants.ANIDB_SITE_CODE
    );

    private final ExternalSiteRepository externalSiteRepository;

    @Autowired
    public ExternalSiteService(ExternalSiteRepository externalSiteRepository) {
        this.externalSiteRepository = externalSiteRepository;
    }

    public ExternalSite save(ExternalSite externalSite) throws FunctionalException {
        Preconditions.checkNotNull(externalSite);

        ExternalSiteRules.validate(externalSite);

        Optional<ExternalSite> siteWithCode = externalSiteRepository.findByCode(externalSite.getCode());
        if (siteWithCode.isPresent()) {
            if (externalSite.getId() == null || !Objects.equals(externalSite.getId(), siteWithCode.get().getId())) {
                throw ExternalSiteErrorCode.VALIDATE_CODE_UNIQUENESS.functional();
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

    public ExternalSite findById(final String id) throws FunctionalException {
        return externalSiteRepository.findById(id)
                .orElseThrow(() -> ExternalSiteErrorCode.NOT_FOUND_BY_ID.functional(id));
    }

    public ExternalSite findByCode(final String code) throws FunctionalException {
        return externalSiteRepository.findByCode(code)
                .orElseThrow(() -> ExternalSiteErrorCode.NOT_FOUND_BY_CODE.functional(code));
    }

    public List<ExternalSite> findAll() {
        return CollectionUtils.toList(
                externalSiteRepository.findAll(Sort.by("siteName"))
        );
    }

    public void delete(String externalSiteId) throws FunctionalException {
        ExternalSite siteToDelete = findById(externalSiteId);

        if (UNMODIFIABLE_CODE_LIST.contains(siteToDelete.getCode())) {
            throw ExternalSiteErrorCode.CANNOT_DELETE_SITE.functional();
        }

        externalSiteRepository.delete(siteToDelete);
    }

    public Map<String, String> getUrlForIdList(final String siteCode, final List<String> idList) {
        final Optional<ExternalSite> site = externalSiteRepository.findByCode(siteCode);

        final Map<String, String> urlMap = new HashMap<>();

        if (site.isPresent() && CollectionUtils.isNotEmpty(idList)) {
            String urlTemplate = site.get().getUrlTemplate();

            for (String id : idList) {
                urlMap.put(id, String.format(urlTemplate, id));
            }
        }

        return urlMap;
    }

    public Map<String, String> findElementIdFromSiteAndRemoteId(final String siteCode, final List<String> idList) {
        final Optional<ExternalSite> site = externalSiteRepository.findByCode(siteCode);

        final Map<String, String> elementMap = new HashMap<>();

        if (site.isPresent()) {
            final List<ElementRemoteId> remoteIdList = externalSiteRepository.findElementForSite(site.get().getId(), idList);

            for (ElementRemoteId remoteId : remoteIdList) {
                elementMap.put(remoteId.getRemoteId(), remoteId.getElement().getId());
            }
        }

        return elementMap;
    }
}
