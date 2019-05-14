package lv.latvijaff.sugoinihongo.utils.helpers;

import java.util.Comparator;
import java.util.function.Function;

public class CustomSorters {

	private CustomSorters() {}

	public static <E, T extends Comparable<T>> Comparator<E> sort(Function<? super E, T> func) {
		return sort(func, SortingExpression.ASCENDING);
	}

	public static <E, T extends Comparable<T>> Comparator<E> sort(Function<? super E, T> func, SortingExpression se) {
		return (obj1, obj2) -> {
			T val1 = func.apply(obj1);
			T val2 = func.apply(obj2);
			int cmp = val1.compareTo(val2);

			return se.compare(cmp);
		};
	}

	public static <E, T1 extends Comparable<T1>, T2 extends Comparable<T2>> Comparator<E> sort(
		Function<? super E, T1> func1, SortingExpression se,
		Function<? super E, T2> func2) {

		return sort(func1, se, func2, SortingExpression.ASCENDING);
	}

	private static <E, T1 extends Comparable<T1>, T2 extends Comparable<T2>> Comparator<E> sort(
		Function<? super E, T1> func1, SortingExpression se1,
		Function<? super E, T2> func2, SortingExpression se2) {

		return (obj1, obj2) -> {
			T1 val1 = func1.apply(obj1);
			T1 val2 = func1.apply(obj2);
			int cmp1 = val1.compareTo(val2);

			if (cmp1 != 0) {
				return se1.compare(cmp1);
			}

			T2 val3 = func2.apply(obj1);
			T2 val4 = func2.apply(obj2);
			int cmp2 = val3.compareTo(val4);

			return se2.compare(cmp2);
		};
	}

	public enum SortingExpression {

		ASCENDING,
		DESCENDING;

		int compare(int value) {
			switch (this) {
				case ASCENDING:
					return value;
				case DESCENDING:
					return -value;

					default:
						throw new IllegalArgumentException("Cannot resolve value for " + this);
			}
		}
	}
}
