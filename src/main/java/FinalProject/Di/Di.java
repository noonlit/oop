package FinalProject.Di;

import FinalProject.Exception.InstanceNotFoundException;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.jetbrains.annotations.NotNull;

/**
 * Dependency injection enabler.
 */
public class Di {
    /**
     * Given a Class, will return an instance of that class with all its dependencies injected.
     *
     * To use this functionality, create a class under Module named {DesiredClass} and bind the needed dependencies.
     * (e.g. for a UserService, create Module/UserService.)
     *
     * The functionality relies on Guice.
     * For documentation, see https://github.com/google/guice/wiki.
     *
     * @param typeClass The class of the desired object.
     * @return The built object or null if the object does not have a corresponding Module class.
     */
    public static Object getInstance(@NotNull Class<?> typeClass) {
        String packageName = Di.class.getPackageName();
        String moduleName = packageName + ".Module." + typeClass.getSimpleName();

        AbstractModule module;
        try {
            module = (AbstractModule) Class.forName(moduleName).getConstructor().newInstance();
        } catch (Exception e) {
            throw new InstanceNotFoundException("Given class does not have a corresponding module class!");
        }

        Injector injector = Guice.createInjector(module);
        Object result = injector.getInstance(typeClass);

        if (result == null) {
            throw new InstanceNotFoundException("Cannot create instance for given class!");
        }

        return result;
    }
}
