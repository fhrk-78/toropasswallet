package io.github.fhrk_78.toropasswallet.client;

import io.github.fhrk_78.toropasswallet.client.data.RideHistory;
import io.github.fhrk_78.toropasswallet.client.renderer.PaymentHudRenderer;

import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ToroPassResponseParser {
    private static final Pattern nyujo = Pattern.compile("^入場: (.*?)$");
    private static final Pattern shutsujo = Pattern.compile("^出場: (.*?) 引去: (\\d+)トロポ$");

    private static final Pattern autoCharge = Pattern.compile("^オートチャージが実行されました。新しい残高: (\\d+)トロポ$");
    private static final Pattern charge = Pattern.compile("^チャージ額: (\\d+)トロポ$");

    private static final Pattern zandaka = Pattern.compile("^残高: (\\d+)トロポ$");
    private static final Pattern genzaiZandaka = Pattern.compile("^現在の残高: (\\d+)トロポ$");

    public static void processIt(String response) {
        Matcher zandakaMatcher = zandaka.matcher(response);
        if (zandakaMatcher.find()) {
            int balance = Integer.parseInt(zandakaMatcher.group(1));
            int currentBalance = DataLoader.getInstance().getBalance();
            if (currentBalance < balance) DataLoader.getInstance().setTotalPayment(balance - currentBalance);
            DataLoader.getInstance().setBalance(balance);
        }
        Matcher genzaiZandakaMatcher = genzaiZandaka.matcher(response);
        if (genzaiZandakaMatcher.find()) {
            int balance = Integer.parseInt(genzaiZandakaMatcher.group(1));
            int currentBalance = DataLoader.getInstance().getBalance();
            if (currentBalance < balance) DataLoader.getInstance().setTotalPayment(balance - currentBalance);
            DataLoader.getInstance().setBalance(balance);
        }

        Matcher nyujoMatcher = nyujo.matcher(response);
        if (nyujoMatcher.find()) {
            ToropasswalletClient.beforeRide = DataLoader.getInstance().getBalance();
            ToropasswalletClient.beforeStationName = nyujoMatcher.group(1);
            PaymentHudRenderer.show();
        }
        Matcher shutsujoMatcher = shutsujo.matcher(response);
        if (shutsujoMatcher.find()) {
            String to = shutsujoMatcher.group(1);
            int amount = -Integer.parseInt(shutsujoMatcher.group(2));
            DataLoader.getInstance().histories.add(new RideHistory(ToropasswalletClient.beforeRide, amount,
                    ToropasswalletClient.beforeStationName, to, Instant.now()));
            DataLoader.getInstance().save();
            PaymentHudRenderer.show();
        }

        Matcher autoChargeMatcher = autoCharge.matcher(response);
        if (autoChargeMatcher.find()) {
            int balance = Integer.parseInt(autoChargeMatcher.group(1));
            DataLoader.getInstance().histories.add(new RideHistory(DataLoader.getInstance().getBalance(),
                    balance - DataLoader.getInstance().getBalance(), "#AUTOCHARGE", "",
                    Instant.now()));
            DataLoader.getInstance().setBalance(balance);
            DataLoader.getInstance().save();
        }
        Matcher chargeMatcher = charge.matcher(response);
        if (chargeMatcher.find()) {
            int amount = Integer.parseInt(chargeMatcher.group(1));
            DataLoader.getInstance().histories.add(new RideHistory(DataLoader.getInstance().getBalance(),
                    amount, "#CHARGE", "",
                    Instant.now()));
            DataLoader.getInstance().setBalance(DataLoader.getInstance().getBalance() + amount);
            DataLoader.getInstance().save();
            PaymentHudRenderer.show();
        }

        if (response.equals("強制出場しました。")) {
            DataLoader.getInstance().histories.add(new RideHistory(ToropasswalletClient.beforeRide, 0,
                    ToropasswalletClient.beforeStationName, "#FORCEEXIT", Instant.now()));
            DataLoader.getInstance().save();
        }
    }
}
