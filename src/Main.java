import java.util.ArrayList;
import java.util.Date;

public class Main {

    /*
     * Er wordt een nieuwe klant aangemaakt, die aan Factuur meegegeven kan
     * worden.
     */
    protected static Klant getKlant (boolean buitenNederland) {

        /*
         * De factuur moet gemakkelijk voor zowel een Nederlandse als een Belgische klant opgesteld kunnen
         * worden. Dat gebeurt op basis van de eerste twee letters van het BTW-nummer.
         */
        String btwNummer = "BE 0826882419";

        if (! buitenNederland) {
            btwNummer = "NL 0826882419";
        }

        Adres adres = new Adres ("Johanna Westerdijkplein", 75, "");
        Postcode pc = new Postcode (2521, "EN");
        FactuurAdres factuurAdres = new FactuurAdres (adres, pc, "DEN HAAG");

        // return new ConsumentKlant("Karel J. van der Lelij", factuurAdres, btwNummer);
        return new OverheidKlant("De Haagse Hogeschool", factuurAdres, btwNummer);
        // return new HorecaKlant("Kobus Kuch", factuurAdres, btwNummer);
    }

    /*
     * In deze methode worden factuurregels aan de factuur toegevoegd.
     */
    protected static void voegFactuurRegelsToe (Factuur factuur) {

        /*
         * Om de werking van de houdbaarheidsdatum goed te kunnen testen, worden
         * drie variabelen voor vers, oud en bedorven toegevoegd.
         */
        String vers = DatumUtil.getDatumStringMetAantalDagenVoorVandaag(-6);
        String oud = DatumUtil.getDatumStringMetAantalDagenVoorVandaag(6);
        String bedorven = DatumUtil.getDatumStringMetAantalDagenVoorVandaag(20);

        /*
         * Voor elke regel worden nu producten aangemaakt, waarvoor de gegevens voor
         * aantal producten, producten per verpakking (bijv. 6 flessen in een doos),
         * gewicht en eenheid voor dat gewicht (bijv. kg) apart worden ingesteld.
         */
        Product product = new ProductPerStuk ("Product 1", 2.50, vers);
        FactuurRegel factuurRegel = new FactuurRegel(20, product);
        factuur.addFactuurRegel(factuurRegel);

        product = new ProductPerColli ("Product 2", 10.0, vers, 250);
        factuurRegel = new FactuurRegel(1, product);
        factuur.addFactuurRegel(factuurRegel);

        product = new ProductPerStuk ("Product 3", 0.22, oud);
        factuurRegel = new FactuurRegel(1000, product);
        factuur.addFactuurRegel(factuurRegel);

        ProductPerGewicht productPerGewicht = new ProductPerGewicht ("Product 4", 1.50, oud, 2.55, "kg");
        factuurRegel = new FactuurRegel(productPerGewicht);
        factuur.addFactuurRegel(factuurRegel);

        product = new ProductPerStuk ("Product 5", 0.88, bedorven);
        factuurRegel = new FactuurRegel(1, product);
        factuur.addFactuurRegel(factuurRegel);
    }

    /*
     * Er wordt een factuur op het scherm getoond.
     */
    public static void main(String[] args) {

        /*
         * Om een long method korter te maken hebben we 'extract method'
         * gebruikt om de code voor het aanmaken van een klant te verplaatsen
         * naar de constructor van
         */
        Klant klant = getKlant(false);
        Factuur factuur = new Factuur (klant);

        /*
         * Om een long method korter te maken hebben we 'extract method'
         * gebruikt om het toevoegen van factuurregels in een aparte methode
         * voegFactuurRegelsToe onder te brengen. Hiervoor is verplaatsing
         * van het toevoegen van factuurregels van de class Factuur in de method
         * maakFactuur naar class Main nodig.
         */
        voegFactuurRegelsToe (factuur);
        new FactuurPrinter(factuur).printFactuur();
    }
}
