package ysaak.anima.view.dto.admin.importer;

public class TagEquivalenceEditDto {
    private String[] tagId;
    private String[] equivalence;

    public String[] getTagId() {
        return tagId;
    }

    public void setTagId(String[] tagId) {
        this.tagId = tagId;
    }

    public String[] getEquivalence() {
        return equivalence;
    }

    public void setEquivalence(String[] equivalence) {
        this.equivalence = equivalence;
    }
}
