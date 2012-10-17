package com.example.helloworld;

import java.sql.SQLException;

import com.vaadin.data.Item;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class ComplexForm extends Window {
	public Form form = new Form();
	public Button btnSave = new Button();
	public Button btnCancel = new Button();
	public KontrahFieldFactory kontrahFieldFactory;
	
	public ComplexForm() {		
		final Window self = this;
		Panel p = new Panel();
		AppData.getContextHelp().addHelpForComponent(this, "Shortcuts:</br>close: <b>ESC</b></br>save: <b>ENTER</b>");
        final TabSheet tabs = new TabSheet();
        tabs.setSizeFull();
        tabs.setStyleName(Reindeer.TABSHEET_SMALL);        
        final FormLayout personalInfo = new FormLayout();
        tabs.addTab( personalInfo, "Personal data" );
        final FormLayout profile = new FormLayout();
        tabs.addTab( profile, "Company" );
        final FormLayout other = new FormLayout();
        tabs.addTab(other, "Other");
        
        kontrahFieldFactory = new KontrahFieldFactory();
        
        addListener(new FieldEvents.FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				AppData.getContextHelp().showHelpFor(self);
				form.getField("IMIE").focus();
			}
        	
        });
                
        form = new Form() {
            @Override
            protected void attachField(Object propertyId, Field field) {
                if ("IMIE".equals(propertyId) || "NAZWISKO".equals(propertyId)) {
                    personalInfo.addComponent( field );
                } else 
                if ("ID_FIRMA".equals(propertyId)) {
                	profile.addComponent(field);
                } else
                if ("WIEK".equals(propertyId) || "PESEL".equals(propertyId)) {
                	other.addComponent(field);
                }
            }
            
        	@Override
        	public void setItemDataSource(Item newDataSource) {
        		if (newDataSource != null) {
        			super.setItemDataSource(newDataSource);
        			if (newDataSource.getItemProperty("ID_FIRMA").getValue() != null) {
        				kontrahFieldFactory.cbFirmy.select(new RowId(new Object[] { newDataSource.getItemProperty("ID_FIRMA").getValue() }));
        			}
        		} else {
        			super.setItemDataSource(null);
        		}
        	}
        };
                
        form.setWriteThrough(false);
        form.setInvalidCommitted(false);
        form.setFormFieldFactory(kontrahFieldFactory);

        p.setContent(tabs);
        this.addComponent(p);
        
        btnSave.setCaption("Save");
        btnSave.setClickShortcut(KeyCode.ENTER, null);
        btnSave.setStyleName(Reindeer.BUTTON_SMALL);
        btnSave.addListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {	
				try {
					form.commit();
					
					try {
						AppData.getDataSource().getKontrahContainer().commit();
					} catch (UnsupportedOperationException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					((Window) getWindow().getParent()).removeWindow(getWindow());
				} catch (Exception e) {
				}
				
//				try {
//					Field fieldWiek = form.getField("WIEK");
//					Object wiekValue = fieldWiek.getValue();
//					Field fieldImie = form.getField("IMIE");
//					Object imieValue = fieldImie.getValue();
//					
//					if ((imieValue == null || imieValue.equals("")) && (wiekValue == null || wiekValue.equals(""))) {
//						 getWindow().showNotification(
//	                             (String) "BLAD",
//	                             (String) "Imie and Wiek can not be empty at the same time",
//	                             Notification.TYPE_ERROR_MESSAGE);
//					} else {
//						form.commit();
//						try {
//							AppData.getDataSource().getKontrahContainer().commit();
//						} catch (UnsupportedOperationException e) {
//							e.printStackTrace();
//						} catch (SQLException e) {
//							e.printStackTrace();
//						}
//						((Window) getWindow().getParent()).removeWindow(getWindow());
//					}
//				} catch (Exception e) {
//					getWindow().showNotification("Save error", e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
//				}
			}
		});
        
		btnCancel.setCaption("Cancel");
		btnCancel.setClickShortcut(KeyCode.ESCAPE, null);
		btnCancel.setStyleName(Reindeer.BUTTON_SMALL);
		btnCancel.addListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				form.discard();
				try {
					AppData.getDataSource().getKontrahContainer().rollback();
				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				((Window) getWindow().getParent()).removeWindow(getWindow());
			}
		});
		
		HorizontalLayout footer = new HorizontalLayout();
		footer.setSpacing(true);
		footer.addComponent(btnCancel);
		footer.addComponent(btnSave);
		this.addComponent(footer);
        
        this.setWidth("350px");
        this.setResizable(false);
        this.center();
        this.setCaption("Customer edit");
        this.setClosable(false);
        this.setModal(true);
	}
}
