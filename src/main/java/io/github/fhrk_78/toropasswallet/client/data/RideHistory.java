package io.github.fhrk_78.toropasswallet.client.data;

import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record RideHistory(
        int before,
        Integer amount,
        String from,
        String to,
        Instant timestamp
) {
    @Override
    public String toString() {
        return "<" +
                "before:" + before +
                ",amount:" + amount +
                ",from:\"" + from + '\"' +
                ",to:\"" + to + '\"' +
                ",timestamp:" + timestamp.getEpochSecond() +
                '>';
    }

    @Nullable
    public static RideHistory fromString(String s) {
        Matcher matcher = Pattern.compile(
                "^<before:(\\d+),amount:(\\d+),from:\"(.*?)\",to:\"(.*?)\",timestamp:(\\d+)>$").matcher(s);
        if (matcher.find()) {
            return new RideHistory(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    matcher.group(3),
                    matcher.group(4),
                    Instant.ofEpochSecond(Long.parseLong(matcher.group(5))));
        }
        return null;
    }

    public static String toString(ArrayList<RideHistory> list) {
        return String.join(";", list.stream().map(RideHistory::toString).toList());
    }

    public static ArrayList<RideHistory> fromStringList(String s) {
        ArrayList<RideHistory> list = new ArrayList<>();

        new StringTokenizer(s, ";").asIterator().forEachRemaining(
                v -> list.add(fromString((String) v)));

        return list;
    }
}
