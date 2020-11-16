package io.avec.crud.views.department;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.avec.crud.views.main.MainView;

@Route(value = "department", layout = MainView.class)
@PageTitle("Department")
@CssImport("./styles/views/department/department-view.css")
public class DepartmentView extends Div {

    public DepartmentView() {
        setId("department-view");
        add(new Label("Content placeholder"));
    }

}
