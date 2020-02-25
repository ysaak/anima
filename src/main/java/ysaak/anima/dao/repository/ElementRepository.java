package ysaak.anima.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ysaak.anima.config.ElementConstants;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementType;

import java.util.List;

@Repository
public interface ElementRepository extends PagingAndSortingRepository<Element, String> {

    @Query(
            value = "SELECT DISTINCT REGEXP_REPLACE(UPPER(SUBSTRING(ELEM_TITLE, 1, 1)), '[^A-Z]', '" + ElementConstants.NON_ALPHA_LETTER + "') FROM ELEMENT",
            nativeQuery = true
    )
    List<String> listUsedLetters();

    @Query(
            value = "SELECT e.* FROM element e WHERE elem_type = :#{#type.getDbCode()} AND UPPER(e.elem_title) REGEXP '^[^A-Z].*' ORDER BY e.elem_title",
            nativeQuery = true
    )
    List<Element> findAllByTypeAndFirstLetterNonAlpha(@Param("type") final ElementType type);

    @Query("SELECT e FROM Element e WHERE e.type = :type AND UPPER(e.title) LIKE CONCAT(:firstLetter, '%') ORDER BY e.title")
    List<Element> findAllByTypeAndFistLetterAlpha(@Param("type") final ElementType type, @Param("firstLetter") final String firstLetter);

    List<Element> findByTitleContainingIgnoreCaseOrderByTitle(String title);
}
