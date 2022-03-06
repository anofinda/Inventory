package System;

import java.util.Objects;

class Good {
    String itemNumber, supplier, description;
    Good(String num, String sup, String des) {
        itemNumber = num;
        supplier = sup;
        description = des;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Good good = (Good) o;
        return Objects.equals(itemNumber, good.itemNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemNumber);
    }
}
