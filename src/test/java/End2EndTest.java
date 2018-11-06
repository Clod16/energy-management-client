import it.eng.jledgerclient.exception.JLedgerClientException;
import model.Data;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.ChaincodeEvent;
import org.hyperledger.fabric.sdk.ChaincodeEventListener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class End2EndTest {


    static EnergyManagementLedgerClient energyManagementLedgerClient;
    static ChaincodeEventListener chaincodeEventListener;


    @BeforeClass
    public static void begin() {
        try {
            chaincodeEventListener = new ChaincodeEventListener() {
                @Override
                public void received(String handle, BlockEvent blockEvent, ChaincodeEvent chaincodeEvent) {
                    String payload = new String(chaincodeEvent.getPayload());
                    System.out.println("Event from chaincode: " + chaincodeEvent.getEventName() + " " + payload);

                }
            };
            energyManagementLedgerClient = new EnergyManagementLedgerClient();

        } catch (JLedgerClientException e) {
            assertFalse(e.getMessage(), true);
        }
    }

    @AfterClass
    public static void end() {
        energyManagementLedgerClient = null;
    }

    @Test
    public void testPutGetData() throws JLedgerClientException {

        Data data = new Data();
        data.setPayload("Fatti non foste a viver come bruti ma per seguir virtute e canoscenza");
        data.setKey("A1");
        try {
            // energyManagementLedgerClient.doRegisterEvent("EVENT", chaincodeEventListener);
            energyManagementLedgerClient.putData(data);
            Data data1 = energyManagementLedgerClient.getData(data.getKey());
            //energyManagementLedgerClient.doRegisterEvent("EVENT", chaincodeEventListener);
            // System.out.println("Ledger Data: " + data1.getPayload());

        } catch (JLedgerClientException e) {
            e.printStackTrace();
        }


    }


}
