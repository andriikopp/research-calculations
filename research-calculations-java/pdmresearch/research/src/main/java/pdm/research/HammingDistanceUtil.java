package pdm.research;

public class HammingDistanceUtil {

	public static int dist(int[] a, int[] b) {
		int d = 0;

		for (int i = 0; i < a.length; i++) {
			d += a[i] != b[i] ? 1 : 0;
		}

		return d;
	}
}
