import it.eng.jledgerclient.exception.JLedgerClientException;
import it.eng.jledgerclient.fabric.HLFLedgerClient;
import it.eng.jledgerclient.fabric.config.ConfigManager;
import it.eng.jledgerclient.fabric.config.Configuration;
import it.eng.jledgerclient.fabric.config.Organization;
import it.eng.jledgerclient.fabric.helper.LedgerInteractionHelper;
import model.Data;
import model.Function;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.ChaincodeEventListener;

import java.util.ArrayList;
import java.util.List;

final public class EnergyManagementLedgerClient extends HLFLedgerClient implements LedgerClient {

    private final static Logger log = LogManager.getLogger(EnergyManagementLedgerClient.class);

    public EnergyManagementLedgerClient() throws JLedgerClientException {
        doLedgerClient();
    }

    private void doLedgerClient() throws JLedgerClientException {
        try {
            configManager = ConfigManager.getInstance();
            Configuration configuration = configManager.getConfiguration();
            if (null == configuration || null == configuration.getOrganizations() || configuration.getOrganizations().isEmpty()) {
                log.error("Configuration missing!!! Check you config file!!!");
                throw new JLedgerClientException("Configuration missing!!! Check you config file!!!");
            }
            List<Organization> organizations = configuration.getOrganizations();
            if (null == organizations || organizations.isEmpty())
                throw new JLedgerClientException("Organizations missing!!! Check you config file!!!");
            //for (Organization org : organizations) {
            //FIXME multiple Organizations
            ledgerInteractionHelper = new LedgerInteractionHelper(configManager, organizations.get(0));
            //}
        } catch (Exception e) {
            log.error(e);
            throw new JLedgerClientException(e);
        }
    }

    @Override
    public String doRegisterEvent(String eventName, ChaincodeEventListener chaincodeEventListener) throws JLedgerClientException {
        return super.doRegisterEvent(eventName, chaincodeEventListener);
    }

    @Override
    public Data getData(String key) throws JLedgerClientException {
        if (key.isEmpty()) {
            throw new JLedgerClientException(Function.getData.name() + "is in error, No input data!");
        }
        List<String> args = new ArrayList<>();
        args.add(key);

        List<String> payloads = doQuery(Function.getData.toString(), args);
        Data data = new Data();
        data.setPayload(payloads.get(0));
        data.setKey(key);
        return data;
    }

    @Override
    public void putData(Data data) throws JLedgerClientException {

        if (data == null) {
            throw new JLedgerClientException((Function.putData.name() + "is in error, No input data!"));
        }
        List<String> stringArrayList = new ArrayList<>();
        stringArrayList.add(data.getKey());
        stringArrayList.add(data.getPayload());
        final String payload = doInvoke(Function.putData.toString(), stringArrayList);
        log.debug("Payload retrieved: " + payload);
    }
}
