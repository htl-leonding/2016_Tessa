package at.htl.tessa.business;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 * Created by Stefanie on 18.05.16.
 */

@Startup
@Singleton
public class InitBean {

    @Inject
    RecipeLoader recipeLoader;

    @Inject
    CookingRecipeFacade recipeFacade;

    @PostConstruct
    public void init(){
        if(recipeFacade.countRecipes() <= 0) {
            recipeLoader.loadVorspeisen();
            recipeLoader.loadHauptspeisen();
            recipeLoader.loadNachspeisen();
            recipeLoader.loadDrinks();
        }
    }

}
