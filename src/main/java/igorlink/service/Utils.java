package igorlink.service;

import igorlink.ArrayUtils;
import igorlink.CharArrayMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static boolean _isPluginActive = true;
    public static int donationSummary = 0;

    private static final CharArrayMap synonymousChars = new CharArrayMap();

    private static final char[] WHITESPACE_CHARS = new char[]{
            '_',
            ' ',
            ',',
            '.',
            '-',
            '%',
            '*',
            '?'
    };

    static {
        synonymousChars.put(
                'h',
                'x', 'х', 'н', 'n'
        ); //eng
        synonymousChars.put(
                'n',
                'н', 'й', 'и'
        ); //eng
        synonymousChars.put(
                'н',
                'h', 'n', 'й', 'и'
        ); //rus
        synonymousChars.put(
                'e',
                'е', '3', 'з'
        ); //eng
        synonymousChars.put(
                'е',
                'e', '3', 'з'
        ); //rus
        synonymousChars.put(
                'г',
                'r', 'я', 'g', '7'
        ); //rus
        synonymousChars.put(
                'r',
                'г', 'я', 'g', '7'
        ); //eng
        synonymousChars.put(
                'g',
                'г', 'r', '7'
        ); //eng
        synonymousChars.put(
                'p',
                'п', 'р', 'n', 'я', 'r'
        ); //eng
        synonymousChars.put(
                'р',
                'p', 'r', 'я'
        ); //rus
        synonymousChars.put(
                'п',
                'p', 'n', 'и', 'р'
        ); //rus
        synonymousChars.put(
                'o',
                'о', '0'
        ); //eng
        synonymousChars.put(
                'о',
                'o', '0'
        ); //rus
        synonymousChars.put(
                'a',
                'а'
        ); //eng
        synonymousChars.put(
                'а',
                'a'
        ); //rus
        synonymousChars.put(
                'и',
                'i', 'n', 'e', 'е', '|', 'l', '!', '1', '3', 'й'
        ); //rus
        synonymousChars.put(
                'i',
                '1', 'и', 'e', 'е', '|', 'l', '!', 'й'
        ); //eng
        synonymousChars.put(
                'с',
                'c', 's', '$', '5'
        ); //rus
        synonymousChars.put(
                's',
                'c', 'с', '$', '5'
        ); //eng
        synonymousChars.put(
                'c',
                's', 'с', '$', '5'
        ); //eng
        synonymousChars.put(
                'л',
                'l', '1', '|'
        ); //rus
        synonymousChars.put(
                'l',
                'л', '1', '|', '!'
        ); //eng
        synonymousChars.put(
                '1',
                'л', 'i', 'l', '|'
        ); //eng
        synonymousChars.put(
                'd',
                'д', 'л'
        ); //eng
        synonymousChars.put(
                'д',
                'd', 'л', '9'
        ); //rus
        synonymousChars.put(
                'y',
                'у', 'u', 'ы'
        ); //eng
        synonymousChars.put(
                'у',
                'y', 'u', 'ы'
        ); //rus
        synonymousChars.put(
                'x',
                'х', 'h'
        ); //eng
        synonymousChars.put(
                'х',
                'x', 'h'
        ); //rus
        synonymousChars.put(
                'ы',
                'у', 'u', 'y'
        ); //rus
        synonymousChars.put(
                'ч',
                '4'
        );//rus
        synonymousChars.put(
                'k',
                'к'
        ); //eng
        synonymousChars.put(
                'к',
                'k'
        ); //rus
        synonymousChars.put(
                '0',
                'o', 'о'
        ); //num
        synonymousChars.put(
                '3',
                'e', 'е', 'з'
        );
        synonymousChars.put(
                '4',
                'ч'
        );
        synonymousChars.put(
                '5',
                'с', 'c', 's'
        );
        synonymousChars.put(
                '9',
                'r', 'я'
        );
        synonymousChars.sort();
    }

    //Вывод сообщения в консоль
    public static void logToConsole(@NotNull String text) {
        Bukkit.getConsoleSender().sendMessage("§c[DonationExecutor] §f" + text);
    }

    //Отправка сообщения игроку со стороны плагина
    public static void sendSysMsgToPlayer(@NotNull Player player, @NotNull String text) {
        player.sendMessage("§c[DE] §f" + text);
    }

    public static byte @NotNull [] decodeUsingBigInteger(@NotNull String hexString) {
        byte[] byteArray = new BigInteger(hexString, 16).toByteArray();
        if (byteArray[0] == 0) {
            byte[] output = new byte[byteArray.length - 1];
            System.arraycopy(byteArray, 1, output, 0, output.length);
            return output;
        }
        return byteArray;
    }

    public static boolean checkNameAndToken() {
        ConfigurationSection tokensSection = MainConfig.getConfig().getConfigurationSection("donation-amounts");

        if(tokensSection != null) {
            Set<String> tokensSet = tokensSection.getKeys(false);

            if((tokensSet.size() == 1 && tokensSet.contains("xxxxxxxxxxxxxxxxxxxx")) || tokensSet.isEmpty()) {
                logNoToken();
                _isPluginActive = false;
            } else {
                _isPluginActive = true;
            }
        } else {
            logNoToken();
            _isPluginActive = false;
        }

        return _isPluginActive;
    }

    private static void logNoToken() {
        logToConsole("Вы не указали свой токен DonationAlerts в файле конфигурации плагина, поэтому сейчас плагин не работает.");
    }

    public static boolean isPluginActive() {
        return _isPluginActive;
    }

    public static void announce(
            @NotNull String donaterName,
            @NotNull String subText,
            @NotNull String alterSubtext,
            @NotNull Player player,
            boolean bigAnnounce
    ) {
        if (donaterName.length() == 0) {
            donaterName = "Кто-то";
        }

        if (bigAnnounce) {
            if (MainConfig.getshowBigAnnouncement()) {
                player.sendTitle("§c" + donaterName, "§f" + subText, 7, MainConfig.getTimeForAnnouncement() * 20, 7);
            }

            player.sendMessage("§c[DE] §fДонатер §c" + donaterName, "§f" + subText);
        }

        String message = "§c[DE] §fДонатер §c" + donaterName + " §f" + alterSubtext + " §b" + player.getName();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!(p.getName().equals(player.getName()))) {
                p.sendMessage(message);
            }
        }
    }

    @NotNull
    public static Vector genVec(@NotNull Location a, @NotNull Location b, boolean isNormalized) {
        Vector vec = locToVec(b).subtract(locToVec(a));

        if (isNormalized) {
            return vec.normalize();
        } else {
            return vec;
        }
    }

    @NotNull
    public static Vector genVec(@NotNull Location a, @NotNull Location b) {
        return genVec(a, b, true);
    }

    @NotNull
    public static Vector locToVec(@NotNull Location loc) {
        return new Vector(loc.getX(), loc.getY(), loc.getZ());
    }

    @NotNull
    public static String cutOffKopeykis(@NotNull String donationAmountWithKopeykis) {
        StringBuilder sb = new StringBuilder(donationAmountWithKopeykis.length());
        for (int i = 0; i <= donationAmountWithKopeykis.length() - 1; i++) {
            if (donationAmountWithKopeykis.charAt(i) == '.') {
                break;
            } else if (donationAmountWithKopeykis.charAt(i) != ' ') {
                sb.append(donationAmountWithKopeykis.charAt(i));
            }
        }

        return sb.toString();
    }

    @NotNull
    private static String removeWhitespaceChars(@NotNull String text) {
        // новая строка точно не будет больше оригинальной
        StringBuilder sb = new StringBuilder(text.length());

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            boolean isWhitespaceFound = false;

            for (char whitespace : WHITESPACE_CHARS) {
                if (c == whitespace) {
                    isWhitespaceFound = true;
                    break;
                }
            }

            if (!isWhitespaceFound) {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public static boolean isBlackListed(@NotNull String text) {
        String validationText = text.toLowerCase();

        Pattern pattern = Pattern.compile("[l1i]*[\\-]*[l1i]*");
        Matcher matcher = pattern.matcher(validationText);
        if (matcher.find() && matcher.group().length() > 0) {
            validationText = validationText.replace(matcher.group(), "н");
        }

        validationText = removeWhitespaceChars(validationText);

        if (validationText.length() == 0) {
            return false;
        }

//        for (String ss : MainConfig.listOfWhiteListedSubstrings) {
//            if (validationText.contains(ss)) {
//                validationText = validationText.replace(ss, "");
//            }
//        }


        if (!(validationText.matches("[a-zа-я0-9$!ё]*"))) {
            return true;
        }

        for (String ss : MainConfig.getListOfBlackListedSubstrings()) {
            for (int i = 0; i <= validationText.length() - ss.length(); i++) {
                int tempi = i;
                for (int j = 0; j <= ss.length(); j++) {

                    if (j == ss.length()) {
                        //если мы прошли всю субстроку до конца - значит слово содержит субстроку из блеклиста
                        return true;
                    }

                    //Если текущая буква субстроки равна или синонимична текущей букве в слове, значит идем дальше смотреть следующий символ
                    if (validationText.charAt(tempi + j) == ss.charAt(j)) {
                        continue;
                    } else {
                        char[] synonymous = synonymousChars.get(ss.charAt(j));

                        if (ArrayUtils.contains(synonymous, validationText.charAt(tempi + j))) {
                            continue;
                        }
                    }

                    while (true) {
                        if (j == 0) {
                            break;
                        }

                        if (!(validationText.charAt(tempi + j) == validationText.charAt(tempi + j - 1))) {
                            char[] synonymous = synonymousChars.get(validationText.charAt(tempi + j));

                            if(ArrayUtils.contains(synonymous, validationText.charAt(tempi + j - 1))) {
                                break;
                            }
                        }
                        tempi++;
                        if ((validationText.length() - tempi - j) < (ss.length() - j)) {
                            break;
                        }
                    }

                    if ((validationText.length()-tempi-j) < (ss.length()-j)) {
                        break;
                    }

                    if (validationText.charAt(tempi + j) == ss.charAt(j)) {
                        continue;
                    } else {
                        char[] synonymous = synonymousChars.get(ss.charAt(j));

                        if(synonymous != null) {
                            if (ArrayUtils.contains(synonymous, validationText.charAt(tempi + j))) {
                                continue;
                            }
                        }
                    }

                    break;

                }
            }
        }

        return false;
    }

    public static int getSum() {
        return donationSummary;
    }

    public static void addSum(int sum) {
        donationSummary = donationSummary + sum;
    }

}

