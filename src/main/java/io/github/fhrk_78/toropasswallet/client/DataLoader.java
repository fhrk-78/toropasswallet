package io.github.fhrk_78.toropasswallet.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import io.github.fhrk_78.toropasswallet.client.data.RideHistory;
import net.fabricmc.loader.impl.FabricLoaderImpl;

public final class DataLoader {
    @Nullable
    private static DataLoader INSTANCE = null;

    public static DataLoader getInstance() {
        if (INSTANCE == null) INSTANCE = new DataLoader();
        return INSTANCE;
    }

    private final Path saveTo;

    private DataLoader() {
        saveTo = FabricLoaderImpl.INSTANCE.getConfigDir().resolve("toropasswallet.txt");
        load();
        tryInit();
    }

    private void tryInit() {
        if (balance == null) balance = 0;
        if (totalPayment == null) totalPayment = 0L;
        save();
    }

    public void load() {
        if (saveTo.toFile().exists()) {
            List<String> raw;
            try {
                raw = Files.readAllLines(saveTo);
            } catch (IOException e) {
                return;
            }

            raw.forEach(s -> {
                if (s.matches("^.*:=.*$")) {
                    String[] split = s.split(":=");
                    switch (split[0]) {
                        case "balance" -> balance = Integer.valueOf(split[1]);
                        case "totalPayment" -> totalPayment = Long.valueOf(split[1]);
                        case "histories" -> histories = RideHistory.fromStringList(split[1]);
                    }
                }
            });
        }
    }

    public void save() {
        String saveText = "balance:=" + balance + "\ntotalPayment:=" + totalPayment + "\nhistories:=" +
                RideHistory.toString(histories);
        try {
            Files.writeString(saveTo, saveText, StandardOpenOption.CREATE);
        } catch (IOException ignored) {}
    }

    private Integer balance = null;

    @Range(from = 0, to = 20000)
    public Integer getBalance() {
        load();
        return balance;
    }

    public void setBalance(@Range(from = 0, to = 20000) int balance) {
        this.balance = balance;
        save();
    }

    private Long totalPayment = null;

    @Range(from = 0, to = Long.MAX_VALUE)
    public long getTotalPayment() {
        load();
        return totalPayment;
    }

    public void setTotalPayment(@Range(from = 0, to = Long.MAX_VALUE) long totalPayment) {
        this.totalPayment = totalPayment;
        save();
    }

    public ArrayList<RideHistory> histories = new ArrayList<>();
}
