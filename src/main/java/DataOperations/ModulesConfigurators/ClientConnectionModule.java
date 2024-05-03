package DataOperations.ModulesConfigurators;

import Data.UserInfo;
import Data.UserInfoSupplier;
import DataOperations.ClientConnection;
import DataOperations.DataTransfer;
import DataOperations.IDataTransfer;
import DataOperations.Interfaces.IAuthUser;
import com.example.taskmaster.Content;
import com.example.taskmaster.Controllers.MainController;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public class ClientConnectionModule extends AbstractModule {
    @Override
    public void configure(){
        bind(IAuthUser.class).to(ClientConnection.class);
        bind(UserInfoSupplier.class).to(UserInfo.class);
        bind(UserInfo.class).in(Singleton.class);
        bind(IDataTransfer.class).to(DataTransfer.class);
        bind(DataTransfer.class).in(Singleton.class);
    }
}
