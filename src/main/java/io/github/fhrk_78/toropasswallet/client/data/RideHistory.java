package io.github.fhrk_78.toropasswallet.client.data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.Nullable;

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
                "^<before:(\\d+),amount:(-?\\d+),from:\"(.*?)\",to:\"(.*?)\",timestamp:(\\d+)>$").matcher(s);
        if (matcher.find()) {
            return new RideHistory(
                Integer.parseInt(matcher.group(1)),
                Integer.valueOf(matcher.group(2)),
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
        if (s.isEmpty()) return list;
        for (var v : s.split(";")) {
            var res = fromString((String) v);
            if (res == null) continue;
            list.add(res);
        }
        return list;
    }
}
