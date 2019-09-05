// TODO: Auto-generated Javadoc
/**
 * The Class RandInt.
 */
/*
 * RandInt
 * This class acts as a random integer generator.
 */
class RandInt {
    /**
     * RandInt
     * This method creates a new random integer generator.
     */
    public RandInt() {
    }

    /**
     * generate
     * This method generates a random integer between a minimum and maximum threshhold.
     * @param min The minimum integer that can be generated.
     * @param max The maximum integer that can be generated.
     * @return Int, the random integer that has been generated.
     */
    static int generate(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
