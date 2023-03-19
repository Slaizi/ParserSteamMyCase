package io.proj3ct.Grad;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserSteam {

    //D{} буквы
    //d{} цифры
    // S{} НЕ ПРОБЕЛ
    // ДЛЯ КЕЙСА РАСКОЛ ЦЕНА СО STEAM \d{2}\.\d{4}

    // Для Курса
    // \d{2}\.\d{4}
    public static Pattern patternRascol= Pattern.compile("\\d{1}\\.\\d{2}");
    public static Pattern patternKurs = Pattern.compile("\\d{2}\\.\\d{4}");

    private static String getKursRub (String kursRub) throws Exception {
        Matcher matcher = patternKurs.matcher(kursRub);

        if (matcher.find()) {
            return matcher.group();

        } throw new Exception("Can't extract date from string");
    }
    private static String getHrefFromString (String priceCase) throws Exception {
        Matcher matcher = patternRascol.matcher(priceCase);

        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Can't extract date from string");
    }
    public static double dollarKurs (Document kurs) throws Exception {
        Elements elements = kurs.select("body.g-finance div.page div.page__center div.finance " +
                "div.finance__main-wrapper div.finance__main div.finance__center div.finance__converter-result " +
                "div.finance__converter-widget div.converter-display div.converter-display__currency-wrapper " +
                "div.converter-display__currency-body div.converter-display__cross-block " +
                "div.converter-display__value");
        String kursRub = elements.text();
        String kursRubNew = getKursRub(kursRub);
        double kursDouble = Double.parseDouble(kursRubNew);
        return kursDouble;
    }

    // Кейс «Разлом»
    public static double caseRascol (Document url) throws Exception {
        Elements caseRascol = url.select("body.responsive_page div.responsive_page_frame.with_header " +
                "div.responsive_page_content div#responsive_page_template_content div.pagecontent.no_header " +
                "div#BG_bottom div#mainContents div#searchResults div#searchResultsTable div#searchResultsRows " +
                "a[href=https://steamcommunity.com/market/listings/730/Fracture%20Case] " +
                "div.market_listing_row.market_recent_listing_row.market_listing_searchresult " +
                "div.market_listing_price_listings_block div.market_listing_right_cell.market_listing_their_price");
        String priceCaseRascol = caseRascol.text();
        String price = getHrefFromString(priceCaseRascol);
        double caseRascoldollars = Double.parseDouble(price);
        return caseRascoldollars;
    }
    public static void main(String[] args) throws Exception {
        Document urlSteam = Jsoup.connect("https://steamcommunity.com/market/search?q=&category_730_ItemSet%5B%5D=any&category_730_ProPlayer%5B%5D=any&category_730_StickerCapsule%5B%5D=any&category_730_TournamentTeam%5B%5D=any&category_730_Weapon%5B%5D=any&category_730_Type%5B%5D=tag_CSGO_Type_WeaponCase&appid=730#p1_price_asc").get();
        double rascol = caseRascol(urlSteam);

        Document urlKurs = Jsoup.connect("https://finance.rambler.ru/calculators/converter/1-USD-RUB/").get();
        double kurs = dollarKurs(urlKurs);
        //Кейс раскол цена:
        double konvertValue = rascol * kurs;
        String resultRascol = String.format("%.3f",konvertValue);
//         Кейс «Разлом»
        System.out.print("Название кейса\tДоллары(USD)\tРубли(RUB)\n");
        System.out.println("Кейс «Разлом»:\t   " +  (rascol) + "\t\t\t  " + (resultRascol));
    }
}
