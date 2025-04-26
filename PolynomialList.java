package polynomialLists;

import polynomial.Polynomial;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PolynomialList {
    private final List<Polynomial> polynomials;

    public PolynomialList() {
        this.polynomials = new ArrayList<>();
    }

    public void add(Polynomial polynomial) {
        polynomials.add(polynomial);
    }

    public void deleteById(int id) {
        Iterator<Polynomial> iterator = polynomials.iterator();
        while (iterator.hasNext()) {
            Polynomial polynomial = iterator.next();
            if (polynomial.getID() == id) {
                iterator.remove();
                break;
            }
        }
    }

    public Polynomial getById(int id) {
        for (Polynomial polynomial : polynomials) {
            if (polynomial.getID() == id) {
                return polynomial;
            }
        }
        return null;
    }

    public int size() {
        return polynomials.size();
    }

    public Polynomial getByIndex(int index) {
        return polynomials.get(index);
    }
}

