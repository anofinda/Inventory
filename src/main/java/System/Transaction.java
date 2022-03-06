package System;

import java.util.Arrays;

class Transaction implements Comparable<Transaction> {
    String[] strings;

    Transaction(String[] strs) {
        strings = strs;
    }

    @Override
    public int compareTo(Transaction transaction) {
        if (this.equals(transaction)) {
            return 0;
        }
        if ("A".equals(this.strings[0]) || "R".equals(this.strings[0])) {
            if ("D".equals(transaction.strings[0]) || "O".equals(transaction.strings[0])) {
                return -1;
            } else {
                if ("A".equals(this.strings[0])) {
                    return -1;
                }
            }
        } else {
            if ("O".equals(transaction.strings[0])&&"O".equals(this.strings[0])) {
                return Integer.valueOf(this.strings[2]).compareTo(Integer.valueOf(transaction.strings[2s]));
            }
        }
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transaction that = (Transaction) o;
        return Arrays.equals(strings, that.strings);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(strings);
    }
}
