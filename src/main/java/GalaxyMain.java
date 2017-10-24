import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GalaxyMain {
    public static void main(String[] args) {

        BufferedReader br = null;

        int galaxynumber = 0;
        int planetnumber = 0;

        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print("Enter galaxy number:");
                String input = br.readLine();
                galaxynumber = Integer.valueOf(input);
                System.out.print("Enter star number:");
                input = br.readLine();
                planetnumber = Integer.valueOf(input);
                printStar(generateGalaxy(galaxynumber, planetnumber));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void printStar(Star star) {
        System.out.flush();

        System.out.println(String.format("Name: %16s", star.getPlanetname()));
        System.out.println(String.format("Loc: %16s:%s", String.valueOf(star.getX()), String.valueOf(star.getY())));
        System.out.println(String.format("Radius: %16s km", String.valueOf(star.getRadius())));
        System.out.println(String.format("Government: %16s km", star.getGovname()));
        System.out.println(String.format("Economy: %16s", star.getEconame()));
        System.out.println(String.format("Inhabitants: %16s", star.getInhabitants()));
        System.out.println(String.format("Tech Level: %16s", String.valueOf(star.getTechlvl())));
        System.out.println(String.format("Population: %16s Billion", String.valueOf(star.getPopulation())));
        System.out.println(String.format("Production: %16s Mcr", String.valueOf(star.getProduction())));

    }

    public static Star generateGalaxy(int galaxynumber, int planetnumber) {
        short[] seed = { 0x5A4A, 0x0248, (short)(0xB753) };
        Star star = new Star();


        seed = createGalaxySeed(seed, galaxynumber);

        star.initStar(seed);
        short[] newseed = star.getSeed();

        for (int i = 0; i < planetnumber; i++) {
            newseed = star.twist(newseed);
            newseed = star.twist(newseed);
            newseed = star.twist(newseed);
            newseed = star.twist(newseed);
        }
        star.initStar(newseed);

        return star;
    }

    public static short[] createGalaxySeed(short[] initseed, int roll) {
        short[] newseed = new short[3];

        if(roll == 0) {
            return initseed;
        }

        for(int j = 0; j < roll; j++) {
            for ( int i = 0; i < 3; i++) {
                byte rpart = (byte) initseed[i];
                byte lpart = (byte) (initseed[i] >>> 8);

                if ((rpart & 0x80) == 0x80) {
                    rpart = (byte) (rpart << 1);
                    rpart =+ 1;
                } else {
                    rpart = (byte) (rpart << 1);
                }

                if ((lpart & 0x80) == 0x80) {
                    lpart = (byte) (lpart << 1);
                    lpart += 1;
                } else {
                    lpart = (byte) (lpart << 1);
                }

                newseed[i] = (short) ((short) ((short) rpart & 0xFF) | (short) (((short) lpart) << 8));
            }
        }


        return newseed;
    }

}
