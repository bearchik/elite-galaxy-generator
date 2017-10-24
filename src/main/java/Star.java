public class Star {
    private short[] seed;

    private String planetname;

    private int x;
    private int y;

    private int govid;
    private String govname;
    private int radius;

    private int ecoid;
    private String econame;

    private int techlvl;
    private float population;
    private int production;

    private String inhabitants;

    private final String[] govarray = {"Anarchy", "Feudal", "Multi-Government", "Dictatorship",
            "Communist", "Confederacy", "Democracy", "Corporate State"};

    private final String[] economies = {"Rich Industrial", "Average Industrial", "Poor Industrial",
            "Mainly Industrial", "Mainly Agricultural", "Rich Agricultural", "Average Agricultural",
            "Poor Agricultural"};

    private final String DIGRAM_STRING = "@@LEXEGEZACEBISOUSESARMAINDIREA'ERATENBERALAVETIEDORQUANTEISRION";

    private final String[] SIZEINH = {"Large", "Fierce", "Small", ""};
    private final String[] COLORINH = {"Green", "Red", "Yellow", "Blue", "Black", "Harmless", ""};
    private final String[] ATTRINH = {"Slimy", "Bug-Eyed", "Horned", "Bony", "Fat", "Furry", ""};
    private final String[] INHAB = {"Rodents", "Frogs", "Lizards", "Lobsters", "Birds", "Humanoids", "Felines", "Insects"};


    public void initStar(short[] seed) {
        this.seed = seed;
        this.planetname = createName(this.seed);
        y = createCoordinates(seed[0]);
        x = createCoordinates(seed[1]);
        radius = createRadius(seed[2]);
        govid = createGoverment(seed[1]);
        ecoid = createEconomca(seed[0]);
        int[] tl_data = createTlData(seed);
        techlvl = createTechlvl(tl_data);
        population = createPopulation();
        production = cteateProduction(tl_data);
        inhabitants = createInhabitants(seed);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getGovid() {
        return govid;
    }

    public String getGovname() {
        return govname;
    }

    public int getRadius() {
        return radius;
    }

    public int getEcoid() {
        return ecoid;
    }

    public String getEconame() {
        return econame;
    }

    private int createCoordinates(short seedpart) {
        return (seedpart >> 8) & 255;
    }

    private String getNameGoverment(short govid) {
        return govarray[govid];
    }

    public short[] getSeed() {
        return seed;
    }

    public String getPlanetname() {
        return planetname;
    }

    public int getTechlvl() {
        return techlvl;
    }

    public float getPopulation() {
        return population;
    }

    public int getProduction() {
        return production;
    }

    public String getInhabitants() {
        return inhabitants;
    }

    public String[] getEconomies() {
        return economies;
    }

    private int createGoverment(short seedpart) {
        short govid = (short) ((seedpart >>> 3) & 0x7);
        govname = getNameGoverment(govid);
        return govid;
    }

    private int createEconomca(short seedpart) {
        short ecoid = (short) ((seedpart >>> 8) & 0x7);
        econame = getEconomicaName(ecoid);
        return ecoid;
    }

    private String getEconomicaName(short ecoid) {
        return economies[ecoid];
    }

    private int createRadius(short seedpart) {
        return 256 * (11 + ((seedpart >>> 8) & 0xf)) + this.x;
    }

    public short[] twist(short[] seed) {
        short[] twistseed = new short[3];
        twistseed[0] = seed[1];
        twistseed[1] = seed[2];
        twistseed[2] = (short)(seed[0] + seed[1] + seed[2]);
        return twistseed;
    }

    private String createName(short[] seed) {
        String name = "";

        short[] nameseed = seed;
        for (int i = 0; i < 4 ; i++) {
            if (i != 3 || ((seed[0] >>> 6) & 1) != 0) {
                name += getNameChar(nameseed[2]);
            }

            nameseed = twist(nameseed);
        }

        name = name.replace("@","");
        name = name.replace("'", "");
        return name;
    }

    private String getNameChar(short seedpart) {
        String pair = "" ;
        int shift = ((seedpart >>> 8) & 31) * 2;
        pair += DIGRAM_STRING.toCharArray()[shift];
        pair += DIGRAM_STRING.toCharArray()[shift + 1];
        return pair;
    }

    private int createTechlvl(int[] tl) {
        return (1 - tl[0]) * 4 + (1 - tl[1]) * 2 + 1 - tl[2] + tl[3] + tl[4] + tl[5] + 1;
    }

    private float createPopulation() {
        return (float) ((techlvl - 1) * 4 + govid + ecoid +1) / 10;
    }

    private int cteateProduction(int[] tl) {
        return ((1 - tl[0]) * 4 + (1 - tl[1]) * 2 + 1 - tl[2] + 3) * (govid + 4) * (int)(population * 80);
    }

    private int[] createTlData(short [] seed) {
        int tl[] = new int[6];
        tl[0] = seed[0] >>> 10 & 1;
        tl[1] = seed[0] >>> 9 & 1;
        tl[2] = seed[0] >>> 8 & 1;
        tl[3] = seed[1] >>> 8 & 3;
        tl[4] = seed[1] >>> 4 & 3;
        tl[5] = seed[1] >>> 3 & 1;
        return tl;
    }

    public String createInhabitants(short[] seed) {
        String inhabitants = "Human Colonists";
        int[] inhabarray = new int[4];
        if ((seed[2] >>> 7 & 1) != 0) {
            inhabarray[0] = Math.min(seed[2] >>> 10 & 7, 3);
            inhabarray[1] = Math.min(seed[2] >>> 13 & 7, 6);
            int third_attribute = 0;

            for(int i = 0; i < 3; i++) {
                int binary_start = 8 - (i + 1);

                int combined_values = seed[0] >>> (15 - binary_start) & 1;
                combined_values = combined_values + (seed[1] >>> (15 - binary_start) & 1);
                combined_values = combined_values % 2;

                if(i > 0) {
                    combined_values *= (i * 2);
                }

                third_attribute = third_attribute + combined_values;
            }
            inhabarray[2] = Math.min(third_attribute, 6);
            inhabarray[3] = ((seed[2] >>> 8 % 3) + third_attribute) % 8;
            return createInhabString(inhabarray);

        }

        return inhabitants;
    }

    private String createInhabString(int[] inhabarray) {
        String inhabstring;

        inhabstring = SIZEINH[inhabarray[0]];
        if(!inhabstring.equals("")) {
            inhabstring += " ";
        }

        inhabstring += COLORINH[inhabarray[1]];
        if(!inhabstring.equals("")) {
            inhabstring += " ";
        }

        inhabstring += ATTRINH[inhabarray[2]];
        if(!inhabstring.equals("")) {
            inhabstring += " ";
        }

        inhabstring += INHAB[inhabarray[3]];
        if(!inhabstring.equals("")) {
            inhabstring += " ";
        }

        return inhabstring;
    }

}
