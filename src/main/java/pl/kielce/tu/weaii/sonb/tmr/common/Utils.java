package pl.kielce.tu.weaii.sonb.tmr.common;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.BitResponse;
import pl.kielce.tu.weaii.sonb.tmr.main.MainComponent;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class Utils {
    public static URL getResourceURL(String relativePath) {
        return Objects.requireNonNull(MainComponent.class.getClassLoader().getResource(relativePath));
    }

    @NotNull
    public static BitResponse doMajorityVote(ArrayList<BitResponse> bitResponses) {
        var bitsResponsesMap = bitResponses.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return bitsResponsesMap.entrySet().stream()
                .filter(entry -> entry.getValue() >= 2)
                .findFirst().map(Map.Entry::getKey)
                .orElse(new BitResponse(BitResponse.Status.ERROR, "Three different results", null));
    }
}
