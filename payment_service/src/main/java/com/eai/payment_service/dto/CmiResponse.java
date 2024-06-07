package com.eai.payment_service.dto;
import com.google.common.collect.ImmutableSortedMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.eai.payment_service.service.PaymentService.removeTrailingNewLines;


@Getter
@RequiredArgsConstructor
@Slf4j
public class CmiResponse {

        private final ImmutableSortedMap<String, String> data;

        public static CmiResponse from(LinkedMultiValueMap<String, String> map) throws Exception {

            SortedMap<String, String> objectObjectMap = convertToCaseInsensitiveSingleValueSortedMapStrict(map);


            return new CmiResponse(ImmutableSortedMap.copyOfSorted(objectObjectMap));
        }

        private static SortedMap<String, String> convertToCaseInsensitiveSingleValueSortedMapStrict(MultiValueMap<String, String> multiValueMap) throws Exception {

            SortedMap<String, String> sortedCaseInsensitiveMap = new TreeMap<>(String::compareToIgnoreCase);
            String lastKey = null;
            for (Map.Entry<String, List<String>> entry : multiValueMap.entrySet()) {
                List<String> valueList = entry.getValue();
                if (valueList.size() > 1) {
                    throw new Exception("Cannot convert MultiValueMap to Map. Key '" + entry.getKey() + "' has multiple values");
                }
                String value = valueList.isEmpty() ? null :removeTrailingNewLines(valueList.get(0));
                sortedCaseInsensitiveMap.put(entry.getKey(), value);
                lastKey = entry.getKey();
            }
            if (sortedCaseInsensitiveMap.size() < multiValueMap.size()) {
                throw new IllegalArgumentException("Some properties differ only by case");
            }
            // This is needed because in callback, cmi adds \n to the last parameter sent in the post
            if (lastKey != null) {
                String lastValue = sortedCaseInsensitiveMap.get(lastKey);
                if (!isStringEmptyOrNull(lastValue) &&  lastValue.charAt(lastValue.length() - 1) == '\n') {
                    lastValue = lastValue.substring(0, lastValue.length() - 1);
                    log.info("Removed \\n from the end of the last key's value ({}={}) in CMI response", lastKey, lastValue);
                }
                sortedCaseInsensitiveMap.put(lastKey, lastValue);
            }

            return sortedCaseInsensitiveMap;
        }

    private static boolean isStringEmptyOrNull(String text) {
        if (text != null) {
            return text.isEmpty();
        }
        return true;
    }
}

