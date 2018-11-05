import it.eng.jledgerclient.exception.JLedgerClientException;
import model.Data;

public interface LedgerClient {


    Data getData(String key) throws JLedgerClientException;
    void putData( Data data) throws JLedgerClientException;

}
