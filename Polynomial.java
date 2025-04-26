package polynomial;
public class Polynomial {
    private static int Counter= 1;

    private final int id;
    private final double[] coefficients;

    public Polynomial(double[] coefficients) {
        this.id = Counter++;
        this.coefficients = coefficients;
    }

    public int getID() {
        return id;
    }

    public int degree() {
        int degree = coefficients.length - 1;
        while (degree > 0 && coefficients[degree] == 0) {
            degree--;
        }
        return degree;
    }

    public double evaluate(double x) {
        double result = 0;
        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x, i);
        }
        return result;
    }

    public Polynomial add(Polynomial other) {
        int maxDegree = Math.max(this.degree(), other.degree());
        double[] resultCoefficients = new double[maxDegree + 1];
        for (int i = 0; i <= maxDegree; i++) {
            resultCoefficients[i] = (i <= this.degree() ? this.coefficients[i] : 0) +
                    (i <= other.degree() ? other.coefficients[i] : 0);
        }
        return new Polynomial(resultCoefficients);
    }

    public Polynomial derivative() {
        int degree = this.degree();
        double[] derivativeCoefficients = new double[Math.max(degree, 1)];
        for (int i = 1; i <= degree; i++) {
            derivativeCoefficients[i - 1] = i * coefficients[i];
        }
        return new Polynomial(derivativeCoefficients);
    }

    @Override
    public String toString() {
        StringBuilder SB = new StringBuilder();
        SB.append("[").append(id).append("] y = ");
        for (int i = coefficients.length - 1; i >= 0; i--) {
            if (coefficients[i] != 0) {
                if (i < coefficients.length - 1) {
                    SB.append(coefficients[i] > 0 ? " + " : " - ");
                }
                if (Math.abs(coefficients[i]) != 1 || i == 0) {
                    SB.append(Math.abs(coefficients[i]));
                }
                if (i > 0) {
                    SB.append("x");
                    if (i > 1) {
                        SB.append("^").append(i);
                    }
                }
            }
        }
        return SB.toString();
    }
}

