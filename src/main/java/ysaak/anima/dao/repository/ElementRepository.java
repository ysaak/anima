package ysaak.anima.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ysaak.anima.config.ElementConstants;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementCollectionCount;
import ysaak.anima.data.ElementTagCount;
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

    @Query("SELECT e.type as elementType, cl.id as collectionId, count(1) as count FROM Element as e JOIN e.collectionList as cl GROUP BY e.type, cl.id")
    List<ElementCollectionCount> countElementTypeByCollection();

    @Query(
        value = "SELECT e.* FROM element e INNER JOIN L_ELEMENT_COLLECTION lec ON lec.ELEMENT_ID = e.ELEM_ID WHERE lec.COLLECTION_ID = :collectionId",
        nativeQuery = true
    )
    List<Element> findByCollectionId(@Param("collectionId") String collectionId);

    @Query("SELECT e.type as elementType, tl.id as tagId, count(1) as count FROM Element as e JOIN e.tagList as tl GROUP BY e.type, tl.id")
    List<ElementTagCount> countElementTypeByTag();

    @Query(
        value = "SELECT e.* FROM element e INNER JOIN L_ELEMENT_TAG let ON let.ELEMENT_ID = e.ELEM_ID WHERE let.TAG_ID = :tagId",
        nativeQuery = true
    )
    List<Element> findByTagId(@Param("tagId") String tagId);
}
