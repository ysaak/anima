package ysaak.anima.view.dto.elements;

import ysaak.anima.data.RelationType;

public class RelationAddDto {
    private String elementId;
    private String relatedElementId;
    private RelationType type;

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getRelatedElementId() {
        return relatedElementId;
    }

    public void setRelatedElementId(String relatedElementId) {
        this.relatedElementId = relatedElementId;
    }

    public RelationType getType() {
        return type;
    }

    public void setType(RelationType type) {
        this.type = type;
    }
}
