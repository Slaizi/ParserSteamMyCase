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
        Element elements = kurs.select("body[class=g-finance]").first();
        Elements elements1 = elements.select("div[class=page]");
        Elements elements2 = elements1.select("div[class=page__center]");
        Elements elements3 = elements2.select("div[class=finance]");
        Elements elements4 = elements3.select("div[class=finance__main-wrapper]");
        Elements elements5 = elements4.select("div[class=finance__main]");
        Elements elements6 = elements5.select("div[class=finance__center]");
        Elements elements7 = elements6.select("div[class=finance__converter-result]");
        Elements elements8 = elements7.select("div[class=finance__converter-widget]");
        Elements elements9 = elements8.select("div[class=converter-display]");
        Elements elements10 = elements9.select("div[class=converter-display__currency-wrapper]");
        Elements elements11 = elements10.select("div[class=converter-display__currency-body]");
        Elements elements12 = elements11.select("div[class=converter-display__cross-block]");
        Elements elements13 = elements12.select("div[class=converter-display__value]");
        String kursRub = elements13.text();
        String kursRubNew = getKursRub(kursRub);
        double kursDouble = Double.parseDouble(kursRubNew);
        return kursDouble;
    }

    // Кейс «Разлом»
    public static double caseRascol (Document url) throws Exception {
        Element elements = url.select("body[class=responsive_page]").first();
        Elements elements1 = elements.select("div[class=responsive_page_frame with_header]");
        Elements elements2 = elements1.select("div[class=responsive_page_content]");
        Elements elements3 = elements2.select("div[id=responsive_page_template_content]");
        Elements elements4 = elements3.select("div[class=pagecontent no_header]");
        Elements elements5 = elements4.select("div[id=BG_bottom]");
        Elements elements6 = elements5.select("div[id=mainContents]");
        Elements elements7 = elements6.select("div[id=searchResults]");
        Elements elements8 = elements7.select("div[id=searchResultsTable]");
        //Пул с кейсами
        Elements pullcase = elements8.select("div[id=searchResultsRows]");
        Elements caseRascol = pullcase.select("a[href=https://steamcommunity.com/market/listings/730/Fracture%20Case]");
        Elements caseRascol1 = caseRascol.select("div[class=market_listing_row market_recent_listing_row market_listing_searchresult]");
        Elements caseRascol2 = caseRascol1.select("div[class=market_listing_price_listings_block]");
        Elements caseRascol3 = caseRascol2.select("div[class=market_listing_right_cell market_listing_their_price]");
        String priceCaseRascol = caseRascol3.text();
        String price = getHrefFromString(priceCaseRascol);
        double caseRascoldollars = Double.parseDouble(price);
        return caseRascoldollars;
    }
    public static void main(String[] args) throws Exception {
        Document urlSteam = Jsoup.connect("https://steamcommunity.com/market/search?q=&category_730_ItemSet%5B%5D=any&category_730_ProPlayer%5B%5D=any&category_730_StickerCapsule%5B%5D=any&category_730_TournamentTeam%5B%5D=any&category_730_Weapon%5B%5D=any&category_730_Type%5B%5D=tag_CSGO_Type_WeaponCase&appid=730#p1_price_asc").get();
        double rascol = caseRascol(urlSteam);
        Document urlKurs = Jsoup.connect("https://finance.rambler.ru/calculators/converter/1-USD-RUB/").get();
        double kurs = dollarKurs(urlKurs);
        double konvertValue = rascol * kurs;
        String resultRascol = String.format("%.3f",konvertValue);
//         Кейс «Разлом»
        System.out.print("Название кейса\tДоллары(USD)\tРубли(RUB)\n");
        System.out.println("Кейс «Разлом»:\t   " +  (rascol) + "\t\t\t  " + (resultRascol));
    }
}
