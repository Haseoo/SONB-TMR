package pl.kielce.tu.weaii.sonb.tmr;

import com.sun.tools.javac.Main;
import lombok.experimental.UtilityClass;

import java.net.URL;
import java.util.Objects;

@UtilityClass
public class Utils {
    public static URL getResourceURL(String relativePath) {
        return Objects.requireNonNull(Main.class.getClassLoader().getResource(relativePath));
    }
}
