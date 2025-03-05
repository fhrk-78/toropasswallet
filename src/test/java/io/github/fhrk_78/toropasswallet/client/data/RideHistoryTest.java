package io.github.fhrk_78.toropasswallet.client.data;

public class RideHistoryTest {
    public static void main(String[] argv) {
        var res = RideHistory.fromString(
                "<before:16332,amount:-1,from:\"神奈崎駅\",to:\"神奈崎駅\",timestamp:1741167230>");
        System.out.println(res);
    }
}
