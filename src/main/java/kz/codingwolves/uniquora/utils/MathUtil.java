package kz.codingwolves.uniquora.utils;

/**
 * Created by sagynysh on 1/7/17.
 */
public class MathUtil {

    public static Integer totalPages(Integer totalCount, Integer pageSize) {
        return new Double(Math.ceil(totalCount.doubleValue() / pageSize)).intValue();
    }
}
