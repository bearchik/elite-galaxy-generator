import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class StarTest {
    @Test
    public void createInhabitants() throws Exception {
        short[] seed = {(short)0xCD80, (short)0x98B8, (short)0x7A1D};
        Star star = new Star();
        star.initStar(seed);
    }


}