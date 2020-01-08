package tgn.content.terraformer.util;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;

public class ArrayUtil {
	public static <A, B> Optional<B[]> map(A[] original, Function<A, B> function, IntFunction<B[]> barray) {
		B[] b = barray.apply(original.length);
		try {
			for (int i = 0; i < original.length; i++)
				b[i] = function.apply(original[i]);
		} catch (Exception e) {
			return Optional.empty();
		}

		return Optional.of(b);
	}
}
