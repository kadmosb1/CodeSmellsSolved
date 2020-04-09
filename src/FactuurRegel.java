import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FactuurRegel {

    private Product product;
    private int aantalProducten;

    public FactuurRegel (String productnaam, double productprijs, String datum) throws ParseException {
        product = new Product (productnaam, productprijs, DatumUtil.sdf.parse(datum));
    }

    public void setAantalProducten (int aantal) {
        aantalProducten = aantal;
    }

    /*
     * Als een product minder dan 10 dagen over datum is, wordt een korting gegeven van 50%.
     * Vanaf 10 dagen over datum wordt een product gratis weg gegeven.
     */
    protected double bepaalKortingVanwegeHoudbaarheidsdatum (Date houdbaarheidsdatum) {
        Date vandaag = new Date ();
        long verschilInMillisecondes = Math.abs(vandaag.getTime() - houdbaarheidsdatum.getTime ());
        long verschilInDagen = TimeUnit.DAYS.convert(verschilInMillisecondes, TimeUnit.MILLISECONDS);

        if (vandaag.before (houdbaarheidsdatum)) {
            return 0.0;
        }
        else if (verschilInDagen < 10) {
            return 50.0;
        }
        else {
            return 100.0;
        }
    }

    public String toString () {

        double prijsMetKorting = aantalProducten * product.getPrijs ();
        double kortingspercentage = 0.0;
        double kortingVanwegeAantalProducten = 0.0;
        double kortingVanwegeHoudbaarheidsdatum = 0.0;

        /*
         * Vanaf 100 stuks van een product geldt een korting van 2%.
         * Vanaf 1.000 stuks geldt een korting van 3%.
         */
        if (aantalProducten >= 1000) {
            kortingVanwegeAantalProducten = 3.0;
        }
        else if (aantalProducten >= 100) {
            kortingVanwegeAantalProducten = 2.0;
        }

        kortingVanwegeHoudbaarheidsdatum = bepaalKortingVanwegeHoudbaarheidsdatum (product.getHoudbaarheidsdatum());

        kortingspercentage = 100.0 - (100.0 - kortingVanwegeAantalProducten) / 100.0 * (100.0 - kortingVanwegeHoudbaarheidsdatum);
        prijsMetKorting *= (100.0 - kortingspercentage) / 100.0;

        return String.format("%6d %-30s  €%8.2f     %3.0f%%  €%8.2f", aantalProducten, product.getNaam(), product.getPrijs (), kortingspercentage, prijsMetKorting);
    }

    public double getTotaalprijs () {

        double prijsMetKorting = aantalProducten * product.getPrijs ();
        double kortingspercentage = 0.0;
        double kortingVanwegeAantalProducten = 0.0;
        double kortingVanwegeHoudbaarheidsdatum = 0.0;

        /*
         * Vanaf 100 stuks van een product geldt een korting van 2%.
         * Vanaf 1.000 stuks geldt een korting van 3%.
         */
        if (aantalProducten >= 1000) {
            kortingVanwegeAantalProducten = 3.0;
        }
        else if (aantalProducten >= 100) {
            kortingVanwegeAantalProducten = 2.0;
        }

        kortingVanwegeHoudbaarheidsdatum = bepaalKortingVanwegeHoudbaarheidsdatum (product.getHoudbaarheidsdatum());

        kortingspercentage = 100.0 - (100.0 - kortingVanwegeAantalProducten) / 100.0 * (100.0 - kortingVanwegeHoudbaarheidsdatum);
        prijsMetKorting *= (100.0 - kortingspercentage) / 100.0;

        return prijsMetKorting;
    }
}