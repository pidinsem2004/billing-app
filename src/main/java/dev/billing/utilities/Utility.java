package dev.billing.utilities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Cette classe va définir un ensemble de méthode
 * utilitaires.
 *
 * @author Jean Pierre NSEM
 */
public abstract class Utility {

    /*
     * predicat pour filtrer les duplicités selon le critère de la clé
     */

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> uniqueMap = new ConcurrentHashMap<>();
        return t -> uniqueMap.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
