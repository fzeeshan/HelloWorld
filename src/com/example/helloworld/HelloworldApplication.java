package com.example.helloworld;

import com.vaadin.Application;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import enterpriseapp.example.ui.Constants;
import enterpriseapp.example.ui.LoginWindow;

public class HelloworldApplication extends Application {
	public final Window mainWindow = new Window("Kontrah Vaadin");
	
	@Override
	public void init() {		
		setTheme("helloworldtheme");
		
		// show content according to the state of getUser()
		if(getUser() == null) {
			showPublicContent();
		} else {
			showPrivateContent();
		}
	}
	
	private void showPublicContent() {
		removeWindow(getMainWindow());
		
		// we are gonna create an empty window and add a new LoginWindow to it
		LoginWindow emptyMainWindow = new LoginWindow();
		Window mainWindow = new Window(Constants.uiAppName);
		
		mainWindow.addWindow(emptyMainWindow);
		setMainWindow(mainWindow);
	}
	
	private void showPrivateContent() {
        setMainWindow(mainWindow);
        mainWindow.getContent().setSizeFull();
        
        // Create application data instance
        AppData sessionData = new AppData(this);
        // Register it as a listener in the application context
        getContext().addTransactionListener(sessionData);
        
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setSpacing(false);
        mainLayout.setMargin(false);
        
        GridLayout topLayout = new GridLayout(3, 1);
        topLayout.setHeight("50px");
        topLayout.setWidth("100%");
        topLayout.addStyleName("blueGradient");
        
        Label appTitle = new Label("<span class='appTitle'><b>K</b>ontrah <b>V</b>aadin</span>");
        appTitle.setContentMode(Label.CONTENT_XHTML);
        //Label have default width = 100% so aligment won't work as espected
        appTitle.setWidth(null);
        topLayout.addComponent(appTitle, 1, 0);
        topLayout.setComponentAlignment(appTitle, Alignment.MIDDLE_CENTER);
        
        KontrahComposition kontrahComposition = new KontrahComposition();
        mainLayout.addComponent(topLayout);
        mainLayout.addComponent(kontrahComposition);
        mainLayout.setExpandRatio(kontrahComposition, 2);
        
        mainWindow.setContent(mainLayout);
        
        mainWindow.addComponent(AppData.getContextHelp());
        AppData.getContextHelp().setFollowFocus(true);
	}
}