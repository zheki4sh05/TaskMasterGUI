package DataOperations.ModulesConfigurators;

import DataOperations.Interfaces.IDataModelSupplier;
import DataOperations.Interfaces.IDataProvider;
import Data.Settings.ISettingsSupplier;
import Data.Settings.Settings;
import com.example.taskmaster.Content;
import com.example.taskmaster.DataModel;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public class MainControllerModule extends AbstractModule {
    @Override
    public void configure(){
        bind(String.class).annotatedWith(Names.named("MAIN_PAGE")).toInstance(Content.MAIN_PAGE.location);
        bind(IDataModelSupplier.class).to(DataModel.class);
        bind(IDataProvider.class).to(DataModel.class);
        bind(DataModel.class).in(Singleton.class);
        bind(ISettingsSupplier.class).to(Settings.class);
        bind(Settings.class).in(Singleton.class);
    }


}
