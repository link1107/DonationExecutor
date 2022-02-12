package igorlink.service;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String prefixShort = ChatColor.RED + "[DE] " + ChatColor.WHITE;
    private static Boolean _isPluginActive = true;
    protected static HashMap<Character, List<Character>> mapOfSynonymousChars = new HashMap<>();
    public static int donationSummary = 0;

    //Вывод сообщения в консоль
    public static void logToConsole(String text){
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[DonationExecutor] " + ChatColor.WHITE + text);
    }

    //Отправка сообщения игроку со стороны плагина
    public static void sendSysMsgToPlayer(Player player, String text){
        player.sendMessage(prefixShort + text);
    }

    public static byte[] decodeUsingBigInteger(String hexString) {
        byte[] byteArray = new BigInteger(hexString, 16).toByteArray();
        if (byteArray[0] == 0) {
            byte[] output = new byte[byteArray.length - 1];
            System.arraycopy(byteArray, 1, output,0, output.length);
            return output;
        }
        return byteArray;
    }

   public static Boolean CheckNameAndToken() {
        Set<String> tokensSet = Objects.requireNonNull(MainConfig.getConfig().getConfigurationSection("donation-amounts")).getKeys(false);
        if ( ((tokensSet.contains("xxxxxxxxxxxxxxxxxxxx")) && (tokensSet.size() == 1)) || (tokensSet.isEmpty()) ) {
            logToConsole("Вы не указали свой токен DonationAlerts в файле конфигурации плагина, поэтому сейчас плагин не работает.");
            _isPluginActive = false;
        } else {
            _isPluginActive = true;
        }
        return _isPluginActive;
    }

    public static Boolean isPluginActive() {
        return _isPluginActive;
    }

    public static void announce(String donaterName, String subText, String alterSubtext, Player player, String donationAmount, Boolean bigAnnounce) {
        String _donaterName = donaterName;

        if  (bigAnnounce) {
            if (donaterName.equals("")) {
                _donaterName = "Кто-то";
            }
            if (MainConfig.getshowBigAnnouncement()) {
                player.sendTitle(ChatColor.RED + _donaterName, ChatColor.WHITE + subText + " за " + ChatColor.AQUA + donationAmount + ChatColor.WHITE + " руб.", 7, MainConfig.getTimeForAnnouncement() * 20, 7);
            }
            player.sendMessage(prefixShort + "Донатер " + ChatColor.RED + _donaterName, ChatColor.WHITE + subText + " за " + ChatColor.AQUA + donationAmount + ChatColor.WHITE + " руб.");
        }

        if (_donaterName.equals("")) {
            _donaterName = "Кто-то";
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if ( !(p.getName().equals(player.getName())) ) {
                p.sendMessage(prefixShort + "Донатер " + ChatColor.RED + _donaterName + " " + ChatColor.WHITE + alterSubtext + " " + ChatColor.AQUA + player.getName() + ChatColor.WHITE + " за " + ChatColor.AQUA + donationAmount + ChatColor.WHITE + " руб.");
            }
        }


    }

    public static Vector genVec(Location a, Location b, Boolean isNormalized) {
        Vector vec = locToVec(b).clone().subtract(locToVec(a));
        if (isNormalized) {
            return vec.normalize();
        } else {
            return vec;
        }
    }

    public static Vector genVec(Location a, Location b) {
        Vector vec = locToVec(b).clone().subtract(locToVec(a));
            return vec.normalize();
    }

    public static Vector locToVec(Location loc) {
        return new Vector(loc.getX(), loc.getY(), loc.getZ());
    }

    public static String cutOffKopeykis(String donationAmountWithKopeykis) {
        StringBuilder amountWithoutKopeykis = new StringBuilder();
        for (int i = 0; i <= donationAmountWithKopeykis.length() - 1; i++) {
            if (donationAmountWithKopeykis.charAt(i) == '.') {
                break;
            } else if (donationAmountWithKopeykis.charAt(i) != ' ') {
                amountWithoutKopeykis.append(donationAmountWithKopeykis.charAt(i));
            }
        }

        return amountWithoutKopeykis.toString();
    }

    public static void fillTheSynonimousCharsHashMap() {
        mapOfSynonymousChars.put('h', (Arrays.asList('x', 'х', 'н', 'n'))); //eng
        mapOfSynonymousChars.put('n', (Arrays.asList('н', 'й', 'и'))); //eng
        mapOfSynonymousChars.put('н', (Arrays.asList('h', 'n', 'й', 'и'))); //rus
        mapOfSynonymousChars.put('e', (Arrays.asList('е', '3', 'з'))); //eng
        mapOfSynonymousChars.put('е', (Arrays.asList('e', '3', 'з'))); //rus
        mapOfSynonymousChars.put('г', (Arrays.asList('r', 'я', 'g', '7', '6'))); //rus
        mapOfSynonymousChars.put('r', (Arrays.asList('г', 'я', 'g', '7', '6'))); //eng
        mapOfSynonymousChars.put('g', (Arrays.asList('г', 'r', '7', '6'))); //eng
        mapOfSynonymousChars.put('p', (Arrays.asList('п', 'р', 'n', 'я', 'r'))); //eng
        mapOfSynonymousChars.put('р', (Arrays.asList('p', 'r', 'я'))); //rus
        mapOfSynonymousChars.put('п', (Arrays.asList('p', 'n', 'и', 'р'))); //rus
        mapOfSynonymousChars.put('o', (Arrays.asList('о', '0'))); //eng
        mapOfSynonymousChars.put('о', (Arrays.asList('o', '0'))); //rus
        mapOfSynonymousChars.put('a', (List.of('а'))); //eng
        mapOfSynonymousChars.put('а', (List.of('a'))); //rus
        mapOfSynonymousChars.put('и', (Arrays.asList('i', 'n', 'e', 'е', '|', 'l', '!', '1', '3', 'й'))); //rus
        mapOfSynonymousChars.put('i', (Arrays.asList('1', 'и', 'e', 'е', '|', 'l', '!', 'й'))); //eng
        mapOfSynonymousChars.put('с', (Arrays.asList('c', 's', '$', '5'))); //rus
        mapOfSynonymousChars.put('s', (Arrays.asList('c', 'с', '$', '5'))); //eng
        mapOfSynonymousChars.put('c', (Arrays.asList('s', 'с', '$', '5'))); //eng
        mapOfSynonymousChars.put('л', (Arrays.asList('l', '1', '|'))); //rus
        mapOfSynonymousChars.put('l', (Arrays.asList('л', '1', '|', '!'))); //eng
        mapOfSynonymousChars.put('1', (Arrays.asList('л', 'i', 'l', '|'))); //eng
        mapOfSynonymousChars.put('d', (Arrays.asList('д', 'л'))); //eng
        mapOfSynonymousChars.put('д', (Arrays.asList('d', 'л', '9'))); //rus
        mapOfSynonymousChars.put('y', (Arrays.asList('у', 'u', 'ы'))); //eng
        mapOfSynonymousChars.put('у', (Arrays.asList('y', 'u', 'ы'))); //rus
        mapOfSynonymousChars.put('x', (Arrays.asList('х', 'h'))); //eng
        mapOfSynonymousChars.put('х', (Arrays.asList('x', 'h'))); //rus
        mapOfSynonymousChars.put('ы', (Arrays.asList('у', 'u', 'y'))); //rus
        mapOfSynonymousChars.put('ч', (List.of('4')));//rus
        mapOfSynonymousChars.put('k', (List.of('к')));//eng
        mapOfSynonymousChars.put('к', (List.of('k')));//rus
        mapOfSynonymousChars.put('0', (Arrays.asList('о', 'o'))); //num
        mapOfSynonymousChars.put('3', (Arrays.asList('e', 'е','з')));
        mapOfSynonymousChars.put('4', (List.of('ч')));
        mapOfSynonymousChars.put('5', (Arrays.asList('с', 'c', 's')));
        mapOfSynonymousChars.put('9', (Arrays.asList('r', 'я')));
    }

    public static Boolean isBlackListed(String text) {

        String validationText = text.toLowerCase();

        Pattern pattern = Pattern.compile("[l1i]*[\\-]*[l1i]*");
        Matcher matcher = pattern.matcher(validationText);
        if ( (matcher.find()) && (matcher.group().length()>0) ) {
           validationText = validationText.replace(matcher.group(), "н");
        }

        validationText = validationText.replace("_", "");
        validationText = validationText.replace(" ", "");
        validationText = validationText.replace(",", "");
        validationText = validationText.replace(".", "");
        validationText = validationText.replace("-", "");
        validationText = validationText.replace("%", "");
        validationText = validationText.replace("*", "");
        validationText = validationText.replace("?", "");

        if (validationText.length() == 0) {
            return false;
        }


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
                    } else if ((mapOfSynonymousChars.containsKey(ss.charAt(j))) && (mapOfSynonymousChars.get(ss.charAt(j)).contains(validationText.charAt(tempi + j)))) {
                        continue;
                    }

                    while (true) {
                        if (j==0) {
                            break;
                        }
                        if (validationText.charAt(tempi + j) != validationText.charAt(tempi + j - 1)) {
                            if (!(mapOfSynonymousChars.containsKey(validationText.charAt(tempi + j)))) {
                                break;
                            } else if (!(mapOfSynonymousChars.get(validationText.charAt(tempi + j)).contains(validationText.charAt(tempi + j - 1)))) {
                                break;
                            }
                        }
                        tempi++;
                        if ((validationText.length()-tempi-j) < (ss.length()-j)) {
                            break;
                        }
                    }

                    if ((validationText.length()-tempi-j) < (ss.length()-j)) {
                        break;
                    }

                    if (validationText.charAt(tempi + j) == ss.charAt(j)) {
                        continue;
                    } else if ((mapOfSynonymousChars.containsKey(ss.charAt(j)))) {
                        if ((mapOfSynonymousChars.get(ss.charAt(j)).contains(validationText.charAt(tempi + j)))) {
                            continue;
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

