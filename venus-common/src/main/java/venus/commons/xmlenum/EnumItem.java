package venus.commons.xmlenum;

import java.io.Serializable;

/**
 * Enum name value bean
 */
public class EnumItem implements Serializable {
// ------------------------------ FIELDS ------------------------------

    public static EnumItem EMPTY_ITEM = new EnumItem("", "-1");
    static final long serialVersionUID = -6048843897105277055L;

    protected String label;
    protected String value;

// --------------------------- CONSTRUCTORS ---------------------------

    public EnumItem() {
    }

    public EnumItem(String label, String value) {
        this.label = label;
        this.value = value;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

// ------------------------ CANONICAL METHODS ------------------------

    public boolean equals(Object obj) {
        if (obj instanceof String) {
            if (getValue() == null)
                return false;

            return getValue().equals(obj);
        }

        return super.equals(obj);
    }

    public String toString() {
        return getLabel();
    }
}

