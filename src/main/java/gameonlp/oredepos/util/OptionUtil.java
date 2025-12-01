package gameonlp.oredepos.util;

import java.util.Optional;

public class OptionUtil {
    public static  <T> Optional<T> option(T input) {
        if (input != null) {
            return Optional.of(input);
        } else {
            return Optional.empty();
        }
    }
}
