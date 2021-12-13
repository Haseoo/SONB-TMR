package pl.kielce.tu.weaii.sonb.tmr.common;

import lombok.experimental.UtilityClass;
import pl.kielce.tu.weaii.sonb.tmr.main.MainComponent;

import java.net.URL;
import java.util.Objects;

@UtilityClass
public class Utils {
    public static URL getResourceURL(String relativePath) {
        return Objects.requireNonNull(MainComponent.class.getClassLoader().getResource(relativePath));
    }
}
