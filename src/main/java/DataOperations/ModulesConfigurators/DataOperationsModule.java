package DataOperations.ModulesConfigurators;

import DataOperations.ClientConnection;
import DataOperations.DataProvider;
import DataOperations.Interfaces.IDataParserToJSON;
import DataOperations.Interfaces.IMakeConnection;
import DataOperations.DataProcessor;
import com.google.inject.AbstractModule;

public class DataOperationsModule extends AbstractModule {
    @Override
    public void configure(){
        bind(DataProvider.class);
        bind(IMakeConnection.class).to(ClientConnection.class);
        bind(IDataParserToJSON.class).to(DataProcessor.class);
    }
}
