package tgn.content.terraformer.util;

import java.util.Optional;
import java.util.function.Function;

public class ArrayUtil {
	public static <A, B> Optional<B[]> map(A[] original, Function<A, B> function) {
		B[] b = (B[]) new Object[original.length];
		try {
			for (int i = 0; i < original.length; i++)
				b[i] = function.apply(original[i]);
		} catch (Exception e) {
			return Optional.empty();
		}

		return Optional.of(b);
	}
}
