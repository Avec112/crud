package io.avec.crud.views.helloworld;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.avec.crud.views.main.MainView;

@Route(value = "hello-world", layout = MainView.class)
@PageTitle("Hello World")
@JsModule("./src/views/helloworld/hello-world-view.js")
@Tag("hello-world-view")
public class HelloWorldView extends PolymerTemplate<TemplateModel> {

    @Id
    private TextField name;

    @Id
    private Button sayHello;

    public HelloWorldView() {
        setId("hello-world-view");
        sayHello.addClickListener( e-> {
            Notification.show("Hello " + name.getValue());
        });
    }
}
