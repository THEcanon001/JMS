package utils;

public class R {
    public static int randomValueFromClosedInterval(int min, int max, MersenneTwisterFast random) {
        if (max - min < 0) // we had an overflow
        {
            int l = 0;
            do l = random.nextInt();
            while (l < min || l > max);
            return l;
        } else return min + random.nextInt(max - min + 1);
    }
}
